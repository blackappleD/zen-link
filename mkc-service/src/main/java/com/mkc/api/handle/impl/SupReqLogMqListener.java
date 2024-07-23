package com.mkc.api.handle.impl;

import com.alibaba.fastjson2.JSON;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.domain.SupReqLog;
import com.mkc.mq.consumer.SupReqLogMqConsumerAbstract;
import com.mkc.service.ISupReqLogService;
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
public class SupReqLogMqListener extends SupReqLogMqConsumerAbstract {

    @Autowired
    private ISupReqLogService supReqService;

    @Override
    public void receive(Message message) throws Exception {
        SupReqLog supLog=null;
        try {
            supLog = JSON.parseObject(message.getBody(), SupReqLog.class);
            log.info("===消费 supReqLog Q 里面消息 orderNo  {} ,supCode {}", supLog.getOrderNo(), supLog.getSupCode());
            supReqService.insertSupReqLog(supLog);
        } catch (Exception e) {
            DdMonitorMsgUtil.sendDdSysErrMsg("add supLog is err {}  {} ", supLog, e);
            throw new RuntimeException(e);
        }
    }
}
