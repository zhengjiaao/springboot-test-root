/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-29 15:44
 * @Since:
 */
package com.zja.controller;

import cloud.tianai.captcha.spring.annotation.Captcha;
import cloud.tianai.captcha.spring.application.CaptchaImageType;
import cloud.tianai.captcha.spring.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.request.CaptchaRequest;
import cloud.tianai.captcha.spring.vo.CaptchaResponse;
import cloud.tianai.captcha.spring.vo.ImageCaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 * @author: zhengja
 * @since: 2023/03/29 15:44
 */
@RestController
public class KaptchaController {

    private static final String CAPTCHA_CODE_KEY = "captcha_code";

    // 只需要在需要验证的controller层加入 @Captcha 注解，
    // 并且接受的参数指定成CaptchaRequest即可自动进行校验
    // 自己真实的参数可以写到 CaptchaRequest对象的泛型中
    // 如果校验失败，会抛出CaptchaValidException异常
    // 对校验失败的处理，可以使用sping的全局异常拦截CaptchaValidException异常进行处理
    @Autowired
    private ImageCaptchaApplication imageCaptchaApplication;


    /**
     * 获取验证码(生成验证码返回给前端)
     */
    @GetMapping("/captcha")
    public ResponseEntity createVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        CaptchaResponse<ImageCaptchaVO> captcha = imageCaptchaApplication.generateCaptcha(CaptchaImageType.JPEG_PNG);
        String captchaId = captcha.getId();

        // 将验证码保存到Session中供后面验证使用
        request.getSession().setAttribute(CAPTCHA_CODE_KEY, captchaId);

        return ResponseEntity.ok(captcha);
    }

    /**
     * 登录(校验验证码)
     * @param loginUserDTO 用户信息
     */
    @PostMapping(value = "login")
    public String checkKaptcha(@RequestBody LoginUserDTO loginUserDTO, HttpServletRequest request) {

        //从session获取验证码
        String verifyCode = (String) request.getSession().getAttribute(CAPTCHA_CODE_KEY);

        if (StringUtils.isEmpty(verifyCode)) {
            return "验证码已过期.";
        }

        //校验验证码
        if (!verifyCode.equals(loginUserDTO.getVerifyCode())) {
            return "验证码错误.";
        }

        //校验验证码成功，删除验证码，避免被多次使用
        request.getSession().setAttribute(CAPTCHA_CODE_KEY, "");
        request.getSession().removeAttribute(CAPTCHA_CODE_KEY);

        //校验用户账户和密码

        return "验证码校验成功.";
    }


    // 只需要在需要验证的controller层加入 @Captcha 注解，
    // 并且接受的参数指定成CaptchaRequest即可自动进行校验
    // 自己真实的参数可以写到 CaptchaRequest对象的泛型中
    // 如果校验失败，会抛出CaptchaValidException异常
    // 对校验失败的处理，可以使用sping的全局异常拦截CaptchaValidException异常进行处理

    @Captcha("SLIDER")
    @PostMapping("/login2")
    public String login(@RequestBody CaptchaRequest<Map> request) {
        // 进入这个方法就说明已经校验成功了
        return "success";
    }
}
