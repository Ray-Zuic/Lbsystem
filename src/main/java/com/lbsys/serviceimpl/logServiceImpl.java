package com.lbsys.serviceimpl;


import com.lbsys.service.logService;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;

public class logServiceImpl implements logService {

    int times = 1;
    int flag = 1;
    @Override
    public void writelog() throws Exception {//写入日志
        if(flag==0){
            times=1;
            flag=1;
        }
        //此处设置为true即可追加
        FileWriter out = new FileWriter("1.txt",true);
        //往文件写入
        String content = times+"";
        times++;
        out.write(content);
        //换行
        out.write("\n");
        //刷新IO内存流
        out.flush();
        //关闭
        out.close();
    }

    @Override
    public void cleanlog() throws Exception {
        //此处设置为true即可追加
        FileWriter out = new FileWriter("1.txt");
        //往文件写入空内容
        String content = "";
        out.write(content);
        //刷新IO内存流
        out.flush();
        //关闭
        out.close();
        flag=0;
    }

    @Override
    public String getLast() throws Exception {
        File file = new File("1.txt");
        String lastLine = "";
        try (ReversedLinesFileReader reversedLinesReader = new ReversedLinesFileReader(file, Charset.defaultCharset())) {
            lastLine = reversedLinesReader.readLine();
        } catch (Exception e) {
            System.out.println("读取出错");
        }
        return lastLine;
    }

}
