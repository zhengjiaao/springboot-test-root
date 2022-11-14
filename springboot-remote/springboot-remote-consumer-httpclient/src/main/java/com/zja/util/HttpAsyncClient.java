package com.zja.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * HttpAsyncClients  封装类
 *
 * @author zhengja@dist.com.cn
 * @data 2019/9/17 19:26
 */
@Component
public class HttpAsyncClient {

    /**封装异步的 httpclient -get 下载
     *
     * @param url  String url = http://192.168.*.*:8098/dgp-dubbo-server-web/rest/ommb/v1/offlinepackage/downloadSplitFile + "?splitFileName=" + filePathName;
     */
    public void httpAsyncClient(String url) {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();

        final CountDownLatch latch = new CountDownLatch(1);
        final HttpGet request = new HttpGet(url);

        System.out.println(" caller thread id is 进入主线程: " + Thread.currentThread().getId());

        httpclient.execute(request, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(final HttpResponse response) {
                //异步执行
                System.out.println("进入子线程...");
                latch.countDown();
                System.out.println(" callback thread id is 子线程正常执行: " + Thread.currentThread().getId());
                System.out.println("子线程总数 : "+Thread.currentThread().getThreadGroup());
                //System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                try {
                    //获取结果转为 String 类型
                    /*String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    System.out.println(" response content is : " + content);*/

                    System.out.println("entity.getContentLength =: " + response.getEntity().getContentLength());
                    //获取结果转为 byte[]
                    final byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                    System.out.println("bytes.length== " + bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(final Exception ex) {
                latch.countDown();
                System.out.println(request.getRequestLine() + "->" + ex);
                System.out.println(" callback thread id is 子线程失败: " + Thread.currentThread().getId());
            }

            @Override
            public void cancelled() {
                latch.countDown();
                System.out.println(request.getRequestLine() + " cancelled");
                System.out.println(" callback thread id is 子线程取消: " + Thread.currentThread().getId());
            }

        });
        System.out.println("主线程总数 : "+Thread.currentThread().getThreadGroup());
        System.out.println("caller thread id is 主线程结束.: "+Thread.currentThread().getId());

        /***** 同步方式：主线程等待所有子线程结束，拿到所有子线程结果 ******/
        //httpResponseFuture.get() 阻塞主线程，等待子线程的所有结果
        /*try {
            final HttpResponse httpResponse = httpResponseFuture.get();
            final HttpEntity entity = httpResponse.getEntity();
            final byte[] bytes = EntityUtils.toByteArray(entity);
            System.out.println("bytes.length== " + bytes.length);
            System.out.println("entity.getContentLength =: " + entity.getContentLength());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /*try {
            httpclient.close();
        } catch (IOException ignore) {

        }*/
    }

}
