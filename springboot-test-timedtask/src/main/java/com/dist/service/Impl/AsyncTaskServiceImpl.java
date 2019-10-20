package com.dist.service.Impl;

import com.dist.service.AsyncTaskService;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/10/9 11:30
 */
public class AsyncTaskServiceImpl implements AsyncTaskService {

    @Override
    public Object get1() {
        try {
            System.out.println("进入get1");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public Object get2() {
        try {
            System.out.println("进入get2");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2;
    }

    @Override
    public Object get3() {
        try {
            System.out.println("进入get3");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 3;
    }

    @Override
    public Object get4() {
        try {
            System.out.println("进入get4");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 4;
    }
}
