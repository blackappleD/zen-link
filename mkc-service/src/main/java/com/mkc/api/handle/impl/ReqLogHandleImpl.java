package com.mkc.api.handle.impl;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.constant.enums.ProductCodeEum;
import com.mkc.api.common.constant.enums.YysCode;
import com.mkc.api.common.constant.enums.YysProductCode;
import com.mkc.api.common.exception.ErrMonitorCode;
import com.mkc.api.handle.ReqLogHandle;
import com.mkc.api.supplier.ISupService;
import com.mkc.api.vo.common.MerReqLogVo;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.domain.MerReqLog;
import com.mkc.domain.ProductSell;
import com.mkc.domain.SupReqLog;
import com.mkc.mq.producer.RabbitProducer;
import com.mkc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tqlei
 * @date 2023/5/24 11:52
 */

@Slf4j
@Service
public class ReqLogHandleImpl implements ReqLogHandle {

    @Autowired
    private ISupReqLogService supReqService;
    @Autowired
    private IMerReqLogService merReqService;
    @Autowired
    private IMerInfoService merInfoService;


    @Autowired
    private ISupplierProductService supplierProductService;

    @Autowired
    private IProductSellService productSellService;

//    @Autowired
//    @Qualifier("logTaskExecutor")
//    private ThreadPoolConf taskPool;

    // 初始化线程池
    private   static   ThreadPoolExecutor logTaskExecutor = new
            ThreadPoolExecutor(8, 60, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(50000), new ThreadPoolExecutor.AbortPolicy());


    @Autowired
    private RabbitProducer rabbitProducer;

    @Override
    public void merReqLogHandle(MerReqLogVo merLog,SupResult supResult) {

        try {

            //异步落地数据
            logTaskExecutor.execute(
                    () -> merLogHandleQueue(merLog,supResult)
            );

        } catch (Throwable e) {
            log.error(ErrMonitorCode.BIZ_ERR+" 流量过大 异步处理 【商户请求日志】 发生异常 merLog = {} , err {}",merLog,e);

        }

    }

    /**
     * 异步落地商户调用日志数据
     * 先用线程池落地，线程池满了 丢入MQ队列去消费
     *
     * @param merLogVo
     */

   // @Async("taskExecutor")
    public void merLogHandleQueue(MerReqLogVo merLogVo,SupResult supResult) {
        MerReqLog merLog=new MerReqLog();
        try {
            BeanUtils.copyProperties(merLogVo,merLog);
            String merCode = merLog.getMerCode();
            if (supResult != null && supResult.isFree()) {
                //判断是否需要设置实际手机号运营商 产品信息
                String newProductCode= getYysProductInfo(merLogVo, supResult);
                if(StringUtils.isNotBlank(newProductCode)){
                    ProductSell productSell = productSellService.selectProductSellByMer(merCode, newProductCode);
                    if (productSell != null) {
                        String productCode = merLog.getProductCode();
                        merLog.setProductCode(productSell.getProductCode());
                        merLog.setProductName(productSell.getProductName());
                        merLog.setSellPrice(productSell.getSellPrice());
                        log.info("merReqLog 设置实际运营商的 产品 成功 old: {}， new: {} , OrderNo {}", productCode, newProductCode,merLog.getOrderNo());
                        String supCode=merLog.getSupCode();

                        SuplierQueryBean newSupQueryBean = supplierProductService.selectSupProductBySupCode(supCode, newProductCode);
                        if (newSupQueryBean != null) {
                            merLog.setInPrice(newSupQueryBean.getInPrice());
                            log.info("merReqLog 产品实际运营商进价设置   成功 old: {}， new: {} ,supCode {} ,orderNo {}", productCode, newProductCode,supCode,merLog.getOrderNo());

                        }else{
                            log.warn(" merReqLog 产品实际运营商进价设置失败 手机三要素核验 相关运营商 产品没有上架 supCode {}, newProductCode {} , orderNo {}", supCode,newProductCode,merLog.getOrderNo());
                        }

                    }else {
                        log.warn("手机三要素核验 相关运营商 产品没有上架 merCode {}, newProductCode {} , orderNo {}", merCode, newProductCode,merLog.getOrderNo());
                    }
                }
                //扣减余额
                merInfoService.dedMerBalance_SYS(merLog);
                merLog.setActualPrice(merLog.getSellPrice());


            }

            //加入rabbitMQ 中
            rabbitProducer.producerMerReqLog(merLog);

            log.info("===加入【merReqLog】队列成功 orderNo {} , merCode {}",merLog.getOrderNo(),merLog.getMerCode());

        } catch (Exception e) {

            log.error("加入rabbitMQ失败  add merLogHandleQueue is orderNo {} , err {} ",merLog.getOrderNo(),e);
            try {
                //将处理异常的消息直接加入数据库
                merReqService.saveMerLog(merLog);

            }catch (Exception e2) {
                log.error(ErrMonitorCode.BIZ_ERR+" merReqService  saveMerLog is err; merLog: {} , err: {}",merLog,e);
            }
        }
    }


