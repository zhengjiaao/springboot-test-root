/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:18
 * @Since:
 */
package com.zja.adapter.plug;

/**
 * 适配器类实现中国插座接口，同时持有英国插头的实例，并将英国插头的方法适配到中国插座接口
 *
 * @author: zhengja
 * @since: 2023/10/08 13:18
 */
public class UKPlugAdapter implements ChinaSocket {
    private UKPlug ukPlug;

    public UKPlugAdapter(UKPlug ukPlug) {
        this.ukPlug = ukPlug;
    }

    @Override
    public void insertChinaPlug() {
        ukPlug.insertUKPlug();
        System.out.println("使用适配器将英国插头适配到中国插座");
    }
}
