package com.zja.juc.threadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zhengja
 * @since: 2024/03/27 17:28
 */
public class InheritableThreadLocalExample2 {

    public static class Main {
        public static void main(String[] args) {
            ContextHolder.setContext("Parent Context");

            ExecutorService executorService = Executors.newFixedThreadPool(2);

            executorService.submit(new MyRunnable());
            executorService.submit(new MyRunnable());

            executorService.shutdown();

            // 输出：
            // Thread: pool-1-thread-1, Context: Parent Context
            // Thread: pool-1-thread-2, Context: Parent Context
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
