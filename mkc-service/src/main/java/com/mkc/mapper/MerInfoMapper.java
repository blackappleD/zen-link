package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.MerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商户信息管理Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */

@Mapper
public interface MerInfoMapper extends BaseMapper<MerInfo>
{
    /**
     * 查询商户信息管理
     * 
     * @param id 商户信息管理主键
     * @return 商户信息管理
     */
    public MerInfo selectMerInfoById(Long id);

    public MerInfo selectMerInfoByCode(String merCode);

    /**
     * 查询商户信息管理列表
     * 
     * @param merInfo 商户信息管理
     * @return 商户信息管理集合
     */
    public List<MerInfo> selectMerInfoList(MerInfo merInfo);

    /**
     * 新增商户信息管理
     * 
     * @param merInfo 商户信息管理
     * @return 结果
     */
    public int insertMerInfo(MerInfo merInfo);

    /**
     * 修改商户信息管理
     * 
     * @param merInfo 商户信息管理
     * @return 结果
     */
    public int updateMerInfo(MerInfo merInfo);

    /**
     * 删除商户信息管理
     * 
     * @param id 商户信息管理主键
     * @return 结果
     */
    public int deleteMerInfoById(Long id);

    /**
     * 批量删除商户信息管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMerInfoByIds(Integer[] ids);


    /**
     * 扣减商户余额
     * @param merCode
     * @param orderAmount
     * @return
     */
    public int dedMerBalance(@Param("merCode") String merCode,@Param("orderAmount")  BigDecimal orderAmount);

    /**
     * 增加商户余额
     * @param merCode
     * @param orderAmount
     * @return
     */
    public int addMerBalance(@Param("merCode") String merCode,@Param("orderAmount")  BigDecimal orderAmount);
}
