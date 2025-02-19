package com.mkc.api.supplier.bg;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.bg.req.*;
import com.mkc.api.dto.bg.res.CreditA016ResDTO;
import com.mkc.api.dto.bg.res.CreditA107ResDTO;
import com.mkc.api.dto.bg.res.CreditA108ResDTO;
import com.mkc.api.dto.bg.res.FinanceIcsResDTO;
import com.mkc.api.supplier.IBgSupService;
import com.mkc.api.supplier.utils.UrlUtils;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author xiewei
 * @date 2024/08/06 15:46
 */
@Service("BG_YRZX")
@Slf4j
public class YrzxBgSupImpl implements IBgSupService {

	private final static String SUCCESS = "001";

	private final static String EMPTY = "999";

	private final static String ERROR_CODE1 = "005";

	@Data
	public static class BaseResDTO<T> {
		private String code;
		private String msg;
		private String uid;
		private String reqid;
		private T result;
		private String verify;
	}

	@Override
	public SupResult<CreditA108ResDTO> queryCreditA108(CreditA108ReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<CreditA108ResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/model/credit/A018/v3";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String cid = dto.getCid();
			String mobile = dto.getMobile();

			params.put("account", appkey);
			params.put("cid", cid);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));

			String verify = params.getString("account") + params.getString("cid") +
					params.getString("name") + params.getString("mobile") +
					params.getString("reqid") + appsecret;
			params.put("verify", Md5Utils.md5(verify).toUpperCase());
			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResDTO<CreditA108ResDTO> response = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<CreditA108ResDTO>>() {
			});
			String code = response.getCode();
			String msg = response.getMsg();
			CreditA108ResDTO data = response.getResult();

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(data);
			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(data);
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  授权多头 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  授权多头 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 授权多头 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<CreditA107ResDTO> queryCreditA107(CreditA107ReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<CreditA107ResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/model/credit/A017/v3";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String cid = dto.getCid();
			String mobile = dto.getMobile();

			params.put("account", appkey);
			params.put("cid", cid);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));

			String verify = params.getString("account") + params.getString("cid") +
					params.getString("name") + params.getString("mobile") +
					params.getString("reqid") + appsecret;
			params.put("verify", Md5Utils.md5(verify).toUpperCase());
			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResDTO<CreditA107ResDTO> response = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<CreditA107ResDTO>>() {
			});
			String code = response.getCode();
			String msg = response.getMsg();
			CreditA107ResDTO data = response.getResult();

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(data);
			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(data);
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  申请多头 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  申请多头 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 申请多头 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<CreditA016ResDTO> queryCreditA016(CreditA016ReqDTO dto, SuplierQueryBean bean) {

		String result = null;
		SupResult<CreditA016ResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/model/credit/A016/v2";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String cid = dto.getCid();
			String mobile = dto.getMobile();

			params.put("account", appkey);
			params.put("cid", cid);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));

			String verify = params.getString("account") + params.getString("cid") +
					params.getString("name") + params.getString("mobile") +
					params.getString("reqid") + appsecret;
			params.put("verify", Md5Utils.md5(verify).toUpperCase());
			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResDTO<CreditA016ResDTO> response = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<CreditA016ResDTO>>() {
			});
			String code = response.getCode();
			String msg = response.getMsg();
			CreditA016ResDTO data = response.getResult();

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(data);
			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(data);
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  逾期多头 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  逾期多头 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 逾期多头 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult queryFinanceInfoV3(FinanceInfoV3ReqVo vo, SuplierQueryBean bean) {
		return getSupResult(vo, bean, "/yrzx/finan/net/jjnl/v3");
	}

	@Override
	public SupResult queryFinanceInfoV7(FinanceInfoV3ReqVo vo, SuplierQueryBean bean) {
		return getSupResult(vo, bean, "/yrzx/finan/net/jjnl/v7");
	}

	private SupResult getSupResult(FinanceInfoV3ReqVo vo, SuplierQueryBean bean, String urlSuffix) {
		String result = null;
		SupResult supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + urlSuffix;
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = vo.getName();
			String mobile = vo.getMobile();
			String idCard = vo.getIdCard();

			params.put("account", appkey);
			params.put("idCard", idCard);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));
			StringBuilder verify = new StringBuilder();
			verify.append(params.getString("account")).append(params.getString("idCard"))
					.append(params.getString("name")).append(params.getString("mobile"))
					.append(params.getString("reqid")).append(appsecret);
			params.put("verify", Md5Utils.md5(verify.toString()).toUpperCase());
			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

				JSONObject resultJson = resultObject.getJSONObject("result");
				if (resultJson != null) {
					supResult.setData(resultJson);
					return supResult;
				}

			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(resultObject.getString("msg"));
				return supResult;
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, url + " 经济能力评级接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
				return supResult;
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 经济能力评级 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}
	}


	@Override
	public SupResult queryFinanceInfo(FinanceInfoReqVo vo, SuplierQueryBean bean) {

		String result = null;
		SupResult supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/finan/net/2w";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = vo.getName();
			String mobile = vo.getMobile();
			String idCard = vo.getIdCard();

			params.put("account", appkey);
			params.put("idCard", idCard);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));
			StringBuilder verify = new StringBuilder();
			verify.append(params.getString("account")).append(params.getString("idCard"))
					.append(params.getString("name")).append(params.getString("mobile"))
					.append(params.getString("reqid")).append(appsecret);
			params.put("verify", Md5Utils.md5(verify.toString()).toUpperCase());
			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

				JSONObject resultJson = resultObject.getJSONObject("result");
				if (resultJson != null) {
					supResult.setData(resultJson);
					return supResult;
				}

			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(resultObject.getString("msg"));
				return supResult;
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力2W查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
				return supResult;
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 经济能力2W 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}

	}

	@Override
	public SupResult queryEconomicRateInfo(EconomicRateReqVo vo, SuplierQueryBean bean) {
		String result = null;
		SupResult supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/finan/net/jjnl/v2";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = vo.getName();
			String mobile = vo.getMobile();
			String idCard = vo.getIdCard();

			params.put("account", appkey);
			params.put("idCard", idCard);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));

			StringBuilder verify = new StringBuilder();
			verify.append(params.getString("account")).append(params.getString("idCard"))
					.append(params.getString("name")).append(params.getString("mobile"))
					.append(params.getString("reqid")).append(appsecret);
			params.put("verify", Md5Utils.md5(verify.toString()).toUpperCase());
			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

				JSONObject resultJson = resultObject.getJSONObject("result");
				if (resultJson != null) {
					supResult.setData(resultJson);
					return supResult;
				}

			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(resultObject.getString("msg"));
				return supResult;
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(resultObject.getString("msg"));
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
				return supResult;
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
				return supResult;
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 经济能力评级 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}
	}

	@Override
	public SupResult querySureScoreInfo(SureScoreInfoReqVo vo, SuplierQueryBean bean) {
		String result = null;
		SupResult supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/score/A11";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = vo.getName();
			String mobile = vo.getMobile();
			String cid = vo.getCid();
			String job = vo.getJob();

			params.put("account", appkey);
			params.put("cid", cid);
			params.put("job", job);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));
			StringBuilder verify = new StringBuilder();
			verify.append(params.getString("account")).append(params.getString("cid"))
					.append(params.getString("mobile")).append(params.getString("name")).append(params.getString("job"))
					.append(params.getString("reqid")).append(appsecret);
			params.put("verify", Md5Utils.md5(verify.toString()).toUpperCase());

			String reqUrl = UrlUtils.getUrl(params, url);

			supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

				JSONObject resultJson = resultObject.getJSONObject("result");
				if (resultJson != null) {
					supResult.setData(resultJson);
					return supResult;
				}

			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(resultObject.getString("msg"));
				return supResult;
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  确信分查询 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
				return supResult;
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 确信分 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
					, bean.getOrderNo(), url, result, e);
			if (supResult == null) {
				supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			}
			supResult.setState(ReqState.ERROR);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			supResult.setRemark("异常：" + e.getMessage());
			return supResult;
		}
	}

	@Override
	public SupResult<FinanceIcsResDTO> financeIcsA(FinanceIcsReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<FinanceIcsResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/finan/incomeCapabilityScore/a";
			String signKey = bean.getSignKey();
			String acc = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String mobile = dto.getMobile();
			String idCard = dto.getIdCard();

			params.put("account", acc);
			params.put("idCard", idCard);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));
			String verify = params.getString("account") + params.getString("idCard") +
					params.getString("name") + params.getString("mobile") +
					params.getString("reqid") + signKey;
			params.put("verify", Md5Utils.md5(verify).toUpperCase());

			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResDTO<FinanceIcsResDTO> response = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<FinanceIcsResDTO>>() {
			});
			String code = response.getCode();
			String msg = response.getMsg();
			FinanceIcsResDTO data = response.getResult();

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(data);
			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(data);
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-青龙分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-青龙分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 经济能力评级-青龙分 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<FinanceIcsResDTO> financeIcsB(FinanceIcsReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<FinanceIcsResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/finan/incomeCapabilityScore/b";
			String signKey = bean.getSignKey();
			String acc = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String mobile = dto.getMobile();
			String idCard = dto.getIdCard();

			params.put("account", acc);
			params.put("idCard", idCard);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));
			String verify = params.getString("account") + params.getString("idCard") +
					params.getString("name") + params.getString("mobile") +
					params.getString("reqid") + signKey;
			params.put("verify", Md5Utils.md5(verify).toUpperCase());

			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResDTO<FinanceIcsResDTO> response = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<FinanceIcsResDTO>>() {
			});
			String code = response.getCode();
			String msg = response.getMsg();
			FinanceIcsResDTO data = response.getResult();

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(data);
			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(data);
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-白虎分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-白虎分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 经济能力评级-白虎分 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<FinanceIcsResDTO> financeIcsE(FinanceIcsReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<FinanceIcsResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/finan/incomeCapabilityScore/e";
			String signKey = bean.getSignKey();
			String acc = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String mobile = dto.getMobile();
			String idCard = dto.getIdCard();

			params.put("account", acc);
			params.put("idCard", idCard);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));
			String verify = params.getString("account") + params.getString("idCard") +
					params.getString("name") + params.getString("mobile") +
					params.getString("reqid") + signKey;
			params.put("verify", Md5Utils.md5(verify).toUpperCase());

			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResDTO<FinanceIcsResDTO> response = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<FinanceIcsResDTO>>() {
			});
			String code = response.getCode();
			String msg = response.getMsg();
			FinanceIcsResDTO data = response.getResult();

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(data);
			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(data);
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-朱雀分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-朱雀分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 经济能力评级-朱雀分 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<FinanceIcsResDTO> financeIcsF(FinanceIcsReqDTO dto, SuplierQueryBean bean) {
		String result = null;
		SupResult<FinanceIcsResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;
		try {
			url = bean.getUrl() + "/yrzx/finan/incomeCapabilityScore/f";
			String signKey = bean.getSignKey();
			String acc = bean.getAcc();
			Integer timeOut = bean.getTimeOut();
			String name = dto.getName();
			String mobile = dto.getMobile();
			String idCard = dto.getIdCard();

			params.put("account", acc);
			params.put("idCard", idCard);
			params.put("name", name);
			params.put("mobile", mobile);
			params.put("reqid", String.valueOf(System.currentTimeMillis()));
			String verify = params.getString("account") + params.getString("idCard") +
					params.getString("name") + params.getString("mobile") +
					params.getString("reqid") + signKey;
			params.put("verify", Md5Utils.md5(verify).toUpperCase());

			String reqUrl = UrlUtils.getUrl(params, url);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.get(reqUrl, timeOut);
			supResult.setRespTime(LocalDateTime.now());
			supResult.setRespJson(result);
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}

			BaseResDTO<FinanceIcsResDTO> response = JsonUtil.fromJson(result, new TypeReference<BaseResDTO<FinanceIcsResDTO>>() {
			});
			String code = response.getCode();
			String msg = response.getMsg();
			FinanceIcsResDTO data = response.getResult();

			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				supResult.setData(data);
			} else if (EMPTY.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setState(ReqState.NOT_GET);
				supResult.setData(data);
			} else if (ERROR_CODE1.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
				supResult.setDefinedFailMsg(true);
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-玄武分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
				supResult.setState(ReqState.ERROR);
				errMonitorMsg(log, "  经济能力评级-玄武分 接口 发生异常 orderNo {} URL {} , 报文: {} "
						, bean.getOrderNo(), url, result);
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【北京银融致信科技供应商】 经济能力评级-玄武分 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
