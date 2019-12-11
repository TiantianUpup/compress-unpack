package com.h2t.study;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * 解压工具类测试
 *
 * @author hetiantian
 * @version 1.0
 * @Date 2019/12/11 15:35
 */
public class UnpackToolTest extends BaseTest {
    /**
     * 解压zip测试
     */
    @Test
    public void zipUnpackTest() throws IOException {
        File file = new File("input/springboot-log.zip");
        UnpackTool.unpackZip(file, "unpack-output/");
    }

    /**
     * 解压rar测试
     */
    @Test
    public void rarUnpackTest() throws Exception {
        File file = new File("input/学习.rar");
        UnpackTool.unpackRar(file, "unpack-output/");
    }
}
