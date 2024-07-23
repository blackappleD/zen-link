package com.mkc.controller.xh;

import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.MerAmountRecord;
import com.mkc.domain.MerInfo;
import com.mkc.service.IMerAmountRecordService;
import com.mkc.service.IMerInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户余额操作记录Controller
 * 
 * @author atd
 * @date 2023-04-24
 */
@Controller
@RequestMapping("/xh/merAmountRecord")
public class MerAmountRecordController extends BaseController
{
    private String prefix = "xh/merAmountRecord";

    @Autowired
    private IMerAmountRecordService merAmountRecordService;

    @Autowired
    private IMerInfoService merInfoService;

    @RequiresPermissions("xh:merAmountRecord:view")
    @GetMapping()
    public String merAmountRecord(ModelMap mmap)
    {

        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.addAttribute("merInfos",merInfos);

        return prefix + "/merAmountRecord";
    }

    /**
     * 查询商户余额操作记录列表
     */
    @RequiresPermissions("xh:merAmountRecord:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MerAmountRecord merAmountRecord)
    {
        startPage();
        List<MerAmountRecord> list = merAmountRecordService.selectMerAmountRecordList(merAmountRecord);
        return getDataTable(list);
    }

    /**
     * 导出商户余额操作记录列表
     */
    @RequiresPermissions("xh:merAmountRecord:export")
    @Log(title = "商户余额操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MerAmountRecord merAmountRecord)
    {
        List<MerAmountRecord> list = merAmountRecordService.selectMerAmountRecordList(merAmountRecord);
        ExcelUtil<MerAmountRecord> util = new ExcelUtil<MerAmountRecord>(MerAmountRecord.class);
        return util.exportExcel(list, "商户余额操作记录数据");
    }

    /**
     * 新增商户余额操作记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存商户余额操作记录
     */
    @RequiresPermissions("xh:merAmountRecord:add")
    @Log(title = "商户余额操作记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MerAmountRecord merAmountRecord)
    {
        merAmountRecord.setCreateBy(getLoginName());
        merAmountRecord.setUpdateBy(getLoginName());
        return toAjax(merAmountRecordService.insertMerAmountRecord(merAmountRecord));
    }

    /**
     * 修改商户余额操作记录
     */
    @RequiresPermissions("xh:merAmountRecord:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        MerAmountRecord merAmountRecord = merAmountRecordService.selectMerAmountRecordById(id);
        mmap.put("merAmountRecord", merAmountRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存商户余额操作记录
     */
    @RequiresPermissions("xh:merAmountRecord:edit")
    @Log(title = "商户余额操作记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MerAmountRecord merAmountRecord)
    {
        merAmountRecord.setUpdateBy(getLoginName());
        return toAjax(merAmountRecordService.updateMerAmountRecord(merAmountRecord));
    }

    /**
     * 删除商户余额操作记录
     */
    @RequiresPermissions("xh:merAmountRecord:remove")
    @Log(title = "商户余额操作记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(merAmountRecordService.deleteMerAmountRecordByIds(ids));
    }
}
