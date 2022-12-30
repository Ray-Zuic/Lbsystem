package com.lbsys.serviceimpl;

import com.lbsys.pojo.Winfo;
import com.lbsys.service.weightService;
import com.lbsys.tools.Linuxcmd;
import com.lbsys.tools.OSUtils;

import java.math.BigDecimal;

public class weightServiceImpl implements weightService {


    @Override
    public String getHardInfo() throws Exception {
        double cpu = OSUtils.cpuUsage();
        double mem = OSUtils.memoryUsage();
        double disk = OSUtils.disk();
        double net = OSUtils.netusage();

        Winfo winfo = new Winfo();
        winfo.setCpu(0.4);
        winfo.setMem(0.4);
        winfo.setDisk(0.1);
        winfo.setNet(0.1);

        double result = Math.pow(cpu, winfo.getCpu())
                        +Math.pow(mem, winfo.getMem())
                        +Math.pow(disk, winfo.getDisk())
                        +Math.pow(net, winfo.getNet());

        BigDecimal b1 = new BigDecimal(result);
        double r = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return r+"";
    }
}
