package com.mkc.api.supplier.bg;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.dto.bg.EnterpriseFourElementsReqVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.ErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/19 17:07
 */
@Service("BG_BXA")
@Slf4j
public class AntBgSupImpl implements IBgSupService {

	private final static boolean SUCCESS = true;
	private final static boolean NO = false;

	/**
	 * 企业不存在
	 */
	private static final String ERROR_CODE1 = "40001";

	@Override
	public SupResult queryFourElementsInfo(EnterpriseFourElementsReqVo vo, SuplierQueryBean bean) {
		String result = null;
		SupResult supResult = null;
		Map<String, String> parameters = new HashMap<>();
		String url = null;
		try {
			url = bean.getUrl() + "/V2/ValidateFourElements";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();

			String ranStr = RandomUtil.randomString(32);
			parameters.put("companyName", vo.getCompanyName());
			parameters.put("creditCode", vo.getCreditCode());
			parameters.put("legalPerson", vo.getLegalPerson());
			parameters.put("certNo", vo.getCertNo());
			parameters.put("merCode", appkey);
			parameters.put("ranStr", ranStr);
			// 将参数按 key=value 格式排序
			String stringA = sortAndFormatParameters(parameters);
			String stringSignTemp = stringA + "&key=" + appsecret;
			// Md5Utils.md5()
			String signature = Md5Utils.md5(stringSignTemp).toUpperCase();
			HttpRequest post = HttpUtil.createPost(url);
			post.header("signature", signature);
			String postBody = JSONUtil.toJsonStr(parameters);
			post.body(postBody);
			post.timeout(timeOut);
			supResult = new SupResult(postBody, LocalDateTime.now());
			result = post.execute().body();
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark(ErrorConstants.SUP_NO_RESPONSE);
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			boolean success = resultObject.getBoolean("success");
			if (success) {
				supResult.setFree(FreeStatus.YES);
				supResult.setState(ReqState.SUCCESS);
				JSONObject data = resultObject.getJSONObject("data");
				if (data != null) {
					supResult.setData(data);
					return supResult;
				}
			} else {
				if (ERROR_CODE1.equals(resultObject.getString("errorCode"))) {
					supResult.setFree(FreeStatus.NO);
					supResult.setDefinedFailMsg(true);
					supResult.setState(ReqState.ERROR);
					supResult.setRemark(resultObject.getString("message"));
					return supResult;
				} else {
					supResult.setFree(FreeStatus.NO);
					supResult.setRemark("查询失败");
					supResult.setState(ReqState.ERROR);
					errMonitorMsg(log, "  企业四要素信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
							, bean.getOrderNo(), url, result);
				}
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【广州本希奥科技有限公司供应商】 企业四要素信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);

			if (supResult == null) {
				supResult = new SupResult(JSONUtil.toJsonStr(parameters), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常:" + e.getMessage());
			return supResult;
		}

	}

	private static String sortAndFormatParameters(Map<String, String> parameters) {
		// 使用 TreeMap 自动按 key 排序
		Map<String, String> sortedParams = new TreeMap<>(parameters);

		// 构建排序后的字符串
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(entry.getKey()).append("=").append(entry.getValue());
		}

		return sb.toString();
	}
}
