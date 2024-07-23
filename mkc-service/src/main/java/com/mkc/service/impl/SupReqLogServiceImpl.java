package com.mkc.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.api.common.constant.ProductPrivacyKey;
import com.mkc.bean.SupReqLogBean;
import com.mkc.common.core.text.Convert;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.domain.SupReqLog;
import com.mkc.mapper.SupReqLogMapper;
import com.mkc.service.ISupReqLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * 调用供应商日志Service业务层处理
 * 
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@DS("business")
@Service
public class SupReqLogServiceImpl implements ISupReqLogService 
{
    @Autowired
    private SupReqLogMapper supReqLogMapper;

    /**
     * 查询调用供应商日志
     * 
     * @param id 调用供应商日志主键
     * @return 调用供应商日志
     */
    @Override
    public SupReqLog selectSupReqLogById(Long id)
    {
        return supReqLogMapper.selectSupReqLogById(id);
    }

    /**
     * 查询调用供应商日志列表
     * 
     * @param supReqLog 调用供应商日志
     * @return 调用供应商日志
     */
    @Override
    public List<SupReqLog> selectSupReqLogList(SupReqLogBean supReqLog)
    {
        return supReqLogMapper.selectSupReqLogList(supReqLog);
    }

    /**
     * 新增调用供应商日志
     * 
     * @param supReqLog 调用供应商日志
     * @return 结果
     */
    @Override
    public int insertSupReqLog(SupReqLog supReqLog)
    {

        supReqLog.setCreateTime(LocalDateTime.now());
        String respJson = supReqLog.getRespJson();
        String reqJson = supReqLog.getReqJson();
        //压缩响应报文
        if(StringUtils.isNotBlank(respJson) && ReqState.SUCCESS.getCode().equals(supReqLog.getStatus())){
            String respJsonGzip = ZipStrUtils.gzip(respJson);
            supReqLog.setRespJson(respJsonGzip);
        }

        //进行敏感字段脱密
        if(StringUtils.isNotBlank(reqJson)){
            JSONObject privacyStr = ProductPrivacyKey.privacySup(JSON.parseObject(reqJson));
            supReqLog.setReqJson(privacyStr.toJSONString());
        }

        return supReqLogMapper.insert(supReqLog);
    }

    /**
     * 修改调用供应商日志
     * 
     * @param supReqLog 调用供应商日志
     * @return 结果
     */
    @Override
    public int updateSupReqLog(SupReqLog supReqLog)
    {
        return supReqLogMapper.updateSupReqLog(supReqLog);
    }

    /**
     * 批量删除调用供应商日志
     * 
     * @param Ids 需要删除的调用供应商日志主键
     * @return 结果
     */
    @Override
    public int deleteSupReqLogByIds(String Ids)
    {
        return supReqLogMapper.deleteSupReqLogByIds(Convert.toIntArray(Ids));
    }

    /**
     * 删除调用供应商日志信息
     * 
     * @param id 调用供应商日志主键
     * @return 结果
     */
    @Override
    public int deleteSupReqLogById(Long id)
    {
        return supReqLogMapper.deleteSupReqLogById(id);
    }
}
