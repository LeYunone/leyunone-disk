package xyz.leyuna.disk.model.constant;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-09-16 17:24
 *
 * 服务器的一些编码
 */
public class ServerCode {

//    public static String FILE_ADDRESS = "https://www.leyuna.xyz/file/";
    public static String FILE_ADDRESS = "c:/file/";

    //临时目录
    public static String TEMP_PATH = "C:/diskTemp/";

    public static ConcurrentHashMap<String,Thread> threadUpload = new ConcurrentHashMap();

}
