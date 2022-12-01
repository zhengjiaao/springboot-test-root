/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-01 13:15
 * @Since:
 */
package com.zja.controller;

import cloud.tianai.captcha.spring.annotation.Captcha;
import cloud.tianai.captcha.spring.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.request.CaptchaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/captcha")
public class CaptchaTianaiController {

    @Autowired
    private ImageCaptchaApplication imageCaptchaApplication;

    @GetMapping("{id}")
    public ResponseEntity queryById(@PathVariable Long id) {
        return ResponseEntity.ok(id);
    }

    // 只需要在需要验证的controller层加入 @Captcha 注解，
    // 并且接受的参数指定成CaptchaRequest即可自动进行校验
    // 自己真实的参数可以写到 CaptchaRequest对象的泛型中
    // 如果校验失败，会抛出CaptchaValidException异常
    // 对校验失败的处理，可以使用sping的全局异常拦截CaptchaValidException异常进行处理

    @Captcha("SLIDER")
    @PostMapping("/login")
    public String login(@RequestBody CaptchaRequest<Map> request) {
        // 进入这个方法就说明已经校验成功了
        return "success";
    }

}
