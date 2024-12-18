package com.mkc.api.dto.bg;

import com.mkc.api.dto.BaseDTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
public class HouseInfoReqVo extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MultipartFile> files;

    private List<String>  types;

    private List<PersonInfoReqVo> persons;

    private String personsStr;
}
