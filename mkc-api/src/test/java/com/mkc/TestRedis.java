package com.mkc;

import com.mkc.common.core.redis.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * @author tqlei
 * @date 2023/5/26 17:12
 */
@SpringBootTest
public class TestRedis {


    @Autowired
    private RedisCache redisCache;

    @Test
    public void increment(){
        BigDecimal a=new BigDecimal("0.1001");
        Double test = redisCache.increment("test", a.negate().doubleValue());
        System.out.println(test);

    }


}
