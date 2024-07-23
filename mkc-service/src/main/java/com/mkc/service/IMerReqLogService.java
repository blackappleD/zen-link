package com.mkc.service;

import com.mkc.bean.MerReqLogBean;
import com.mkc.domain.MerReqLog;

import java.util.List;

/**
 * 商户调用日志Service接口
 * 
 * @author atd
 * @date 2023-04-24
 */
public interface IMerReqLogService 
{
    /**
     * 查询商户调用日志
     * 
     * @param id 商户调用日志主键
     * @return 商户调用日志
     */
    public MerReqLog selectMerReqLogById(Long id);

    /**
     * 查询商户调用日志
     *
     * @param orderNo 商户调用日志主键
     * @return 商户调用日志
     */
    public MerReqLog selectMerReqLogByOrderNo(String orderNo);

    /**
     * 查询商户调用日志列表
     * 
     * @param merReqLog 商户调用日志
     * @return 商户调用日志集合
     */
    public List<MerReqLog> selectMerReqLogList(MerReqLogBean merReqLog);



    /**
     * 修改商户调用日志
     * 
     * @param merReqLog 商户调用日志
     * @return 结果
     */
    public int updateMerReqLog(MerReqLog merReqLog);

    /**
     * 批量删除商户调用日志
     * 
     * @param Ids 需要删除的商户调用日志主键集合
     * @return 结果
     */
    public int deleteMerReqLogByIds(String Ids);

    /**
     * 删除商户调用日志信息
     * 
     * @param id 商户调用日志主键
     * @return 结果
     */
    public int deleteMerReqLogById(Long id);


    /**
     * 保存商户调用日志，及扣减商户本次调用产生的费用
     * @param merLog
     */
    public void saveMerLog(MerReqLog merLog);
}
