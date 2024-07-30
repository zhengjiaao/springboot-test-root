package com.zja.gis.supermap.xgis.licence;

import com.supermap.data.Workspace;
import com.zja.gis.supermap.xgis.util.SuperMapLicenceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-17 14:54
 */
@Service
public class SuperMapLicenceService {

    @Bean
    public Workspace checkAndGetWorkSpace() {
        return SuperMapLicenceUtil.checkAndGetWorkSpace();
    }
}
