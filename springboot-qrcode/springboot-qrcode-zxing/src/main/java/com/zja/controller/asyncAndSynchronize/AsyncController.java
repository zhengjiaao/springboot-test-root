package com.zja.controller.asyncAndSynchronize;

import com.zja.server.AsynchronousService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * 同步、异步测试
 *
 * @author zhengja@dist.com.cn
 * @data 2019/5/7 14:01
 */
@RestController("AsyncController")
@RequestMapping("rest/get")
@EnableAsync  //springboot 自带注解 controller上加上@EnableAsync,异步执行的方法上加上@Async注解
@Api(tags = {"AsyncController"}, description = "同步、异步测试")
public class AsyncController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AsynchronousService asynchronousService;

    /**
     * 同步的方式处理
     * 当这100此循环完成后，页面才会返回 ：同步,正在解析......
     * 当后台在循环处理时，前台的页面始终处于等待状态。可以发现，使用都是一个线程在处理
     *
     * @return
     */
    @ApiOperation(value = "同步-100循环完成后才会返回结果", notes = "当这50此循环完成后，页面才会返回 ：同步,正在解析...当后台在循环处理时，前台的页面始终处于等待状态。可以发现，使用都是一个线程在处理")
    @RequestMapping(value = "v1/synchronize/data", method = RequestMethod.GET)
    public String testSynchronize() {
        long start = System.currentTimeMillis();
        //调用同步方法
        asynchronousService.testSynchronize();
        long syncTime = System.currentTimeMillis();
        logger.info("同步方法用时：{}", syncTime - start);
        logger.info("主线程名：====== > " + Thread.currentThread().getName());
        return "同步，正在解析......";
    }

    /**
     * 测试异步处理：使用方式，springboot自带async注解
     *
     * @return
     */
    @ApiOperation(value = "异步-使用springboot自带async注解", notes = "springboot自带async注解")
    @RequestMapping(value = "v1/async/data", method = RequestMethod.GET)
    public String getAsyncData() {
        //asyncAndSynchronize 获取不到结果 null
        String asynchronous = asynchronousService.testAsynchronous();
        logger.info("====== > " + Thread.currentThread().getName());
        return "异步，正在解析......";
    }

    /**
     * 异步处理1：线程池，创建新线程处理
     * 发现：主线程，和处理任务的线程，不是一个线程
     * 页面请求后，主线程会返回我们想要返回的标识，这里返回的是一个字符串：异步，正在解析......
     * 线程池新开了一个线程，在后台处理业务逻辑
     * 此时访问接口后，会立马返回，页面不用等待，处理逻辑在后台默默进行
     *
     * @return
     */
    @ApiOperation(value = "异步-线程池，创建新线程处理", notes = "异步处理：线程池，创建新线程处理")
    @RequestMapping(value = "v2/async/data", method = RequestMethod.GET)
    public String getAsyncData2() {
        //asyncAndSynchronize 获取不到结果 null
        ExecutorService service = Executors.newFixedThreadPool(5);
        RunnableTask1 task1 = new RunnableTask1();
        service.execute(task1);
        logger.info("=========》当前线程名：" + Thread.currentThread().getName());
        return "异步,正在解析......";
    }

    class RunnableTask1 implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void run() {
            String user = "Zhengja";
            synchronized (user) {
                try {
                    for (int i = 1; i <= 100; i++) {
                        System.out.println(Thread.currentThread().getName() + "----------异步：>" + i);
                        user.wait(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("异步错误信息: ", e);
                }
            }
        }
    }


    /**
     * 自定义线程池-进行线程的复用
     * 异步+自定义线程池
     *
     * @return
     */
    @ApiOperation(value = "异步+自定义线程池", notes = "自定义线程池-进行线程的复用")
    @RequestMapping(value = "v3/asyn/data", method = RequestMethod.GET)
    public String asyncEvent() throws InterruptedException {
        asynchronousService.asyncEvent();
        logger.info("主线程名：====== > " + Thread.currentThread().getName());
        return "自定义线程池，正在解析......";
    }

    /**
     * 自定义线程池异步调用及超时处理
     * <p>
     * 当某个业务功能可以同时拆开一起执行时，可利用异步回调机制，可有效的减少程序执行时间，提高效率
     *
     * @return
     * @throws InterruptedException
     */
    @ApiOperation(value = "异步-自定义线程池调用及超时处理", notes = "当某个业务功能可以同时拆开一起执行时，可利用异步回调机制，可有效的减少程序执行时间，提高效率")
    @RequestMapping(value = "v4/async/future", method = RequestMethod.GET)
    public String asyncFuture() throws Exception {

        long syncTime = System.currentTimeMillis();
        System.out.println("正在执行异步方式...");
        //调用异步方法-具有返回值
        Future<String> doFutrue = asynchronousService.asyncFuture();
        //超时30s
        String result = doFutrue.get(30, TimeUnit.SECONDS);
        logger.info("返回值：" + result);
        while (true) {
            //判断异步任务是否完成
            if (doFutrue.isDone()) {
                break;
            }
            Thread.sleep(100);
        }
        logger.info("返回值2：" + result);
        long asyncTime = System.currentTimeMillis();
        logger.info("异步方法用时：{}", asyncTime - syncTime);

        return "方法执行完成!";
    }

    /*
    超时处理:
        对于一些需要异步回调的函数，不能无期限的等待下去，所以一般上需要设置超时时间，
        超时后可将线程释放，而不至于一直堵塞而占用资源。
        Future配置超时,通过get方法

        //get方法会一直堵塞，直到等待执行完成才返回
        //get(long timeout, TimeUnit unit) 在设置时间类未返回结果，会直接排除异常TimeoutException，messages为null
        String result = doFutrue.get(60, TimeUnit.SECONDS);//60s

        超时后，会抛出异常TimeoutException类，此时可进行统一异常捕获即可
     */
}
