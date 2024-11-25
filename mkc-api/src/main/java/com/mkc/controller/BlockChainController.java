package com.mkc.controller;

import com.mkc.dto.sddw.AuthInfoGetDTO;
import com.mkc.dto.sddw.AuthInfoPostDTP;
import com.mkc.dto.sddw.ProductDataGetDTO;
import com.mkc.service.SddwBlockChainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/11/25 14:55
 */
@Slf4j
@RestController
@RequestMapping("/bc")
public class BlockChainController {

	@Resource
	private SddwBlockChainService sddwBlockChainService;

	@PostMapping("/query_auth_info")
	public String queryAuthInfo(@Valid AuthInfoGetDTO dto) {

		return sddwBlockChainService.queryAuthInfo(dto);
	}

	@PostMapping("/apply_auth_info")
	public String applyAuthInfo(@Valid AuthInfoPostDTP dto) {

		return sddwBlockChainService.insertAuthPerm(dto);
	}

	@PostMapping("/query_data")
	public String test(@Valid ProductDataGetDTO dto) {

		return sddwBlockChainService.queryData(dto);
	}

}
