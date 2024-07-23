package com.mkc.bean.ent;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tqlei
 * @date 2024/3/25 13:39
 */

@Data
public class EntityInfoBean implements Serializable {

    /**
     * 公司名称
     */
    private String entityName;

    /**
     * 法人名称
     */
    private String legalName;

    /**
     * 统一社会信用代码
     */
    private String usCreditCode;



    /**
     * 营业期限开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessStartTime;

    /**
     * 营业期限终止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessEndTime;

    /**
     * 企业状态
     */
    private String entityStatus;

}
