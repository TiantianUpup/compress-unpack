package com.h2t.study.util;

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
            LOGGER.error("the file is not exist, file name: {}", file.getName());
            throw new CustomException("the file is not exist");
        }

        return file.delete();
    }

    /**
     * 源文件路径判断
     *
     * @param sourcePath 待解压文件路径
     * @return
     */
    public static File validateSourcePath(String sourcePath) {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            LOGGER.error("the source file is not exist, source path: {}", sourceFile.getAbsolutePath());
            throw new CustomException("the source file is not exist");
        }
        return sourceFile;
    }

    /**
     * 解压路径存在判断
     *
     * @param targetPath
     * @return
     */
    public static File validateTargetPath(String targetPath) {
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            LOGGER.info("the target file is not exist, target path: {}. create", targetFile.getAbsolutePath());
            targetFile.mkdirs();
        }

        return targetFile;
    }
}
