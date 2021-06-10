package com.zja.distributed.java;

import com.zja.BaseTests;
import com.zja.entity.*;
import org.junit.Test;
import org.redisson.api.*;
import org.redisson.api.map.event.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 17:00
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：参考 https://github.com/redisson/redisson/wiki/7.-Distributed-collections
 *
 * 包含： Map Set List Queue、Deque  Stream  RingBuffer
 */
public class DistributedCollectionsTests extends BaseTests {

    /**************************************redis 分布式集合 Map 测试 start***************************************/

    /**
     * ConcurrentMap
     */
    //Redis based distributed Map object for Java implements ConcurrentMap interface. Consider to use Live Object service to store POJO object as Redis Map
    //
    //If Map used mostly for read operations and/or network roundtrips are undesirable use Map with Local cache support
    @Test
    public void TestMapExample() {
       /* MapOptions<K, V> options = MapOptions.<K, V>defaults()
                .loader(mapLoader);
        RMap<K, V> map = redisson.getMap("test", options);
        // or
        RMapCache<K, V> map = redisson.getMapCache("test", options);
        // or with boost up to 45x times
        RLocalCachedMap<K, V> map = redisson.getLocalCachedMap("test", options);*/
    }

    @Test
    public void testgetMap() {
        RMap<String, SomeObject> map = redisson.getMap("anyMap");
        SomeObject prevObject = map.put("123", new SomeObject("123"));
        SomeObject currentObject = map.putIfAbsent("323", new SomeObject("323"));
        SomeObject obj = map.remove("123");

        // use fast* methods when previous value is not required
        map.fastPut("a", new SomeObject("a"));
        map.fastPutIfAbsent("d", new SomeObject("d"));
        map.fastRemove("b");

        RFuture<SomeObject> putAsyncFuture = map.putAsync("321", new SomeObject("321"));
        RFuture<Boolean> fastPutAsyncFuture = map.fastPutAsync("321", new SomeObject("321"));

        map.fastPutAsync("321", new SomeObject("321"));
        map.fastRemoveAsync("321");
    }

    //RMap object allows to bind a Lock/ReadWriteLock/Semaphore/CountDownLatch object per key:
    @Test
    public void testLockgetMap() {
        RMap<MyKey, MyValue> map = redisson.getMap("anyMap");
        MyKey k = new MyKey("key1");
        RLock keyLock = map.getLock(k);
        keyLock.lock();
        try {
            MyValue v = map.get(k);
            map.put(k, new MyValue("value1"));
            // process value ...
        } finally {
            keyLock.unlock();
        }

        RReadWriteLock rwLock = map.getReadWriteLock(k);
        rwLock.readLock().lock();
        try {
            MyValue v = map.get(k);
            // process value ...
        } finally {
            rwLock.readLock().unlock();
        }
    }

    //推荐 Map
    //Map eviction
    @Test
    public void testgetMapCache() throws InterruptedException {
        RMapCache<String, SomeObject> map = redisson.getMapCache("anyMap");

        //以下是 收费的 Redisson PRO
        // or
//        RMapCache<String, SomeObject> map = redisson.getLocalCachedMapCache("anyMap", LocalCachedMapOptions.defaults());
        // or
//        RMapCache<String, SomeObject> map = redisson.getClusteredLocalCachedMapCache("anyMap", LocalCachedMapOptions.defaults());
        // or
//        RMapCache<String, SomeObject> map = redisson.getClusteredMapCache("anyMap");

        //定时驱逐

        // ttl = 10 minutes,
        map.put("key1", new SomeObject("value1"), 10, TimeUnit.MINUTES);
        // ttl = 10 minutes, maxIdleTime = 10 seconds
        map.put("key1", new SomeObject("value1"), 10, TimeUnit.MINUTES, 10, TimeUnit.SECONDS);

        // ttl = 5 seconds
        map.putIfAbsent("key2", new SomeObject("value2"), 5, TimeUnit.SECONDS);
        Thread.sleep(6000); //停留6秒,执行删除任务, 断开连接不会删除

        // ttl = 15 seconds, maxIdleTime = 10 seconds
        map.putIfAbsent("key2", new SomeObject("value2"), 15, TimeUnit.SECONDS, 10, TimeUnit.SECONDS);
        Thread.sleep(16000); //停留16秒,执行删除任务, 断开连接不会删除


        // if object is not used anymore  删除anyMap
//        map.destroy();
    }


