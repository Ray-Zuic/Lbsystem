package com.lbsys.service;

import org.springframework.stereotype.Service;

@Service
public interface logService {
    //处理请求记录一个日志
    public void writelog() throws Exception;

    //清空日志
    public void cleanlog() throws Exception;

    //获取最后的记录数
    public String getLast() throws Exception;
}
