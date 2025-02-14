package com.mkc.bean;

import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tqlei
 * @date 2023/5/17 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CkMerBean {

	private String merCode;
	private String merSeq;
	private String key;
	private String plaintext;
	private String merSign;
	private String productCode;

	public CkMerBean(String merCode, String key, String plaintext, String merSign, String merSeq) {
		this.merCode = merCode;
		this.plaintext = plaintext;
		this.merSign = merSign;
		this.merSeq = merSeq;
		this.key = key;
	}

	public static CkMerBean build(BaseDTO dto, ProductCodeEum productCode) {
		return CkMerBean.builder().key(dto.getKey())
				.merCode(dto.getMerCode())
				.merSign(dto.getSign())
				.merSeq(dto.getMerSeq())
				.productCode(productCode.getCode())
				.build();
	}

	public CkMerBean plaintext(String plaintext) {
		this.plaintext = plaintext;
		return this;
	}
}
