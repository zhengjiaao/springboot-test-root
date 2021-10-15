package com.zja.test;

import com.zja.SpringbootTestApplication;
import com.zja.constant.Operator;
import com.zja.entity.Calculator;
import com.zja.factory.BonusStrategyFactory;
import com.zja.service.BonusService;
import com.zja.service.Impl.AddCommand;
import com.zja.service.Operation;
import com.zja.service.OperatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 21:42
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringbootTestApplication.class})
public class TestOperator {

    /**
     * 用模式 去掉if else
     *
     * @param
     */
    @Test
    public void test() {

        //方式一  工厂模式
        int add = calculateUsingFactory(2, 4, "add");
        System.out.println("工厂=" + add);

        //方式二 工厂+枚举
        Calculator calculator = new Calculator();
        int result = calculator.calculate(3, 4, Operator.valueOf("MULTIPLY"));
        System.out.println("枚举=" + result);

        //命令模式
        int addCommand = calculator.calculate(new AddCommand(3, 7));
        System.out.println("命令模式=" + addCommand);

        //工厂模式+策略模式  推荐
        String bonusPlan = getBonusPlan(300);
        System.out.println("工厂+策略=" + bonusPlan);
    }

    //工厂模式  并没有减少if语句，只是增加了可读性
    public int calculateUsingFactory(int a, int b, String operator) {
        Operation targetOperation = OperatorFactory
                .getOperation(operator)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Operator"));
        return targetOperation.apply(a, b);
    }

    //工厂模式+策略模式
    public String getBonusPlan(Integer bonus) {
        //  在工厂里通过奖金拿到对应的使用策略类
        BonusService strategyService = BonusStrategyFactory.getByBonus(bonus);
        //  调用策略类相应的方法
        return strategyService.useBonusPlan(bonus);
    }


}
