package test.ck;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.utils.Md5Utils;
import com.mkc.api.supplier.utils.GmUtils;
import com.mkc.common.utils.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author tqlei
 * @date 2024/7/1 11:33
 */

@Slf4j
public class FpCkTest {

    private static String DOMAIN = "http://211.148.24.156:9955";
    private static String USERNAME = "dtyixin";
    private static String CHANNEL = "dtyixin";
    private static String SIGN = "13E22794F37EE34556A06737A5EBB93A";
    private static String KEY = "1F364F52BD03587BB8A39CEBC31179F3";

    //http://211.148.24.156:9955/Core3Zither/winchampiond0/comService/invoiceVerification
    @Test
    public void invoiceVerification() throws Exception {

        String url=DOMAIN+"/Core3Zither/winchampiond0/comService/invoiceVerification";

        JSONObject params=new JSONObject();
        //开票⽇期 billingDate String 是 格式为 yyyyMMdd 例 如：20220812
        //发票代码 invoiceCode String 是
        //发票号码 invoiceNumber String 是
        //⾦额 amount String 是 保留两位⼩数,例如 100.00
        //校验码 checkCode String 否
        String billingDate="20240627";
        String invoiceCode="031002300211";
        String invoiceNumber="90386612";
        //String checkCode="";
        String checkCode="18321349384470528029";

        String amount="819.00";

        params.put("channel", CHANNEL);
        params.put("userName", USERNAME);
        params.put("sign", SIGN);

        JSONObject message=new JSONObject();
        //开票日期
        message.put("billingDate", billingDate);
        //发票代码
        message.put("invoiceCode", invoiceCode);
        //发票号码
        message.put("invoiceNumber", invoiceNumber);
        //⾦额 保留两位⼩数,例如100.00
        message.put("amount", amount);
        message.put("checkCode", checkCode);

        String messageStr=message.toJSONString();
        String signature= Base64Util.encodeString(Md5Utils.md5(CHANNEL+"_"+messageStr+"_"+CHANNEL));
        params.put("message", GmUtils.sm4Encrypt(messageStr,KEY));
        params.put("signature", signature);

        String result = HttpUtil.post(url, params);
        log.info("=====params {} , result =  {}",message,result);

        JSONObject resultJson = JSON.parseObject(result);
        //001 查询成功！
        //002 超过该张发票当⽇查验次数(请于次⽇再次查 验)!
        //003 发票查验请求太频繁，请稍后再试！
        //004 超过服务器最⼤请求数，请稍后访问!
        //005 请求不合法!
        //006 发票信息不⼀致
        //007 验证码失效
        //008 验证码错误
        //009 查⽆此票
        //010 ⽹络超时，请重试！
        //020 由于查验⾏为异常，涉嫌违规，当前⽆法使⽤ 查验服务！
        //rqerr 当⽇开具发票可于次⽇进⾏查验
        String resCode = resultJson.getString("resCode");
        String resDesc = resultJson.getString("resDesc");
        String queryOrderNumber = resultJson.getString("queryOrderNumber");
        JSONObject data = resultJson.getJSONObject("data");
        if(data == null){
            log.error(" resultJson.data is null  {}",data);
            return;
        }
        String resultStr=data.getString("result");
        if(StringUtils.isNotBlank(resultStr)){
            resultStr = GmUtils.sm4Decrypt(resultStr,KEY);
        }
        log.info("====resultStr2  {}",resultStr);
        //成功
        if("0000".equals(resCode)){

            JSONObject resultJson2 = JSON.parseObject(resultStr);
            String code = resultJson2.getString("code");
            String msg = resultJson2.getString("msg");
            if("001".equals(code)) {
                JSONObject data2 = resultJson2.getJSONObject("data");
                log.info("查询成功 code {} ，msg {} , data {}",code,msg,data2);
                //查无
            }else if("009".equals(code)){
                log.info("查无 code {} , msg {}",code,msg);
            }else {
                log.info("查询失败 code {} ， msg {}",code,msg);
            }
        }else {
            log.error("查询失败 resCode {} , resDesc {}",resCode,resDesc);

        }

    }


    public static void main(String[] args) {
        String invoiceStr="24312000000128570533";
        String billingDate="20240508";
        String invoiceCode="";
        String invoiceNumber="24312000000128570533";
        if(invoiceStr.length()==20){
            invoiceCode=invoiceStr.substring(0,12);
            invoiceNumber=invoiceStr.substring(12,20);
            System.out.println(invoiceCode  +"  === " + invoiceNumber);
        }


    }

}
