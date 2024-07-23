package com.mkc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.MerReport;

/**
 * 商户调用日志统计Mapper接口
 * 
 * @author mkc
 * @date 2023-06-16
 */
@Mapper
public interface MerReportMapper extends BaseMapper<MerReport> {
	
	public List<MerReport> selectMerReport(MerReport merReport);
	
}
