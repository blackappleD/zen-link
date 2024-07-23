package com.mkc.service;

import java.time.LocalDate;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mkc.domain.SupReport;

/**
 * 供应商调用日志统计Service接口
 * 
 * @author mkc
 * @date 2023-06-16
 */
public interface ISupReportService extends IService<SupReport> {

	public boolean statSupReqLogReport(LocalDate date, String supCode);
	
	public List<SupReport> listSupReport(SupReport supReport);
	
}
