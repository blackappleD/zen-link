package com.mkc.api.supplier.bg;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.bg.req.CorporateAppointmentsReqDTO;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service("BG_MOKA")
@Slf4j
public class MokaSupImpl implements IBgSupService {

	private final static String SUCCESS = "00";

	private final static String NO_FOUND = "01";

	@Data
	private static class BaseResDTO<T> {
		private String code;
		private String msg;
		private String seqNo;
		private String free;
		private T data;
	}

	@Data
	private static class BaseReqDTO {
		private String userid;

		private String api;

		private String params;

		private String sign;

		public static BaseReqDTO create(String userid, String api) {
			BaseReqDTO req = new BaseReqDTO();
			req.setUserid(userid);
			req.setApi(api);
			return req;
		}

		public BaseReqDTO params(Object params) {
			this.params = JsonUtil.toJson(params);
			return this;
		}

		public BaseReqDTO sign(String key) {

			this.sign = Md5Utils
					.md5(CharSequenceUtil.format("userid={}&api={}&params={}&key={}", userid, api, params, key));
			return this;
		}

		public String getRequestUrl(String baseUrl) {
			return CharSequenceUtil.format("{}?userid={}&api={}&params={}&sign={}", baseUrl, userid, api, params, sign);
		}
	}

	@Override
	public SupResult corporateAppointments(CorporateAppointmentsReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<JSONObject> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl();
			String userid = bean.getAcc();
			String api = "1003";
			String key = bean.getSignKey();
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());

			Map<String, String> map = new HashMap<>();
			String idCard = dto.getIdCard();
			// 判断是否是MD5字符串
			if (idCard != null && (idCard.length() != 32 && idCard.length() != 64)) {
				// 不是MD5字符串，先转大写再进行MD5加密
				idCard = Md5Utils.md5(idCard.toUpperCase());
			}
			map.put("cerno", idCard);
			map.put("ent", dto.getEnterpriseName());
			map.put("name", dto.getName());

			String requestUrl = BaseReqDTO.create(userid, api)
					.params(map)
					.sign(key)
					.getRequestUrl(url);

			log.info("请求地址：{}", requestUrl);
			result = HttpUtil.get(requestUrl);
			supResult.setRespTime(LocalDateTime.now());
			log.info(CharSequenceUtil.format("【企业任职关联信息查询】{}", result));
			supResult.setRespJson(result);

			// 判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			JSONObject jsonObject = JSON.parseObject(result);
			String code = jsonObject.getString("code");

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(jsonObject.getJSONObject("data"));
			} else if (NO_FOUND.equals(code)) {
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
			} else {
				supResult.setRemark("查询失败");
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【企业任职关联信息查询】接口 发生异常 orderNo {} URL {} , 报文: {} , err {}", bean.getOrderNo(), url,
					result, e);
			if (supResult == null) {
				supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}
	}
}
