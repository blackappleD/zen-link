package com.mkc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkc.domain.FxReqRecord;

public interface FxReqRecordMapper extends BaseMapper<FxReqRecord> {

   public int selectCountByReqOrderNo(String reqOrderNo);

   public FxReqRecord selectFxReqRecordByReqOrderNoAndUserFlag(FxReqRecord fxReqRecord);

   public int insertFxReqRecord(FxReqRecord fxReqRecord);

   public int updateFxReqRecordByRequestOrderNo(FxReqRecord fxReqRecord);
}
