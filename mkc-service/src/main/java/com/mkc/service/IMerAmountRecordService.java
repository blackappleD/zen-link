package com.mkc.service;

import com.mkc.domain.MerAmountRecord;

import java.util.List;

/**
 * 商户余额操作记录Service接口
 * 
 * @author atd
 * @date 2023-04-24
 */
public interface IMerAmountRecordService 
{
    /**
     * 查询商户余额操作记录
     * 
     * @param id 商户余额操作记录主键
     * @return 商户余额操作记录
     */
    public MerAmountRecord selectMerAmountRecordById(Long id);

    /**
     * 查询商户余额操作记录列表
     * 
     * @param merAmountRecord 商户余额操作记录
     * @return 商户余额操作记录集合
     */
    public List<MerAmountRecord> selectMerAmountRecordList(MerAmountRecord merAmountRecord);

    /**
     * 新增商户余额操作记录
     * 
     * @param merAmountRecord 商户余额操作记录
     * @return 结果
     */
    public int insertMerAmountRecord(MerAmountRecord merAmountRecord);

    /**
     * 修改商户余额操作记录
     * 
     * @param merAmountRecord 商户余额操作记录
     * @return 结果
     */
    public int updateMerAmountRecord(MerAmountRecord merAmountRecord);

    /**
     * 批量删除商户余额操作记录
     * 
     * @param ids 需要删除的商户余额操作记录主键集合
     * @return 结果
     */
    public int deleteMerAmountRecordByIds(String ids);

    /**
     * 删除商户余额操作记录信息
     * 
     * @param id 商户余额操作记录主键
     * @return 结果
     */
    public int deleteMerAmountRecordById(Long id);
}
