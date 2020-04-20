package com.h2t.study;

import com.h2t.study.util.UnpackUtil;
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
        String sourcePath = "1.zip";
        String targetPath = "unpack-output/";
        File file = new File(sourcePath);
        //方式一
        UnpackUtil.unpackZip(file, targetPath);
        //方式二
        UnpackUtil.unpackZip(sourcePath, targetPath);
    }

    /**
     * 解压rar测试
     */
    @Test
    public void rarUnpackTest() throws Exception {
        String sourcePath = "input/算法SDK_2.rar";
        String targetPath = "unpack-output";
        File file = new File(sourcePath);
        //方式一
        UnpackUtil.unpackRar(sourcePath, targetPath);
        //方式二
        //UnpackUtil.unRar(file, targetPath);
        //UnpackUtil.RarFiles(sourcePath, targetPath);
    }

    /**
     * 解压tar.gz测试
     */
    @Test
    public void tarGzUnpackTest() throws IOException {
        String sourcePath = "input/test.tar.gz";
        String targetPath = "unpack-output/";
        UnpackUtil.unpackTarGz(sourcePath, targetPath);
    }
}
