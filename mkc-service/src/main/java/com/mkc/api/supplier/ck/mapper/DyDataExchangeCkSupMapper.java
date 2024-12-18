package com.mkc.api.supplier.ck.mapper;

import com.mkc.api.dto.ck.ProQualifyCertReqDTO;
import com.mkc.api.dto.ck.ProQualifyCertResDTO;
import com.mkc.api.supplier.dto.ProQualifyCertSupReqDTO;
import com.mkc.api.supplier.dto.ProQualifyCertSupResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/17 17:29
 */
@Mapper(componentModel = "spring")
public interface DyDataExchangeCkSupMapper {

	@Mapping(target = "name", source = "dto.AAC003")
	@Mapping(target = "idNum", source = "dto.AAC002")
	@Mapping(target = "profession", source = "dto.ACA111")
	@Mapping(target = "level", source = "dto.AAC015")
	@Mapping(target = "certNum", source = "dto.AHC011")
	@Mapping(target = "issueDate", source = "dto.AAC187")
	ProQualifyCertResDTO supRes2Res(ProQualifyCertSupResDTO dto);

	@Mapping(target = "Authorization", source = "dto.authorization")
	ProQualifyCertSupReqDTO req2SupReq(ProQualifyCertReqDTO dto);


}
