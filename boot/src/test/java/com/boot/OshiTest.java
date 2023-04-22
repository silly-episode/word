package com.boot;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import org.junit.jupiter.api.Test;
import oshi.SystemInfo;
import oshi.hardware.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 9:37
 * @FileName: OshiTest
 * @Description:
 */
public class OshiTest {

    @Test
    public void main() {
        //获取cpu利用率
        getOsInfo();
        //获取内存数据
        getMemoryInfo();
        //获取硬盘使用量
        getDiskUsed();
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        NetworkIF[] nets = hal.getNetworkIFs().toArray(new NetworkIF[0]);
        System.out.println("网络开始===========");
        for (NetworkIF net : nets) {
            System.out.println("ipv4" + Arrays.toString(net.getIPv4addr()));
            System.out.println("网络接收" + net.getBytesRecv() + "字节");
            System.out.println("网络发送" + net.getBytesSent() + "字节");
            System.out.println("显示名称" + net.getDisplayName());
            System.out.println("mac地址" + net.getMacaddr());
            System.out.println("ipv6" + Arrays.toString(net.getIPv6addr()));
            System.out.println("名称" + net.getName());
            System.out.println("速度" + net.getSpeed() + "千兆/百兆");
            System.out.println("发包" + net.getPacketsSent());
            System.out.println("收包" + net.getPacketsRecv());
        }
        System.out.println("网络结束===========");
        HWDiskStore[] diskStores = hal.getDiskStores().toArray(new HWDiskStore[0]);
        System.out.println("硬盘信息开始===========");
        for (HWDiskStore diskStore : diskStores) {
            System.out.println("名称" + diskStore.getName());
            System.out.println("大小" + diskStore.getSize());
            //对应着设备看，我的是希捷的
            // ST：代表bai希捷
            // 3：代表du3.5吋硬盘zhi
            // 500：代表硬盘容量为500G
            // 4：指的是dao硬盘的缓存数zhuan是16M(2的4次方)
            // 1：代表1张盘片shu
            // 8：版本号，也可能对应产地等
            // AS：Serial ATA，即 SATA 硬盘接口。（A，代表ATA接口）
            System.out.println("硬盘详细信息" + diskStore.getModel());
            System.out.println("串行接口" + diskStore.getSerial());
            System.out.println("当前队列长度" + diskStore.getCurrentQueueLength());
        }
        System.out.println("硬盘信息结束===========");
        PowerSource[] powerSources = hal.getPowerSources().toArray(new PowerSource[0]);
        System.out.println("电源开始==========");
        for (PowerSource powerSource : powerSources) {
            System.out.println("电源名称" + powerSource.getName());
            System.out.println("剩余时间" + powerSource.getTimeRemainingEstimated());
            System.out.println("剩余容量" + powerSource.getRemainingCapacityPercent());
        }
        System.out.println("电源结束==========");
        SoundCard[] soundCards = hal.getSoundCards().toArray(new SoundCard[0]);
        System.out.println("声卡开始==========");
        for (SoundCard soundCard : soundCards) {
            System.out.println("名称" + soundCard.getName());
            //编译码器。指的是数字通信中具有编码、译码功能的器件。
            System.out.println("多媒体数字信号编解码器" + soundCard.getCodec());
            System.out.println("运行版本" + soundCard.getDriverVersion());
        }
        System.out.println("声卡结束==========");
        //tree代表是否用嵌套结构进行展示
        UsbDevice[] usbDevices = hal.getUsbDevices(false).toArray(new UsbDevice[0]);
        System.out.println("usb设备开始============");
        for (UsbDevice usbDevice : usbDevices) {
            System.out.println("名称" + usbDevice.getName());
            System.out.println("产品id" + usbDevice.getProductId());
            System.out.println("序列号" + usbDevice.getSerialNumber());
            System.out.println("设备厂商" + usbDevice.getVendor());
            System.out.println("设备厂商id" + usbDevice.getVendorId());
        }
        System.out.println("usb设备结束============");

        ComputerSystem computerSystem = hal.getComputerSystem();
        System.out.println("电脑系统开始=========");
        System.out.println("模型" + computerSystem.getModel());
        System.out.println("序列号" + computerSystem.getSerialNumber());
        System.out.println("制造商" + computerSystem.getManufacturer());
        System.out.println("底板开始========");
        Baseboard baseboard = computerSystem.getBaseboard();
        System.out.println("版本" + baseboard.getVersion());
        System.out.println("序列号" + baseboard.getSerialNumber());
        System.out.println("模型" + baseboard.getModel());
        System.out.println("厂商" + baseboard.getManufacturer());
        System.out.println("底板结束========");
        Firmware firmware = computerSystem.getFirmware();
        System.out.println("固件开始==========");
        System.out.println("固件名" + firmware.getName());
        System.out.println("厂商" + firmware.getManufacturer());
        System.out.println("固件描述" + firmware.getDescription());
        System.out.println("发布日期" + firmware.getReleaseDate());
        System.out.println("固件结束==========");
        System.out.println("电脑系统结束=========");
        System.out.println("传感器开始===============");
        Sensors sensors = hal.getSensors();
        System.out.println("风扇转速" + Arrays.toString(sensors.getFanSpeeds()));
        System.out.println("cpu温度" + sensors.getCpuTemperature());
        System.out.println("cpu电压" + sensors.getCpuVoltage());
        System.out.println("传感器结束===============");

    }

    /**
     * 获取cpu利用率
     */
    public void getOsInfo() {
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        double free = cpuInfo.getFree();
        DecimalFormat format = new DecimalFormat("#.00");
        System.out.println("cpu利用率：" + Double.parseDouble(format.format(100.0D - free)));
    }

    /**
     * 获取内存数据
     */
    public void getMemoryInfo() {
        System.out.println("内存总量：" + OshiUtil.getMemory().getTotal() / 1024 / 1024);
        System.out.println("已用内存：" + OshiUtil.getMemory().getAvailable() / 1024 / 1024);
    }

    /**
     * 获取硬盘使用量
     */
    public void getDiskUsed() {
        File win = new File("/");
        if (win.exists()) {
            long total = win.getTotalSpace();
            long freeSpace = win.getFreeSpace();
            System.out.println("磁盘总量：" + total / 1024 / 1024 / 1024);
            System.out.println("磁盘剩余总量：" + freeSpace / 1024 / 1024 / 1024);
            System.out.println("磁盘已用总量：" + (total - freeSpace) / 1024 / 1024 / 1024);
        }
    }


}
