package com.h2t.study.util;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
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
public class UnpackUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(UnpackUtil.class);
    private static final int BUFFER_SIZE = 1024;

    /**
     * 解压zip格式的压缩包
     *
     * @param sourcePath 待解压文件路径
     * @param targetPath 解压路径
     */
    public static void unpackZip(String sourcePath, String targetPath) {
        File sourceFile = FileUtil.validateSourcePath(sourcePath);
        unpackZip(sourceFile, targetPath);
    }

    public static void unpackZip(File sourceFile, String targetPath) {
        //校验解压地址是否存在
        FileUtil.validateTargetPath(targetPath);

        LOGGER.info("start to unpack zip file, file name:{}", sourceFile.getName());
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
                    File tempFile = new File(targetPath + File.separator + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!tempFile.getParentFile().exists()) {
                        tempFile.getParentFile().mkdirs();
                    }
                    tempFile.createNewFile();

                    // 将压缩文件内容写入到这个文件中
                    try (InputStream is = zipFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(tempFile)) {
                        int len;
                        byte[] buf = new byte[BUFFER_SIZE];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    }
                }

            }
            LOGGER.info("finish unpack zip file, file name:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            LOGGER.error("unpack zip throw exception:{}", e);
        }
    }

    /**
     * 解压rar格式的压缩包
     *
     * @param sourcePath 待解压文件
     * @param targetPath 解压路径
     */
    public static void unpackRar(String sourcePath, String targetPath) {
        File sourceFile = FileUtil.validateSourcePath(sourcePath);
        unpackRar(sourceFile, targetPath);
    }

    public static void unpackRar(File sourceFile, String targetPath) {
        //校验解压地址是否存在
        FileUtil.validateTargetPath(targetPath);

        LOGGER.info("start to unpack rar file, file name:{}", sourceFile.getName());
        long start = System.currentTimeMillis();
        System.out.println("absolute path is ============= " + sourceFile.getAbsolutePath());
        try (Archive archive = new Archive(new FileInputStream(sourceFile))) {
            FileHeader fileHeader = archive.nextFileHeader();
            while (fileHeader != null) {
                //如果是文件夹
                if (fileHeader.isDirectory()) {
                    fileHeader = archive.nextFileHeader();
                    continue;
                }

                //防止文件名中文乱码问题的处理
                File out = new File(String.format("%s%s%s", targetPath, File.separator, fileHeader.getFileNameW().isEmpty() ? fileHeader
                        .getFileNameString() : fileHeader.getFileNameW()));

                if (!out.exists()) {
                    if (!out.getParentFile().exists()) {
                        out.getParentFile().mkdirs(); //相对路径可能多级，可能需要创建父目录.
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
        } catch (IOException | RarException e) {
            LOGGER.error("unpack rar throw exception, file name:{}, e:{}", sourceFile.getName(), e);
        }

        LOGGER.info("finish unpack rar file, file name:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
    }


    /**
     * 解压tar.gz格式的压缩包为tar压缩包
     *
     * @param sourcePath 待解压文件路径
     * @param targetPath 解压路径
     */
    public static void unpackTarGz(String sourcePath, String targetPath) throws IOException {
        long start = System.currentTimeMillis();
        FileUtil.validateSourcePath(sourcePath);
        File sourceFile = new File(sourcePath);
        LOGGER.info("start to unpack tar.gz file, file name:{}", sourceFile.getName());

        try (FileInputStream fileInputStream = new FileInputStream(sourceFile);
             GzipCompressorInputStream gzipCompressorInputStream = new GzipCompressorInputStream(fileInputStream);
             TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gzipCompressorInputStream)) {
            File targetFile = new File(targetPath);
            TarArchiveEntry entry;
            while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                File curFile = new File(targetFile, entry.getName());
                File parent = curFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }

                try (FileOutputStream outputStream = new FileOutputStream(curFile)) {
                    IOUtils.copy(tarArchiveInputStream, outputStream);
                }
            }

        }
        LOGGER.info("finish unpack tar.gz file, file name:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
    }
}
