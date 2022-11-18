/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 13:16
 * @Since:
 */
package com.zja.consumer;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.PullStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 拉动 消费
 */
public class PullConsume {

    /**
     * Pull Consumer
     * 4.6 之前的方式
     */
    @Deprecated
    public static class PullConsumerTest {
        public static void main(String[] args) throws MQClientException {
            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("pull_consumer");
            consumer.setNamesrvAddr("127.0.0.1:9876");
            consumer.start();
            try {
                MessageQueue mq = new MessageQueue();
                mq.setQueueId(0);
                mq.setTopic("TopicTest");
                mq.setBrokerName("jinrongtong-MacBook-Pro.local");
                long offset = 26;
                PullResult pullResult = consumer.pull(mq, "*", offset, 32);
                if (pullResult.getPullStatus().equals(PullStatus.FOUND)) {
                    System.out.printf("%s%n", pullResult.getMsgFoundList());
                    consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            consumer.shutdown();
        }
    }


    /**
     * Lite Pull Consumer
     * 4.6 之后拉去方式：提供了Subscribe和Assign两种方式，更加方便。
     * 方式一：Subscribe
     */
    public static class LitePullConsumerSubscribe {
        public static volatile boolean running = true;

        public static void main(String[] args) throws Exception {
            DefaultLitePullConsumer litePullConsumer = new DefaultLitePullConsumer("lite_pull_consumer_test");
            litePullConsumer.subscribe("TopicTest", "*");
            litePullConsumer.setPullBatchSize(20);
            litePullConsumer.start();
            try {
                while (running) {
                    List<MessageExt> messageExts = litePullConsumer.poll();
                    System.out.printf("%s%n", messageExts);
                }
            } finally {
                litePullConsumer.shutdown();
            }
        }
    }

    /**
     * Lite Pull Consumer
     * 4.6 之后拉去方式：提供了Subscribe和Assign两种方式，更加方便。
     * 方式二：Assign
     */
    public static class LitePullConsumerAssign {
        public static volatile boolean running = true;

        public static void main(String[] args) throws Exception {
            DefaultLitePullConsumer litePullConsumer = new DefaultLitePullConsumer("please_rename_unique_group_name");
            litePullConsumer.setAutoCommit(false);
            litePullConsumer.start();
            Collection<MessageQueue> mqSet = litePullConsumer.fetchMessageQueues("TopicTest");
            List<MessageQueue> list = new ArrayList<>(mqSet);
            List<MessageQueue> assignList = new ArrayList<>();
            for (int i = 0; i < list.size() / 2; i++) {
                assignList.add(list.get(i));
            }
            litePullConsumer.assign(assignList);
            litePullConsumer.seek(assignList.get(0), 10);
            try {
                while (running) {
                    List<MessageExt> messageExts = litePullConsumer.poll();
                    System.out.printf("%s %n", messageExts);
                    litePullConsumer.commitSync();
                }
            } finally {
                litePullConsumer.shutdown();
            }
        }
    }


}
