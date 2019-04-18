package com.company.project.util;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * @author Administrator
 * 2017/4/20 0020
 */
public class FileSizeUtil {

    private static final long BYTE = 1;
    private static final long KB = 1024 * BYTE;
    private static final long MB = 1024 * KB;
    private static final long GB = 1024 * MB;
    private static final long TB = 1024 * GB;
    /**
     * 获取文件大小单位为B的double值
     */
    private static final int SIZETYPE_B = 1;
    /**
     * 获取文件大小单位为KB的double值
     */
    private static final int SIZETYPE_KB = 2;
    /**
     * 获取文件大小单位为MB的double值
     */
    public static final int SIZETYPE_MB = 3;
    /**
     * 获取文件大小单位为GB的double值
     */
    private static final int SIZETYPE_GB = 4;
    /**
     * 获取文件大小单位为GB的double值
     */
    private static final int SIZETYPE_TB = 5;

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(@Size(min = 1) String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TLog.e("获取文件大小---获取失败!");
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    @SuppressWarnings("unused")
    public static String getAutoFileOrFilesSize(@Size(min = 1) String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TLog.e("获取文件大小---获取失败!");
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file file
     * @return long size
     * @throws Exception exception
     */
    private static long getFileSize(@NonNull File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        } else {
            TLog.e("获取文件大小---文件不存在!");
            if (file.createNewFile()) {
                TLog.e("创建文件成功");
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f file
     * @return byte - size
     * @throws Exception exception
     */
    private static long getFileSizes(@NonNull File f) throws Exception {
        long size = 0;
        File[] fList = f.listFiles();
        for (File aFList : fList) {
            if (aFList.isDirectory()) {
                size = size + getFileSizes(aFList);
            } else {
                size = size + getFileSize(aFList);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS file long byte size
     * @return 最大单位制文件大小
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < KB) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < MB) {
            fileSizeString = df.format((double) fileS / KB) + "KB";
        } else if (fileS < GB) {
            fileSizeString = df.format((double) fileS / MB) + "MB";
        } else if (fileS < TB) {
            fileSizeString = df.format((double) fileS / GB) + "GB";
        } else {
            fileSizeString = df.format((double) fileS / TB) + "TB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS    file long byte size
     * @param sizeType 指定单位制
     * @return 指定单位制文件大小
     */
    private static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#0.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / KB));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / MB));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / GB));
                break;
            case SIZETYPE_TB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / TB));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}
