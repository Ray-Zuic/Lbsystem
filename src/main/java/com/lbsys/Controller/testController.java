package com.lbsys.Controller;

import com.lbsys.config.FiveSecTime;
import com.lbsys.service.logService;
import com.lbsys.service.weightService;
import com.lbsys.serviceimpl.logServiceImpl;
import com.lbsys.serviceimpl.weightServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    logService service = new logServiceImpl();
    weightService wservice = new weightServiceImpl();
    @RequestMapping("/one")//可用
    public String getLastInfo() throws Exception{
        return "123";
    }

    @RequestMapping("/log")
    public void wlog() throws Exception{
        service.writelog();
        System.out.println("already write a log");
    }

    @RequestMapping("/clog")
    public void clog() throws Exception{
        service.cleanlog();
        System.out.println("already clean the log file");
    }

    @RequestMapping("/rlog")
    public String rlog() throws Exception{
        String result = service.getLast();
        return result;
    }
    @RequestMapping("/getWinfo")
    public String getWinfo() throws Exception{
        return FiveSecTime.weight;
    }

}
