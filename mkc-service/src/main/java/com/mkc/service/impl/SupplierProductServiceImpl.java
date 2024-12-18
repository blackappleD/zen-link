package com.mkc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.core.text.Convert;
import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.StringUtils;
import com.mkc.domain.SupplierProduct;
import com.mkc.mapper.SupplierProductMapper;
import com.mkc.service.ISupplierProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商产品信息管理Service业务层处理
 *
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@DS("business")
@Service
public class SupplierProductServiceImpl implements ISupplierProductService {
	@Autowired
	private SupplierProductMapper supplierProductMapper;

	@Autowired
	private RedisCache redisCache;

	/**
	 * 查询供应商产品信息管理
	 *
	 * @param id 供应商产品信息管理主键
	 * @return 供应商产品信息管理
	 */
	@Override
	public SupplierProduct selectSupplierProductById(Long id) {
		return supplierProductMapper.selectSupplierProductById(id);
	}


	/**
	 * 查询供应商产品信息管理
	 *
	 * @param supCode     供应商
	 * @param productCode 产品
	 * @return 供应商产品信息管理
	 */
	@Override
	public SuplierQueryBean selectSupProductBySupCode(String supCode, String productCode) {
		String key = RedisKey.SUP_PRODUCT_KEY + supCode;
		String hkey = productCode;

		SuplierQueryBean suplierQueryBean = redisCache.getCacheMapValue(key, hkey);
		if (suplierQueryBean != null) {
			return suplierQueryBean;
		}

		suplierQueryBean = supplierProductMapper.querySupProductBySupPro(supCode, productCode);
		if (suplierQueryBean == null) {
			return null;
		}

		redisCache.setCacheMapValue(key, hkey, suplierQueryBean);

		return suplierQueryBean;
	}

	@Override
	public void cleanSupplierProductCache(String supCode, String productCode) {

		String key = RedisKey.SUP_PRODUCT_KEY + supCode;
		String hkey = productCode;
		log.info("===cleanSupplierProductCache supCode  {} , productCode  {}", supCode, productCode);
		if (StringUtils.isBlank(productCode)) {
			redisCache.deleteObject(key);
		} else {
			redisCache.deleteHash(key, hkey);
		}

	}

	/**
	 * 查询供应商产品信息管理列表
	 *
	 * @param supplierProduct 供应商产品信息管理
	 * @return 供应商产品信息管理
	 */
	@Override
	public List<SupplierProduct> selectSupplierProductList(SupplierProduct supplierProduct) {

		return supplierProductMapper.selectSupplierProductList(supplierProduct);
	}


	/**
	 * 新增供应商产品信息管理
	 *
	 * @param supplierProduct 供应商产品信息管理
	 * @return 结果
	 */
	@Override
	public int insertSupplierProduct(SupplierProduct supplierProduct) {
		supplierProduct.setCreateTime(DateUtils.getNowDate());
		supplierProduct.setUpdateTime(DateUtils.getNowDate());
		return supplierProductMapper.insert(supplierProduct);
	}

	/**
	 * 修改供应商产品信息管理
	 *
	 * @param supProduct 供应商产品信息管理
	 * @return 结果
	 */
	@Override
	public int updateSupplierProduct(SupplierProduct supProduct) {
		supProduct.setUpdateTime(DateUtils.getNowDate());
		SupplierProduct supplierProduct = supplierProductMapper.selectSupplierProductById(supProduct.getId());
		int num = supplierProductMapper.updateSupplierProduct(supProduct);

		//清除缓存
		cleanSupplierProductCache(supplierProduct.getSupCode(), supplierProduct.getProductCode());

		return num;
	}

	/**
	 * 批量删除供应商产品信息管理
	 *
	 * @param Ids 需要删除的供应商产品信息管理主键
	 * @return 结果
	 */
	@Override
	public int deleteSupplierProductByIds(String Ids) {
		Integer[] intArr = Convert.toIntArray(Ids);
		return supplierProductMapper.deleteSupplierProductByIds(intArr);
	}

	/**
	 * 删除供应商产品信息管理信息
	 *
	 * @param id 供应商产品信息管理主键
	 * @return 结果
	 */
	@Override
	public int deleteSupplierProductById(Long id) {
		return supplierProductMapper.deleteSupplierProductById(id);
	}


}
