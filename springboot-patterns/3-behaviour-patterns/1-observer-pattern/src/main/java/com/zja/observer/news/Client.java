/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 11:07
 * @Since:
 */
package com.zja.observer.news;

/**
 * @author: zhengja
 * @since: 2023/10/09 11:07
 */
public class Client {
    public static void main(String[] args) {
        NewsPublisher publisher = new NewsPublisher();

        Observer subscriber1 = new Subscriber("Subscriber1");
        Observer subscriber2 = new Subscriber("Subscriber2");
        Observer subscriber3 = new Subscriber("Subscriber3");

        publisher.registerObserver("Sports", subscriber1);
        publisher.registerObserver("Politics", subscriber2);
        publisher.registerObserver("Sports", subscriber3);

        publisher.publishNews("Sports", "New record set in the Olympics");
        publisher.publishNews("Politics", "Election results announced");

        publisher.unregisterObserver("Sports", subscriber3);

        publisher.publishNews("Sports", "Football league match canceled");

        //输出结果：
        //Publishing news: New record set in the Olympics
        //Subscriber1 received news in category Sports: New record set in the Olympics
        //Subscriber3 received news in category Sports: New record set in the Olympics
        //Publishing news: Election results announced
        //Subscriber2 received news in category Politics: Election results announced
        //Publishing news: Football league match canceled
        //Subscriber1 received news in category Sports: Football league match canceled
    }
}
