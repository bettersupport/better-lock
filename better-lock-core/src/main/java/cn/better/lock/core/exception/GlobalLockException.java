package cn.better.lock.core.exception;

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
