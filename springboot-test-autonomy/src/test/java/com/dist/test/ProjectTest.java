package com.dist.test;

import org.junit.Test;

import java.net.URISyntaxException;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/18 13:27
 */
public class ProjectTest {
    @Test
    public void test() throws URISyntaxException {
        String templates = this.getClass().getClassLoader().getResource("templates").toURI().getPath();
        System.out.println("templates="+templates);
    }
}
