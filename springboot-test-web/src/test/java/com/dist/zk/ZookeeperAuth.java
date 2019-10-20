package com.dist.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**Zookeeper 节点授权
 * 这里测试的认证方式：digest  相当于  user:pass 认证
 *
 * @author zhengja@dist.com.cn
 * @data 2019/8/22 15:29
 */
public class ZookeeperAuth implements Watcher {

    /** 连接地址 */
    final static String CONNECT_ADDR = "127.0.0.1:2182";
    /** 测试路径 */
    final static String PATH = "/dubbo";
    final static String PATH_DEL = "/dubbo/deldubbo";
    /** 认证类型 */
    final static String authentication_type = "digest";
    /** 认证正确方法 */
    final static String correctAuthentication = "123456";
    /** 认证错误方法 */
    final static String badAuthentication = "654321";

    static ZooKeeper zk = null;
    /** 计时器 */
    AtomicInteger seq = new AtomicInteger();
    /** 标识 */
    private static final String LOG_PREFIX_OF_MAIN = "【Main】";

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (event==null) {
            return;
        }
        // 连接状态
        Event.KeeperState keeperState = event.getState();
        // 事件类型
        Event.EventType eventType = event.getType();
        // 受影响的path
        String path = event.getPath();

        String logPrefix = "【Watcher-" + this.seq.incrementAndGet() + "】";

