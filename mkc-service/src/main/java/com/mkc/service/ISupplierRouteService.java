package com.mkc.service;

import com.mkc.domain.ProductSell;
import com.mkc.domain.SupplierRoute;

import java.util.List;

/**
 * 供应商路由Service接口
 * 
 * @author atd
 * @date 2023-04-24
 */
public interface ISupplierRouteService 
{
    /**
     * 查询供应商路由
     * 
     * @param id 供应商路由主键
     * @return 供应商路由
     */
    public SupplierRoute selectSupplierRouteById(Long id);

    /**
     * 查询供应商路由列表
     * 
     * @param supplierRoute 供应商路由
     * @return 供应商路由集合
     */
    public List<SupplierRoute> selectSupplierRouteList(SupplierRoute supplierRoute);
    /**
     * 查询供应商路由列表
     *
     * @param merCode 商户code
     * @param productCode 产品code
     * @return 供应商路由集合
     */
    public List<SupplierRoute> querySupRouteList(String merCode,String productCode);

    /**
     * 新增供应商路由
     * 
     * @param supplierRoute 供应商路由
     * @return 结果
     */
    public int insertSupplierRoute(SupplierRoute supplierRoute);

    /**
     * 新增供应商产品信息管理
     *
     * @param productSell 供应商产品信息管理
     * @return 结果
     */
    public int batchAdd(ProductSell productSell);

    /**
     * 修改供应商路由
     * 
     * @param supplierRoute 供应商路由
     * @return 结果
     */
    public int updateSupplierRoute(SupplierRoute supplierRoute);

    /**
     * 批量删除供应商路由
     * 
     * @param Ids 需要删除的供应商路由主键集合
     * @return 结果
     */
    public int deleteSupplierRouteByIds(String Ids);

    /**
     * 删除供应商路由信息
     * 
     * @param id 供应商路由主键
     * @return 结果
     */
    public int deleteSupplierRouteById(Long id);
}
