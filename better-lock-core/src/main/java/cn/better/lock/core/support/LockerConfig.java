package cn.better.lock.core.support;

import cn.better.lock.core.properties.BetterLockProperties;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

public class LockerConfig {

    private BetterLockProperties betterLockProperties;

    private StringRedisTemplate redisTemplate;

    private RedissonClient redissonClient;

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public BetterLockProperties getBetterLockProperties() {
        return betterLockProperties;
    }

    public void setBetterLockProperties(BetterLockProperties betterLockProperties) {
        this.betterLockProperties = betterLockProperties;
    }

    public static LockerConfig build() {
        LockerConfig lockerConfig = new LockerConfig();
        return lockerConfig;
    }

    public LockerConfig buildRedisConfig(StringRedisTemplate redisTemplate) {
        this.setRedisTemplate(redisTemplate);
        return this;
    }

    public LockerConfig buildRedisClusterConfig(RedissonClient redissonClient) {
        this.setRedissonClient(redissonClient);
        return this;
    }

    public LockerConfig buildProperties(BetterLockProperties properties) {
        this.setBetterLockProperties(properties);
        return this;
    }
}
