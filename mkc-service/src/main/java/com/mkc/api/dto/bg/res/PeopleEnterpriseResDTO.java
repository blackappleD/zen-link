package com.mkc.api.dto.bg.res;

import lombok.Data;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/7 11:18
 */
@Data
public class PeopleEnterpriseResDTO {

	private List<ResultInfo> resultInfo;
	private int total;

	@Data
	public static class ResultInfo {
		private String orgName;
		private String name;
		private List<String> relationship;
		private String fSource;
		private BaseInfo basicInfo;
		private StockHolderItem stockHolderItem;
		private List<AdminPenalty> adminPenalty;
	}

	@Data
	public static class BaseInfo {
		private String entStatus;
		private String regDate;
		private String regCapital;
		private double regCap;
		private String industry;
		private String type;
		private String regCapCur;
		private String legalName;
		private String regNo;
		private String uscc;
		private String entName;
		private String entType;
		private String entTypeCode;
		private String registerPlace;
		private String base;
		private String revDate;
		private String apprDate;
		private String canDate;
		private double actualCap;
		private String actualCapCur;
		private String province;
		private String city;
		private String district;
		private String regOrg;
		private String opScope;
		private String nicCode;
		private String nicName;
		private String industryCode;
		private String tel;
		private String isListed;
		private StaffList staffList;
	}

	@Data
	public static class StaffList {
		private List<Staff> result;
	}

	@Data
	public static class Staff {
		private String name;
		private String type;
		private List<String> typeJoin;
	}

	@Data
	public static class StockHolderItem {
		private String orgHolderType;
		private String investDate;
		private String investRate;
		private double subscriptAmt;
		private String orgHolderName;
		private double acconam;
		private String currency;
		private String confrom;
	}

	@Data
	public static class AdminPenalty {
		private String deptName;
		private String reason;
		private String punishNo;
		private String type;
		private String content;
		private String decisionDate;
		private String legalPersonName;
		private String caseType;
	}

}
