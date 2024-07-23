package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.SupplierRoute;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 供应商路由Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */
@Mapper
public interface SupplierRouteMapper extends BaseMapper<SupplierRoute>
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
     * 新增供应商路由
     * 
     * @param supplierRoute 供应商路由
     * @return 结果
     */
    public int insertSupplierRoute(SupplierRoute supplierRoute);

    /**
     * 修改供应商路由
     * 
     * @param supplierRoute 供应商路由
     * @return 结果
     */
    public int updateSupplierRoute(SupplierRoute supplierRoute);

    /**
     * 删除供应商路由
     * 
     * @param id 供应商路由主键
     * @return 结果
     */
    public int deleteSupplierRouteById(Long id);

    /**
     * 批量删除供应商路由
     * 
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSupplierRouteByIds(Integer[] Ids);
}
