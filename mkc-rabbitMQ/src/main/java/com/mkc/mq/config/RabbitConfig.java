package com.mkc.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	// 业务topic队列
	public static final String EXCHANGE_TOPICS = "amq.topic";
	
	// 死信队列，未实现，绑定时报错，以后再优化
	public static final String EXCHANGE_FANOUT = "amq.fanout";
	public static final String QUEUE_ALL_ERROR = "queue_all_error";
	
	// 邮件队列
	public static final String QUEUE_EMAIL = "queue_email";
	public static final String QUEUE_EMAIL_ERROR = "queue_email_error";
	
	// 钉钉队列
	public static final String QUEUE_DING_MKC_SYS = "queue_ding_mkc_sys";
	public static final String QUEUE_DING_MKC_SYS_ERROR = QUEUE_DING_MKC_SYS+"_error";
	public static final String QUEUE_DING_MKC_BUSINESS = "queue_ding_mkc_business";
	public static final String QUEUE_DING_MKC_BUSINESS_ERROR = QUEUE_DING_MKC_BUSINESS+"_error";
	
	// 业务队列
	public static final String QUEUE_MRL = "queue_mrl";
	public static final String QUEUE_MRL_ERROR = "queue_mrl_error";
	public static final String QUEUE_SRL = "queue_srl";
	public static final String QUEUE_SRL_ERROR = "queue_srl_error";




	@Bean(EXCHANGE_TOPICS)
	public Exchange exchangeTopics() {
		// durable(true)默认持久化，mq重启之后交换机还在
		return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS).durable(true).build();
	}
	
	// 用作死信队列
	@Bean(EXCHANGE_FANOUT)
	public Exchange exchangeFanout() {
		// durable(true)默认持久化，mq重启之后交换机还在
		return ExchangeBuilder.fanoutExchange(EXCHANGE_FANOUT).durable(true).build();
	}
	@Bean(QUEUE_ALL_ERROR)
	public Queue queueError() {
		return new Queue(QUEUE_ALL_ERROR);
	}
	@Bean
	public Binding bindingRoutingkeyError(@Qualifier(QUEUE_ALL_ERROR) Queue queue,
			@Qualifier(EXCHANGE_FANOUT) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_ALL_ERROR).noargs();
	}
	

	// 邮件队列
	@Bean(QUEUE_EMAIL)
	public Queue queueEmail() {
		return new Queue(QUEUE_EMAIL);
	}
	@Bean
	public Binding bindingQueueEmail(@Qualifier(QUEUE_EMAIL) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_EMAIL).noargs();
	}
	@Bean(QUEUE_EMAIL_ERROR)
	public Queue queueEmailError() {
		return new Queue(QUEUE_EMAIL_ERROR);
	}
	@Bean
	public Binding bindingQueueEmailError(@Qualifier(QUEUE_EMAIL_ERROR) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_EMAIL_ERROR).noargs();
	}
	
	// 钉钉队列
	@Bean(QUEUE_DING_MKC_SYS)
	public Queue queueDingMkcdev() {
		return new Queue(QUEUE_DING_MKC_SYS);
	}
	@Bean
	public Binding bindingQueueDingMkcdev(@Qualifier(QUEUE_DING_MKC_SYS) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_DING_MKC_SYS).noargs();
	}
	@Bean(QUEUE_DING_MKC_SYS_ERROR)
	public Queue queueDingMkcdevError() {
		return new Queue(QUEUE_DING_MKC_SYS_ERROR);
	}
	@Bean
	public Binding bindingQueueDingMkcdevError(@Qualifier(QUEUE_DING_MKC_SYS_ERROR) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_DING_MKC_SYS_ERROR).noargs();
	}
	
	@Bean(QUEUE_DING_MKC_BUSINESS)
	public Queue queueDingMkcpro() {
		return new Queue(QUEUE_DING_MKC_BUSINESS);
	}
	@Bean
	public Binding bindingQueueDingMkcpro(@Qualifier(QUEUE_DING_MKC_BUSINESS) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_DING_MKC_BUSINESS).noargs();
	}
	@Bean(QUEUE_DING_MKC_BUSINESS_ERROR)
	public Queue queueDingMkcproError() {
		return new Queue(QUEUE_DING_MKC_BUSINESS_ERROR);
	}
	@Bean
	public Binding bindingQueueDingMkcproError(@Qualifier(QUEUE_DING_MKC_BUSINESS_ERROR) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_DING_MKC_BUSINESS_ERROR).noargs();
	}
	
	
	
	
	// 业务队列
	@Bean(QUEUE_MRL)
	public Queue queueMrl() {
		return new Queue(QUEUE_MRL);
	}
	@Bean
	public Binding bindingRoutingkeyMrl(@Qualifier(QUEUE_MRL) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_MRL).noargs();
	}
	
	@Bean(QUEUE_MRL_ERROR)
	public Queue queueMrlError() {
		return new Queue(QUEUE_MRL_ERROR);
	}
	@Bean
	public Binding bindingRoutingkeyMrlError(@Qualifier(QUEUE_MRL_ERROR) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_MRL_ERROR).noargs();
	}
	
	@Bean(QUEUE_SRL)
	public Queue queueSrl() {
		return new Queue(QUEUE_SRL);
	}
	@Bean
	public Binding bindingRoutingkeySrl(@Qualifier(QUEUE_SRL) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_SRL).noargs();
	}
	
	@Bean(QUEUE_SRL_ERROR)
	public Queue queueSrlError() {
		return new Queue(QUEUE_SRL_ERROR);
	}
	@Bean
	public Binding bindingRoutingkeySrlError(@Qualifier(QUEUE_SRL_ERROR) Queue queue,
			@Qualifier(EXCHANGE_TOPICS) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_SRL_ERROR).noargs();
	}



	
}