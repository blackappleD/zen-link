package com.mkc.mq.consumer;

import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.receive.RabbitReceiveAbstract;

public abstract class SupReqLogMqConsumerAbstract extends RabbitReceiveAbstract {

	@Override
	public String queueName() {
		return RabbitConfig.QUEUE_SRL;
	}
	
}
