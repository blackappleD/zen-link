package com.mkc.api.dto.bg.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/14 11:51
 */
@Data
public class SsPlusResDTO {

	private String crc;
	private Count count;
	private Object civil;
	private Criminal criminal;
	private Object administrative;
	private Object preservation;
	private Object implement;
	private Object bankrupt;
	@JsonProperty("cases_tree")
	private CasesTree casesTree;

	@Data
	public static class Count {
		@JsonProperty("count_total")
		private Integer countTotal;
		@JsonProperty("count_wei_total")
		private Integer countWeiTotal;
		@JsonProperty("count_jie_total")
		private Integer countJieTotal;
		@JsonProperty("count_yuangao")
		private Integer countYuangao;
		@JsonProperty("count_wei_yuangao")
		private Integer countWeiYuangao;
		@JsonProperty("count_jie_yuangao")
		private Integer countJieYuangao;
		@JsonProperty("count_beigao")
		private Integer countBeigao;
		@JsonProperty("count_wei_beigao")
		private Integer countWeiBeigao;
		@JsonProperty("count_jie_beigao")
		private Integer countJieBeigao;
		@JsonProperty("count_other")
		private Integer countOther;
		@JsonProperty("count_wei_other")
		private Integer countWeiOther;
		@JsonProperty("count_jie_other")
		private Integer countJieOther;
		@JsonProperty("money_total")
		private Integer moneyTotal;
		@JsonProperty("money_wei_total")
		private Integer moneyWeiTotal;
		@JsonProperty("money_wei_percent")
		private Object moneyWeiPercent;
		@JsonProperty("money_jie_total")
		private Integer moneyJieTotal;
		@JsonProperty("money_yuangao")
		private Integer moneyYuangao;
		@JsonProperty("money_wei_yuangao")
		private Integer moneyWeiYuangao;
		@JsonProperty("money_jie_yuangao")
		private Integer moneyJieYuangao;
		@JsonProperty("money_beigao")
		private Integer moneyBeigao;
		@JsonProperty("money_wei_beigao")
		private Integer moneyWeiBeigao;
		@JsonProperty("money_jie_beigao")
		private Integer moneyJieBeigao;
		@JsonProperty("money_other")
		private Integer moneyOther;
		@JsonProperty("money_wei_other")
		private Integer moneyWeiOther;
		@JsonProperty("money_jie_other")
		private Integer moneyJieOther;
		@JsonProperty("ay_stat")
		private String ayStat;
		@JsonProperty("larq_stat")
		private String larqStat;
		@JsonProperty("area_stat")
		private String areaStat;
		@JsonProperty("jafs_stat")
		private String jafsStat;
	}

	@Data
	public static class Criminal {
		private Count count;
		private List<CriminalCase> cases;
	}

	@Data
	public static class CriminalCase {
		@JsonProperty("n_ajlx")
		private String nAjlx;
		@JsonProperty("c_ah")
		private String cAh;
		@JsonProperty("c_ah_ys")
		private String cAhYs;
		@JsonProperty("c_ah_hx")
		private String cAhHx;
		@JsonProperty("n_ajbs")
		private String nAjbs;
		@JsonProperty("n_jbfy")
		private String nJbfy;
		@JsonProperty("n_jbfy_cj")
		private String nJbfyCj;
		@JsonProperty("n_ajjzjd")
		private String nAjjzjd;
		@JsonProperty("n_slcx")
		private String nSlcx;
		@JsonProperty("c_ssdy")
		private String cSsdy;
		@JsonProperty("d_larq")
		private String dLarq;
		@JsonProperty("n_laay")
		private String nLaay;
		// ... 其他字段省略，按相同模式添加
		@JsonProperty("c_dsrxx")
		private List<Dsrxx> cDsrxx;
	}

	@Data
	public static class Dsrxx {
		@JsonProperty("c_mc")
		private String cMc;
		@JsonProperty("n_dsrlx")
		private String nDsrlx;
		@JsonProperty("n_ssdw")
		private String nSsdw;
	}

	@Data
	public static class CasesTree {
		private Object civil;
		private List<CriminalTree> criminal;
		private Object administrative;
		private Object preservation;
		private Object implement;
		private Object bankrupt;
	}

	@Data
	public static class CriminalTree {
		@JsonProperty("c_ah")
		private String cAh;
		@JsonProperty("n_ajbs")
		private String nAjbs;
		@JsonProperty("stage_type")
		private Integer stageType;
		@JsonProperty("case_type")
		private Integer caseType;
		private Object next;
	}
}
