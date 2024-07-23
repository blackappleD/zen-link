package com.mkc.config;

/**
 * @author tqlei
 * @date 2023/4/25 15:55
 */


/**
 * 全局异常处理
 *
 * @author ly
 * @since 2022/11/9
 */

import com.mkc.api.common.constant.bean.Result;
import com.mkc.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
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
    public Result<?> handleGlobalException(HttpServletResponse response, HttpServletRequest req, Exception e) throws Exception {

        log.error(" GlobalException  Exception error , URL  "+req.getRequestURL(), e);

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

        log.error(" GlobalException  Throwable  error ,  URL  "+req.getRequestURL(), e);

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

        log.error(" ServiceException  error , URL  "+req.getRequestURL(), e);

        return Result.fail();
    }

}
