package com.mkc.framework.web.service;

import com.mkc.system.service.ISysConfigService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RuoYi首创 html调用 thymeleaf 实现参数管理
 * 
 * @author  atd
 */
@Service("config")
public class ConfigService
{
	@Autowired
    private ISysConfigService configService;
	@Autowired
	private Environment env;

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String getKey(String configKey)
    {
    	String value = configService.selectConfigByKey(configKey);
    	if(StringUtils.isBlank(value)) {
    		value = env.getProperty(configKey, StringUtils.EMPTY);
    	}
        return value;
    }
}
