/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:34
 * @Since:
 */
package com.zja.visitor.book;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:34
 */
public class Client {
    public static void main(String[] args) {
        // 创建具体元素对象
        Book book = new Book("Design Patterns", 50.0, 1.5);
        Electronics electronics = new Electronics("Smartphone", 500.0, 0.3);

        // 创建具体访问者对象
        Visitor discountVisitor = new DiscountVisitor();
        Visitor shippingVisitor = new ShippingVisitor();

        // 创建对象结构
        ObjectStructure objectStructure = new ObjectStructure();
        objectStructure.addElement(book);
        objectStructure.addElement(electronics);

        // 应用访问者操作
        objectStructure.accept(discountVisitor);
        objectStructure.accept(shippingVisitor);

        //输出结果：
        //Applied discount to book: Design Patterns
        //Applied discount to electronics: Smartphone
        //Shipping cost for book Design Patterns: $0.75
        //Shipping cost for electronics Smartphone: $0.36
    }
}