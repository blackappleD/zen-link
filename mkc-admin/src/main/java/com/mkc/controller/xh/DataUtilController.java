package com.mkc.controller.xh;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.ProductPrivacyKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mkc.api.common.tool.AESUtil;
import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.ZipStrUtils;

/**
 * 数据加密压缩Controller
 *
 * @author mkc
 * @date 2023-06-16
 */
@Controller
@RequestMapping("/xh/dataUtil")
public class DataUtilController extends BaseController {
    private String prefix = "xh";

    @RequiresPermissions("xh:dataUtil:view")
    @GetMapping()
    public String report(Model model) {
        model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return prefix + "/dataUtil";
    }

    /**
     * 加密
     *
     * @throws Exception
     */
    @RequiresPermissions("xh:dataUtil:enc")
    @Log(title = "加密", businessType = BusinessType.OTHER)
    @PostMapping("/enc")
    @ResponseBody
    public AjaxResult enc(String data) throws Exception {

        if (StringUtils.isBlank(data)) {
            return error("左侧数据不能为空");
        }

        data = AESUtil.encrypt(data);
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 解密
     *
     * @throws Exception
     */
    @RequiresPermissions("xh:dataUtil:dec")
    @Log(title = "解密", businessType = BusinessType.OTHER)
    @PostMapping("/dec")
    @ResponseBody
    public AjaxResult dec(String data) throws Exception {
        if (StringUtils.isBlank(data)) {
            return error("右侧数据不能为空");
        }
        try {
            JSONObject jsonObject = JSON.parseObject(data);
            jsonObject = ProductPrivacyKey.privacyDecryptMer(jsonObject);
            data = jsonObject.toJSONString();
            return AjaxResult.success("操作成功", data);
        }catch (Exception e){

        }

        data = AESUtil.decrypt(data);

        if (StringUtils.isBlank(data)) {
            return AjaxResult.error("操作失败", "解密失败");
        }
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 加压
     *
     * @throws Exception
     */
    @RequiresPermissions("xh:dataUtil:zip")
    @Log(title = "加压", businessType = BusinessType.OTHER)
    @PostMapping("/zip")
    @ResponseBody
    public AjaxResult zip(String data) throws Exception {
        if (StringUtils.isBlank(data)) {
            return error("左侧数据不能为空");
        }
        data = ZipStrUtils.gzip(data);

        return AjaxResult.success("操作成功", data);
    }

    /**
     * 解压
     *
     * @throws Exception
     */
    @RequiresPermissions("xh:dataUtil:unzip")
    @Log(title = "解压", businessType = BusinessType.OTHER)
    @PostMapping("/unzip")
    @ResponseBody
    public AjaxResult unzip(String data) throws Exception {
        if (StringUtils.isBlank(data)) {
            return error("右侧数据不能为空");
        }
        data = ZipStrUtils.gunzip(data);

        return AjaxResult.success("操作成功", data);
    }


    public static void main(String[] args) {
        System.out.println(JSON.isValid("127775ggg3"));
        System.out.println(JSON.isValid("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}"));
        JSONObject jsonObject = JSON.parseObject("123");

    }
}
