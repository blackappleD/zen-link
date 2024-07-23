package com.mkc.controller.xh;

import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.enums.MerAmountType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.MerInfo;
import com.mkc.service.IMerInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商户信息管理Controller
 *
 * @author atd
 * @date 2023-04-24
 */
@Controller
@RequestMapping("/xh/merInfo")
public class MerInfoController extends BaseController
{
    private String prefix = "xh/merInfo";

    @Autowired
    private IMerInfoService merInfoService;

    @RequiresPermissions("xh:merInfo:view")
    @GetMapping()
    public String merInfo(ModelMap mmap)
    {
        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.put("merInfos",merInfos);
        return prefix + "/merInfo";
    }

    /**
     * 查询商户信息管理列表
     */
    @RequiresPermissions("xh:merInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MerInfo merInfo)
    {
        startPage();
        List<MerInfo> list = merInfoService.selectMerInfoList(merInfo);
        return getDataTable(list);
    }

    /**
     * 导出商户信息管理列表
     */
    @RequiresPermissions("xh:merInfo:export")
    @Log(title = "商户信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MerInfo merInfo)
    {
        List<MerInfo> list = merInfoService.selectMerInfoList(merInfo);
        ExcelUtil<MerInfo> util = new ExcelUtil<MerInfo>(MerInfo.class);
        return util.exportExcel(list, "商户信息管理数据");
    }

    /**
     * 新增商户信息管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存商户信息管理
     */
    @RequiresPermissions("xh:merInfo:add")
    @Log(title = "商户信息管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MerInfo merInfo)
    {
        String merCode = merInfo.getMerCode();
        MerInfo old_merInfo = merInfoService.selectMerInfoByCode(merCode);
        if (old_merInfo != null) {
            return error("该商户Code 已被使用： "+merCode);
        }
        merInfo.setCreateBy(getLoginName());
        merInfo.setUpdateBy(getLoginName());
        int i = merInfoService.insertMerInfo(merInfo);

        return toAjax(i);
    }

    /**
     * 商户信息详情
     */
    //@RequiresPermissions("xh:merInfo:edit")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        MerInfo merInfo = merInfoService.selectMerInfoById(id);
        mmap.put("merInfo", merInfo);
        return prefix + "/detail";
    }
    /**
     * 修改商户信息管理
     */
    @RequiresPermissions("xh:merInfo:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        MerInfo merInfo = merInfoService.selectMerInfoById(id);
        mmap.put("merInfo", merInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存商户信息管理
     */
    @RequiresPermissions("xh:merInfo:edit")
    @Log(title = "商户信息管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MerInfo merInfo)
    {
        merInfo.setUpdateBy(getLoginName());
        merInfo.setBalance(null);
        return toAjax(merInfoService.updateMerInfo(merInfo));
    }

    /**
     * 删除商户信息管理
     */
    @RequiresPermissions("xh:merInfo:remove")
    @Log(title = "商户信息管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(merInfoService.deleteMerInfoByIds(ids));
    }


    /**
     * 修改商户信息管理
     */
    @RequiresPermissions("xh:merInfo:topup")
    @GetMapping("/topup/{id}")
    public String topup(@PathVariable("id") Long id, ModelMap mmap)
    {
        MerInfo merInfo = merInfoService.selectMerInfoById(id);
        mmap.put("merInfo", merInfo);
        return prefix + "/topup";
    }


    /**
     * 商户秘钥信息展示
     */
    @RequiresPermissions("xh:merInfo:key")
    @GetMapping("/merKey/{id}")
    public String key(@PathVariable("id") Long id, ModelMap mmap)
    {
        MerInfo merInfo = merInfoService.selectMerInfoById(id);
        mmap.put("merInfo", merInfo);
        return prefix + "/key";
    }

    /**
     * 发送商户秘钥信息
     */
    @RequiresPermissions("xh:merInfo:sendMail")
    @GetMapping("/sendMerKeyMail/{id}")
    @ResponseBody
    public AjaxResult sendMerKeyMail(@PathVariable("id") Long id, ModelMap mmap)
    {
        merInfoService.sendMerKeyMail(id);

        return success();
    }



    /**
     * 修改保存商户信息管理
     */
    @RequiresPermissions("xh:merInfo:topup")
    @Log(title = "商户信息管理", businessType = BusinessType.UPDATE)
    @PostMapping("/topup")
    @ResponseBody
    public AjaxResult topupSave(String merCode, BigDecimal upAmount, String upType,String remark) {

        if(StringUtils.isBlank(merCode)){
            return error("缺少参数 merCode");
        }
        MerAmountType merType = MerAmountType.findByCode(upType);
        if(merType==null){
            return error("缺少参数 merType");
        }
        String updBy=getLoginName();

        return toAjax( merInfoService.updMerBalance(merCode,upAmount,merType,updBy, remark));
    }


}
