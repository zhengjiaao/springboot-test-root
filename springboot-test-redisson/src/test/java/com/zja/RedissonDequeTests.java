package com.zja;

import com.zja.entity.MyEntry;
import com.zja.entity.SomeObject;
import org.junit.Test;
import org.redisson.api.*;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 13:26
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class RedissonDequeTests extends BaseTests{

    /**************************************redis 分布式集合 Queue 测试 start***************************************/

    /**
     * Queue 基于链表实现单向队列 先进先出 插入和删除操作，效率会比较高
     *
     * Redis based distributed unbounded Queue object for Java implements java.util.Queue interface.
     */
    @Test
    public void testQueue() {
        RQueue<SomeObject> queue = redisson.getQueue("anyQueue");
        queue.add(new SomeObject("a"));
        queue.add(new SomeObject("b"));
        SomeObject obj = queue.peek(); //获取第一个元素
        SomeObject someObj = queue.poll(); //取出队列中第一个元素(先进先出)
    }

    //Deque 双向队列 若将Deque限制只能从一端入队和出队，就是栈的数据结构 先进后出 队列两端的元素，既能入队（offer）也能出队
    //Redis based distributed unbounded Deque object for Java implements java.util.Deque interface
    @Test
    public void testDeque() {
        RDeque<SomeObject> queue = redisson.getDeque("anyDeque");
        queue.addFirst(new SomeObject("a"));
        queue.addLast(new SomeObject("b"));

        SomeObject obj = queue.removeFirst();
        SomeObject someObj = queue.removeLast();
    }

    //阻塞队列
    //Redis based distributed unbounded BlockingQueue object for Java implements java.util.concurrent.BlockingQueue interface
    //poll, pollFromAny, pollLastAndOfferFirstTo and take methods are resubscribed automatically during re-connection to Redis server or failover
    @Test
    public void testBlockingQueue() throws InterruptedException {
        RBlockingQueue<SomeObject> queue = redisson.getBlockingQueue("anyBlockingQueue");

        queue.offer(new SomeObject("a"));

        SomeObject someObject = queue.peek();
//        SomeObject obj = queue.poll();
        SomeObject obj = queue.poll(10, TimeUnit.SECONDS);  //在放弃之前需要等待多久时间
    }


    //有界阻塞队列
    //Redis based distributed BoundedBlockingQueue for Java implements java.util.concurrent.BlockingQueue interface
    //Queue capacity should be defined once by trySetCapacity() method before the usage
    @Test
    public void testBoundedBlockingQueue() throws InterruptedException {
        RBoundedBlockingQueue<SomeObject> queue = redisson.getBoundedBlockingQueue("boundedBlockingQueue");
        // returns `true` if capacity set successfully and `false` if it already set.
        queue.trySetCapacity(2);

        queue.offer(new SomeObject("1"));
        queue.offer(new SomeObject("2"));
        // will be blocked until free space available in queue
        queue.put(new SomeObject("3")); //将被阻塞，直到队列中有可用空间
    }

    //取出队列的元素
    @Test
    public void testBoundedBlockingQueue2() throws InterruptedException {
        RBoundedBlockingQueue<SomeObject> queue = redisson.getBoundedBlockingQueue("boundedBlockingQueue");
        SomeObject object = queue.poll();
        System.out.println("object: " + object); // 1

        Iterator<SomeObject> iterator = queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next()); //2 3
        }
    }

    //阻塞双端队列
    //Java implementation of Redis based BlockingDeque implements java.util.concurrent.BlockingDeque interface
    @Test
    public void testBlockingDeque() throws InterruptedException {
        RBlockingDeque<Integer> deque = redisson.getBlockingDeque("blockingDeque");
        deque.putFirst(1);
        deque.put(2);
        deque.putLast(3);

        Integer firstValue = deque.takeFirst(); //取出队列第一个元素 1
        Integer lastValue = deque.takeLast(); //3

//        Integer firstValue = deque.pollFirst(10, TimeUnit.MINUTES);
//        Integer lastValue = deque.pollLast(3, TimeUnit.MINUTES);
    }


    //优先队列
    //Java implementation of Redis based PriorityQueue implements java.util.Queue interface. Elements are ordered according to natural order of java.lang.Comparable interface or defined java.util.Comparator.
    @Test
    public void testPriorityQueue() {
        //按优先级进行排序
        RPriorityQueue<MyEntry> queue = redisson.getPriorityQueue("priorityQueue");
        queue.add(new MyEntry("b", 1));
        queue.add(new MyEntry("c", 1));
        queue.add(new MyEntry("a", 1));

        MyEntry e = queue.poll(); // MyEntry [a:1]
        MyEntry e1 = queue.poll(); // MyEntry [b:1]
        MyEntry e2 = queue.poll(); // MyEntry [c:1]
    }

    //优先双端队列
    //Java implementation of Redis based PriorityDeque implements java.util.Deque interface. Elements are ordered according to natural order of java.lang.Comparable interface or defined java.util.Comparator.
    @Test
    public void testPriorityDeque() {
        RPriorityDeque<MyEntry> queue = redisson.getPriorityDeque("PriorityDeque");
        queue.add(new MyEntry("b", 1));
        queue.add(new MyEntry("c", 1));
        queue.add(new MyEntry("a", 1));

        MyEntry e = queue.pollFirst(); // MyEntry [a:1]
        MyEntry e1 = queue.pollLast();  // MyEntry [c:1]
    }

    //优先阻塞队列
    @Test
    public void testPriorityBlockingQueue() throws InterruptedException {
        RPriorityBlockingQueue<MyEntry> queue = redisson.getPriorityBlockingQueue("priorityBlockingQueue");
        queue.add(new MyEntry("b", 1));
        queue.add(new MyEntry("c", 1));
        queue.add(new MyEntry("a", 1));

        MyEntry e = queue.take(); // Entry [a:1]
    }

    @Test
    public void testPriorityBlockingDeque() throws InterruptedException {
        RPriorityBlockingDeque<MyEntry> queue = redisson.getPriorityBlockingDeque("priorityBlockingDeque");
        queue.add(new MyEntry("b", 1));
        queue.add(new MyEntry("c", 1));
        queue.add(new MyEntry("a", 1));

        MyEntry e = queue.takeFirst(); // Entry [a:1]
        MyEntry e1 = queue.takeLast(); // Entry [c:1]
    }

}
