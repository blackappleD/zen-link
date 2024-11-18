package com.mkc.api.supplier;

import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.vo.bg.*;
import com.mkc.bean.SuplierQueryBean;

public interface IBgSupService extends ISupService {

    /**
     * 高校学历核查结果
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryHighSchoolEducationResultInfo(HighSchoolEducationResultInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 高校学历核查
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryHighSchoolEducationInfo(HighSchoolEducationInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 高校学历核查实时
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryHighSchoolEducation(HighSchoolEducationInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 全国高等学历信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryEducationInfo(EducationInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }


    /**
     * 车辆信息5三要素-简版
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryCarInfo(CarInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 人车核验详版
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryPersonCarInfo(PersonCarDetailReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 人车核验详版
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryFourElementsInfo(EnterpriseFourElementsReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 不动产信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryHouseInfo(HouseInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 不动产结果信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryHouseResultInfo(HouseResultInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 经济能力评级查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryFinanceInfo(FinanceInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 经济能力评级V3查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryFinanceInfoV3(FinanceInfoV3ReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 经济能力评级V7查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryFinanceInfoV7(FinanceInfoV3ReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 确信分信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult querySureScoreInfo(SureScoreInfoReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }


    /**
     * 行驶证信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryVehicleLicenseInfo(VehicleLicenseReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 经济能力评级查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryEconomicRateInfo(EconomicRateReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }

    /**
     * 行驶身份核验
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryDrivingLicenseInfo(DrivingLicenseReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }


    /**
     * 婚姻状况信息查询
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryMaritalStatus(MarriageInfoReqInfo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }


    /**
     * 婚姻关系验证
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryMaritalRelationship(MaritalRelationshipReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }
    /**
     * 婚姻稳定状况
     * @param vo
     * @param bean
     * @return
     */
    default public SupResult queryMaritalStability(MaritalStabilityReqVo vo, SuplierQueryBean bean)
    {
        return SupResult.err();
    }
}
