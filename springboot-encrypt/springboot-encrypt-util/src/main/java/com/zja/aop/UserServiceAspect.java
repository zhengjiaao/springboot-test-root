package com.zja.aop;

import com.zja.base.dto.PageDTO;
import com.zja.entity.User;
import com.zja.security.IEncrypt;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 注意使用过滤器和拦截器的区别 过滤 就是对原有数据进行筛选过滤 ，
 * 拦截是对正常执行过程进行拦截干预，这里使用了 切面进行编程
 * 对service 方法进行干预 ，结合使用注解 可以做到可配置的功能
 * Created by Administrator on 2018/12/26.
 * @author  xupp
 * @date  2018/12/27
 */

@Aspect
@Component
public class UserServiceAspect {

    @Autowired
    @Qualifier("systemEncrypt")
    IEncrypt encrypt;

    @Pointcut("execution(public * com.zja.service.impl.AOPEncryptImpl.*(..)) && @annotation(com.zja.annotation.DREncrypt)" )
    public void addAdvice(){}

    /**
     * 返回值加密，前端解密
     * @param data
     * @throws Exception
     */
    @AfterReturning(returning = "data",pointcut = "addAdvice()")
    public void encryptAspect(Object data) throws Exception {
        if(data==null){
            return;
        }
        if(data instanceof User){
            ((User) data).setLoginpwd(this.encrypt.encrypt(((User) data).getLoginpwd()));
        }else if(data instanceof PageDTO){
            List<User> users= ((PageDTO) data).getContent();
            for(User user:users){
                user.setLoginpwd(this.encrypt.encrypt(user.getLoginpwd()));
            }
        }else if(data instanceof List){
            if(((List) data).get(0) instanceof User){
                for(User user:(List<User>)data){
                    user.setLoginpwd(this.encrypt.encrypt(user.getLoginpwd()));
                }
            }
        }else if(data instanceof StringBuffer){
            String temp=this.encrypt.encrypt(data.toString());
//            清空
            ((StringBuffer) data).delete(0,((StringBuffer) data).length());
//            重新赋值
            ((StringBuffer) data).append(temp);
        }
    }

}
