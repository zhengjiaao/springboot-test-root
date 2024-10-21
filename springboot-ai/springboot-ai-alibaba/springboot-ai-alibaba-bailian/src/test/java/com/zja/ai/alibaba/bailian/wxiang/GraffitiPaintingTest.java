package com.zja.ai.alibaba.bailian.wxiang;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-21 15:21
 */
public class GraffitiPaintingTest {

    // 同步调用
    @Test
    public void test_1() {
        syncCall();
    }

    // 异步调用
    @Test
    public void test_2() {
        asyncCall();
    }

    // 由于本模型请求处理时间较长，服务采用异步方式提供，SDK进行了封装。您既可以按异步方式调用，也可以按照同步方式调用。
    public void syncCall() {
        String prompt = "一棵参天大树";
        String sketchImageUrl = "https://help-static-aliyun-doc.aliyuncs.com/assets/img/zh-CN/6609471071/p743851.jpg";
        String model = "wanx-sketch-to-image-lite";
        ImageSynthesisParam param = ImageSynthesisParam.builder()
                .model(model)
                .prompt(prompt)
                .n(1)
                .size("768*768")
                .sketchImageUrl(sketchImageUrl)
                .style("<watercolor>")
                .build();

        String task = "image2image";
        ImageSynthesis imageSynthesis = new ImageSynthesis(task);
        ImageSynthesisResult result = null;
        try {
            System.out.println("---sync call, please wait a moment----");
            result = imageSynthesis.call(param);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
    }

    public void asyncCall() {
        System.out.println("---create task----");
        String taskId = this.createAsyncTask();
        System.out.println("---wait task done then return result----");
        this.waitAsyncTask(taskId);
    }

    public String createAsyncTask() {
        String prompt = "一棵参天大树";
        String sketchImageUrl = "https://help-static-aliyun-doc.aliyuncs.com/assets/img/zh-CN/6609471071/p743851.jpg";
        String model = "wanx-sketch-to-image-lite";
        ImageSynthesisParam param = ImageSynthesisParam.builder()
                .model(model)
                .prompt(prompt)
                .n(1)
                .size("768*768")
                .sketchImageUrl(sketchImageUrl)
                .style("<watercolor>")
                .build();

        String task = "image2image";
        ImageSynthesis imageSynthesis = new ImageSynthesis(task);
        ImageSynthesisResult result = null;
        try {
            System.out.println("---sync call, please wait a moment----");
            result = imageSynthesis.asyncCall(param);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        String taskId = result.getOutput().getTaskId();
        System.out.println("taskId=" + taskId);
        return taskId;
    }

    public void waitAsyncTask(String taskId) {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            result = imageSynthesis.wait(taskId, null);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println(JsonUtils.toJson(result));
    }

}

