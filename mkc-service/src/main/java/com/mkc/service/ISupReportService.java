package com.mkc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mkc.domain.SupReport;

import java.time.LocalDate;
import java.util.List;

/**
 * 供应商调用日志统计Service接口
 *
 * @author mkc
 * @date 2023-06-16
 */
public interface ISupReportService extends IService<SupReport> {

	boolean statSupReqLogReport(LocalDate date, String supCode);

	List<SupReport> listSupReport(SupReport supReport);

}
