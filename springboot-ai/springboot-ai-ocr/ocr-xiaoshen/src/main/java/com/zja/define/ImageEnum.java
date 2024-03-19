/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 11:21
 * @Since:
 */
package com.zja.define;

/**
 * @author: zhengja
 * @since: 2023/11/08 11:21
 */
public enum ImageEnum {
    jpg,
    png,
    gif,
    bmp;

    public static ImageEnum get(String name) {
        try {
            return ImageEnum.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }
}
