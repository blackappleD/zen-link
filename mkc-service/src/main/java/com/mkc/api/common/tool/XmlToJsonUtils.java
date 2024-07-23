package com.mkc.api.common.tool;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.XML;

import java.io.IOException;

/**
 * @author tqlei
 * @date 2023/5/31 17:02
 */

public class XmlToJsonUtils {

    public static JSONObject hXmlToJson2(String xml)  {

        JSONObject entries = JSONUtil.parseFromXml(xml);
        JSONObject errorRes = entries.getJSONObject("PSG_certIDverify")
                .getJSONObject("ErrorRes");
        System.out.println(entries.toString());
        System.out.println(errorRes.toString());
        return entries;

    }
    public static String XmlToJson2(String xml) throws IOException {
        JSONObject object =  XML.toJSONObject(xml);
        return  object.toString();
    }


    public static void main(String[] args) throws IOException {
        String xml="<PSG_certIDverify xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://xiaoher.jzdata.com/\"><ErrorRes><Err_code>200</Err_code><Err_content>身份证与姓名一致</Err_content></ErrorRes></PSG_certIDverify>";

        System.out.println(hXmlToJson2(xml));
        System.out.println(XmlToJson2(xml));


    }

}
