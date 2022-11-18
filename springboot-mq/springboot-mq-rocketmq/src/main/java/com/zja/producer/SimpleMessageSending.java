/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 11:13
 * @Since:
 */
package com.zja.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 普通消息发送
 */
public class SimpleMessageSending {

    /**
     * 同步发送
     */
    public static class SyncProducer {
        public static void main(String[] args) throws Exception {
            // 初始化一个producer并设置Producer group name
            DefaultMQProducer producer = new DefaultMQProducer("producer"); //（1）首先会创建一个producer。普通消息可以创建 DefaultMQProducer，创建时需要填写生产组的名称，生产者组是指同一类Producer的集合，这类Producer发送同一类消息且发送逻辑一致。
            // 设置NameServer地址
            producer.setNamesrvAddr("localhost:9876");  //（2）设置 NameServer 的地址。Apache RocketMQ很多方式设置NameServer地址(客户端配置中有介绍)，这里是在代码中调用producer的API setNamesrvAddr进行设置，如果有多个NameServer，中间以分号隔开，比如"127.0.0.2:9876;127.0.0.3:9876"。
            // 启动producer
            producer.start();
            for (int i = 0; i < 100; i++) {
                // 创建一条消息，并指定topic、tag、body等信息，tag可以理解成标签，对消息进行再归类，RocketMQ可以在消费端对tag进行过滤
                Message msg = new Message("TopicTest" /* Topic */,
                        "TagA" /* Tag */,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );   //（3）第三步是构建消息。指定topic、tag、body等信息，tag可以理解成标签，对消息进行再归类，RocketMQ可以在消费端对tag进行过滤。
                // 利用producer进行发送，并同步等待发送结果
                SendResult sendResult = producer.send(msg);   //（4）最后调用send接口将消息发送出去。同步发送等待结果最后返回SendResult，SendResut包含实际发送状态还包括SEND_OK（发送成功）, FLUSH_DISK_TIMEOUT（刷盘超时）, FLUSH_SLAVE_TIMEOUT（同步到备超时）, SLAVE_NOT_AVAILABLE（备不可用），如果发送失败会抛出异常。
                System.out.printf("%s%n", sendResult);
            }
            // 一旦producer不再使用，关闭producer
            producer.shutdown();
        }
    }

    /**
     * 异步发送
     */
    public static class AsyncProducer {
        public static void main(String[] args) throws Exception {
            // 初始化一个producer并设置Producer group name
            DefaultMQProducer producer = new DefaultMQProducer("async_producer");
            // 设置NameServer地址
            producer.setNamesrvAddr("localhost:9876");
            // 启动producer
            producer.start();
            producer.setRetryTimesWhenSendAsyncFailed(0);
            for (int i = 0; i < 100; i++) {
                final int index = i;
                // 创建一条消息，并指定topic、tag、body等信息，tag可以理解成标签，对消息进行再归类，RocketMQ可以在消费端对tag进行过滤
                Message msg = new Message("TopicTest",
                        "TagA",
                        "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
                // 异步发送消息, 发送结果通过callback返回给客户端
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.printf("%-10d OK %s %n", index,
                                sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        System.out.printf("%-10d Exception %s %n", index, e);
                        e.printStackTrace();
                    }
                });
            }
            // 一旦producer不再使用，关闭producer
            producer.shutdown();
        }
    }

    /**
     * 单向发送
     */
    public static class OnewayProducer {
        public static void main(String[] args) throws Exception {
            // 初始化一个producer并设置Producer group name
            DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
            // 设置NameServer地址
            producer.setNamesrvAddr("localhost:9876");
            // 启动producer
            producer.start();
            for (int i = 0; i < 100; i++) {
                // 创建一条消息，并指定topic、tag、body等信息，tag可以理解成标签，对消息进行再归类，RocketMQ可以在消费端对tag进行过滤
                Message msg = new Message("TopicTest" /* Topic */,
                        "TagA" /* Tag */,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                // 由于在oneway方式发送消息时没有请求应答处理，如果出现消息发送失败，则会因为没有重试而导致数据丢失。若数据不可丢，建议选用可靠同步或可靠异步发送方式。
                producer.sendOneway(msg);
            }
            // 一旦producer不再使用，关闭producer
            producer.shutdown();
        }
    }

}
