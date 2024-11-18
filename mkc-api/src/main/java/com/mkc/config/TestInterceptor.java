package com.mkc.config;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.api.common.exception.ApiServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * openapi拦截器
 *
 * @author chw
 * @date
 */
@Slf4j
@Component
public class TestInterceptor implements HandlerInterceptor {


    private final static String KEY = "fsdgdfe647234bvdfgdfy54767";


    @Override
    @SneakyThrows
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object a) {
        if (!Objects.equals(request.getParameter("key"), KEY)) {
            throw new SecurityException("无效的身份访问");
        }
        return true;
    }

}
