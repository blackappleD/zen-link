package com.mkc.common.utils.security;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * @author tqlei
 * @date 2023/4/25 17:38
 */

public class SHA1Utils {


    /***
     * 利用Apache的工具类实现SHA-256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA1Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encdeStr;
    }


    public static void main(String[] args) {
        System.out.println(getSHA1Str("18621126580"));
    }
}
