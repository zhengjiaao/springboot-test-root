/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:07
 * @Since:
 */
package com.zja.proxy.java.static1;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:07
 */
// 使用示例
public class Client {
    public static void main(String[] args) {
        //客户端通过代理对象调用目标对象的方法，代理对象在必要时才创建真实的目标对象，并在调用目标对象的方法之前或之后进行额外的操作。
        Image image = new ImageProxy("image.jpg");
        image.display();
    }
}