package com.dist.controller;

import com.dist.constant.Constants;
import com.dist.util.exception.IllegalRequestException;
import com.dist.model.dto.RegisterDto;
import com.dist.utils.IdUtil;
import com.dist.utils.ObjectUtil;
import com.dist.utils.ValidatorUtil;
import com.dist.utils.dayu.DayuSendMessage;
import com.dist.utils.dayu.SendSms;
import com.dist.utils.session.SessionKey;
import com.dist.utils.string.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @program: springbootdemo
 * @Date: 2019/1/14 10:22
 * @Author: Mr.Zheng
 * @Description:
 */
@Slf4j
@RestController("restVerificationController")
@RequestMapping(value = "/rest/public")
@Api(tags = {"DEMO-VerificationController"}, description = "手机短信验证码")
public class VerificationController extends BaseController {

    //正则表达式：验证密码
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,12}$";

    @Autowired
    DayuSendMessage sendMessage;

    @Autowired
    SendSms sendSms;

    @ApiOperation(value = "大鱼-获取手机短信验证码", httpMethod = "GET")
    @RequestMapping(value = "/rest/public/common/verificationCode", method = RequestMethod.GET)
    public Object getVerificationCode(
            @ApiParam(value = "手机号",required = true) @RequestParam(value = "phone") String phone) {

        if (StringUtil.isNullOrEmpty(phone)) {
            log.error("获取手机短信验证码时，入参phone的值:{}非法",phone);
            throw new RuntimeException("入参phone的值不能为空");
        }
        // 检验手机号格式
        if (!ValidatorUtil.isMobile(phone)) {
            log.error("获取手机短信验证码时，入参phone的值:{}非法",phone);
            throw new RuntimeException("请输入正确格式的手机号码");
        }

        // 控制每分钟最多允许发送一次 （开发测试时，可以注释掉）
        if (!requestLimit()) {
            log.error("获取手机短信验证码时，{}访问太频繁",phone);
            throw new RuntimeException("发送太频繁，请稍后再试");
        }

        // 发送验证码
        String code = IdUtil.uuid6();
        boolean sendSuccess = sendMessage.taobaoSendMoblieMessage(phone, code);
        if (sendSuccess) {
            // 最近一次成功访问时间
            session.setAttribute(SessionKey.CODE_LAST_VISIT_LONG, System.currentTimeMillis());
            // 将验证码，手机号保存在session
            Map<String, Object> verification = new HashMap<>();
            verification.put(SessionKey.VERIFICATION_CODE_STRING, code);
            verification.put(SessionKey.VERIFICATION_PHONE_STRING, phone);
            verification.put(SessionKey.VERIFICATION_SEND_TIME_LONG, System.currentTimeMillis());
            session.setAttribute(SessionKey.CODE_VISIT_SESSION_MAP + phone, verification);
        } else {
            return "发送失败，请稍后再试";
        }

        return "手机验证码发送成功";
    }

    @ApiOperation(value = "阿里云-获取手机短信验证码", httpMethod = "GET")
    @RequestMapping(value = "/rest/public/common/verificationCodes", method = RequestMethod.GET)
    public Object getVerificationCodes(
            @ApiParam(value = "手机号",required = true) @RequestParam(value = "phone") String phone) {

        if (StringUtil.isNullOrEmpty(phone)) {
            log.error("获取手机短信验证码时，入参phone的值:{}非法",phone);
            throw new RuntimeException("入参phone的值不能为空");
        }
        // 检验手机号格式
        if (!ValidatorUtil.isMobile(phone)) {
            log.error("获取手机短信验证码时，入参phone的值:{}非法",phone);
            throw new RuntimeException("请输入正确格式的手机号码");
        }

        // 控制每分钟最多允许发送一次 （开发测试时，可以注释掉）
        if (!requestLimit()) {
            log.error("获取手机短信验证码时，{}访问太频繁",phone);
            throw new RuntimeException("发送太频繁，请稍后再试");
        }

        // 发送验证码
        String code = IdUtil.uuid6();
        Map<String, String> sms = sendSms.sendSms(phone, code);
        boolean sendSuccess = false;
        if (sms.containsKey("success")) {
            sendSuccess = true;
        } else {
            log.error(">>send sms error {}", sms.get("error"));
        }
        if (sendSuccess) {
            // 最近一次成功访问时间
            session.setAttribute(SessionKey.CODE_LAST_VISIT_LONG, System.currentTimeMillis());
            // 将验证码，手机号保存在session
            Map<String, Object> verification = new HashMap<>();
            verification.put(SessionKey.VERIFICATION_CODE_STRING, code);
            verification.put(SessionKey.VERIFICATION_PHONE_STRING, phone);
            verification.put(SessionKey.VERIFICATION_SEND_TIME_LONG, System.currentTimeMillis());
            session.setAttribute(SessionKey.CODE_VISIT_SESSION_MAP + phone, verification);
        } else {
            return "发送失败，请稍后再试";
        }

        return "手机验证码发送成功";
    }

    @ApiOperation(value = "注册用户", httpMethod = "POST")
    @RequestMapping(value = "/v1/register/user",method = RequestMethod.POST)
    public Object registerAccount(@ApiParam(value = "具体信息参考-Model") @RequestBody RegisterDto reqDto) throws Exception {

        String phone = reqDto.getPhone();
        String password = reqDto.getPassword();
        String username = reqDto.getUsername();

        // 检验手机号格式
        verifyPhoneNumberFormat(phone);

        // 校验手机号是否已经注册
       /* UserEntity sysUser = securityService.getSysUser(phone);
        if (ObjectUtil.isNonNull(sysUser)){
            throw new IllegalRequestException("当前手机号【"+phone+"】已被注册");
        }*/

        //短信验证码验证
        checkVerificationCode(phone,reqDto.getCode());

        //验证密码强度
        if (!Pattern.matches(REGEX_PASSWORD, password)) {
            log.error("用户注册时，用户密码{}强度不符合",password);
            throw new IllegalRequestException("密码强度不符合");
        }

        //验证用户名是否合法
        if (StringUtils.isEmpty(username)) {
            throw new IllegalRequestException("用户账号不合法");
        }

        //注册用户接口
        //return this.securityService.registerAccount(username, password,phone);
        return "注册成功！";
    }

    /**
     * 验证手机号格式是否正确
     * @param phone 手机号
     * @throws Exception
     */
    private void verifyPhoneNumberFormat(String phone) throws IllegalRequestException {
        // 检验手机号格式
        if (!ValidatorUtil.isMobile(phone)) {
            log.error("用户注册(公众)时，入参phone的值:{}非法",phone);
            throw new IllegalRequestException("当前手机号【"+phone+"】格式不正确");
        }
    }

    /**
     * 手机验证码校验
     * @param phone
     * @param verificationCode
     * @return
     */
    private boolean checkVerificationCode(String phone,String verificationCode) throws IllegalRequestException {
        // 从session中校验
        Map<String,Object> verification = (Map<String, Object>) session.getAttribute(SessionKey.CODE_VISIT_SESSION_MAP + phone);
        if (ObjectUtil.isNull(verification)) {
            log.error("{}手机验证码校验时无法从session中获取验证码",phone);
            throw new IllegalRequestException("当前手机号【"+phone+"】未获取短信码");
        }
        String sessionCode = (String) verification.get(SessionKey.VERIFICATION_CODE_STRING);
        long sendTime = (long) verification.get(SessionKey.VERIFICATION_SEND_TIME_LONG);
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - sendTime;

        if (diff > Constants.CODE_EFFECTIVE_TIME) {
            log.error("手机验证码校验时,验证码已经过期");
            throw new IllegalRequestException("验证码已经过期，请重新获取验证码");
        }

        if (!verificationCode.equalsIgnoreCase(sessionCode)) {
            log.error("手机验证码校验时,验证码不正确");
            throw new IllegalRequestException("验证码不正确，请重新输入验证码");
        }

        // 销毁验证码session
        session.removeAttribute(SessionKey.CODE_VISIT_SESSION_MAP + phone);

        return true;
    }

    /**
     * 连续请求的时间间隔限制
     * @return
     */
    private boolean requestLimit() {
        Long lastVisitTime = (Long) session.getAttribute(SessionKey.CODE_LAST_VISIT_LONG);
        if (null == lastVisitTime) {
            return true;
        }
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - lastVisitTime.longValue();

        if ((diff > 60000)) {
            return true;
        }

        return false;
    }

}
