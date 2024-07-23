package com.mkc.controller.xh;

import com.mkc.bean.MerReqLogBean;
import com.mkc.bean.SupReqLogBean;
import com.mkc.common.annotation.Log;
import com.mkc.common.core.controller.BaseController;
import com.mkc.common.core.domain.AjaxResult;
import com.mkc.common.core.page.TableDataInfo;
import com.mkc.common.enums.BusinessType;
import com.mkc.common.utils.poi.ExcelUtil;
import com.mkc.domain.MerInfo;
import com.mkc.domain.MerReqLog;
import com.mkc.domain.SupReqLog;
import com.mkc.service.IMerInfoService;
import com.mkc.service.IMerReqLogService;
import com.mkc.service.ISupReqLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户调用日志Controller
 * 
 * @author atd
 * @date 2023-04-24
 */
@Controller
@Slf4j
@RequestMapping("/xh/merLog")
public class MerReqLogController extends BaseController
{
    private String prefix = "xh/merLog";

    @Autowired
    private IMerReqLogService merReqLogService;

    @Autowired
    private ISupReqLogService supReqLogService;

    @Autowired
    private IMerInfoService merInfoService;

    @RequiresPermissions("xh:merLog:view")
    @GetMapping()
    public String merLog(ModelMap mmap)
    {

        List<MerInfo> merInfos = merInfoService.selectMerInfoList(null);
        mmap.addAttribute("merInfos",merInfos);

        return prefix + "/merLog";
    }

    /**
     * 查询商户调用日志列表
     */
    @RequiresPermissions("xh:merLog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MerReqLogBean merReqLog)
    {
        try{
            startPage();
            List<MerReqLog> list = merReqLogService.selectMerReqLogList(merReqLog);
            return getDataTable(list);
        }catch (Exception e){
            log.error("查询商户调用日志列表发生异常 ",e);
            throw e;
        }

    }

    /**
     * 导出商户调用日志列表
     */
    @RequiresPermissions("xh:merLog:export")
    @Log(title = "商户调用日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MerReqLogBean merReqLog)
    {
        List<MerReqLog> list = merReqLogService.selectMerReqLogList(merReqLog);
        ExcelUtil<MerReqLog> util = new ExcelUtil<MerReqLog>(MerReqLog.class);
        return util.exportExcel(list, "商户调用日志数据");
    }

    /**
     * 新增商户调用日志
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存商户调用日志
     */
    @RequiresPermissions("xh:merLog:add")
    @Log(title = "商户调用日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MerReqLog merReqLog)
    {
       // toAjax(merReqLogService.insertMerReqLog(merReqLog))
        return toAjax(0);
    }

    /**
     * 修改商户调用日志
     */
    @RequiresPermissions("xh:merLog:edit")
    @GetMapping("/edit/{orderNo}")
    public String edit(@PathVariable("orderNo") String orderNo, ModelMap mmap)
    {
        MerReqLog merReqLog = merReqLogService.selectMerReqLogByOrderNo(orderNo);
        mmap.put("merReqLog", merReqLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存商户调用日志
     */
    @RequiresPermissions("xh:merLog:edit")
    @Log(title = "商户调用日志", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MerReqLog merReqLog)
    {

        //return toAjax(merReqLogService.updateMerReqLog(merReqLog));

        return toAjax(1);
    }

    /**
     * 根据流水号查询 供应商路由日志
     */
    @GetMapping("/supReqLog/{orderNo}")
    public String supReqLog(@PathVariable("orderNo") String orderNo, ModelMap mmap)
    {
        mmap.put("orderNo", orderNo);
        log.info("orderNo  {} ", orderNo);
        return prefix + "/supLogList";
    }


    /**
     * 查询商户请求供应商日志列表
     */
    @RequestMapping("/supLogList/{orderNo}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("orderNo") String orderNo)
    {
        startPage();
        SupReqLogBean supReqLog = new SupReqLogBean();
        supReqLog.setOrderNo(orderNo);
        List<SupReqLog> supReqLogs = supReqLogService.selectSupReqLogList(supReqLog);
        return getDataTable(supReqLogs);
    }




    /**
     * 删除商户调用日志
     */
    @RequiresPermissions("xh:merLog:remove")
    @Log(title = "商户调用日志", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(merReqLogService.deleteMerReqLogByIds(ids));
    }




}
