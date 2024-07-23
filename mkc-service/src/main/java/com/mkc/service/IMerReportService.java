package com.mkc.service;

import java.time.LocalDate;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mkc.domain.MerReport;

/**
 * 商户调用日志统计Service接口
 * 
 * @author mkc
 * @date 2023-06-16
 */
public interface IMerReportService extends IService<MerReport> {

	public boolean statMerReqLogReport(LocalDate date, String merCode);
	
	public List<MerReport> listMerReport(MerReport merReport);
	
}
