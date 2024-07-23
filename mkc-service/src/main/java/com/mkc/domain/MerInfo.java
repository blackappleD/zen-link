package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkc.common.annotation.Excel;
import com.mkc.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户信息管理对象 t_mer_info
 * 
 * @author atd
 * @date 2023-04-24
 */

@Data
@Accessors(chain = true)
@TableName(value = "t_mer_info")
public class MerInfo extends BaseEntity
{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商户编码 */
    @Excel(name = "商户编码")
    private String merCode;

    /** 商户名称 */
    @Excel(name = "商户名称")
    private String merName;

    /** 销售负责人 */
    @Excel(name = "销售负责人")
    private String sellPerson;

    /** 发票类型 1.电子，2.纸质 */
    @Excel(name = "发票类型")
    private String invoiceType;

    /** 账单类型 */
    @Excel(name = "账单类型")
    private String billType;

    /** 客户类型 */
    @Excel(name = "客户类型")
    private String merType;

    /** 商户联系人邮箱 */
    @Excel(name = "商户联系人邮箱")
    private String email;

    /** 结算方式  Y预付费，H后付费 */
    @Excel(name = "结算方式")
    private String settleType;

    /** ip 白名单 */
    @Excel(name = "ip 白名单")
    private String ips;

    /** 后付费授信额度 */
    @Excel(name = "后付费授信额度")
    private BigDecimal lineOfCredit;

    /** 状态 00 启用 01 停用 */
    private String status;

    /** 是否有余额标志 true 有，false 余额不足 */
    @TableField(exist = false)
    private Boolean balanceFlag=false;

    /** 签名key 令牌 */
    private String signKey;

    /** 秘钥 秘钥 */
    private String signPwd;

    /** 账户余额 */
    @Excel(name = "账户余额")
    private BigDecimal balance;
    /** 账户预警金额 */
    @Excel(name = "账户预警金额")
    private BigDecimal warnAmount;

    /** 协议状态  00 已签约, 01 未签约 */
    @Excel(name = "协议状态")
    private String protocolStatus;

    /** 协议开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "协议开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date starTime;

    /** 协议结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "协议结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    public MerInfo() {

    }
    public MerInfo(String merCode, String merName) {
        this.merCode = merCode;
        this.merName = merName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("merCode", getMerCode())
            .append("merName", getMerName())
            .append("sellPerson", getSellPerson())
            .append("invoiceType", getInvoiceType())
            .append("billType", getBillType())
            .append("merType", getMerType())
            .append("email", getEmail())
            .append("settleType", getSettleType())
            .append("lineOfCredit", getLineOfCredit())
            .append("status", getStatus())
          //  .append("signkey", getSignKey())
           // .append("signPwd", getSignPwd())
            .append("balance", getBalance())
            .append("protocolStatus", getProtocolStatus())
            .append("starTime", getStarTime())
            .append("endTime", getEndTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
