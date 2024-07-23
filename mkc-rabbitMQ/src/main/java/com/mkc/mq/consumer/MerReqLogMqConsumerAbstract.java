package com.mkc.mq.consumer;

import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.receive.RabbitReceiveAbstract;

public abstract class MerReqLogMqConsumerAbstract extends RabbitReceiveAbstract {
	
	@Override
	public String queueName() {
		return RabbitConfig.QUEUE_MRL;
	}
	
}
