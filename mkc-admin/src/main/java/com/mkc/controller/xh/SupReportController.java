package com.mkc.controller.xh;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.common.annotation.Log;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.SupReport;
import com.mkc.service.IMerInfoService;
import com.mkc.service.IProductService;
import com.mkc.service.ISupReportService;
import com.mkc.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 供应商核验统计Controller
 *
 * @author mkc
 * @date 2023-06-16
 */
@Controller
@RequestMapping("/xh/supReport")
@Slf4j
public class SupReportController extends BaseController {
	private String prefix = "xh/supReport";

	@Autowired
	private ISupReportService supReportService;

	@Autowired
	private ISupplierService supplierService;
	@Autowired
	private IMerInfoService merInfoService;
	@Autowired
	private IProductService productService;

	@Autowired
	private RedisCache redisCache;

	@RequiresPermissions("xh:supReport:view")
	@GetMapping()
	public String report(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("merInfos", merInfoService.selectMerInfoList(null));
		model.addAttribute("products", productService.selectProductList(null));

		return prefix + "/report";
	}

	@RequiresPermissions("xh:supReport:cost")
	@GetMapping("/cost")
	public String cost(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("products", productService.selectProductList(null));

		return prefix + "/cost";
	}

	@RequiresPermissions("xh:supReport:chartMea")
	@GetMapping("/chartMea")
	public String chartMea(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("products", productService.selectProductList(null));

		return prefix + "/chartMea";
	}

	/**
	 * 查询供应商核验统计列表
	 */
	@RequiresPermissions("xh:supReport:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SupReport supReport) {
		startPage();
		List<SupReport> list = queryAll(supReport);

		return getDataTable(list);
	}

	private static String camelToUnderscore(String camel) {
		// 使用正则表达式将小驼峰格式的字符串替换为下划线格式
		return camel.replaceAll("([A-Z])", "_$1").toLowerCase();
	}

	private List<SupReport> queryAll(SupReport supReport) {
		List<SupReport> list = null;

		if (StringUtils.isBlank(supReport.getStatClm())) {
			LambdaQueryWrapper<SupReport> queryWrapper = new LambdaQueryWrapper<>();
			if (StringUtils.isNotBlank(supReport.getSupCode())) {
				queryWrapper.in(SupReport::getSupCode, Arrays.asList(supReport.getSupCode().split(",")));
			}
			if (StringUtils.isNotBlank(supReport.getMerCode())) {
				queryWrapper.in(SupReport::getMerCode, Arrays.asList(supReport.getMerCode().split(",")));
			}

			queryWrapper.eq(StringUtils.isNotBlank(supReport.getProductCode()), SupReport::getProductCode, supReport.getProductCode());
			queryWrapper.eq(StringUtils.isNotBlank(supReport.getCgCode()), SupReport::getCgCode, supReport.getCgCode());
			queryWrapper.ge(supReport.getStartTime() != null, SupReport::getReqDate, supReport.getStartTime());
			queryWrapper.le(supReport.getEndTime() != null, SupReport::getReqDate, supReport.getEndTime());

			list = supReportService.list(queryWrapper);
		} else {
			supReport.setStatClm(camelToUnderscore(supReport.getStatClm()));
			list = supReportService.listSupReport(supReport);
		}

		return list;
	}

	/**
	 * 导出供应商日志统计列表
	 */
	@RequiresPermissions("xh:supReport:export")
	@Log(title = "供应商日志统计", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SupReport supReport) {
		startOrderBy();
		List<SupReport> list = queryAll(supReport);
		ExcelUtil<SupReport> util = new ExcelUtil<SupReport>(SupReport.class);
		return util.exportExcel(list, "供应商日志统计");
	}

	/**
	 * 导出供应商结算报表
	 */
	@RequiresPermissions("xh:supSettleReport:export")
	@Log(title = "供应商结算报表", businessType = BusinessType.EXPORT)
	@PostMapping("/exportCost")
	@ResponseBody
	public AjaxResult exportCost(SupReport supReport) {
		startOrderBy();
		List<SupReport> list = queryAll(supReport);
		list.forEach(sup -> sup.setFeeTimes(sup.getInPrice().doubleValue() > 0 ? sup.getStatusOkFit() + sup.getStatusOkUnfit() + sup.getStatusNo() + sup.getStatusErr() : 0));
		ExcelUtil<SupReport> util = new ExcelUtil<SupReport>(SupReport.class);
		util.includeColumn("reqDate", "supName", "productName", "inPrice", "feeTimes", "totalPrice");
		return util.exportExcel(list, "供供应商结算报表");
	}

	/**
	 * 手动更新供应商日志统计数据
	 */
	@RequiresPermissions("xh:supReport:stat")
	@Log(title = "手动更新供应商日志统计", businessType = BusinessType.OTHER)
	@PostMapping("/stat")
	@ResponseBody
	public AjaxResult statData(String startDate, String endDate, String supCode) {
		if (StringUtils.isBlank(startDate) || startDate.length() != 10 || !startDate.contains("-")
				|| StringUtils.isBlank(endDate) || endDate.length() != 10 || !endDate.contains("-")) {
			return error("日期格式不正确");
		}

		String key = RedisKey.SUP_REPORT_PREFIX;
		Boolean hasLook = redisCache.setIfAbsent(key, startDate + "|" + endDate, 10, TimeUnit.MINUTES);
		if (!hasLook) {
			return error("已有程序正在执行中请稍后再试");
		}

		StringBuffer buffer = new StringBuffer();
		String msg = "手动更新供应商日志统计数据失败，日期：";

		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		long daysBetween = ChronoUnit.DAYS.between(start, end);
		if (daysBetween > 30) {
			return error("日期跨度不能超过30天，当前跨度" + daysBetween + "天");
		}
		while (start.compareTo(end) <= 0) {
			try {
				boolean res = supReportService.statSupReqLogReport(start, supCode);
				if (!res) {
					String errorMsg = "Service返回未成功，" + msg + start;
					buffer.append(errorMsg + "\n");
					log.error("supReport statData " + errorMsg);

					DdMonitorMsgUtil.sendDdSysErrMsg(errorMsg);
				}
			} catch (Exception e) {
				String errorMsg = msg + start;
				buffer.append(errorMsg + "\n");
				log.error("supReport statData " + errorMsg, e);

				DdMonitorMsgUtil.sendDdSysErrMsg(errorMsg, e);
			} finally {
				start = start.plusDays(1);
			}
		}
		redisCache.deleteObject(key);
		if (buffer.length() > 0) {
			return error(buffer.toString());
		}
		// DdMonitorMsgUtil.sendDdSysErrMsg("手动更新供应商日志统计" + startDate + "到" + endDate + "成功");
		return success();
	}

}
