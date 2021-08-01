package cn.better.lock.core.exception;

/**
 * 分布式锁异常
 * @author wang.wencheng
 * @date 2021-7-11
 * @remark
 */
public class GlobalLockException extends Exception{

    public GlobalLockException() {
    }

    public GlobalLockException(String message) {
        super(message);
    }

    public GlobalLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalLockException(Throwable cause) {
        super(cause);
    }

    public GlobalLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
