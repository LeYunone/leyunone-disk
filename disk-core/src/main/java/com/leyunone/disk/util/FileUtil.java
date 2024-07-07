package com.leyunone.disk.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import org.springframework.web.multipart.MultipartFile;

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

    public static String getFileType(MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public static String getTxtFile(String filePath) {
//        return HttpUtil.createRequest(Method.GET,filePath).execute().body();
        File file = new File(filePath);
        if (file.exists()) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath), 8192)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    content.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }
        return null;
    }
}
