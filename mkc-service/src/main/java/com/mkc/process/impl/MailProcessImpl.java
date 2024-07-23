package com.mkc.process.impl;

import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.domain.MerInfo;
import com.mkc.mq.domain.Email;
import com.mkc.mq.producer.RabbitProducer;
import com.mkc.process.IMailProcess;
import com.mkc.process.template.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author tqlei
 * @date 2023/7/17 15:08
 */

@Component
@Slf4j
public class MailProcessImpl implements IMailProcess {

    @Autowired
    private RabbitProducer rabbitProducer;

    //通知邮箱
    @Value("${monitor.notify.email}")
    private String notifyEmail;

    @Value("${monitor.notify.subject}")
    private String notifySubject;

    @Value("${monitor.mer.subject}")
    private String merSubject;

    @Override
    public void sendMerKey(MerInfo info){

        String subject="账户信息 "+(merSubject==null?"":merSubject);
        String text=String.format(MailTemplate.E_MER_KEY,  info.getMerName(),info.getMerCode(), info.getSignKey(), info.getSignPwd());;

        Email email=new Email();
        email.setSubject(subject);
        email.setText(text);
        email.setTo(info.getEmail());

        rabbitProducer.producerEmail(email);


    }

    @Override
    public void sendProductNotSup(String merName, String productName) {

        try {
            String subject=notifySubject+"-商户产品未配置供应商";
            String text="该商户：%s 、产品：%s  未配置可查询的供应商,请尽快配置!";
            String content = String.format(text,merName,productName);
            Email email=new Email();
            email.setSubject(subject);
            email.setText(content);
            email.setTo(notifyEmail);

            rabbitProducer.producerEmail(email);

            //发送钉钉消息 通知
            DdMonitorMsgUtil.sendDdBusinessMsg(content);

        }catch (Exception e){
            log.error("sendProductNotSup is err merName {} , productName {} ",merName,productName,e);
        }


    }



    public static void main(String[] args) {

        String format = String.format(MailTemplate.E_MER_KEY, "ces", "key", "pwd");
        System.out.println(format);

    }

}
