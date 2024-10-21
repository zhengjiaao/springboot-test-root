package com.zja.ai.alibaba.bailian.wxiang;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisListResult;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.task.AsyncTaskListParam;
import com.alibaba.dashscope.utils.JsonUtils;
import org.junit.jupiter.api.Test;

/**
 * 通义万相系列：文本生成图像API
 *
 * <p>
 * 参考：https://help.aliyun.com/zh/model-studio/developer-reference/tongyi-wanxiang-series/?spm=a2c4g.11186623.0.0.63fc6e43SM1aKT
 *
 * @Author: zhengja
 * @Date: 2024-10-21 15:08
 */
public class TextToImagesTest {

    // 同步调用模式: 基础文生图
    @Test
    public void test_1() {
        try {
            basicCall();
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        }
    }

    // 同步调用模式: 相似图片生成
    @Test
    public void test_2() {
        syncCall();
    }

    // 异步调用模式: 基础文生图
    @Test
    public void test_3() {
        asyncCall();
    }

    // 异步调用模式: 相似图片生成
    @Test
    public void test_4() {
        asyncCall2();
    }

    // 同步调用模式: 基础文生图
    public static void basicCall() throws ApiException, NoApiKeyException {
        ImageSynthesis is = new ImageSynthesis();
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .model(ImageSynthesis.Models.WANX_V1)
                        .n(4)
                        .size("1024*1024")
                        .prompt("雄鹰自由自在的在蓝天白云下飞翔")
                        .build();

        ImageSynthesisResult result = is.call(param);
        System.out.println(result);
    }

    public static void listTask() throws ApiException, NoApiKeyException {
        ImageSynthesis is = new ImageSynthesis();
        AsyncTaskListParam param = AsyncTaskListParam.builder().build();
        ImageSynthesisListResult result = is.list(param);
        System.out.println(result);
    }

    public void fetchTask() throws ApiException, NoApiKeyException {
        String taskId = "your task id";
        ImageSynthesis is = new ImageSynthesis();
        // If set DASHSCOPE_API_KEY environment variable, apiKey can null.
        ImageSynthesisResult result = is.fetch(taskId, null);
        System.out.println(result.getOutput());
        System.out.println(result.getUsage());
    }

    // 同步调用模式: 相似图片生成
    public void syncCall() {
        String prompt = "玫瑰花海，背景是日出，采用淡紫色和粉红色的概念艺术装置风格，郁郁葱葱的风景，以及有序的布局";
        String refImage = "https://huarong123.oss-cn-hangzhou.aliyuncs.com/image/%E7%8E%AB%E7%91%B0.png";
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .model(ImageSynthesis.Models.WANX_V1)
                        .prompt(prompt)
                        .n(1)
                        .size("1024*1024")
                        .refImage(refImage)
                        .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            System.out.println("---sync call, please wait a moment----");
            result = imageSynthesis.call(param);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
    }

    // ----------------------------------

    // 异步调用模式: 基础文生图
    public void asyncCall() {
        System.out.println("---create task----");
        String taskId = this.createAsyncTask();
        System.out.println("---wait task done then return image url----");
        this.waitAsyncTask(taskId);
    }

    /**
     * 创建异步任务
     *
     * @return taskId
     */
    public String createAsyncTask() {
        String prompt = "少女，高分辨率，增加细节，细节强化，侧面视角，森林，奶油风，暖色调，精致的脸部比例，精细的裙子，五官立体，长卷发，极高分辨率，清晰度强化，全身像，微笑，五颜六色的花瓣飞舞，自然光";
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .model(ImageSynthesis.Models.WANX_V1)
                        .prompt(prompt)
                        .n(1)
                        .size("1024*1024")
                        .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            result = imageSynthesis.asyncCall(param);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        String taskId = result.getOutput().getTaskId();
        System.out.println("taskId=" + taskId);
        return taskId;
    }


    /**
     * 等待异步任务结束
     *
     * @param taskId 任务id
     */
    public void waitAsyncTask(String taskId) {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            // If you have set the DASHSCOPE_API_KEY in the system environment variable, the apiKey can be null.
            result = imageSynthesis.wait(taskId, null);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println(JsonUtils.toJson(result.getOutput()));
        System.out.println(JsonUtils.toJson(result.getUsage()));
    }


    // 异步调用模式: 相似图片生成
    public void asyncCall2() {
        System.out.println("---create task----");
        String taskId = this.createAsyncTask2();
        System.out.println("---wait task done then return image url----");
        this.waitAsyncTask2(taskId);
    }


    /**
     * 创建异步任务
     *
     * @return taskId
     */
    public String createAsyncTask2() {
        String prompt = "玫瑰花海，背景是日出，采用淡紫色和粉红色的概念艺术装置风格，郁郁葱葱的风景，以及有序的布局";
        String refImage = "https://huarong123.oss-cn-hangzhou.aliyuncs.com/image/%E7%8E%AB%E7%91%B0.png";
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .model(ImageSynthesis.Models.WANX_V1)
                        .prompt(prompt)
                        .n(1)
                        .size("1024*1024")
                        .refImage(refImage)
                        .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            result = imageSynthesis.asyncCall(param);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        String taskId = result.getOutput().getTaskId();
        System.out.println("taskId=" + taskId);
        return taskId;
    }


    /**
     * 等待异步任务结束
     *
     * @param taskId 任务id
     */
    public void waitAsyncTask2(String taskId) {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            // If you have set the DASHSCOPE_API_KEY in the system environment variable, the apiKey can be null.
            result = imageSynthesis.wait(taskId, null);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println(JsonUtils.toJson(result.getOutput()));
        System.out.println(JsonUtils.toJson(result.getUsage()));
    }

}
