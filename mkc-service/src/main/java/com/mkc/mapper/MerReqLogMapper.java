package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.bean.MerReqLogBean;
import com.mkc.domain.MerReport;
import com.mkc.domain.MerReqLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 商户调用日志Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */

@Mapper
public interface MerReqLogMapper extends BaseMapper<MerReqLog>
{
    /**
     * 查询商户调用日志
     * 
     * @param id 商户调用日志主键
     * @return 商户调用日志
     */
    public MerReqLog selectMerReqLogById(Long id);
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
     * 删除商户调用日志
     * 
     * @param id 商户调用日志主键
     * @return 结果
     */
    public int deleteMerReqLogById(Long id);

    /**
     * 批量删除商户调用日志
     * 
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMerReqLogByIds(Integer[] Ids);
    
    
    
    /**
     * 统计商户调用日志
     * 
     * @param startDate 开始日期 2023-06-17
     * @param endDate 结束日期 2023-06-18
     * @return 统计商户调用集合
     */
    public List<MerReport> selectMerReqLogReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("merCode") String merCode);
}
