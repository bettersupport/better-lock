package io.github.bettersupport.lock.core.support;

import io.github.bettersupport.lock.core.model.LockAttribute;

import java.util.Stack;

/**
 * @author wang.wencheng
 * @since 2022-1-15
 */
public class StackThreadLocalHandler {

    public static <T> void set(ThreadLocal<Stack<T>> threadLocal, T item) {
        if (threadLocal.get() == null) {
            threadLocal.set(new Stack<>());
        }
        threadLocal.get().push(item);
    }

    public static <T> void setTop(ThreadLocal<Stack<T>> threadLocal, T item) {
        if (threadLocal.get() == null) {
            threadLocal.set(new Stack<>());
        } else {
            threadLocal.get().pop();
            threadLocal.get().push(item);
        }
    }

    public static <T> T get(ThreadLocal<Stack<T>> threadLocal) {
        return threadLocal.get() != null ? threadLocal.get().peek() : null;
    }

    public static <T> T getAndRelease(ThreadLocal<Stack<T>> threadLocal) {
        if (threadLocal.get() == null) {
            return null;
        } else {
            if (threadLocal.get().empty()) {
                threadLocal.set(null);
                return null;
            } else {
                T item = threadLocal.get().pop();
                if (threadLocal.get().empty()) {
                    threadLocal.set(null);
                }
                return item;
            }
        }
    }

    public static <T> void release(ThreadLocal<Stack<T>> threadLocal) {
        if (threadLocal.get() != null) {
            if (threadLocal.get().empty()) {
                threadLocal.set(null);
            } else {
                threadLocal.get().pop();
            }
        }
    }

}
