package com.mkc.api.supplier;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import org.apache.commons.compress.utils.Sets;
import org.slf4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Set;

/**
 * @author tqlei
 * @date 2023/8/1 11:12
 */


public interface ISupService {

    /**
     * 百行定制 供应商
     */
    public String SUP_QZDZ = "QZDZ";
    /**
     * 自研模型 供应商
     */
    public String SUP_ZYMX = "ZYMX";

    Set ZY_SET= Sets.newHashSet(SUP_ZYMX, SUP_QZDZ);

    /**
     * 供应商处理发生异常  报警处理
     *
     * @param log
     * @param messagePattern
     * @param arguments
     * @return
     */
    default public void errMonitorMsg(Logger log, String messagePattern, Object... arguments) {

        try {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(messagePattern, arguments);
            String msg = formattingTuple.getMessage();

            //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //   PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
            //   formattingTuple.getThrowable().printStackTrace(printWriter);
            //   String errorMsg = new String(byteArrayOutputStream.toByteArray());
            Throwable throwable = formattingTuple.getThrowable();
            String errorMsg ="";
            if (throwable != null) {
                errorMsg =  ExceptionUtil.stacktraceToString(throwable);
            }

            log.error("===suplier msg:  {} ,  errorMsg: {} ",msg,errorMsg);

            DdMonitorMsgUtil.sendDdSysErrMsg(msg + "\n" + errorMsg);

        } catch (Throwable e) {
            log.error("errMonitorMsg is err ; " + messagePattern, arguments, e);
        }


    }

}
