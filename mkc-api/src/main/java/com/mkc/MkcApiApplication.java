package com.mkc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 * 
 * @author  atd
 */
@EnableScheduling
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@MapperScan("com.mkc.**.mapper")
public class MkcApiApplication
{


    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(MkcApiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  数据聚合营销云 API启动成功   ლ(´ڡ`ლ)ﾞ  \n");


    }
}