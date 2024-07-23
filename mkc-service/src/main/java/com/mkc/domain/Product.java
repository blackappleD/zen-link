package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mkc.common.annotation.Excel;
import com.mkc.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 产品对象 t_product
 * 
 * @author atd
 * @date 2023-04-24
 */
@TableName(value = "t_product")
@Data
public class Product extends BaseEntity
{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 产品code */
    @Excel(name = "产品code")
    private String productCode;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品分类编码 */
    @Excel(name = "产品分类编码")
    private String cgCode;

    /** 状态 */
    @Excel(name = "状态")
    private String status;


    public Product() {

    }
    public Product(String productCode, String productName) {
        this.productCode = productCode;
        this.productName = productName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("cgCode", getCgCode())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
