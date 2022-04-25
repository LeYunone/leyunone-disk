package xyz.leyuna.disk.validator;

import cn.hutool.core.util.ObjectUtil;
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

        //查询用户列表
        FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(userId).selectOne();
        if(ObjectUtil.isEmpty(fileUpLogCO)){
            FileUpLogE.queryInstance().setUserId(userId).setUpSign(1).setUpFileTotalSize(0L).save();
        }else{
            AssertUtil.isFalse(fileUpLogCO.getUpSign()==2,ErrorEnum.USER_BLACK.getName());
        }
    }
}
