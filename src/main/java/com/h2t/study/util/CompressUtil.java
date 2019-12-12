package com.h2t.study.util;

import com.h2t.study.enums.FileTypeEnum;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 *
 * @author hetiantian
 * @version 1.0
 * @Date 2019/12/10 10:09
 */
public class CompressUtil {
    private static final int BUFFER = 1024;
    private final static Logger LOGGER = LoggerFactory.getLogger(CompressUtil.class);

    /**
     * 压缩为zip格式，支持文件、文件夹的压缩
     *
     * @param sourcePath 被压缩文件地址
     * @param targetPath 压缩文件保存地址
     */
    public static void compressToZip(String sourcePath, String targetPath) {
        File sourceFile = FileUtil.validateSourcePath(sourcePath);
        compressToZip(sourceFile, targetPath);
    }

    /**
     * 压缩为zip格式，支持文件、文件夹的压缩
     *
     * @param sourceFile 被压缩文件
     * @param targetPath 压缩文件保存地址
     */
    public static void compressToZip(File sourceFile, String targetPath) {
        FileUtil.validateTargetPath(targetPath);
        //输入文件路径包含文件名
        File targetFile = new File(String.format("%s%s%s.%s", targetPath, File.separator, sourceFile.getName(), FileTypeEnum.ZIP.getTypeName()));

        //1.使用try-with-resource优雅关闭流
        //2.使用CRC32进行文件校验
        LOGGER.info("start to compress file to zip, file name:{}", sourceFile.getName());
        long start = System.currentTimeMillis();
        try (FileOutputStream fileOut = new FileOutputStream(targetFile);
             CheckedOutputStream cos = new CheckedOutputStream(fileOut, new CRC32());
             ZipOutputStream zipOut = new ZipOutputStream(cos)) {
            String baseDir = "";
            compressToZip(sourceFile, zipOut, baseDir);
        } catch (FileNotFoundException e) {
            LOGGER.error("compress file to zip throw exception:{}", e);
        } catch (IOException e) {
            LOGGER.error("compress file to zip throw exception:{}", e);
        }
        LOGGER.info("finish compress file to zip, file name:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
    }

    /**
     * 真正文件/文件夹的压缩部分
     *
     * @param sourceFile 待压缩文件
     * @param zipOut     压缩流
     * @param baseDir
     */
    private static void compressToZip(File sourceFile, ZipOutputStream zipOut, String baseDir) throws IOException {
        //文件夹的压缩
        if (sourceFile.isDirectory()) {
            compressDirectoryToZip(sourceFile, zipOut, baseDir);
        } else {
            //文件的压缩
            compressFileToZip(sourceFile, zipOut, baseDir);
        }
    }

    /**
     * 文件夹的压缩
     *
     * @param sourceFile 待压缩文件
     * @param zipOut     压缩流
     * @param basePath   基本路径
     */
    private static void compressDirectoryToZip(File sourceFile, ZipOutputStream zipOut, String basePath) throws IOException {
        File[] files = sourceFile.listFiles();
        for (File file : files) {
            compressToZip(file, zipOut, basePath + sourceFile.getName() + File.separator);
        }
    }

    /**
     * 文件的压缩
     *
     * @param sourceFile 待压缩文件
     * @param zipOut     压缩流
     * @param basePath   基本路径
     */
    private static void compressFileToZip(File sourceFile, ZipOutputStream zipOut, String basePath) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {
            ZipEntry entry = new ZipEntry(basePath + sourceFile.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }
        }
    }

    /**
     * 压缩为rar格式
     */
    private static void compressRar() {
    }

