package cn.better.lock.core.support;

import cn.better.lock.core.exception.GlobalLockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class RedisCLusterLocker implements LockInterface{

    private static ThreadLocal<RLock> rLockThreadLocal = new ThreadLocal<>();

    private RedissonClient redissonClient;

    public RedisCLusterLocker(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void lock(String lockKey, long timeOut) throws GlobalLockException {
        redissonClient.getLock("test");
    }

    @Override
    public boolean lockWithoutWait(String lockKey, long timeOut) throws GlobalLockException {
        return true;
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {

    }

    @Override
    public void unlockMost(String lockKey) throws GlobalLockException {

    }
}
