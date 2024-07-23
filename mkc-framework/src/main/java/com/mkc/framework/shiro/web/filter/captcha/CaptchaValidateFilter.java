package com.mkc.framework.shiro.web.filter.captcha;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.anji.captcha.model.common.CaptchaTypeEnum;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.mkc.common.constant.ShiroConstants;
import com.mkc.common.utils.StringUtils;

/**
 * 验证码过滤器
 * 
 * @author atd
 */
public class CaptchaValidateFilter extends AccessControlFilter {
	/**
	 * 是否开启验证码
	 */
	private boolean captchaEnabled = true;

	private CaptchaService captchaService;

	public void setCaptchaEnabled(boolean captchaEnabled) {
		this.captchaEnabled = captchaEnabled;
	}

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		request.setAttribute(ShiroConstants.CURRENT_ENABLED, captchaEnabled);
		return super.onPreHandle(request, response, mappedValue);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 验证码禁用 或不是表单提交 允许访问
		if (captchaEnabled == false || !"post".equals(httpServletRequest.getMethod().toLowerCase())) {
			return true;
		}
		return validateResponse(httpServletRequest);
	}

	public boolean validateResponse(HttpServletRequest request) {
		CaptchaVO captchaVO = new CaptchaVO();
		captchaVO.setCaptchaVerification(request.getParameter("__captchaVerification"));
		
		if (CaptchaTypeEnum.DEFAULT.getCodeValue().equals(captchaService.captchaType())) {
			String captchaType = request.getParameter("captchaType");
			captchaVO.setCaptchaType(StringUtils.isBlank(captchaType) ? CaptchaTypeEnum.CLICKWORD.getCodeValue() : captchaType);
		}

		if (StringUtils.isEmpty(captchaVO.getCaptchaVerification()) || !captchaService.verification(captchaVO).isSuccess()) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		request.setAttribute(ShiroConstants.CURRENT_CAPTCHA, ShiroConstants.CAPTCHA_ERROR);
		return true;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}
}
