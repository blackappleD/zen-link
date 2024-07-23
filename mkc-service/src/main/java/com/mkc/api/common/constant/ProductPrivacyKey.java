package com.mkc.api.common.constant;

import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.tool.AESUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 需要脱敏的字段
 *
 * @author tqlei
 * @date 2023/5/29 16:48
 */

public class ProductPrivacyKey {
    /***
     * 商户敏感字段参数
     */
    private static Set<String> merReqSet;
    /***
     * 供应商敏感字段参数
     */
    private static Set<String> supReqSet;

    static {
        merReqSet = new HashSet<>();
        merReqSet.add("certno");
        merReqSet.add("pid");
        merReqSet.add("name");
        merReqSet.add("certname");
        merReqSet.add("mobile");
        merReqSet.add("bankcard");

        supReqSet = new HashSet<>();
        supReqSet.add("passname");
        supReqSet.add("pid");
        supReqSet.add("mobile");
        supReqSet.add("bankcard");
        supReqSet.add("certno");
        supReqSet.add("certname");
        supReqSet.add("username");
        supReqSet.add("name");
        supReqSet.add("usercertno");

    }


    public static JSONObject privacyMer(JSONObject json) {
        json.keySet().forEach((key)->{
            //将KEY 转小写
            String lowerKey = key.toLowerCase();
            if (merReqSet.contains(lowerKey)) {
                String val = json.getString(key);
                if (StringUtils.isNotBlank(val)) {
                    String encrypt = AESUtil.encrypt(val);
                    json.put(key, encrypt);
                }
            }
        });
//        for (String key : merReqSet) {
//            if (json.containsKey(key)) {
//                String val = json.getString(key);
//                if (StringUtils.isBlank(val)) {
//                    continue;
//                }
//                String encrypt = AESUtil.encrypt(val);
//                json.put(key, encrypt);
//            }
//        }
        return json;
    }



    public static JSONObject privacyDecryptMer(JSONObject json) {

        json.keySet().forEach((key)->{
            //将KEY 转小写
            String lowerKey = key.toLowerCase();
            if (merReqSet.contains(lowerKey) || supReqSet.contains(lowerKey)) {
                String val = json.getString(key);
                if (StringUtils.isNotBlank(val)) {
                    String encrypt = null;
                    try {
                         encrypt = AESUtil.decrypt(val);
                    }catch (Exception e){
                        encrypt=val;
                    }
                    json.put(key, encrypt);
                }
            }
        });

        return json;
    }



    public static JSONObject privacySup(JSONObject json) {

        json.keySet().forEach((key)->{
            //将KEY 转小写
            String lowerKey = key.toLowerCase();
            if (supReqSet.contains(lowerKey)) {
                String val = json.getString(key);
                if (StringUtils.isNotBlank(val)) {
                    String encrypt = AESUtil.encrypt(val);
                    json.put(key, encrypt);
                }
            }
        });



//        for (String key : supReqSet) {
//            if (json.containsKey(key)) {
//                String val = json.getString(key);
//                if (StringUtils.isBlank(val)) {
//                    continue;
//                }
//                String encrypt = AESUtil.encrypt(val);
//                json.put(key, encrypt);
//            }
//        }
        return json;
    }


}
