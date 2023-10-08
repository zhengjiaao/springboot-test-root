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
 * ImageProxy 是代理类
 *
 * @author: zhengja
 * @since: 2023/10/08 15:07
 */
// 代理类
class ImageProxy implements Image {
    private RealImage realImage;
    private String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    //ImageProxy 代理类在 display() 方法中创建并管理真实的目标类对象。
    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}