package com.zja.utils.id;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-12-18 15:36
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：对象ID生成器 来源：mongo ObjectId
 */
public class ObjectIdGenerator {
    private static final AtomicInteger nextInc = new AtomicInteger(RandomUtil.randomInt());

    public ObjectIdGenerator() {
    }

    public static boolean isValid(String s) {
        if (s == null) {
            return false;
        } else {
            s = StrUtil.removeAll(s, "-");
            int len = s.length();
            if (len != 24) {
                return false;
            } else {
                for(int i = 0; i < len; ++i) {
                    char c = s.charAt(i);
                    if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public static byte[] nextBytes() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
        bb.putInt((int) DateUtil.currentSeconds());
        bb.putInt(RandomUtil.randomInt());
        bb.putInt(nextInc.getAndIncrement());
        return bb.array();
    }

    public static String next() {
        return next(false);
    }

    public static String next(boolean withHyphen) {
        byte[] array = nextBytes();
        StringBuilder buf = new StringBuilder(withHyphen ? 26 : 24);

        for(int i = 0; i < array.length; ++i) {
            if (withHyphen && i % 4 == 0 && i != 0) {
                buf.append("-");
            }

            int t = array[i] & 255;
            if (t < 16) {
                buf.append('0');
            }

            buf.append(Integer.toHexString(t));
        }

        return buf.toString();
    }

    private static int getMachinePiece() {
        int machinePiece;
        try {
            StringBuilder netSb = new StringBuilder();
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)e.nextElement();
                netSb.append(ni.toString());
            }

            machinePiece = netSb.toString().hashCode() << 16;
        } catch (Throwable var4) {
            machinePiece = RandomUtil.randomInt() << 16;
        }

        return machinePiece;
    }

    private static int getProcessPiece() {
        int processId;
        int atIndex;
        try {
            String processName = ManagementFactory.getRuntimeMXBean().getName();
            atIndex = processName.indexOf(64);
            if (atIndex > 0) {
                processId = Integer.parseInt(processName.substring(0, atIndex));
            } else {
                processId = processName.hashCode();
            }
        } catch (Throwable var5) {
            processId = RandomUtil.randomInt();
        }

        ClassLoader loader = ClassLoaderUtil.getClassLoader();
        atIndex = loader != null ? System.identityHashCode(loader) : 0;
        String processSb = Integer.toHexString(processId) + Integer.toHexString(atIndex);
        int processPiece = processSb.hashCode() & '\uffff';
        return processPiece;
    }
}