        System.out.println(logPrefix + "收到Watcher通知");
        System.out.println(logPrefix + "连接状态:\t" + keeperState.toString());
        System.out.println(logPrefix + "事件类型:\t" + eventType.toString());
        if (Event.KeeperState.SyncConnected == keeperState) {
            // 成功连接上ZK服务器
            if (Event.EventType.None == eventType) {
                System.out.println(logPrefix + "成功连接上ZK服务器");
                connectedSemaphore.countDown();
            }
        } else if (Event.KeeperState.Disconnected == keeperState) {
            System.out.println(logPrefix + "与ZK服务器断开连接");
        } else if (Event.KeeperState.AuthFailed == keeperState) {
            System.out.println(logPrefix + "权限检查失败");
        } else if (Event.KeeperState.Expired == keeperState) {
            System.out.println(logPrefix + "会话失效");
        }
        System.out.println("--------------------------------------------");
    }
    /**
     * 创建ZK连接
     *
     * @param connectString
     *            ZK服务器地址列表
     * @param sessionTimeout
     *            Session超时时间
     */
    public void createConnection(String connectString, int sessionTimeout) {
        this.releaseConnection();
        try {
            zk = new ZooKeeper(connectString, sessionTimeout, this);
            //添加节点授权
            zk.addAuthInfo(authentication_type,correctAuthentication.getBytes());
            System.out.println(LOG_PREFIX_OF_MAIN + "开始连接ZK服务器");
            //倒数等待
            connectedSemaphore.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭ZK连接
     */
    public void releaseConnection() {
        if (this.zk!=null) {
            try {
                this.zk.close();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     *
     * <B>方法名称：</B>测试函数<BR>
     * <B>概要说明：</B>测试认证<BR>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        ZookeeperAuth testAuth = new ZookeeperAuth();
        testAuth.createConnection(CONNECT_ADDR,2000);
        //acls设置具有的权限：READ | WRITE | CREATE | DELETE | ADMIN   包括：读、写、创建、删除、添加修改
        List<ACL> acls = new ArrayList<ACL>(1);
        for (ACL ids_acl : ZooDefs.Ids.CREATOR_ALL_ACL) {
            acls.add(ids_acl);
        }

        try {
            zk.create(PATH, "init content".getBytes(), acls, CreateMode.PERSISTENT); //PERSISTENT：持久性,在客户端断开连接时，不会自动删除znode
            System.out.println("使用授权key：" + correctAuthentication + "创建节点："+ PATH + ", 初始内容是: init content");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            zk.create(PATH_DEL, "will be deleted! ".getBytes(), acls, CreateMode.PERSISTENT);
            System.out.println("使用授权key：" + correctAuthentication + "创建节点："+ PATH_DEL + ", 初始内容是: will be deleted!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getDataByCorrectAuthentication();  //采用正确的密码
        // 获取数据
        getDataByNoAuthentication();  //获取数据：不采用密码
        getDataByBadAuthentication(); //获取数据：采用错误的密码
        getDataByCorrectAuthentication();  //采用正确的密码

        // 更新数据
        updateDataByNoAuthentication();  //更新数据：不采用密码
        updateDataByBadAuthentication();  //更新数据：采用错误的密码
        updateDataByCorrectAuthentication();  //更新数据：采用正确的密码

        // 删除数据
        deleteNodeByNoAuthentication();  //不使用密码 删除节点
        deleteNodeByBadAuthentication(); //采用错误的密码删除节点
        deleteNodeByCorrectAuthentication();  //使用正确的密码删除节点

        //线程等待
        Thread.sleep(1000);

        //使用正确的密码删除父节点
        deleteParent();

        //释放连接
        testAuth.releaseConnection();
    }

    /** 获取数据：采用错误的密码 */
    static void getDataByBadAuthentication() {
        String prefix = "[使用错误的授权信息]";
        try {
            ZooKeeper badzk = new ZooKeeper(CONNECT_ADDR, 2000, null);
            //授权
            badzk.addAuthInfo(authentication_type,badAuthentication.getBytes());
            Thread.sleep(2000);
            System.out.println(prefix + "获取数据：" + PATH);
            System.out.println(prefix + "成功获取数据：" + badzk.getData(PATH, false, null));
        } catch (Exception e) {
            System.err.println(prefix + "获取数据失败，原因：" + e.getMessage());
        }
    }

    /** 获取数据：不采用密码 */
    static void getDataByNoAuthentication() {
        String prefix = "[不使用任何授权信息]";
        try {
            System.out.println(prefix + "获取数据：" + PATH);
            ZooKeeper nozk = new ZooKeeper(CONNECT_ADDR, 2000, null);
            Thread.sleep(2000);
            System.out.println(prefix + "成功获取数据：" + nozk.getData(PATH, false, null));
        } catch (Exception e) {
            System.err.println(prefix + "获取数据失败，原因：" + e.getMessage());
        }
    }

    /** 采用正确的密码 */
    static void getDataByCorrectAuthentication() {
        String prefix = "[使用正确的授权信息]";
        try {
            System.out.println(prefix + "获取数据：" + PATH);

            System.out.println(prefix + "成功获取数据：" + zk.getData(PATH, false, null));
        } catch (Exception e) {
            System.out.println(prefix + "获取数据失败，原因：" + e.getMessage());
        }
    }

    /**
     * 更新数据：不采用密码
     */
    static void updateDataByNoAuthentication() {

        String prefix = "[不使用任何授权信息]";

        System.out.println(prefix + "更新数据： " + PATH);
        try {
            ZooKeeper nozk = new ZooKeeper(CONNECT_ADDR, 2000, null);
            Thread.sleep(2000);
            Stat stat = nozk.exists(PATH, false);
            if (stat!=null) {
                nozk.setData(PATH, prefix.getBytes(), -1);
                System.out.println(prefix + "更新成功");
            }
        } catch (Exception e) {
            System.err.println(prefix + "更新失败，原因是：" + e.getMessage());
        }
    }

    /**
     * 更新数据：采用错误的密码
     */
    static void updateDataByBadAuthentication() {

        String prefix = "[使用错误的授权信息]";

        System.out.println(prefix + "更新数据：" + PATH);
        try {
            ZooKeeper badzk = new ZooKeeper(CONNECT_ADDR, 2000, null);
            //授权
            badzk.addAuthInfo(authentication_type,badAuthentication.getBytes());
            Thread.sleep(2000);
            Stat stat = badzk.exists(PATH, false);
            if (stat!=null) {
                badzk.setData(PATH, prefix.getBytes(), -1);
                System.out.println(prefix + "更新成功");
            }
        } catch (Exception e) {
            System.err.println(prefix + "更新失败，原因是：" + e.getMessage());
        }
    }

    /**
     * 更新数据：采用正确的密码
     */
    static void updateDataByCorrectAuthentication() {

        String prefix = "[使用正确的授权信息]";

        System.out.println(prefix + "更新数据：" + PATH);
        try {
            Stat stat = zk.exists(PATH, false);
            if (stat!=null) {
                zk.setData(PATH, prefix.getBytes(), -1);
                System.out.println(prefix + "更新成功");
            }
        } catch (Exception e) {
            System.err.println(prefix + "更新失败，原因是：" + e.getMessage());
        }
    }

    /**
     * 不使用密码 删除节点
     */
    static void deleteNodeByNoAuthentication() throws Exception {

        String prefix = "[不使用任何授权信息]";

        try {
            System.out.println(prefix + "删除节点：" + PATH_DEL);
            ZooKeeper nozk = new ZooKeeper(CONNECT_ADDR, 2000, null);
            Thread.sleep(2000);
            Stat stat = nozk.exists(PATH_DEL, false);
            if (stat!=null) {
                nozk.delete(PATH_DEL,-1);
                System.out.println(prefix + "删除成功");
            }
        } catch (Exception e) {
            System.err.println(prefix + "删除失败，原因是：" + e.getMessage());
        }
    }

    /**
     * 采用错误的密码删除节点
     */
    static void deleteNodeByBadAuthentication() throws Exception {

        String prefix = "[使用错误的授权信息]";

        try {
            System.out.println(prefix + "删除节点：" + PATH_DEL);
            ZooKeeper badzk = new ZooKeeper(CONNECT_ADDR, 2000, null);
            //授权
            badzk.addAuthInfo(authentication_type,badAuthentication.getBytes());
            Thread.sleep(2000);
            Stat stat = badzk.exists(PATH_DEL, false);
            if (stat!=null) {
                badzk.delete(PATH_DEL, -1);
                System.out.println(prefix + "删除成功");
            }
        } catch (Exception e) {
            System.err.println(prefix + "删除失败，原因是：" + e.getMessage());
        }
    }

    /**
     * 使用正确的密码删除节点
     */
    static void deleteNodeByCorrectAuthentication() throws Exception {

        String prefix = "[使用正确的授权信息]";

        try {
            System.out.println(prefix + "删除节点：" + PATH_DEL);
            Stat stat = zk.exists(PATH_DEL, false);
            if (stat!=null) {
                zk.delete(PATH_DEL, -1);
                System.out.println(prefix + "删除成功");
            }
        } catch (Exception e) {
            System.out.println(prefix + "删除失败，原因是：" + e.getMessage());
        }
    }

    /**
     * 使用正确的密码删除父节点
     */
    static void deleteParent() throws Exception {
        String prefix = "[使用正确的授权信息]";
        try {
            Stat stat = zk.exists(PATH_DEL, false);
            if (stat == null) {
                zk.delete(PATH, -1);
            }
        } catch (Exception e) {
            System.out.println(prefix + "删除父节点失败，原因是：" + e.getMessage());
            e.printStackTrace();
        }
    }
}