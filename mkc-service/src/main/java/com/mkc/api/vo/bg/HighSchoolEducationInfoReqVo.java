package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XIEWEI
 * @date 2024/9/25 11:46
 */
@Data
public class HighSchoolEducationInfoReqVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String xm;

    private String zsbh;
}
