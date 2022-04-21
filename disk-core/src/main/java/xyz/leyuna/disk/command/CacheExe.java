package xyz.leyuna.disk.command;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author pengli
 * @create 2021-09-02 15:24
 *
 * 清除缓存指令
 */
@Component
public class CacheExe {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //存放需要消除的缓存key
    private  CopyOnWriteArrayList<String> copyOnWriteArrayList=new CopyOnWriteArrayList();

    /**
     * 清空当前所有缓存
     */
    public  void clearAllCache(){
        Set<String> allKeys = getAllKeys();
        if (ObjectUtil.isNotEmpty(allKeys)) {
            stringRedisTemplate.delete(allKeys);
        }
    }

    /**
     *  获得当前所有缓存key
     * @return
     */
    public  Set<String> getAllKeys(){
        Set<String> keys = stringRedisTemplate.keys("*");
        return keys;
    }

    /**
     * 根据key判断是否有缓存
     * @param key
     * @return
     */
    public boolean hasCacheByKey(String key){
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     * @param sec
     */
    public void setCacheKey(String key,String value,long sec){
        stringRedisTemplate.opsForValue().set(key,value,sec*1, TimeUnit.SECONDS);
    }

    /**
     * 根据key获取缓存值
     * @param key
     * @return
     */
    public String getCacheByKey(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 情况key对应缓存
     * @param key
     */
    public void clearCacheKey(String key){
        stringRedisTemplate.delete(key);
    }
}
