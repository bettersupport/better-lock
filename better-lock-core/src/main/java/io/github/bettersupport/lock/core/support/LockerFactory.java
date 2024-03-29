package io.github.bettersupport.lock.core.support;

import io.github.bettersupport.lock.core.properties.BetterLockProperties;

/**
 * 锁工厂
 * @author wang.wencheng
 * date 2021-7-13
 * describe
 */
public class LockerFactory {

    public static LockInterface getLocker(String lockType, LockerConfig lockerConfig) {
        if (BetterLockProperties.LockType.REDIS_LOCK.equals(lockType)) {
            return new RedisLocker(lockerConfig.getRedisTemplate());
        } else if (BetterLockProperties.LockType.REDISSON_LOCK.equals(lockType)) {
            return new RedisCLusterLocker(lockerConfig.getRedissonClient());
        } else if (BetterLockProperties.LockType.ZOOKEEPER_LOCK.equals(lockType)) {
            return new ZooKeeperLocker(lockerConfig.getZookeeperClient());
        } else {
            return null;
        }
    }

}