    @Test
    public void testgetLocalCachedMap() {
        RLocalCachedMap<String, Integer> map = redisson.getLocalCachedMap("localcachedmap", LocalCachedMapOptions.defaults());

        //收费 Redisson PRO 提供
        // or
//        RLocalCachedMap<String, SomeObject> map = redisson.getLocalCachedMapCache("anyMap", LocalCachedMapOptions.defaults());
        // or
//        RLocalCachedMap<String, SomeObject> map = redisson.getClusteredLocalCachedMapCache("anyMap", LocalCachedMapOptions.defaults());
        // or
//        RLocalCachedMap<String, SomeObject> map = redisson.getClusteredLocalCachedMap("anyMap", LocalCachedMapOptions.defaults());


        Integer prevObject = map.put("123", 1);
        Integer currentObject = map.putIfAbsent("323", 2);
        Integer obj = map.remove("123");

        // use fast* methods when previous value is not required
        map.fastPut("a", 1);
        map.fastPutIfAbsent("d", 32);
        map.fastRemove("b");

        RFuture<Integer> putAsyncFuture = map.putAsync("321", 321);
        RFuture<Boolean> fastPutAsyncFuture = map.fastPutAsync("321", 321);

        RFuture<Boolean> booleanRFuture = map.fastPutAsync("321", 321);
        map.fastRemoveAsync("321");

        map.destroy();
    }

    @Test
    public void testloadDataCachedMap() {

        Map<String, String> map = new HashMap<>();
        map.put("aaa", "aaa");
        map.put("bbb", "bbb");

        loadData("loadData", map);
    }

    public void loadData(String cacheName, Map<String, String> data) {
        RLocalCachedMap<Object, Object> clearMap = redisson.getLocalCachedMap(cacheName,
                LocalCachedMapOptions.defaults().cacheSize(1).syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE));
        RLocalCachedMap<Object, Object> loadMap = redisson.getLocalCachedMap(cacheName,
                LocalCachedMapOptions.defaults().cacheSize(1).syncStrategy(LocalCachedMapOptions.SyncStrategy.NONE));

