package cn.better.lock.core.support;

import org.springframework.data.redis.core.StringRedisTemplate;

public class LockerConfig {

    private StringRedisTemplate redisTemplate;

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static LockerConfig build() {
        LockerConfig lockerConfig = new LockerConfig();
        return lockerConfig;
    }

    public LockerConfig buildRedisConfig(StringRedisTemplate redisTemplate) {
        this.setRedisTemplate(redisTemplate);
        return this;
    }
}
