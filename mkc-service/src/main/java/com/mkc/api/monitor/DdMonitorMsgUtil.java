package com.mkc.api.monitor;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.spring.SpringUtils;
import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.producer.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 钉钉消息发送工具类
 * @author tqlei
 * @date 2023/7/31 15:16
 */

@Slf4j
public class DdMonitorMsgUtil {


    private static ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolTaskExecutor");

    private static RabbitProducer rabbitProducer=SpringUtils.getBean("rabbitProducer");;

    /**
     *  通过钉钉 将系统错误日志 发送
     * @param messagePattern  用 {} 做占位符
     */
    public static void sendDdSysErrMsg(String messagePattern, Object... arguments){

        try {
            executor.execute(() ->{
                FormattingTuple formattingTuple = MessageFormatter.arrayFormat(messagePattern, arguments);
                String msg= formattingTuple.getMessage();

                Throwable throwable = formattingTuple.getThrowable();
                String errorMsg ="";
                if (throwable != null) {
                    errorMsg =  ExceptionUtil.stacktraceToString(throwable,-1);
                }

                log.error("==api msg:  {} ,  errorMsg: {} ",msg,errorMsg);
                String curTime = DateUtils.getTime();
                rabbitProducer.producer(curTime+" , "+msg+"\n"+errorMsg, RabbitConfig.QUEUE_DING_MKC_SYS);
            });
        }catch (Throwable e){
            log.error(" send DdSys ErrMsg  is err "+messagePattern, arguments,e);
        }

    }


    /**
     *  通过钉钉 将系统错误日志 发送
     */
    public static void sendDdSysErrMsg(String msg){

        try {
            String curTime = DateUtils.getTime();
            executor.execute(() ->{
                rabbitProducer.producer(curTime+" , "+msg, RabbitConfig.QUEUE_DING_MKC_SYS);

            });
        }catch (Throwable e){
            log.error(" send DdSys ErrMsg  is err "+msg,e);
        }

    }

    /**
     * 通过钉钉 将业务预警日志 发送
     * @param messagePattern 用 {} 做占位符
     */
    public static void sendDdBusinessMsg(String messagePattern, Object... arguments){

        try {
            executor.execute(() ->{
                //格式化日志格式
                String msg=MessageFormatter.arrayFormat(messagePattern, arguments).getMessage();
                log.warn("DDMSG sendDdBusinessMsg {} ",msg);
                String curTime = DateUtils.getTime();
                rabbitProducer.producer(curTime+" , "+msg, RabbitConfig.QUEUE_DING_MKC_BUSINESS);
            });
        }catch (Throwable e){
            log.error(" send DdBusiness Msg  is err  "+messagePattern, arguments,e);
        }

    }






}
