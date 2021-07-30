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
    public void lock(String lockKey, long leaseTime) throws GlobalLockException {
        try {
            RLock rlock = redissonClient.getLock(lockKey);
            rLockThreadLocal.set(rlock);
            rlock.lock(leaseTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }

    }

    @Override
    public boolean lockWithoutWait(String lockKey, long leaseTime) throws GlobalLockException {
        try {
            RLock rlock = redissonClient.getLock(lockKey);
            rLockThreadLocal.set(rlock);
            return rlock.tryLock(0, leaseTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {
        try {
            RLock rlock = rLockThreadLocal.get();
            rLockThreadLocal.set(null);
            if (rlock.isLocked() && rlock.isHeldByCurrentThread()) {
                try {
                    rlock.unlock();
                } catch (IllegalMonitorStateException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

}
