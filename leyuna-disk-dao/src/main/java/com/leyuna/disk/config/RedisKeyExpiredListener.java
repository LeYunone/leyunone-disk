package com.leyuna.disk.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.co.FileUpLogCO;
import com.leyuna.disk.constant.ServerCode;
import com.leyuna.disk.domain.FileInfoE;
import com.leyuna.disk.domain.FileUpLogE;
import com.leyuna.disk.enums.FileEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.File;
import java.util.List;

/**
 * @author pengli
 * @create 2022-01-17 16:22
 *
 *  redis key过期监听事件
 */
@Configuration
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpiredListener (RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String redisKey = message.toString();
        //走云盘 暂时保存业务逻辑
        if(redisKey.contains("disk")){
            fileExpiration(message.toString());
        }
    }

    /**
     * 云盘内处理过期事件 ： 处理掉暂时保存文件
     * @param message
     */
    private void fileExpiration(String message){
        // file: id
        String fileId = message.substring(message.indexOf(":")+1);
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        List<FileUpLogCO> fileUpLogCOS = FileUpLogE.queryInstance().setUserId(fileInfoCO.getUserId()).selectByCon();
        if(CollectionUtils.isEmpty(fileUpLogCOS)){
            //理论上不可能出现的情况，如果出现，只跳过这次文件的操作行为
            return;
        }
        //
        FileUpLogCO fileUpLogCO = fileUpLogCOS.get(0);
        //如果在缓存里 只需要计算数据库

        //半永久则需要进行硬盘中删除文件
        if(message.contains("deleted")){
            //从硬盘上删除该文件
            deleteFile(fileId);
        }

        //删除数据库信息
        FileInfoE.queryInstance().setId(fileInfoCO.getId()).setDeleted(1).update();
        //重新计算用户的内存大小
        Double lastFileSize = fileUpLogCO.getUpFileTotalSize() - fileInfoCO.getFileSize();
        FileUpLogE.queryInstance().setId(fileUpLogCO.getId()).setUpFileTotalSize(lastFileSize).update();
    }

    /**
     * 物理删除文件
     * @param fileId
     */
    private void deleteFile(String fileId){
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        if(null!=fileInfoCO){
            FileEnum fileEnum = FileEnum.loadName(fileInfoCO.getFileType());
            //物理删除文件
            File file = new File(ServerCode.FILE_ADDRESS + fileInfoCO.getUserId() + "/"+fileEnum.getName()+"/"+ fileInfoCO.getName());
            if(file.exists()){
                file.delete();
            }
        }
    }
}
