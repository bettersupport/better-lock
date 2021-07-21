package cn.better.lock.core.support;

import cn.better.lock.core.exception.GlobalLockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedisCLusterLocker implements LockInterface{

    private static ThreadLocal<RLock> rLockThreadLocal = new ThreadLocal<>();

    private RedissonClient redissonClient;

    public RedisCLusterLocker(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void lock(String lockKey, long timeOut) throws GlobalLockException {
        RLock rlock = redissonClient.getLock(lockKey);
        rLockThreadLocal.set(rlock);
        rlock.lock(timeOut, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean lockWithoutWait(String lockKey, long timeOut) throws GlobalLockException {
        RLock rlock = redissonClient.getLock(lockKey);
        rLockThreadLocal.set(rlock);
        return rlock.tryLock();
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {
        RLock rlock = rLockThreadLocal.get();
        if (rlock.isLocked() && rlock.isHeldByCurrentThread()) {
            rlock.unlock();
        }
    }

    @Override
    public void unlockMost(String lockKey) throws GlobalLockException {

    }
}
