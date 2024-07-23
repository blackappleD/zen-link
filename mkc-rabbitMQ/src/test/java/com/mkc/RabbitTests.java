package com.mkc;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.mkc.mq.component.DingMsgSysMqReceive;
import com.mkc.mq.component.EmailMqReceive;
import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.domain.Email;
import com.mkc.mq.producer.RabbitProducer;

@SpringBootTest
@SpringBootApplication
public class RabbitTests {
	
	@Autowired
	private RabbitProducer rabbitProducer;
	@Autowired
	private EmailMqReceive emailMqReceive;
	@Autowired
	private DingMsgSysMqReceive dingMkcSysMqReceive;
	
	/**
	 * 直接发送邮件
	 * @throws Exception
	 */
	@Test
	public void testSendEmail() throws Exception {
		Email email = new Email();
		email.setSubject("东方明珠");
		email.setText("<b style=\"color:blue;\">这里真的好漂亮啊！</b>");
		email.setTo("zhengyw@qt300061.com");
		
		emailMqReceive.sendEmail(email);
		
		System.out.println("Email 发送完成！！！");
	}
	/**
	 * 推送邮箱消息到队列
	 * @throws InterruptedException
	 */
	@Test
	public void testProducerEmail() throws InterruptedException {
		Email email = new Email();
		email.setSubject("东方明珠");
		email.setText("<b style=\"color:blue;\">这里真的好漂亮啊!!! 用mkc接收 </b>");
		email.setTo("zhengyw@qt300061.com");
		
		rabbitProducer.producerEmail(email);
		
		System.out.println("producerEmail 推送完成！！！");
		
		Thread.sleep(5000);
	}
	
	
	/**
	 * 直接发送钉钉消息到群里
	 * @throws Exception
	 */
	@Test
	public void testSendDingMsg() throws Exception {
		String msg = dingMkcSysMqReceive.sendDingMsg("测试消息4！！！");
		System.out.println(msg);
		System.out.println("ding 发送完成");
	}
	
	/**
	 * 推送消息到队列
	 * @throws InterruptedException
	 */
	@Test
	public void testProducerDingMkcSys() throws InterruptedException {
		String msg = "【敬众供应商】个人云逸分查询 接口 发生异常@#消息详细内容 ";
		
		for(int i = 0;i < 100;i++) {
			rabbitProducer.producer(msg + i, RabbitConfig.QUEUE_DING_MKC_SYS);
		}
		
		System.out.println("producerDingMkcSys 推送完成！！！");
		Thread.sleep(120 * 1000);
	}
	
	/**
	 * 模拟接收队列把消息发送到钉钉
	 * @throws InterruptedException
	 */
	@Test
	public void testReceive() throws InterruptedException {
		for(int i = 0;i < 105;i++) {
			final String name = "线程" + i;
			new Thread(() -> {
				try {
					String msg = name + "发出的第六批测试消息，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，各种错误信息好多好多，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点，还是不够得再加一点。";
					dingMkcSysMqReceive.receive(new Message(("\"" + msg + "\"").getBytes()));
					System.out.println(name + " 发送成功");
				} catch (Exception e) {
					System.out.println(name + " 出错了！！！");
					e.printStackTrace();
				}
			}).start();
		}
		System.out.println("结束，完成");
		TimeUnit.MINUTES.sleep(10);
	}
	
	
	
	/**
	 * 单独等待队列消息接收，也可以在发送的时候自己等待
	 * @throws InterruptedException
	 */
	@Test
	public void receive() throws InterruptedException {
		System.out.println("************ 监听中 ************");
		Thread.sleep(10 * 60 * 1000);
	}
	
}
