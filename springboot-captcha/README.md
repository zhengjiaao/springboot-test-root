# springboot-captcha

**验证码：滑块验证码、旋转验证码、滑动还原验证码、文字点选验证码**

## spring-boot 集成(组件)示例

> 以下是已经完成的示例模块

- [springboot-captcha-easy 推荐，效果好](./springboot-captcha-easy)
- [springboot-captcha-hutool](./springboot-captcha-hutool)
- [springboot-captcha-penggle](./springboot-captcha-penggle)
- [springboot-captcha-tianai](./springboot-captcha-tianai)

## 验证码实现思路

实现验证码的方式有很多，下面列举一些主流的方式：

- 在前端使用JS/Jquery实现验证码；
- 在后端使用Jcaptcha实现验证码；
- 在后端使用Kaptcha实现验证码；
- 使用图片库，如Captcha.js、MGen等；
- 使用人机验证组件，如Google reCAPTCHA、阿里滑动验证码等；
- 使用短信验证码。

## 验证码试用场景

每种方式都有其适用场景和特点。推荐使用哪种方式可以根据实际需求来判断。以下是一些建议：

- 如果是简单的验证码，如数字验证码，可以使用JS/Jquery实现；
- 如果需要复杂的设置和配置，可以使用Jcaptcha；
- Kaptcha相对比Jcaptcha更简单易用，如果对性能和定制有较高要求，可以使用Kaptcha；
- 如果需要图片验证码，则可以使用图片库来实现；
- 需要防止恶意攻击时，可以使用人机验证组件（如Google reCAPTCHA、阿里滑动验证码等）；
- 对安全要求较高或通信渠道被篡改的情况下，可以考虑短信验证码。

总之，选择哪种方式要综合考虑实际需求、性能、安全性等因素。






