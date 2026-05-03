package com.yueping.volunteer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yueping.volunteer.dto.MapLocationView;
import com.yueping.volunteer.properties.TencentMapProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TencentMapSearchService {

    private static final Logger log = LoggerFactory.getLogger(TencentMapSearchService.class);

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;
    private static final long CACHE_TTL_MILLIS = 5 * 60 * 1000L;
    private static final double DEFAULT_LATITUDE = 30.5728D;
    private static final double DEFAULT_LONGITUDE = 104.0668D;

    private final TencentMapProperties tencentMapProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public TencentMapSearchService(TencentMapProperties tencentMapProperties) {
        this.tencentMapProperties = tencentMapProperties;
    }

    public List<MapLocationView> searchLocations(String keyword, Double latitude, Double longitude) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        if (!StringUtils.hasText(normalizedKeyword)) {
            throw new IllegalArgumentException("请输入地点关键词");
        }
        if (!StringUtils.hasText(tencentMapProperties.getWebServiceKey())) {
            throw new IllegalStateException("腾讯地图 WebService Key 未配置");
        }

        double centerLatitude = latitude == null ? DEFAULT_LATITUDE : latitude;
        double centerLongitude = longitude == null ? DEFAULT_LONGITUDE : longitude;
        String cacheKey = normalizedKeyword + "|" + roundCoordinate(centerLatitude) + "|" + roundCoordinate(centerLongitude);
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.expiresAt >= System.currentTimeMillis()) {
            return cached.locations;
        }

        IllegalArgumentException lastNonQuotaError = null;
        IllegalArgumentException firstQuotaError = null;

        SearchAttempt nearbyAttempt = runSearchAttempt("/ws/place/v1/search", () -> searchByPlace(normalizedKeyword, centerLatitude, centerLongitude));
        if (!nearbyAttempt.locations.isEmpty()) {
            return cacheAndReturn(cacheKey, nearbyAttempt.locations);
        }
        if (nearbyAttempt.quotaExceeded) {
            firstQuotaError = firstNonNull(firstQuotaError, nearbyAttempt.error);
        } else {
            lastNonQuotaError = firstNonNull(lastNonQuotaError, nearbyAttempt.error);
        }

        SearchAttempt regionAttempt = runSearchAttempt("/ws/place/v1/search(region)", () -> searchByRegion(normalizedKeyword));
        if (!regionAttempt.locations.isEmpty()) {
            return cacheAndReturn(cacheKey, regionAttempt.locations);
        }
        if (regionAttempt.quotaExceeded) {
            firstQuotaError = firstNonNull(firstQuotaError, regionAttempt.error);
        } else {
            lastNonQuotaError = firstNonNull(lastNonQuotaError, regionAttempt.error);
        }

        SearchAttempt geocoderAttempt = runSearchAttempt("/ws/geocoder/v1", () -> searchByGeocoder(normalizedKeyword));
        if (!geocoderAttempt.locations.isEmpty()) {
            return cacheAndReturn(cacheKey, geocoderAttempt.locations);
        }
        if (geocoderAttempt.quotaExceeded) {
            firstQuotaError = firstNonNull(firstQuotaError, geocoderAttempt.error);
        } else {
            lastNonQuotaError = firstNonNull(lastNonQuotaError, geocoderAttempt.error);
        }

        SearchAttempt suggestionAttempt = runSearchAttempt("/ws/place/v1/suggestion", () -> searchBySuggestion(normalizedKeyword, centerLatitude, centerLongitude));
        if (!suggestionAttempt.locations.isEmpty()) {
            return cacheAndReturn(cacheKey, suggestionAttempt.locations);
        }
        if (suggestionAttempt.quotaExceeded) {
            firstQuotaError = firstNonNull(firstQuotaError, suggestionAttempt.error);
        } else {
            lastNonQuotaError = firstNonNull(lastNonQuotaError, suggestionAttempt.error);
        }

        if (lastNonQuotaError != null) {
            throw lastNonQuotaError;
        }
        if (firstQuotaError != null) {
            throw firstQuotaError;
        }

        return cacheAndReturn(cacheKey, Collections.emptyList());
    }

    private List<MapLocationView> searchBySuggestion(String keyword, double latitude, double longitude) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("keyword", keyword);
        params.put("region_fix", "0");
        params.put("location", latitude + "," + longitude);
        JsonNode payload = request("/ws/place/v1/suggestion", params);
        return extractLocations(payload.path("data"), "suggestion");
    }

    private List<MapLocationView> searchByPlace(String keyword, double latitude, double longitude) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("keyword", keyword);
        params.put("boundary", "nearby(" + latitude + "," + longitude + ",50000,1)");
        params.put("page_size", "10");
        JsonNode payload = request("/ws/place/v1/search", params);
        return extractLocations(payload.path("data"), "search");
    }

    private List<MapLocationView> searchByRegion(String keyword) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("keyword", keyword);
        params.put("boundary", "region(成都市,1)");
        params.put("page_size", "10");
        JsonNode payload = request("/ws/place/v1/search", params);
        return extractLocations(payload.path("data"), "region");
    }

    private List<MapLocationView> searchByGeocoder(String keyword) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("address", keyword);
        JsonNode payload = request("/ws/geocoder/v1", params);
        JsonNode result = payload.path("result");
        JsonNode location = result.path("location");
        if (!location.hasNonNull("lat") || !location.hasNonNull("lng")) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new MapLocationView(
                "geocoder-0",
                keyword,
                result.path("address").asText(keyword),
                location.path("lat").asDouble(),
                location.path("lng").asDouble()
        ));
    }

    private JsonNode request(String path, Map<String, String> params) {
        HttpURLConnection connection = null;
        try {
            Map<String, String> requestParams = new TreeMap<>();
            requestParams.put("key", tencentMapProperties.getWebServiceKey());
            requestParams.put("output", "json");
            requestParams.putAll(params);

            String signature = buildSignature(path, requestParams);
            if (StringUtils.hasText(signature)) {
                requestParams.put("sig", signature);
            }

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tencentMapProperties.getApiBaseUrl() + path);
            requestParams.forEach(builder::queryParam);

            connection = (HttpURLConnection) new URL(builder.toUriString()).openConnection(Proxy.NO_PROXY);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestProperty("Accept", "application/json");

            JsonNode payload = objectMapper.readTree(
                    connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream()
            );
            int status = payload.path("status").asInt(-1);
            if (status != 0) {
                String message = payload.path("message").asText();
                log.warn("Tencent map request failed: path={}, status={}, message={}, params={}",
                        path, status, message, requestParams);
                throw new IllegalArgumentException(StringUtils.hasText(message) ? message : "地点搜索失败");
            }
            return payload;
        } catch (IOException ex) {
            log.warn("Tencent map request exception: path={}, params={}, error={}", path, params, ex.getMessage(), ex);
            throw new IllegalArgumentException("地点搜索失败，请稍后重试");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String buildSignature(String path, Map<String, String> params) {
        String secretKey = tencentMapProperties.getWebServiceSecretKey();
        if (!StringUtils.hasText(secretKey)) {
            return "";
        }
        StringBuilder queryBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                queryBuilder.append("&");
            }
            queryBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        return md5(path + "?" + queryBuilder + secretKey);
    }

    private String md5(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte value : digest) {
                builder.append(String.format("%02x", value));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("MD5 算法不可用", ex);
        }
    }

    private List<MapLocationView> extractLocations(JsonNode items, String prefix) {
        if (items == null || !items.isArray()) {
            return Collections.emptyList();
        }
        List<MapLocationView> locations = new ArrayList<>();
        for (int index = 0; index < items.size(); index++) {
            JsonNode item = items.get(index);
            JsonNode location = item.path("location");
            JsonNode latitudeNode = item.hasNonNull("latitude") ? item.path("latitude") : location.path("lat");
            JsonNode longitudeNode = item.hasNonNull("longitude") ? item.path("longitude") : location.path("lng");
            if (!latitudeNode.isNumber() || !longitudeNode.isNumber()) {
                continue;
            }
            String title = firstNonBlank(item.path("title").asText(), item.path("name").asText(), item.path("address").asText());
            String address = firstNonBlank(item.path("address").asText(), item.path("addr").asText(), title);
            locations.add(new MapLocationView(
                    prefix + "-" + index,
                    StringUtils.hasText(title) ? title : "未命名地点",
                    StringUtils.hasText(address) ? address : "暂无地址信息",
                    latitudeNode.asDouble(),
                    longitudeNode.asDouble()
            ));
        }
        return locations;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return "";
    }

    private String roundCoordinate(double value) {
        return String.format("%.4f", value);
    }

    private List<MapLocationView> cacheAndReturn(String cacheKey, List<MapLocationView> locations) {
        cache.put(cacheKey, new CacheEntry(locations, System.currentTimeMillis() + CACHE_TTL_MILLIS));
        return locations;
    }

    private SearchAttempt runSearchAttempt(String path, SearchSupplier supplier) {
        try {
            return new SearchAttempt(supplier.get(), null, false);
        } catch (IllegalArgumentException ex) {
            boolean quota = isQuotaExceededMessage(ex.getMessage());
            if (quota) {
                log.warn("Tencent map request limited: path={}, message={}", path, ex.getMessage());
            }
            return new SearchAttempt(Collections.emptyList(), ex, quota);
        }
    }

    private boolean isQuotaExceededMessage(String message) {
        if (!StringUtils.hasText(message)) {
            return false;
        }
        String normalized = message.toLowerCase();
        return normalized.contains("每日调用量")
                || normalized.contains("达到上限")
                || normalized.contains("quota")
                || normalized.contains("qps")
                || normalized.contains("频次");
    }

    private IllegalArgumentException firstNonNull(IllegalArgumentException current, IllegalArgumentException candidate) {
        return current != null ? current : candidate;
    }

    @FunctionalInterface
    private interface SearchSupplier {
        List<MapLocationView> get();
    }

    private static class SearchAttempt {
        private final List<MapLocationView> locations;
        private final IllegalArgumentException error;
        private final boolean quotaExceeded;

        private SearchAttempt(List<MapLocationView> locations, IllegalArgumentException error, boolean quotaExceeded) {
            this.locations = locations;
            this.error = error;
            this.quotaExceeded = quotaExceeded;
        }
    }

    private static class CacheEntry {
        private final List<MapLocationView> locations;
        private final long expiresAt;

        private CacheEntry(List<MapLocationView> locations, long expiresAt) {
            this.locations = locations;
            this.expiresAt = expiresAt;
        }
    }
}
