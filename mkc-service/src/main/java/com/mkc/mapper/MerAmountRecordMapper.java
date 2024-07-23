package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.MerAmountRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商户余额操作记录Mapper接口
 * 
 * @author atd
 * @date 2023-04-24
 */
@Mapper
public interface MerAmountRecordMapper  extends BaseMapper<MerAmountRecord>
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
     * 删除商户余额操作记录
     * 
     * @param id 商户余额操作记录主键
     * @return 结果
     */
    public int deleteMerAmountRecordById(Long id);

    /**
     * 批量删除商户余额操作记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMerAmountRecordByIds(Integer[] ids);
}
