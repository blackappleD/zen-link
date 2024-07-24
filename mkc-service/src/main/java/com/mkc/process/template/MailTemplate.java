package com.mkc.process.template;

/**
 * @author tqlei
 * @date 2023/7/17 15:34
 */

public interface MailTemplate {

    public final static String E_MER_KEY="<p>尊敬的 %s 客户：</p>\n" +
            "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！以下是贵司在我司开通的账号相关信息，该信息作为贵司在我司系统后台身份识别的唯一标识，请注意保密！</p>\n" +
            "<p></p>\n" +
            "<p>merCode：%s</p>\n" +
            "<p>秘钥KEY：%s</p>\n" +
            "<p>秘钥PWD：%s</p>\n" +
            "<p>生产地址：http://api.zjbhsk.com</p>\n" +
            "<p></p>\n" +
            "<p></p>\n" +
            "<p>祝商祺！</p>\n" +
            "<p>——————————————————————</p>\n" +
            "<p>浙江百行数据科技有限责任公司</p>\n";


}
