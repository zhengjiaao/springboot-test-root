package com.dist.service;

import com.dist.utils.AppWebSocketUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * 移动版本推送共功能-WebSocket 端口 ： 8989
 * @author zhengja@dist.com.cn
 * @data 2019/4/15 13:58
 */
public class AppVersionWebSocketServer extends WebSocketServer {

    public AppVersionWebSocketServer() throws Exception {
    }
    public AppVersionWebSocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }
    //打开连接 webSocket 连接，clientHandshake 握手
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    }
    //关闭连接
    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        sendToAll(webSocket.getRemoteSocketAddress().getAddress().getHostAddress()+ " 断开连接 ！");
        //断开连接触发
        boolean deviceConnections = AppWebSocketUtil.removeDeviceConnections(webSocket);
        if (deviceConnections){
            System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress()+ " 断开连接成功 ！");
        }else {
            System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress()+ " 连接已经失效 ！");
        }
    }
    //发送消息  前端上线格式 "online"+deviceCode
    @Override
    public void onMessage(WebSocket webSocket, String s) {
        if(null != s && s.startsWith("online")){
            String[] msg = s.split(",", 2);
            AppWebSocketUtil.addDeviceConnections(msg[1],webSocket); //接入到连接池

            sendToAll(webSocket.getLocalSocketAddress().getAddress().getHostAddress()+"连接成功 ！");
            System.out.println("HostAddress:"+webSocket.getLocalSocketAddress().getAddress().getHostAddress()+"连接成功");
        }else if(null != s && s.startsWith("offline")){
            AppWebSocketUtil.removeDeviceConnections(webSocket); //从连接池中移除

            sendToAll(webSocket.getRemoteSocketAddress().getAddress().getHostAddress()+ " 断开连接 ！");
            //断开连接触发
            boolean deviceConnections = AppWebSocketUtil.removeDeviceConnections(webSocket);
            if (deviceConnections){
                System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress()+ " 断开连接成功 ！");
            }else {
                System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress()+ " 连接已经失效 ！");
            }
        }else {
            String[] msg = s.split("@", 2);//以@为分隔符把字符串分为xxx和xxxxx两部分,msg[0]表示发送至的用户名，all则表示发给所有人
            if (msg[0].equals("all")) {
                AppWebSocketUtil.sendMessageToOnlineAllDevice(msg[1]);
            } else {
                //指定用户发送消息
                sendMessageToUser(webSocket,msg[0], msg[1]);
            }
        }
    }
    //异常信息
    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("异常连接：【"+webSocket+"】 地址：【"+webSocket.getRemoteSocketAddress().getAddress().getHostAddress()+"】");
        System.out.println("异常信息："+ e );
    }

    //发送消息给所有的连接者
    private void sendToAll(String text) {
        Collection<WebSocket> conns = connections();
        synchronized (conns) {
            for (WebSocket client : conns) {
                client.send(text);
            }
        }
    }

    /**
     * 发送消息给指定用户
     * @param deviceCode
     * @param message
     */
    public void sendMessageToUser(WebSocket currentConnection,String deviceCode,String message){
        //获取在线用户的连接
        WebSocket conn = AppWebSocketUtil.getWebSocketByDevice(deviceCode);
        if (null != conn){
            String deviceCodeMessage = "接收消息为：【 "+message+" 】";
            //向在线特定用户发送消息
            AppWebSocketUtil.sendMessageToOnlineDevice(conn,deviceCodeMessage);
            //同时发送消息给当前用户
            String currentDeviceCode = "您发送的信息为：【 "+message+" 】";
            AppWebSocketUtil.sendMessageToOnlineDevice(currentConnection,currentDeviceCode);
        }else {
            AppWebSocketUtil.sendMessageToOnlineDevice(currentConnection,"["+deviceCode+"] 设备不在线,请您稍后发送！");
            System.out.println("["+deviceCode+"] 设备不在线,请您稍后发送！");
        }
    }
}
