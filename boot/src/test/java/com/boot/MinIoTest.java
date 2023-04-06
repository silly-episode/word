package com.boot;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/31 15:49
 * @FileName: minIoTest
 * @Description:测试minIO
 */
@SpringBootTest
public class MinIoTest {

    @Autowired
    @Qualifier("Client")
    private MinioClient minioClient;

    private static void createBucket(MinioClient minioClient) throws Exception {
        boolean exists = minioClient
                .bucketExists(BucketExistsArgs.builder().bucket("miniofile1").build());
        if (exists) {
            return;
        }
        minioClient.makeBucket(MakeBucketArgs.builder().bucket("miniofile1").build());
    }

    @Test
    public void test() throws Exception {
        System.out.println(123);

        createBucket(minioClient);
        System.out.println(123);
    }


}
