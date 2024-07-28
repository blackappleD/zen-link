package com.mkc.api.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author tqlei
 * @date 2023/6/17 19:17
 */

public class Md5Utils {

    public static String md5(String text)  {

        return  DigestUtils.md5Hex(text);
    }

    public static void main(String[] args) {
       // System.out.println(md5("123456"));

       // String s = "BJYRZXKJ[{\"name\":\"bbb\", \"cardNum\":\"22222\"},{\"name\":\"aaa\",\"cardNum\":\"11111\"}]3ac730b23371d4fac4f410c1af5bedfa";
        String s = "BJYRZXKJ5f80c677753b409280a0a491bda9941e3ac730b23371d4fac4f410c1af5bedfa";
        System.out.println(md5(s));


    }
}
