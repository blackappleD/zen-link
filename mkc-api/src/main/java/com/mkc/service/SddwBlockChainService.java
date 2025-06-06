package com.mkc.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.mkc.dto.sddw.AuthInfoGetDTO;
import com.mkc.dto.sddw.AuthInfoPostDTO;
import com.mkc.dto.sddw.ProductDataGetDTO;
import com.mkc.tool.Sm3Utils;
import com.mkc.tool.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
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

	public static void main(String[] args) {
		System.out.println(Sm4Utils.decryptEcb(Sm4Utils.getSecretKey(APP_SECRET, "1732589697638"), "G/cWaxuccsMz5fvuNZFi12+QwggcvgHNIz39K3CdyJvbdKyUHe4vDSBtHXrAWi5RuBCQXi+au0Bn3Z8JofRzPAUjLd4Bz9b+myhY19sYFTl8VziaJ4N4TYiuzBBO7FXNdfYEvrSvxlUe6NOJ11yODtKbRy7j1/8ru4ARYxI7uRabu3G8AqFrH6gAZOYI8xE3OICWJo4N9Ub5BUvp0it7HyXo1NWpJdPFxvl3m1kwxT8MHohSzp/L53tFnxRJwxi30r8lA7VgzQFsEIdm9t+cKJlPpCOTSv1ott4k70H3B9Wdb//s1ck1FFfuQDltmGjT30xkCkQWjc/U2xheRHKFUAcxYcP0QgHNjJCLRPZWJqM="));
	}

	/**
	 * 查询授权信息
	 *
	 * @return
	 */
	public String queryAuthInfo(AuthInfoGetDTO dto) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String uri = "open/v3/api/auth/queryAuthInfo";
		String sm4SecretKey = Sm4Utils.getSecretKey(APP_SECRET, timestamp);
		String body = Sm4Utils.encryptEcb(sm4SecretKey, JSONUtil.toJsonStr(dto));
		try (HttpResponse response = HttpUtil.createPost(BASE_URI + uri)
				.body(body)
				.headerMap(buildHeaders(MapUtil.newHashMap(), timestamp), true)
				.execute()) {
			String responseBody = Sm4Utils.decryptEcb(sm4SecretKey, response.body());
			log.info("【山大地纬_查询授权信息】{}, Body:{}, responseBody:{}", BASE_URI + uri, body, responseBody);

			return responseBody;
		}

	}

	/**
	 * 消息授权模式获取授权令牌
	 *
	 * @return
	 */
	public String applyAuthPerm(AuthInfoPostDTO dto) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		log.info("【消息授权模式获取授权令牌】timestamp:{}", timestamp);
		String uri = "open/v3/api/auth/getAuthIdBaseonMsg";
		String sm4SecretKey = Sm4Utils.getSecretKey(APP_SECRET, timestamp);
		String body = Sm4Utils.encryptEcb(sm4SecretKey, JSONUtil.toJsonStr(dto));
		System.out.println(body);
		try (HttpResponse response = HttpUtil.createPost(BASE_URI + uri)
				.body(body)
				.headerMap(buildHeaders(MapUtil.newHashMap(), timestamp), true)
				.execute()) {
			String responseBody = Sm4Utils.decryptEcb(sm4SecretKey, response.body());
			log.info("【消息授权模式获取授权令牌】{}, Body:{}, responseBody:{}", BASE_URI + uri, body, responseBody);
			return responseBody;
		}
	}

	/**
	 * 查询已授权数据
	 *
	 * @return
	 */
	public String queryData(ProductDataGetDTO dto) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String uri = "open/v3/api/assets/queryAuthDataBasedOnBusiness";
		String sm4SecretKey = Sm4Utils.getSecretKey(APP_SECRET, timestamp);
		String body = Sm4Utils.encryptEcb(sm4SecretKey, JSONUtil.toJsonStr(dto));
		try (HttpResponse response = HttpUtil.createPost(BASE_URI + uri)
				.body(body)
				.headerMap(buildHeaders(MapUtil.newHashMap(), timestamp), true)
				.execute()) {
			String responseBody = Sm4Utils.decryptEcb(sm4SecretKey, response.body());
			log.info("【山大地纬_查询已授权数据】{}, Body:{}, responseBody:{}", BASE_URI + uri, body, responseBody);
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
		headers.put("Content-Type", "application/json");

		return headers;
	}

}
