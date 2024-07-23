package com.mkc.mq.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson2.JSON;

@Configuration
public class MessageConverterConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new AbstractMessageConverter() {
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                return JSON.parse(message.getBody());
            }
            @Override
            protected Message createMessage(Object object, MessageProperties messageProperties) {
                return new Message(JSON.toJSONBytes(object), messageProperties);
            }
        };
    }
    
}
