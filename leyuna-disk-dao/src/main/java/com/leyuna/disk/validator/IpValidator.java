package com.leyuna.disk.validator;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.leyuna.disk.co.FileUpLogCO;
import com.leyuna.disk.domain.FileUpLogE;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author pengli
 * @create 2021-12-24 17:07
 * ip 校验
 */
@Component
public class IpValidator {

    /**
     *  校验主体
     * @param ip
     */
    public void validator(String ip){

        //TODO ip是否在白名单中

        //根据ip查询上传记录
        List<FileUpLogCO> fileUpLogCOS = FileUpLogE.queryInstance().setIp(ip).selectByCon();
        if(CollectionUtils.isNotEmpty(fileUpLogCOS)){
            //如果文件不为空，则
        }
        //最后一条记录时间和当前时间对比

    }
}
