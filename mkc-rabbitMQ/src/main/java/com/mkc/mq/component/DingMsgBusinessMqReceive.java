package com.mkc.mq.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.consumer.DingMsgMqAbstract;

@Component
@ConditionalOnProperty(prefix = "spring.ding.business", name = "token")
public class DingMsgBusinessMqReceive extends DingMsgMqAbstract {

	@Value("${spring.ding.business.token}")
	private String token;
	@Value("${spring.ding.business.secret}")
	private String secret;

	@Value("${monitor.notify.subject:}")
	private String subject;
	
	@Override
	public String queueName() {
		return RabbitConfig.QUEUE_DING_MKC_BUSINESS;
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
