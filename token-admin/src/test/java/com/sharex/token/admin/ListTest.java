package com.sharex.token.admin;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListTest {

    @Test
    public void linkedList() {

        List<String> stringList = new LinkedList<>();

        // 尾插 linkLast(e);
        stringList.add("a1");
        stringList.add("a2");
        stringList.add("a3");

        // 头插 linkBefore(element, node(index));
        stringList.add(0, "a4");

        // 尾插 linkLast(e);
        stringList.add(4, "a5");

        //public void add(int index, E element) {
        //    checkPositionIndex(index);
        //
        //    if (index == size)
        //        linkLast(element);
        //    else
        //        linkBefore(element, node(index));
        //}
        // 查找位置 node(index) 的时候做了一个简单的算法
        // 如果 index < size/2
        //   从头节点开始找
        // 否则
        //   从尾节点开始找

        stringList.forEach(str -> System.out.println(str));
    }

    @Test
    public void arrayList() {

        // new ArrayList<>() == {}
        List<String> stringList = new ArrayList<>();

        // ensureCapacityInternal(size + 1);
        // elementData == {} 进行扩容
        // minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);

        // initialCapacity > 0
        // Object[] objects = new Object[10];

        // initialCapacity 默认最大值 Integer.MAX_VALUE - 8
        // initialCapacity 可设置最大值 Integer.MAX_VALUE

        // 尾插
        stringList.add("a1");
        stringList.add("a2");
        stringList.add("a3");

        // 头插
        stringList.add(0, "a4");

        // 位置插入
        stringList.add(4, "a5");

        // 扩容问题
        // elementData = Arrays.copyOf(elementData, newCapacity);
        // System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));

        stringList.forEach(str -> System.out.println(str));
    }
}
