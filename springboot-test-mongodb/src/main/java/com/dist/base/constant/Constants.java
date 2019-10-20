package com.dist.base.constant;

/**
 * 全局常量
 */
public abstract class Constants {

    //banner移动
    public static final Integer SORT_UP = 0;    // 排序上移
    public static final Integer SORT_DOWN = 1;     // 排序下移

    // 新闻公告排序
    public static final Integer NEWS_ORDER_BY_CREATE_TIME = 0;    // 最新
    public static final Integer NEWS_ORDER_BY_VIEW_NUMBER = 1;    // 最热

    public static final Integer NEWS_HOT_LIMIT = 5;     // 热点新闻条数
    public static final Integer NEWS_CAROUSEL_LIMIT = 3;     // 工作动态/规划普及轮播条数

    public static final String UPLOAD_FILE_LEGAL_SUFFIX = "pdf";    // 允许合法上传的文件类型


    //登录功能
    public static final long CODE_REQUEST_LIMIT = 60*1000;       // 验证码允许请求的时间间隔，一分钟允许请求一次

    public static final long CODE_EFFECTIVE_TIME = 10 * 60 * 1000;       // 验证码允许请求的时间间隔，一分钟允许请求一次

    public static final Integer LOGIN_REMEMBER_ME = 1;       // 登录-记住密码

    public static final long REDIS_QRGEN_EXPIRE_LONG = 1800;     // redis缓存二维码有效30分钟

    public static final long QRGEN_EXPIRE = 10 * 60 * 1000;      // 微信二维码有效时间10分钟

    //response.setStatus(xxxx);
    public static final int FILE_LOAD_ERROR = 404;          // 资源文件下载失败
    public static final int WEIXIN_REQUEST_ERROR = 403;     // 微信回调失败


    // error_code
    public static final int WECHAT_UNAUTHORIZATION_ERROR = 401;     // 微信未绑定
    public static final int LOGIN_SESSION_EXPIRE = 405;     // 登录状态过期，禁止访问


    public static final long REDIS_DEFULT_EXPIRE = 30 * 60 * 1000;      // redis默认有效时间30分钟

    // 用户是否设置密码
    public final static String USER_HAS_PWD = "1";   // 已设置
    public final static String USER_HAS_NOT_PWD = "0";      // 未设置

    // 标题TITLE字段，用户名/昵称USERNAME字段允许的最大长度
    public final static int TITLE_FILED_LIMIT_LEN = 255;
    public final static int USERNAME_FILED_LIMIT_LEN = 25;
    public final static int CONTENT_FILED_LIMIT_LEN = 4000;

    // 系统初始化默认的管理员用户userid，请保持与初始化语句中的id一致
    public static final String SYS_DEFULT_ADMIN_USER_ID = "2c90906b65aa08320165aa099fec0001";

    //获取当前用户登录名
    public static final String CURRENT_USER_LOGIN_NAME="currentUserLoginName";
    //在线统计人数
    public static final String ONLINE_USER_KEYS="onlineUserKeys";

    //=====

    /** 表示当前在线人数的key */
    public static Integer ON_LINE_COUNT = 0;

    /** 微信访问人次的key */
    public final static String WX_ACCESS_COUNT = "WX_ACCESS";
    /** PC访问人次的key */
    public final static String PC_ACCESS_COUNT = "PC_ACCESS";

    /*递增因子 大于1*/
    //微信访问量
    public static long WEIXIN_COUNT=1;
    //PC访问量
    public static long PC_COUNT=1;
}