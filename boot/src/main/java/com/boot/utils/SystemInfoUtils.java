package com.boot.utils;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.boot.bo.MyCpuInfo;
import com.boot.bo.MyFileInfo;
import com.boot.bo.MyMemoryInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 15:36
 * @FileName: SystemInfoUtils
 * @Description:
 */
public class SystemInfoUtils {
    static DecimalFormat df = new DecimalFormat("#.##");

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

            String usableSpace = convertFileSize(store.getUsableSpace());

            String totalSpace = convertFileSize(store.getTotalSpace());

            String usablePercent = Double.parseDouble(df.format(100.0 * store.getUsableSpace() / store.getTotalSpace())) + " %";

            info
                    .setFreeSpace(freeSpace)
                    .setUsableSpace(usableSpace)
                    .setTotalSpace(totalSpace)
                    .setUsablePercent(usablePercent);

            newList.add(info);
        }

        return newList;
    }


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



