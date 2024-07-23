package com.mkc.bean;

import com.mkc.common.annotation.Excel;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商户批量查询项目对象 t_batch_project
 * 
 * @author tieqiao
 * @date 2023-12-28
 */
@Data
public class BatchProjectBean implements Serializable {

    private static final long serialVersionUID = 1L;


    private MultipartFile uploadFile;



    /** 商户code */
   // @Excel(name = "商户code")
    private String merCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 商户名称 */
    @Excel(name = "商户名称")
    private String merName;

    /** 产品code */
   // @Excel(name = "产品code")
    private String productCode;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchNo;

    /** 产品单价 */
    @Excel(name = "产品单价")
    private BigDecimal productPrice;

    /** 总数量 */
    @Excel(name = "总数量")
    private Integer totalNum;






}
