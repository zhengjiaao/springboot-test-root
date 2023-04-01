/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-29 17:28
 * @Since:
 */
package com.zja.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 *
 * @author: zhengja
 * @since: 2023/03/29 17:28
 */
@RestController
public class CaptchaController {

    private static final String CAPTCHA_CODE_KEY = "captcha_code";
    private static final String IMAGE_PNG = "image/png";
    private static final String CAPTCHA_EXPIRED = "验证码已过期.";
    private static final String CAPTCHA_ERROR = "验证码错误.";
    private static final String CAPTCHA_SUCCESS = "验证码校验成功.";


    @GetMapping("/captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //响应立即过期
        response.setDateHeader("Expires", 0);

        //不缓存任何图片数据
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");

        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);

        //输出code
        String code = lineCaptcha.getCode();

        ServletOutputStream out = response.getOutputStream();
        lineCaptcha.write(out);

        request.getSession().setAttribute(CAPTCHA_CODE_KEY, code);
        response.setContentType(IMAGE_PNG);

        out.close();
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
            return CAPTCHA_EXPIRED;
        }

        //校验验证码
        if (!verifyCode.equals(loginUserDTO.getVerifyCode())) {
            return CAPTCHA_ERROR;
        }

        //校验验证码成功，删除验证码，避免被多次使用
        request.getSession().removeAttribute(CAPTCHA_CODE_KEY);

        //校验用户账户和密码

        return CAPTCHA_SUCCESS;
    }

}
