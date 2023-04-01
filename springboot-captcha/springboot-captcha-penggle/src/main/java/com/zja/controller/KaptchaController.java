/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-29 15:44
 * @Since:
 */
package com.zja.controller;

import com.google.code.kaptcha.Producer;
import com.zja.dto.LoginUserDTO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 *
 * @author: zhengja
 * @since: 2023/03/29 15:44
 */
@RestController
public class KaptchaController {

    @Resource
    private Producer kaptchaProducer;

    private static final String CAPTCHA_CODE_KEY = "captcha_code";


    /**
     * 获取验证码(生成验证码返回给前端)
     */
    @GetMapping("/captcha.jpg")
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //响应立即过期
        response.setDateHeader("Expires", 0);

        //不缓存任何图片数据
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");

        //生成验证码字符文本
        String verifyCode = kaptchaProducer.createText();
        request.getSession().setAttribute(CAPTCHA_CODE_KEY, verifyCode);

        //创建验证图片
        BufferedImage image = kaptchaProducer.createImage(verifyCode);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "png", out);

        //关闭输出
        out.close();
    }

    /**
     * 登录(校验验证码)
     *
     * @param loginUserDTO 用户信息
     */
    @PostMapping(value = "login")
    public String checkKaptcha(@RequestBody LoginUserDTO loginUserDTO, HttpServletRequest request) {
        //校验验证码
        boolean isCaptchaValid = verifyCaptcha(request, loginUserDTO.getVerifyCode());
        if (!isCaptchaValid) {
            return "验证码错误.";
        }

        //校验用户账户和密码

        //校验验证码成功后，删除验证码，避免被多次使用
        request.getSession().removeAttribute(CAPTCHA_CODE_KEY);

        return "验证码校验成功.";
    }


    public boolean verifyCaptcha(HttpServletRequest request, String captcha) {
        String captchaCode = (String) request.getSession().getAttribute(CAPTCHA_CODE_KEY);
        return captchaCode != null && captchaCode.equalsIgnoreCase(captcha);
    }
}
