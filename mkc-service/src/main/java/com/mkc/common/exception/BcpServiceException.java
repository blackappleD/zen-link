package com.mkc.common.exception;

import com.mkc.api.common.constant.ApiReturnCode;

/**
 * 批量核验 业务异常
 * 
 * @author  atd
 */

public final class BcpServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    /**
     * 错误编码
     */
    private String code;
    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     *
     *
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public BcpServiceException()
    {
    }

    public BcpServiceException(String message)
    {
        this.message = message;
    }
    public BcpServiceException(String code, String message)
    {
        this.message = message;
        this.code = code;
    }
    public BcpServiceException(ApiReturnCode returnErr)
    {
        this.message = returnErr.getMsg();
        this.code = returnErr.getCode();
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public BcpServiceException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public BcpServiceException setMessage(String message)
    {
        this.message = message;
        return this;
    }
}