package com.mkc.api.supplier.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/17 17:31
 */
@Data
public class ProQualifyCertSupReqDTO {

	private MultipartFile Authorization;

	private String name;

	private String idNum;
}
