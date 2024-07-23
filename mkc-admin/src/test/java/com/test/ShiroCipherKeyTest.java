package com.test;

import com.mkc.common.utils.security.CipherUtils;
import org.apache.shiro.codec.Base64;
/**
 * @author tqlei
 * @date 2023/4/13 9:29
 */

public class ShiroCipherKeyTest {

    public static void main(String[] args) {
        String aes = Base64.encodeToString(CipherUtils.generateNewKey(128, "AES").getEncoded());
        System.out.println(aes);
    }


}
