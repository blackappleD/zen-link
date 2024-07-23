package com.mkc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.common.core.text.Convert;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.MerAmountRecord;
import com.mkc.mapper.MerAmountRecordMapper;
import com.mkc.service.IMerAmountRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户余额操作记录Service业务层处理
 * 
 * @author atd
 * @date 2023-04-24
 */
@Service
@Slf4j
@DS("business")
public class MerAmountRecordServiceImpl implements IMerAmountRecordService 
{
    @Autowired
    private MerAmountRecordMapper merAmountRecordMapper;

    /**
     * 查询商户余额操作记录
     * 
     * @param id 商户余额操作记录主键
     * @return 商户余额操作记录
     */
    @Override
    public MerAmountRecord selectMerAmountRecordById(Long id)
    {
        return merAmountRecordMapper.selectMerAmountRecordById(id);
    }

    /**
     * 查询商户余额操作记录列表
     * 
     * @param merAmountRecord 商户余额操作记录
     * @return 商户余额操作记录
     */
    @Override
    public List<MerAmountRecord> selectMerAmountRecordList(MerAmountRecord merAmountRecord)
    {
        return merAmountRecordMapper.selectMerAmountRecordList(merAmountRecord);
    }

    /**
     * 新增商户余额操作记录
     * 
     * @param merAmountRecord 商户余额操作记录
     * @return 结果
     */
    @Override
    public int insertMerAmountRecord(MerAmountRecord merAmountRecord)
    {
        merAmountRecord.setCreateTime(DateUtils.getNowDate());
        merAmountRecord.setUpdateTime(DateUtils.getNowDate());
        return merAmountRecordMapper.insert(merAmountRecord);
    }

    /**
     * 修改商户余额操作记录
     * 
     * @param merAmountRecord 商户余额操作记录
     * @return 结果
     */
    @Override
    public int updateMerAmountRecord(MerAmountRecord merAmountRecord)
    {
        merAmountRecord.setUpdateTime(DateUtils.getNowDate());
        return merAmountRecordMapper.updateMerAmountRecord(merAmountRecord);
    }

    /**
     * 批量删除商户余额操作记录
     * 
     * @param ids 需要删除的商户余额操作记录主键
     * @return 结果
     */
    @Override
    public int deleteMerAmountRecordByIds(String ids)
    {
        return merAmountRecordMapper.deleteMerAmountRecordByIds(Convert.toIntArray(ids));
    }

    /**
     * 删除商户余额操作记录信息
     * 
     * @param id 商户余额操作记录主键
     * @return 结果
     */
    @Override
    public int deleteMerAmountRecordById(Long id)
    {
        return merAmountRecordMapper.deleteMerAmountRecordById(id);
    }
}
