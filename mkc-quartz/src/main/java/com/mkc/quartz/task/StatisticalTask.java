package com.mkc.quartz.task;

import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.service.IMerReportService;
import com.mkc.service.ISupReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 统计图表数据任务
 */
@Slf4j
@Component("statTask")
public class StatisticalTask {
	
	@Autowired
	private IMerReportService merReportService;
	@Autowired
	private ISupReportService supReportService;

	public void merReportNow() {
		String remark = "调用 merReportNow 商户日志统计(当日)";
		log.info("{}开始", remark);
		boolean result = false;
		try {
			LocalDate now = LocalDate.now();
			result = merReportService.statMerReqLogReport(now, null);
		} catch (Exception e) {
			log.error("{}出错", remark, e);
		}
		log.info("{}结束 --> {}", remark, result);
	}
	
	public void merReportLast() {
		String remark = "调用 merReportLast 商户日志统计(昨日)";
		log.info("{}开始", remark);
		boolean result = false;
		LocalDate yesterday=null;
		try {
			LocalDate now = LocalDate.now();
			yesterday = now.minusDays(1);
			result = merReportService.statMerReqLogReport(yesterday, null);
		} catch (Exception e) {
			log.error("{}出错", remark, e);
			//发生异常信息给钉钉通知
			DdMonitorMsgUtil.sendDdSysErrMsg(remark+" 时间 {} ",yesterday ,e);

		}
		log.info("{}结束 --> {}", remark, result);
	}
	
	
	public void supReportNow() {
		String remark = "调用 supReportNow 供应商日志统计(当日)";
		log.info("{}开始", remark);
		boolean result = false;
		try {
			LocalDate now = LocalDate.now();
			result = supReportService.statSupReqLogReport(now, null);
		} catch (Exception e) {
			log.error("{}出错", remark, e);
		}
		log.info("{}结束 --> {}", remark, result);
	}
	
	public void supReportLast() {
		String remark = "调用 supReportLast 供应商日志统计(昨日)";
		log.info("{}开始", remark);
		boolean result = false;
		LocalDate yesterday=null;
		try {
			LocalDate now = LocalDate.now();
			yesterday = now.minusDays(1);
			result = supReportService.statSupReqLogReport(yesterday, null);
		} catch (Exception e) {
			log.error("{}出错", remark, e);
			//发生异常信息给钉钉通知
			DdMonitorMsgUtil.sendDdSysErrMsg(remark+" 时间 {} ",yesterday ,e);
		}
		log.info("{}结束 --> {}", remark, result);
	}
	
}
