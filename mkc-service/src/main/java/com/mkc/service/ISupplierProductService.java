package com.mkc.service;

import com.mkc.bean.SuplierQueryBean;
import com.mkc.domain.SupplierProduct;

import java.util.List;

/**
 * 供应商产品信息管理Service接口
 * 
 * @author atd
 * @date 2023-04-24
 */
public interface ISupplierProductService 
{
    /**
     * 查询供应商产品信息管理
     * 
     * @param id 供应商产品信息管理主键
     * @return 供应商产品信息管理
     */
    public SupplierProduct selectSupplierProductById(Long id);


    /**
     * 查询供应商和 产品 信息
     * @param supCode
     * @param productCode
     * @return
     */
    public SuplierQueryBean selectSupProductBySupCode(String supCode, String productCode);


    /**
     * 查询供应商产品信息管理列表
     * 
     * @param supplierProduct 供应商产品信息管理
     * @return 供应商产品信息管理集合
     */
    public List<SupplierProduct> selectSupplierProductList(SupplierProduct supplierProduct);

    /**
     * 新增供应商产品信息管理
     * 
     * @param supplierProduct 供应商产品信息管理
     * @return 结果
     */
    public int insertSupplierProduct(SupplierProduct supplierProduct);


    /**
     * 修改供应商产品信息管理
     * 
     * @param supplierProduct 供应商产品信息管理
     * @return 结果
     */
    public int updateSupplierProduct(SupplierProduct supplierProduct);

    /**
     * 批量删除供应商产品信息管理
     * 
     * @param Ids 需要删除的供应商产品信息管理主键集合
     * @return 结果
     */
    public int deleteSupplierProductByIds(String Ids);

    /**
     * 删除供应商产品信息管理信息
     * 
     * @param id 供应商产品信息管理主键
     * @return 结果
     */
    public int deleteSupplierProductById(Long id);

    /**
     * 清除缓存
     * @param supCode
     * @param productCode
     */
    public  void cleanSupplierProductCache(String supCode,String productCode);
}
