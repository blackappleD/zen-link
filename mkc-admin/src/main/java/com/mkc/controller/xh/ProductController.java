package com.mkc.controller.xh;

import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.Product;
import com.mkc.domain.ProductSell;
import com.mkc.service.IProductService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 产品Controller
 * 
 * @author atd
 * @date 2023-04-24
 */
@Controller
@RequestMapping("/xh/product")
public class ProductController extends BaseController
{
    private String prefix = "xh/product";

    @Autowired
    private IProductService productService;

    @RequiresPermissions("xh:product:view")
    @GetMapping()
    public String product()
    {
        return prefix + "/product";
    }

    /**
     * 查询产品列表
     */
    @RequiresPermissions("xh:product:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Product product)
    {
        startPage();
        List<Product> list = productService.selectProductList(product);
        return getDataTable(list);
    }


    /**
     * 根据分类Code查询列表
     */
    @ResponseBody
    @GetMapping("/productsByCgCode/{cgCode}")
    public List<Product> productsByCgCode(@PathVariable("cgCode") String cgCode)
    {
         Product product=new Product();
         product.setCgCode(cgCode);
         List<Product> products = productService.selectProductList(product);

        return products;
    }


    /**
     * 导出产品列表
     */
    @RequiresPermissions("xh:product:export")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Product product)
    {
        List<Product> list = productService.selectProductList(product);
        ExcelUtil<Product> util = new ExcelUtil<Product>(Product.class);
        return util.exportExcel(list, "产品数据");
    }

    /**
     * 新增产品
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存产品
     */
    @RequiresPermissions("xh:product:add")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Product product)
    {
        product.setCreateBy(getLoginName());
        product.setUpdateBy(getLoginName());
        int i = productService.insertProduct(product);
        return toAjax(i);
    }

    /**
     * 修改产品
     */
    @RequiresPermissions("xh:product:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Product product = productService.selectProductById(id);
        mmap.put("product", product);
        return prefix + "/edit";
    }

    /**
     * 修改保存产品
     */
    @RequiresPermissions("xh:product:edit")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Product product)
    {
        product.setUpdateBy(getLoginName());
        return toAjax(productService.updateProduct(product));
    }

    /**
     * 删除产品
     */
    @RequiresPermissions("xh:product:remove")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(productService.deleteProductByIds(ids));
    }
}
