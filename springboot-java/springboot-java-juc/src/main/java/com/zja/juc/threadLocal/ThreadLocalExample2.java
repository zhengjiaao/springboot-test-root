package com.zja.juc.threadLocal;

/**
 * 线程上下文管理
 *
 * @author: zhengja
 * @since: 2024/03/27 17:43
 */
public class ThreadLocalExample2 {

    public static class Main {
        public static void main(String[] args) {

            Thread thread1 = new Thread(new MyRunnable());
            Thread thread2 = new Thread(new MyRunnable());

            thread1.start();
            thread2.start();

            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ContextHolder.clearContext();
        }
    }

    public static class ContextHolder {
        private static final ThreadLocal<String> context = new ThreadLocal<>();

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
            ContextHolder.setContext("Thread Context"); // 设置上下文值

            String context = ContextHolder.getContext(); // 获取上下文值
            System.out.println("Thread: " + Thread.currentThread().getName() + ", Context: " + context);

            ContextHolder.clearContext(); // 清除上下文值
        }
    }


}
