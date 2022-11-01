/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-23 17:30
 * @Since:
 */
package com.zja.service;

import com.zja.tangram.AbstractTangram;
import com.zja.tangram.ProcessService;
import com.zja.tangram.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 规委在线 流程管理
 */
@Service
public class GwzxTangramService extends AbstractTangram {

    @Value("${tangram.gwzx.process_id}")
    public String gwzxProcessId;

    @Autowired
    ProcessService processService;
    @Autowired
    ProjectService projectService;

    public GwzxTangramService() {
        super.processService = processService;
        super.projectService = projectService;
        super.processId = gwzxProcessId;
    }

}
