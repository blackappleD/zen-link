package com.mkc.tool;

import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 正则工具类
 *
 * @author tqlei
 * @date 2023/5/24 16:52
 */


public class RegTool {

    private final static String REG_MOB = "^1[3-9]\\d{9}$";
    private final static String REG_NAME = "^[\\u4E00-\\u9FA5]{1,10}(·?)[\\u4E00-\\u9FA5]{1,10}$";
    private final static String REG_MD5 = "^[0-9,a-f,A-F]{32}$";
    private final static String REG_SHA256 = "^[0-9,a-f,A-F]{64}$";
    private final static String REG_CARDNO_18 = "^[1-8][0-9]\\d{4}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
   // private final static String REG_CARDNO_18 = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}([0-9]|X|x)$";
   // private final static String REG_CARDNO_15 = "^[1-8][0-9]\\d{6}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$";
    private final static String REG_CARDNO_15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
//6 2012373 03 11 091

    private final static String REG_VIN = "^[0-9,A-Z,a-z]{17}$";

    /**航班号*/
    private final static String REG_FLIGHTNUMBER = "^[A-Z\\d]{2}\\d{3,4}$";

    /** ip 地址*/
    private final static String REG_IP ="^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";



   // private final static String REG_PLATE_NUMBER = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4,5}[A-Z0-9挂学警港澳]{1}$";
    //private final static String REG_PLATE_NUMBER = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Za-z]{1}[A-Za-z0-9]{4,5}[A-Za-z0-9挂学警]{1}$";

