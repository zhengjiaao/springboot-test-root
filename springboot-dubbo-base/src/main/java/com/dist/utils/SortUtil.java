package com.dist.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author yangmin
 * @date 2019/3/5 16:38
 * @desc
 */
public class SortUtil {

	/**
	 * 冒泡排序
	 * 
	 * @param list
	 */
	public static void bubbleSort(List<Integer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - 1 - i; j++) {
				if (list.get(j) > list.get(j + 1)) {
					Integer temp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, temp);
				}
			}
		}
	}

	/**
	 * 用于对象的冒泡排序，list中的对象需要实现Comparable接口，实现compareTo方法（自定义）
	 * @author jiangyk
	 * @param <T>
	 * @param list
	 */
	public static <T extends Comparable<T>> void bubbleSortWithObject(List<T> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - 1 - i; j++) {
				// 如果前面的元素比后面的大，交换
				if (list.get(j).compareTo(list.get(j + 1)) == 1) {
					T temp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, temp);
				}
			}
		}
	}

	public static void main(String[] args) {
		Student student1 = new Student();
		student1.setNumber(5);
		Student student2 = new Student();
		student2.setNumber(2);
		Student student3 = new Student();
		student3.setNumber(1);
		Student student4 = new Student();
		student4.setNumber(4);
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(student1);
		list.add(student2);
		list.add(student3);
		list.add(student4);
		System.out.println("-------排序前-------");
		Iterator<Student> iterator = list.iterator();
		while (iterator.hasNext()) {
			Student stu = iterator.next();
			System.out.println(stu.getNumber());
		}
		// 使用Collections的sort方法对list进行排序
		System.out.println("-------排序后-------");
		bubbleSortWithObject(list);
		iterator = list.iterator();
		while (iterator.hasNext()) {
			Student stu = iterator.next();
			System.out.println(stu.getNumber());
		}

	}
}

class Student implements Comparable<Student> {
	
	private int number = 0; // 学号

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public int compareTo(Student student) {
		if (this.number == student.number) {
			return 0; // 如果学号相同，那么两者就是相等的
		} else if (this.number > student.getNumber()) {
			return 1; // 如果这个学生的学号大于传入学生的学号
		} else {
			return -1; // 如果这个学生的学号小于传入学生的学号
		}
	}
}