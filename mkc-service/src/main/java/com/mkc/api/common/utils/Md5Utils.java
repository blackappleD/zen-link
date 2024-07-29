package com.mkc.api.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author tqlei
 * @date 2023/6/17 19:17
 */

public class Md5Utils {

    public static String md5(String text)  {

        return  DigestUtils.md5Hex(text);
    }

    public static void main(String[] args) {
       // System.out.println(md5("123456"));
// String s = "merCode[{\"name\":\"bbb\", \"cardNum\":\"22222\"},{\"name\":\"aaa\",\"cardNum\":\"11111\"}]pwd";
       // String s = "BJYRZXKJ[{\"name\":\"bbb\", \"cardNum\":\"22222\"},{\"name\":\"aaa\",\"cardNum\":\"11111\"}]pwd";
        //String s = "BJYRZXKJ5f80c677753b409280a0a491bda9941e3ac730b23371d4fac4f410c1af5bedfa";
//        String s = "BJYRZXKJ5f80c677753b409280a0a491bda9941e1db60213a1b34aaac057d775cfdf56e9";
//        String s = "BJYRZXKJ[{\"name\":\"bbb\", \"cardNum\":\"22222\"},{\"name\":\"aaa\",\"cardNum\":\"11111\"}]1db60213a1b34aaac057d775cfdf56e9";
//        System.out.println(md5(s));

//        String s = "YRZX[{\"name\":\"bbb\", \"cardNum\":\"22222\"},{\"name\":\"aaa\",\"cardNum\":\"11111\"}]3a3c7fad69b96f95a8e82630119f08a1";
//        System.out.println(md5(s));
//        String s = "YRZX5f80c677753b409280a0a491bda9941e3a3c7fad69b96f95a8e82630119f08a1";
//        System.out.println(md5(s));

//        String s = "BJYRZXKJ[{\"name\":\"孙秀容\",\"cardNum\":\"330125195609065528\"},{\"name\":\"鲍广斌\",\"cardNum\":\"340223197812197415\"}]3ac730b23371d4fac4f410c1af5bedfa";
//        System.out.println(md5(s));

        String s = "BJYRZXKJ[{\"name\":\"孙秀容\",\"cardNum\":\"330125195609065528\"},{\"name\":\"鲍广斌\",\"cardNum\":\"340223197812197415\"},{\"name\":\"杨汉荣\",\"cardNum\":\"532123197809084710\"},{\"name\":\"李红姣\",\"cardNum\":\"410526198004063046\"},{\"name\":\"何依楠\",\"cardNum\":\"140110199510060020\"},{\"name\":\"吴旭艳\",\"cardNum\":\"140104197001210020\"},{\"name\":\"朱宝桥\",\"cardNum\":\"612401198201151471\"},{\"name\":\"贺苗苗\",\"cardNum\":\"330902198207280025\"},{\"name\":\"施卫彬\",\"cardNum\":\"310230197010286473\"}]3ac730b23371d4fac4f410c1af5bedfa";
        System.out.println(md5(s));


    }
}
