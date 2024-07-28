package com.mkc.api.supplier.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA2Utils {

    public static String DEFAULT_CHARSET = "UTF-8";

    static {
        //清除安全设置
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }

    /**
     * 生成密钥对
     *
     * @return 密钥对
     * @throws Exception 生成密钥对异常
     */
    public static KeyPair generateRsa2KeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * 解密方法
     *
     * @param cipherTextBase64 已加密内容（经过Base64编码）
     * @param privateKey       私钥（经过Base64编码）
     * @return 解密后明文
     * @throws Exception 解密异常
     */
    public static String doDecrypt(String cipherTextBase64, String privateKey) throws Exception {
        byte[] encryptedData = Base64.decodeBase64(cipherTextBase64.getBytes(DEFAULT_CHARSET));
        PrivateKey priKey = getPrivateKeyFromPKCS8(privateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        int inputLen = encryptedData.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int maxDecrypt = 256;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxDecrypt) {
                    cache = cipher.doFinal(encryptedData, offSet, maxDecrypt);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxDecrypt;
            }
            return new String(out.toByteArray(), DEFAULT_CHARSET);
        }
    }

    /**
     * 加密方法
     *
     * @param plainText 待加密明文
     * @param publicKey 公钥（经过Base64编码）
     * @return 加密后字符串（经过Base64编码）
     * @throws Exception 加密异常
     */
    public static String doEncrypt(String plainText, String publicKey) throws Exception {
        byte[] data = plainText.getBytes(DEFAULT_CHARSET);
        PublicKey pubKey = getPublicKeyFromX509(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            int maxEncrypt = 244;
            int inputLen = data.length;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxEncrypt) {
                    cache = cipher.doFinal(data, offSet, maxEncrypt);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxEncrypt;
            }
            return new String(Base64.encodeBase64(out.toByteArray()), DEFAULT_CHARSET);
        }
    }

    /**
     * 签名方法
     *
     * @param content    待加密字符串
     * @param privateKey 私钥（经过Base64编码）
     * @return 签名结果
     * @throws Exception 签名异常
     */
    public static String doSign(String content, String privateKey) throws Exception {
        PrivateKey priKey = getPrivateKeyFromPKCS8(privateKey);
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(priKey);
        signature.update(content.getBytes(DEFAULT_CHARSET));
        byte[] signed = signature.sign();
        return Base64.encodeBase64String(signed);
    }

    /**
     * 验证签名方法
     *
     * @param content   待验证字符串
     * @param publicKey 公钥
     * @param sign      外部计算的签名字符串（经过Base64编码）
     * @return 签名验证通过返回true，否则返回false
     * @throws Exception 验证签名异常
     */
    public static boolean doVerifySign(String content, String publicKey, String sign) throws Exception {
        PublicKey pubKey = getPublicKeyFromX509(publicKey);
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(pubKey);
        signature.update(content.getBytes(DEFAULT_CHARSET));
        return signature.verify(Base64.decodeBase64(sign));
    }

    private static PrivateKey getPrivateKeyFromPKCS8(String encodedKeyStr) throws Exception {
        if (null == encodedKeyStr || encodedKeyStr.isEmpty()) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(encodedKeyStr);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    private static PublicKey getPublicKeyFromX509(String encodedKeyStr) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(encodedKeyStr);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }
}
