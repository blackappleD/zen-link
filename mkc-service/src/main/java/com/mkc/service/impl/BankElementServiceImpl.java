package com.mkc.service.impl;

import com.mkc.common.utils.bean.BeanUtils;
import com.mkc.domain.BankElement;
import com.mkc.mapper.BankElementMapper;
import com.mkc.service.BankElementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/3/12 14:29
 */
@Service
public class BankElementServiceImpl implements BankElementService {

	@Resource
	private BankElementMapper bankElementMapper;

	@Override
	public int insert(BankElement bankElement) {
		return bankElementMapper.insert(bankElement);
	}

	@Override
	public int insert(Object params, Object responseBody) {
		BankElement bankElement = new BankElement();
		BeanUtils.copyProperties(responseBody, bankElement);
		BeanUtils.copyProperties(params, bankElement);
		return insert(bankElement);
	}
}
