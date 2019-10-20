package com.dist.utils.session;

/**
 * httpsession中的统一key
 *
 * @author yinxp@dist.com.cn
 */
public abstract class SessionKey {

    // 手机验证码-缓存在session中的数据
    public static final String CODE_VISIT_SESSION_MAP = "codeVisitSession";

    // 手机验证码-最近一次请求时间
    public static final String CODE_LAST_VISIT_LONG = "codeLastVisitTime";

    // 手机验证码-发送的验证码
    public static final String VERIFICATION_CODE_STRING = "verificationCode";

    // 手机验证码-发送验证码的手机号码
    public static final String VERIFICATION_PHONE_STRING = "verificationPhone";

    // 手机验证码-验证码发送时间
    public static final String VERIFICATION_SEND_TIME_LONG = "verificationSendTime";


    // 已经登录的用户userid
    public static final String LOGINED_USER_ID_STRING = "loginedUserId";

    // 用户登录状态，true登录/false未登录
    public static final String LOGIN_STATUS_BOOLEAN = "loginStatus";


    // 自动登录-用户token的id
    public static final String TOKEN_ID_STRING= "TOKENID";

    // 自动登录-用户tiken序列
    public static final String TOKEN_SEQ_STRING= "TOKENSEQ";

    // 登录-微信openId
    public static final String WECHAT_OPENID_STRING = "openId";

    // 图形验证码
    public static final String KAPTCHA_SESSION_KEY_STRING = "kaptchaCode";

    // 图形验证码生成时间
    public static final String KAPTCHA_SESSION_TIME_LONG = "kaptchaCreateTime";

    // redis中二维码属性
    public static final String REDIS_QRGEN_FILE_BYTE= "file";
    public static final String REDIS_QRGEN_STATUS_INT= "status";
    public static final String REDIS_QRGEN_TIME_INT= "time";

}
