package com.mkc.api.dto.ck.req;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/17 16:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProQualifyCertReqDTO extends BaseDTO {

	private MultipartFile authorization;

	@NotBlank(message = "姓名不能为空")
	private String name;

	@NotBlank(message = "证件号码不能为空")
	private String idNum;

}
