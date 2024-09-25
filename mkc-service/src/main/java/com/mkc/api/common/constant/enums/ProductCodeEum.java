package com.mkc.api.common.constant.enums;

/**
 * 标准产品 code 枚举类
 * @author tqlei
 * @date 2023/5/25 15:55
 */

public enum ProductCodeEum {





    CK_CRE_TWO("CK_002", "个人身份二要素认证","/ck/certIDverify"){},


    CK_MOB_THREE("CK_003", "个人手机三要素认证","/ck/mobileThree"){},
    CK_MOB_THREE_10000("CK_003_10000", "个人手机三要素认证-电信","/ck/mobileThree"){},
    CK_MOB_THREE_10086("CK_003_10086", "个人手机三要素认证-移动","/ck/mobileThree"){},
    CK_MOB_THREE_10010("CK_003_10010", "个人手机三要素认证-联通","/bg/mobileThree"){},
    CK_PERSON_CAR("CK_PERSONCAR_001", "人车核验","/ck/personCarVerify"){},
    CK_WORK_UNIT("CK_WORKUNIT_001", "工作单位核验","/ck/workUnitVerify"){},
    CK_POPULATION_THREE("CK_POPULATION_THREE_001", "全国⼈⼝身份信息三要素核验","/ck/populationThree"){},

    BG_CAR_INFO("BG_CAR_001", "车五项信息查询","/bg/carInfo"){},

    BG_VEHICLE_LICENSE_INFO("BG_VEHICLE_LICENSE_001", "行驶证信息查询","/bg/vehicleLicenseInfo"){},

    BG_ECONOMIC_RATE_INFO("BG_ECONOMIC_RATE_001", "经济能力评级","/bg/economicRate"){},

    BG_HOUSE_INFO("BG_HOUSE_001", "不动产信息查询","/bg/houseInfo"){},

    BG_HOUSE_RESULT_INFO("BG_HOUSE_002", "不动产结果信息查询","/bg/houseResultInfo"){},

    BG_FINANCE_INFO("BG_FINANCE_001", "经济能力2W查询","/bg/financeInfo"){},

    BG_FINANCE_INFO_V3("BG_FINANCE_002", "经济能力评级V3","/bg/financeInfoV3"){},

    BG_SURE_SCORE_INFO("BG_SURE_SCORE_001", "确信分","/bg/sureScoreInfo"){},

    BG_PERSON_CAR_INFO("BG_PERSON_CAR_001", "人车核验详版","/bg/personCarInfo"){},

    BG_ENTERPRISE_FOUR_ELEMENT_INFO("BG_ENTERPRISE_FOUR_ELEMENT_001", "企业四要素","/bg/enterpriseFourElementInfo"){},

    BG_EDUCATION_INFO("BG_EDUCATION_001", "全国高等学历信息查询","/bg/educationInfo"){},

    BG_HIGH_SCHOOL_EDUCATION_INFO("BG_HIGH_SCHOOL_EDUCATION_001", "高校学历核查接口","/bg/highSchoolEduInfo"){},

    BG_DRIVING_LICENSE_INFO("BG_DRIVING_LICENSE_001", "行驶身份核验","/bg/drivingLicense"){},

    SF_DISHONEST_EXECUTIVE_INFO("SF_DISHONEST_EXECUTIVE_001", "失信被执行人","/sf/dishonestExecutive"){},

    SF_RESTRICTED_CONSUMER_INFO("SF_RESTRICTED_CONSUMER_001", "限制高消费被执行人接口","/sf/restrictedConsumer"){},
    ;





    ProductCodeEum(String code, String name,String url) {
        this.code = code;
        this.name = name;
        this.url = url;
    }

    private String code;
    private String name;

    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return " ApiReturnCode{" + "code='" + code + '\'' + "name" + name + '\'' + '}';
    }

    /**
     * 判断当前商品是否需要获取 实际运营商价格
     * @param productCode
     * @return
     */
    public static boolean isGetYysInfo(String productCode) {

        //判断是否 个人手机三要素认证 及详版 不是直接返回

       return CK_MOB_THREE.getCode().equals(productCode)

               ;

    }

    /**
     * 根据名字匹配枚举
     * @param productCode
     * @return 无的时候 默认返回移动
     */
    public static ProductCodeEum findByProductCode(String productCode){
        for (ProductCodeEum value : ProductCodeEum.values()) {
            if(value.getCode().equals(productCode)){
                return value;
            }
        }
        //根据自身的业务 查不到可以返回null，或者抛出异常。
        return null;
    }



}
