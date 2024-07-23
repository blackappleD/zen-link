package com.mkc.service;

import java.util.List;

import com.mkc.bean.SupReqLogBean;
import com.mkc.domain.SupReqLog;

/**
 * 调用供应商日志Service接口
 * 
 * @author atd
 * @date 2023-04-24
 */
public interface ISupReqLogService 
{
    /**
     * 查询调用供应商日志
     * 
     * @param id 调用供应商日志主键
     * @return 调用供应商日志
     */
    public SupReqLog selectSupReqLogById(Long id);

    /**
     * 查询调用供应商日志列表
     * 
     * @param supReqLog 调用供应商日志
     * @return 调用供应商日志集合
     */
    public List<SupReqLog> selectSupReqLogList(SupReqLogBean supReqLog);

    /**
     * 新增调用供应商日志
     * 
     * @param supReqLog 调用供应商日志
     * @return 结果
     */
    public int insertSupReqLog(SupReqLog supReqLog);

    /**
     * 修改调用供应商日志
     * 
     * @param supReqLog 调用供应商日志
     * @return 结果
     */
    public int updateSupReqLog(SupReqLog supReqLog);

    /**
     * 批量删除调用供应商日志
     * 
     * @param Ids 需要删除的调用供应商日志主键集合
     * @return 结果
     */
    public int deleteSupReqLogByIds(String Ids);

    /**
     * 删除调用供应商日志信息
     * 
     * @param id 调用供应商日志主键
     * @return 结果
     */
    public int deleteSupReqLogById(Long id);
}
