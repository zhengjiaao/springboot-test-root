/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-29 17:28
 * @Since:
 */
package com.zja.controller;

import com.zja.util.CaptchaUtils;
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

    /**
     * 获取验证码(生成验证码返回给前端)
     */
    @GetMapping("/captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //定义图形验证码的长和宽
        CaptchaUtils.outResponse(request, response);
    }

    /**
     * 登录(校验验证码)
     *
     * @param loginUserDTO 用户信息
     */
    @PostMapping(value = "login")
    public String checkKaptcha(@RequestBody LoginUserDTO loginUserDTO, HttpServletRequest request) {
        //校验验证码
        boolean checkCode = CaptchaUtils.checkCode(request, loginUserDTO.getVerifyCode());
        if (!checkCode) {
            return "验证码错误.";
        }

        //校验用户账户和密码

        return "验证码校验成功.";
    }

}
