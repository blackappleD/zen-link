package com.mkc.common.utils;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author wuq
 * @version 1.0.0
 * @Description Base64工具类
 * @createTime 2022年09月22日 08:50:00
 */
public class Base64Util {

    private static final Charset DEFAULT_CHARSET;

    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
    }


    public static  byte[]  encode (String str) {
        byte[] src=str.isEmpty() ? new byte[0] : str.getBytes(DEFAULT_CHARSET);
        return _encode(src);
    }
    public static  String  encode (byte[] src) {
        return new String(_encode(src));
    }

    // 编码
    public static String encodeString(String str) {
        byte[] src=str.isEmpty() ? new byte[0] : str.getBytes(DEFAULT_CHARSET);
        return new String(_encode(src));
    }

    public static byte[]  _encode(byte[] src) {
        return src.length == 0 ? src : Base64.getEncoder().encode(src);
    }

    // 解码
    public static String decodeString(String str) throws UnsupportedEncodingException {
        byte[] src=str.isEmpty() ? new byte[0] : str.getBytes(DEFAULT_CHARSET);
        return new String(_decode(src));
    }
    public static byte[] decode(String str) throws UnsupportedEncodingException {
        byte[] src=str.isEmpty() ? new byte[0] : str.getBytes(DEFAULT_CHARSET);
        return _decode(src);
    }
    public static byte[] decode(byte[] src) throws UnsupportedEncodingException {
        return _decode(src);
    }


    private static  byte[]  _decode( byte[] src) throws UnsupportedEncodingException {

        return src.length == 0 ? src : Base64.getDecoder().decode(src);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Base64Util.encode(new String("wdasd").getBytes()));

    }
}