    @Override
    public void supReqLogHandle(SupResult supResult, MerReqLogVo merReqLog, SuplierQueryBean supQueryBean) {

        try {
            //异步落地数据
            logTaskExecutor.execute(
                    () ->  supReqLogHandleQueue(supResult, merReqLog, supQueryBean)
            );

        } catch (Exception e) {
            log.error(ErrMonitorCode.BIZ_ERR+" 流量过大 异步处理 【商户请求日志】 发生异常 merLog = {} , err {}",merReqLog,e);
        }

    }

    //@Async("taskExecutor")
    public void supReqLogHandleQueue(SupResult supResult, MerReqLogVo merReqLog, SuplierQueryBean supQueryBean) {

        SupReqLog supLog = null;
        try {
            supLog = packSupReqlog(supResult, merReqLog, supQueryBean);
            rabbitProducer.producerSupReqLog(supLog);
            log.info("===加入【supReqLog】队列成功 orderNo {} , supCode {}",supLog.getOrderNo(),supLog.getSupCode());
        } catch (Exception e) {
            log.error(" add supReqLogHandle supLog {}  异常", supLog, e);
            if (supLog != null) {
                try {
                    // 将插入异常的数据直接加入队列中
                    supReqService.insertSupReqLog(supLog);
                }catch (Exception e1){
                    log.error(" supReqService.insertSupReqLog err  ",e1);
                }

            }
        }

    }

    private SupReqLog packSupReqlog(SupResult supResult, MerReqLogVo merReqLog, SuplierQueryBean supQueryBean) {

        SupReqLog supLog = new SupReqLog();
        supLog.setMerCode(merReqLog.getMerCode());
        supLog.setCgCode(merReqLog.getCgCode());
        supLog.setProcductCode(merReqLog.getProductCode());
        supLog.setSupProduct(supQueryBean.getSupProductCode());
        supLog.setStatus(supResult.getState().getCode());
        supLog.setOrderNo(merReqLog.getOrderNo());
        supLog.setSupCode(supQueryBean.getSupCode());
        supLog.setSupName(supQueryBean.getSupName());
        supLog.setRemark(supResult.getRemark());
        //判断是否需要付费
        if (supResult.isFree()) {

            supLog.setInPrice(supQueryBean.getInPrice());
            String newProductCode = getYysProductInfo(merReqLog, supResult);
            if(StringUtils.isNotBlank(newProductCode)){
                String supCode = supQueryBean.getSupCode();
                String productCode = merReqLog.getProductCode();
                SuplierQueryBean newSupQueryBean = supplierProductService.selectSupProductBySupCode(supCode, newProductCode);
                if (newSupQueryBean != null) {
                    supLog.setProcductCode(newProductCode);
                    supLog.setSupProduct(newSupQueryBean.getSupProductCode());
                    supLog.setInPrice(newSupQueryBean.getInPrice());
                    log.info("supReqLog 设置实际运营商的 产品 成功 old: {}， new: {} ,supCode {} ,orderNo {}", productCode, newProductCode,supCode,merReqLog.getOrderNo());

                }else{
                    log.warn(" supReqLog 手机三要素核验 相关供应商 运营商 产品没有上架 supCode {}, newProductCode {} , orderNo {}", supCode,newProductCode,merReqLog.getOrderNo());
                }
            }

        } else {
            supLog.setInPrice(BigDecimal.ZERO);
        }
        supLog.setSupSeq(supResult.getSupSeqNo());

        supLog.setReqJson(supResult.getReqJson());
        supLog.setRespJson(supResult.getRespJson());

        LocalDateTime reqTime = supResult.getReqTime();
        LocalDateTime respTime = supResult.getRespTime();

        supLog.setReqTime(reqTime);
        supLog.setRespTime(respTime);
        supLog.setFree(supResult.getFree().getCode());

        //计算处理花费时长
        long millis = Duration.between(reqTime, respTime).toMillis();
        supLog.setTotalTime(millis);

        //log.info("===== supReqLogHandle supLog {} ", supLog);

        return supLog;
    }




    /**
     * 运营商 相关产品 特殊处理逻辑
     * 为了拿到 手机号实际运营商的 产品code ,好查询时间的进价和 售价
     * 判断是否手机类核验 总产品
     * 如果是 需要重新 设置实际的运营商产品 销售信息
     *
     * @param merLog
     * @param supResult
     */
    private String getYysProductInfo(MerReqLogVo merLog, SupResult supResult) {
        String reqProductCode = merLog.getReqProductCode();
        if (supResult == null || StringUtils.isBlank(reqProductCode)) {
            return null;
        }
        //运营商 相关产品
        YysProductCode reqYysProductCode = merLog.getYysProductCode();
        YysCode yysCode = supResult.getYysCode();
        //当前运营商code 和 供应商返回的运营商code 是否一致 一致就不需要 再获取 其它运营商产品code
        if (yysCode == null || reqYysProductCode == null || yysCode.getCode().equals(reqYysProductCode.getYysCode())) {
            return null;
        }

        String productCode = merLog.getProductCode();
        String yysInfocode = yysCode.getCode();
        //判断是否 需要获取 运营商实际售价 不是直接返回
        if (!ProductCodeEum.isGetYysInfo(reqProductCode)) {
            return null;
        }
        YysProductCode yysProductCode = YysProductCode.findByCode(yysInfocode, reqProductCode);
        if (yysProductCode == null) {
            return null;
        }
        String newProductCode = yysProductCode.getMobThreeYysProductCode();

        return newProductCode;

    }


}