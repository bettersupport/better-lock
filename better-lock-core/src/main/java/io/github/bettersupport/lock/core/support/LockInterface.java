package io.github.bettersupport.lock.core.support;

import io.github.bettersupport.lock.core.exception.GlobalLockException;

/**
 * 锁接口
 * @author wang.wencheng
 * date 2021-7-13
 * describe
 */
public interface LockInterface {

    String lockKeyPrefix = "betterLock";
    String lockKeyColon = ":";

    /**
     * 等待锁
     * @param lockKey 锁KEY
     * @param leaseTime 超时时间
     * @throws GlobalLockException
     */
    void lock(String lockKey, long leaseTime) throws GlobalLockException;

    /**
     * 非等待锁
     * @param lockKey 锁KEY
     * @param leaseTime 超时时间
     * @return
     * @throws GlobalLockException
     */
    boolean lockWithoutWait(String lockKey, long leaseTime) throws GlobalLockException;

    /**
     * 解锁
     * @param lockKey 锁KEY
     * @throws GlobalLockException
     */
    void unlock(String lockKey) throws GlobalLockException;

}
