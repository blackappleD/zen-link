package com.mkc.process;

import com.mkc.domain.MerInfo;

/**
 * @author tqlei
 * @date 2023/7/17 15:07
 */

public interface IMailProcess {

    /**
     * 发送 客户账户信息
     * @param info
     */
    void sendMerKey(MerInfo info);


    /***
     * 商户产品未配置供应商
     * @param merName 商户名称
     * @param productName 产品名称
     */

    void sendProductNotSup(String merName,String productName);


}
