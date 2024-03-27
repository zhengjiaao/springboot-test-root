package com.zja.juc.threadLocal;

/**
 * InheritableThreadLocal 是 ThreadLocal 的一个子类，它具有与 ThreadLocal 类似的功能，但具有一项额外的特性：它允许子线程继承父线程的线程局部变量。
 * 作用：子线程可以访问父线程设置的线程局部变量的值
 * <p/>
 * 例如:将上下文信息传递给子线程
 *
 * @author: zhengja
 * @since: 2024/03/27 17:21
 */
public class InheritableThreadLocalExample {

    public static class Main {
        public static void main(String[] args) {
            ContextHolder.setContext("Parent Context"); // 上下文传递给子线程,子线程可以访问父线程设置的线程局部变量的值

            Thread parentThread = new Thread(new MyRunnable());
            parentThread.start();

            Thread childThread = new Thread(parentThread, "Thread-1");
            childThread.start();

            // 输出:
            // Thread: Thread-1, Context: Parent Context
            // Thread: Thread-0, Context: Parent Context

            // 可以看到，父线程和子线程都能够获取到相同的上下文信息 "Parent Context"
        }
    }

    public static class ContextHolder {
        private static final InheritableThreadLocal<String> context = new InheritableThreadLocal<>();

        public static void setContext(String value) {
            context.set(value);
        }

        public static String getContext() {
            return context.get();
        }

        public static void clearContext() {
            context.remove();
        }
    }

    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            String context = ContextHolder.getContext();
            System.out.println("Thread: " + Thread.currentThread().getName() + ", Context: " + context);
        }
    }


}
