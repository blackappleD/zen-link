package com.mkc.controller.xh;

import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.MerInfo;
import com.mkc.domain.Product;
import com.mkc.domain.ProductSell;
import com.mkc.domain.Supplier;
import com.mkc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * 产品销售Controller
 * 
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@Controller
@RequestMapping("/xh/productSell")
public class ProductSellController extends BaseController
{
    private String prefix = "xh/productSell";

    @Autowired
    private IProductSellService productSellService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IMerInfoService merInfoService;

    @Autowired
    private ISupplierService supplierService;



    @Autowired
    private ISupplierRouteService supplierRouteService;

    @RequiresPermissions("xh:productSell:view")
    @GetMapping()
    public String productSell(ModelMap mmap)
    {
        // 获取商户列表
        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.addAttribute("merInfos", merInfos);
        
        // 添加产品列表数据
        List<Product> products = productService.selectProductList(null);
        mmap.addAttribute("products", products);
        
        return prefix + "/productSell";
    }

    /**
     * 查询产品销售列表
     */
    @RequiresPermissions("xh:productSell:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProductSell productSell)
    {
        startPage();
        List<ProductSell> list = productSellService.selectProductSellList(productSell);
        return getDataTable(list);
    }

    /**
     * 导出产品销售列表
     */
    @RequiresPermissions("xh:productSell:export")
    @Log(title = "产品销售", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProductSell productSell)
    {
        List<ProductSell> list = productSellService.selectProductSellList(productSell);
        ExcelUtil<ProductSell> util = new ExcelUtil<ProductSell>(ProductSell.class);
        return util.exportExcel(list, "产品销售数据");
    }

    /**
     * 新增产品销售
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.addAttribute("merInfos",merInfos);
//        List<Product> products = productService.selectProductList(null);
//        mmap.addAttribute("products",products);
        return prefix + "/add";
    }
    /**
     * 根据分类Code查询列表
     */
    @ResponseBody
    @GetMapping("/productSellListByCgCode/{cgCode}")
    public List<ProductSell> productSellListByCgCode(@PathVariable("cgCode") String cgCode)
    {
      //  Product product=new Product();
       // product.setCgCode(cgCode);
       // List<Product> products = productService.selectProductList(product);
        ProductSell sell=new ProductSell();
        sell.setCgCode(cgCode);
        List<ProductSell> list = productSellService.selectProductSellList(sell);

        List<ProductSell> productSells = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(ProductSell::getProductCode))), ArrayList::new));

        return productSells;
    }
    @ResponseBody
    @RequestMapping("/productSellListByParm")
    public List<ProductSell> productSellListByParm(String cgCode,String merCode)
    {
        ProductSell sell=new ProductSell();
        sell.setCgCode(cgCode);
        sell.setMerCode(merCode);
        List<ProductSell> list = productSellService.selectProductSellList(sell);

        List<ProductSell> productSells = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(ProductSell::getProductCode))), ArrayList::new));

        return productSells;
    }


    @ResponseBody
    @GetMapping("/productSellListByCode/{productCode}")
    public List<ProductSell> productListByCode(@PathVariable("productCode") String productCode)
    {
        ProductSell sell=new ProductSell();
        sell.setProductCode(productCode);
        List<ProductSell> list = productSellService.selectProductSellList(sell);

        return list;
    }

    /**
     * 新增保存产品销售
     */
    @RequiresPermissions("xh:productSell:add")
    @Log(title = "产品销售", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ProductSell productSell)
    {
        productSell.setCreateBy(getLoginName());
        productSell.setUpdateBy(getLoginName());
        String productCode = productSell.getProductCode();
        String merCode = productSell.getMerCode();
        ProductSell query=new ProductSell();
        query.setMerCode(merCode);
        query.setProductCode(productCode);
        List<ProductSell> productSells = productSellService.selectProductSellList(query);
        if(productSells!=null && productSells.size()>0){
            return error("该商户此产品已经配置销售信息。");
        }
        Product product = productService.selectProductByCode(productCode);
        productSell.setCgCode(product.getCgCode());
        productSell.setProductName(product.getProductName());

        //ProductSell productSell = productSellService.selectProductSellById(newProductSell.getId());

        int num=productSellService.insertProductSell(productSell);
        String supNames = productSell.getSupNames();
        if( StringUtils.isNotBlank(supNames)){
            ProductSell newProductSell = productSellService.selectProductSellById(productSell.getId());
            if(newProductSell!=null){
                newProductSell.setSupNames(supNames);
                supplierRouteService.batchAdd(newProductSell);
            }
        }

        return toAjax(num);

    }

    /**
     * 修改产品销售
     */
    @RequiresPermissions("xh:productSell:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        //ProductSell productSell = productSellService.selectProductSellById(id);
        ProductSell productSell = productSellService.selectProductSellAndRutesById(id);
        MerInfo merInfo = merInfoService.selectMerInfoByCode(productSell.getMerCode());
        productSell.setMerName(merInfo.getMerName());
        mmap.put("productSell", productSell);

        List<Supplier> suppliers = supplierService.selectSupListByproCode(productSell.getProductCode());
        mmap.put("suppliers",suppliers);

        return prefix + "/edit";
    }

    /**
     * 修改保存产品销售
     */
    @RequiresPermissions("xh:productSell:edit")
    @Log(title = "产品销售", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ProductSell productSell)
    {
        productSell.setUpdateBy(getLoginName());
        return toAjax(productSellService.updateProductSell(productSell));
    }
    /**
     * 修改产品供应商路由
     */
    @RequiresPermissions("xh:productSell:route")
    @GetMapping("/route/{id}")
    public String route(@PathVariable("id") Long id, ModelMap mmap)
    {
        ProductSell productSell = productSellService.selectProductSellAndRutesById(id);
        MerInfo merInfo = merInfoService.selectMerInfoByCode(productSell.getMerCode());
        productSell.setMerName(merInfo.getMerName());
        mmap.put("productSell", productSell);

        List<Supplier> suppliers = supplierService.selectSupListByproCode(productSell.getProductCode());
        mmap.put("suppliers",suppliers);

        return prefix + "/route";
    }




    /**
     * 修改保存产品供应商路由
     */
    @RequiresPermissions("xh:productSell:route")
    @Log(title = "产品销售", businessType = BusinessType.UPDATE)
    @PostMapping("/route")
    @ResponseBody
    public AjaxResult routeSave(ProductSell newProductSell)
    {

        ProductSell productSell = productSellService.selectProductSellById(newProductSell.getId());

        productSell.setUpdateBy(getLoginName());
        productSell.setSupNames(newProductSell.getSupNames());

        return toAjax(supplierRouteService.batchAdd(productSell));
    }

    /**
     * 修改保存产品供应商路由
     */
    @RequiresPermissions("xh:productSell:batchRoute")
    @Log(title = "产品销售", businessType = BusinessType.UPDATE)
    @PostMapping("/bathcRouteAdd")
    @ResponseBody
    public AjaxResult bathcRouteAdd(ProductSell newProductSell)
    {

        log.info(" bathcRouteAdd ========== {}",newProductSell);


        newProductSell.setUpdateBy(getLoginName());
        int i = supplierRouteService.batchAdd(newProductSell);
        return toAjax(i);
    }



    @RequiresPermissions("xh:productSell:batchRoute")
    @GetMapping("/batchRoute")
    public String batchRoute( ModelMap mmap)
    {

        return prefix + "/batchRoute";
    }




    /**
     * 删除产品销售
     */
    @RequiresPermissions("xh:productSell:remove")
    @Log(title = "产品销售", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(productSellService.deleteProductSellByIds(ids));
    }
}
