/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:08
 * @Since:
 */
package com.zja.task;

import com.zja.task.service.ActivityService;

/**活动观察者
 * @author: zhengja
 * @since: 2023/06/14 16:08
 */
public class ActivityObserver implements Observer {
    private ActivityService activityService;
    @Override
    public void response(Long taskId) {
        activityService.notifyFinished(taskId);
    }
}
