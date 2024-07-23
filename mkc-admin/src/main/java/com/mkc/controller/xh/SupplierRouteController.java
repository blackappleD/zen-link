package com.mkc.controller.xh;

import com.mkc.common.annotation.Log;
import com.mkc.common.enums.OnOffState;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.MerInfo;
import com.mkc.domain.Product;
import com.mkc.domain.Supplier;
import com.mkc.domain.SupplierRoute;
import com.mkc.service.IMerInfoService;
import com.mkc.service.IProductService;
import com.mkc.service.ISupplierRouteService;
import com.mkc.service.ISupplierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商路由Controller
 * 
 * @author atd
 * @date 2023-04-24
 */
@Controller
@RequestMapping("/xh/supRoute")
public class SupplierRouteController extends BaseController
{
    private String prefix = "xh/supRoute";

    @Autowired
    private ISupplierRouteService supplierRouteService;


    @Autowired
    private IProductService productService;

    @Autowired
    private IMerInfoService merInfoService;

    @Autowired
    private ISupplierService supplierService;

    @RequiresPermissions("xh:supRoute:view")
    @GetMapping()
    public String supRoute(ModelMap mmap)
    {
        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.addAttribute("merInfos",merInfos);
        List<Product> products = productService.selectProductList(null);
        mmap.addAttribute("products",products);
        return prefix + "/supRoute";
    }

    /**
     * 查询供应商路由列表
     */
    @RequiresPermissions("xh:supRoute:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SupplierRoute supplierRoute)
    {
        startPage();
        List<SupplierRoute> list = supplierRouteService.selectSupplierRouteList(supplierRoute);
        return getDataTable(list);
    }

    /**
     * 导出供应商路由列表
     */
    @RequiresPermissions("xh:supRoute:export")
    @Log(title = "供应商路由", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SupplierRoute supplierRoute)
    {
        List<SupplierRoute> list = supplierRouteService.selectSupplierRouteList(supplierRoute);
        ExcelUtil<SupplierRoute> util = new ExcelUtil<SupplierRoute>(SupplierRoute.class);
        return util.exportExcel(list, "供应商路由数据");
    }

    /**
     * 新增供应商路由
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
//        Supplier query=new Supplier();
//        //只查询启用的供应商
//        query.setStatus(OnOffState.STATE_ON);
//        List<Supplier> suppliers = supplierService.selectSupplierList(query);
//        mmap.addAttribute("suppliers",suppliers);

        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.addAttribute("merInfos",merInfos);
        List<Product> products = productService.selectProductList(null);
        mmap.addAttribute("products",products);

        return prefix + "/add";
    }

    /**
     * 新增保存供应商路由
     */
    @RequiresPermissions("xh:supRoute:add")
    @Log(title = "供应商路由", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SupplierRoute supplierRoute)
    {
        supplierRoute.setUpdateBy(getLoginName());
        supplierRoute.setCreateBy(getLoginName());
        Supplier supplier = supplierService.selectSupplierByCode(supplierRoute.getSupCode());
        supplierRoute.setSupName(supplier.getName());
        return toAjax(supplierRouteService.insertSupplierRoute(supplierRoute));
    }

    /**
     * 修改供应商路由
     */
    @RequiresPermissions("xh:supRoute:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SupplierRoute supplierRoute = supplierRouteService.selectSupplierRouteById(id);
        mmap.put("supplierRoute", supplierRoute);
        return prefix + "/edit";
    }

    /**
     * 修改保存供应商路由
     */
    @RequiresPermissions("xh:supRoute:edit")
    @Log(title = "供应商路由", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SupplierRoute supplierRoute)
    {
        supplierRoute.setUpdateBy(getLoginName());
        return toAjax(supplierRouteService.updateSupplierRoute(supplierRoute));
    }

    /**
     * 删除供应商路由
     */
    @RequiresPermissions("xh:supRoute:remove")
    @Log(title = "供应商路由", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(supplierRouteService.deleteSupplierRouteByIds(ids));
    }
}
