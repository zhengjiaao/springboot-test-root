package com.zja.service;

import com.zja.utils.TestWebSocketUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Set;

/**
 * WebSocket推送测试  端口： 8887
 * @author zhengja@dist.com.cn
 * @data 2019/4/9 10:42
 */
public class TestChatWebSocketServer extends WebSocketServer {

    //登录对象-推送对象
    private String username;

    //移动版本信息服务模块
    /*@Reference
    MobileAppVersionService appVersionService;*/

    public TestChatWebSocketServer() throws Exception{
    }

    public TestChatWebSocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public TestChatWebSocketServer(InetSocketAddress address) {
        super(address);
        System.out.println("地址："+address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        System.out.println("连接："+conn);

        sendToAll(conn.getRemoteSocketAddress().getAddress().getHostAddress()
                + " 进入房间 ！");

        System.out.println(conn.getRemoteSocketAddress().getAddress()
                .getHostAddress()
                + " 进入房间 ！");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

        sendToAll(conn.getRemoteSocketAddress().getAddress().getHostAddress()
                + " 离开房间 ！");

        System.out.println(conn.getRemoteSocketAddress().getAddress()
                .getHostAddress()
                + " 离开房间 ！");

        //触发关闭事件
        userLeave(conn);
    }

    //消息发送
    @Override
    public void onMessage(WebSocket conn, String message) {

        //判断是否是第一次接收的消息
        boolean isfirst = true;
        System.out.println("消息："+message);
        /*sendToAll("["
                + conn.getRemoteSocketAddress().getAddress().getHostAddress()
                + "]" + message);*/

        /*System.out.println("["
                + conn.getRemoteSocketAddress().getAddress().getHostAddress()
                + "]" + message);*/

        //判断是否已在连接池中
        Set<WebSocket> webSockets= TestWebSocketUtil.getAllWebSocket();
        for (WebSocket webSocket : webSockets){
            if (webSocket.equals(conn)){
                isfirst =false;
            }
        }

        if (isfirst) {
            this.username = message;
            //客户端发送消息到服务器是触发事件
            if (message != null){
                //判断用户是否已经在线
                WebSocket webSocketByUser = TestWebSocketUtil.getWebSocketByUser(message);
                if (null == webSocketByUser){
                //将用户加入
                this.userJoin(username, conn);
                System.out.println("用户" + username + "上线,在线人数：" + TestWebSocketUtil.getUserCount());
                }else {
                    TestWebSocketUtil.sendMessageToOnlineUser(conn,"["+username+"] 用户已在线,请您换个用户登录！");
                }
            }
        } else {
            String[] msg = message.split("@", 2);//以@为分隔符把字符串分为xxx和xxxxx两部分,msg[0]表示发送至的用户名，all则表示发给所有人
            if (msg[0].equals("all")) {
                sendToAll(msg[1]);
            } else {
                //指定用户发送消息
                sendMessageToUser(conn,msg[0], msg[1]);
            }
        }


    }

    //异常抛出
    @Override
    public void onError(WebSocket conn, Exception e) {
        e.printStackTrace();
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * 用户下线处理
     * @param conn
     */
    public void userLeave(org.java_websocket.WebSocket conn) {
        String user = TestWebSocketUtil.getUserByKey(conn);
        boolean b = TestWebSocketUtil.removeUser(conn); // 在连接池中移除连接
        if (b) {
            TestWebSocketUtil.sendMessageToOnlineAllUser(user); // 把当前用户从所有在线用户列表中删除
            String leaveMsg = "[系统]" + user + "下线了";
            System.out.println(leaveMsg);
            TestWebSocketUtil.sendMessageToOnlineAllUser(leaveMsg); // 向在线用户发送当前用户退出的信息
        }
    }

    /**
     * 用户上线处理
     * @param user
     * @param conn
     */
    public void userJoin(String user, org.java_websocket.WebSocket conn) {
        TestWebSocketUtil.sendMessageToOnlineAllUser(user); // 把当前用户加入到所有在线用户列表中
        String joinMsg = "[系统]" + user + "上线了！";
        System.out.println(joinMsg);
        TestWebSocketUtil.sendMessageToOnlineAllUser(joinMsg); // 向所有在线用户推送当前用户上线的消息
        TestWebSocketUtil.addUser(user, conn); // 向连接池添加当前的连接的对象
        TestWebSocketUtil.sendMessageToOnlineUser(conn, TestWebSocketUtil.getOnlineUser().toString());  // 向当前连接发送当前在线用户的列表
        //TestWebSocketUtil.sendMessageToOnlineUser(conn,"所有App版本信息："+appVersionService.getAllAppVersions().toString());
    }

    /**
     * 发送消息给指定用户
     * @param user
     * @param message
     */
    public void sendMessageToUser(WebSocket currentConnection,String user,String message){
        //获取在线用户的连接
        WebSocket conn = TestWebSocketUtil.getWebSocketByUser(user);
        if (null != conn){
            //向在线特定用户发送消息
            TestWebSocketUtil.sendMessageToOnlineUser(conn,message);
            //同时发送消息给当前用户
            TestWebSocketUtil.sendMessageToOnlineUser(currentConnection,message);
        }else {
            TestWebSocketUtil.sendMessageToOnlineUser(currentConnection,"["+user+"] 用户不在线,请您稍后发送！");
            System.out.println("["+user+"] 用户不在线,请您稍后发送！");
        }
    }

    // 发送给所有的聊天者
    private void sendToAll(String text) {
        Collection<WebSocket> conns = connections();
        synchronized (conns) {
            for (WebSocket client : conns) {
                client.send(text);
            }
        }
    }

}
