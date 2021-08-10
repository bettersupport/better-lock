package io.github.bettersupport.lock.core.support;

import io.github.bettersupport.lock.core.exception.GlobalLockException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Springboot redis锁
 * @author wang.wencheng
 * date 2021-7-13
 * describe
 */
public class RedisLocker implements LockInterface{

    private static ThreadLocal<String> lockValueThreadLocal = new ThreadLocal<>();

    private StringRedisTemplate redisTemplate;

    public RedisLocker(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void lock(String lockKey, long leaseTime) throws GlobalLockException {
        try {
            while (true) {
                String lockValue = String.valueOf(System.currentTimeMillis() + leaseTime);
                lockValueThreadLocal.set(lockValue);
                if (lock(lockKey, lockValue, leaseTime)) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public boolean lockWithoutWait(String lockKey, long leaseTime) throws GlobalLockException {
        try {
            String lockValue = String.valueOf(System.currentTimeMillis() + leaseTime);
            lockValueThreadLocal.set(lockValue);
            return lock(lockKey, lockValue, leaseTime);
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {
        try {
            String lockValue = lockValueThreadLocal.get();
            unlock(lockKey, lockValue);
            // 释放ThreadLocal
            lockValueThreadLocal.set(null);
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    private boolean lock(String key, String value, long expire) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.MILLISECONDS)) {
            return true;
        }

        String currentValue = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
                return true;
            }
        }
        return false;
    }

    private void unlock(String key, String value) {
        String currentValue = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
            redisTemplate.opsForValue().getOperations().delete(key);
        }
    }
}
