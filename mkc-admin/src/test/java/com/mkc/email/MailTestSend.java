package com.mkc.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


/**
 * @author tqlei
 * @date 2023/7/17 9:37
 */

@SpringBootTest
public class MailTestSend {


    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void contextLoads() {
        Context context = new Context();
        context.setVariable("username", "qx");
        context.setVariable("position", "总经理");
        context.setVariable("salary", "10000");
        // 返回渲染后的内容 通过邮件等发送出去
        String email = templateEngine.process("email/merKey.html", context);
        System.out.println(email);
    }




}
