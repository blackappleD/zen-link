package com.mkc.mq.receive;

import com.mkc.mq.producer.RabbitProducer;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public abstract class RabbitReceiveAbstract implements InitializingBean {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RabbitProducer rabbitProducer;
	
	public abstract String queueName();
	public abstract void receive(Message message) throws Exception;
	
	@RabbitListener(queues = "@queueName")
	public void handle(Message message, Channel channel) throws Exception {
		try {
			this.receive(message);
		} catch (Exception e) {
			log.error(new String(message.getBody(), "utf-8"));
			log.error("消费队列(" + message.getMessageProperties().getConsumerQueue() + ")时出错，队列数据如上，错误信息如下: ", e);
			try {
				// 加入到队列自己的错误队列中
				rabbitProducer.producerError(message);
			} catch (Exception e1) {
				log.error("加入到错误队列(" + message.getMessageProperties().getConsumerQueue() + "_ERROR)依然出错: ", e1);
				// 加入出错时不消费队列，会重回队列，错误一直不解决会出现死循环。
				// 暂时不抛异常日志有记录数据，队列直接丢掉。
				// 前提是加入到错误队列还失败的情况下。基本不会到这。
				// throw new Exception(e1);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final void afterPropertiesSet() {
		try {
			Method method = this.getClass().getMethod("handle", Message.class, Channel.class);
			RabbitListener rblis = method.getAnnotation(RabbitListener.class);
			String[] queues = rblis.queues();
			if(rblis == null || queues == null || queues.length == 0 || !queues[0].startsWith("@")) {
				return;
			}
			
			method = this.getClass().getMethod(queues[0].substring(1));
			if(method == null || !method.getReturnType().equals(String.class)) {
				return;
			}
			String queueName = (String) method.invoke(this);
			
			method = RabbitReceiveAbstract.class.getDeclaredMethod("handle", Message.class, Channel.class);
			rblis = method.getAnnotation(RabbitListener.class);
			
			InvocationHandler ivth = Proxy.getInvocationHandler(rblis);
			Field annoField = ivth.getClass().getDeclaredField("memberValues");
			annoField.setAccessible(true);
 
			Map<String, Object> memberValues = (Map<String, Object>) annoField.get(ivth);
			memberValues.put("queues", new String[] { queueName });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
