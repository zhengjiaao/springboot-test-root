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
 * 贷款流程审批人-初级贷款审批人
 *
 * @author: zhengja
 * @since: 2023/10/09 16:05
 */
public class JuniorLoanApprover extends LoanApprover {
    private static final double MAX_JUNIOR_APPROVAL_AMOUNT = 10000.0;

    @Override
    protected boolean canApproveLoan(double amount) {
        return amount <= MAX_JUNIOR_APPROVAL_AMOUNT;
    }

    @Override
    protected void doApproveLoan(double amount) {
        System.out.println("Loan approved by Junior Loan Approver. Amount: $" + amount);
    }
}
