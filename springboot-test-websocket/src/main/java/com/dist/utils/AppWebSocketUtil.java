package com.dist.utils;

import org.java_websocket.WebSocket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**app设备版本推送信息， 在线设备-工具类
 * @author zhengja@dist.com.cn
 * @data 2019/4/15 15:03
 */
public class AppWebSocketUtil {

    //新:优化-保证多线程安全同时方便利用map.get(userId)进行推送到指定端口
    private static ConcurrentHashMap<WebSocket,String> deviceConnections = new ConcurrentHashMap<>();

    /**
     * 添加设备连接
     * @param deviceCode
     * @param conn
     */
    public static void addDeviceConnections(String deviceCode, WebSocket conn) {
        deviceConnections.put(conn, deviceCode);
    }

    /**
     * 删除设备连接
     * @param conn
     * @return
     */
    public static boolean removeDeviceConnections(WebSocket conn) {
        if (deviceConnections.containsKey(conn)) {
            deviceConnections.remove(conn); // 移除连接
            return true;
        } else{
            return false;
        }
    }

    /**
     * 获取所有设备连接
     * @return
     */
    public static Set<WebSocket> getAllWebSocket() {
        return deviceConnections.keySet();
    }

    /**
     * 根据连接获取在线设备
     * @param conn
     * @return
     */
    public static String getDeviceByWebSocket(WebSocket conn) {
        return deviceConnections.get(conn);
    }

    /**
     * 获取所有在线设备
     * @return
     */
    public static Collection<String> getAllDevices() {
        List<String> deviceCollection = new ArrayList<String>();
        Collection<String> deviceCodes = deviceConnections.values();
        for (String deviceCode: deviceCodes) {
            deviceCollection.add(deviceCode);
        }
        return deviceCollection;
    }

    /**
     * 获取在线设备数量
     * @return
     */
    public static int getDevicesCount() {
        return deviceConnections.size();
    }

    /**
     * 根据设备获取连接
     * @param deviceCode
     * @return
     */
    public static WebSocket getWebSocketByDevice(String deviceCode) {
        Set<WebSocket> keySet = deviceConnections.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String cuser = deviceConnections.get(conn);
                if (cuser.equals(deviceCode)) {
                    return conn;
                }
            }
        }
        return null;
    }

    /**
     * 向指定在线设备发送消息
     * @param conn
     * @param message
     * @return
     */
    public static void sendMessageToOnlineDevice(WebSocket conn, String message) {
        if (null != conn && null != deviceConnections.get(conn)) {
            conn.send(message);
        }
    }

    /**
     * 向所有在线设备发送信息
     * @param message
     */
    public static void sendMessageToOnlineAllDevice(String message) {
        Set<WebSocket> keySet = deviceConnections.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String deviceCode = deviceConnections.get(conn);
                if (deviceCode != null) { conn.send(message);
                }
            }
        }
    }
}
