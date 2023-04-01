/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-12-06 14:36
 * @Since:
 */
package com.zja.util;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码 管理
 */
public class CaptchaUtils {

    private static final String SESSION_KEY = "captcha";

    /**
     * 校验验证码  对比存在，则清除验证码，避免code被多次利用
     * @param request
     * @param code 验证码 code
     */
    public static boolean checkCode(HttpServletRequest request, String code) {
        boolean ver = CaptchaUtil.ver(code, request);
        if (ver) {
            CaptchaUtil.clear(request);
        }
        return ver;
    }

    /**
     *  默认验证码 输出到响应中
     * @param request
     * @param response
     */
    public static void outResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        outResponse(captcha(), request, response);
    }

    /**
     * 验证码 输出到响应中
     * @param captcha
     * @param request
     * @param response
     */
    public static void outResponse(Captcha captcha, HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaUtil.out(captcha, request, response);
    }

    /**
     * 验证码 输出到响应中
     * @param width     验证码显示宽度
     * @param height    验证码显示高度
     * @param len       验证码随机字符长度
     * @param charType  验证码类型 1-字母数字混合,2-纯数字,3-纯字母,4-纯大写字母,5-纯小写字母,6-数字大写字母
     * @param request
     * @param response
     */
    public static void outResponse(int width, int height, int len, int charType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaUtil.out(captcha(width, height, len, charType), request, response);
    }

    /**
     * 获取Base64验证码
     * @param width     验证码显示宽度
     * @param height    验证码显示高度
     * @param len       验证码随机字符长度
     * @param charType  验证码类型 1-字母数字混合,2-纯数字,3-纯字母,4-纯大写字母,5-纯小写字母,6-数字大写字母
     * @param request
     * @return Base64验证码
     */
    public static String getBase64Captcha(int width, int height, int len, int charType, HttpServletRequest request) {
        Captcha captcha = captcha(width, height, len, charType);
        request.getSession().setAttribute(SESSION_KEY, captcha.text().toLowerCase());
        return captcha.toBase64();
    }


    //-------------------私有方法--------------------

    /**
     * 生成 默认验证码
     */
    private static Captcha captcha() {
        return captcha(115, 42, 4);
    }

    /**
     * 自定义生成验证码  随机4位数字
     */
    private static Captcha captcha(int width, int height, int len) {
        return captcha(width, height, len, 2);
    }

    /**
     * 自定义生成验证码
     * @param width     验证码显示宽度
     * @param height    验证码显示高度
     * @param len       验证码随机字符长度
     * @param charType  验证码类型 1-字母数字混合,2-纯数字,3-纯字母,4-纯大写字母,5-纯小写字母,6-数字大写字母
     */
    private static Captcha captcha(int width, int height, int len, int charType) {
        Captcha captcha = new SpecCaptcha(width, height, len);
        captcha.setCharType(charType);
        return captcha;
    }
}
