package com.mkc.api.handle.impl;

import com.alibaba.fastjson2.JSON;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.domain.MerReqLog;
import com.mkc.mq.consumer.MerReqLogMqConsumerAbstract;
import com.mkc.service.IMerReqLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tqlei
 * @date 2023/6/17 13:18
 */

@Slf4j
@Component
public class MerReqLogMqListener extends MerReqLogMqConsumerAbstract {

    @Autowired
    private IMerReqLogService merReqLogService;

    @Override
    public void receive(Message message) throws Exception {
        MerReqLog merReqLog=null;
        try {
            merReqLog = JSON.parseObject(message.getBody(), MerReqLog.class);
            log.info("===消费 merReqLog Q 里面消息 orderNo  {}, merCode {}", merReqLog.getOrderNo(), merReqLog.getMerCode());
            merReqLogService.saveMerLog(merReqLog);
        } catch (Exception e) {
            DdMonitorMsgUtil.sendDdSysErrMsg("add merReqLog is err {}  {} ",merReqLog,e);
            throw new RuntimeException(e);
        }


    }
}
