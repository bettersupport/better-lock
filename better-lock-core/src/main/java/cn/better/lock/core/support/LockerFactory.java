package cn.better.lock.core.support;

import cn.better.lock.core.properties.BetterLockProperties;

/**
 * 锁工厂
 * @author wang.wencheng
 * @date 2021-7-13
 * @remark
 */
public class LockerFactory {

    public static LockInterface getLocker(String lockType, LockerConfig lockerConfig) {
        if (BetterLockProperties.LockType.REDIS_LOCK.equals(lockType)) {
            return new RedisLocker(lockerConfig.getRedisTemplate());
        } else if (BetterLockProperties.LockType.REDIS_CLUSTER_LOCK.equals(lockType)) {
            return new RedisCLusterLocker(lockerConfig.getRedissonClient());
        } else if (BetterLockProperties.LockType.ZOOKEEPER_LOCK.equals(lockType)) {
            return new ZooKeeperLocker(lockerConfig.getZookeeperClient());
        } else {
            return null;
        }
    }

}
