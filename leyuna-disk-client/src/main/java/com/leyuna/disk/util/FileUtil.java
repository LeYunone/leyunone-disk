package com.leyuna.disk.util;

import com.leyuna.disk.constant.ServerCode;
import com.leyuna.disk.enums.ErrorEnum;
import com.leyuna.disk.enums.FileEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author pengli
 * @create 2021-12-27 11:08
 * <p>
 * 文件工具类
 */
public class FileUtil {

    /**
     * 从磁盘中搜索文件
     *
     * @param name
     * @param size
     * @return
     */
    public static File searchFile (String name, Long size) {
        //服务器文件夹目录
        File file = new File(ServerCode.FILE_ADDRESS);

        Queue<File> queue = new LinkedList<>();
        queue.add(file);
        while (!queue.isEmpty()) {
            File thisFile = queue.poll();
            //如果这个是文件则比较其名字和大小
            if (thisFile.isFile()) {
                if (thisFile.length() == size && name.equals(thisFile.getName())) {
                    return thisFile;
                }
            } else {
                //如果这个是文件夹则下面的元素都加入到遍历序列中
                File[] files = thisFile.listFiles();
                for (File f : files) {
                    queue.add(f);
                }
            }
        }
        return null;
    }

    /**
     * 拷贝文件
     */
    public static void copyFile (File file, String path) {
        File out = new File(ServerCode.FILE_ADDRESS + path);
        FileOutputStream fos = null;
        FileInputStream fis = null;
        FileChannel fo = null;
        FileChannel fi = null;
        try {
            fos = new FileOutputStream(out);
            fis = new FileInputStream(file);

            fo = fos.getChannel();
            fi = fis.getChannel();
            fi.transferTo(0,fi.size(),fo);
        } catch (Exception e) {
            AssertUtil.isFalse(false, ErrorEnum.FILE_UPLOAD_FILE.getName());
        } finally {
            try {
                if(fos!=null){
                    fos.close();;
                }
                if(fis!=null){
                    fis.close();
                }
                if(fo!=null){
                    fo.close();
                }
                if(fi!=null){
                    fi.close();
                }
            }catch (Exception e){
                AssertUtil.isFalse(false,ErrorEnum.SERVER_ERROR.getName());
            }
        }
    }

    public static File getFile(String name,String path,String type){

        File file=new File(ServerCode.FILE_ADDRESS+path+"/"+type+"/"+name);
        AssertUtil.isFalse(ObjectUtils.isEmpty(file),ErrorEnum.SELECT_NOT_FOUND.getName());

        return file;
    }
}
