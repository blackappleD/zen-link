package com.mkc.api.dto.ck;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @AUTHOR XIEWEI
 * @Date 2024/8/21 17:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PopulationTwoReqDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "身份证号不能为空")
    private String idcard;

    private String authorization;

}
