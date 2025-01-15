package com.mkc.api.supplier.bg;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.bg.req.HighRiskPeopleReqDTO;
import com.mkc.api.dto.bg.res.HighRiskPeopleResDTO;
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

@Service("BG_QXKJ")
@Slf4j
public class QxkjCkSupImpl implements IBgSupService {

	private final static String SUCCESS = "200";

	private final static String NOGET = "404";

	private final static String NOT = "403";

	private final static String ERROR = "500";

	@Data
	private static class BaseResDTO<T> {
		private String code;
		private String msg;
		private String seqNo;
		private String free;
		private T data;
	}


	@Override
	public SupResult<HighRiskPeopleResDTO> queryHighRiskPeople(HighRiskPeopleReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<HighRiskPeopleResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/bg/kxdrz";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String certNo = dto.getCertNo();

			params.put("certNo", certNo);
			params.put("merCode", appkey);
			params.put("merSeq", dto.getMerSeq());
			params.put("key", appsecret);
			String sign = Md5Utils.md5(appkey + certNo + signPwd);
			params.put("sign", sign);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.post(url, params.toJSONString(), timeOut);
			supResult.setRespTime(LocalDateTime.now());
			log.info(CharSequenceUtil.format("【高风险人群核查】{}", result));
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			BaseResDTO<HighRiskPeopleResDTO> resDto = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<HighRiskPeopleResDTO>>() {
			});
			String code = resDto.getCode();
			supResult.setData(resDto.getData());
			supResult.setFree(resDto.getFree().equals("1") ? FreeStatus.YES : FreeStatus.NO);
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			} else if (NOGET.equals(code)) {
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
			} else if (NOT.equals(code)) {
				supResult.setRemark("不一致");
				supResult.setState(ReqState.NOT);
			} else {
				supResult.setRemark("查询失败");
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【高风险人群核查】接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);
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
