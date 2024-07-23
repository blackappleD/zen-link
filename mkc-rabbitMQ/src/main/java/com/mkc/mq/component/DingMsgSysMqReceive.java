package com.mkc.mq.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.consumer.DingMsgMqAbstract;

@Component
@ConditionalOnProperty(prefix = "spring.ding.syserr", name = "token")
public class DingMsgSysMqReceive extends DingMsgMqAbstract {

	@Value("${spring.ding.syserr.token}")
	private String token;
	@Value("${spring.ding.syserr.secret}")
	private String secret;

	@Value("${monitor.notify.subject:}")
	public  String subject;

	@Override
	public String queueName() {
		return RabbitConfig.QUEUE_DING_MKC_SYS;
	}

	@Override
	public String token() {
		return token;
	}

	@Override
	public String secret() {
		return secret;
	}

	@Override
	public String subject() {
		return subject;
	}

}
