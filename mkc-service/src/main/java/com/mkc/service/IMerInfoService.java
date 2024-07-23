package com.mkc.service;

import com.mkc.common.enums.MerAmountType;
import com.mkc.domain.MerAmountRecord;
import com.mkc.domain.MerInfo;
import com.mkc.domain.MerReqLog;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商户信息管理Service接口
 *
 * @author atd
 * @date 2023-04-24
 */
public interface IMerInfoService
{
    /**
     * 查询商户信息管理
     *
     * @param id 商户信息管理主键
     * @return 商户信息管理
     */
    public MerInfo selectMerInfoById(Long id);

    /**
     * 查询商户信息管理
     *
     * @param code 商户信息管理主键
     * @return 商户信息管理
     */
    public MerInfo selectMerInfoByCode(String code);
    /**
     * 查询商户信息管理 (API接口定制接口)
     *
     * @param code 商户信息管理主键
     * @return 商户信息管理
     */
    public MerInfo selectMerInfoByCodeApi(String code);

    /**
     * 查询商户信息管理列表
     *
     * @param merInfo 商户信息管理
     * @return 商户信息管理集合
     */
    public List<MerInfo> selectMerInfoList(MerInfo merInfo);

    List<MerInfo> selectMerNameList();

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
     * 批量删除商户信息管理
     *
     * @param ids 需要删除的商户信息管理主键集合
     * @return 结果
     */
    public int deleteMerInfoByIds(String ids);

    /**
     * 删除商户信息管理信息
     *
     * @param id 商户信息管理主键
     * @return 结果
     */
    public int deleteMerInfoById(Long id);

    /**
     * 扣减商户账户余额
     * @param merLog
     */
    public void dedMerBalance_SYS(MerReqLog merLog);


    /**
     *  更新商户账户余额
     * @param merCode 商户code
     * @param amount 金额
     * @param type 操作类型
     * @param updBy 操作者
     */
    public int updMerBalance(String merCode, BigDecimal amount, MerAmountType type,String updBy,String remark);

    /**
     * 发送邮件商户秘钥信息
     * @param id
     * @return
     */
    void sendMerKeyMail(Long id);
}
