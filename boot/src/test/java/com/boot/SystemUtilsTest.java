package com.boot;

import com.boot.bo.MyJvmInfo;
import com.boot.utils.SystemInfoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/5 10:48
 * @FileName: SystemUtilsTest
 * @Description:
 */

@SpringBootTest
public class SystemUtilsTest {

    @Test
    public void test() throws FileNotFoundException, UnknownHostException {


//        MyMemoryInfo memoryInfo = SystemInfoUtils.getMemory();
//        System.out.println(memoryInfo);
//        MyCpuInfo myCpuInfo = SystemInfoUtils.getCpuInfo();
//        System.out.println(myCpuInfo);
//        List<MyFileInfo> fileInfoList = SystemInfoUtils.getFileInfo();
//        System.out.println(fileInfoList);
        MyJvmInfo jvmInfo = SystemInfoUtils.getJvmInfo();
        System.out.println(jvmInfo);
//        MyServerInfo serverInfo = SystemInfoUtils.getServerInfo();
//        System.out.println(serverInfo);

    }

}
