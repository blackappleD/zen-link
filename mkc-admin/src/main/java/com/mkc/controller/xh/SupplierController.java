package com.mkc.controller.xh;

import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.Product;
import com.mkc.domain.Supplier;
import com.mkc.service.ISupplierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商信息管理Controller
 * 
 * @author atd
 * @date 2023-04-24
 */
@Controller
@RequestMapping("/xh/supplier")
public class SupplierController extends BaseController
{
    private String prefix = "xh/supplier";

    @Autowired
    private ISupplierService supplierService;

    @RequiresPermissions("xh:supplier:view")
    @GetMapping()
    public String supplier()
    {
        return prefix + "/supplier";
    }

    /**
     * 查询供应商信息管理列表
     */
    @RequiresPermissions("xh:supplier:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Supplier supplier)
    {
        startPage();
        List<Supplier> list = supplierService.selectSupplierList(supplier);
        return getDataTable(list);
    }

    /**
     * 根据产品Code查询 可用供应商 列表
     */
    @ResponseBody
    @GetMapping("/supListByProCode/{productCode}")
    public List<Supplier> querySupListByProCode(@PathVariable("productCode") String productCode)
    {
        List<Supplier> suppliers = supplierService.selectSupListByproCode(productCode);

        return suppliers;
    }



    /**
     * 导出供应商信息管理列表
     */
    @RequiresPermissions("xh:supplier:export")
    @Log(title = "供应商信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Supplier supplier)
    {
        List<Supplier> list = supplierService.selectSupplierList(supplier);
        ExcelUtil<Supplier> util = new ExcelUtil<Supplier>(Supplier.class);
        return util.exportExcel(list, "供应商信息管理数据");
    }

    /**
     * 新增供应商信息管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存供应商信息管理
     */
    @RequiresPermissions("xh:supplier:add")
    @Log(title = "供应商信息管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Supplier supplier)
    {
        supplier.setCreateBy(getLoginName());
        supplier.setUpdateBy(getLoginName());
        return toAjax(supplierService.insertSupplier(supplier));
    }

    /**
     * 修改供应商信息管理
     */
    @RequiresPermissions("xh:supplier:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Supplier supplier = supplierService.selectSupplierById(id);
        mmap.put("supplier", supplier);
        return prefix + "/edit";
    }

    /**
     * 修改保存供应商信息管理
     */
    @RequiresPermissions("xh:supplier:edit")
    @Log(title = "供应商信息管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Supplier supplier)
    {
        supplier.setUpdateBy(getLoginName());
        return toAjax(supplierService.updateSupplier(supplier));
    }

    /**
     * 删除供应商信息管理
     */
    @RequiresPermissions("xh:supplier:remove")
    @Log(title = "供应商信息管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(supplierService.deleteSupplierByIds(ids));
    }
}
