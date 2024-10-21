package com.zja.ai.alibaba.bailian.wxiang;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * 图像局部重绘API参考
 *
 * @Author: zhengja
 * @Date: 2024-10-21 15:32
 */
public class RedrawingImagesTest {

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

    public void syncCall() {
        String task = "image2image";
        ImageSynthesis imageSynthesis = new ImageSynthesis(task);
        ImageSynthesisParam param = genImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            System.out.println("---sync call, please wait a moment----");
            result = imageSynthesis.call(param);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
    }

    private ImageSynthesisParam genImageSynthesis() {
        HashMap<String, Object> extraInputMap = new HashMap<>();
        extraInputMap.put("base_image_url", "http://synthesis-source.oss-accelerate.aliyuncs.com/lingji/validation/mask2img/demo/source3.jpg");
        extraInputMap.put("mask_image_url", "http://synthesis-source.oss-accelerate.aliyuncs.com/lingji/validation/mask2img/demo/glasses.png");
        String prompt = "a dog wearing red glasses";
        String model = "wanx-x-painting";
        return ImageSynthesisParam.builder()
                .model(model)
                .prompt(prompt)
                .n(1)
                .size("1024*1024")
                .extraInputs(extraInputMap)
                .build();
    }

    public void asyncCall() {
        System.out.println("---create task----");
        String taskId = this.createAsyncTask();
        System.out.println("---wait task done then return result----");
        this.waitAsyncTask(taskId);
    }

    private String createAsyncTask() {
        String task = "image2image";
        ImageSynthesis imageSynthesis = new ImageSynthesis(task);
        ImageSynthesisParam param = genImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            result = imageSynthesis.asyncCall(param);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("result: " + JsonUtils.toJson(result));
        System.out.println("taskId: " + result.getOutput().getTaskId());
        return result.getOutput().getTaskId();
    }

    private void waitAsyncTask(String taskId) {
        String task = "image2image";
        ImageSynthesis imageSynthesis = new ImageSynthesis(task);
        ImageSynthesisResult result = null;
        try {
            result = imageSynthesis.wait(taskId, null);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
    }

}
