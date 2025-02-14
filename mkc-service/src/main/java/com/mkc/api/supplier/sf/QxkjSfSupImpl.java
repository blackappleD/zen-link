package com.mkc.api.supplier.sf;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.bg.res.SsPlusResDTO;
import com.mkc.api.dto.sf.SsPlusReqDTO;
import com.mkc.api.supplier.ISfSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("SF_QXKJ")
@Slf4j
public class QxkjSfSupImpl implements ISfSupService {

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
	public SupResult<SsPlusResDTO> querySsPlus(SsPlusReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<SsPlusResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/sf/ssPlus";
			String merCode = bean.getAcc();
			String signKey = bean.getSignKey();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String certName = dto.getCertName();
			String certNo = dto.getCertNo();
			String startDate = dto.getStartDate();
			String type = dto.getType();

			params.put("certNo ", certNo);
			params.put("certName", certName);
			params.put("type", type);
			params.put("startDate", startDate);

			params.put("merCode", merCode);
			params.put("merSeq", dto.getMerSeq());
			params.put("key", signKey);
			String sign = Md5Utils.md5(merCode + certName + certNo + startDate + signPwd);
			params.put("sign", sign);
			String paramJson = params.toJSONString();
			supResult = new SupResult<>(paramJson, LocalDateTime.now());
			log.info(CharSequenceUtil.format("【司法涉诉公开版】请求体：{}", paramJson));
			result = HttpUtil.post(url, paramJson, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			log.info(CharSequenceUtil.format("【司法涉诉公开版】返回体：{}", result));
			supResult.setRespJson(result);

			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			BaseResDTO<SsPlusResDTO> resDto = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<SsPlusResDTO>>() {
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
			errMonitorMsg(log, " 【司法涉诉公开版】接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
