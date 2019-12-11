package com.h2t.study;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.h2t.study.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 解缩工具类
 *
 * @author hetiantian
 * @version 1.0
 * @Date 2019/12/10 10:02
 */
public class UnpackTool {
    private final static Logger LOGGER = LoggerFactory.getLogger(UnpackTool.class);
    private static final int BUFFER_SIZE = 1024;

    /**
     * 解压zip格式的压缩包
     *
     * @param sourceFile 待解压文件
     * @param targetPath 解压路径
     */
    public static void unpackZip(File sourceFile, String targetPath) {
        //参数校验
        if (!sourceFile.exists()) {
            LOGGER.error("the source file is not exist, source file name: {}", sourceFile.getName());
            throw new CustomException("the source file is not exist");
        }

        // 开始解压
        LOGGER.info("start to unpack zip, fileName:{}", sourceFile.getName());
        long start = System.currentTimeMillis();
        try (ZipFile zipFile = new ZipFile(sourceFile)) {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = targetPath + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(targetPath + File.separator + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();

                    // 将压缩文件内容写入到这个文件中
                    try (InputStream is = zipFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(targetFile)) {
                        int len;
                        byte[] buf = new byte[BUFFER_SIZE];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    }
                }

            }
            LOGGER.info("finish unpack zip, fileName:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            LOGGER.error("unpack zip throw exception:{}", e);
        }
    }

    /**
     * 解压rar格式的压缩包
     *
     * @param sourceFile 待解压文件
     * @param targetPath 解压路径
     */
    public static void unpackRar(File sourceFile, String targetPath) {
        //参数校验
        if (!sourceFile.exists()) {
            LOGGER.error("the source file is not exist, source file name: {}", sourceFile.getName());
            throw new CustomException("the source file is not exist");
        }

        LOGGER.info("start to unpack rar, filename:{}", sourceFile.getName());
        long start = System.currentTimeMillis();
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        try (Archive archive = new Archive(new FileInputStream(sourceFile))) {
            FileHeader fileHeader = archive.nextFileHeader();
            while (fileHeader != null) {
                //如果是文件夹
                if (fileHeader.isDirectory()) {
                    fileHeader = archive.nextFileHeader();
                    continue;
                }

                File out = new File(targetPath + fileHeader.getFileNameW());
                if (!out.exists()) {
                    if (!out.getParentFile().exists()) {
                        out.getParentFile().mkdirs();
                    }
                    out.createNewFile();
                }
                try (FileOutputStream os = new FileOutputStream(out)) {
                    archive.extractFile(fileHeader, os);
                } catch (RarException e) {
                    LOGGER.error("unpack rar throw exception, filename:{}, e:{}", sourceFile.getName(), e);
                }
                fileHeader = archive.nextFileHeader();
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("unpack rar throw exception, filename:{}, e:{}", sourceFile.getName(), e);
        } catch (IOException e) {
            LOGGER.error("unpack rar throw exception, filename:{}, e:{}", sourceFile.getName(), e);
        } catch (RarException e) {
            LOGGER.error("unpack rar throw exception, filename:{}, e:{}", sourceFile.getName(), e);
        }

        LOGGER.info("finish unpack rar, filename:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
    }

    /**
     * 解压tar.gz格式的压缩包
     */
    private static void unpackGz() {
    }
}
