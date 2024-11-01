package com.mkc.api.common.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * 请求生产环境产品
 *
 * @author linst
 */

@Slf4j
public class ApiUtils {

    public static JSONObject queryApi(String url, JSONObject bodyObject, String plaintext) {
        String merCode = "BhCpTest";
        bodyObject.put("merCode", merCode);
        bodyObject.put("key", "80bb75f192ad62fc9dd59e6b39ce9ba5");
        bodyObject.put("merSeq", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
        String signText = merCode + plaintext + pwd;
        String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
        bodyObject.put("sign", signMd5);
        String bodyJson = bodyObject.toJSONString();
        HttpResponse execute = HttpRequest.post(url)
                .body(bodyJson)
                .execute();
        String result = execute.body();
//        String result = "{\"code\":\"200\",\"seqNo\":\"1850731571662835712\",\"free\":\"1\",\"msg\":\"成功\",\"data\":{\"reqOrderNo\":\"8b1fcf9b9e694282bf7d5e98cbb814a1\",\"approvalStatus\":\"APPROVED\",\"authResults\":[{\"cardNum\":\"110102198012130815\",\"authState\":\"30\",\"authStateDesc\":\"核查成功\",\"isReAuth\":0,\"resultList\":[{\"certNo\":\"京(2021)海不动产权第0020831号\",\"unitNo\":\"110108005001GB00301F00040030\",\"location\":\"海淀区阜成路53号院水6号楼3门501号\",\"ownership\":\"/\",\"houseArea\":\"64.10\",\"rightsType\":\"国有建设用地使用权/房屋所有权\",\"isSealUp\":\"否\",\"isMortgaged\":\"否\",\"rightsStartTime\":\"1899-12-31\",\"rightsEndTime\":\"1899-12-31\",\"useTo\":\"住宅\"},{\"certNo\":\"京(2021)海不动产权第0020831号\",\"unitNo\":\"110108005001GB00301F00040030\",\"location\":\"海淀区阜成路53号院水6号楼3门501号\",\"ownership\":\"/\",\"houseArea\":\"64.10\",\"rightsType\":\"国有建设用地使用权/房屋所有权\",\"isSealUp\":\"否\",\"isMortgaged\":\"否\",\"rightsStartTime\":\"1899-12-31\",\"rightsEndTime\":\"1899-12-31\",\"useTo\":\"住宅\"}]}]}}";
//        String result = "{\"code\":\"200\",\"data\":{\"reqOrderNo\":\"619f85bf61364ea5ae57ed9ea52dec6b\",\"status\":\"2\",\"isExists\":true,\"xm\":\"李晓阳\",\"zsbh\":\"*g13\",\"zjbh\":\"*\",\"yxmc\":\"天津理工大学\",\"zymc\":\"软件工程\",\"cc\":\"本科\",\"rxrq\":\"20210901\",\"byrq\":\"20230618\",\"xxxs\":\"普通全日制\"},\"free\":\"1\",\"msg\":\"成功\",\"seqNo\":\"1849330580378128384\"}";
        log.info("queryPost url = 【{}】 request = 【{}】, response = 【{}】", url, bodyJson, result);
        return JSONObject.parseObject(result);
    }


    public static void main(String[] args) {
        System.err.println(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
//        JSONObject jsonObject = new JSONObject();
//        String idCard = sha256("330682199111102830");
//        String name = sha256("谢刚强");
//        String mobile = sha256("18888888888");
//        jsonObject.put("idCard", idCard);
//        jsonObject.put("name", name);
//        jsonObject.put("mobile", mobile);
//        String plainText = idCard + name + mobile;
//        System.err.println(ApiUtils.queryApi("http://api.zjbhsk.com/bg/financeInfo", jsonObject, plainText));
    }

    @SneakyThrows
    public static String sha256(String data) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] encryptedData = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : encryptedData) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
    }

}
