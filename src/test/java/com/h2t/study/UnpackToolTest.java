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
        String sourcePath = "input/springboot-log.zip";
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
        String sourcePath = "input/学习.rar";
        String targetPath = "unpack-output/";
        File file = new File(sourcePath);
        //方式一
        UnpackUtil.unpackRar(file, targetPath);
        //方式二
        UnpackUtil.unpackRar(sourcePath, targetPath);
    }

    /**
     * 解压tar.gz为tar测试
     */
    @Test
    public void gzUnpackTest() throws Exception {
        String sourcePath = "input/springboot-log.tar";
        String targetPath = "unpack-output/";
        UnpackUtil.unpackTar(sourcePath, targetPath);
    }

    /**
     * 解压tar测试
     */
    @Test
    public void tarUnpackTest() {
        String sourcePath = "input/springboot-log.tar.gz";
        String targetPath = "unpack-output/";
        UnpackUtil.unpackGz(sourcePath, targetPath);
    }

    /**
     * 解压tar.gz测试
     */
    @Test
    public void tarGzUnpackTest() {
        String sourcePath = "input/springboot-log.tar.gz";
        String targetPath = "unpack-output/";
        UnpackUtil.unpackTarGz(sourcePath, targetPath);
    }
}
