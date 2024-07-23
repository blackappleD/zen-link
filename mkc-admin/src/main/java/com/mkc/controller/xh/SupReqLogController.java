package com.mkc.controller.xh;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mkc.bean.SupReqLogBean;
import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.MerInfo;
import com.mkc.domain.SupReqLog;
import com.mkc.domain.Supplier;
import com.mkc.service.IMerInfoService;
import com.mkc.service.ISupReqLogService;
import com.mkc.service.ISupplierService;

/**
 * 调用供应商日志Controller
 *
 * @author atd
 * @date 2023-04-24
 */
@Controller
@RequestMapping("/xh/supLog")
public class SupReqLogController extends BaseController {
    private String prefix = "xh/supLog";

    @Autowired
    private ISupReqLogService supReqLogService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private IMerInfoService merInfoService;

    @RequiresPermissions("xh:supLog:view")
    @GetMapping()
    public String supLog(ModelMap mmap) {

        List<Supplier> suppliers = supplierService.selectSupplierList(null);
        mmap.addAttribute("suppliers", suppliers);

//        List<Product> products = productService.selectProductList(null);
//        mmap.addAttribute("products",products);

        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.addAttribute("merInfos",merInfos);

        return prefix + "/supLog";
    }

    /**
     * 查询调用供应商日志列表
     */
    @RequiresPermissions("xh:supLog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SupReqLogBean supReqLog) {
        startPage();
        List<SupReqLog> list = supReqLogService.selectSupReqLogList(supReqLog);
        return getDataTable(list);
    }

    /**
     * 导出调用供应商日志列表
     */
    @RequiresPermissions("xh:supLog:export")
    @Log(title = "调用供应商日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SupReqLogBean supReqLog) {
        List<SupReqLog> list = supReqLogService.selectSupReqLogList(supReqLog);
        ExcelUtil<SupReqLog> util = new ExcelUtil<SupReqLog>(SupReqLog.class);
        return util.exportExcel(list, "调用供应商日志数据");
    }

    /**
     * 新增调用供应商日志
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存调用供应商日志
     */
    @RequiresPermissions("xh:supLog:add")
    @Log(title = "调用供应商日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SupReqLog supReqLog) {
        return toAjax(supReqLogService.insertSupReqLog(supReqLog));
    }

    /**
     * 修改调用供应商日志
     */
    @RequiresPermissions("xh:supLog:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SupReqLog supReqLog = supReqLogService.selectSupReqLogById(id);
        mmap.put("supReqLog", supReqLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存调用供应商日志
     */
    @RequiresPermissions("xh:supLog:edit")
    @Log(title = "调用供应商日志", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SupReqLog supReqLog) {
        return toAjax(supReqLogService.updateSupReqLog(supReqLog));
    }

    /**
     * 删除调用供应商日志
     */
    @RequiresPermissions("xh:supLog:remove")
    @Log(title = "调用供应商日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(supReqLogService.deleteSupReqLogByIds(ids));
    }
}
