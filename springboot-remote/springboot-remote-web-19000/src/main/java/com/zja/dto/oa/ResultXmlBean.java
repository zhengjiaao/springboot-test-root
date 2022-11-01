/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-06-30 14:54
 * @Since:
 */
package com.zja.dto.oa;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * OA返回数据结构封装
 * @param <T> 返回数据
 */
@Data
@XmlRootElement(name = "MessageBean")
public class ResultXmlBean<T> {

    //返回代码值 例 200
    private String code;
    //枚举型 包括：success、error、warning
    //请使用type判断是否成功执行
    private String type;
    //返回消息
    private String message;
    //请求服务时传递的唯一标识UUID
    private String token;

    private List<OAOrgUser> data; //正常

    //返回数据内容
//    private T data; //@XmlRootElement T 报错
//    private Object data;  //@XmlRootElement Object 报错

   /* public MessageBean() {
    }

    public MessageBean(String code) {
        this.code = code;
    }

    public MessageBean(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public MessageBean(String message, String code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public MessageBean(String type, String message, String code, T data) {
        this.type = type;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public MessageBean(String type, String message, String code, String token, T data) {
        this.type = type;
        this.message = message;
        this.code = code;
        this.token = token;
        this.data = data;
    }*/
}
