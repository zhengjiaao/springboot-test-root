package com.dist.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ChiV3n
 * @date 2017/12/18.
 * desc 票据信息entity，存储票据信息
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TicketEntity implements Serializable{

    private static String datePattern = "yyyy-MM-dd hh:MM:ss";

    private String loginName;
    private String ticketNo;

    private long ticketCreateTime;

    private String createTime;

    private UserEntity userEntity;

    @JsonIgnore
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getCreateTime(){
        return createTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    @JsonIgnore
    public long getTicketCreateTime() {
        return ticketCreateTime;
    }

    public void setTicketCreateTime(long ticketCreateTime) {
        Date date = new Date(ticketCreateTime);
        this.createTime = new SimpleDateFormat(datePattern).format(date);
        this.ticketCreateTime = ticketCreateTime;
    }
}
