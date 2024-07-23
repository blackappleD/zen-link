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
        System.out.println(md5("123456"));
    }
}
