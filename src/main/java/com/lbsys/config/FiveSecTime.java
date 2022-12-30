package com.lbsys.config;

import com.lbsys.service.weightService;
import com.lbsys.serviceimpl.weightServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class FiveSecTime {
    public static String weight;

    @Scheduled(cron="10 * * * * ?")
    private  void configureTasks() throws Exception{
        weightServiceImpl wservice = new weightServiceImpl();
        weight= wservice.getHardInfo();
    }
}
