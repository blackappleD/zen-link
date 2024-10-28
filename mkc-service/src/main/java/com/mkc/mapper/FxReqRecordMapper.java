package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.bean.MerReqLogBean;
import com.mkc.domain.FxReqRecord;

import java.util.List;

public interface FxReqRecordMapper extends BaseMapper<FxReqRecord> {

   public int selectCountByReqOrderNo(String reqOrderNo);

   public FxReqRecord selectFxReqRecordByReqOrderNoAndUserFlag(FxReqRecord fxReqRecord);

   public int insertFxReqRecord(FxReqRecord fxReqRecord);

   public int updateFxReqRecordByRequestOrderNo(FxReqRecord fxReqRecord);

   List<FxReqRecord> listByRangeTime(MerReqLogBean merReqLog);
}
