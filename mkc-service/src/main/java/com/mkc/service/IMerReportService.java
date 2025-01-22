package com.mkc.service;

import java.time.LocalDate;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mkc.domain.FxReqRecord;
import com.mkc.domain.MerReport;
import com.mkc.dto.MerReportExcel;

/**
 * 商户调用日志统计Service接口
 *
 * @author mkc
 * @date 2023-06-16
 */
public interface IMerReportService extends IService<MerReport> {

    public boolean statMerReqLogReport(LocalDate date, String merCode);

    public List<MerReport> listMerReport(MerReport merReport);

    List<FxReqRecord> listFxHouseReport(MerReport merReport);


    List<FxReqRecord> listFxHouseReportV2(MerReport merReport);
    /**
     * 法信学历调用报告
     *
     * @param merReport
     * @return
     */
    List<FxReqRecord> listFxEduReport(MerReport merReport);

    /**
     * 账单
     *
     * @param fxReqRecords
     * @return
     */
    List<MerReportExcel> listReport(List<FxReqRecord> fxReqRecords, String productName);

    /**
     * 日期对账单
     *
     * @param fxReqRecords
     * @param name
     * @return
     */
    List<MerReportExcel> listDateReport(List<FxReqRecord> fxReqRecords, String name);
}
