package com.mkc.dto.fx;

import lombok.Data;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/11 9:33
 */
@Data
public class HouseInfoDTO {

	private String reqOrderNo;
	private String approvalStatus;
	private List<AuthResult> authResults;

	@Data
	public static class AuthResult {
		private String cardNum;
		private String authState;
		private String authStateDesc;
		private int isReAuth;
		private List<HouseDetail> resultList;
	}

	@Data
	public static class HouseDetail {
		private String certNo;
		private String unitNo;
		private String location;
		private String ownership;
		private String houseArea;
		private String rightsType;
		private String isSealUp;
		private String isMortgaged;
		private String rightsStartTime;
		private String rightsEndTime;
		private String useTo;

	}


}
