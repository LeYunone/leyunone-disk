package xyz.leyuna.disk.util;

import org.springframework.util.ObjectUtils;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.enums.ErrorEnum;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-27 11:08
 * <p>
 * 文件工具类
 */
public class FileUtil {

    public static void deleteFile(String path) {
        //TODO 删除文件 4-21暂时用File直接删除
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static File getFile(String path) {

        //TODO 获取文件 4-21暂时用File直接返回 没考虑FTP
        File file = new File(path);
        AssertUtil.isFalse(ObjectUtils.isEmpty(file), ErrorEnum.SELECT_NOT_FOUND.getName());

        return file;
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
