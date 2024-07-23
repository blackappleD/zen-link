package com.mkc.config;

import com.mkc.common.core.domain.R;
import com.mkc.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerConfig {

	/**
	 * 内部服务异常处理
	 *
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public R<?> handleGlobalException(HttpServletResponse response, HttpServletRequest req, Exception e)
			throws Exception {

		log.error(" GlobalException  error ； URL {} , err {} " , req.getRequestURL(),e.getMessage(), e);

		return R.fail();
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(BindException.class)
	public R<?> handleBindException(BindException e) {
		log.error(e.getMessage(), e);
		String message = e.getAllErrors().get(0).getDefaultMessage();
		return R.fail(message);
	}

	/**
	 * 内部服务异常处理
	 *
	 * @param e
	 * @return
	 * @throws ServiceException
	 */
	@ResponseBody
	@ExceptionHandler(ServiceException.class)
	public R<?> handleServiceException(HttpServletResponse response, HttpServletRequest req, ServiceException e)
			throws Exception {

		log.error(" ServiceException  error ； URL  " + req.getRequestURL(), e);

		return R.fail(e.getMessage());
	}

}
