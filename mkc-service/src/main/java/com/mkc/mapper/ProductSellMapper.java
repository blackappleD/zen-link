package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.ProductSell;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品销售Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */
@Mapper
public interface ProductSellMapper extends BaseMapper<ProductSell>
{
    /**
     * 查询产品销售
     * 
     * @param id 产品销售主键
     * @return 产品销售
     */
    public ProductSell selectProductSellById(Long id);
    /**
     * 查询产品销售
     *
     * @param id 产品销售主键
     * @return 产品销售
     */
    public ProductSell selectProductSellAndRutesById(Long id);

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
     * 删除产品销售
     * 
     * @param id 产品销售主键
     * @return 结果
     */
    public int deleteProductSellById(Long id);

    /**
     * 批量删除产品销售
     * 
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductSellByIds(Integer[] Ids);
}
