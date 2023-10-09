/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:05
 * @Since:
 */
package com.zja.chain.approvalProcess;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:05
 */
//客户端示例
public class Client {
    public static void main(String[] args) {
        // 创建处理者对象
        LoanApprover juniorApprover = new JuniorLoanApprover();
        LoanApprover midLevelApprover = new MidLevelLoanApprover();
        LoanApprover seniorApprover = new SeniorLoanApprover();

        // 设置处理者的顺序
        juniorApprover.setNextApprover(midLevelApprover);
        midLevelApprover.setNextApprover(seniorApprover);

        // 处理贷款申请
        double loanAmount = 75000.0; // 贷款金额
        juniorApprover.approveLoan(loanAmount);

        //输出结果：
        //Loan approved by Senior Loan Approver. Amount: $75000.0
    }
}
