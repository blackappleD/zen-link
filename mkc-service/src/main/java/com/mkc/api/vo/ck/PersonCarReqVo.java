package com.mkc.api.vo.ck;

import com.mkc.api.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiewei
 * @date 2024/08/02 16:06
 */
@Data
public class PersonCarReqVo extends BaseVo implements Serializable {

    private String name;

    private String plateNo;

}
