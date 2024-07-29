package com.mkc.api.supplier.bg;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gzy.sign.api.*;
import com.gzy.sign.api.config.FaXinApiClientConfig;
import com.gzy.sign.api.request.FaXinFileStreamUploadApiRequest;
import com.gzy.sign.api.request.FaXinHouseVerifyApiRequest;
import com.gzy.sign.api.request.FaXinHouseVerifyResultApiRequest;
import com.gzy.sign.api.response.FaXinFileStreamUploadApiResponse;
import com.gzy.sign.api.response.FaXinHouseVerifyApiResponse;
import com.gzy.sign.api.response.FaXinHouseVerifyResultApiResponse;
import com.mkc.api.vo.bg.MaterialReqVo;
import com.mkc.api.vo.bg.PersonInfoReqVo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FxSdkTool {

    private static FaXinDefaultApiClient getDefaultApiClient(String privateKey, String publicKey, String baseUrl, String fileUrl, String appId) {
        //平台方openApi的地址
        String baseServerUrl = baseUrl;
        //平台方openApi文件服务的地址
        String fileUploadBaseServerUrl = fileUrl;
        FaXinApiClientConfig config = new FaXinApiClientConfig(baseServerUrl, fileUploadBaseServerUrl, appId, privateKey, publicKey, "RSA2", "RSA2");
        return new FaXinDefaultApiClient(config);
    }

    public static <T extends AbstractFaXinResponse> FaXinResponseResultInfo<T> executeRequest(FaXinApiRequest<T> request,String baseUrl, String fileUrl, String appId, String privateKey, String publicKey) {
        //合作方自己封装的请求，不区分返回结果，由合作方决定是否使用
        long s = System.currentTimeMillis();
        FaXinDefaultApiClient client = getDefaultApiClient(privateKey, publicKey, baseUrl, fileUrl, appId);
        FaXinResponseResultInfo<T> resultInfo = client.executeRequest(request);
        System.out.println("请求耗时：" + (System.currentTimeMillis() - s) + "ms");
        return resultInfo;
    }

    /**
     * 单文件上传
     */
    public static String uploadFile(File file, String privateKey, String publicKey, String fileUrl, String appId){
        //File file = new File("E:\\文件3.jpg");
        String baseUrl = null;
        FaXinFileStreamUploadApiRequest apiRequest = new FaXinFileStreamUploadApiRequest(file, null, file.getName());
        //文件上传请求的响应实体类
        FaXinResponseResultInfo<FaXinHouseVerifyApiResponse> faXinResponseResultInfo = executeRequest(apiRequest, baseUrl, fileUrl, appId, privateKey, publicKey);
        return JSONUtil.toJsonStr(faXinResponseResultInfo);
    }

    public static void main(String[] args) {
        File file = new File("F:\\1.png");
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnlRJY/k5Itnm7EtdMCXiv6ve7F9jvxktrHWblDoq4q+awVrxgDQWaYpk9hLG0p9VFH7j02GwpdOBy/kmsx5N9mMcK2r5IwLgweX7Jk0uSYoOIJdTh7Fm3Vqfbtpd0uMB1dualZTsbHw4TeBVMvXMuQKzlp6HKKLC/dgldLMNNwGplq5I7/hUkyAW1UccO4oxLqtRwuFKL37YRVugP5k2+yXTazoa5SwWjaOBS1i+RW1QkxkHGKRP8zB0lDHLRH68GSEXfSGuxz3DRbX204LLk/GgRUSdOlfUi4XnVK3lA/IKNJ6nI+yqOXPsS5qGEVo2qfObAoPhIqPA83uHnAznawIDAQAB";
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDabpdfEzvhApPAiKKoBlsNrvMGnJOLh8awBhzQHo9yo0nv0JStQr2E4mcgVHPvvziLal6iF0sUH3uQCkWIhkDVyNR6PXb0sIPtq1x4w417gfQbUuHN5EbNOFz8plBwxGHfcp0vsxz8O+sMX6ZaghwOq7XOJYhRglzMDBKG9hhPrgTJHeowy/A2fcc72gysxVgCzeMT6yRFfPtUSF8vDmBaHPD+y4M9zbiNkej4xI4oDCjBaJYvbSHN4q513wKMun9c01DGhY4u9rCjX6JdFQlEyqWHO1gCx6vJV94g/GeQu/KB6Z0g2YL6TdBsk0o9ANL/rNrMtdhfFs6Zvy2Zk3K9AgMBAAECggEBAKNZJNYy2gqxFLFc/NbOufUHqjJ9kVE9Hnawe4ef8wJ13mr6/tR7HqNrdFeXEIRv6edIirCI3tqTmKHlLI6mZ4H4h/1KTdU6084+4g5lnz6HtndgigCg/9mkltVRwsIx+kdRvP7Y4Yo3fNVqCkuRFU5bVsBtDP53kNH/DTPkgdXDGPS6DEjacfO+yP5OQXL2RgBEWQC89SmsVzXrcPRxRg+nRgW35bOqlMZPt/+UxKmH/4jMRtvgyRMFlO84CmsDElZ4lAAefOcN4dw+Haxm8f1NylWL8K8PvlrB/+fgxCYe9rVaVpNgd9SbkPRSgBnm1X/tq/ZplGoU8VVd8PId7jkCgYEA+2chvkrn70j3GvANCGqLBYYfzlWbJVYHckjEvuDTvEyPIvTWATND3mUlQT4gRanQ6APJWMHxTktI09GivogFU3Ujb4Br+hLLatuVF0bw+sTDwc8+58dpEchKZVAf7uUAoNPJFEoUSgR0BXB5dxQLMPJ4YDGTuvQgYvL6juX8tt8CgYEA3m0du17Mc8Tfn8O+ugRc8CM4yaKmQYfe6yO3UrT+VsiA9PgYZ0oAaUEQ43JogUqBczySt0oSHA3X9fdgdJnm3DABelOy/DRu1ogDZ+tw/DTU5zt3+0uMqi8EU9Wsd8ySH3CMQ0ZeXfJwErah1zC8zid5AsUPEOTRZmJ1u/EjFeMCgYBYPZO3ch1EfMzW9Kjuak8PiktBpgSFr1N/xpDgGpPXBNw8vckbtL2+oqvX4ZF8QiWkOigC8lfbTktqpatEywNrjrL4YRAFP3GMuTP0bp8IteWr8i03N2PmrPGH6/65YEG/MXbjmzjR3gxHGHpZ1Hw1GvYrdVtQBH4VnQla/Nid6wKBgFE3UpXqJYlYJ7d0iB3wHedpVQD7LO3cqN7/KZ8/i2cwyzWnqXF9qezE31/NlK8w/1eOlJvspfQsceDUNRoeYBeiR8fc7Av/MUGQZH37kEPnHp5YLpVJntE9Xb0m1lTgE7L7K5wdA7QWJCoRGcMzfp1f+GW0gq3Of099QDQh9oe1AoGATFpvAph5kLfP5Ya7A8jD35kgbL9Xf9dLPfyWQ2MUWNb5Yn8AITaVtDWrh71T2GsxJwp+3kgI/dU7OboRhh6TjNRYQ12+ESP9T6IlQRKZFXxAbaZff1Ci20RzPlzdJKYmMn0XRxdH2tjesnK2ecnFo/u6f8CPFluh3FoJJKhMAKE=";
        String str = uploadFile(file, privateKey, publicKey, "", "");
        System.out.println(str);
    }

    /***
     *不动产信息核查接口
     */
    public static String houseCheck(JSONObject jsonObject, String baseUrl, String appId, String privateKey, String publicKey) {
        FaXinHouseVerifyApiRequest faXinHouseVerifyApiRequest = new FaXinHouseVerifyApiRequest();
        JSONObject data = jsonObject.getJSONObject("data");
        List<PersonInfoReqVo> persons = data.getList("persons", PersonInfoReqVo.class);
        List<FaXinHouseVerifyApiRequest.PersonDTO> personDTOList = new ArrayList<>();
        for (PersonInfoReqVo reqVo : persons) {
            FaXinHouseVerifyApiRequest.PersonDTO personDTO = new FaXinHouseVerifyApiRequest.PersonDTO();
            personDTO.setName(reqVo.getName());
            personDTO.setCardNum(reqVo.getCardNum());
            personDTOList.add(personDTO);
        }
        List<FaXinHouseVerifyApiRequest.MaterialDTO> materialDTOList = new ArrayList<>();
        List<MaterialReqVo> materials = data.getList("materials", MaterialReqVo.class);
        for (MaterialReqVo materialReqVo : materials) {
            FaXinHouseVerifyApiRequest.MaterialDTO materialDTO = new FaXinHouseVerifyApiRequest.MaterialDTO();
            materialDTO.setMaterialType(materialReqVo.getType());
            materialDTO.setMaterialFileId(materialReqVo.getFileId());
            materialDTOList.add(materialDTO);
        }
        faXinHouseVerifyApiRequest.setPersons(personDTOList);
        faXinHouseVerifyApiRequest.setMaterials(materialDTOList);
        FaXinResponseResultInfo<FaXinHouseVerifyApiResponse> faXinResponseResultInfo = executeRequest(faXinHouseVerifyApiRequest, baseUrl, null, appId, privateKey, publicKey);
        return JSONUtil.toJsonStr(faXinResponseResultInfo);
    }



    /**
     * 不动产信息核查结果接口
     */
    public static String houseCheckResult(JSONObject jsonObject, String baseUrl, String appId, String privateKey, String publicKey) {
        JSONObject data = jsonObject.getJSONObject("data");
        FaXinHouseVerifyResultApiRequest faXinHouseVerifyResultApiRequest = new FaXinHouseVerifyResultApiRequest();
        faXinHouseVerifyResultApiRequest.setReqOrderNo(data.getString("reqOrderNo"));
        List<String> personCardNumList = data.getList("personCardNumList", String.class);
        List<FaXinHouseVerifyResultApiRequest.PersonCardNum> cardNumList = new ArrayList<>();
        for (String cardNum : personCardNumList) {
            FaXinHouseVerifyResultApiRequest.PersonCardNum personCardNum = new FaXinHouseVerifyResultApiRequest.PersonCardNum();
            personCardNum.setCardNum(cardNum);
            cardNumList.add(personCardNum);
        }
        faXinHouseVerifyResultApiRequest.setPersonCardNumList(cardNumList);
        FaXinResponseResultInfo<FaXinHouseVerifyResultApiResponse> faXinResponseResultInfo = executeRequest(faXinHouseVerifyResultApiRequest,baseUrl, null, appId, privateKey, publicKey);
        return JSONUtil.toJsonStr(faXinResponseResultInfo);
    }


}
