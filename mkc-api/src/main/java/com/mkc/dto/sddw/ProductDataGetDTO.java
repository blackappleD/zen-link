package com.mkc.dto.sddw;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/25 17:45
 */
@Data
public class ProductDataGetDTO {

	private String invokeFlowId;

	private String productCode;

	private String useIdType;

	private String useIdNumber;

	private String idType;

	private String idNumber;

	private String authId;

	private Map<String, String> extraParam;

	private String assetsTypeId;

	private String dataProductIdList;

	private String description;

}
