package com.h2t.study;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * 压缩工具测试类
 *
 * @author hetiantian
 * @version 1.0
 * @Date 2019/12/11 11:48
 */
public class CompressToolTest extends BaseTest {
    /**
     * 压缩为zip测试
     */
    @Test
    public void zipCompressTest() throws IOException {
        String sourcePath = "input/springboot-log";
        String targetPath = "compress-output/";
        File sourceFile = new File(sourcePath);
        //方式一
        CompressUtil.compressToZip(sourceFile, targetPath);
        //方式二
        CompressUtil.compressToZip(sourcePath, targetPath);
    }

    /**
     * 压缩为tar测试
     */
    @Test
    public void tarCompressTest() throws IOException {
        String sourcePath = "input/springboot-log";
        String targetPath = "compress-output/";
        File sourceFile = new File(sourcePath);
        System.out.println("target path :-----------" + CompressUtil.compressToTar(sourcePath, targetPath));
    }

    /**
     * 压缩为gz测试
     */
    @Test
    public void gzCompressTest() throws IOException {
        String sourcePath = "input/springboot-log.tar";
        String targetPath = "compress-output/";
        File sourceFile = new File(sourcePath);
        CompressUtil.compressToGz(sourcePath, targetPath);
    }

    /**
     * 将tar压缩为gz测试
     */
    @Test
    public void tarGzCompressTest() {
        String sourcePath = "input/springboot-log";
        String targetPath = "compress-output/";
        CompressUtil.compressToTarGz(sourcePath, targetPath);
    }
}
