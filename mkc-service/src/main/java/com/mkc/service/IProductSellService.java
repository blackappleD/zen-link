package com.mkc.service;

import com.mkc.domain.ProductSell;

import java.util.List;

/**
 * 产品销售Service接口
 * 
 * @author atd
 * @date 2023-04-24
 */
public interface IProductSellService 
{
    /**
     * 查询产品销售
     * 
     * @param id 产品销售主键
     * @return 产品销售
     */
    public ProductSell selectProductSellById(Long id);

    /**
     * 查询产品销售 及 供应商路由信息
     *
     * @param id 产品销售主键
     * @return 产品销售
     */
    public ProductSell selectProductSellAndRutesById(Long id);

    /**
     * 根据商户查询商家中的  产品销售
     *
     * @param merCode  商户code
     * @param productCode 产品code
     * @return 产品销售信息
     */
    public ProductSell selectProductSellByMer(String merCode,String productCode);

    /**
     * 查询产品销售列表
     * 
     * @param productSell 产品销售
     * @return 产品销售集合
     */
    public List<ProductSell> selectProductSellList(ProductSell productSell);

    /**
     * 新增产品销售
     * 
     * @param productSell 产品销售
     * @return 结果
     */
    public int insertProductSell(ProductSell productSell);

    /**
     * 修改产品销售
     * 
     * @param productSell 产品销售
     * @return 结果
     */
    public int updateProductSell(ProductSell productSell);

    /**
     * 批量删除产品销售
     * 
     * @param Ids 需要删除的产品销售主键集合
     * @return 结果
     */
    public int deleteProductSellByIds(String Ids);

    /**
     * 删除产品销售信息
     * 
     * @param id 产品销售主键
     * @return 结果
     */
    public int deleteProductSellById(Long id);
}
