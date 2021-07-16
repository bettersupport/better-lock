package cn.better.lock.core.support;

import cn.better.lock.core.properties.BetterLockProperties;

public class LockerFactory {

    public static LockInterface getLocker(String lockType, LockerConfig lockerConfig) {
        if (BetterLockProperties.LockType.REDIS_LOCK.equals(lockType)) {
            return new RedisLocker(lockerConfig.getRedisTemplate());
        } else if (BetterLockProperties.LockType.REDIS_CLUSTER_LOCK.equals(lockType)) {
            return new RedisLocker(lockerConfig.getRedisTemplate());
        } else if (BetterLockProperties.LockType.ZOOKEEPER_LOCK.equals(lockType)) {
            return new ZooKeeperLocker();
        } else {
            return null;
        }
    }

}
