package com.mkc.controller.xh;


import com.anji.captcha.util.StringUtils;
import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.Product;
import com.mkc.domain.Supplier;
import com.mkc.domain.SupplierProduct;
import com.mkc.service.IProductService;
import com.mkc.service.ISupplierProductService;
import com.mkc.service.ISupplierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商产品信息管理Controller
 * 
 * @author atd
 * @date 2023-04-24
 */
@Controller
@RequestMapping("/xh/supProduct")
public class SupplierProductController extends BaseController
{
    private String prefix = "xh/supProduct";

    @Autowired
    private ISupplierProductService supplierProductService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ISupplierService supplierService;

    @RequiresPermissions("xh:supProduct:view")
    @GetMapping()
    public String supProduct(ModelMap mmap)
    {
        List<Supplier> suppliers = supplierService.selectSupplierList(null);
        mmap.addAttribute("suppliers",suppliers);
        List<Product> products = productService.selectProductList(null);
        mmap.addAttribute("products",products);
        return prefix + "/supProduct";
    }

    /**
     * 查询供应商产品信息管理列表
     */
    @RequiresPermissions("xh:supProduct:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SupplierProduct supplierProduct)
    {
        startPage();
        List<SupplierProduct> list = supplierProductService.selectSupplierProductList(supplierProduct);
        return getDataTable(list);
    }

    /**
     * 导出供应商产品信息管理列表
     */
    @RequiresPermissions("xh:supProduct:export")
    @Log(title = "供应商产品信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SupplierProduct supplierProduct)
    {
        List<SupplierProduct> list = supplierProductService.selectSupplierProductList(supplierProduct);
        ExcelUtil<SupplierProduct> util = new ExcelUtil<SupplierProduct>(SupplierProduct.class);
        return util.exportExcel(list, "供应商产品信息管理数据");
    }

    /**
     * 新增供应商产品信息管理
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {

        List<Supplier> suppliers = supplierService.selectSupplierList(null);
        mmap.addAttribute("suppliers",suppliers);
        List<Product> products = productService.selectProductList(null);
        mmap.addAttribute("products",products);

        return prefix + "/add";
    }

    /**
     * 新增保存供应商产品信息管理
     */
    @RequiresPermissions("xh:supProduct:add")
    @Log(title = "供应商产品信息管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SupplierProduct supplierProduct)
    {

        String supCode = supplierProduct.getSupCode();
        String productCode = supplierProduct.getProductCode();
        if(StringUtils.isBlank(supCode)){
            return error("缺少供应商参数。");
        }
        if(StringUtils.isBlank(productCode)){
            return error("缺少关联产品参数。");
        }
        SupplierProduct qeury=new SupplierProduct();
        qeury.setProductCode(productCode);
        qeury.setSupCode(supCode);
        List<SupplierProduct> supplierProducts = supplierProductService.selectSupplierProductList(qeury);
        if(supplierProducts!=null && supplierProducts.size()>0){
            return error("当前供应商已和此产品进行过关联，请确认。");
        }
        qeury.setProductCode(null);
        qeury.setSupProductCode(supplierProduct.getSupProductCode());
        supplierProducts = supplierProductService.selectSupplierProductList(qeury);
        if(supplierProducts!=null && supplierProducts.size()>0){
            return error("当前供应商已添加该产品，请检查供应产品code是否正确。"+supplierProduct.getSupProductCode());
        }
        Supplier supplier = supplierService.selectSupplierByCode(supCode);
        supplierProduct.setSupName(supplier.getName());
        supplierProduct.setCreateBy(getLoginName());
        supplierProduct.setUpdateBy(getLoginName());
        //Product product = productService.selectProductByCode(supplierProduct.getProductCode());
       // supplierProduct.setProductName(product.getProductName());
        return toAjax(supplierProductService.insertSupplierProduct(supplierProduct));
    }

    /**
     * 修改供应商产品信息管理
     */
    @RequiresPermissions("xh:supProduct:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SupplierProduct supplierProduct = supplierProductService.selectSupplierProductById(id);
        Product product = productService.selectProductByCode(supplierProduct.getProductCode());
        supplierProduct.setProductName(product.getProductName());
        mmap.put("supplierProduct", supplierProduct);
        return prefix + "/edit";
    }

    /**
     * 修改保存供应商产品信息管理
     */
    @RequiresPermissions("xh:supProduct:edit")
    @Log(title = "供应商产品信息管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SupplierProduct supplierProduct)
    {
        supplierProduct.setUpdateBy(getLoginName());
        return toAjax(supplierProductService.updateSupplierProduct(supplierProduct));
    }

    /**
     * 删除供应商产品信息管理
     */
    @RequiresPermissions("xh:supProduct:remove")
    @Log(title = "供应商产品信息管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(supplierProductService.deleteSupplierProductByIds(ids));
    }


    /**
     * 发送商户秘钥信息
     */
    @PostMapping("/querySupProduct")
    @ResponseBody
    public AjaxResult querySupProduct(@RequestParam("supCode") String supCode, @RequestParam("productCode") String productCode)
    {

        SupplierProduct supplierProduct=new SupplierProduct();
        supplierProduct.setProductCode(productCode);
        supplierProduct.setSupCode(supCode);
        List<SupplierProduct> list = supplierProductService.selectSupplierProductList(supplierProduct);
        if(ObjectUtils.isEmpty(list)){
            return error("没有查到相关供应商产品信息");
        }
        return success(list.get(0));
    }
}
