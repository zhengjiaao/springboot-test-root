package com.dist.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinxi on 2018/8/28.
 */
public abstract  class ListUtil {

    /**
     * 计算目标对象中的元素不存在于参照对象范围内的值
     * @param refer 参照对象
     * @param target  目标对象
     * @return
     */
    public static List<String> diff(final List<String> refer, final List<String> target) {
        if (ObjectUtil.isNull(target)) {
            return new ArrayList<>();
        }
        if (ObjectUtil.isNull(refer)) {
            return target;
        }
        //System.out.println("refersize="+refer.size());
        //System.out.println("targetsize="+target.size());
        List<String> cloneRefer = CloneUtil.deepClone(refer);  //对象克隆工具在utils中
        List<String> cloneTarget = CloneUtil.deepClone(target);
        //System.out.println("cloneRefersze="+cloneRefer.size());
        //System.out.println("cloneTarget="+cloneTarget.size());
        cloneRefer.retainAll(cloneTarget);      // 交集
        cloneTarget.removeAll(cloneRefer);
        return cloneTarget;
    }

    public static void main(String[] args) {

        List<String> refer = new ArrayList<>();
        refer.add("a");
        refer.add("b");
        refer.add("c");
        refer.add("e");
        List<String> target = new ArrayList<>();
        target.add("a");
        target.add("b");
        target.add("c");
        //target.add("d");

        List<String> list = diff(refer, target);
        //[d]
        System.out.println(list.toString());

        List<String> list2 = diff(target, refer);
        //[e]
        System.out.println(list2.toString());

    }

}
