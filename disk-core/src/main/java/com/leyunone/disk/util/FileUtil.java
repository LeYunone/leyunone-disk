package com.leyunone.disk.util;

import java.io.*;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-27 11:08
 * <p>
 * 文件工具类
 */
public class FileUtil {

    /**
     * 内存文本化
     *
     * @return
     */
    public static String sizeText(long fileSize) {
        String sizeTest = null;
        if (fileSize < 1024) {
            sizeTest = fileSize + "B";
        } else if (fileSize < 1048576) {
            sizeTest = String.format("%.2f", fileSize / 1024.0) + "KB";
        } else if (fileSize < 1073741824) {
            sizeTest = String.format("%.2f", fileSize / (1024 * 1024.0)) + "MB";
        } else {
            sizeTest = String.format("%.2f", fileSize / (1024 * 1024 * 1024.0)) + "G";
        }
        return sizeTest;
    }

    public static byte[] FiletoByte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
