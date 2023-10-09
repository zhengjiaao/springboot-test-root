/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:40
 * @Since:
 */
package com.zja.command.order;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:40
 */
public class Client {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        CommandManager commandManager = new CommandManager();

        Order order1 = new Order("1001");
        Order order2 = new Order("1002");

        Command createCommand = new CreateOrderCommand(orderService, order1);
        commandManager.executeCommand(createCommand);

        Command cancelCommand = new CancelOrderCommand(orderService, order1);
        commandManager.executeCommand(cancelCommand);

        Command payCommand = new PayOrderCommand(orderService, order2);
        commandManager.executeCommand(payCommand);

        commandManager.undo(); // 撤销支付命令

        commandManager.undo(); // 撤销取消命令

        //输出结果：
        //Order created: 1001
        //Order cancelled: 1001
        //Order paid: 1002
        //Payment cancelled for order: 1002
        //Order created: 1001
    }
}
