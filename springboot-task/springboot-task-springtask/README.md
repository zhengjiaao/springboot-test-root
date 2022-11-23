# springboot-task-springtask




## 简单示例

```java
@SpringBootApplication
@EnableScheduling //启用定时任务
public class TaskSpringTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskSpringTaskApplication.class, args);
	}

}

@Component
public class SpringTaskExample {

    /**默认是fixedDelay 上一次执行完毕时间后执行下一轮*/
    @Scheduled(cron = "0/5 * * * * *")  //cron 表示 在指定时间执行
//    @Scheduled(cron = "0 9 15 * * ?")   //每天15点9分执行一次  ，具体参考 README.md 格式
    public void run() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println("SpringTaskExample " + Thread.currentThread().getName() + "=====>>>>>使用cron  {}" + "时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }
}
```
