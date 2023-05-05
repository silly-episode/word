package com.boot;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Properties;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 11:14
 * @FileName: SystemTest
 * @Description:
 */
public class SystemTest {

    @Test
    public void Test() throws FileNotFoundException, UnknownHostException {
        DecimalFormat df = new DecimalFormat("#.##");

//        /*系统内存和JVM内存*/
//        GlobalMemory memory = OshiUtil.getMemory();
//        System.out.println("系统内存和JVM内存:++++++++++++++++++++");
//        double memoryTotal = memory.getTotal() * 1.0 / 1024 / 1024 / 1024;
//        double remainMemory = memory.getAvailable() * 1.0 / 1024 / 1024 / 1024;
//        double usedMemory = memoryTotal - remainMemory;
//        double memoryPercent = 100 * usedMemory / memoryTotal;
//        System.out.println("系统内存总量：" + Double.parseDouble(df.format(memoryTotal)) + "GB");
//        System.out.println("系统已用内存：" + Double.parseDouble(df.format(usedMemory)) + "GB");
//        System.out.println("系统剩余内存：" + Double.parseDouble(df.format(remainMemory)) + "GB");
//        System.out.println("系统内存使用率：" + Double.parseDouble(df.format(memoryPercent)) + "%");
//
//        double jvmMemoryTotal = Runtime.getRuntime().totalMemory() * 1.0 / 1024 / 1024;
//        double jvmRemainMemory = Runtime.getRuntime().freeMemory() * 1.0 / 1024 / 1024;
//        double jvmUsedMemory = jvmMemoryTotal - jvmRemainMemory;
//        double jvmMemoryPercent = jvmUsedMemory * 100 / jvmMemoryTotal;
//        System.out.println("JVM内存总量：" + Double.parseDouble(df.format(jvmMemoryTotal)) + "MB");
//        System.out.println("JVM已用内存：" + Double.parseDouble(df.format(jvmUsedMemory)) + "MB");
//        System.out.println("JVM剩余内存：" + Double.parseDouble(df.format(jvmRemainMemory)) + "MB");
//        System.out.println("JVM内存使用率：" + Double.parseDouble(df.format(jvmMemoryPercent)) + "%");
//
//        System.out.println("\n\n");
//
//        /*CPU信息*/
//        System.out.println("CPU信息:++++++++++++++++++++");
//
//        /**
//         * 构造
//         *
//         * @param cpuNum   CPU核心数
//         * @param toTal    CPU总的使用率
//         * @param sys      CPU系统使用率
//         * @param user     CPU用户使用率
//         * @param wait     CPU当前等待率
//         * @param free     CPU当前空闲率
//         * @param cpuModel CPU型号信息
//         */
//        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
//        int cpuNum = cpuInfo.getCpuNum();
//        double sys = cpuInfo.getSys();
//        double user = cpuInfo.getUser();
//        double free = cpuInfo.getFree();
//        String cpuModel = cpuInfo.getCpuModel().split("\\n")[0];
//
//        System.out.println("CPU核心数: " + cpuNum);
//        System.out.println("CPU系统使用率: " + sys);
//        System.out.println("CPU用户使用率: " + user);
//        System.out.println("CPU当前空闲率: " + free);
//        System.out.println("CPU型号信息: " + cpuModel);
//
//
//        System.out.println("\n\n");


        //        /*外存信息*/
//        System.out.println("外存信息:++++++++++++++++++++");
//
//        OshiUtil.getOs().getBitness();
//
//
//        List<OSFileStore> list = OshiUtil.getOs().getFileSystem().getFileStores();
//        for (OSFileStore store : list) {
//            System.out.println(store);
//        }
//
//        System.out.println("\n\n");

        /*服务器信息*/
        System.out.println("服务器信息:++++++++++++++++++++");
        Properties props = System.getProperties();
        Map<String, String> envs = System.getenv();
        System.out.println("操作系统的名称：" + props.get("os.name"));
        System.out.println("操作系统的版本：" + props.get("os.version"));
        System.out.println("操作系统的架构：" + props.get("os.arch"));
        System.out.println("用户名：" + props.get("user.name"));
        System.out.println("计算机名：" + envs.get("COMPUTERNAME"));
        System.out.println("IP：" + InetAddress.getLocalHost().getHostAddress());


        System.out.println("\n\n");

//        /*JVM信息*/
//        System.out.println("JVM信息:++++++++++++++++++++");
//        Properties props = System.getProperties();
//        String name=ManagementFactory.getRuntimeMXBean().getVmName();
//        long currentProcessIdentifier = Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
//        long timestamp  = ManagementFactory.getRuntimeMXBean().getStartTime();
//        System.out.println(timestamp);
//        LocalDateTime startTime=LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
//        File path = new File(ResourceUtils.getURL("classpath:").getPath());
//        if(!path.exists()) path = new File("");
//
//        System.out.println("path:"+path.getAbsolutePath());
//        System.out.println("名称 : "+name);
//        System.out.println("java版本：" + props.getProperty("java.version"));
//        System.out.println("java安装目录：" + props.getProperty("java.home"));
//        System.out.println("处理级别：" + System.getenv("PROCESSOR_LEVEL"));
//        System.out.println("Jvm启动时间："+ startTime);
//        System.out.println("Pid  " + currentProcessIdentifier);
//
//        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
//        List<String> aList = bean.getInputArguments();
//        for (Object o : aList) {
//            System.out.println(o);
//
//        }
//        System.out.println("\n\n");


    }


}
