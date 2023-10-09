/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:30
 * @Since:
 */
package com.zja.command.textEditor;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:30
 */
// Command
interface Command {
    void execute();
    void undo();
}
