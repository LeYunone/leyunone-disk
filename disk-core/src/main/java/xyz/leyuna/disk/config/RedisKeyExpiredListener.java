package xyz.leyuna.disk.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.enums.FileEnum;

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
            //如果找不到值，则说明该文件已经被前置删除 不需要处理
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