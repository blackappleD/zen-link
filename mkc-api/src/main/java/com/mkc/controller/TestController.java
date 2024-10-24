package com.mkc.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.support.csv.CSVWriter;
import com.mkc.api.common.constant.bean.Result;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.ExcelRead;
import com.mkc.domain.MerInfo;
import com.mkc.service.IMerInfoService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author tqlei
 * @date 2023/6/30 14:53
 */

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private IMerInfoService merchantService;

    private final static String KEY = "fsdgdfe647234bvdfgdfy54767";

    /**
     * 测试服务状态
     *
     * @return
     */
    @GetMapping("/state")
    public Result test(HttpServletRequest request, String key) {
        if (!KEY.equals(key)) {
            return Result.fail("无效的身份访问");
        }
        try {
            MerInfo merInfo = merchantService.selectMerInfoByCode(BaseController.TEST_MERCODE);
            return Result.ok("服务启动成功");

        } catch (Exception e) {
            return Result.fail("服务还未启动成功");
        }
    }

    @PostMapping("/excelTest")
    public void excelTest(@RequestBody MultipartFile excel, HttpServletResponse response) {
        try {
            List<ExcelRead> readList = EasyExcel.read(excel.getInputStream())
                    .headRowNumber(1)
                    .head(ExcelRead.class)
                    .sheet(0)
                    .doReadSync();

            for (ExcelRead read : readList) {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
                com.alibaba.fastjson.JSONObject post = post("http://api.zjbhsk.com/bg/financeInfo", jsonObject);
                read.setCode(post.getString("code"));
                try {
                    JSONObject data = post.getJSONObject("data");
                    if (Objects.nonNull(data)) {
                        read.setRange(data.getString("range"));
                        read.setHistory(data.getString("history"));
                        read.setStability(data.getString("stability"));
                    }
                } catch (Exception e) {
                    read.setRange(post.getString("data"));
                    log.error(e.getMessage());
                }
            }
            log.info(readList.size() + "条样例测试完毕！");
            setExcelRespProp(response, DateUtils.dateTimeNow() + "经济能力2W测试结果");
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelRead.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("经济能力2W测试结果")
                    .doWrite(readList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * post
     *
     * @return
     */
    public JSONObject post(String url, JSONObject bodyObject) {
        String merCode = "BhCpTest";
        bodyObject.put("merCode", merCode);
        bodyObject.put("key", "80bb75f192ad62fc9dd59e6b39ce9ba5");
        bodyObject.put("merSeq", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        String plaintext = merCode + bodyObject.getString("idCard") + bodyObject.getString("name") + bodyObject.getString("mobile");
        String pwd = "1503a2208bc4cc8dec63d82948157fa9";
        String signText = plaintext + pwd;
        String signMd5 = DigestUtils.md5DigestAsHex(signText.getBytes());
        bodyObject.put("sign", signMd5);
        String bodyJson = bodyObject.toJSONString();
        HttpResponse execute = HttpRequest.post(url)
                .body(bodyJson)
                .execute();
        log.info("queryPost url = 【{}】 request = 【{}】, response = 【{}】", url, bodyJson, execute.body());
        return JSONObject.parseObject(execute.body());
    }

    public static void setExcelRespProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }


}
