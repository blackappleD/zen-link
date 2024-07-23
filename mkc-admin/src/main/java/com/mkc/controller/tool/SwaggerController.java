package com.mkc.controller.tool;

import com.mkc.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * swagger 表单构建
 * 
 * @author  atd
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController
{
    private String prefix = "/swagger-ui";

    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index()
    {
        return redirect(prefix + "/index.html");
    }
}
