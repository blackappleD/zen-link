package com.mkc.config;

/**
 * 全局异常处理
 *
 * @author ly
 * @since 2022/11/9
 */

import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
	public Result<?> handleGlobalException(HttpServletResponse response, HttpServletRequest req, Exception e) throws Exception {

		log.error(" GlobalException  Exception error:{}, URL  {}", e.getMessage(), req.getRequestURL(), e);

		return Result.fail();
	}

	/**
	 * 内部服务异常处理
	 *
	 * @param e
	 * @return
	 * @throws Throwable
	 */
	@ResponseBody
	@ExceptionHandler(Throwable.class)
	public Result<?> handleGlobalException(HttpServletResponse response, HttpServletRequest req, Throwable e) throws Exception {

		log.error(" GlobalException  Throwable  error {},  URL {} ", e.getMessage(), req.getRequestURL(), e);

		return Result.fail();
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
	public Result<?> handleServiceException(HttpServletResponse response, HttpServletRequest req, ServiceException e) throws Exception {

		log.error(" ServiceException  error: {} , URL {} ", e.getMessage(), req.getRequestURL(), e);

		return Result.fail();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<?> handleValidationException(MethodArgumentNotValidException ex) {
		BindingResult errors = ex.getBindingResult();
		List<ObjectError> allErrors = errors.getAllErrors();
		// 将错误信息转换为JSON格式返回给客户端
		StringBuilder errorMsg = new StringBuilder();
		for (ObjectError error : allErrors) {
			errorMsg.append(error.getDefaultMessage()).append(";");
		}
		errorMsg.deleteCharAt(errorMsg.length() - 1);

		return Result.fail(ApiReturnCode.ERR_009.getCode(), errorMsg.toString());
	}

	@ExceptionHandler(ApiServiceException.class)
	public Result<?> handleValidationException(ApiServiceException ex) {
		log.error(ex.getMessage(), ex);
		return Result.fail(ex.getCode(), ex.getMessage());
	}
}
