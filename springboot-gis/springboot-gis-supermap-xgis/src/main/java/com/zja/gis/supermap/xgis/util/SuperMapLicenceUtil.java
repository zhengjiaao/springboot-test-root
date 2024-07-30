/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-09 9:53
 * @Since:
 */
package com.zja.gis.supermap.xgis.util;

import com.supermap.data.Workspace;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: zhengja
 * @since: 2023/02/09 9:53
 */
@Slf4j
public class SuperMapLicenceUtil {

    /**
     * 检查是否支持超图的环境，如果支持，初始化Workspace时，是不会报错的
     */
    public static Workspace checkAndGetWorkSpace() {
        try {
            final Workspace workspace = new Workspace();
            log.info("当前环境支持超图，workSpace=" + workspace);
            return workspace;
        } catch (Exception | Error ignored) {
            ignored.printStackTrace();
            if (ignored instanceof Error) {
                log.warn("提示, 当前应用需要部署超图环境: 1.检查是否部署超图环境，并挂载超图环境目录 2.检测超图环境版本，必须是：[10.2.0] ，错误信息可能是：no WrapjGeo in java.library.path");
            }

        }
        return null;
    }

    /**
     * 校验超图环境，当不存在超图环境，则抛异常
     */
    public static Workspace preCheckAndGetWorkSpace() {
        Workspace workspace = SuperMapLicenceUtil.checkAndGetWorkSpace();
        if (workspace == null) {
            throw new RuntimeException("提示, 当前应用需要部署超图环境: 1.检查是否部署超图环境，并挂载超图环境目录 2.检测超图环境版本，必须是：[10.2.0]");
        }
        return workspace;
    }

    // 无效验证，必须启动项目才能验证
/*    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        preCheckAndGetWorkSpace();
    }*/
}
