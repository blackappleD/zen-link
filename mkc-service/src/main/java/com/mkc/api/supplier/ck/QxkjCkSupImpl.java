package com.mkc.api.supplier.ck;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.ck.req.MobThreeReqDTO;
import com.mkc.api.dto.ck.res.MobileThreePlusResDTO;
import com.mkc.api.dto.ck.res.MobileThreeResDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.dto.bg.res.BankFourResDTO;
import com.mkc.api.dto.ck.req.BankReqDTO;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/9/13 14:19
 */
@Service("CK_QXKJ")
@Slf4j
public class QxkjCkSupImpl implements ICkSupService {

	private final static String SUCCESS = "200";

	/**
	 * 查无
	 */
	private final static String NOGET = "404";

	/**
	 * 认证不一致
	 */
	private final static String NOT = "403";

	private final static String ERROR = "500";


	@Override
	public SupResult ckBankTwo(BankReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/ck/twNameBank_YL";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String bankCard = vo.getBankCard();
			String certName = vo.getCertName();
//			String certNo = dto.getCertNo();
//			String mobile = dto.getMobile();

			params.put("bankCard", bankCard);
			params.put("certName", certName);
			params.put("merCode", appkey);
			params.put("merSeq", vo.getMerSeq());
			params.put("key", appsecret);
			String sign = Md5Utils.md5(appkey + certName + bankCard + signPwd);
			params.put("sign", sign);
			supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.post(url, params.toJSONString(), timeOut);
			supResult.setRespTime(LocalDateTime.now());
			log.info(CharSequenceUtil.format("【银行二要素核验返回体】{}", result));
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");

			//                0：成功（收费）
			//                405：查无（不收费）
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
				JSONObject resultJson = resultObject.getJSONObject("data");
				if (resultJson != null) {
					supResult.setData(resultJson);
					return supResult;
				}
			} else if (NOGET.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);

			} else if (NOT.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("不一致");
				supResult.setState(ReqState.NOT);

			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark("查询失败");
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【上海敬众科技股份有限公司供应商】 【银联】x二要素vip 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult ckBankThree(BankReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/ck/threeBank_YL";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String bankCard = vo.getBankCard();
			String certName = vo.getCertName();
			String certNo = vo.getCertNo();
//			String mobile = dto.getMobile();

			params.put("bankCard", bankCard);
			params.put("certName", certName);
			params.put("certNo", certNo);
			params.put("merCode", appkey);
			params.put("merSeq", vo.getMerSeq());
			params.put("key", appsecret);
			String sign = Md5Utils.md5(appkey + certName + certNo + bankCard + signPwd);
			params.put("sign", sign);
			supResult = new SupResult(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.post(url, params.toJSONString(), timeOut);
			log.info(CharSequenceUtil.format("【银行三要素核验返回体】{}", result));
			supResult.setRespJson(result);
			supResult.setRespTime(LocalDateTime.now());
			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			String code = resultObject.getString("code");
			String msg = resultObject.getString("msg");
			JSONObject resultJson = resultObject.getJSONObject("data");
			supResult.setData(resultJson);
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			} else if (NOGET.equals(code)) {
				supResult.setRemark("查无");
				supResult.setState(ReqState.NOT_GET);
			} else if (NOT.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("不一致");
				supResult.setState(ReqState.NOT);
				return supResult;
			} else if (ERROR.equals(code)) {
				supResult.setRemark(CharSequenceUtil.format("查询失败:{}", msg));
			} else {
				supResult.setRemark(CharSequenceUtil.format("异常:{}", msg));
			}
			return supResult;
		} catch (Throwable e) {
			errMonitorMsg(log, " 【上海敬众科技股份有限公司供应商】 【银联】x三要素vip 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<BankFourResDTO> ckBankFour(BankReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<BankFourResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/ck/fourBank_YL";
			String appsecret = bean.getSignKey();
			String appkey = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String bankCard = vo.getBankCard();
			String certName = vo.getCertName();
			String certNo = vo.getCertNo();
			String mobile = vo.getMobile();

			params.put("bankCard", bankCard);
			params.put("certName", certName);
			params.put("certNo", certNo);
			params.put("mobile", mobile);
			params.put("merCode", appkey);
			params.put("merSeq", vo.getMerSeq());
			params.put("key", appsecret);
			String sign = Md5Utils.md5(appkey + certName + certNo + mobile + bankCard + signPwd);
			params.put("sign", sign);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.post(url, params.toJSONString(), timeOut);

			log.info(CharSequenceUtil.format("【银行四要素核验返回体】{}", result));
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			supResult.setRespTime(LocalDateTime.now());
			String code = resultObject.getString("code");
			String msg = resultObject.getString("msg");
			JSONObject resultJson = resultObject.getJSONObject("data");
			supResult.setSupCode(code);
			if (resultJson != null) {
				supResult.setData(JSONUtil.toBean(resultJson.toJSONString(), BankFourResDTO.class));
			}
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

			} else if (NOT.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【上海敬众科技股份有限公司供应商】 【银联】x四要素vip 接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<MobileThreeResDTO> ckMobThree(MobThreeReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<MobileThreeResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/ck/mobileThree";
			String signKey = bean.getSignKey();
			String acc = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String certName = vo.getCertName();
			String certNo = vo.getCertNo();
			String mobile = vo.getMobile();

			params.put("certName", certName);
			params.put("certNo", certNo);
			params.put("mobile", mobile);
			params.put("merCode", acc);
			params.put("merSeq", vo.getMerSeq());
			params.put("key", signKey);
			String sign = Md5Utils.md5(acc + certName + certNo + mobile + signPwd);
			params.put("sign", sign);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.post(url, params.toJSONString(), timeOut);

			log.info(CharSequenceUtil.format("【手机三要素核验】{}", result));
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			supResult.setRespTime(LocalDateTime.now());
			String code = resultObject.getString("code");
			String msg = resultObject.getString("msg");
			JSONObject resultJson = resultObject.getJSONObject("data");
			supResult.setSupCode(code);
			if (resultJson != null) {
				supResult.setData(JSONUtil.toBean(resultJson.toJSONString(), MobileThreeResDTO.class));
			}
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

			} else if (NOT.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【上海敬众科技股份有限公司供应商】 手机三要素核验接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
	public SupResult<MobileThreePlusResDTO> ckMobThreePlus(MobThreeReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<MobileThreePlusResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/ck/mobileThreePlus";
			String signKey = bean.getSignKey();
			String acc = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String certName = vo.getCertName();
			String certNo = vo.getCertNo();
			String mobile = vo.getMobile();

			params.put("certName", certName);
			params.put("certNo", certNo);
			params.put("mobile", mobile);
			params.put("merCode", acc);
			params.put("merSeq", vo.getMerSeq());
			params.put("key", signKey);
			String sign = Md5Utils.md5(acc + certName + certNo + mobile + signPwd);
			params.put("sign", sign);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			result = HttpUtil.post(url, params.toJSONString(), timeOut);

			log.info(CharSequenceUtil.format("【手机三要素核验】{}", result));
			supResult.setRespJson(result);

			//判断是否有响应结果 无就是请求异常或超时
			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			JSONObject resultObject = JSON.parseObject(result);
			supResult.setRespTime(LocalDateTime.now());
			String code = resultObject.getString("code");
			String msg = resultObject.getString("msg");
			JSONObject resultJson = resultObject.getJSONObject("data");
			supResult.setSupCode(code);
			if (resultJson != null) {
				supResult.setData(JSONUtil.toBean(resultJson.toJSONString(), MobileThreePlusResDTO.class));
			}
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

			} else if (NOT.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【上海敬众科技股份有限公司供应商】 手机三要素核验接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
