package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mkc.common.annotation.Excel;
import lombok.Data;

/**
 * 号段所属运营商 t_phone_city
 * 
 * @author atd
 * @date 2023-04-24
 */
@TableName(value = "t_phone_city")
@Data
public class PhoneCity
{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    /** 手机号段 前7位*/
    @Excel(name = "手机号段")
    @TableId(value = "phone", type = IdType.AUTO)
    private String phone;

    /** 运营商 */
    @Excel(name = "运营商")
    private String operator;

    /** 省份 */
    @Excel(name = "省份")
    private String province;
    /** 城市 */
    @Excel(name = "城市")
    private String city;






}
