package com.mkc.api.common.constant;

import com.mkc.api.common.constant.enums.YysCode;
import com.mkc.api.common.exception.ApiServiceException;

/**
 * @author tqlei
 * @date 2024|2|18 9:34
 */

public class OperatorInfo {

	/**
	 * 一/移动：
	 * ①支持号段：
	 * 134（0～8）/135/136/137/138/139/147/150/151/152/157/158/159/172/178/182/183/184/187/188/195/198
	 * ②不支持号段：144/148/165/1703/1705/1706/197
	 * <p>
	 * 二/联通：
	 * ①支持号段：130/131/132/145/155/156/166/175/176/185/186
	 * ②不支持号段：140/146/163/167/171/1704/1707/1708/1709/192/196
	 * <p>
	 * 三/电信：
	 * ①支持号段：133/1349/141/149/153/173/174/177/180/181/189/191/199/190/193。
	 * 不支持号段 1410/168/169/162/1700/1701/1702
	 */

	//  public final static String YD="^(134[0-8][0-9]{7})|([135|136|137|138|139|147|150|151|152|157|158|159|172|178|182|183|184|187|188|195|198][0-9]{8})$";
	//移动
	public final static String YD = "^(134[0-8][0-9]{7})|(13[5-9]|147|15[0-9]|17[28]|18[23478]|19[58])[0-9]{8}$";
	//联通
	public final static String LT = "(13[0-2]|145|15[56]|166|17[56]|18[56])[0-9]{8}$";
	//电信
	public final static String DX = "^(1349[0-9]{7})|(133|141|149|153|17[347]|18[019]|19[0139])[0-9]{8}$";

	//虚拟号段 或者 物联网号段 等 不能核验的
	public final static String XL_WL = "144|148|165|170|197|140|146|163|167|171|192|196|168|169|162";


	public static YysCode getPhoneOperator(String mobile) {

		if (mobile.matches(YD)) {
			return YysCode.CM;
		} else if (mobile.matches(LT)) {
			return YysCode.CU;
		} else if (mobile.matches(DX)) {
			return YysCode.CT;
		} else {
			String phonePrefix = mobile.substring(0, 3);
			if (XL_WL.contains(phonePrefix)) {
				return YysCode.CM;
			}
			throw new ApiServiceException("没有查询到号段所属运营商");
		}
	}

	public static void main(String[] args) {
		String mobile = "14221125680";
		System.out.println(getPhoneOperator(mobile));
	}

}
