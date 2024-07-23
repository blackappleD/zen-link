package com.mkc.mq.producer;

import com.alibaba.fastjson2.JSON;
import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.domain.Email;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public <T> void producer(T t, String queueName) {
		rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS, queueName, t);
	}
	
	public void producerError(Message message) {
		MessageProperties properties = message.getMessageProperties();
		String consumerQueue = properties.getConsumerQueue();
		rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS, consumerQueue + "_error", JSON.parse(message.getBody()));
	}
	
	public void producerEmail(Email email) {
		rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS, RabbitConfig.QUEUE_EMAIL, email);
	}
	
	public <T> void producerMerReqLog(T t) {
		rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS, RabbitConfig.QUEUE_MRL, t);
	}
	
	public <T> void producerSupReqLog(T t) {
		rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS, RabbitConfig.QUEUE_SRL, t);
	}


}
