package com.mkc.api.supplier.ck;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.dto.ck.req.MobThreeReqDTO;
import com.mkc.api.dto.ck.res.JzMobileThreePlusResDTO;
import com.mkc.api.dto.ck.res.JzMobileThreeResDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.enums.FreeStatus;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.StringUtils;
import com.mkc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

/**
 * 敬众
 *
 * @AUTHOR XIEWEI
 * @Date 2024/9/13 14:19
 */
@Service("CK_JZKJ")
@Slf4j
public class JzCkSupImpl implements ICkSupService {

	private final static String SUCCESS = "200";

	/**
	 * 查无
	 */
	private final static String NOT_GET = "404";


	@Override
	public SupResult<JzMobileThreeResDTO> ckMobThree(MobThreeReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<JzMobileThreeResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/ws/verification/mobile.asmx/mobilecheck";
			String signKey = bean.getSignKey();
			String acc = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String certName = vo.getCertName();
			String certNo = vo.getCertNo();
			String mobile = vo.getMobile();

			params.put("passname", certName);
			params.put("pid", certNo);
			params.put("mobile", mobile);
			params.put("Hashcode", acc);
			String sign = Md5Utils.md5(acc + mobile + certName + certNo + signPwd);
			params.put("sign", sign);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
//			String xmlString = HttpUtil.post(url, params.toJSONString(), timeOut);
			String xmlString = "<JZD_MobileCheckData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
					"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +  // 修正这里的空格
					"xmlns=\"https://api.xiaoheer.com/\">" +
					"<ErrorRes>" +
					"<Err_code>200</Err_code>" +
					"<Err_content>手机三要素一致</Err_content>" +
					"</ErrorRes></JZD_MobileCheckData>";

			log.info(CharSequenceUtil.format("【敬众 手机三要素核验】{}", xmlString));
//			supResult.setRespJson(result);
			String code = "500";
			String msg = "";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
			NodeList errorResList = document.getElementsByTagName("ErrorRes");
			JzMobileThreeResDTO res = new JzMobileThreeResDTO();

			if (errorResList.getLength() > 0) {
				Element errorRes = (Element) errorResList.item(0);
				String errCode = errorRes.getElementsByTagName("Err_code").item(0).getTextContent();
				String errContent = errorRes.getElementsByTagName("Err_content").item(0).getTextContent();
				code = errCode;
				msg = errContent;
				res.setErrorCode(errCode);
				res.setErrorContent(errContent);
				result = JsonUtil.toJson(res);
			}

			if (StringUtils.isBlank(result)) {
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			supResult.setRespTime(LocalDateTime.now());
			supResult.setSupCode(code);
			supResult.setData(res);
			supResult.setRespJson(JsonUtil.toJson(res));
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);

			} else if (NOT_GET.equals(code)) {
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
	public SupResult<JzMobileThreePlusResDTO> ckMobThreePlus(MobThreeReqDTO vo, SuplierQueryBean bean) {
		String result = null;
		SupResult<JzMobileThreePlusResDTO> supResult = null;
		JSONObject params = new JSONObject();
		String url = null;

		try {
			url = bean.getUrl() + "/ws/verification/MobileV2.asmx/MobileAll";
			String acc = bean.getAcc();
			String signPwd = bean.getSignPwd();
			Integer timeOut = bean.getTimeOut();
			String certName = vo.getCertName();
			String certNo = vo.getCertNo();
			String mobile = vo.getMobile();

			params.put("hashcode", acc);
			params.put("passname", certName);
			params.put("pid", certNo);
			params.put("mobile", mobile);
			String sign = Md5Utils.md5(acc + mobile + certName + certNo + signPwd);
			params.put("sign", sign);
			supResult = new SupResult<>(params.toJSONString(), LocalDateTime.now());
			String xmlString = HttpUtil.post(url, params.toJSONString(), timeOut);

			log.info(CharSequenceUtil.format("【敬众 手机三要素核验详版】{}", xmlString));

			String code = "500";
			String msg = "";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
			NodeList errorResList = document.getElementsByTagName("ErrorRes");
			JzMobileThreePlusResDTO res = new JzMobileThreePlusResDTO();

			if (errorResList.getLength() > 0) {
				Element errorRes = (Element) errorResList.item(0);
				String errCode = errorRes.getElementsByTagName("Err_code").item(0).getTextContent();
				String errContent = errorRes.getElementsByTagName("Err_content").item(0).getTextContent();
				String identifyResult = errorRes.getElementsByTagName("identify_result").item(0).getTextContent();
				String isp = errorRes.getElementsByTagName("isp").item(0).getTextContent();
				code = errCode;
				msg = errContent;
				res.setErrorCode(errCode);
				res.setErrorContent(errContent);
				res.setIdentifyResult(identifyResult);
				res.setIsp(isp);
				result = JsonUtil.toJson(res);
			}

			if (StringUtils.isBlank(result)) {
				supResult.setRespJson(result);
				supResult.setRemark("供应商没有响应结果");
				supResult.setState(ReqState.ERROR);
				return supResult;
			}
			supResult.setRespTime(LocalDateTime.now());
			supResult.setSupCode(code);
			supResult.setData(res);
			supResult.setRespJson(JsonUtil.toJson(res));
			if (SUCCESS.equals(code)) {
				supResult.setFree(FreeStatus.YES);
				supResult.setRemark("查询成功");
				supResult.setState(ReqState.SUCCESS);
			} else if (NOT_GET.equals(code)) {
				supResult.setFree(FreeStatus.NO);
				supResult.setState(ReqState.NOT_GET);
			} else {
				supResult.setFree(FreeStatus.NO);
				supResult.setRemark(msg);
			}
			return supResult;

		} catch (Throwable e) {
			errMonitorMsg(log, " 【上海敬众科技股份有限公司供应商】 手机三要素核验详版接口 发生异常 orderNo {} URL {} , 报文: {} , err {}"
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
