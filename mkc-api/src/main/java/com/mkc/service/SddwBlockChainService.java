package com.mkc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.mkc.dto.sddw.AuthInfoReqDTO;
import com.mkc.tool.Sm3Utils;
import com.mkc.tool.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.TIMEOUT;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/25 15:02
 */
@Slf4j
@Service
public class SddwBlockChainService {

	private static final String BASE_URI = "http://dareway.cn:14037/gateway/";
	private static final String APP_ID = "e10606debc1d4fe5be0507b5cb8a06d6";
	private static final String APP_SECRET = "b427ba38a529415fa054bffe00b8cf76";

	// 山大地纬 区块链服务测试
	public static class PreCheck {

	}


	/**
	 * @return
	 */
	public String queryAuthInfo(AuthInfoReqDTO dto) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String uri = "open/v3/api/auth/queryAuthInfo";
		String sm4SecretKey = Sm4Utils.getSecretKey(APP_SECRET, timestamp);
		String body = Sm4Utils.encryptEcb(sm4SecretKey, JSONUtil.toJsonStr(dto));
		try (HttpResponse response = HttpUtil.createPost(BASE_URI + uri)
				.body(body)
				.headerMap(buildHeaders(MapUtil.newHashMap(), timestamp), true)
				.execute()) {
			String responseBody = response.body();
			log.info("【山大地纬_区块链认证信息查询】{}, Body:{}, responseBody:{}", BASE_URI + uri, body, responseBody);
			Sm4Utils.decryptEcb(sm4SecretKey, responseBody);
			return responseBody;
		}

	}

	/**
	 * 构造header
	 *
	 * @param headers
	 * @return
	 */
	private static Map<String, String> buildHeaders(Map<String, String> headers, String timestamp) {

		String nonce = RandomUtil.randomString(32);
		headers.put("appId", APP_ID);
		headers.put("nonce", nonce);
		headers.put("timestamp", timestamp);
		headers.put("signature", Sm3Utils.getSign(APP_ID, APP_SECRET, timestamp, nonce));
		headers.put("secret-method", "dsm-1");

		return headers;
	}

}
