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

	@Data
	public static class Response {
		private List<DataItem> datalist;
		private int total;
	}

	@Data
	public static class DataItem {
		private List<AdminPenalty> adminPenalty;
		private BasicInfo basicInfo;
		private String fSource;
		private String orgName;
		private String pName;
		private List<String> relationship;
		private StockHolderItem stockHolderItem;
		private StockHolderItem hisStockHolderItem;
	}

	@Data
	public static class AdminPenalty {
		private String caseType;
		private String content;
		private String decisionDate;
		private String departmentName;
		private String legalPersonName;
		private String punishNumber;
		private String reason;
		private String type;
	}

	@Data
	public static class BasicInfo {
		private String apprDate;
		private String base;
		private String canDate;
		private String city;
		private String companyOrgType;
		private String companyOrgTypeCode;
		private String creditCode;
		private String district;
		private String dom;
		private String email;
		private String empNum;
		private String estiblishTime;
		private String industry;
		private String industryCode;
		private String isListed;
		private String legalPersonName;
		private String name;
		private String nicCode;
		private String nicName;
		private String opScope;
		private String province;
		private String recCap;
		private String recCapCur;
		private String regCap;
		private String regCapital;
		private String regCapitalCurrency;
		private String regNumber;
		private String regOrg;
		private String regStatus;
		private String revDate;
		private StaffList staffList;
		private String taxLevel;
		private String tel;
		private String type;
	}

	@Data
	public static class StaffList {
		private List<StaffResult> result;
	}

	@Data
	public static class StaffResult {
		private String name;
		private String type;
		private List<String> typeJoin;
	}

	@Data
	public static class StockHolderItem {
		private String acconam;
		private String confrom;
		private String currency;
		private String investDate;
		private String investRate;
		private String orgHolderName;
		private String orgHolderType;
		private String subscriptAmt;
	}
}
