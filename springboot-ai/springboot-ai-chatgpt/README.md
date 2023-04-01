# springboot-ai-chatgpt

**chatgpt java接入方式实现测试**

## 本地代理问题
描述：本地启动了代理，浏览器和postman可以正常访问，发现idea中java无法正常请求接口。

报错信息：域名:ip连接错误，解析不了。


参考：

https://blog.csdn.net/qq_25620797/article/details/121004332

https://www.cnblogs.com/kn-zheng/p/17029456.html



解决方式：

```python
#idea中代理

#VM 参数：添加参数
-Djava.net.preferIPv4Stack=true -DsocksProxyHost=127.0.0.1 -DsocksProxyPort=7890

```

