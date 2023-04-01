# springboot-captcha-hutool


- [captcha | hutool 官网](https://hutool.cn/docs/#/captcha/%E6%A6%82%E8%BF%B0)


验证码功能位于cn.hutool.captcha包中，核心接口为ICaptcha，此接口定义了以下方法：

- createCode 创建验证码，实现类需同时生成随机验证码字符串和验证码图片
- getCode 获取验证码的文字内容
- verify 验证验证码是否正确，建议忽略大小写
- write 将验证码写出到目标流中

其中write方法只有一个OutputStream，ICaptcha实现类可以根据这个方法封装写出到文件等方法。

AbstractCaptcha为一个ICaptcha抽象实现类，此类实现了验证码文本生成、非大小写敏感的验证、写出到流和文件等方法，通过继承此抽象类只需实现createImage方法定义图形生成规则即可。
