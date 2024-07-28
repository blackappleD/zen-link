package com.mkc.api.vo.bg;

import com.mkc.api.vo.BaseVo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
public class HouseInfoReqVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MultipartFile> files;

    private List<String>  types;

    private List<PersonInfoReqVo> persons;

    private String personsStr;
}
