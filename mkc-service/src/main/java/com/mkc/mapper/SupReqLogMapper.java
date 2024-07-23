package com.mkc.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.bean.SupReqLogBean;
import com.mkc.domain.SupReport;
import com.mkc.domain.SupReqLog;

/**
 * 调用供应商日志Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */
@Mapper
public interface SupReqLogMapper   extends BaseMapper<SupReqLog>
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
     * 删除调用供应商日志
     * 
     * @param id 调用供应商日志主键
     * @return 结果
     */
    public int deleteSupReqLogById(Long id);

    /**
     * 批量删除调用供应商日志
     * 
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSupReqLogByIds(Integer[] Ids);
    
    /**
     * 统计供应商调用日志
     * 
     * @param startDate 开始日期 2023-06-17
     * @param endDate 结束日期 2023-06-18
     * @return 统计商户调用集合
     */
    public List<SupReport> selectSupReqLogReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("supCode") String supCode);
}
