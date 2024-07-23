package com.mkc.common.constant;

/**
 * @author tqlei
 * @date 2023/5/15 16:57
 */

public interface  RedisKey {

    public static Integer DEFAULT_OUTTIME=60;



    public static final String PREFIX = "xh_";
    public static final String ZYMX_PREFIX = "zymx:";
    public static final String SUPPLIER_KEY ="xh_supplier:Info:";
    public static final String MERINFO_KEY ="xh_mer:Info:";
    public static final String MER_KEY ="xh_mer:";
    public static final String PRODUCT_KEY ="xh_product:Info:";

    public static final String PRODUCT_ALL_KEY ="xh_product:";

    public final String MERINFO_API_KEY ="xh_mer:apiInfo:";
    public static final String MER_BALANCE_KEY = PREFIX+"mer:BALANCE_";
    //商户账户余额预警 KEY
    public static final String MER_BALANCE_WARN_KEY = PREFIX+"mer:BALANCE_WARN_";
    //商户账户余额不足预警 KEY
    public static final String MER_NO_BALANCE_WARN_KEY = PREFIX+"mer:NO_BALANCE_";
    public static final String MER_PRODUCT_SELL_KEY = PREFIX+"mer:prodSell";
    /**供应商产品缓存*/
    public static final String SUP_PRODUCT_KEY = "xh_supplier:prod";
    public static final String SUP_TOKEN_KEY = "xh_supplier:token";



    public static final String DING_TIMES_KEY = "ding:times:";

    /**
     * 报表key
     */
    public static final String XH_REPORT_PREFIX = "xh:report";

    public static final String MER_REPORT_PREFIX = XH_REPORT_PREFIX+":merStat";
    public static final String SUP_REPORT_PREFIX = XH_REPORT_PREFIX+":supStat";


}
