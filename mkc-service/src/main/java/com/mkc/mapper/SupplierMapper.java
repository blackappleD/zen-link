package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.Supplier;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 供应商信息管理Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier>
{
    /**
     * 查询供应商信息管理
     * 
     * @param id 供应商信息管理主键
     * @return 供应商信息管理
     */
    public Supplier selectSupplierById(Long id);

    public Supplier selectSupplierByCode(String code);

    /**
     * 查询供应商信息管理列表
     * 
     * @param supplier 供应商信息管理
     * @return 供应商信息管理集合
     */
    public List<Supplier> selectSupplierList(Supplier supplier);



    /**
     * 查询产品code 联合供应商产品 查询可用的 供应商信息管理列表 （供应商和供应商产品都是启用的状态）
     *
     * @param productCode 产品code
     * @return 供应商信息管理集合
     */
    public List<Supplier> selectSupListByproCode(String productCode);


    /**
     * 新增供应商信息管理
     * 
     * @param supplier 供应商信息管理
     * @return 结果
     */
    public int insertSupplier(Supplier supplier);

    /**
     * 修改供应商信息管理
     * 
     * @param supplier 供应商信息管理
     * @return 结果
     */
    public int updateSupplier(Supplier supplier);

    /**
     * 删除供应商信息管理
     * 
     * @param id 供应商信息管理主键
     * @return 结果
     */
    public int deleteSupplierById(Long id);

    /**
     * 批量删除供应商信息管理
     * 
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSupplierByIds(Integer[] Ids);
}
