package cn.better.lock.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.better.lock")
public class BetterLockProperties {

    private String lockType = LockType.REDIS_LOCK;

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public static class LockType {
        public static final String REDIS_LOCK = "redis";
        public static final String REDIS_CLUSTER_LOCK = "redisCluster";
        public static final String ZOOKEEPER_LOCK = "zookeeper";
    }

}
