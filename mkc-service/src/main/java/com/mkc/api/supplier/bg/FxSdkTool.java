package com.mkc.api.supplier.bg;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gzy.sign.api.AbstractFaXinResponse;
import com.gzy.sign.api.FaXinApiRequest;
import com.gzy.sign.api.FaXinDefaultApiClient;
import com.gzy.sign.api.FaXinResponseResultInfo;
import com.gzy.sign.api.config.FaXinApiClientConfig;
import com.gzy.sign.api.request.FaXinFileStreamUploadApiRequest;
import com.gzy.sign.api.request.FaXinHouseVerifyApiRequest;
import com.gzy.sign.api.request.FaXinHouseVerifyResultApiRequest;
import com.gzy.sign.api.response.FaXinHouseVerifyApiResponse;
import com.gzy.sign.api.response.FaXinHouseVerifyResultApiResponse;
import com.mkc.api.dto.bg.req.MaterialReqVo;
import com.mkc.api.dto.bg.req.PersonInfoReqVo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    /**
     * 不动产信息核查结果接口
     */
    public static String highSchoolCheck(JSONObject jsonObject, String baseUrl, String appId, String privateKey, String publicKey) {
        JSONObject data = jsonObject.getJSONObject("data");
        FaXinApiRequest request = null;
        FaXinResponseResultInfo<FaXinHouseVerifyResultApiResponse> faXinResponseResultInfo = executeRequest(null, baseUrl, null, appId, privateKey, publicKey);
        return JSONUtil.toJsonStr(faXinResponseResultInfo);
    }


}
