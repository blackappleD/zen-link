package com.mkc.api.supplier.ck;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.ck.req.EnterpriseThreeElementsReqDTO;
import com.mkc.api.dto.ck.res.EnterpriseThreeElementsResDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.ErrorConstants;
import com.mkc.util.JsonUtil;
import lombok.Data;
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
@Service("CK_BXA")
@Slf4j
public class BxaCkSupImpl implements ICkSupService {

	private final static boolean SUCCESS = true;
	private final static boolean NO = false;

	/**
	 * 企业不存在
	 */
	private static final String ERROR_CODE1 = "40001";

	@Data
	private static class BaseResponse<T> {
		private Boolean success;

		private String errorCode;

		private String message;

		private T data;
	}


	@Override
	public SupResult<EnterpriseThreeElementsResDTO> enterpriseThreeElements(EnterpriseThreeElementsReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<EnterpriseThreeElementsResDTO> supResult = null;
		Map<String, String> parameters = new HashMap<>();
		String url = null;
		String jsonStr = "";
		try {
			url = bean.getUrl() + "/ValidateThreeElements";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();

			String ranStr = RandomUtil.randomString(32);
			parameters.put("orgName", vo.getOrgName());
			parameters.put("orgCertNo", vo.getOrgCertNo());
			parameters.put("legalPerson", vo.getLegalPerson());
			parameters.put("merCode", appkey);
			parameters.put("ranStr", ranStr);
			String stringA = sortAndFormatParameters(parameters);
			String stringSignTemp = stringA + "&key=" + appsecret;
			String signature = Md5Utils.md5(stringSignTemp).toUpperCase();
			jsonStr = JSONUtil.toJsonStr(parameters);
			try (HttpResponse response = HttpUtil.createPost(url).
					header("signature", signature)
					.body(jsonStr)
					.timeout(timeOut)
					.execute()) {
				result = response.body();
			}
			supResult = new SupResult<>(jsonStr, LocalDateTime.now());
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);

			if (StringUtils.isBlank(result)) {
				supResult.setRemark(ErrorConstants.SUP_NO_RESPONSE);
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResponse<EnterpriseThreeElementsResDTO> res = JsonUtil.fromJson(result, new TypeReference<BaseResponse<EnterpriseThreeElementsResDTO>>() {
			});

			boolean success = res.getSuccess();
			if (success) {
				supResult.setFree(FreeStatus.YES);
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(res.getData());
				return supResult;
			} else {
				if (ERROR_CODE1.equals(res.getErrorCode())) {
					supResult.setFree(FreeStatus.NO);
					supResult.setDefinedFailMsg(true);
					supResult.setState(ReqState.ERROR);
					supResult.setRemark(res.getMessage());
					return supResult;
				} else {
					supResult.setFree(FreeStatus.NO);
					supResult.setRemark("查询失败");
					supResult.setState(ReqState.ERROR);
					errMonitorMsg(log, "  企业三要素信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
							, bean.getOrderNo(), url, result);
				}
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【广州本希奥科技有限公司供应商】 企业四要素信息查询 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);

			if (supResult == null) {
				supResult = new SupResult<>(jsonStr, LocalDateTime.now());
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
