package com.mkc.api.common.tool;


import com.mkc.common.utils.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用
 * 数据库aes加密解密
 * -- 加密
 *    SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 *    -- 解密
 *    SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 * @author 836508
 *
 */
@Slf4j
public class AESUtil {

    //"算法/模式/补码方式"  CBC 密文每次不一样，ECB每次都一样
    private static String ALGORITHM="AES/ECB/PKCS5Padding";

    private static final String KEY_ALGORITHM = "AES";
    private static final String KEY = "BHSK_DATA_2024!!";

    /***
     *
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText)  {
        try {
           // byte[] raw = sKey.getBytes("utf-8");
            //SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            SecretKey skeySpec =getSecretKey(KEY);

            Cipher cipher = Cipher.getInstance(ALGORITHM);//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes("utf-8"));
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return  Base64Util.encode(encrypted);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
    /***
     *
     * @param plainText
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, String sKey)  {
        try {
           // byte[] raw = sKey.getBytes("utf-8");
            //SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            SecretKey skeySpec =getSecretKey(sKey);

            Cipher cipher = Cipher.getInstance(ALGORITHM);//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes("utf-8"));
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return  Base64Util.encode(encrypted);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    // 解密
    public static String decrypt(String sSrc) throws Exception {
        try {

            //byte[] raw = sKey.getBytes("utf-8");
            //SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            SecretKey skeySpec =getSecretKey(KEY);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 =  Base64Util.decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    // 解密
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {

            //byte[] raw = sKey.getBytes("utf-8");
            //SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            SecretKey skeySpec =getSecretKey(sKey);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 =  Base64Util.decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    //SecretKeySpec
    private static SecretKey getSecretKey(String sKey) throws NoSuchAlgorithmException {

        // 判断Key是否正确
        if (StringUtils.isBlank(sKey)) {
            System.out.print("signKey为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("signKey长度不是16位");
            return null;
        }


    /*
        linux 解密有问题
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        kg.init(128, new SecureRandom(encryptPass.getBytes()));
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥*/

        try{
            KeyGenerator _generator=KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom=SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(sKey.getBytes());
            _generator.init(128,secureRandom);
            return _generator.generateKey();
        }catch(Exception e){
            throw new RuntimeException("初始化密钥出现异常");
        }

    }



    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "jkl;POIU1234++==";
        // 需要加密的字串
        String cSrc = "www.gowhere.so";
        System.out.println(cSrc);
        // 加密
        String enString = AESUtil.encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AESUtil.decrypt(enString, cKey);
        System.out.println("解密后的字串是：" + DeString);

        System.out.println(encrypt("123456"));
    }
}