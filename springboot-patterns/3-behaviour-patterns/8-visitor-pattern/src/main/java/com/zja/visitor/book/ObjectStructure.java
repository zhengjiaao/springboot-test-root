/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:34
 * @Since:
 */
package com.zja.visitor.book;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象结构类ObjectStructure，用于存储和操作元素对象。
 *
 * @author: zhengja
 * @since: 2023/10/09 16:34
 */
public class ObjectStructure {
    private List<Element> elements = new ArrayList<>();

    public void addElement(Element element) {
        elements.add(element);
    }

    public void removeElement(Element element) {
        elements.remove(element);
    }

    public void accept(Visitor visitor) {
        for (Element element : elements) {
            element.accept(visitor);
        }
    }
}