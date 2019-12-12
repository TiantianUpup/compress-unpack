package com.h2t.study;

import com.h2t.study.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件工具类
 *
 * @author hetiantian
 * @version 1.0
 * @Date 2019/12/12 11:14
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return deleteFile(file);
    }

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            LOGGER.error("the source file is not exist, source path: {}", file.getName());
            throw new CustomException("the source file is not exist");
        }

        return file.delete();
    }
}
