package com.mkc.api.supplier.sf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.dto.bg.res.EnterpriseLitigationResDTO;
import com.mkc.api.dto.bg.res.PersonLitigationResDTO;
import com.mkc.api.dto.sf.EnterpriseLitigationReqDTO;
import com.mkc.api.dto.sf.PersonLitigationReqDTO;
import com.mkc.api.supplier.ISfSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/23 16:12
 */
@Service("SF_CDDATAGROUP")
@Slf4j
public class CdDataGroupSfSupImpl implements ISfSupService {

	private final static String SUCCESS = "200";

	private final static String NO_FEE = "0";

	private final static String ERROR1 = "207";

	private final static String ERROR2 = "208";

	private final static String ERROR3 = "603";

	private final static String ERROR4 = "604";

	@Override
	public SupResult<PersonLitigationResDTO> personLitigation(PersonLitigationReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<PersonLitigationResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/xs-api/xs-data-api/api/proxy/a18d1ec856a8789fd";
			String signKey = bean.getSignKey();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String idcard = dto.getIdCard();
			String familyName = dto.getFamilyName();
			String inquiredAuth = dto.getInquiredAuth();
			String authorization = dto.getAuthorization();

			JSONObject entries = new JSONObject();
			entries.put("name", name);
			entries.put("idcard", idcard);
			entries.put("inquired_auth", inquiredAuth);
			if (CharSequenceUtil.isNotBlank(familyName)) {
				entries.put("family_name", familyName);
			}
			entries.put("authorization", authorization);
			params.put("param", entries);
			String payload = params.toString();
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = reqData(url, signKey, signPwd, payload, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");
			String status = resultObject.getString("status");
			if (NO_FEE.equals(status)) {
				supResult.setFree(FreeStatus.NO);
			} else {
				supResult.setFree(FreeStatus.YES);
			}
			if (SUCCESS.equals(code)) {
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

				PersonLitigationResDTO data = JsonUtil.fromJson(resultObject.getString("data"), PersonLitigationResDTO.class);
				supResult.setData(data);
			} else if (ERROR1.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else if (ERROR2.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else if (ERROR3.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else if (ERROR4.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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

	@Override
	public SupResult<EnterpriseLitigationResDTO> enterpriseLitigation(EnterpriseLitigationReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<EnterpriseLitigationResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/xs-api/xs-data-api/api/proxy/a18cf99c10f4789fe";
			String signKey = bean.getSignKey();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String orgName = dto.getOrgName();
			String uscc = dto.getUscc();
			String inquiredAuth = dto.getInquiredAuth();
			String authorization = dto.getAuthorization();

			JSONObject entries = new JSONObject();
			entries.put("org_name", orgName);
			if (CharSequenceUtil.isNotBlank(uscc)) {
				entries.put("uscc", uscc);
			}
			entries.put("inquired_auth", inquiredAuth);
			entries.put("authorization", authorization);
			params.put("param", entries);
			String payload = params.toString();
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = reqData(url, signKey, signPwd, payload, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");
			String status = resultObject.getString("status");
			if (NO_FEE.equals(status)) {
				supResult.setFree(FreeStatus.NO);
			} else {
				supResult.setFree(FreeStatus.YES);
			}
			if (SUCCESS.equals(code)) {
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

				EnterpriseLitigationResDTO data = JsonUtil.fromJson(resultObject.getString("data"), EnterpriseLitigationResDTO.class);
				supResult.setData(data);
			} else if (ERROR1.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else if (ERROR2.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else if (ERROR3.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else if (ERROR4.equals(code)) {
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setState(ReqState.ERROR);
				supResult.setDefinedFailMsg(true);
				errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【成都数据集团股份有限公司】 全国⼈⼝身份信息三要素核验 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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


	/**
	 * 基于将appKey、timestamp、appSecret⽣成sign，将签名结果转换为⼗六进制
	 *
	 * @param timestamp 当前时间
	 * @return ⼗六进制字符串
	 */
	public static String createSign(String appKey, String timestamp, String appSecret) {
		return DigestUtils.md5Hex(appKey + timestamp + appSecret);
	}

	/**
	 * 请求接⼝，获取数据
	 *
	 * @return
	 */
	public static String reqData(String url, String appKey, String appSecret, String payload, Integer timeOut) {
		String timestamp = DateUtil.now(); // ⽣成当前时间

		return HttpRequest.post(url)
				.header("Content-Type", "application/json")
				.header("appKey", appKey)
				.header("timestamp", timestamp)
				.header("sign", createSign(appKey, timestamp, appSecret))
				.body(payload)
				.timeout(timeOut).execute().body();
	}
}
