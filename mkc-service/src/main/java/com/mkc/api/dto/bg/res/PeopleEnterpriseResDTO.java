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
	private ResultInfo resultInfo;
	private String reportNo;
	private String reportTime;
	private String resultStatus;

	@Data
	// ResultInfo类
	public class ResultInfo {
		private List<CompanyData> datalist;
		private int total;

		// getter 和 setter
	}

	@Data
	// 公司数据类
	public class CompanyData {
		private String fSource;
		private String orgName;
		private String pName;
		private StockHolderItem stockHolderItem;
		private List<String> relationship;
		private BasicInfo basicInfo;

		// getter 和 setter
	}

	@Data
	// 股东信息类
	public class StockHolderItem {
		private String orgHolderType;
		private String investDate;
		private String investRate;
		private String subscriptAmt;
		private String currency;
		private String confrom;
		private String orgHolderName;
		private String acconam;

		// getter 和 setter
	}

	@Data
	// 基本信息类
	public class BasicInfo {
		private String nicName;
		private String opScope;
		private String companyOrgTypeCode;
		private String regStatus;
		private String dom;
		private String regCapital;
		private String city;
		private String recCapCur;
		private String industry;
		private StaffList staffList;
		private String type;
		private String revDate;
		private String industryCode;
		private String legalPersonName;
		private String regNumber;
		private String creditCode;
		private String empNum;
		private String province;
		private String companyOrgType;
		private String regCap;
		private String tel;
		private String email;
		private String canDate;
		private String estiblishTime;
		private String recCap;
		private String apprDate;
		private String regCapitalCurrency;
		private String taxLevel;
		private String isListed;
		private String district;
		private String regOrg;
		private String name;
		private String nicCode;
		private String base;

		// getter 和 setter
	}

	@Data
	// 员工列表类
	public class StaffList {
		private List<StaffMember> result;

		// getter 和 setter
	}

	@Data
	// 员工成员类
	public class StaffMember {
		private String name;
		private String type;
		private List<String> typeJoin;

		// getter 和 setter
	}
}
