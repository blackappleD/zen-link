package com.mkc.common.enums;

import com.sun.org.apache.xpath.internal.objects.XNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 是否收费状态码 0 不收费 1收费
 *
 * @author tqlei
 * @date 2023/5/18 17:01
 */


public enum HouseLevelEnum {

    /**
     * 档次一
     */
    A(1, 10),

    /**
     * 档次二
     */
    B(2, 8),

    /**
     * 档次三
     */
    C(3, 5),

    ;


    HouseLevelEnum(Integer level, Integer price) {
        this.level = level;
        this.price = price;
    }

    private Integer level;
    private Integer price;


    public Integer getlevel() {
        return level;
    }

    public void setlevel(Integer level) {
        this.level = level;
    }

    public Integer getprice() {
        return price;
    }

    public void setprice(Integer price) {
        this.price = price;
    }

    public static Integer getLevelByPrice(Integer price) {
        HouseLevelEnum houseLevelEnum = Arrays.stream(HouseLevelEnum.values()).filter(p -> Objects.equals(p.getprice(), price))
                .findFirst().orElse(null);
        return Objects.nonNull(houseLevelEnum) ? houseLevelEnum.getlevel():null;
    }

    private static final List<String> FIRST_CLASS_AREAS = Arrays.asList("京", "沪", "申", "广", "粤");
    private static final List<String> THIRD_CLASS_AREAS = Arrays.asList("陕", "秦", "甘", "陇", "蒙", "琼", "宁", "新", "川", "蜀", "吉", "贵", "黔", "赣");

    /**
     * 一类地区（北京、上海、广州、深圳）
     * 二类地区（除一三类以外的区域）
     * 三类地区（陕西、甘肃、内蒙、海南、宁夏、新疆、四川、吉林、陕西、贵州、江西）
     *
     * @param realEstateCertNo
     * @return
     */
    public static HouseLevelEnum getAreaLevel(String realEstateCertNo) {
        if (StringUtils.isBlank(realEstateCertNo)) {
            return HouseLevelEnum.B;
        }
        if (FIRST_CLASS_AREAS.stream().anyMatch(realEstateCertNo::contains)) {
            return HouseLevelEnum.A;
        } else if (THIRD_CLASS_AREAS.stream().anyMatch(realEstateCertNo::contains)) {
            return HouseLevelEnum.C;
        } else {
            return HouseLevelEnum.B;
        }
    }

}
