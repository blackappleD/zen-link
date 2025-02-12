package com.mkc.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.IdUtil;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.ContantCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.constant.enums.YysCode;
import com.mkc.api.common.constant.enums.YysProductCode;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.api.dto.BaseDTO;
import com.mkc.api.dto.common.MerReqLogDTO;
import com.mkc.bean.CkMerBean;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.enums.OnOffState;
import com.mkc.domain.MerInfo;
import com.mkc.domain.ProductSell;
import com.mkc.service.IMerInfoService;
import com.mkc.service.IPhoneService;
import com.mkc.service.IProductSellService;
import com.mkc.tool.IPUtils;
import com.mkc.tool.IdUtils;
import com.mkc.tool.RegTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author tqlei
 * @date 2023/5/14 21:22
 */

@Slf4j
@RestController
public class BaseController {

	@GetMapping("/health")
	public Result<String> test() {
		return Result.ok("服务启动成功");
	}


	@Autowired
	private IMerInfoService merchantService;

	public final static String TEST_MERCODE = "BhCpTest";
	@Autowired
	private IProductSellService productSellService;

	@Autowired
	private RedisCache redisCache;

	@Autowired
	private IPhoneService phoneService;

	// private static Long workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()) >> 16 & 31;

	public MerReqLogDTO ckMer(HttpServletRequest request, CkMerBean bean) {

		String merCode = bean.getMerCode();
		String plaintext = bean.getPlaintext();
		String merSign = bean.getMerSign();
		String productCode = bean.getProductCode();
		String key = bean.getKey();
		MerReqLogDTO merLog = new MerReqLogDTO();

		MerInfo merInfo = merchantService.selectMerInfoByCodeApi(merCode);
		if (merInfo == null || !OnOffState.STATE_ON.equals(merInfo.getStatus())) {
			log.error("无效的商户code  {}", merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_002);
		}
		String merKey = merInfo.getSignKey();
		String pwd = merInfo.getSignPwd();
		String signText = plaintext + pwd;
		String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
		if (!merKey.equals(key)) {
			log.error("令牌失败 merCode {} , key {} ", merCode, key);
			throw new ApiServiceException(ApiReturnCode.ERR_008);
		}

		//测试需要
		if (!TEST_MERCODE.equals(merCode) && !signMd5.equals(merSign) && !ContantCode.PRIVATE_SIGN.equals(merSign)) {
			log.error("验签失败 merCode {} , signMd5 {} ，merSign {} , merSeq  {}", merCode, signMd5, merSign, bean.getMerSeq());
			throw new ApiServiceException(ApiReturnCode.ERR_006);
		}


		String ipAddr = IPUtils.getIpAddress(request);

		if (StringUtils.isBlank(ipAddr) || (StringUtils.isNotBlank(merInfo.getIps()) && !ipAddr.matches(merInfo.getIps()))) {
			log.error("ip 无访问权限 merCode {} , ip {} ", merCode, ipAddr);
			throw new ApiServiceException(ApiReturnCode.ERR_005);
		}

		ProductSell productSell = productSellService.selectProductSellByMer(merCode, productCode);

		if (productSell == null) {
			log.error("该商户 产品接口未开通  merCode {} , productCode  {} ", merCode, productCode);
			throw new ApiServiceException(ApiReturnCode.ERR_004);
		}

		if (!merInfo.getBalanceFlag()) {
			log.error("该商户账户余额不足  merCode {} ", merCode);
			sendMerBalanceMonitorMsg(merCode, "该商户【{} , {}】 账户余额不足,下单失败，请联系商户充值！ ", merCode, merInfo.getMerName());
			throw new ApiServiceException(ApiReturnCode.ERR_003);
		} else {
			//以防队列消费比较慢，导致账户余额扣减不及时
			String merBalanceKey = RedisKey.MER_BALANCE_KEY + merCode;
			Number merBalance = redisCache.getCacheObject(merBalanceKey);
			//merBalance.compareTo(BigDecimal.ZERO)
			if (merBalance != null && productSell.getSellPrice().compareTo(new BigDecimal(merBalance.doubleValue())) > 0) {
				log.error("redis merBalance {} 该商户账户余额不足  merCode {} ", merBalance, merCode);
				sendMerBalanceMonitorMsg(merCode, "该商户【{} , {}】 账户余额不足 {} , 下单失败，请联系商户充值 redis", merCode, merInfo.getMerName(), merBalance);
				throw new ApiServiceException(ApiReturnCode.ERR_003);
			}
		}


		merLog.setMerSeq(bean.getMerSeq());
		merLog.setMerCode(merCode);
		merLog.setMerName(merInfo.getMerName());
		merLog.setCgCode(productSell.getCgCode());
		merLog.setProductCode(productCode);
		merLog.setProductName(productSell.getProductName());
		merLog.setRouteCon(productSell.getRouteCon());
		merLog.setReqLimit(productSell.getReqLimit());
		merLog.setIpaddr(ipAddr);
		merLog.setReqTime(LocalDateTime.now());
		merLog.setSellPrice(productSell.getSellPrice());

		String seqNo = null;
		try {
			seqNo = IdUtils.getNextId();
		} catch (Exception e) {
			log.error("IdUtils.getNextId is err", e);
			seqNo = Long.toString(IdUtil.getSnowflakeNextId());
		}

		//log.info("==== workerId  {} , seqNo  {}",workerId,seqNo);

		merLog.setOrderNo(seqNo);

		return merLog;

	}

