package com.h2t.study;

import com.h2t.study.util.CompressUtil;
import org.junit.jupiter.api.Test;

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
        CompressUtil.compressToZip(sourcePath, targetPath);
    }

    /**
     * 压缩为tar测试
     */
    @Test
    public void tarCompressTest() throws IOException {
        String sourcePath = "input/springboot-log";
        String targetPath = "compress-output/";
        CompressUtil.compressToTar(sourcePath, targetPath);
    }

    /**
     * 压缩为gz测试
     */
    @Test
    public void gzCompressTest() throws IOException {
        String sourcePath = "input/springboot-log.tar";
        String targetPath = "compress-output/";
        CompressUtil.compressTarToGz(sourcePath, targetPath);
    }

    /**
     * 压缩为tar.gz测试
     */
    @Test
    public void tarGzCompressTest() {
        String sourcePath = "input/springboot-log";
        String targetPath = "compress-output/";
        CompressUtil.compressToTarGz(sourcePath, targetPath);
    }
}
