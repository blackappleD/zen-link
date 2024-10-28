package com.mkc.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.mkc.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkc.mapper.MerReportMapper;
import com.mkc.mapper.MerReqLogMapper;
import com.mkc.service.IMerInfoService;
import com.mkc.service.IMerReportService;
import com.mkc.service.IProductService;
import com.mkc.service.ISupplierService;

/**
 * 商户调用日志统计Service业务层处理
 *
 * @author mkc
 * @date 2023-06-16
 */
@Service
@DS("business")
public class MerReportServiceImpl extends ServiceImpl<MerReportMapper, MerReport> implements IMerReportService {

	@Autowired
	private MerReqLogMapper merReqLogMapper;
	@Autowired
	private MerReportMapper merReportMapper;

	@Autowired
	private ISupplierService supplierService;
	@Autowired
	private IMerInfoService merInfoService;
	@Autowired
	private IProductService productService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean statMerReqLogReport(LocalDate date, String merCode) {
		LocalDate end = date.plusDays(1);

		List<MerReport> reports = selectMerReqLogReport(date, end, merCode);
		if(reports.isEmpty()) { return true; }

		reports.forEach(merReport -> {
			merReport.setReqDate(date);
			merReport.setSupName(getSupName(merReport.getSupCode()));
			merReport.setMerName(getMerName(merReport.getMerCode()));
			merReport.setProductName(getProcductName(merReport.getProductCode()));
		});

		LambdaQueryWrapper<MerReport> queryWrapper = new LambdaQueryWrapper<MerReport>();
		queryWrapper.ge(MerReport::getReqDate, date).lt(MerReport::getReqDate, end);
		queryWrapper.eq(StringUtils.isNotBlank(merCode), MerReport::getMerCode, merCode);
		remove(queryWrapper);

		return saveBatch(reports);
	}


	private List<MerReport> selectMerReqLogReport(LocalDate date,LocalDate end, String merCode){
		List<MerReport> reports = merReqLogMapper.selectMerReqLogReport(date, end, merCode);
		return reports;
	}


	private String getSupName(String supCode) {
		Supplier supplier = supplierService.selectSupplierByCode(supCode);
		return supplier != null ? supplier.getName() : "缺省";
	}
	private String getMerName(String merCode) {
		MerInfo merInfo = merInfoService.selectMerInfoByCode(merCode);
		return merInfo != null ? merInfo.getMerName() : "缺省";
	}
	private String getProcductName(String procductCode) {
		Product product = productService.selectProductByCode(procductCode);
		return product != null ? product.getProductName() : "缺省";
	}

	@Override
	public List<MerReport> listMerReport(MerReport merReport) {
		return merReportMapper.selectMerReport(merReport);
	}


	@Override
	public JSONObject listFxReport(MerReport merReport) {
//		List<FxReqRecord> fxReqRecords = fxReqRecordMapper.listByRangeTime(merReqLog);
		JSONObject jsonObject = new JSONObject();
////        jsonObject.put("res", fxReqRecords);
//		System.err.println(merReqLog);
//		jsonObject.put("total", fxReqRecords.size());
		return jsonObject;
	}

}
