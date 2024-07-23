package com.mkc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mkc.service.IMerInfoService;

@SpringBootTest
public class MkcAdminTest {
	
	@Autowired
	private IMerInfoService merInfoService;

	@Test
	public void testSelectMerInfoByCode() {
		System.out.println(merInfoService.selectMerInfoByCode("AtdCpTest"));
	}
	
}
