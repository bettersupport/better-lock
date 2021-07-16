package cn.better.lock.core.support;

import cn.better.lock.core.exception.GlobalLockException;

public interface LockInterface {

    void lock(String lockKey, long timeOut) throws GlobalLockException;

    boolean lockWithoutWait(String lockKey, long timeOut) throws GlobalLockException;

    void unlock(String lockKey) throws GlobalLockException;

    void unlockMost(String lockKey) throws GlobalLockException;

}
