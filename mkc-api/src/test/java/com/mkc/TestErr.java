package com.mkc;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tqlei
 * @date 2023/6/28 15:28
 */

@Slf4j
public class TestErr {

    public static void main(String[] args) {
        int i=1;
        try {
            i= 1/0;
            //HttpUtil.get("www",4000);
        }catch (Exception e){
           log.error("=========== {} err {} ",i,e);
        }
    }
}
