package com.lbsys.tools;

import java.io.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.commons.io.FileSystemUtils;

public class OSUtils {


    private final static float TotalBandwidth = 100;    //网口带宽,Mbps

    /**
     * 功能：可用磁盘
     */
    public static double disk() {
        try {
            long total = FileSystemUtils.freeSpaceKb("/home");
            double disk = (double) total / 1024 / 1024;

            BigDecimal r = new BigDecimal(1 - disk);
            double result = r.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return disk;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 功能：获取Linux系统cpu使用率
     */
    public static double cpuUsage() {
        try {
            Map<?, ?> map1 = OSUtils.cpuinfo();
            Thread.sleep(5 * 1000);
            Map<?, ?> map2 = OSUtils.cpuinfo();

            long user1 = Long.parseLong(map1.get("user").toString());
            long nice1 = Long.parseLong(map1.get("nice").toString());
            long system1 = Long.parseLong(map1.get("system").toString());
            long idle1 = Long.parseLong(map1.get("idle").toString());

            long user2 = Long.parseLong(map2.get("user").toString());
            long nice2 = Long.parseLong(map2.get("nice").toString());
            long system2 = Long.parseLong(map2.get("system").toString());
            long idle2 = Long.parseLong(map2.get("idle").toString());

            long total1 = user1 + system1 + nice1;
            long total2 = user2 + system2 + nice2;
            float total = total2 - total1;

            long totalIdle1 = user1 + nice1 + system1 + idle1;
            long totalIdle2 = user2 + nice2 + system2 + idle2;
            float totalidle = totalIdle2 - totalIdle1;

            float cpusage = (total / totalidle);

            BigDecimal r = new BigDecimal(1 - cpusage);
            double result = r.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 功能：CPU使用信息
     */
    public static Map<?, ?> cpuinfo() {
        InputStreamReader inputs = null;
        BufferedReader buffer = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            inputs = new InputStreamReader(new FileInputStream("/proc/stat"));
            buffer = new BufferedReader(inputs);
            String line = "";
            while (true) {
                line = buffer.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("cpu")) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    List<String> temp = new ArrayList<String>();
                    while (tokenizer.hasMoreElements()) {
                        String value = tokenizer.nextToken();
                        temp.add(value);
                    }
                    map.put("user", temp.get(1));
                    map.put("nice", temp.get(2));
                    map.put("system", temp.get(3));
                    map.put("idle", temp.get(4));
                    map.put("iowait", temp.get(5));
                    map.put("irq", temp.get(6));
                    map.put("softirq", temp.get(7));
                    map.put("stealstolen", temp.get(8));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                inputs.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
/*        double idle = (double)map.get("idle");
        double user = (double)map.get("user");
        double nice = (double)map.get("nice");
        double result = idle/(idle+user+nice);
        return result;*/
        return map;
    }

    /**
     * 功能：内存使用率
     */
    public static double memoryUsage() {
        Map<String, Object> map = new HashMap<String, Object>();
        InputStreamReader inputs = null;
        BufferedReader buffer = null;
        try {
            inputs = new InputStreamReader(new FileInputStream("/proc/meminfo"));
            buffer = new BufferedReader(inputs);
            String line = "";
            while (true) {
                line = buffer.readLine();
                if (line == null)
                    break;
                int beginIndex = 0;
                int endIndex = line.indexOf(":");
                if (endIndex != -1) {
                    String key = line.substring(beginIndex, endIndex);
                    beginIndex = endIndex + 1;
                    endIndex = line.length();
                    String memory = line.substring(beginIndex, endIndex);
                    String value = memory.replace("kB", "").trim();
                    map.put(key, value);
                }
            }

            long memTotal = Long.parseLong(map.get("MemTotal").toString());
            long memFree = Long.parseLong(map.get("MemFree").toString());
            long memused = memTotal - memFree;
            long buffers = Long.parseLong(map.get("Buffers").toString());
            long cached = Long.parseLong(map.get("Cached").toString());

            double usage = (double) (memused - buffers - cached) / memTotal;

            BigDecimal r = new BigDecimal(1 - usage);
            double result = r.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();


            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                inputs.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return 0;

    }

    /*
     * disk
     * */
    public static double getDiskUsage() throws Exception {
        double totalHD = 0;
        double usedHD = 0;

        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("df -hl");// df -hl 查看硬盘空间
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = null;
            String[] strArray = null;
            while ((str = in.readLine()) != null) {
                int m = 0;
                strArray = str.split(" ");
                for (String tmp : strArray) {
                    if (tmp.trim().length() == 0)
                        continue;
                    ++m;
                    if (tmp.indexOf("G") != -1) {
                        if (m == 2) {
                            if (!tmp.equals("") && !tmp.equals("0"))
                                totalHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1)) * 1024;
                        }
                        if (m == 3) {
                            if (!tmp.equals("none") && !tmp.equals("0"))
                                usedHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1)) * 1024;
                        }
                    }
                    if (tmp.indexOf("M") != -1) {
                        if (m == 2) {
                            if (!tmp.equals("") && !tmp.equals("0"))
                                totalHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1));
                        }
                        if (m == 3) {
                            if (!tmp.equals("none") && !tmp.equals("0"))
                                usedHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1));
                        }
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            in.close();
        }
        // 保留2位小数
        double precent = (usedHD / totalHD);
        BigDecimal b1 = new BigDecimal(1-precent);
        precent = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return precent;
    }


    /*
    网络占有率
    * */
    /**
     * @Purpose:采集网络带宽使用率
     * @return float,网络带宽使用率,小于1
     */
    public static double netusage() {

        float netUsage = 0.0f;
        Process pro1,pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/net/dev";
            //第一次采集流量数据
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            long inSize1 = 0, outSize1 = 0;
            while((line=in1.readLine()) != null){
                line = line.trim();
                if(line.startsWith("wlp2s0")){

                    String[] temp = line.split("\\s+");
                    //System.out.println(temp[1]);
                    inSize1 = Long.parseLong(temp[1]);    //Receive bytes,单位为Byte
                    outSize1 = Long.parseLong(temp[9]);                //Transmit bytes,单位为Byte
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));

            }
            //第二次采集流量数据
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long inSize2 = 0 ,outSize2 = 0;
            while((line=in2.readLine()) != null){
                line = line.trim();
                if(line.startsWith("wlp2s0")){
                    String[] temp = line.split("\\s+");
                    inSize2 = Long.parseLong(temp[1]);
                    outSize2 = Long.parseLong(temp[9]);
                    break;
                }
            }
            //if(inSize1 != 0 && outSize1 !=0 && inSize2 != 0 && outSize2 !=0){
                float interval = (float)(endTime - startTime)/1000;
                //网口传输速度,单位为bps
                float curRate = (float)(inSize2 - inSize1 + outSize2 - outSize1)*8/(1000000*interval);
                netUsage = curRate/TotalBandwidth;
/*                log.info("本节点网口速度为: " + curRate + "Mbps");
                log.info("本节点网络带宽使用率为: " + netUsage);*/
                //System.out.println("本节点网络带宽使用率为: " + curRate);

            //}
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        BigDecimal b1 = new BigDecimal(1-netUsage);
        double result = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }
}
