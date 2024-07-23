package com.mkc.tool;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author tqlei
 * @date 2023/6/25 17:03
 */

@Slf4j
@Component
public class IdUtils {

   // private static StringRedisTemplate stringRedisTemplate = ApplicationContextHolder.getBean(StringRedisTemplate.class);
   // private static StringRedisTemplate stringRedisTemplate = SpringUtils.getBean(StringRedisTemplate.class);
    private static String SNOWFLAKE_WORKID = "snowflake:workid";
    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(getWorkerId(SNOWFLAKE_WORKID), getDatacenterId());


    public static String getNextId() {
        return SNOWFLAKE.nextIdStr();
    }


    /**
     * 容器环境生成workid 并redis缓存
     * @param key
     * @return
     */
    public static Long getWorkerId(String key) {
        Long redisId = SpringUtils.getBean(RedisCache.class).increment(key);
        long id=redisId % 31;
        log.info("init=====getWorkerId====redisId {}, id {}",redisId,id);
        return id;
    }

    /**
     * 容器环境生成datacenterId 并redis缓存
     * @return
     */
    public static Long getDatacenterId() {
        Random random = new Random();
        int id=random.nextInt(31);
        log.info("init=====datacenterId==== id {}",id);
        return Long.valueOf(id);
    }



}
