package com.boot.utils;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.boot.bo.*;
import org.springframework.util.ResourceUtils;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 15:36
 * @FileName: SystemInfoUtils
 * @Description:
 */
public class SystemInfoUtils {
    static DecimalFormat df = new DecimalFormat("#.##");

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 内存和jvm信息
     * @Date: 2023/5/5 11:16
     */
    public static MyMemoryInfo getMemory() {
        /*系统内存和JVM内存*/
        GlobalMemory memory = OshiUtil.getMemory();
        double memoryTotal = memory.getTotal() * 1.0 / 1024 / 1024 / 1024;
        double remainMemory = memory.getAvailable() * 1.0 / 1024 / 1024 / 1024;
        double usedMemory = memoryTotal - remainMemory;
        double memoryPercent = 100 * usedMemory / memoryTotal;
        double jvmMemoryTotal = Runtime.getRuntime().totalMemory() * 1.0 / 1024 / 1024;
        double jvmRemainMemory = Runtime.getRuntime().freeMemory() * 1.0 / 1024 / 1024;
        double jvmUsedMemory = jvmMemoryTotal - jvmRemainMemory;
        double jvmMemoryPercent = jvmUsedMemory * 100 / jvmMemoryTotal;

        return new MyMemoryInfo()
                .setJvmMemoryPercent(Double.parseDouble(df.format(jvmMemoryPercent)) + " %")
                .setJvmMemoryTotal(Double.parseDouble(df.format(jvmMemoryTotal)) + " MB")
                .setJvmRemainMemory(Double.parseDouble(df.format(jvmRemainMemory)) + " MB")
                .setJvmUsedMemory(Double.parseDouble(df.format(jvmUsedMemory)) + " MB")
                .setMemoryPercent(Double.parseDouble(df.format(memoryPercent)) + " %")
                .setMemoryTotal(Double.parseDouble(df.format(memoryTotal)) + " GB")
                .setRemainMemory(Double.parseDouble(df.format(remainMemory)) + " GB")
                .setUsedMemory(Double.parseDouble(df.format(usedMemory)) + " GB");
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO cpu信息
     * @Date: 2023/5/5 11:16
     */
    public static MyCpuInfo getCpuInfo() {
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        String cpuModel = cpuInfo.getCpuModel().split("\\n")[0];
        /*核心数量*/
        int cpuNum = cpuInfo.getCpuNum();
        /*系统使用率*/
        String sys = cpuInfo.getSys() + "%";
        /*用户使用率*/
        String user = cpuInfo.getUser() + "%";
        /*当前空闲率*/
        String free = cpuInfo.getFree() + "%";

        return new MyCpuInfo()
                .setCpuModel(cpuModel)
                .setCpuNum(cpuNum)
                .setFree(free)
                .setSys(sys)
                .setUser(user);
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 外存信息
     * @Date: 2023/5/5 11:16
     */
    public static List<MyFileInfo> getFileInfo() {

        List<OSFileStore> oldList = OshiUtil.getOs().getFileSystem().getFileStores();
        List<MyFileInfo> newList = new ArrayList<>(oldList.size());

        for (OSFileStore store : oldList) {
            MyFileInfo info = new MyFileInfo()
                    .setLabel(store.getLabel())
                    .setMount(store.getMount())
                    .setName(store.getName())
                    .setFsType(store.getType());

            String freeSpace = convertFileSize(store.getFreeSpace());

            String unFreeSpace = convertFileSize(store.getTotalSpace() - store.getFreeSpace());

            String totalSpace = convertFileSize(store.getTotalSpace());

            String unFreePercent = Double.parseDouble(df.format(100.0 * (store.getTotalSpace() - store.getFreeSpace()) / store.getTotalSpace())) + " %";

            info
                    .setFreeSpace(freeSpace)
                    .setUnFreeSpace(unFreeSpace)
                    .setTotalSpace(totalSpace)
                    .setUnFreePercent(unFreePercent);

            newList.add(info);
        }

        return newList;
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO Jvm运行信息
     * @Date: 2023/5/5 11:18
     */
    public static MyJvmInfo getJvmInfo() throws FileNotFoundException {
        Properties props = System.getProperties();
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();

        String jvmName = bean.getVmName();

        String jvmVersion = props.getProperty("java.vm.version");

        long timeStamp = bean.getStartTime();
        LocalDateTime jvmStartTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());

        // 依次定义 天、时、分、秒
//        long nd = 1000 * 24 * 60 * 60;
//        long nh = 1000 * 60 * 60;
//        long nm = 1000 * 60;
//        long ns = 1000;
        long diff = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - timeStamp;
        String jvmRunTime = DateUtil.formatBetween(diff, BetweenFormatter.Level.SECOND);

        String jvmPath = props.getProperty("java.home");

        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");
        }
        String projectPath = path.getAbsolutePath();

        String jvmPid = bean.getName().split("@")[0];

        String processingLevel = System.getenv("PROCESSOR_LEVEL");

        String runParams = String.valueOf(bean.getInputArguments());

        return new MyJvmInfo()
                .setJvmName(jvmName)
                .setJvmPath(jvmPath)
                .setJvmPid(jvmPid)
                .setJvmRunTime(jvmRunTime)
                .setJvmVersion(jvmVersion)
                .setJvmStartTime(jvmStartTime)
                .setProjectPath(projectPath)
                .setRunParams(runParams)
                .setProcessingLevel(processingLevel);
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 服务器信息
     * @Date: 2023/5/5 11:25
     */
    public static MyServerInfo getServerInfo() throws UnknownHostException {
        Properties props = System.getProperties();
        Map<String, String> envs = System.getenv();

        String serverName = envs.get("COMPUTERNAME");

        String serverSystem = props.getProperty("os.name");

        String serverIp = InetAddress.getLocalHost().getHostAddress();

        String serverArchitecture = props.getProperty("os.arch");

        String serverUser = envs.get("USERNAME");

        String serverVersion = props.getProperty("os.version");

        return new MyServerInfo()
                .setServerIp(serverIp)
                .setServerName(serverName)
                .setServerArchitecture(serverArchitecture)
                .setServerSystem(serverSystem)
                .setServerUser(serverUser)
                .setServerVersion(serverVersion);
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 单位换算
     * @Date: 2023/5/5 10:47
     */
    private static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}



