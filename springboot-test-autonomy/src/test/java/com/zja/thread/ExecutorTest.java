package com.zja.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池机制
 *
 * @author zhengja@dist.com.cn
 * @data 2019/5/6 13:12
 */
public class ExecutorTest {

    /**
     * 1. newFixedThreadPool创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程.
     *
     * 运行结果：总共只会创建5个线程， 开始执行五个线程，当五个线程都处于活动状态，
     * 再次提交的任务都会加入队列等到其他线程运行结束，当线程处于空闲状态时会被下一个任务复用
     */
    @Test
    public void newFixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 20; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程名称：" + Thread.currentThread().getName());
                }
            };
            executorService.execute(syncRunnable);
        }
    }

    /**
     * 2. newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程
     * <p>
     * 运行结果：可以看出缓存线程池大小是不定值，可以需要创建不同数量的线程，在使用缓存型池时，先查看池中有没有以前创建的线程，
     * 如果有，就复用.如果没有，就新建新的线程加入池中，缓存型池子通常用于执行一些生存期很短的异步型任务
     */
    @Test
    public void newCachedThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程名称：" + Thread.currentThread().getName());
                }
            };
            executorService.execute(syncRunnable);
        }
        Thread.yield();
    }

    /**
     * 3. newScheduledThreadPool创建一个定长线程池，支持定时及周期性任务执行
     *schedule(Runnable command,long delay, TimeUnit unit)创建并执行在给定延迟后启用的一次性操作
     *
     * 运行结果：和newFixedThreadPool类似，不同的是newScheduledThreadPool是延时一定时间之后才执行
     */
    @Test
    public void newScheduledThreadPool (){
        //表示从提交任务开始计时，5000毫秒后执行
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 20; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程名称：" + Thread.currentThread().getName());
                }
            };
            executorService.schedule(syncRunnable, 5000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 3.2 scheduleAtFixedRate 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；也就是将在 initialDelay 后开始执行，
     * 然后在initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推
     *
     */
    @Test
    public void scheduleAtFixedRate (){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        Runnable syncRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程名称：" + Thread.currentThread().getName());
            }
        };
        //scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnitunit)
        executorService.scheduleAtFixedRate(syncRunnable, 5000, 3000, TimeUnit.MILLISECONDS);
    }

    /**
     * 3.3 scheduleWithFixedDelay创建并执行一个在给定初始延迟后首次启用的定期操作，
     * 随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟
     *
     */
    @Test
    public void scheduleWithFixedDelay (){

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        Runnable syncRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程名称：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        executorService.scheduleWithFixedDelay(syncRunnable, 5000, 3000, TimeUnit.MILLISECONDS);
    }

    /**
     * 4 newSingleThreadExecutor创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     *
     * 运行结果：只会创建一个线程，当上一个执行完之后才会执行第二个
     */
    @Test
    public void newSingleThreadExecutor (){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 20; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程名称：" + Thread.currentThread().getName());
                }
            };
            executorService.execute(syncRunnable);
        }
    }

    /**
     *Executor执行Runnable任务
     *
     * 一旦Runnable任务传递到execute（）方法，该方法便会自动在一个线程上执行。
     * 下面是Executor执行Runnable任务
     */
    @Test
    public void TestRunnable(){
        ExecutorService executorService = Executors.newCachedThreadPool();
//      ExecutorService executorService = Executors.newFixedThreadPool(5);
//      ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++){
            executorService.execute(new TestRunnable());
            System.out.println("************* a" + i + " *************");
        }
        executorService.shutdown();
    }

    class TestRunnable implements Runnable{
        public void run(){
            System.out.println(Thread.currentThread().getName() + "线程被调用了。");
        }
    }

    /**
     *Executor执行Callable任务
     *
     * 任务分两类：一类是实现了Runnable接口的类，一类是实现了Callable接口的类
     *两者都可以被ExecutorService执行，但是Runnable任务没有返回值，而Callable任务有返回值
     * 并且Callable的call()方法只能通过ExecutorService的submit(Callable task) 方法来执行，并且返回一个 Future，是表示任务等待完成的 Future
     *
     * 结果: submit也是首先选择空闲线程来执行任务，如果没有，才会创建新的线程来执行任务
     * 需要注意：如果Future的返回尚未完成，则get（）方法会阻塞等待，直到Future完成返回，可以通过调用isDone（）方法判断Future是否完成了返回
     */
    @Test
    public void TestCallable (){

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<String>> resultList = new ArrayList<Future<String>>();

        //创建10个任务并执行
        for (int i = 0; i < 10; i++){
            //使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
            Future<String> future = executorService.submit(new TaskWithResult(i));
            //将任务执行结果存储到List中
            resultList.add(future);
        }

        //遍历任务的结果
        for (Future<String> fs : resultList){
            try{
                while(!fs.isDone());//Future返回如果没有完成，则一直循环等待，直到Future返回完成
                System.out.println(fs.get());     //打印各个线程（任务）执行的结果
            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(ExecutionException e){
                e.printStackTrace();
            }finally{
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务
                executorService.shutdown();
            }
        }
    }
    class TaskWithResult implements Callable<String>{
        private int id;

        public TaskWithResult(int id){
            this.id = id;
        }

        /**
         * 任务的具体过程，一旦任务传给ExecutorService的submit方法，
         * 则该方法自动在一个线程上执行
         */
        public String call() throws Exception {
            System.out.println("call()方法被自动调用！！！    " + Thread.currentThread().getName());
            //该返回结果将被Future的get方法得到
            return "call()方法被自动调用，任务返回的结果是：" + id + "    " + Thread.currentThread().getName();
        }
    }

    /**
     *自定义线程池
     *
     * 自定义线程池，可以用ThreadPoolExecutor类创建，它有多个构造方法来创建线程池，用该类很容易实现自定义的线程池
     *
     */
    @Test
    public void ThreadPoolTest (){
        //创建等待队列
        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(20);
        //创建线程池，池中保存的线程数为3，允许的最大线程数为5
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3,5,50,TimeUnit.MILLISECONDS,bqueue);
        //创建七个任务
        Runnable t1 = new MyThread();
        Runnable t2 = new MyThread();
        Runnable t3 = new MyThread();
        Runnable t4 = new MyThread();
        Runnable t5 = new MyThread();
        Runnable t6 = new MyThread();
        Runnable t7 = new MyThread();
        //每个任务会在一个线程上执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        pool.execute(t6);
        pool.execute(t7);
        //关闭线程池
        pool.shutdown();
    }
    class MyThread implements Runnable{
        @Override
        public void run(){
            System.out.println(Thread.currentThread().getName() + "正在执行。。。");
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
