package cn.better.lock.core.support;

import cn.better.lock.core.exception.GlobalLockException;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisLocker implements LockInterface{

    private StringRedisTemplate redisTemplate;

    public RedisLocker(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void lock(String lockKey, long timeOut) throws GlobalLockException {

    }

    @Override
    public void unlock(String lockKey) throws GlobalLockException {

    }

    @Override
    public void unlockMost(String lockKey) throws GlobalLockException {

    }
}
