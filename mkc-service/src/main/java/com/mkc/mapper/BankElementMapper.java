package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.BankElement;

public interface BankElementMapper extends BaseMapper<BankElement> {

	int insert(BankElement bankElement);

}
