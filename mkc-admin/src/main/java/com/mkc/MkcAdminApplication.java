package com.mkc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author  atd
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MkcAdminApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(MkcAdminApplication.class, args);
        System.out.println("SpringApplication.run over");
        System.out.println("  (♥◠‿◠)ﾉﾞ  数据聚合营销云后台管理系统启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}