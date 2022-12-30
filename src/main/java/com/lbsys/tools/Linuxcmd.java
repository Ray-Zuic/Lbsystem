package com.lbsys.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author 杨永琪
 * @date 2021年5月9日
 */
public class Linuxcmd {

    private static String DEFAULTCHART = "UTF-8";

    public static String exec(String cm){
        String os = System.getProperty("os.name").toLowerCase();
        Process ps = null;
        InputStream er = null;
        InputStream is = null;
        String result = null;
        try {
            if(os.contains("win")){
                String[] cmd = { "cmd", "/c", cm };
                ps = Runtime.getRuntime().exec(cmd);
            }else if (os.contains("linux")){
                if(cm.contains("rm ")){
                    System.err.println("不能执行危险命令"+cm);
                    return result;
                }
                if(cm.contains("mv ")){
                    System.err.println("不能执行危险命令"+cm);
                    return result;
                }
                String[] cmd = { "/bin/sh", "-c", cm };
                ps = Runtime.getRuntime().exec(cmd);
            }
            is = ps.getInputStream();
            result = getInString(is);
//			System.err.println("执行命令："+cm+" 执行结果--<"+result+" >--");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (null != ps)
                    ps.waitFor();
                if (null != is)
                    is.close();
                if (null != er)
                    er.close();
                if (null != ps)
                    ps.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String execEnter(String cm){
        String os = System.getProperty("os.name").toLowerCase();
        Process ps = null;
        InputStream er = null;
        InputStream is = null;
        String result = null;
        try {
            if(os.contains("win")){
                String[] cmd = { "cmd", "/c", cm };
                ps = Runtime.getRuntime().exec(cmd);
            }else if (os.contains("linux")){
                String[] cmd = { "/bin/sh", "-c", cm };
                ps = Runtime.getRuntime().exec(cmd);
            }
            is = ps.getInputStream();
            result = getInStringEnter(is);
            System.err.println("执行命令："+cm+" 执行结果--<"+result+" >--");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (null != ps)
                    ps.waitFor();
                if (null != is)
                    is.close();
                if (null != er)
                    er.close();
                if (null != ps)
                    ps.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getInString(InputStream is) throws Exception{
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,DEFAULTCHART));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line + " ");
        }
        return result.toString();
    }
    public static String getInStringEnter(InputStream is) throws Exception{
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,DEFAULTCHART));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line + "\n");
        }
        return result.toString();
    }
}


