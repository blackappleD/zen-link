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
 * 供应商路由对象 t_supplier_route
 * 
 * @author atd
 * @date 2023-04-24
 */

@TableName(value = "t_supplier_route")
@Data
public class SupplierRoute extends BaseEntity
{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 产品分类编码 */
    @Excel(name = "产品分类编码")
    private String cgCode;

    /** 供应商code */
    @Excel(name = "供应商code")
    private String supCode;

    /** 商户code */
    @Excel(name = "商户code")
    private String merCode;

    /** 类名 */
    @Excel(name = "供应商名")
    private String supName;

    /** 产品id */
    @Excel(name = "产品code")
    private String productCode;

    /** 排序 1最高 */
    @Excel(name = "排序 1最高")
    private Long sort;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cgCode", getCgCode())
            .append("supCode", getSupCode())
            .append("merCode", getMerCode())
            .append("supName", getSupName())
            .append("productCode", getProductCode())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
