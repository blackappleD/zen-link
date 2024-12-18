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
 * 供应商信息管理对象 t_supplier
 *
 * @author atd
 * @date 2023-04-24
 */

@TableName(value = "t_supplier")
@Data
public class Supplier extends BaseEntity {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * code
	 */
	@Excel(name = "code")
	private String code;

	/**
	 * code
	 */
	@Excel(name = "处理器")
	private String processor;

	/**
	 * 名称
	 */
	@Excel(name = "名称")
	private String name;

	/**
	 * 账号
	 */
	@Excel(name = "账号")
	private String acc;

	/**
	 * 状态
	 */
	@Excel(name = "状态")
	private String status;

	/**
	 * 接口地址
	 */
	@Excel(name = "接口地址")
	private String url;

	/**
	 * 秘钥 加签相关pwd
	 */
	@Excel(name = "秘钥 加签相关pwd")
	private String signPwd;

	/**
	 * 加签相关key
	 */
	@Excel(name = "加签相关key")
	private String signKey;

	private String authAccount;

	private String authPwd;

//    /** 一致或者成功状态 */
//    @Excel(name = "一致或者成功状态")
//    private String okCode;
//
//    /** 不一致状态 */
//    @Excel(name = "不一致状态")
//    private String noCode;
//
//    /** 查无状态码 */
//    @Excel(name = "查无状态码")
//    private String notCode;

	@TableField(exist = false)
	/** 产品供应商路由是否选中 默认不存在 */
	private boolean flag = false;


	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("code", getCode())
				.append("name", getName())
				.append("acc", getAcc())
				.append("status", getStatus())
				.append("url", getUrl())
				.append("signpwd", getSignPwd())
				.append("signkey", getSignKey())
				.append("authAccount", getAuthAccount())
				.append("authPwd", getAuthPwd())
				.append("createBy", getCreateBy())
				.append("updateBy", getUpdateBy())
				.append("createTime", getCreateTime())
				.append("updateTime", getUpdateTime())
				.append("remark", getRemark())
				.toString();
	}
}
