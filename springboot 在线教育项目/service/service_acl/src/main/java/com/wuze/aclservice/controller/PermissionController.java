package com.wuze.aclservice.controller;


import com.wuze.aclservice.entity.Permission;
import com.wuze.aclservice.service.PermissionService;
import com.wuze.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 菜单管理
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/permission")
@CrossOrigin    //跨域
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    //获取全部菜单
    @ApiOperation(value = "查询所有菜单 wz")
    @GetMapping
    public R indexAllPermission() {
        List<Permission> list =  permissionService.queryAllMenuInfo();
        return R.ok().data("children",list);
    }

    @ApiOperation(value = "递归删除菜单 wz")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        permissionService.removeChildMenuById(id);
        return R.ok();
    }

    @ApiOperation(value = "给角色分配权限 wz")
    @PostMapping("/doAssign")
    public R doAssign(String roleId,String[] permissionId) {
        permissionService.saveRolePermissionRealtionShipByWz(roleId,permissionId);
        return R.ok();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public R toAssign(@PathVariable String roleId) {
        List<Permission> list = permissionService.selectAllMenu(roleId);
        return R.ok().data("children", list);
    }



    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public R save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return R.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public R updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }

}

