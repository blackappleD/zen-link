package com.mkc.controller;

import com.mkc.api.common.constant.bean.Result;
import com.mkc.domain.MerInfo;
import com.mkc.service.IMerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tqlei
 * @date 2023/6/30 14:53
 */

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


        @Autowired
        private IMerInfoService merchantService;

        private final static String KEY="fsdgdfe647234bvdfgdfy54767";
        /**
         * 测试服务状态
         *
         * @return
         */
        @GetMapping("/state")
        public Result test(HttpServletRequest request, String key) {
        if(!KEY.equals(key)){
            return Result.fail("无效的身份访问");
        }
        try {
            MerInfo merInfo = merchantService.selectMerInfoByCode(BaseController.TEST_MERCODE);
            return  Result.ok("服务启动成功");

        }catch (Exception e){
            return  Result.fail("服务还未启动成功");
        }


    }


}
