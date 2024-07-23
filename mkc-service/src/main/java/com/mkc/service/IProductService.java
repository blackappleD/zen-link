package com.mkc.service;

import com.mkc.domain.Product;

import java.util.List;

/**
 * 产品Service接口
 * 
 * @author atd
 * @date 2023-04-24
 */
public interface IProductService 
{
    /**
     * 查询产品
     * 
     * @param id 产品主键
     * @return 产品
     */
    public Product selectProductById(Long id);
    /**
     * 查询产品
     *
     * @param code 产品主键
     * @return 产品
     */
    public Product selectProductByCode(String code);

    /**
     * 查询产品列表
     * 
     * @param product 产品
     * @return 产品集合
     */
    public List<Product> selectProductList(Product product);

    List<Product> getProductNameAll();

    /**
     * 新增产品
     * 
     * @param product 产品
     * @return 结果
     */
    public int insertProduct(Product product);

    /**
     * 修改产品
     * 
     * @param product 产品
     * @return 结果
     */
    public int updateProduct(Product product);

    /**
     * 批量删除产品
     * 
     * @param Ids 需要删除的产品主键集合
     * @return 结果
     */
    public int deleteProductByIds(String Ids);

    /**
     * 删除产品信息
     * 
     * @param id 产品主键
     * @return 结果
     */
    public int deleteProductById(Long id);
}
