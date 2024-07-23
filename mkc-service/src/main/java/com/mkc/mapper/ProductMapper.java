package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */
@Mapper
public interface ProductMapper  extends BaseMapper<Product>
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
     * 删除产品
     * 
     * @param id 产品主键
     * @return 结果
     */
    public int deleteProductById(Long id);

    /**
     * 批量删除产品
     * 
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductByIds(Integer[] Ids);
}
