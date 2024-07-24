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
    CK_MOB_THREE_10010("CK_003_10010", "个人手机三要素认证-联通","/ck/mobileThree"){},

    BG_CAR_INFO("BG_CAR_001", "车五项信息查询","/ck/mobileThree"){},

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
