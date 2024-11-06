package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkc.common.annotation.Excel;
import com.mkc.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 法信请求成功的记录号 t_fx_req_record
 *
 * @author xiewei
 * @date 2024-07-31
 */
@TableName(value = "t_fx_req_record")
@Data
public class FxReqRecord extends BaseEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 请求订单号 */
    @Excel(name = "请求订单号")
    private String reqOrderNo;

    /** 查询次数 */
    @Excel(name = "查询次数")
    private String queryCount;

    /** 商户code*/
    @Excel(name = "商户code")
    private String merCode;

    /** 商户code*/
    @Excel(name = "商户名称")
    private transient String merName;

    /** 商户code*/
    @Excel(name = "人员信息")
    private String persons;

    /**商户请求查询结果人员信息*/
    @Excel(name = "商户最终请求查询人员信息")
    private String merRequestData;

    /**供应商返回的结果*/
    @Excel(name = "商户最终请求查询结果")
    private String merResultData;
    /**商户请求查询结果人员信息*/
    @Excel(name = "用户查询结果标记")
    private String userFlag;
    @Excel(name = "创建时间")
    private transient String createTimeStr;
    @Excel(name = "更新时间")
    private transient String updateTimeStr;
    @Excel(name = "商户未查询信息")
    private transient String unknownInfo;
    @Excel(name = "是否查得")
    private transient Boolean isGet;
    @Excel(name = "进价")
    private transient Integer inPrice = 0;
    @Excel(name = "计费人次")
    private transient Integer billedTimes = 0;
    @Excel(name = "备注")
    private transient String remark;

}
