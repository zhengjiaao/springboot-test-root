package com.zja.view;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: springbootdemo
 * @Date: 2019/1/8 16:17
 * @Author: Mr.Zheng
 * @Description:
 */
@Data
@Entity
//@Subselect("select o.id,tuser.name"+
//" from t_order o,t_user tuser WHERE o.GUID = tuser.GUID")
//@Synchronize({"Order","UserEntity"})
public class OrderUserView implements Serializable {

    @Id
    private Long id;
    private String name;

}
