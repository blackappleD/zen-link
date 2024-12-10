package com.mkc.controller.xh;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.bean.MerProfitReportBean;
import com.mkc.bean.MerSettleReportBean;
import com.mkc.common.annotation.Log;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.domain.entity.SysUser;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.ShiroUtils;
import com.mkc.common.utils.poi.ExcelMultipleSheetsUtil;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.FxReqRecord;
import com.mkc.domain.MerInfo;
import com.mkc.domain.MerReport;
import com.mkc.domain.MerReportExcel;
import com.mkc.service.IMerInfoService;
import com.mkc.service.IMerReportService;
import com.mkc.service.IProductService;
import com.mkc.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商户核验统计Controller
 *
 * @author mkc
 * @date 2023-06-16
 */
@Controller
@RequestMapping("/xh/merReport")
@Slf4j
public class MerReportController extends BaseController {
	private String prefix = "xh/merReport";

	@Autowired
	private IMerReportService merReportService;

	@Autowired
    private ISupplierService supplierService;
	@Autowired
    private IMerInfoService merInfoService;
	@Autowired
    private IProductService productService;

	@Autowired
	private RedisCache redisCache;

	@RequiresPermissions("xh:merReport:view")
	@GetMapping
	public String report(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("merInfos", merInfoService.selectMerInfoList(null));
		model.addAttribute("products", productService.selectProductList(null));

		return prefix + "/report";
	}

	@RequiresPermissions("xh:merReport:viewSell")
	@GetMapping("/sell")
	public String reportSell(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("products", productService.selectProductList(null));

		MerInfo merInfo = new MerInfo().setSellPerson(String.valueOf(ShiroUtils.getSysUser().getUserId()));
		model.addAttribute("merInfos", merInfoService.selectMerInfoList(merInfo));

		return prefix + "/reportSell";
	}

	@RequiresPermissions("xh:merReport:cost")
	@GetMapping("/cost")
	public String cost(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("merInfos", merInfoService.selectMerInfoList(null));
		model.addAttribute("products", productService.selectProductList(null));

		return prefix + "/cost";
	}

	@RequiresPermissions("xh:merReport:profit")
	@GetMapping("/profit")
	public String profit(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("merInfos", merInfoService.selectMerInfoList(null));
		model.addAttribute("products", productService.selectProductList(null));

		return prefix + "/profit";
	}

	@RequiresPermissions("xh:merReport:chartMea")
	@GetMapping("/chartMea")
	public String chartMea(Model model) {
		model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
		model.addAttribute("sups", supplierService.selectSupplierList(null));
		model.addAttribute("merInfos", merInfoService.selectMerInfoList(null));
		model.addAttribute("products", productService.selectProductList(null));

		return prefix + "/chartMea";
	}

	/**
	 * 查询商户日志统计列表
	 */
	@RequiresPermissions("xh:merReport:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MerReport merReport) {
		startPage();
		List<MerReport> list = queryAll(merReport);

		return getDataTable(list);
	}

