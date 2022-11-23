# springboot-task-quartz

> java最全的定时任务 quartz    

- [quartz 官网](http://www.quartz-scheduler.org/)
- [quartz github](https://github.com/quartz-scheduler/quartz)


## 引入依赖

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-quartz</artifactId>
		</dependency>
```

## 简单示例

执行任务类

```java
public class QuartzJob {

    public String quartzMethod1() {
        System.out.println("执行方法：quartzMethod1  " + "当前线程名称：" + Thread.currentThread().getName() + " " + new Date());
        return "quartzMethod1";
    }

    public void quartzMethod2() {
        System.out.println("执行方法：quartzMethod2  " + "当前线程名称：" + Thread.currentThread().getName() + " " + new Date());
    }
}
```

配置类 QuartzConfig

```java
@Configuration
public class QuartzConfig {
    //Quartz 方式二 使用 cron 表达式 :启动方式-项目启动自动执行
    // 定义方法，做什么
    @Bean(name = "job1")
    public MethodInvokingJobDetailFactoryBean job1(QuartzJob quartzJob) {  //参数：可以传类/可以是接口，但是类要注入到 bean里，不然找不到
        MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        factoryBean.setConcurrent(true);
        // 使用哪个对象
        factoryBean.setTargetObject(quartzJob);
        // 使用对象中的哪个方法
        factoryBean.setTargetMethod("quartzMethod1");

        return factoryBean;
    }

    // 定义什么时候做，使用 cron 表达式
    @Bean(name = "cron1")
    public CronTriggerFactoryBean cron1(@Qualifier("job1") MethodInvokingJobDetailFactoryBean job1) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        // 设置job对象
        factoryBean.setJobDetail(job1.getObject());
        // 设置执行时间
        factoryBean.setCronExpression("0/10 * * * * ?");
        return factoryBean;
    }
}
```
