package com.mkc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.core.text.Convert;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.Supplier;
import com.mkc.mapper.SupplierMapper;
import com.mkc.service.ISupplierProductService;
import com.mkc.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商信息管理Service业务层处理
 * 
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@DS("business")
@Service
public class SupplierServiceImpl implements ISupplierService {
	@Autowired
	private SupplierMapper supplierMapper;

	@Autowired
	private ISupplierProductService supProductService;

	@Autowired
	private RedisCache redisCache;

	/**
	 * 查询供应商信息管理
	 * 
	 * @param id 供应商信息管理主键
	 * @return 供应商信息管理
	 */
	@Override
	public Supplier selectSupplierById(Long id) {
		return supplierMapper.selectSupplierById(id);
	}

	@Override
	public Supplier selectSupplierByCode(String code) {
		if(StringUtils.isBlank(code)){
			return null;
		}

		String key = RedisKey.SUPPLIER_KEY + code;

		Supplier supplier = redisCache.getCacheObject(key);
		if (supplier == null) {
			supplier = supplierMapper.selectSupplierByCode(code);
			if (supplier != null) {
				redisCache.setCacheObject(key, supplier);
			}
		}
		return supplier;
	}

	/**
	 * 查询供应商信息管理列表
	 * 
	 * @param supplier 供应商信息管理
	 * @return 供应商信息管理
	 */
	@Override
	public List<Supplier> selectSupplierList(Supplier supplier) {

		return supplierMapper.selectSupplierList(supplier);
	}

	@Override
	public List<Supplier> selectSupListByproCode(String productCode) {

		return supplierMapper.selectSupListByproCode(productCode);
	}

	/**
	 * 新增供应商信息管理
	 * 
	 * @param supplier 供应商信息管理
	 * @return 结果
	 */
	@Override
	public int insertSupplier(Supplier supplier) {
		supplier.setCreateTime(DateUtils.getNowDate());
		supplier.setUpdateTime(DateUtils.getNowDate());
		return supplierMapper.insert(supplier);
	}

	/**
	 * 修改供应商信息管理
	 * 
	 * @param supplier 供应商信息管理
	 * @return 结果
	 */
	@Override
	public int updateSupplier(Supplier supplier) {
		supplier.setUpdateTime(DateUtils.getNowDate());
		supProductService.cleanSupplierProductCache(supplier.getCode(), null);
		return supplierMapper.updateSupplier(supplier);
	}

	/**
	 * 批量删除供应商信息管理
	 * 
	 * @param Ids 需要删除的供应商信息管理主键
	 * @return 结果
	 */
	@Override
	public int deleteSupplierByIds(String Ids) {

		return supplierMapper.deleteSupplierByIds(Convert.toIntArray(Ids));
	}

	/**
	 * 删除供应商信息管理信息
	 * 
	 * @param id 供应商信息管理主键
	 * @return 结果
	 */
	@Override
	public int deleteSupplierById(Long id) {

		return supplierMapper.deleteSupplierById(id);
	}

}
