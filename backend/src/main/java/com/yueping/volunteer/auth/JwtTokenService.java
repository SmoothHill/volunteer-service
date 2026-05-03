package com.yueping.volunteer.auth;

import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtTokenService {

    private final JwtProperties jwtProperties;

    public JwtTokenService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(UserProfile userProfile) {
        String secret = resolveSecret();
        Instant now = Instant.now();
        Instant expireAt = now.plus(jwtProperties.getExpireHours(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setSubject(String.valueOf(userProfile.getId()))
                .claim("openId", userProfile.getOpenId())
                .claim("name", userProfile.getName())
                .claim("role", userProfile.getRole() == null ? "" : userProfile.getRole().name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expireAt))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public AuthenticatedUser parseToken(String token) {
        String secret = resolveSecret();
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        Long userId = Long.valueOf(claims.getSubject());
        String openId = claims.get("openId", String.class);
        String name = claims.get("name", String.class);
        String roleValue = claims.get("role", String.class);
        UserRole role = StringUtils.hasText(roleValue) ? UserRole.valueOf(roleValue) : null;
        return new AuthenticatedUser(userId, openId, name, role);
    }

    private String resolveSecret() {
        if (StringUtils.hasText(jwtProperties.getSecret())) {
            return jwtProperties.getSecret().trim();
        }
        throw new IllegalStateException("Missing APP_JWT_SECRET");
    }
}
