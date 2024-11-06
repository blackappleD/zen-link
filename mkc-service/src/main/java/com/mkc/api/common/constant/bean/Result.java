package com.mkc.api.common.constant.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mkc.api.common.constant.ApiReturnCode;
import com.mkc.common.enums.FreeState;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 响应信息主体
 *
 * @author  atd
 */

@Data
public class Result<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
//    public static final String SUCCESS = ApiReturnCode.SUCCESS.getCode();
//
//    /** 查无 */
//    public static final String NO = ApiReturnCode.NO.getCode();
//
//    /** 失败 */
//    public static final String FAIL = ApiReturnCode.FAIL.getCode();

    private String code;

    private String seqNo;

    private String free=FreeState.NO.getCode();

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer billedTimes;

    private T data;

    //==============查询成功

    public static <T> Result<T> ok()
    {
        return restResult(null,ApiReturnCode.SUCCESS, FreeState.YES);
    }

    public static <T> Result<T> ok(T data) {

        return restResult(data,ApiReturnCode.SUCCESS, FreeState.YES);
    }

    public static <T> Result<T> ok(T data, FreeState freeState, String seqNo) {

        return restResult(data, ApiReturnCode.SUCCESS,Objects.nonNull(freeState) ? freeState : FreeState.YES,seqNo);
    }

    public static <T> Result<T> ok(T data, String seqNo,String msg) {

        return restResult(data, ApiReturnCode.SUCCESS.getCode(),msg,FreeState.YES,seqNo);
    }
    public static <T> Result<T> ok(T data, String seqNo) {

        return restResult(data, ApiReturnCode.SUCCESS,FreeState.YES,seqNo);
    }


    //==================不一致======================

    /**
     * 不一致
     * @return
     * @param <T>
     */
    public static <T> Result<T> not()
    {
        return restResult(null,ApiReturnCode.NOT, FreeState.YES);
    }
    /**
     * 不一致 收费
     * @return
     * @param <T>
     */
    public static <T> Result<T> not(T data) {

        return restResult(data,ApiReturnCode.NOT, FreeState.YES);
    }
    /**
     * 不一致
     * @return
     * @param <T>
     */
    public static <T> Result<T> not(T data,String seqNo) {

        return restResult(data, ApiReturnCode.NOT,FreeState.YES,seqNo);
    }


    //==============查无


    /**
     * 查无
     * @return
     * @param <T>
     */
    public static <T> Result<T> no(FreeState freeState,String seqNo)
    {
        return restResult(null,ApiReturnCode.NO, freeState,seqNo);
    }

    /**
     * 查无
     * @return
     * @param <T>
     */
    public static <T> Result<T> no(T data,FreeState freeState,String seqNo) {

        return restResult(data, ApiReturnCode.NO,freeState,seqNo);
    }

    /**
     * 查询失败状态码 适用于 一下场景
     * 1.内部配置错误查询失败
     * 2.查询供应商数据异常 或 失败
     * @return
     * @param <T>
     */

    public static <T> Result<T> fail()
    {
        return restResult(null,ApiReturnCode.FAIL, FreeState.NO);
    }

    /**
     * 查询失败状态码 适用于 一下场景
     * 1.内部配置错误查询失败
     * 2.查询供应商数据异常 或 失败
     * @return
     * @param <T>
     */

    public static <T> Result<T> fail(ApiReturnCode code)
    {
        return restResult(null,code, FreeState.NO);
    }

    public static <T> Result<T> fail(String seqNo){
        return restResult(null,ApiReturnCode.FAIL, FreeState.NO,seqNo);
    }

//    public static <T> Result<T> fail(T data) {
//        return restResult(data,ApiReturnCode.FAIL, FreeState.NO.getCode());
//    }

    public static <T> Result<T> fail(T data,String seqNo) {

        return restResult(data, ApiReturnCode.FAIL,FreeState.NO,seqNo);
    }

    public static <T> Result<T> fail(String code,String msg) {

        return restResult(null, code, msg,FreeState.NO);
    }
    public static <T> Result<T> fail(String code,String msg,String seqNo) {

        return restResult(null, code, msg,FreeState.NO,seqNo);
    }

    public static <T> Result<T> fail(T data,String msg,String seqNo) {

        return restResult(null, ApiReturnCode.FAIL.getCode(), msg,FreeState.NO,seqNo);
    }




    private static <T> Result<T> restResult(T data,ApiReturnCode returnCode,FreeState free) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(returnCode.getCode());
        apiResult.setMsg(returnCode.getMsg());
        apiResult.setData(data);
        apiResult.setFree(free.getCode());
        return apiResult;
    }



    private static <T> Result<T> restResult(T data,ApiReturnCode returnCode,FreeState free,String seqNo) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(returnCode.getCode());
        apiResult.setMsg(returnCode.getMsg());
        apiResult.setData(data);
        apiResult.setSeqNo(seqNo);
        apiResult.setFree(free.getCode());
        return apiResult;
    }

    private static <T> Result<T> restResult(T data, String code, String msg,FreeState free) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setFree(free.getCode());
        apiResult.setMsg(msg);
        return apiResult;
    }

    private static <T> Result<T> restResult(T data, String code, String msg,FreeState free,String seqNo) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setFree(free.getCode());
        apiResult.setMsg(msg);
        apiResult.setSeqNo(seqNo);
        return apiResult;
    }


}
