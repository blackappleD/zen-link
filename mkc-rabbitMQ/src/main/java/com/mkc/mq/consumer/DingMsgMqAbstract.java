package com.mkc.mq.consumer;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.mq.receive.RabbitReceiveAbstract;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DingMsgMqAbstract extends RabbitReceiveAbstract {
	
	private static final String APP_KEY = "dingoimrjpcn37uakvew";
	private static final String APP_SECRET = "jOMZT_WGuCmIThxEkzhnsK1DtI6CsMpqyyVd1W5UsLxsxIb1JXSBPcO66ioSnULP";
	
	private static final String APP_TYPE = "APP_OBG6I4D20SP8774RKFXB";
	private static final String SYSTEM_TOKEN = "4A9667B1KT54UI4071DR59QF4E5Z1XTWP6I8LRC";
	private static final String FORM_UUID = "FORM-IS866C918R2D5FB9CO6ES8JKXO7S26ZD2N1LL5";
	private static final String USER_ID = "020650126136845125";
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	private static String apiToken;
	private static long lastTime;
	
	@Autowired
	private RedisCache redisCache;
	

	public abstract String token();
	public abstract String secret();
	public abstract String subject();
	
	@Override
	public void receive(Message message) throws Exception {
		String content = (String) JSON.parse(message.getBody());
		
		String key = RedisKey.DING_TIMES_KEY + queueName() + "_test";
		Long times = getTimes(key);
		while(times > 20) {
			// 发送太频繁等一分钟
			int timeout = (int)(Math.random() * 50) + 10;
			TimeUnit.SECONDS.sleep(timeout);
			times = getTimes(key);
		}
		
		String result = sendDingMsg(content);
		if(StringUtils.isBlank(result)){
			log.warn("dingding msg send failure； token is null --> {}  ,content {}", result,content);
			return ;
		}
		// 发送成功以后再次重置过期时间，因为要以现在的时间开始进行等待
		redisCache.expire(key, 1, TimeUnit.MINUTES);
		
		JSONObject jsonObj = JSON.parseObject(result);
		if(!"ok".equals(jsonObj.getString("errmsg"))) {
			log.warn("dingding msg send failure --> {} , content {}", result,content);
		}
	}
	
	public String sendDingMsg(String content) throws Exception {
		String token=token();
		if(StringUtils.isBlank(token)){
			return null;
		}
		Long timestamp = System.currentTimeMillis();
		String url = "https://oapi.dingtalk.com/robot/send?access_token=" + token + "&timestamp=" + timestamp + "&sign=" + getSign(timestamp);
		
		String formInst = saveFormData(content);
		
		String msgBody = null;
		if(ObjectUtil.isEmpty(formInst)) {
			msgBody = getTextMsgBody(content);
		} else {
			msgBody = getCardMsgBody(content, formInst);
		}
		
		return HttpUtil.post(url, msgBody, 10000);
	}
	
	private String getTextMsgBody(String content) {
		log.info("getTextMsgBody --> content：{}", content);
		
		JSONObject jsonObj = JSON.parseObject("{\"text\":{},\"msgtype\":\"text\"}");
		JSONObject text = jsonObj.getJSONObject("text");
		text.put("content", subject() + content);
		
		return jsonObj.toJSONString();
	}
	private String getCardMsgBody(String text, String formInst) {
		String singleURL = "https://qt300061.aliwork.com/" + APP_TYPE + "/formDetail/" + FORM_UUID + "?formInstId=" + formInst;
		if(ObjectUtil.isEmpty(formInst)) {
			singleURL = "@link";
		}
		
		log.info("getCardMsgBody --> singleURL：{}，text：{}", singleURL, text);
		
		int len = text.length();
		int mark = text.indexOf("@#");
		int split = mark > 0 ? mark : len > 25 ? 25 : len;
		
		String title = subject() + text.substring(0, split).replace("<", "&lt;");
		if(mark > 0) split += 2;
		text = len > 180 + split ? text.substring(split, 180 + split) + "..." : text.substring(split);
		
		text = "### " + title + "\n\n" + text.replace("<", "&lt;");
		
		JSONObject jsonObj = JSON.parseObject("{\"actionCard\":{},\"msgtype\":\"actionCard\"}");
		JSONObject actionCard = jsonObj.getJSONObject("actionCard");
		actionCard.put("title", title);
		actionCard.put("text", text);
		actionCard.put("btnOrientation", 0);
		actionCard.put("singleTitle", "查看详情");
		actionCard.put("singleURL", singleURL);
		
		return jsonObj.toJSONString();
	}
	
	
	private String saveFormData(String content) {
		if(content.length() < 205) {
			return null;
		}
		
		lock.lock();
		long currTime = System.currentTimeMillis();
		try {
			if(apiToken == null || currTime - lastTime >= (7200 - 100) * 1000) {
				lastTime = currTime;
				
				String url = "https://api.dingtalk.com/v1.0/oauth2/accessToken";
				JSONObject form = new JSONObject();
				form.put("appKey", APP_KEY);
				form.put("appSecret", APP_SECRET);
				
				String jsonStr = HttpUtil.post(url, form.toJSONString());
				apiToken = JSON.parseObject(jsonStr).getString("accessToken");
				
				log.info("saveFormData getNewToken --> accessToken：{}, currTime: {}", apiToken, currTime);
			} else {
				log.info("saveFormData useOldToken --> accessToken：{}, currTime: {}, lastTime: {}, diff: {}", apiToken, currTime, lastTime, currTime - lastTime);
			}
		} catch (Exception e) {
			log.info("saveFormData 获取token时异常 --> currTime: {}, lastTime: {}, diff: {}", currTime, lastTime, currTime - lastTime);
		} finally {
			lock.unlock();
		}
		
		String api = "https://api.dingtalk.com/v1.0/yida/forms/instances";
		JSONObject form = new JSONObject();
		form.put("appType", APP_TYPE);
		form.put("systemToken", SYSTEM_TOKEN);
		form.put("userId", USER_ID);
		form.put("formUuid", FORM_UUID);
		
		JSONObject formDataJson = new JSONObject();
		formDataJson.put("selectField_ll1n5t6f", "数据营销");
		formDataJson.put("textareaField_ll1n5t6h", content);
		form.put("formDataJson", formDataJson.toJSONString());
		
		String jsonStr = HttpUtil.createPost(api)
				.header("x-acs-dingtalk-access-token", apiToken)
				.body(form.toJSONString())
				.execute().body();
		
		log.info("saveFormData insertForm --> {}", jsonStr);
		
		return JSON.parseObject(jsonStr).getString("result");
	}
	
	private Long getTimes(String key) {
		Long times = redisCache.increment(key);
		Long expire = redisCache.getExpire(key);
		if(expire == -1) redisCache.expire(key, 1, TimeUnit.MINUTES);
		return times;
	}

	private String getSign(Long timestamp) throws Exception {
		String stringToSign = timestamp + "\n" + secret();
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret().getBytes("UTF-8"), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
		return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
	}
	
}
