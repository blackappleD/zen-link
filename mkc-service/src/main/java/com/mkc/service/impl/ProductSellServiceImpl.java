package com.mkc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.core.text.Convert;
import com.mkc.common.enums.OnOffState;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.ProductSell;
import com.mkc.mapper.ProductSellMapper;
import com.mkc.service.IProductSellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品销售Service业务层处理
 * 
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@DS("business")
@Service
public class ProductSellServiceImpl implements IProductSellService 
{

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProductSellMapper productSellMapper;

    /**
     * 查询产品销售
     * 
     * @param id 产品销售主键
     * @return 产品销售
     */
    @Override
    public ProductSell selectProductSellById(Long id)
    {
        return productSellMapper.selectProductSellById(id);
    }

    @Override
    public ProductSell selectProductSellAndRutesById(Long id) {
        return productSellMapper.selectProductSellAndRutesById(id);
    }


    @Override
    public ProductSell selectProductSellByMer(String merCode,String productCode){


        String key= RedisKey.MER_PRODUCT_SELL_KEY+merCode+":"+productCode;

        ProductSell productSell = redisCache.getCacheObject(key);

        if (productSell != null) {
            return productSell;
        }

        QueryWrapper<ProductSell> query=new QueryWrapper();

        query.lambda().eq(ProductSell::getMerCode,merCode)
                .eq(ProductSell::getProductCode,productCode)
                .eq(ProductSell::getStatus, OnOffState.STATE_ON);

        productSell = productSellMapper.selectOne(query);

        if(productSell==null){
            return null;
        }
        redisCache.setCacheObject(key,productSell);

        return productSell;

    }

    /**
     * 查询产品销售列表
     * 
     * @param productSell 产品销售
     * @return 产品销售
     */
    @Override
    public List<ProductSell> selectProductSellList(ProductSell productSell)
    {
        return productSellMapper.selectProductSellList(productSell);
    }

    /**
     * 新增产品销售
     * 
     * @param productSell 产品销售
     * @return 结果
     */
    @Override
    public int insertProductSell(ProductSell productSell)
    {
        productSell.setCreateTime(DateUtils.getNowDate());
        productSell.setUpdateTime(DateUtils.getNowDate());
        return productSellMapper.insert(productSell);
    }

    /**
     * 修改产品销售
     * 
     * @param productSell 产品销售
     * @return 结果
     */
    @Override
    public int updateProductSell(ProductSell productSell)
    {
        productSell.setUpdateTime(DateUtils.getNowDate());

        int num=productSellMapper.updateProductSell(productSell);

        reloadSellInfo(productSell.getId());

        return num;
    }

    /**
     * 批量删除产品销售
     * 
     * @param Ids 需要删除的产品销售主键
     * @return 结果
     */
    @Override
    public int deleteProductSellByIds(String Ids)
    {
        return productSellMapper.deleteProductSellByIds(Convert.toIntArray(Ids));
    }

    /**
     * 删除产品销售信息
     * 
     * @param id 产品销售主键
     * @return 结果
     */
    @Override
    public int deleteProductSellById(Long id)
    {
        return productSellMapper.deleteProductSellById(id);
    }

    private void reloadSellInfo(long id){
        ProductSell productSell = selectProductSellById(id);
        if(productSell == null){
            return;
        }
        String merCode=productSell.getMerCode();
        String productCode=productSell.getProductCode();

        if(!OnOffState.STATE_ON.equals(productSell.getStatus())){
            cleanCache(merCode,productCode);
            return;
        }
        String key= RedisKey.MER_PRODUCT_SELL_KEY+merCode+":"+productCode;
        log.info(" reloadSellInfo 产品销售缓存信息  KEY  {}",key);
        redisCache.setCacheObject(key,productSell);
    }

    private void cleanCache(String merCode,String productCode){

        String key= RedisKey.MER_PRODUCT_SELL_KEY+merCode+":"+productCode;

        log.info(" ===清除产品销售缓存信息  KEY  {}",key);
        redisCache.deleteObject(key);
    }
}