    /**
     * 导出FX不动产对账单V1
     */
    @RequiresPermissions("xh:merReport:list")
    @Log(title = "商户调用日志", businessType = BusinessType.EXPORT)
    @ResponseBody
    @PostMapping("/fxHouseReportV1")
    public AjaxResult fxHouseReportV1(MerReport merReport) {
    	List<FxReqRecord> fxReqRecords = merReportService.listFxHouseReport(merReport);
        List<MerReportExcel> merReports = merReportService.listReport(fxReqRecords, ProductCodeEum.BG_HOUSE_RESULT_INFO.getName());
        List<MerReportExcel> merDateReports = merReportService.listDateReport(fxReqRecords, ProductCodeEum.BG_HOUSE_RESULT_INFO.getName());
        HashMap<String, Object> map = new HashMap<>();
        map.put("不动产总账", merReports);
        map.put("不动产对账单", merDateReports);
        map.put("不动产详情", fxReqRecords);
        return ExcelMultipleSheetsUtil.excelMultipleSheets(map, "不动产账单" + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
    }

    /**
     * 导出FX不动产对账单V2
     */
    @RequiresPermissions("xh:merReport:list")
    @Log(title = "商户调用日志", businessType = BusinessType.EXPORT)
    @ResponseBody
    @PostMapping("/fxHouseReportV2")
    public AjaxResult fxHouseReportV2(MerReport merReport) {
        merReport.setProductCode(ProductCodeEum.BG_HOUSE_RESULT_INFO.getCode());
        List<FxReqRecord> fxReqRecords = merReportService.listFxHouseReportV2(merReport);
        List<MerReportExcel> merReports = merReportService.listReport(fxReqRecords, ProductCodeEum.BG_HOUSE_RESULT_INFO.getName());
        List<MerReportExcel> merDateReports = merReportService.listDateReport(fxReqRecords, ProductCodeEum.BG_HOUSE_RESULT_INFO.getName());
        HashMap<String, Object> map = new HashMap<>();
        map.put("不动产对账单", merReports);
        map.put("不动产明细", merDateReports);
        map.put("不动产详情", fxReqRecords);
        return ExcelMultipleSheetsUtil.excelMultipleSheets(map, "不动产账单" + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
    }

	/**
	 * 查询商户日志统计列表
	 */
	@RequiresPermissions("xh:merReport:list")
	@Log(title = "商户调用日志", businessType = BusinessType.EXPORT)
	@ResponseBody
	@PostMapping("/fxEduReport")
	public AjaxResult fxEduReport(MerReport merReport, HttpServletResponse response) {
//		ExcelUtil util = new ExcelUtil(FxReqRecord.class, MerReport.class);
		List<FxReqRecord> fxReqRecords = merReportService.listFxEduReport(merReport);
		List<MerReportExcel> merReports = merReportService.listReport(fxReqRecords, ProductCodeEum.BG_HIGH_SCHOOL_EDUCATION_INFO.getName());
		List<MerReportExcel> merDateReports = merReportService.listDateReport(fxReqRecords, ProductCodeEum.BG_HIGH_SCHOOL_EDUCATION_INFO.getName());
		HashMap<String, Object> map = new HashMap<>();
		map.put("学历总账", merReports);
		map.put("学历对账单", merDateReports);
		map.put("学历详情", fxReqRecords);
//		return util.exportExcel(fxReqRecords, "", merReports, "", "");
		return ExcelMultipleSheetsUtil.excelMultipleSheets(map, "学历账单" + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
//		return util.exportExcel(map, "法信学历");
//		util.exportExcel(response,fxReqRecords, "法信不动产");
//		return util.exportExcel(fxReqRecords, "法信学历");
	}

	/**
	 * 查询商户日志统计列表，仅销售人员相关
	 */
	@RequiresPermissions("xh:merReport:listSell")
	@PostMapping("/listSell")
	@ResponseBody
	public TableDataInfo listSell(MerReport merReport) {
		SysUser user = ShiroUtils.getSysUser();
		List<MerInfo> merInfos = merInfoService.selectMerInfoList(new MerInfo().setSellPerson(String.valueOf(user.getUserId())));

		List<String> merCodes = merInfos.stream().map(MerInfo::getMerCode).collect(Collectors.toList());
		String merCode = merCodes.stream().collect(Collectors.joining(","));
		if(!StringUtils.isBlank(merReport.getMerCode())) {
			merCode = Arrays.stream(merReport.getMerCode().split(",")).filter(mc -> merCodes.contains(mc)).collect(Collectors.joining(","));
		}

		if(StringUtils.isBlank(merCode)) {
			return getDataTable(Collections.emptyList());
		}

		merReport.setMerCode(merCode);

		startPage();
		List<MerReport> list = queryAll(merReport);

		return getDataTable(list);
	}

	private static String camelToUnderscore(String camel) {
        // 使用正则表达式将小驼峰格式的字符串替换为下划线格式
		return camel.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

	private List<MerReport> queryAll(MerReport merReport) {
		List<MerReport> list = null;

		if(StringUtils.isBlank(merReport.getStatClm())) {
			LambdaQueryWrapper<MerReport> queryWrapper = new LambdaQueryWrapper<>();
			if(StringUtils.isNotBlank(merReport.getMerCode())) {
				queryWrapper.in(MerReport::getMerCode, Arrays.asList(merReport.getMerCode().split(",")));
			}
			if(StringUtils.isNotBlank(merReport.getSupCode())) {
				queryWrapper.in(MerReport::getSupCode, Arrays.asList(merReport.getSupCode().split(",")));
			}

			queryWrapper.eq(StringUtils.isNotBlank(merReport.getProductCode()), MerReport::getProductCode, merReport.getProductCode());
			queryWrapper.eq(StringUtils.isNotBlank(merReport.getCgCode()), MerReport::getCgCode, merReport.getCgCode());
			queryWrapper.ge(merReport.getStartTime() != null, MerReport::getReqDate, merReport.getStartTime());
			queryWrapper.le(merReport.getEndTime() != null, MerReport::getReqDate, merReport.getEndTime());

			list = merReportService.list(queryWrapper);
		} else {
			merReport.setStatClm(camelToUnderscore(merReport.getStatClm()));
			list = merReportService.listMerReport(merReport);
		}

		return list;
	}

	/**
	 * 导出商户日志统计列表
	 */
	@RequiresPermissions("xh:merReport:export")
	@Log(title = "商户日志统计", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(MerReport merReport) {
		startOrderBy();
		List<MerReport> list = queryAll(merReport);
		ExcelUtil<MerReport> util = new ExcelUtil<MerReport>(MerReport.class);
		return util.exportExcel(list, "商户核日志计数据");
	}

	/**
	 * 导出商户日志统计列表，销售岗
	 */
	@RequiresPermissions("xh:merReport:exportSell")
	@Log(title = "商户日志统计-销售岗", businessType = BusinessType.EXPORT)
	@PostMapping("/exportSell")
	@ResponseBody
	public AjaxResult exportSell(MerReport merReport) {
		List<MerReport> list = Collections.emptyList();
		SysUser user = ShiroUtils.getSysUser();
		List<MerInfo> merInfos = merInfoService.selectMerInfoList(new MerInfo().setSellPerson(String.valueOf(user.getUserId())));

		List<String> merCodes = merInfos.stream().map(MerInfo::getMerCode).collect(Collectors.toList());
		String merCode = merCodes.stream().collect(Collectors.joining(","));
		if(!StringUtils.isBlank(merReport.getMerCode())) {
			merCode = Arrays.stream(merReport.getMerCode().split(",")).filter(mc -> merCodes.contains(mc)).collect(Collectors.joining(","));
		}

		if(StringUtils.isNotBlank(merCode)) {
			merReport.setMerCode(merCode);

			startOrderBy();
			list = queryAll(merReport);
		}

		ExcelUtil<MerReport> util = new ExcelUtil<MerReport>(MerReport.class);
		return util.exportExcel(list, "商户核日志计数据-销售岗");
	}

	/**
	 * 商户结算报表导出
	 */
	@RequiresPermissions("xh:merSettleReport:export")
	@Log(title = "商户结算报表导出", businessType = BusinessType.EXPORT)
	@PostMapping("/merSettleExport")
	@ResponseBody
	public AjaxResult merSettleExport(MerReport merReport) {
		startOrderBy();
		List<MerReport> list = queryAll(merReport);
		List<MerSettleReportBean> settleList=new ArrayList<>();
		list.stream().forEach((merReport1 -> {
			MerSettleReportBean bean=new MerSettleReportBean();
			BeanUtils.copyProperties(merReport1,bean);
			settleList.add(bean);
		}));
		ExcelUtil<MerSettleReportBean> util = new ExcelUtil<>(MerSettleReportBean.class);
		return util.exportExcel(settleList, "商户结算报表");
	}
	/**
	 * 商户结算报表导出
	 */
	@RequiresPermissions("xh:merProfit:export")
	@Log(title = "商户利润报表导出", businessType = BusinessType.EXPORT)
	@PostMapping("/merProfitExport")
	@ResponseBody
	public AjaxResult merProfitExport(MerReport merReport) {
		startOrderBy();
		List<MerReport> list = queryAll(merReport);
		List<MerProfitReportBean> settleList=new ArrayList<>();
		list.stream().forEach((merReport1 -> {
			MerProfitReportBean bean=new MerProfitReportBean();
			BeanUtils.copyProperties(merReport1,bean);
			bean.setFeeTimes(bean.getStatusOkFit()+bean.getStatusErr()+bean.getStatusOkUnfit()+bean.getStatusNo());
			settleList.add(bean);
		}));
		ExcelUtil<MerProfitReportBean> util = new ExcelUtil<>(MerProfitReportBean.class);
		return util.exportExcel(settleList, "商户利润报表");
	}


	/**
	 * 更新商户日志统计数据
	 */
	@RequiresPermissions("xh:merReport:stat")
	@Log(title = "更新商户日志统计数据", businessType = BusinessType.OTHER)
	@PostMapping("/stat")
	@ResponseBody
	public AjaxResult statData(String startDate, String endDate, String merCode) {
		if(StringUtils.isBlank(startDate) || startDate.length() != 10 || !startDate.contains("-")
				|| StringUtils.isBlank(endDate) || endDate.length() != 10 || !endDate.contains("-")) {
			return error("日期格式不正确");
		}

		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		long daysBetween = ChronoUnit.DAYS.between(start, end);
		if(daysBetween > 30) {
			return error("日期跨度不能超过30天，当前跨度" + daysBetween + "天");
		}

		String key = RedisKey.MER_REPORT_PREFIX;
		Boolean hasLook = redisCache.setIfAbsent(key, startDate + "|" + endDate, 10, TimeUnit.MINUTES);
		if(!hasLook) {
			return error("已有程序正在执行中请稍后再试");
		}

		StringBuffer buffer = new StringBuffer();
		String msg = "手动更新商户日志统计数据失败，日期：";
		while(start.compareTo(end) <= 0) {
			try {
				boolean res = merReportService.statMerReqLogReport(start, merCode);
				if(!res) {
					throw new RuntimeException("Service返回未成功");
				}
			} catch (Exception e) {
				String errorMsg = msg + start.toString() + "到" + end.toString();
				buffer.append(errorMsg + "\n");
				log.error("merReport statData " + errorMsg, e);

				DdMonitorMsgUtil.sendDdSysErrMsg(errorMsg, e);
			} finally {
				start = start.plusDays(1);
			}
		}
		redisCache.deleteObject(key);

		if(buffer.length() > 0) {
			return error(buffer.toString());
		}

		// DdMonitorMsgUtil.sendDdSysErrMsg("手动更新商户日志统计" + startDate + "到" + endDate + "成功");
		return success();
	}

}
