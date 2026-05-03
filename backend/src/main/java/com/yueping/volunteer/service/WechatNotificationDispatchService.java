package com.yueping.volunteer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yueping.volunteer.model.NotificationChannel;
import com.yueping.volunteer.model.NotificationSendStatus;
import com.yueping.volunteer.model.NotificationType;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.properties.WechatProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WechatNotificationDispatchService {

    private static final Logger log = LoggerFactory.getLogger(WechatNotificationDispatchService.class);

    private final WechatProperties wechatProperties;
    private final NotificationTemplateResolver templateResolver;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private volatile String cachedAccessToken;
    private volatile LocalDateTime accessTokenExpireAt;

    public WechatNotificationDispatchService(WechatProperties wechatProperties,
                                            NotificationTemplateResolver templateResolver) {
        this.wechatProperties = wechatProperties;
        this.templateResolver = templateResolver;
    }

    public boolean supports(NotificationType notificationType) {
        return templateResolver.supportsWechat(notificationType);
    }

    public NotificationDispatchResult dispatch(UserProfile user,
                                               NotificationType notificationType,
                                               String title,
                                               String content,
                                               String templateId) {
        NotificationDispatchResult result = new NotificationDispatchResult();
        result.setChannel(NotificationChannel.WECHAT_SUBSCRIBE);
        result.setSendStatus(NotificationSendStatus.FAILED);
        result.setFailureReason("未完成微信模板字段映射配置");
        if (user == null || !StringUtils.hasText(user.getOpenId())) {
            result.setFailureReason("当前用户缺少 openId，无法发送微信订阅消息");
            return result;
        }

        Optional<Map<String, Object>> payload = resolveTemplateData(notificationType, title, content);
        if (!payload.isPresent()) {
            return result;
        }

        try {
            String accessToken = getAccessToken();
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("touser", user.getOpenId());
            requestBody.put("template_id", templateId);
            requestBody.put("page", templateResolver.resolvePagePath(notificationType));
            requestBody.put("data", payload.get());
            JsonNode response = postJson(
                    wechatProperties.getSubscribeMessageSendUrl().replace("{accessToken}", accessToken),
                    objectMapper.writeValueAsString(requestBody)
            );
            int errorCode = response.path("errcode").asInt(-1);
            if (errorCode == 0) {
                result.setSendStatus(NotificationSendStatus.SENT);
                result.setSentAt(LocalDateTime.now());
                result.setFailureReason("");
                result.setProviderMessageId(response.path("msgid").asText(""));
                return result;
            }
            result.setFailureReason(response.path("errmsg").asText("微信订阅消息发送失败"));
            return result;
        } catch (Exception ex) {
            log.warn("Wechat subscribe notification failed: type={}, userId={}, reason={}",
                    notificationType,
                    user.getId(),
                    ex.getMessage());
            result.setFailureReason(ex.getMessage());
            return result;
        }
    }

    private Optional<Map<String, Object>> resolveTemplateData(NotificationType notificationType,
                                                              String title,
                                                              String content) {
        List<String> keys = templateResolver.resolveTemplateDataKeys(notificationType);
        if (keys.isEmpty()) {
            return Optional.empty();
        }
        List<String> values = buildTemplateValues(notificationType, title, content);
        Map<String, Object> data = new LinkedHashMap<>();
        for (int index = 0; index < keys.size(); index += 1) {
            String key = keys.get(index);
            if (!StringUtils.hasText(key)) {
                continue;
            }
            String value = index < values.size() ? values.get(index) : "";
            Map<String, String> node = new LinkedHashMap<>();
            node.put("value", value);
            data.put(key, node);
        }
        return data.isEmpty() ? Optional.empty() : Optional.of(data);
    }

    private List<String> buildTemplateValues(NotificationType notificationType, String title, String content) {
        List<String> values = new ArrayList<>();
        values.add(limit(title, 20));
        values.add(limit(content, 50));
        values.add(LocalDateTime.now().toString().replace('T', ' '));
        values.add(notificationType.name());
        return values;
    }

    private String getAccessToken() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        if (StringUtils.hasText(cachedAccessToken) && accessTokenExpireAt != null && accessTokenExpireAt.isAfter(now.plusMinutes(1))) {
            return cachedAccessToken;
        }
        String url = UriComponentsBuilder.fromHttpUrl(wechatProperties.getAccessTokenUrl())
                .queryParam("grant_type", "client_credential")
                .queryParam("appid", wechatProperties.getAppId())
                .queryParam("secret", wechatProperties.getAppSecret())
                .toUriString();
        JsonNode response = getJson(url);
        if (!response.hasNonNull("access_token")) {
            throw new IllegalArgumentException(response.path("errmsg").asText("获取微信 access_token 失败"));
        }
        cachedAccessToken = response.path("access_token").asText();
        int expiresIn = response.path("expires_in").asInt(7200);
        accessTokenExpireAt = now.plusSeconds(Math.max(300, expiresIn - 300));
        return cachedAccessToken;
    }

    private JsonNode getJson(String url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection(Proxy.NO_PROXY);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Accept", "application/json");
            if (connection.getResponseCode() >= 400) {
                return objectMapper.readTree(connection.getErrorStream());
            }
            return objectMapper.readTree(connection.getInputStream());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private JsonNode postJson(String url, String body) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection(Proxy.NO_PROXY);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
            }
            if (connection.getResponseCode() >= 400) {
                return objectMapper.readTree(connection.getErrorStream());
            }
            return objectMapper.readTree(connection.getInputStream());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String limit(String value, int maxLength) {
        String text = StringUtils.hasText(value) ? value.trim() : "";
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }
}
