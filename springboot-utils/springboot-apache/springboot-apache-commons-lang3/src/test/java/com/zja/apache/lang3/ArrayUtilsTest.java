package com.zja.apache.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * ArrayUtils： 用于数组操作的工具类，包括数组的拷贝、查找、反转、合并等，简化了数组处理。
 *
 * @Author: zhengja
 * @Date: 2024-10-16 9:36
 */
public class ArrayUtilsTest {

    @Test
    public void _test1() {

        String[] myArray = {"A", "B", "C"};

        // 获取数组的长度。
        int length = ArrayUtils.getLength(myArray);
        System.out.println("Length of the array: " + length);

        // 查找数组中指定元素的索引。
        String myElement = "B";
        int index = ArrayUtils.indexOf(myArray, myElement);
        System.out.println("Element found at index: " + index);

        // 判断数组是否包含指定元素。
        boolean containsOnly = ArrayUtils.containsAny(myArray, "A", "B", "C");
        System.out.println("Array contains only specified elements: " + containsOnly);

        // 合并两个数组。
        String[] myArray1 = {"D", "E"};
        String[] myArray2 = {"F", "G"};
        String[] mergedArray = ArrayUtils.addAll(myArray1, myArray2);
        System.out.println("Merged array: " + Arrays.toString(mergedArray));
    }

    @Test
    public void _test2() {
        String[] myArray = {"A", "B", "C"};
        System.out.println(Arrays.toString(myArray)); // [A, B, C]

        // 添加元素
        String[] addArray = ArrayUtils.add(myArray, "D");
        System.out.println(Arrays.toString(myArray)); // [A, B, C]
        System.out.println(Arrays.toString(addArray)); // [A, B, C, D]

        // 反转数组
        ArrayUtils.reverse(myArray);
        System.out.println(Arrays.toString(myArray)); // [C, B, A]

        // 删除元素
        String[] removeArray = ArrayUtils.remove(myArray, 1);
        System.out.println(Arrays.toString(myArray)); // [C, B, A]
        System.out.println(Arrays.toString(removeArray)); // [C, A]

        // 判断数组是否为空
        boolean empty = ArrayUtils.isEmpty(myArray);
        System.out.println(empty);
        boolean notEmpty = ArrayUtils.isNotEmpty(myArray);
        System.out.println(notEmpty);
    }
}
