package xyz.leyuna.disk.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.co.FileUserCO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.enums.FileEnum;

import java.io.File;
import java.util.List;

/**
 * @author LeYuna
 * @email 365627310@qq.com
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
        if(redisKey.contains("disk_")){
            fileExpiration(message.toString());
        }
    }

    /**
     * 云盘内处理过期事件 ： 处理掉暂时保存文件
     * @param message
     */
    private void fileExpiration(String message){
        // file: id
        String[] split = message.substring(message.indexOf(":") + 1).split(",");
        String fileId = split[0];
        String userId = split[1];
        FileUserCO fileUserCO = FileUserE.queryInstance().setFileId(fileId).setUserId(userId).selectOne();
        if(ObjectUtil.isEmpty(fileUserCO)){
            //如果找不到值，则说明该文件已经被前置删除 不需要处理
            return;
        }

        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        //删除该文件
        deleteFile(fileInfoCO);

        FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(userId).selectOne();
        //重新计算用户的内存大小
        Long lastFileSize = fileUpLogCO.getUpFileTotalSize() - fileInfoCO.getFileSize();
        FileUpLogE.queryInstance().setId(fileUpLogCO.getId()).setUpFileTotalSize(lastFileSize).update();
    }

    /**
     * 删除文件
     * @param
     */
    private void deleteFile(FileInfoCO fileInfoCO){
        if(null!=fileInfoCO){
            //物理删除文件
            File file = new File(fileInfoCO.getFilePath());
            if(file.exists()){
                file.delete();
            }
        }
        //删除数据库信息
        FileInfoE.queryInstance().setId(fileInfoCO.getId()).setDeleted(1).update();
    }
}
