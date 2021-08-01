package cn.better.lock.core.configuration;

import cn.better.lock.core.properties.BetterLockProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson的配置类
 * @author wang.wencheng
 * @date 2021-7-18
 * @remark
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "spring.better.lock", name = "lock-type", havingValue = BetterLockProperties.LockType.REDISSON_LOCK)
public class RedissonAutoConfiguration {

    private static final String REDIS_SSL_PREFIX = "rediss://";
    private static final String REDIS_PREFIX = "redis://";

    @Autowired
    private RedisProperties redisProperties;

    /**
     * redis单机 RedissonClient
     * @return RedissonClient
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.redis", name = "host")
    public RedissonClient redissonSingle() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        config.useSingleServer().setDatabase(redisProperties.getDatabase());
        config.useSingleServer().setPassword(redisProperties.getPassword());
        config.useSingleServer().setConnectionMinimumIdleSize(redisProperties.getJedis().getPool().getMinIdle());
        String redisPrefix = redisProperties.isSsl() ? REDIS_SSL_PREFIX : REDIS_PREFIX;
        config.useSingleServer().setAddress(redisPrefix + redisProperties.getHost() + ":" + redisProperties.getPort());
        return Redisson.create(config);
    }

    /**
     * redis集群 RedissonClient
     * @return RedissonClient
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.redis.cluster", name = "nodes")
    public RedissonClient redissonCluster() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        config.useClusterServers().setPassword(redisProperties.getPassword());
        String redisPrefix = redisProperties.isSsl() ? REDIS_SSL_PREFIX : REDIS_PREFIX;
        for (String node : redisProperties.getCluster().getNodes()) {
            config.useClusterServers().addNodeAddress(redisPrefix + node);
        }
        return Redisson.create(config);
    }

    /**
     * redis哨兵模式 RedissonClient
     * @return RedissonClient
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.redis.sentinel", name = "nodes")
    public RedissonClient redissonSentinel() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        config.useSentinelServers().setDatabase(redisProperties.getDatabase());
        config.useSentinelServers().setPassword(redisProperties.getPassword());
        config.useSentinelServers().setMasterName(redisProperties.getSentinel().getMaster());
        config.useSentinelServers().setSentinelPassword(redisProperties.getSentinel().getPassword());
        String redisPrefix = redisProperties.isSsl() ? REDIS_SSL_PREFIX : REDIS_PREFIX;
        for (String node : redisProperties.getSentinel().getNodes()) {
            config.useSentinelServers().addSentinelAddress(redisPrefix + node);
        }
        return Redisson.create(config);
    }

}