	/**
	 * 发送商户余额不足预警 监控
	 *
	 * @param merCode
	 * @param messagePattern
	 * @param arguments
	 */
	private void sendMerBalanceMonitorMsg(String merCode, String messagePattern, Object... arguments) {

		String merNoBalanceWarnKey = RedisKey.MER_NO_BALANCE_WARN_KEY + merCode;
		//10分钟提示一次
		Integer remainSecondsOneDay = 60 * 10;
		//判断今天是否已经提醒过
		boolean flag = redisCache.setIfAbsent(merNoBalanceWarnKey, merCode, remainSecondsOneDay, TimeUnit.SECONDS);
		if (flag) {
			DdMonitorMsgUtil.sendDdBusinessMsg(messagePattern, arguments);
		}

	}


	public void ckCommonParams(BaseDTO params) {

		String merCode = params.getMerCode();
		String sign = params.getSign();
		String key = params.getKey();
		String merSeq = params.getMerSeq();

		if (StringUtils.isBlank(merCode)) {
			log.error("缺少参数 merCode {}", merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(key)) {
			log.error("缺少参数 merCode {}, key {}", merCode, key);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		if (StringUtils.isBlank(sign)) {
			log.error("缺少参数 merCode {} , sign {}", merCode, sign);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (StringUtils.isBlank(merSeq)) {
			log.error("缺少参数 merCode {} , merSeq {}", merCode, merSeq);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (merSeq.length() > 32) {
			log.error("无效的参数 merCode {} , merSeq {}", merCode, merSeq);
			throw new ApiServiceException(ApiReturnCode.ERR_009.getCode(), "无效的商户请求流水号");
		}
	}


	/**
	 * 校验 身份证二要素信息 不包含 md5
	 *
	 * @param merCode
	 * @param certName
	 * @param certNo
	 */
	public void ckTwoParam(String merCode, String certName, String certNo) {

		ckCertName(merCode, certName);
		ckCertNo(merCode, certNo);


	}

	/**
	 * 校验姓名信息
	 *
	 * @param merCode
	 * @param certName
	 */
	public boolean ckCertName(String merCode, String certName) {

		if (StringUtils.isBlank(certName)) {
			log.error("缺少参数 certName {} , merCode： {}", certName, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (!RegTool.checkCertName(certName)) {
			log.error("无效的参数 certName {} , merCode： {}", certName, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_009);
		}

		return true;
	}

	/**
	 * 校验身份证信息
	 *
	 * @param merCode
	 * @param certNo
	 */
	public boolean ckCertNo(String merCode, String certNo) {

		if (StringUtils.isBlank(certNo)) {
			log.error("缺少参数 certNo {} , merCode： {}", certNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}
		if (!RegTool.checkCertNo(certNo)) {
			log.error("无效的参数 certNo： {} , merCode： {}", certNo, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_009.getCode(), "无效的参数-身份证号");
		}
		return true;
	}

	/**
	 * 校验手机号信息
	 *
	 * @param merCode
	 * @param mobile
	 */
	public boolean ckMobile(String merCode, String mobile) {

		if (StringUtils.isBlank(mobile)) {
			log.error("缺少参数 mobile {} , merCode： {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}


		if (!RegTool.checkMobile(mobile)) {
			log.error("无效的参数 mobile {} , merCode： {}", mobile, merCode);
			throw new ApiServiceException(ApiReturnCode.ERR_009.getCode(), "无效的参数-手机号");
		}
		return true;
	}

	/**
	 * 校验银行卡号信息
	 *
	 * @param merCode
	 * @param merCode
	 */
	public void ckBankCard(String merCode, String bankCard) {

		if (StringUtils.isBlank(bankCard)) {
			log.error("缺少参数 merCode {} ,bankCard {}", merCode, bankCard);
			throw new ApiServiceException(ApiReturnCode.ERR_001);
		}

		if (!RegTool.checkBankCard(bankCard)) {
			log.error("无效的参数 merCode {} , bankCard {}", merCode, bankCard);
			throw new ApiServiceException(ApiReturnCode.ERR_009.getCode(), "无效的参数-银行卡号");
		}

	}


	/**
	 * 校验 身份证手机 三要素信息 不包含 md5
	 *
	 * @param merCode
	 * @param certName
	 * @param certNo
	 * @param mobile
	 */

	public void ckThreeParam(String merCode, String certName, String certNo, String mobile) {

		ckTwoParam(merCode, certName, certNo);
		ckMobile(merCode, mobile);

	}


	/**
	 * 供应商处理发生异常  报警处理
	 *
	 * @param messagePattern
	 * @param arguments
	 * @return
	 */
	public void errMonitorMsg(String messagePattern, Object... arguments) {

		try {
			FormattingTuple formattingTuple = MessageFormatter.arrayFormat(messagePattern, arguments);
			String msg = formattingTuple.getMessage();

			Throwable throwable = formattingTuple.getThrowable();
			String errorMsg = "";
			if (throwable != null) {
				errorMsg = ExceptionUtil.stacktraceToString(throwable);
			}

			log.error("==api msg:  {} ,  errorMsg: {} ", msg, errorMsg);

			DdMonitorMsgUtil.sendDdSysErrMsg(msg + "\n" + errorMsg);

		} catch (Throwable e) {
			log.error("api errMonitorMsg is err ; " + messagePattern, arguments, e);
		}


	}

	/**
	 * 根据手机号 和 请求的接口 产品code  获取对应手机号运营商Code
	 *
	 * @param mobile         手机号
	 * @param reqProductCode 请求产品code
	 * @return
	 */
	public YysProductCode getYysProductCode(String mobile, ProductCodeEum reqProductCode) {
		try {
			YysCode yysCode = phoneService.queryOperatorByPhone(mobile);

			YysProductCode yysProductCode = YysProductCode.findByCode(yysCode.getCode(), reqProductCode.getCode());
			if (yysProductCode == null) {
				throw new ApiServiceException(ApiReturnCode.ERR_009.getCode(), "无效的参数");
			}
			return yysProductCode;
		} catch (Exception e) {
			errMonitorMsg("获取产品手机号对应运营商发生异常 mobile {} , reqProductCode {}", mobile, reqProductCode.getCode());
		}
		//默认返回移动
		return YysProductCode.findByCode(YysCode.CM.getCode(), reqProductCode.getCode());

	}

}
