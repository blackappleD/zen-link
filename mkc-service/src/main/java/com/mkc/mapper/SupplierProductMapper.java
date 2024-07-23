package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.domain.SupplierProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商产品信息管理Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */

@Mapper
public interface SupplierProductMapper  extends BaseMapper<SupplierProduct>
{
    /**
     * 查询供应商产品信息管理
     * 
     * @param id 供应商产品信息管理主键
     * @return 供应商产品信息管理
     */
    public SupplierProduct selectSupplierProductById(Long id);


    public SuplierQueryBean querySupProductBySupPro(@Param("supCode") String supCode, @Param("productCode") String productCode);

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
     * 删除供应商产品信息管理
     * 
     * @param id 供应商产品信息管理主键
     * @return 结果
     */
    public int deleteSupplierProductById(Long id);

    /**
     * 批量删除供应商产品信息管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSupplierProductByIds(Integer[] ids);
}
