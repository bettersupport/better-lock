package cn.better.lock.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

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
        private List<String> nodes;

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }

}
