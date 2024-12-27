package com.macro.mall.controller;

import cn.hutool.core.collection.CollUtil;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.UmsAdminLoginParam;
import com.macro.mall.dto.UmsAdminParam;
import com.macro.mall.dto.UpdateAdminPasswordParam;
import com.macro.mall.model.UmsAdmin;
import com.macro.mall.model.UmsRole;
import com.macro.mall.service.UmsAdminService;
import com.macro.mall.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台用户管理控制器
 */
@Controller
@Api(tags = "UmsAdminController")
@Tag(name = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private UmsRoleService roleService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    @ResponseBody
    public CommonResult<UmsAdmin> registerUser(@Validated @RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin registeredAdmin = adminService.register(umsAdminParam);
        return registeredAdmin == null ? CommonResult.failed() : CommonResult.success(registeredAdmin);
    }

    @ApiOperation("登录后返回token")
    @PostMapping("/login")
    @ResponseBody
    public CommonResult loginUser(@Validated @RequestBody UmsAdminLoginParam loginParam) {
        String token = adminService.login(loginParam.getUsername(), loginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("token", token);
        tokenInfo.put("tokenHead", tokenHead);
        return CommonResult.success(tokenInfo);
    }

    @ApiOperation("刷新Token")
    @GetMapping("/refreshToken")
    @ResponseBody
    public CommonResult refreshAuthToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = adminService.refreshToken(token);
        if (refreshedToken == null) {
            return CommonResult.failed("Token已过期！");
        }
        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("token", refreshedToken);
        tokenInfo.put("tokenHead", tokenHead);
        return CommonResult.success(tokenInfo);
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info")
    @ResponseBody
    public CommonResult getUserInfo(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        UmsAdmin adminInfo = adminService.getAdminByUsername(username);
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", adminInfo.getUsername());
        userData.put("menus", roleService.getMenuList(adminInfo.getId()));
        userData.put("icon", adminInfo.getIcon());
        List<UmsRole> roles = adminService.getRoleList(adminInfo.getId());
        if (CollUtil.isNotEmpty(roles)) {
            userData.put("roles", roles.stream().map(UmsRole::getName).collect(Collectors.toList()));
        }
        return CommonResult.success(userData);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    @ResponseBody
    public CommonResult logoutUser(Principal principal) {
        adminService.logout(principal.getName());
        return CommonResult.success(null);
    }

    @ApiOperation("根据关键字分页获取用户列表")
    @GetMapping("/list")
    @ResponseBody
    public CommonResult<CommonPage<UmsAdmin>> listUsers(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsAdmin> userList = adminService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(userList));
    }

    @ApiOperation("获取指定用户详细信息")
    @GetMapping("/{id}")
    @ResponseBody
    public CommonResult<UmsAdmin> getUserDetails(@PathVariable Long id) {
        return CommonResult.success(adminService.getItem(id));
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/update/{id}")
    @ResponseBody
    public CommonResult updateUser(@PathVariable Long id, @RequestBody UmsAdmin admin) {
        return adminService.update(id, admin) > 0 ? CommonResult.success(1) : CommonResult.failed();
    }

    @ApiOperation("更新用户密码")
    @PostMapping("/updatePassword")
    @ResponseBody
    public CommonResult updatePassword(@Validated @RequestBody UpdateAdminPasswordParam param) {
        int result = adminService.updatePassword(param);
        switch (result) {
            case 1:
                return CommonResult.success(result);
            case -1:
                return CommonResult.failed("参数非法");
            case -2:
                return CommonResult.failed("用户不存在");
            case -3:
                return CommonResult.failed("旧密码错误");
            default:
                return CommonResult.failed();
        }
    }

    @ApiOperation("删除用户")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public CommonResult deleteUser(@PathVariable Long id) {
        return adminService.delete(id) > 0 ? CommonResult.success(1) : CommonResult.failed();
    }

    @ApiOperation("更新账户状态")
    @PostMapping("/updateStatus/{id}")
    @ResponseBody
    public CommonResult updateAccountStatus(@PathVariable Long id, @RequestParam("status") Integer status) {
        UmsAdmin admin = new UmsAdmin();
        admin.setStatus(status);
        return adminService.update(id, admin) > 0 ? CommonResult.success(1) : CommonResult.failed();
    }

    @ApiOperation("为用户分配角色")
    @PostMapping("/role/update")
    @ResponseBody
    public CommonResult assignRoles(@RequestParam("adminId") Long adminId, @RequestParam("roleIds") List<Long> roleIds) {
        return adminService.updateRole(adminId, roleIds) >= 0 ? CommonResult.success(1) : CommonResult.failed();
    }

    @ApiOperation("获取用户角色")
    @GetMapping("/role/{adminId}")
    @ResponseBody
    public CommonResult<List<UmsRole>> getUserRoles(@PathVariable Long adminId) {
        return CommonResult.success(adminService.getRoleList(adminId));
    }
}
