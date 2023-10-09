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
 * 贷款审批人
 *
 * @author: zhengja
 * @since: 2023/10/09 16:05
 */
public abstract class LoanApprover {
    private LoanApprover nextApprover;

    public void setNextApprover(LoanApprover approver) {
        this.nextApprover = approver;
    }

    public void approveLoan(double amount) {
        if (canApproveLoan(amount)) {
            doApproveLoan(amount);
        } else if (nextApprover != null) {
            nextApprover.approveLoan(amount);
        } else {
            System.out.println("Loan cannot be approved.");
        }
    }

    protected abstract boolean canApproveLoan(double amount);

    protected abstract void doApproveLoan(double amount);
}
