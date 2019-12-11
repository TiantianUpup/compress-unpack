package com.h2t.study;

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
        CompressTool.compressToZip("input/springboot-log", "compress-output/");
    }
}
