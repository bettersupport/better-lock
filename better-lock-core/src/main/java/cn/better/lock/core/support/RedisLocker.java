package cn.better.lock.core.support;

import cn.better.lock.core.exception.GlobalLockException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLocker implements LockInterface{

    public static ThreadLocal<String> lockValueThreadLocal = new ThreadLocal<>();

    private StringRedisTemplate redisTemplate;

    public RedisLocker(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void lock(String lockKey, long timeOut) throws GlobalLockException {
        while (true) {
            String lockValue = String.valueOf(System.currentTimeMillis() + timeOut);
            lockValueThreadLocal.set(lockValue);
            if (lock(lockKey, lockValue, timeOut)) {
                break;
            }
        }
    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {
        try {
            String lockValue = lockValueThreadLocal.get();
            unlock(lockKey, lockValue);
        } catch (Exception e) {
            throw new GlobalLockException(e);
        }
    }

    @Override
    public void unlockMost(String lockKey) throws GlobalLockException {
        try {
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            if (!StringUtils.isEmpty(currentValue)) {
                redisTemplate.opsForValue().getOperations().delete(lockKey);//删除key
            }
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
