package io.github.bettersupport.lock.core.support;

import io.github.bettersupport.lock.core.model.ZookeeperClient;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wang.wencheng
 * date 2021-7-13
 * describe
 */
public class LockerConfig {

    /**
     * springboot redis数据源
     */
    private StringRedisTemplate redisTemplate;

    /**
     * Redisson客户端
     */
    private RedissonClient redissonClient;

    /**
     * Zookeeper客户端
     */
    private ZookeeperClient zookeeperClient;

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

    public ZookeeperClient getZookeeperClient() {
        return zookeeperClient;
    }

    public void setZookeeperClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
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

    public LockerConfig buildZookeeperClient(ZookeeperClient zookeeperClient) {
        this.setZookeeperClient(zookeeperClient);
        return this;
    }
}