        loadMap.putAll(data);
        clearMap.clearLocalCache();
    }

    // map 操作监听器
    @Test
    public void testMapListener() throws InterruptedException {

        RMapCache<String, SomeObject> map = redisson.getMapCache("anyMap");
// or
//        RLocalCachedMap<Object, Object> map = redisson.getLocalCachedMap("anyMap",LocalCachedMapOptions.defaults());

        int updateListener = map.addListener(new EntryUpdatedListener<Integer, Integer>() {
            @Override
            public void onUpdated(EntryEvent<Integer, Integer> event) {
                event.getKey(); // key
                event.getValue(); // new value
                event.getOldValue(); // old value
                // ...
            }
        });

        int createListener = map.addListener(new EntryCreatedListener<Integer, Integer>() {
            @Override
            public void onCreated(EntryEvent<Integer, Integer> event) {
                event.getKey(); // key
                event.getValue();// value
                // ...
            }
        });

        int expireListener = map.addListener(new EntryExpiredListener<Integer, Integer>() {
            @Override
            public void onExpired(EntryEvent<Integer, Integer> event) {
                event.getKey(); // key
                event.getValue();// value
                // ...
            }
        });

        int removeListener = map.addListener(new EntryRemovedListener<Integer, Integer>() {
            @Override
            public void onRemoved(EntryEvent<Integer, Integer> event) {
                event.getKey(); // key
                event.getValue();// value
                // ...
            }
        });


        //移除的监听器不再执行监听
//        map.removeListener(updateListener);
//        map.removeListener(createListener);
//        map.removeListener(expireListener);
//        map.removeListener(removeListener);

        Thread.sleep(5000000);
    }


    //Map object which implements RMapCache interface could be bounded using Least Recently Used (LRU) or Least Frequently Used (LFU) order. Bounded Map allows to store map entries within defined limit and retire entries in defined order
    // limited Redis memory
    @Test
    public void testLRUBoundedMap() throws InterruptedException {
        RMapCache<String, SomeObject> map = redisson.getMapCache("anyMap");

        // tries to set limit map to 10 entries using LRU eviction algorithm
        map.trySetMaxSize(10);

        // set or change limit map to 10 entries using LRU eviction algorithm
        map.setMaxSize(10);

        map.put("1", new SomeObject("1"));
        map.put("2", new SomeObject("1"));
        map.put("3", new SomeObject("1"));
        map.put("4", new SomeObject("1"));
        map.put("5", new SomeObject("1"));
        map.put("6", new SomeObject("1"));
        map.put("7", new SomeObject("1"));
        map.put("8", new SomeObject("1"));
        map.put("9", new SomeObject("1"));
        map.put("10", new SomeObject("1"));
        System.out.println(map.keySet()); //[10, 5, 6, 7, 3, 9, 4, 8, 1, 2]
        map.put("11", new SomeObject("1"));
        System.out.println(map.keySet()); //[10, 5, 6, 7, 3, 9, 4, 8, 11, 2] 先进先出
        map.put("12", new SomeObject("1"));
        System.out.println(map.keySet()); //[10, 5, 6, 7, 3, 9, 4, 12, 8, 11]
        map.put("13", new SomeObject("1"));
        System.out.println(map.keySet()); //[10, 5, 6, 7, 9, 4, 12, 8, 11, 13]
        map.put("14", new SomeObject("1"), 3, TimeUnit.SECONDS); //3秒删除
        System.out.println(map.keySet());
        Thread.sleep(4000); //停留4秒,执行定时删除任务
        System.out.println(map.keySet());
    }


    /**
     * Multimap
     *
     * Redis based Multimap for Java allows to bind multiple values per key. Keys amount limited by Redis to 4 294 967 295 elements.
     */
    //Set based Multimap
    //Set based Multimap doesn't allow duplications for values per key
    @Test
    public void testSetMultimap() {
        // key相同 值不能重复
        RSetMultimap<SimpleKey, SimpleValue> map = redisson.getSetMultimap("setMultimap");
        map.put(new SimpleKey("0"), new SimpleValue("1"));
        map.put(new SimpleKey("0"), new SimpleValue("2"));
        map.put(new SimpleKey("3"), new SimpleValue("4"));

        Set<SimpleValue> allValues = map.get(new SimpleKey("0"));

        List<SimpleValue> newValues = Arrays.asList(new SimpleValue("7"), new SimpleValue("6"), new SimpleValue("5"));
        Set<SimpleValue> oldValues = map.replaceValues(new SimpleKey("0"), newValues);

        Set<SimpleValue> removedValues = map.removeAll(new SimpleKey("0"));
    }

    //List based Multimap
    //List based Multimap object for Java stores entries in insertion order and allows duplicates for values mapped to key
    @Test
    public void testListMultimap() {
        //key相同 允许值重复
        RListMultimap<SimpleKey, SimpleValue> map = redisson.getListMultimap("listMultimap");
        map.put(new SimpleKey("0"), new SimpleValue("1"));
        map.put(new SimpleKey("0"), new SimpleValue("2"));
        map.put(new SimpleKey("0"), new SimpleValue("1"));
        map.put(new SimpleKey("3"), new SimpleValue("4"));

        List<SimpleValue> allValues = map.get(new SimpleKey("0"));

        Collection<SimpleValue> newValues = Arrays.asList(new SimpleValue("7"), new SimpleValue("6"), new SimpleValue("5"));
        List<SimpleValue> oldValues = map.replaceValues(new SimpleKey("0"), newValues);

        List<SimpleValue> removedValues = map.removeAll(new SimpleKey("0"));
    }


    //Multimap eviction
    //Multimap object for Java with eviction support implemented by separated MultimapCache object. There are RSetMultimapCache and RListMultimapCache objects for Set and List Multimaps respectivly.
    //Expired entries are cleaned by org.redisson.EvictionScheduler. It removes 100 expired entries at once. Task launch time tuned automatically and depends on expired entries amount deleted in previous time and varies between 1 second to 2 hours. Thus if clean task deletes 100 entries each time it will be executed every second (minimum execution delay). But if current expired entries amount is lower than previous one then execution delay will be increased by 1.5 times.
    @Test
    public void testMultimapEviction() throws InterruptedException {
        RSetMultimapCache<String, String> multimap = redisson.getSetMultimapCache("setMultimapCache");
        multimap.put("1", "a");
        multimap.put("1", "b");
        multimap.put("1", "c");

        multimap.put("2", "e");
        multimap.put("2", "f");

        multimap.expireKey("2", 3, TimeUnit.SECONDS);
        System.out.println(multimap.keySet());
        Thread.sleep(6000);
        System.out.println(multimap.keySet());
    }


    /**************************************redis 分布式集合 Set 测试 start***************************************/

    /**
     * Set
     *
     * Redis based Set object for Java implements java.util.Set interface. Keeps elements uniqueness via element state comparison. Set size limited by Redis to 4 294 967 295 elements
     */

    //set
    @Test
    public void testSet() {
        RSet<SomeObject> set = redisson.getSet("anySet");
        set.add(new SomeObject("a"));
        set.remove(new SomeObject("a"));
    }

    //RSet object allows to bind a Lock/ReadWriteLock/Semaphore/CountDownLatch object per value
    @Test
    public void testLockSet() {
        RSet<MyObject> set = redisson.getSet("anySet");
        MyObject value = new MyObject("a");
        RLock lock = set.getLock(value);
        lock.lock();
        try {
            // process value ...
            set.add(value);
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testSetCache() throws InterruptedException {
        RSetCache<SomeObject> set = redisson.getSetCache("setCache");

        // ttl = 5 seconds,
        set.add(new SomeObject("a"), 5, TimeUnit.SECONDS);
        Thread.sleep(10000);
        // if object is not used anymore
//        set.destroy();
    }

    //Redis based distributed SortedSet for Java implements java.util.SortedSet interface. It uses comparator to sort elements and keep uniqueness. For String data type it's recommended to use LexSortedSet object due to performance gain
    @Test
    public void testSortedSet() throws InterruptedException {
        RSortedSet<Integer> set = redisson.getSortedSet("sortedSet");
//        set.trySetComparator((Comparator<? super Integer>) new MyComparator(111)); // set object comparator
        set.add(3);
        set.add(1);
        set.add(2);

        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

//        set.removeAsync(0);
//        set.addAsync(5);
    }

    //Redis based distributed ScoredSortedSet object. Sorts elements by score defined during element insertion. Keeps elements uniqueness via element state comparison.
    @Test
    public void testScoredSortedSet() {
        RScoredSortedSet<SomeObject> set = redisson.getScoredSortedSet("scoredSortedSet");

        set.add(0.13, new SomeObject("a", "b"));
        set.addAsync(0.251, new SomeObject("c", "d"));
        set.add(0.302, new SomeObject("g", "d"));

        int index = set.rank(new SomeObject("g", "d")); // get element index
        Double score = set.getScore(new SomeObject("g", "d")); // get element score

        SomeObject someObject = set.pollFirst(); //取出第一个元素
        SomeObject someObject1 = set.pollLast(); //取出最后一个元素
    }


    //Redis based distributed Set object for Java allows String objects only and implements java.util.Set<String> interface. It keeps elements in lexicographical order and maintain elements uniqueness via element state comparison
    @Test
    public void testLexSortedSet() {
        RLexSortedSet set = redisson.getLexSortedSet("lexSortedSet");
        set.add("d");
        set.addAsync("e");
        set.add("f");

        //返回 d 之后的元素,且不包含d元素
        Collection<String> rangeTail = set.rangeTail("d", false);
        //返回 e 之前的元素个数
        int countHead = set.countHead("e", false);
        //返回指定范围内的元素
        Collection<String> range = set.range("d", true, "z", false);
    }

    /**************************************redis 分布式集合 List 测试 start***************************************/

    /**
     * List
     *
     * Redis based distributed List object for Java implements java.util.List interface. It keeps elements in insertion order
     */

    // List size is limited by Redis to 4 294 967 295 elements
    @Test
    public void testList() {
        RList<SomeObject> list = redisson.getList("anyList");
        list.add(new SomeObject("a"));
        SomeObject someObject = list.get(0);
        list.remove(new SomeObject("a"));
    }

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


    /**************************************redis 分布式集合 Stream 测试 start***************************************/

    /**
     * Stream  Redis 5.0
     *
     * Java implementation of Redis based Stream object wraps Redis Stream feature. Basically it allows to create Consumers Group which consume data added by Producers
     */
    @Test
    public void testStream() {
        RStream<String, String> stream = redisson.getStream("testStream");

        //生成消息id
        stream.add(new StreamMessageId(1),"0", "0");

        stream.createGroup("testGroup");

        //自定义消息id
        StreamMessageId messageId = stream.add("1", "1");
        StreamMessageId messageId1 = stream.add("2", "2");

        //没测出效果
        Map<StreamMessageId, Map<String, String>> group = stream.readGroup("testGroup", "consumer1", messageId);
        long amount = stream.ack("testGroup", messageId, messageId1);
    }

    @Test
    public void testAsyncStream() throws ExecutionException, InterruptedException {
        RStream<String, String> stream = redisson.getStream("test");

        RFuture<StreamMessageId> smFuture = stream.addAsync("0", "0");

        RFuture<Void> groupFuture = stream.createGroupAsync("testGroup");

        RFuture<StreamMessageId> streamMessageIdRFuture = stream.addAsync("1", "1");
        RFuture<StreamMessageId> streamMessageIdRFuture1 = stream.addAsync("2", "2");

        RFuture<Map<StreamMessageId, Map<String, String>>> groupResultFuture = stream.readGroupAsync("testGroup", "consumer1");

        //没测出效果
//        RFuture<Long> amountFuture = stream.ackAsync("testGroup", streamMessageIdRFuture.get(), streamMessageIdRFuture1);

//        amountFuture.whenComplete((res, exception) -> {
//            // ...
//        });
    }

    //环形缓冲器
    //Java implementation of Redis based RingBuffer implements java.util.Queue interface. This structure evicts elements from the head if queue capacity became full.
    //Should be initialized with capacity size by trySetCapacity() method before usage
    @Test
    public void testRingBuffer(){
        RRingBuffer<Integer> buffer = redisson.getRingBuffer("ringBuffer");

        // buffer capacity is 4 elements
        buffer.trySetCapacity(4);

        buffer.add(1);
        buffer.add(2);
        buffer.add(3);
        buffer.add(4);
        // buffer state is 1, 2, 3, 4

        buffer.add(5);
        buffer.add(6);
        // buffer state is 3, 4, 5, 6
    }

    @Test
    public void testAsyncRingBuffer(){
        RRingBuffer<Integer> buffer = redisson.getRingBuffer("ringBuffer");

        // buffer capacity is 4 elements
        RFuture<Boolean> capacityFuture = buffer.trySetCapacityAsync(4);

        RFuture<Boolean> addFuture1 = buffer.addAsync(1);
        RFuture<Boolean> addFuture2 = buffer.addAsync(2);
        RFuture<Boolean> addFuture3 = buffer.addAsync(3);
        RFuture<Boolean> addFuture4 = buffer.addAsync(4);
        // buffer state is 1, 2, 3, 4

        RFuture<Boolean> addFuture5 = buffer.addAsync(5);
        RFuture<Boolean> addFuture6 = buffer.addAsync(6);
        // buffer state is 3, 4, 5, 6

        addFuture6.whenComplete((res, exception) -> {
            // ...
        });
    }


    //传输队列
    //Java implementation of Redis based TransferQueue implements java.util.concurrent.TransferQueue interface. Provides set of transfer methods which return only when value was successfully hand off to consumer.
    //poll and take methods are resubscribed automatically during re-connection to Redis server or failover
    @Test
    public void testTransferQueue() throws InterruptedException {
        RTransferQueue<String> queue = redisson.getTransferQueue("transferQueue");

//        queue.transfer("data"); //等待, 仅在值成功移交给消费者时才返回
        // or try transfer immediately
//        queue.tryTransfer("data");  //立即尝试转移
        // or try transfer up to 10 seconds
        queue.tryTransfer("data", 10, TimeUnit.SECONDS); //尝试传输最多 10 秒
    }

    @Test
    public void testTransferQueue2() throws InterruptedException {
        RTransferQueue<String> queue = redisson.getTransferQueue("transferQueue");

        // in other thread or JVM
        String data = queue.take(); //等待, 仅当接收到数据才会结束
        // or
//        String data = queue.poll();  //立即返回数据 null 或 有值
        System.out.println(data);
    }

    @Test
    public void testAsyncTransferQueue() throws InterruptedException {
        RTransferQueue<String> queue = redisson.getTransferQueue("transferQueue");

        RFuture<Void> future = queue.transferAsync("data"); //不等待,直接发送
        // or try transfer immediately
//        RFuture<Boolean> future = queue.tryTransferAsync("data");
        // or try transfer up to 10 seconds
//        RFuture<Boolean> future = queue.tryTransferAsync("data", 10, TimeUnit.SECONDS);
    }

    @Test
    public void testAsyncTransferQueue2() throws InterruptedException, ExecutionException {
        RTransferQueue<String> queue = redisson.getTransferQueue("transferQueue");
        // in other thread or JVM
        RFuture<String> future = queue.takeAsync(); //等待,有值才会向下进行
        // or
//        RFuture<String> future = queue.pollAsync();

        future.await(2,TimeUnit.SECONDS);

        future.whenComplete((res, exception) -> {
            System.out.println(res);
            // ...
        });
    }

    @Test
    public void testTimeSeries() throws InterruptedException {
        RTimeSeries<String> ts = redisson.getTimeSeries("timeSeries");

        ts.add(201908110501L, "10%");
        ts.add(201908110502L, "30%");
        ts.add(201908110504L, "10%");
        ts.add(201908110508L, "75%");

        // entry time-to-live is 10 seconds
        ts.add(201908110510L, "85%", 10, TimeUnit.SECONDS);
        ts.add(201908110510L, "95%", 10, TimeUnit.SECONDS);

        String value = ts.get(201908110508L); //75%
        ts.remove(201908110508L);

        String value1 = ts.get(201908110510L); //85%

        Collection<String> values = ts.pollFirst(2); //85% 95%
        Collection<String> range = ts.range(201908110501L, 201908110508L); //10% 30% 10%

        Thread.sleep(15000);
        String value2 = ts.get(201908110510L); //null
    }

    @Test
    public void testAsyncTimeSeries() throws InterruptedException {
        RTimeSeries<String> ts = redisson.getTimeSeries("timeSeries");

        ts.addAsync(201908110501L, "10%");
        ts.addAsync(201908110502L, "30%");
        ts.addAsync(201908110504L, "10%");
        ts.addAsync(201908110508L, "75%");

        // entry time-to-live is 10 seconds
        ts.addAsync(201908110510L, "85%", 10, TimeUnit.SECONDS);
        ts.addAsync(201908110510L, "95%", 10, TimeUnit.SECONDS);

        ts.getAsync(201908110508L);
        ts.removeAsync(201908110508L);

        ts.pollFirstAsync(2);
        RFuture<Collection<String>> future = ts.rangeAsync(201908110501L, 201908110508L);

        future.await(5,TimeUnit.SECONDS);

        future.whenComplete((res, exception) -> {
            System.out.println(res); // [10%, 30%, 10%]
            // ...
        });
    }


}
