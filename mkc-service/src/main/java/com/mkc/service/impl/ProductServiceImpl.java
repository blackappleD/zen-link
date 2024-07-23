package com.mkc.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.core.text.Convert;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.Product;
import com.mkc.mapper.ProductMapper;
import com.mkc.service.IProductService;

import lombok.extern.slf4j.Slf4j;

/**
 * 产品Service业务层处理
 *
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@DS("business")
@Service("productService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    @Override
    public Product selectProductById(Long id) {
        return productMapper.selectProductById(id);
    }

    /**
     * 查询产品
     *
     * @param code 产品主键
     * @return 产品
     */
    @Override
    public Product selectProductByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        String key = RedisKey.PRODUCT_KEY + code;

        Product product = redisCache.getCacheObject(key);
        if (product == null) {
            product = productMapper.selectProductByCode(code);
            if (product != null) {
                redisCache.setCacheObject(key, product);
            }
        }
        return product;
    }

    /**
     * 查询产品列表
     *
     * @param product 产品
     * @return 产品
     */
    @Override
    public List<Product> selectProductList(Product product) {
        return productMapper.selectProductList(product);
    }


    @Override
    @Cacheable(cacheNames = RedisKey.PRODUCT_ALL_KEY, key = "'all'")
    public List<Product> getProductNameAll() {
        return productMapper.selectProductList(null).stream()
                .map(product -> new Product(product.getProductCode(),
                        product.getProductName())).collect(Collectors.toList());
    }

    /**
     * 新增产品
     *
     * @param product 产品
     * @return 结果
     */
    @Override
    @CacheEvict(cacheNames = RedisKey.PRODUCT_ALL_KEY, key = "'all'")
    public int insertProduct(Product product) {
        product.setCreateTime(DateUtils.getNowDate());
        product.setUpdateTime(DateUtils.getNowDate());
        return productMapper.insert(product);
    }

    /**
     * 修改产品
     *
     * @param product 产品
     * @return 结果
     */
    @Override
    public int updateProduct(Product product) {
        product.setUpdateTime(DateUtils.getNowDate());
        return productMapper.updateProduct(product);
    }

    /**
     * 批量删除产品
     *
     * @param Ids 需要删除的产品主键
     * @return 结果
     */
    @Override
    @CacheEvict(cacheNames = RedisKey.PRODUCT_ALL_KEY, key = "'all'")
    public int deleteProductByIds(String Ids) {
        return productMapper.deleteProductByIds(Convert.toIntArray(Ids));
    }

    /**
     * 删除产品信息
     *
     * @param id 产品主键
     * @return 结果
     */
    @Override
    @CacheEvict(cacheNames = RedisKey.PRODUCT_ALL_KEY, key = "'all'")
    public int deleteProductById(Long id) {
        return productMapper.deleteProductById(id);
    }
}
