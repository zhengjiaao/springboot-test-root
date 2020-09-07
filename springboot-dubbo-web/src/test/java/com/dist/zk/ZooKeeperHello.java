package com.dist.zk;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/21 15:21
 */
public class ZooKeeperHello {

    /**
     * zookeeper无权限设置节点和获取节点
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws KeeperException
     */
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = new ZooKeeper("192.168.2.113:2183", 300000, new DemoWatcher());//连接zk server
        String node = "/app1";
        Stat stat = zk.exists(node, false);//检测/app1是否存在
        if (stat == null) {
            //创建节点
            String createResult = zk.create(node, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(createResult);
        }
        //获取节点的值
        byte[] b = zk.getData(node, false, stat);
        System.out.println(new String(b));
        zk.close();
    }

    static class DemoWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            System.out.println("----------->");
            System.out.println("path:" + event.getPath());
            System.out.println("type:" + event.getType());
            System.out.println("stat:" + event.getState());
            System.out.println("<-----------");
        }
    }

    /**Spring Boot2.0之 整合Zookeeper集群
     * 普通的连接：无权限设置节点
     */
    @Test
    public void testZkClient(){
        String connection = "192.168.2.113:2183,192.168.2.113:2184,192.168.2.113:2185";
        ZkClient zkClient = new ZkClient(connection);
        zkClient.createPersistent("/toov5_01");  //添加节点
        zkClient.close();
    }

    /**
     * zk设置超级管理员 :适合linx和windows配置的方式
     */
    @Test
    public void testZkSuper(){
        //用户 super:admin
        try {
            String m = DigestAuthenticationProvider.generateDigest("super:admin");
            System.out.println(m);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    //返回的结果 super:xQJmxLMiHGwaqBvst5y6rkB6HQs=

//=======================================以下是设置不同用户具有节点的不同使用权限：读，写/添加，改，删等===================================================

    @Test
    public void testSuperServer() {
        List<ACL> acls = new ArrayList<ACL>(6);
        try {
            Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("user1:123456"));
            ACL acl1 = new ACL(ZooDefs.Perms.WRITE, id1);

            Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("user2:123456"));
            ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);

            Id id3 = new Id("digest", DigestAuthenticationProvider.generateDigest("user3:123456"));
            ACL acl3 = new ACL(ZooDefs.Perms.DELETE, id3);

            Id id4 = new Id("digest", DigestAuthenticationProvider.generateDigest("user4:123456"));
            ACL acl4 = new ACL(ZooDefs.Perms.ADMIN, id4);

            Id id5 = new Id("digest", DigestAuthenticationProvider.generateDigest("user5:123456"));
            ACL acl5 = new ACL(ZooDefs.Perms.CREATE, id5);

            Id id6 = new Id("digest", DigestAuthenticationProvider.generateDigest("user6:123456"));
            ACL acl6 = new ACL(ZooDefs.Perms.ALL, id6);

            acls.add(acl1);
            acls.add(acl2);
            acls.add(acl3);
            acls.add(acl4);
            acls.add(acl5);
            acls.add(acl6);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }

        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper("127.0.0.1:2183,127.0.0.1:2184,127.0.0.1:2185", 300000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });
            if (zk.exists("/test", true) == null) {
                System.out.println(zk.create("/test", "ACL测试".getBytes(), acls, CreateMode.PERSISTENT));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 测试权限-测试配置的超级用户是否成功
     */
    @Test
    public void testSuperClient() {
        try {
            ZooKeeper zk = new ZooKeeper("127.0.0.1:2183,127.0.0.1:2184,127.0.0.1:2185", 300000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });
            //zk 配置的超级用户super:admin
            zk.addAuthInfo("digest", "super:admin".getBytes());
            System.out.println(new String(zk.getData("/test", null, null)));
            zk.setData("/test", "I change！".getBytes(), -1);
            zk.delete("/test",-1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
