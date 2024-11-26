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
		System.out.println(Sm4Utils.decryptEcb(Sm4Utils.getSecretKey(APP_SECRET, "1732589279251"), "0HY4YoilGXHnFWekev4rWHxQb7BzaqJkxXxZt6QaiRLZNV6iyuoWRPGvrptufM2AmIdBAHMVwL/FYHolECLfXsi2irSzhTJv886n9+L80YlYZQZ75iSylMcvbPqAWagQgJZ76FBsjy6I4ul7C4SdWXW3Dhqv7moHDjDGADz0axKk9T09yQEIBsCmx2yZuoWZccv7SV2CNvr9SgLucrr9F/QWOLyPaG74A4EJ/w6VE3BsacONimUTYzREeiufjrotFATQddIIoUfS7gl5gNM3F45gyHgx46/e2+isiGrzbelMiLavTU6j/CDCIa508gc8"));
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

		return headers;
	}

}
