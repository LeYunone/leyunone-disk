package xyz.leyuna.disk.validator;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.constant.AdminCode;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.model.enums.SortEnum;
import xyz.leyuna.disk.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-24 17:07
 * ip 校验
 */
@Component
public class UserValidator {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     *  校验主体
     * @param userId
     */
    public void validator(String userId){

        //跳过站长用户
        if(AdminCode.ADMIN_USER_ID.equals(userId)){
            return;
        }
        //用户是否在黑名单中
        AssertUtil.isFalse(stringRedisTemplate.hasKey(userId), ErrorEnum.USER_BLACK.getName());

        //根据用户id查询最后一次上传记录
        List<FileUpLogCO> fileUpLogCOS = FileUpLogE.queryInstance().
                selectByConOrder(SortEnum.CREATE_DESC);
        if(CollectionUtils.isNotEmpty(fileUpLogCOS)){
            FileUpLogCO fileUpLogCO = fileUpLogCOS.get(0);
            //如果最后一次上传记录，被记录不合法，则说明这个用户的云盘有一些问题[内存满了，账号异常等等]
            AssertUtil.isFalse(fileUpLogCO.getUpSign()==1,ErrorEnum.USER_UPLOAD_FILE.getName());
        }
    }
}