    public static boolean checkPlateNumber(String plateNumber) {

        if (StringUtils.isBlank(plateNumber)) {
            return false;
        }
        for (VehiclePlateNoEnum vehiclePlateNoEnum : VehiclePlateNoEnum.values()) {

            Matcher matcher = vehiclePlateNoEnum.getPattern().matcher(plateNumber);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
    public static boolean ck_flightNumber(String flightNumber) {

        if (StringUtils.isBlank(flightNumber)) {
            return false;
        }
        if (!flightNumber.matches(REG_FLIGHTNUMBER)) {
            return false;
        }
        return true;
    }
    public static boolean checkVin(String vin) {

        if (StringUtils.isBlank(vin)) {
            return false;
        }
        if (!vin.matches(REG_VIN)) {
            return false;
        }
        return true;
    }

    public static boolean ckDate_YYYYMMDD(String date){
        try {
            DateUtils.parseDate(date,DateUtils.YYYYMMDD);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public static boolean ckDate(String date,String dateType){
        try {
            DateUtils.parseDate(date,dateType);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    public static boolean checkIpv4(String ipv4) {

        if (StringUtils.isBlank(ipv4)) {
            return false;
        }
        if (!ipv4.matches(REG_IP)) {
            return false;
        }
        return true;
    }
    public static boolean checkMobile(String mobile) {

        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        if (!mobile.matches(REG_MOB)) {
            return false;
        }

        return true;
    }


    public static boolean checkCertNo(String certNo) {

        if (StringUtils.isBlank(certNo)) {
            return false;
        }

        //18位身份证
        if(certNo.matches(REG_CARDNO_18)){
            if (!checkProv(certNo)) {
                return false;
            }
            try {
                char[] charArray = certNo.toCharArray();
                //前十七位加权因子
                int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                //这是除以11后，可能产生的11位余数对应的验证码
                String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                int sum = 0;
                for (int i = 0; i < idCardWi.length; i++) {
                    int current = Integer.parseInt(String.valueOf(charArray[i]));
                    int count = current * idCardWi[i];
                    sum += count;
                }
                char idCardLast = charArray[17];
                int idCardMod = sum % 11;
                if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                    return true;
                }

                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            //15位身份证
        }else if (certNo.matches(REG_CARDNO_15)) {
            return checkProv(certNo);
        }

        return false;
    }
    public static boolean checkCertName(String certName) {

        if (StringUtils.isBlank(certName)) {
            return false;
        }
        if (!certName.matches(REG_NAME)) {
            return false;
        }
        return true;
    }
    public static boolean checkMd5(String str) {

        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (!str.matches(REG_MD5)) {
            return false;
        }
        return true;
    }
    public static boolean ckSha256(String str) {

        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (!str.matches(REG_SHA256)) {
            return false;
        }
        return true;
    }



    // 省份数据
    private static Map<Integer, String> provs = new HashMap<>();

    static {
        provs.put(11, "北京");
        provs.put(12, "天津");
        provs.put(13, "河北");
        provs.put(14, "山西");
        provs.put(15, "内蒙古");
        provs.put(21, "辽宁");
        provs.put(22, "吉林");
        provs.put(23, "黑龙江");
        provs.put(31, "上海");
        provs.put(32, "江苏");
        provs.put(33, "浙江");
        provs.put(34, "安徽");
        provs.put(35, "福建");
        provs.put(36, "江西");
        provs.put(37, "山东");
        provs.put(41, "河南");
        provs.put(42, "湖北 ");
        provs.put(43, "湖南");
        provs.put(44, "广东");
        provs.put(45, "广西");
        provs.put(46, "海南");
        provs.put(50, "重庆");
        provs.put(51, "四川");
        provs.put(52, "贵州");
        provs.put(53, "云南");
        provs.put(54, "西藏 ");
        provs.put(61, "陕西");
        provs.put(62, "甘肃");
        provs.put(63, "青海");
        provs.put(64, "宁夏");
        provs.put(65, "新疆");
        provs.put(71, "台湾");
        provs.put(81, "香港");
        provs.put(82, "澳门");
        provs.put(83, "台湾");

    }

    // 检查省份
    private static boolean checkProv(String certNo) {
       String prov= certNo.substring(0, 2);
        return provs.containsKey(Integer.parseInt(prov));
    }


    /**
     * 将15位的转成18的身份证
     * @param SId
     * @return
     */
    public static String conversion15_18(String SId) {
        StringBuilder sb = new StringBuilder(SId);
        sb.insert(6, "19");

        String s = sb.toString();

        final int[] WEIGHTING_FACTOR = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; // 加权因子
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int a = s.charAt(i) - 48;
            int b = WEIGHTING_FACTOR[i];
            sum += a * b;
        }
        int checkNum = sum % 11;

        String[] CHECK_CODE = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"}; // 校验码

        return s + CHECK_CODE[checkNum];
    }







    /**
     * 校验银行卡卡号
     *
     * @param bankCard
     * @return
     */
    public static boolean checkBankCard(String bankCard) {

        try {
            char bit = getBankCardCheckCode(bankCard
                    .substring(0, bankCard.length() - 1));
            return bankCard.charAt(bankCard.length() - 1) == bit;
        }catch (Exception e){

            return false;
        }

    }
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if(StringUtils.isBlank(nonCheckCodeCardId)){
            throw new IllegalArgumentException("不是银行卡的卡号!");
        }
        int cardLenth = nonCheckCodeCardId.trim().length();
        if (cardLenth == 0
                || !nonCheckCodeCardId.matches("^[0-9]{15,19}$")) {
            throw new IllegalArgumentException("不是银行卡的卡号!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }








    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println("ckbank  "+RegTool.checkBankCard("6214832103120668"));

        System.out.println(" id card: "+checkCertNo("522730500405062316"));
        System.out.println(" id card: "+checkCertNo("43290219790903871x"));

//        System.out.println(" mobile : "+checkMobile("13621123698"));
//        System.out.println(" name: "+checkCertName("阿布木艾山"));
//        System.out.println(" id card: "+checkCertNo("432902790903881"));
//        System.out.println(" idcard: "+conversion15_18("432902790903881"));
//        System.out.println(" car: "+checkPlateNumber("贵sfs65ty"));
//        System.out.println(" bankcard: "+checkBankCard("6217003810026896707"));
//        System.out.println("md5_1 : "+checkMd5("e10adc3949ba59abbe56e057f20f883e"));
//        System.out.println("md5_2 : "+checkMd5(Md5Utils.md5("1234")));

        System.out.println("checkIpv4 : "+checkIpv4("1.333.1.1"));
        System.out.println("checkMd5 : "+checkMd5("C969ED4A82A5FD796530B96EA45CE52E"));
    }

}
