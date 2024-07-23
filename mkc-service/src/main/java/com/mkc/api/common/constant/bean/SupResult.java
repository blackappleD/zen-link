package com.mkc.api.common.constant.bean;

import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.enums.YysCode;
import com.mkc.common.enums.FreeState;
import com.mkc.common.enums.ReqState;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供应商响应信息主体
 *
 * @author  atd
 */

@Data
public class SupResult<T> implements Serializable
{
    private static final long serialVersionUID = 1L;


    private ReqState state=ReqState.ERROR;

//    private String code;


    private String supSeqNo;

    private String supCode;
    private String supName;

    /**
     * 请求供应商信息
     */
    private String reqJson;

    /**
     * 供应商响应信息
     */
    private String respJson;

    /**
     * 备注信息
     */
    private String remark;
    /**
     * 自定义错误消息
     */
    private Boolean definedFailMsg=false;

    /** 请求时间 */
    private LocalDateTime reqTime;

    /** 响应时间 */
    private LocalDateTime respTime;


    private FreeState free=FreeState.NO;

    //运营商 针对手机号 相关接口 使用，需要根据手机号实际的运营商重新判断
    private YysCode yysCode;

    private BigDecimal inPrice;

    private T data;


    //public SupResult(){}

    public SupResult(String reqJson,LocalDateTime reqTime){
        this.reqJson=reqJson;
        this.reqTime = reqTime;
    }
    public SupResult(JSONObject reqJson, LocalDateTime reqTime){
        this.reqJson=reqJson.toJSONString();
        this.reqTime = reqTime;
    }

    public SupResult(String reqJson,LocalDateTime reqTime,LocalDateTime respTime,String remark){
        this.reqJson=reqJson;
        this.reqTime = reqTime;
        this.respTime = respTime;
        this.remark=remark;
    }


    public static  SupResult err(){
       return  new SupResult<>("", LocalDateTime.now(), LocalDateTime.now(), "该供应商不支持这种产品查询");
    }


//    private static <T> SupResult<T> restResult(T data, ReqState state ,String reqJson)
//    {
//        SupResult<T> apiResult = new SupResult<>();
//        apiResult.setState(state);
//        apiResult.setData(data);
//        apiResult.setReqJson(reqJson);
//        return apiResult;
//    }

    /**
     * 判断是否查查询失败 true 是
     * @return
     */
    public  Boolean isFail()
    {
        return  ReqState.ERROR.equals( this.getState());
    }



    /**
     * 判断是否查得  包含一致 或 不一致的情况
     * @return
     */
    public  boolean isQueryGet()
    {
        return ReqState.SUCCESS.equals( this.getState()) || ReqState.NOT.equals( this.getState());
    }

    /**
     * 判断是否成功查询到数据 或 核验类表示数据  一致
     * @return
     */
    public  boolean isSuccess()
    {
        return ReqState.SUCCESS.equals( this.getState());
    }

    /**
     * 判断是否查无 true 是
     * @return
     */
    public  boolean isNoGet()
    {
        return ReqState.NOGET.equals( this.getState());
    }

    /**
     * 不一致
     * @return
     */
    public  boolean isNot()
    {
        return ReqState.NOT.equals( this.getState());
    }

    /**
     * 是否收费
     * true 收费 false不收费
     * @return
     */
    public  boolean isFree()
    {
        return FreeState.YES.equals(this.getFree());
    }




}
