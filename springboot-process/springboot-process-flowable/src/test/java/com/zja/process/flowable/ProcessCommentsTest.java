package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-09-01 20:31
 */
public class ProcessCommentsTest extends ProcessFlowableApplicationTests {

    @Autowired
    private TaskService taskService;

    static final String processInstanceId = "92fdb871-872b-11f0-90b7-8245ddc034b5";

    // 在 Flowable 中设置评论（Comment）
    @Test
    public void testAddComments() {
        // String processInstanceId = "your-process-instance-id"; // 一定是真实存在的
        String taskId = "your-task-id"; // 一定是真实存在的

        // 为特定任务添加评论
        // taskService.addComment(taskId, processInstanceId, "审批通过，同意申请");

        // 添加带用户ID的评论
        // taskService.addComment(taskId, processInstanceId, "admin", "请补充相关材料");

        // 添加仅关联流程实例的评论
        taskService.addComment(null, processInstanceId, "流程已启动");

        // 验证评论是否添加成功
        List<Comment> taskComments = taskService.getTaskComments(taskId);
        List<Comment> processComments = taskService.getProcessInstanceComments(processInstanceId);

        System.out.println("任务评论数量: " + taskComments.size());
        System.out.println("流程评论数量: " + processComments.size());
    }

    // 在实际业务场景中添加评论的示例
    @Test
    public void testBusinessScenarioWithComments() {
        // 假设这是处理任务的业务场景
        String processInstanceId = "your-process-instance-id";
        String taskId = "current-task-id";

        try {
            // 添加处理意见
            taskService.addComment(taskId, processInstanceId, "审核人", "资料齐全，符合要求");

            // 完成任务
            taskService.complete(taskId);

            System.out.println("任务处理完成并添加评论");
        } catch (Exception e) {
            // 处理异常时添加评论
            taskService.addComment(taskId, processInstanceId, "系统", "任务处理异常: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 获取整个流程实例的所有评论
     */
    @Test
    public void getAllCommentsByProcessInstanceId() {
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId); // 使用 TaskService 查询流程实例评论

        for (Comment comment : comments) {
            System.out.println("评论ID：" + comment.getId());
            System.out.println("用户ID：" + comment.getUserId());
            System.out.println("时间：" + comment.getTime());
            System.out.println("任务ID：" + comment.getTaskId());
            System.out.println("内容：" + comment.getFullMessage());
            System.out.println("流程实例ID：" + comment.getProcessInstanceId());
            System.out.println("关联的元素类型：" + comment.getType());
            System.out.println();
        }
    }

    /**
     * 获取某个任务的所有评论，查询处理意见（评论/批注）
     */
    @Test
    public void getCommentsByTaskId() {
        String taskId = "current-task-id";
        List<Comment> comments = taskService.getTaskComments(taskId); // 使用 TaskService 查询评论

        for (Comment comment : comments) {
            System.out.println("评论ID：" + comment.getId());
            System.out.println("用户ID：" + comment.getUserId());
            System.out.println("时间：" + comment.getTime());
            System.out.println("任务ID：" + comment.getTaskId());
            System.out.println("内容：" + comment.getFullMessage());
            System.out.println();
        }
    }

}
