package com.mkc.tool;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import com.mkc.common.core.text.Convert;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/26 9:46
 */
public class Sm2Utils {

//	public static String decrypt(String data, PrivateKey key) throws InvalidCipherTextException {
//
//		byte[] dataByte = HexUtil.decodeHex(data);
//		BCECPrivateKey privateKey = (BCECPrivateKey) key;
//		ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(), ecDomainParameters);
//		SM2Engine engine = new SM2Engine(new SM3Digest(), SM2Engine.Mode.C1C3C2);
//		engine.init(false, ecPrivateKeyParameters);
//		return Convert.str(engine.processBlock(dataByte, 0, dataByte.length), StandardCharsets.UTF_8);
//	}

}
