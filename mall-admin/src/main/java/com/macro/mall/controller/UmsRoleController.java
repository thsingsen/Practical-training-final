package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.*;
import com.macro.mall.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户角色管理接口
 */
@Controller
@Api(tags = "UmsRoleController")
@Tag(name = "UmsRoleController", description = "管理后台用户角色")
@RequestMapping("/role")
public class UmsRoleController {
    @Autowired
    private UmsRoleService roleService;

    @ApiOperation("新增角色")
    @PostMapping("/create")
    @ResponseBody
    public CommonResult create(@RequestBody UmsRole role) {
        int created = roleService.create(role);
        return created > 0 ? CommonResult.success(created) : CommonResult.failed();
    }

    @ApiOperation("更新角色信息")
    @PostMapping("/update/{id}")
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsRole role) {
        int updated = roleService.update(id, role);
        return updated > 0 ? CommonResult.success(updated) : CommonResult.failed();
    }

    @ApiOperation("批量移除角色")
    @PostMapping("/delete")
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int deleted = roleService.delete(ids);
        return deleted > 0 ? CommonResult.success(deleted) : CommonResult.failed();
    }

    @ApiOperation("获取所有角色信息")
    @GetMapping("/listAll")
    @ResponseBody
    public CommonResult<List<UmsRole>> listAll() {
        List<UmsRole> roles = roleService.list();
        return CommonResult.success(roles);
    }

    @ApiOperation("按角色名称分页查询")
    @GetMapping("/list")
    @ResponseBody
    public CommonResult<CommonPage<UmsRole>> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsRole> roles = roleService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(roles));
    }

    @ApiOperation("更改角色状态")
    @PostMapping("/updateStatus/{id}")
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsRole role = new UmsRole();
        role.setStatus(status);
        int updated = roleService.update(id, role);
        return updated > 0 ? CommonResult.success(updated) : CommonResult.failed();
    }

    @ApiOperation("获取角色关联的菜单")
    @GetMapping("/listMenu/{roleId}")
    @ResponseBody
    public CommonResult<List<UmsMenu>> listMenu(@PathVariable Long roleId) {
        List<UmsMenu> menus = roleService.listMenu(roleId);
        return CommonResult.success(menus);
    }

    @ApiOperation("获取角色关联的资源")
    @GetMapping("/listResource/{roleId}")
    @ResponseBody
    public CommonResult<List<UmsResource>> listResource(@PathVariable Long roleId) {
        List<UmsResource> resources = roleService.listResource(roleId);
        return CommonResult.success(resources);
    }

    @ApiOperation("分配菜单给角色")
    @PostMapping("/allocMenu")
    @ResponseBody
    public CommonResult allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        int assigned = roleService.allocMenu(roleId, menuIds);
        return CommonResult.success(assigned);
    }

    @ApiOperation("分配资源给角色")
    @PostMapping("/allocResource")
    @ResponseBody
    public CommonResult allocResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        int assigned = roleService.allocResource(roleId, resourceIds);
        return CommonResult.success(assigned);
    }
}
