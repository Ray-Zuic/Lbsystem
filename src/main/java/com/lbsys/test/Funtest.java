package com.lbsys.test;

import com.lbsys.tools.OSUtils;
import org.junit.Test;

public class Funtest {
    @Test
    public void abc() throws Exception {
        //返回结果为两位小数的空闲率
        System.out.println(OSUtils.cpuUsage());
        System.out.println(OSUtils.memoryUsage());
        System.out.println(OSUtils.getDiskUsage());
        System.out.println(OSUtils.netusage());
    }
}
