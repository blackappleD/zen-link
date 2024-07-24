package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 *车五项信息查询
 * @author tqlei
 * @date 2023/5/26 10:33
 */

@Data
public class CarInfoReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车牌号
     */
    private String plateNo;

    /**
     * 车辆类型
     */
    private String plateType;





}
