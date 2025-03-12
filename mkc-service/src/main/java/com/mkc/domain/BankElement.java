package com.mkc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mkc.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/3/12 13:49
 */
@TableName(value = "t_bank_element")
@EqualsAndHashCode(callSuper = true)
@Data
public class BankElement extends BaseEntity {

	@TableId(type = IdType.AUTO)
	private Long id;

	@TableField(value = "bank_card")
	private String bankCard;

	@TableField(value = "cert_name")
	private String certName;

	@TableField(value = "cert_no")
	private String certNo;

	@TableField
	private String mobile;

	@TableField(value = "dc_type")
	private String dcType;

	@TableField(value = "detail_resp_code")
	private String detailRespCode;

	@TableField(value = "detail_resp_msg")
	private String detailRespMsg;


}
