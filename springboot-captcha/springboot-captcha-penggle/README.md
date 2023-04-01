# springboot-captcha-penggle


**penggle captcha 验证码**

### 功能
- penggle Kaptcha是一款Java验证码生成器，广泛应用于Web开发中的验证码实现。以下是penggle Kaptcha提供的一些主要功能：

- 验证码样式定制：penggle Kaptcha 提供了丰富的配置接口，可以自定义验证码的样式、大小、颜色、字体和噪点等。可以根据实际应用场景来调整验证码的外观，达到更好的验证码识别效果。

- 验证码文本干扰：penggle Kaptcha提供多种验证码文本的干扰形式，如波浪线、扭曲、随机文本等，这些形式可以有效的提高验证码的安全性和对恶意攻击的防范能力。

- 多语言支持：penggle Kaptcha 提供多种语言的支持，可生成多语言的验证码图片，大大增加了 penggle Kaptcha 的实用性。

- 验证码有效时间：penggle Kaptcha 可以为验证码设置有效时间，每张验证码只在有效时间内被允许使用，可保障验证码在时间范围内有效，同时减小针对和利用验证码的攻击和机会。

- 图片和音频验证码：penggle Kaptcha 提供了图片和音频验证码两种选择，可以根据实际需求进行选择和使用。

总之，penggle Kaptcha提供了很多有用的用于验证码生成和管理的工具，可以应用于各种场景的验证码实现。无论是应用于表单难度抑制还是用户认证， penggle Kaptcha 都能提供出彩的服务，让软件的用户交互更加安全便

### 依赖引入

```xml
        <!-- Kaptcha验证码组件-->
        <dependency>
            <groupId>com.github.penggle</groupId>
            <artifactId>kaptcha</artifactId>
            <version>2.3.2</version>
        </dependency>
```

### 简单配置

> KaptchaConfig.java

```java
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        //是否有边框
        properties.setProperty(Constants.KAPTCHA_BORDER,"yes");
        //验证码文本颜色
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,"blue");
        //验证码图片宽度
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH,"160");
        //验证码图片高度
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT,"60");
        //文本字符大小
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE,"38");
        //验证码session的值
        properties.setProperty(Constants.KAPTCHA_SESSION_CONFIG_KEY,"kaptchaCode");
        //验证码文本长度
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,"4");
        //字体
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}

```

### 基本使用

```java
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
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        response.setHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");

        //生成验证码字符文本
        String verifyCode = kaptchaProducer.createText();
        request.getSession().setAttribute(CAPTCHA_CODE_KEY, verifyCode);
        System.out.println(request.getSession().getAttribute(CAPTCHA_CODE_KEY));

        //创建验证图片
        BufferedImage image = kaptchaProducer.createImage(verifyCode);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "png", out);

        //输出和关闭输出
        out.flush();
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

}

```

### 效果图

不演示了，直接运行看效果吧。