    /**
     * 压缩为tar.gz格式
     *
     * @param sourcePath
     * @param targetPath
     */
    public static void compressToTarGz(String sourcePath, String targetPath) {
        File sourceFile = new File(sourcePath);
        LOGGER.info("start to compress file to tar.gz, file name:{}", sourceFile.getName());
        long start = System.currentTimeMillis();
        //1.压缩为tar
        String rarSourcePath = compressToTar(sourcePath, targetPath);
        //2.压缩为gz
        compressTarToGz(rarSourcePath, targetPath);
        //3.删除tar
        if (!FileUtil.deleteFile(rarSourcePath)) {
            LOGGER.error("delete rar file field, rar file path:{}", rarSourcePath);
        }

        LOGGER.info("finish compress file to tar.gz, file name:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
    }

    /**
     * 压缩格式为gz
     *
     * @param sourcePath tar文件路径
     * @param targetPath 压缩文件保存地址
     */
    public static void compressTarToGz(String sourcePath, String targetPath) {
        File sourceFile = FileUtil.validateSourcePath(sourcePath);
        compressTarToGz(sourceFile, targetPath);
    }

    /**
     * 压缩格式为gz
     *
     * @param sourceFile tar文件路径
     * @param targetPath 压缩文件保存地址
     */
    public static void compressTarToGz(File sourceFile, String targetPath) {
        //校验解压路径是否存在
        FileUtil.validateTargetPath(targetPath);
        LOGGER.info("start to compress tar file to tar.gz, file name:{}", sourceFile.getName());
        long start = System.currentTimeMillis();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {
            try (GzipCompressorOutputStream gos = new GzipCompressorOutputStream(new BufferedOutputStream(
                    new FileOutputStream(String.format("%s%s%s.%s",
                            targetPath, File.separator, sourceFile.getName(), FileTypeEnum.GZ.getTypeName()))))) {
                byte[] buffer = new byte[BUFFER];
                int read;
                while ((read = bis.read(buffer)) != -1) {
                    gos.write(buffer, 0, read);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("finish compress tar file to tar.gz, file name:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
    }

    /**
     * 压缩为tar格式
     *
     * @param sourcePath 待压缩文件路径
     * @param targetPath 压缩文件保存地址
     * @return tar压缩文件路径
     */
    public static String compressToTar(String sourcePath, String targetPath) {
        File sourceFile = FileUtil.validateSourcePath(sourcePath);
        //校验解压路径是否存在
        FileUtil.validateTargetPath(targetPath);

        File tarFile = new File(targetPath, String.format("%s.%s", sourceFile.getName(), FileTypeEnum.TAR.getTypeName()));
        LOGGER.info("start compress file to tar, file name:{}, cost:{} ms", sourceFile.getName());
        long start = System.currentTimeMillis();
        try (TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(tarFile))) {
            tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);  //解决长路径问题
            String base = sourceFile.getName();
            if (sourceFile.isDirectory()) {
                compressDirectoryToTar(sourceFile, tos, base);
            } else {
                compressFileToTar(tos, sourceFile, base);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("finish compress file to tar, file name:{}, cost:{} ms", sourceFile.getName(), System.currentTimeMillis() - start);
        //返回tar压缩文件路径
        return tarFile.getAbsolutePath();
    }

    /**
     * 文件夹压缩为tar包，本质递归文件压缩处理
     *
     * @param sourceFile
     * @param tos
     * @param basePath   基本路径
     */
    private static void compressDirectoryToTar(File sourceFile, TarArchiveOutputStream tos, String basePath) {
        File[] files = sourceFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                compressDirectoryToTar(file, tos, String.format("%s%s%s", basePath, File.separator, file.getName()));
            } else {
                try {
                    compressFileToTar(tos, file, basePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文件压缩为tar包
     *
     * @param tos
     * @param sourceFile
     * @throws IOException
     */
    private static void compressFileToTar(TarArchiveOutputStream tos, File sourceFile, String basePath) throws IOException {
        TarArchiveEntry tEntry = new TarArchiveEntry(String.format("%s%s%s", basePath, File.separator, sourceFile.getName()));
        tEntry.setSize(sourceFile.length());
        tos.putArchiveEntry(tEntry);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {

            byte[] buffer = new byte[BUFFER];
            int read;
            while ((read = bis.read(buffer)) != -1) {
                tos.write(buffer, 0, read);
            }
        }
        tos.closeArchiveEntry();
    }
}
