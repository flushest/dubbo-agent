package com.alibaba.dubbo.agent;

import org.junit.Test;

public class SimpleTest {
    @Test
    public void printArrayType() {
        String[] args = new String[0];
        System.out.println(args.getClass().getName());
        System.out.println(String.class.getName());
    }
}
