package com.mkc.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkc.domain.MerInfo;
import com.mkc.domain.Product;
import com.mkc.domain.SupReport;
import com.mkc.domain.Supplier;
import com.mkc.mapper.SupReportMapper;
import com.mkc.mapper.SupReqLogMapper;
import com.mkc.service.IMerInfoService;
import com.mkc.service.IProductService;
import com.mkc.service.ISupReportService;
import com.mkc.service.ISupplierService;

/**
 * 供应商调用日志统计Service业务层处理
 * 
 * @author mkc
 * @date 2023-06-16
 */
@Service
@DS("business")
public class SupReportServiceImpl extends ServiceImpl<SupReportMapper, SupReport> implements ISupReportService {

	@Autowired
	private SupReqLogMapper supReqLogMapper;
	@Autowired
	private SupReportMapper supReportMapper;
	
	@Autowired
	private ISupplierService supplierService;
	@Autowired
	private IMerInfoService merInfoService;
	@Autowired
	private IProductService productService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean statSupReqLogReport(LocalDate date, String supCode) {
		LocalDate end = date.plusDays(1);
		
		List<SupReport> reports = selectSupReqLogReport(date, end, supCode);
		if(reports.isEmpty()) { return true; }
		
		reports.forEach(supReport -> {
			supReport.setReqDate(date);
			supReport.setSupName(getSupName(supReport.getSupCode()));
			supReport.setMerName(getMerName(supReport.getMerCode()));
			supReport.setProductName(getProcductName(supReport.getProductCode()));
		});
		
		LambdaQueryWrapper<SupReport> queryWrapper = new LambdaQueryWrapper<SupReport>();
		queryWrapper.ge(SupReport::getReqDate, date).lt(SupReport::getReqDate, end);
		queryWrapper.eq(StringUtils.isNotBlank(supCode), SupReport::getSupCode, supCode);
		remove(queryWrapper);
		
		return saveBatch(reports);
	}

	private List<SupReport> selectSupReqLogReport(LocalDate date,LocalDate end , String supCode){
		List<SupReport> reports = supReqLogMapper.selectSupReqLogReport(date, end, supCode);
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
	public List<SupReport> listSupReport(SupReport supReport) {
		return supReportMapper.selectSupReport(supReport);
	}

}
