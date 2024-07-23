package com.mkc.mq.component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.mkc.mq.config.RabbitConfig;
import com.mkc.mq.domain.Email;
import com.mkc.mq.receive.RabbitReceiveAbstract;

@Component
@ConditionalOnProperty(prefix = "spring", name = "mail.host")
public class EmailMqReceive extends RabbitReceiveAbstract {
	
	@Value("${spring.mail.username:market_sys@qt300061.com}")
	private String from;
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public String queueName() {
		return RabbitConfig.QUEUE_EMAIL;
	}

	@Override
	public void receive(Message message) throws Exception {
		Email email = JSON.parseObject(message.getBody(), Email.class);
		
		sendEmail(email);
	}
	
	public void sendEmail(Email email) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setSubject(email.getSubject());
		helper.setText(email.getText(), true);
		helper.setFrom(StringUtils.isBlank(email.getFrom()) ? from : email.getFrom());
		helper.setTo(InternetAddress.parse(email.getTo()));

		// helper.addAttachment("ico.jpg", new File("D:/ico.jpg"));
		
		mailSender.send(mimeMessage);
	}

}
