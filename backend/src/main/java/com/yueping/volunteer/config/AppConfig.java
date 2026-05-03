package com.yueping.volunteer.config;

import com.yueping.volunteer.properties.JwtProperties;
import com.yueping.volunteer.properties.LoginVerificationProperties;
import com.yueping.volunteer.properties.LocalTestLoginProperties;
import com.yueping.volunteer.properties.NotificationProperties;
import com.yueping.volunteer.properties.SensitiveDataProperties;
import com.yueping.volunteer.properties.StorageProperties;
import com.yueping.volunteer.properties.SuperAdminWebProperties;
import com.yueping.volunteer.properties.TencentMapProperties;
import com.yueping.volunteer.properties.WechatProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        JwtProperties.class,
        LoginVerificationProperties.class,
        LocalTestLoginProperties.class,
        NotificationProperties.class,
        SensitiveDataProperties.class,
        StorageProperties.class,
        TencentMapProperties.class,
        WechatProperties.class,
        SuperAdminWebProperties.class
})
public class AppConfig {
}
