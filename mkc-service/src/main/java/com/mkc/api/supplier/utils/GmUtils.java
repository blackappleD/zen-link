package com.mkc.api.supplier.utils;

/**
 * @author tqlei
 * @date 2024/7/1 13:07
 */

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

public class GmUtils {
    public static Cipher sm4CbcPkcs7PaddingCipher;
    public static Cipher sm4EcbPkcs7PaddingCipher;

    public static byte[] sm4Encrypt(byte[] input, byte[] key, byte[] iv)
            throws Exception {
        return sm4(input, key, iv, 1);
    }

    public static String sm4Encrypt(String input, String key, String iv) {
        try {
            return Hex.encodeHexString(sm4Encrypt(input.getBytes("UTF-8")
                    , Hex.decodeHex(key.toCharArray()),
                    iv == null ? null : iv.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sm4Encrypt(String input, String key) {
        try {
            return Hex.encodeHexString(sm4Encrypt(input.getBytes("UTF-8")
                    , Hex.decodeHex(key.toCharArray()), null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] sm4Decrypt(byte[] input, byte[] key, byte[] iv)
            throws Exception {
        return sm4(input, key, iv, 2);
    }

    public static String sm4Decrypt(String input, String key, String iv)
            throws Exception {
        return new String(sm4Decrypt(Hex.decodeHex(input.toCharArray()),
                Hex.decodeHex(key.toCharArray()),
                iv == null ? null : iv.getBytes()), "UTF-8");
    }

    public static String sm4Decrypt(String input, String key) throws Exception {
        return new String(sm4Decrypt(Hex.decodeHex(input.toCharArray()),
                Hex.decodeHex(key.toCharArray()), null),
                "UTF-8");
    }

    private static byte[] sm4(byte[] input, byte[] key, byte[] iv, int mode) throws Exception {
        IvParameterSpec ivParameterSpec = null;
        if (iv != null) {
            ivParameterSpec = new IvParameterSpec(iv);
        }
        SecretKeySpec sm4Key = new SecretKeySpec(key, "SM4");
        if (ivParameterSpec == null) {
            sm4EcbPkcs7PaddingCipher.init(mode, sm4Key);
            return sm4EcbPkcs7PaddingCipher.doFinal(input);
        }
        sm4CbcPkcs7PaddingCipher.init(mode, sm4Key, ivParameterSpec);
        return sm4CbcPkcs7PaddingCipher.doFinal(input);
    }

    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    uid.append((char) (rd.nextInt(25) + 97));
            }
        }
        return uid.toString();
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
        try {
            sm4CbcPkcs7PaddingCipher = Cipher.getInstance(SM4ModeAndPaddingEnum.SM4_CBC_PKCS7Padding.getName(), "BC");
            sm4EcbPkcs7PaddingCipher = Cipher.getInstance(SM4ModeAndPaddingEnum.SM4_ECB_PKCS7Padding.getName(), "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public static enum SM4ModeAndPaddingEnum {
        SM4_ECB_NoPadding("SM4/ECB/NoPadding"), SM4_ECB_PKCS5Padding("SM4/ECB/PKCS5Padding"),
        SM4_ECB_PKCS7Padding("SM4/ECB/PKCS7Padding"), SM4_CBC_NoPadding(
                "SM4/CBC/NoPadding"),
        SM4_CBC_PKCS5Padding("SM4/CBC/PKCS5Padding"), SM4_CBC_PKCS7Padding("SM4/CBC/PKCS7Padding");
        private String name;

        private SM4ModeAndPaddingEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static void main(String[] args) throws Exception {
        String message = sm4Encrypt("1111", "E96E7405FF99660F7F6CBAC0B7C5CC27");
        System.out.println(message);
        System.out.println(message);


        String str = sm4Decrypt("7ef77eae347e4086f1490ceffec4b399", "E96E7405FF99660F7F6CBAC0B7C5CC27");
        System.out.println(str);
    }
}