package com.dist.constants;

import java.util.concurrent.TimeUnit;

/**
 * 状态枚举
 * @program: dgpoms-server-root
 * @Date: 2018/12/27 11:40
 * @Author: Mr.Zheng
 * @Description:
 */
public abstract  class Status {

    //设备状态
    public static enum DeviceStatusEnum{

        UNREGISTERED(1,"未注册"),
        REGISTERED(2,"已注册"),
        PENDING(3,"待审核"),
        LOSS(4,"挂失"),
        DESTROY(5,"销毁");

        private int code;
        private String desc;

        DeviceStatusEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer code(){
            return this.code;
        }
        public String desc(){
            return this.desc;
        }
    }

    //设备类型
    public static enum DeviceTypeEnum {

        IPHONE(1,"iPhone"),
        IPAD(2,"iPad"),
        ANDROIDPHONE(3,"AndroidPhone"),
        ANDROIDPAD(4,"AndroidPad");

        private int code;
        private String desc;

        DeviceTypeEnum(int code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer code(){
            return this.code;
        }
        public String desc(){
            return this.desc;
        }
    }


    public static enum DeviceConnectionEnum {

        ISFIRST(1,"加入连接池"),
        SPECIFYDEVICE(2,"发送给指定在线设备"),
        ALLDEVICE(3,"发送给所有在线设备"),
        ALLCONNDEVICE(4,"发送给所有已连接设备");

        private int code;
        private String desc;

        DeviceConnectionEnum(int code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer code(){
            return this.code;
        }
        public String desc(){
            return this.desc;
        }
    }

    /**
     * 过期时间相关枚举
     */
    public static enum ExpireEnum{
        //未读消息的有效期为30天
        UNREAD_MSG(30L, TimeUnit.DAYS)
        ;

        /**
         * 过期时间
         */
        private Long time;
        /**
         * 时间单位
         */
        private TimeUnit timeUnit;

        ExpireEnum(Long time, TimeUnit timeUnit) {
            this.time = time;
            this.timeUnit = timeUnit;
        }

        public Long getTime() {
            return time;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }
}
