package com.mkc.quartz.task;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mkc.common.utils.StringUtils;

/**
 * 定时任务调度测试
 * 
 * @author  atd
 */
@Component("ryTask")
public class RyTask
{
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(LocalDateTime.now() + StringUtils.format(" 执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println(LocalDateTime.now() + " 执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println(LocalDateTime.now() + " 执行无参方法");
    }
}
