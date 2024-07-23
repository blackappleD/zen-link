package com.mkc.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.api.common.constant.ProductPrivacyKey;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.common.exception.ErrMonitorCode;
import com.mkc.bean.MerReqLogBean;
import com.mkc.common.core.text.Convert;
import com.mkc.common.enums.ReqState;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.domain.MerReqLog;
import com.mkc.mapper.MerReqLogMapper;
import com.mkc.service.IMerReqLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商户调用日志Service业务层处理
 * 
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@DS("business")
@Service
public class MerReqLogServiceImpl implements IMerReqLogService 
{
    @Autowired
    private MerReqLogMapper merReqLogMapper;

    /**
     * 查询商户调用日志
     * 
     * @param id 商户调用日志主键
     * @return 商户调用日志
     */
    @Override
    public MerReqLog selectMerReqLogById(Long id)
    {
        return merReqLogMapper.selectMerReqLogById(id);
    }

    @Override
    public MerReqLog selectMerReqLogByOrderNo(String orderNo) {
        return merReqLogMapper.selectMerReqLogByOrderNo(orderNo);
    }

    /**
     * 查询商户调用日志列表
     * 
     * @param merReqLog 商户调用日志
     * @return 商户调用日志
     */
    @Override
    public List<MerReqLog> selectMerReqLogList(MerReqLogBean merReqLog)
    {
        return merReqLogMapper.selectMerReqLogList(merReqLog);
    }


    /**
     * 修改商户调用日志
     * 
     * @param merReqLog 商户调用日志
     * @return 结果
     */
    @Override
    public int updateMerReqLog(MerReqLog merReqLog)
    {
        return merReqLogMapper.updateMerReqLog(merReqLog);
    }

    /**
     * 批量删除商户调用日志
     * 
     * @param Ids 需要删除的商户调用日志主键
     * @return 结果
     */
    @Override
    public int deleteMerReqLogByIds(String Ids)
    {
        return merReqLogMapper.deleteMerReqLogByIds(Convert.toIntArray(Ids));
    }

    /**
     * 删除商户调用日志信息
     * 
     * @param id 商户调用日志主键
     * @return 结果
     */
    @Override
    public int deleteMerReqLogById(Long id)
    {
        return merReqLogMapper.deleteMerReqLogById(id);
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW,rollbackFor= Exception.class)
    public void saveMerLog(MerReqLog merLog){

        try {

            LocalDateTime reqTime = merLog.getReqTime();
            LocalDateTime respTime = merLog.getRespTime();
            if (respTime == null) {
                respTime=LocalDateTime.now();
                merLog.setRespTime(respTime);
            }
            //计算处理花费时长
            long millis = Duration.between(reqTime, respTime).toMillis();
            merLog.setTotalTime(millis);
            merLog.setCreateTime(LocalDateTime.now());

          //  log.info("============== merLog {}",merLog);



            String reqJson = merLog.getReqJson();
            String respJson = merLog.getRespJson();
            if(StringUtils.isNotBlank(respJson) && ReqState.SUCCESS.getCode().equals(merLog.getStatus())){
                String respJsonGzip = ZipStrUtils.gzip(respJson);
                merLog.setRespJson(respJsonGzip);
            }
            //进行敏感字段脱密
            if(StringUtils.isNotBlank(reqJson)){
                JSONObject privacyStr = ProductPrivacyKey.privacyMer(JSON.parseObject(reqJson));
                merLog.setReqJson(privacyStr.toJSONString());
            }

            merReqLogMapper.insert(merLog);

            log.info("保存成功 merLogHandleQueue",merLog.getOrderNo());

        }catch (Exception e){
            log.error(ErrMonitorCode.BIZ_ERR+" 保存商户请求日志 发生异常 "+merLog,e);
            throw new ApiServiceException("保存商户请求日志失败");
        }

    }

//    自然人
//    运营商
//    司法
//    工商
//    交通

}
