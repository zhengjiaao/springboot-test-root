package com.zja.distributed.servers;

import com.zja.BaseTests;
import com.zja.service.SomeServiceInterface;
import com.zja.service.impl.SomeServiceImpl;
import org.junit.Test;
import org.redisson.api.RRemoteService;
import org.redisson.api.RemoteInvocationOptions;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-03 13:44
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc: Distributed services 分布式服务
 */
public class DistributedServicesTests extends BaseTests {

    /**
     * Remote service
     *
     * Redisson provides Java Remote Services to execute remote procedure call using Redis. Remote interface could have any type of method parameters and result object. Redis is used to store method request and corresponding execution result.
     *
     * The RemoteService provides two types of RRemoteService instances:
     */

    // Server side instance - executes remote method(worker instance). Example
    //服务器端实例 - 执行远程方法
    @Test
    public void testExecutesRemoteMethod(){
        RRemoteService remoteService = redisson.getRemoteService();
        SomeServiceImpl someServiceImpl = new SomeServiceImpl();

        // register remote service before any remote invocation
        // can handle only 1 invocation concurrently
        remoteService.register(SomeServiceInterface.class, someServiceImpl);

        // register remote service able to handle up to 12 invocations concurrently
//        remoteService.register(SomeServiceInterface.class, someServiceImpl, 12);


        //获取服务
        //Client and server side instances shall be using the same remote interface and backed by redisson instances created using the same server connection configuration
        SomeServiceInterface service = remoteService.get(SomeServiceInterface.class);

        String result = service.doSomeStuff("a");
        System.out.println(result);
    }

    //Client side instance - invokes remote method. Example:
    //客户端实例 - 调用远程方法
    @Test
    public void testInvokesRemoteMethod(){
        RRemoteService remoteService = redisson.getRemoteService();

        //Client and server side instances shall be using the same remote interface and backed by redisson instances created using the same server connection configuration
        SomeServiceInterface service = remoteService.get(SomeServiceInterface.class);

        //不成功，必须在一个redisson实例中调用
        String result = service.doSomeStuff("a");
        System.out.println(result);
    }

    //RemoteService options for each remote invocation could be defined via RemoteInvocationOptions object. Such options allow to change timeouts and skip ack-response and/or result-response. Examples:
    @Test
    public void testRemoteServiceFireAndForgetAndAckResponseModes(){
        // 1 second ack timeout and 30 seconds execution timeout
//        RemoteInvocationOptions options = RemoteInvocationOptions.defaults();

        // no ack but 30 seconds execution timeout
//        RemoteInvocationOptions options = RemoteInvocationOptions.defaults().noAck();

        // 1 second ack timeout then forget the result
        RemoteInvocationOptions options = RemoteInvocationOptions.defaults().noResult();

        // 1 minute ack timeout then forget about the result
//        RemoteInvocationOptions options = RemoteInvocationOptions.defaults().expectAckWithin(1, TimeUnit.MINUTES).noResult();

        // no ack and forget about the result (fire and forget)
//        RemoteInvocationOptions options = RemoteInvocationOptions.defaults().noAck().noResult();

        RRemoteService remoteService = redisson.getRemoteService();
        SomeServiceInterface service = remoteService.get(SomeServiceInterface.class, options);
    }

}
