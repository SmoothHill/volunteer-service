package com.yueping.volunteer.cache;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class CheckInCodeCacheService {

    private static final Duration CODE_TTL = Duration.ofMinutes(30);

    private final StringRedisTemplate stringRedisTemplate;

    public CheckInCodeCacheService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void cacheCode(Long activityId, String code) {
        try {
            stringRedisTemplate.opsForValue().set(buildKey(activityId), code, CODE_TTL);
        } catch (DataAccessException ex) {
            // Redis 不可用时回退到数据库字段，避免影响主流程。
        }
    }

    public Optional<String> getCode(Long activityId) {
        try {
            return Optional.ofNullable(stringRedisTemplate.opsForValue().get(buildKey(activityId)));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    private String buildKey(Long activityId) {
        return "activity:checkin:code:" + activityId;
    }
}
