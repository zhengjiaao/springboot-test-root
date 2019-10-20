package com.dist.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池管理
 * @author weifj
 *
 */
public class DistThreadManager {

	/**
	 * 核心线程数
	 */
	private static int CORE_POOL_SIZE = 5;
	/**
	 * 线程池最大线程数
	 */
	private static int MAX_POOL_SIZE = 100;
	/**
	 * 额外线程空状态生存时间
	 */
	private static int KEEP_ALIVE_TIME = 30 * 1000;
	/**
	 * 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
	 */
	private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(CORE_POOL_SIZE);
	private static ThreadFactory factory = new ThreadFactory() {
		private final AtomicInteger integer = new AtomicInteger();

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "ThreadPool thread: " + integer.getAndIncrement());
		}
	};
	/**
	 * 缓存线程池对象
	 */
	public static ExecutorService MyCacheThreadPool  = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
			workQueue, factory);
}
