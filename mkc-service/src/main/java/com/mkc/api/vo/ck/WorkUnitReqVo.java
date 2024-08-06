package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiewei
 * @date 2024/08/05 9:52
 */
@Data
public class WorkUnitReqVo extends BaseVo implements Serializable {

    private String name;

    private String cid;

    private String workplace;



}
