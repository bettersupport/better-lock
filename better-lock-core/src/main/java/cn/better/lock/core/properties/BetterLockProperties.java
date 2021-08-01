package cn.better.lock.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.better.lock")
public class BetterLockProperties {

    private String lockType = LockType.REDIS_LOCK;

    private Zookeeper zookeeper;

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Zookeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public static class LockType {
        public static final String REDIS_LOCK = "redis";
        public static final String REDIS_CLUSTER_LOCK = "redisCluster";
        public static final String ZOOKEEPER_LOCK = "zookeeper";
    }

    public static class Zookeeper {
        private String nodes;

        public String getNodes() {
            return nodes;
        }

        public void setNodes(String nodes) {
            this.nodes = nodes;
        }
    }

}
