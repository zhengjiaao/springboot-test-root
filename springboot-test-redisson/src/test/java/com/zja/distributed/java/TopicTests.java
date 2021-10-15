package com.zja.distributed.java;

import com.zja.BaseTests;
import com.zja.entity.AnyObject;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.PatternMessageListener;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 17:56
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：参考 https://github.com/redisson/redisson/wiki/6.-Distributed-objects
 */
public class TopicTests extends BaseTests {

    //Java implementation of Redis based RTopic object implements Publish / Subscribe mechanism. It allows to subscribe on events published with multiple instances of RTopic object with the same name.
    //Listeners are re-subscribed automatically after reconnection to Redis or Redis failover. All messages sent during absence of connection to Redis are lost. Use Reliable Topic for reliable delivery.

    //发布
    @Test
    public void testTopicPublish() {
        // in other thread or JVM
        RTopic topic = redisson.getTopic("myTopic");
        long clientsReceivedMessage = topic.publish(new AnyObject(222222));
    }

    //订阅  如果没有在线订 ，消息将丢失
    @Test
    public void testTopicListener() throws InterruptedException {
        RTopic topic = redisson.getTopic("myTopic");
        int listenerId = topic.addListener(AnyObject.class, new MessageListener<AnyObject>() {
            @Override
            public void onMessage(CharSequence channel, AnyObject msg) {
                System.out.println(msg.toString());
            }
        });
        Thread.sleep(500000);
    }

    //发布
    @Test
    public void testAsyncTopicPublish() {
        // in other thread or JVM
        RTopicAsync topic = redisson.getTopic("myTopic");
        RFuture<Long> publishFuture = topic.publishAsync(new AnyObject(33333));
    }

    //订阅  如果没有在线订 ，消息将丢失
    @Test
    public void testAsyncTopicListener() throws InterruptedException {
        RTopicAsync topic = redisson.getTopic("myTopic");
        RFuture<Integer> listenerFuture = topic.addListenerAsync(AnyObject.class, new MessageListener<AnyObject>() {
            @Override
            public void onMessage(CharSequence channel, AnyObject msg) {
                System.out.println(msg.toString());
            }
        });
        Thread.sleep(500000);
    }


    //发布
    @Test
    public void testPublish() {
        // in other thread or JVM
        RTopic topic = redisson.getTopic("topic-test");
        long clientsReceivedMessage = topic.publish(new AnyObject(222222));
    }

    //Java implementation of Redis based RPatternTopic object. It allows to subscribe to multiple topics by specified glob-style pattern.
    //
    //Listeners are re-subscribed automatically after reconnection to Redis or Redis failover.
    @Test
    public void testPatternTopic() throws InterruptedException {
        // subscribe to all topics by `topic*` pattern
        RPatternTopic patternTopic = redisson.getPatternTopic("topic*");
        int listenerId = patternTopic.addListener(AnyObject.class, new PatternMessageListener<AnyObject>() {

            @Override
            public void onMessage(CharSequence pattern, CharSequence channel, AnyObject msg) {
                System.out.println(msg);
            }
        });
        Thread.sleep(500000);
    }

    @Test
    public void testAsyncPatternTopic() throws InterruptedException {
        RPatternTopic patternTopic = redisson.getPatternTopic("topic*");
        RFuture<Integer> listenerFuture = patternTopic.addListenerAsync(AnyObject.class, new PatternMessageListener<AnyObject>() {

            @Override
            public void onMessage(CharSequence pattern, CharSequence channel, AnyObject msg) {
                System.out.println(msg);
            }
        });
        Thread.sleep(500000);
    }

    //Java implementation of Redis based RReliableTopic object implements Publish / Subscribe mechanism with reliable delivery of messages. In case of Redis connection interruption all missed messages are delivered after reconnection to Redis. Message considered as delivered when it was received by Redisson and submited for processing by topic listeners.
    //
    //Each RReliableTopic object instance (subscriber) has own watchdog which is started when the first listener was registered. Subscriber expires after org.redisson.config.Config#reliableTopicWatchdogTimeout timeout if watchdog didn't extend it to the next timeout time interval. This prevents against infinity grow of stored messages in topic due to Redisson client crash or any other reason when subscriber unable to consume messages

    //Topic listeners are resubscribed automatically after reconnection to Redis or Redis failover
    //可靠的消息订阅-不丢失消息  redis 5.0
    @Test
    public void testReliableTopicPublish(){
        // in other thread or JVM
        RReliableTopic topic = redisson.getReliableTopic("reliableTopic");
        long subscribersReceivedMessage = topic.publish(new AnyObject(1111));
    }

    //接收消息  禁止用FastjsonCodec 会抛异常
    @Test
    public void testReliableTopicListener() throws InterruptedException {
        RReliableTopic topic = redisson.getReliableTopic("reliableTopic");
        topic.addListener(AnyObject.class, new MessageListener<AnyObject>() {
            @Override
            public void onMessage(CharSequence channel, AnyObject message) {
                System.out.println(message.toString());
            }
        });
        Thread.sleep(500000);
    }

}
