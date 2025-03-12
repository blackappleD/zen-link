package com.mkc.service;

import com.mkc.domain.BankElement;

/**
 * @author tqlei
 * @date 2023/11/15 17:15
 */

public interface BankElementService {

	int insert(BankElement bankElement);

	int insert(Object params, Object responseBody);

}
