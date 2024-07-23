package com.mkc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mkc.common.core.text.Convert;
import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.StringUtils;
import com.mkc.domain.ProductSell;
import com.mkc.domain.Supplier;
import com.mkc.domain.SupplierRoute;
import com.mkc.mapper.SupplierMapper;
import com.mkc.mapper.SupplierRouteMapper;
import com.mkc.service.ISupplierRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 供应商路由Service业务层处理
 * 
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@DS("business")
@Service
public class SupplierRouteServiceImpl implements ISupplierRouteService 
{
    @Autowired
    private SupplierRouteMapper supplierRouteMapper;
    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 查询供应商路由
     * 
     * @param id 供应商路由主键
     * @return 供应商路由
     */
    @Override
    public SupplierRoute selectSupplierRouteById(Long id)
    {
        return supplierRouteMapper.selectSupplierRouteById(id);
    }

    /**
     * 查询供应商路由列表
     * 
     * @param supplierRoute 供应商路由
     * @return 供应商路由
     */
    @Override
    public List<SupplierRoute> selectSupplierRouteList(SupplierRoute supplierRoute)
    {
        return supplierRouteMapper.selectSupplierRouteList(supplierRoute);
    }

    /**
     * 新增供应商路由
     * 
     * @param supplierRoute 供应商路由
     * @return 结果
     */
    @Override
    public int insertSupplierRoute(SupplierRoute supplierRoute)
    {
        supplierRoute.setCreateTime(DateUtils.getNowDate());
        supplierRoute.setUpdateTime(DateUtils.getNowDate());
        return supplierRouteMapper.insert(supplierRoute);
    }

    /**
     * 修改供应商路由
     * 
     * @param supplierRoute 供应商路由
     * @return 结果
     */
    @Override
    public int updateSupplierRoute(SupplierRoute supplierRoute)
    {
        supplierRoute.setUpdateTime(DateUtils.getNowDate());
        return supplierRouteMapper.updateSupplierRoute(supplierRoute);
    }

    /**
     * 批量删除供应商路由
     * 
     * @param Ids 需要删除的供应商路由主键
     * @return 结果
     */
    @Override
    public int deleteSupplierRouteByIds(String Ids)
    {
        return supplierRouteMapper.deleteSupplierRouteByIds(Convert.toIntArray(Ids));
    }

    /**
     * 删除供应商路由信息
     * 
     * @param id 供应商路由主键
     * @return 结果
     */
    @Override
    public int deleteSupplierRouteById(Long id)
    {
        return supplierRouteMapper.deleteSupplierRouteById(id);
    }


    /**
     * 新增供应商产品信息管理
     *
     * @param productSell 供应商产品信息管理
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchAdd(ProductSell productSell){

        String supNames = productSell.getSupNames();
        String merCodes = productSell.getMerCode();
        log.info("===============  {}",productSell);
        if(StringUtils.isBlank(supNames)){
            throw new RuntimeException("  supNames is err ");
        }
        if(StringUtils.isBlank(merCodes)){
            throw new RuntimeException(" merCode is err");
        }
        String[] supCodes = supNames.split(",");
        String[] merCodeStrs = merCodes.split(",");

        SupplierRoute supplierRoute=null;

        String productCode = productSell.getProductCode();

        for (String merCode : merCodeStrs){
            upSupRoute(productSell, merCode, supCodes, productCode);
        }
        return 1;
    }

    private void upSupRoute(ProductSell productSell, String merCode, String[] supCodes, String productCode) {
        SupplierRoute supplierRoute;
        //先删除所有的供应商路由配置
        QueryWrapper<SupplierRoute> wrapper=new QueryWrapper<SupplierRoute>();
        wrapper.lambda().eq(SupplierRoute::getMerCode, merCode)
                .eq(SupplierRoute::getProductCode, productCode);
        supplierRouteMapper.delete(wrapper);

        //新增新的
        for (int i = 0; i < supCodes.length; i++) {
            supplierRoute=new SupplierRoute();
            String supCode = supCodes[i];
            Supplier supplier = supplierMapper.selectSupplierByCode(supCode);
            supplierRoute.setCreateBy(productSell.getUpdateBy());
            supplierRoute.setUpdateBy(productSell.getUpdateBy());
            supplierRoute.setCreateTime(DateUtils.getNowDate());
            supplierRoute.setUpdateTime(DateUtils.getNowDate());
            supplierRoute.setMerCode(merCode);
            supplierRoute.setProductCode(productCode);
            supplierRoute.setCgCode(productSell.getCgCode());
            supplierRoute.setSupName(supplier.getName());
            supplierRoute.setSupCode(supplier.getCode());
            long sort=i+1;
            supplierRoute.setSort(sort);
            log.info("商户-产品路由配置 {}",supplierRoute);
            supplierRouteMapper.insert(supplierRoute);
        }
    }


    @Override
    public List<SupplierRoute> querySupRouteList(String merCode,String productCode){
        QueryWrapper<SupplierRoute> query=new QueryWrapper<SupplierRoute>();
        query.lambda()
                .eq(SupplierRoute::getMerCode,merCode)
                .eq(SupplierRoute::getProductCode,productCode)
                .orderByAsc(SupplierRoute::getSort);

        List<SupplierRoute> supplierRoutes = supplierRouteMapper.selectList(query);

        return supplierRoutes;
    }
}
