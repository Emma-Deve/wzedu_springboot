package com.wuze.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.aclservice.entity.Permission;
import com.wuze.aclservice.entity.RolePermission;
import com.wuze.aclservice.entity.User;
import com.wuze.aclservice.helper.MemuHelper;
import com.wuze.aclservice.helper.PermissionHelper;
import com.wuze.aclservice.mapper.PermissionMapper;
import com.wuze.aclservice.service.PermissionService;
import com.wuze.aclservice.service.RolePermissionService;
import com.wuze.aclservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UserService userService;

    
    //获取全部菜单
    @Override
    public List<Permission> queryAllMenu() {

        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        List<Permission> result = bulid(permissionList);

        return result;
    }

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(String roleId) {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        //转换给角色id与角色权限对应Map对象
//        List<String> permissionIdList = rolePermissionList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
//        allPermissionList.forEach(permission -> {
//            if(permissionIdList.contains(permission.getId())) {
//                permission.setSelect(true);
//            } else {
//                permission.setSelect(false);
//            }
//        });
        for (int i = 0; i < allPermissionList.size(); i++) {
            Permission permission = allPermissionList.get(i);
            for (int m = 0; m < rolePermissionList.size(); m++) {
                RolePermission rolePermission = rolePermissionList.get(m);
                if(rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }


        List<Permission> permissionList = bulid(allPermissionList);
        return permissionList;
    }

    //给角色分配权限
    @Override
    public void saveRolePermissionRealtionShip(String roleId, String[] permissionIds) {

        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));

  

        List<RolePermission> rolePermissionList = new ArrayList<>();
        for(String permissionId : permissionIds) {
            if(StringUtils.isEmpty(permissionId)) continue;
      
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
    }

    //递归删除菜单
    @Override
    public void removeChildById(String id) {
        List<String> idList = new ArrayList<>();
        this.selectChildListById(id, idList);

        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    /**
     *	递归获取子节点
     * @param id
     * @param idList
     */
    private void selectChildListById(String id, List<String> idList) {
        List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
        childList.stream().forEach(item -> {
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    private static List<Permission> bulid(List<Permission> treeNodes) {
        List<Permission> trees = new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if ("0".equals(treeNode.getPid())) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    private static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())) {
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }


    //========================递归查询所有菜单================================================
    //获取全部菜单
/*    @Override
    public List<Permission> queryAllMenuInfo() {
        //1 查询菜单表所有数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        //2 把查询所有菜单list集合按照要求进行封装
        List<Permission> resultList = bulidPermission(permissionList);
        return resultList;
    }*/

    //把返回所有菜单list集合进行封装的方法
    /*public static List<Permission> bulidPermission(List<Permission> permissionList) {

        //创建list集合，用于数据最终封装
        List<Permission> finalNode = new ArrayList<>();
        //把所有菜单list集合遍历，得到顶层菜单 pid=0菜单，设置level是1
        for(Permission permissionNode : permissionList) {
            //得到顶层菜单 pid=0菜单
            if("0".equals(permissionNode.getPid())) {
                //设置顶层菜单的level是1
                permissionNode.setLevel(1);
                //根据顶层菜单，向里面进行查询子菜单，封装到finalNode里面
                finalNode.add(selectChildren(permissionNode,permissionList));
            }
        }
        return finalNode;
    }

    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
        //1 因为向一层菜单里面放二层菜单，二层里面还要放三层，把对象初始化
        permissionNode.setChildren(new ArrayList<Permission>());

        //2 遍历所有菜单list集合，进行判断比较，比较id和pid值是否相同
        for(Permission it : permissionList) {
            //判断 id和pid值是否相同
            if(permissionNode.getId().equals(it.getPid())) {
                //把父菜单的level值+1
                int level = permissionNode.getLevel()+1;
                it.setLevel(level);
                //如果children为空，进行初始化操作
                if(permissionNode.getChildren() == null) {
                    permissionNode.setChildren(new ArrayList<Permission>());
                }
                //把查询出来的子菜单放到父菜单里面
                permissionNode.getChildren().add(selectChildren(it,permissionList));
            }
        }
        return permissionNode;
    }*/




    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////    递归获取全部菜单 wz     ///////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    //递归获取全部菜单wz
    @Override
    public List<Permission> queryAllMenuInfo(){
        //1、先查询出所有菜单（顺便按id对菜单进行排序）
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        List<Permission> finalNodeList = new ArrayList<>();

        //2、查询出 type 为 0 的菜单（一级菜单，也就是递归入口）
        for(Permission pmenu : permissionList){
            //pmenu = new Permission();
            //pid为"0" 一级菜单
            if(pmenu.getPid().equals("0")){
                pmenu.setLevel(1);//设置层级为1

                //3、递归查询下面的每一层菜单
                //queryPerMenuInfo方法 返回 Permission对象
                Permission perMenuNode = queryPerMenuInfo(pmenu,permissionList);
                //关键：finalNodeList 保存每个一级菜单 组成的list（注：其中的每个一级菜单里面又嵌套多级菜单）
                finalNodeList.add(perMenuNode);
            }
        }
        return finalNodeList;
    }



    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////

    //传入一级菜单对象，和整个菜单列表，递归查询所有子菜单
    //返回 Permission对象
    public Permission queryPerMenuInfo(Permission pmenu, List<Permission> permissionList) {
        List NextMenuList = new ArrayList();
        pmenu.setChildren(new ArrayList<>());//初始化（分配空间，避免报错）

        for (Permission pmenuchild : permissionList){
            //注：这里的比较不要用 ==，要用 equals，因为id和pid都是String类型
            if(pmenuchild.getPid().equals(pmenu.getId())){
                pmenuchild.setLevel(pmenu.getLevel()+1);//设置为下一个层级（level+1）

                //保证 children 这个List对象不为空
                if(pmenu.getChildren() == null){
                    //如果为空，创建空间
                    pmenu.setChildren(new ArrayList<>());//初始化
                }



                //这里set子菜单有两种方法，
                // 方法一：一种是把 递归查出所有子菜单（2级 ～ n级）全部一次性放在一个List中，然后把这个List给set到children这个list中，即 pmenu.setChildren(NextMenuList)
                // 方法二：一种是把 每一次递归查询出来的 Permission 对象都放在 children 这个list中，即 pmenu.getChildren().add(Permission对象)


                /////////////////////// 方法一： /////////////////////////////////////////////////////
                //递归查询每一个层级（pmenuchild：菜单的子菜单。。。（一直递归，知道List结束））
                NextMenuList.add(queryPerMenuInfo(pmenuchild,permissionList));
                //将这一级（pmenu） 的子菜单全部set到 “List<Permission> children” 对象 里面
                // （所以要保证 children 这个List对象不为空）
                pmenu.setChildren(NextMenuList);


                /////////////////////// 方法二： /////////////////////////////////////////////////////
                //pmenu.getChildren().add(queryPerMenuInfo(pmenuchild,permissionList))

            }
        }
        return pmenu;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////





    //////////////////////////////////////////////////

    //============递归删除菜单==================================
    /*@Override
    public void removeChildByIdGuli(String id) {
        //1 创建list集合，用于封装所有删除菜单id值
        List<String> idList = new ArrayList<>();
        //2 向idList集合设置删除菜单id
        this.selectPermissionChildById(id,idList);
        //把当前id封装到list里面
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //2 根据当前菜单id，查询菜单里面子菜单id，封装到list集合
    private void selectPermissionChildById(String id, List<String> idList) {
        //查询菜单里面子菜单id
        QueryWrapper<Permission>  wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<Permission> childIdList = baseMapper.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getId());
            //递归查询
            this.selectPermissionChildById(item.getId(),idList);
        });
    }
*/










    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////    递归删除菜单 wz     ///////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    //递归删除菜单 (wz)
    @Override
    public void removeChildMenuById(String id) {
        //1、先查询这个id菜单对象
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        Permission permission = baseMapper.selectOne(wrapper);
        //System.out.println(permission);

        //2、递归该菜单下的所有子菜单
        //将所有 子菜单id 都加到 idList 集合中
        List<String> idList = new ArrayList<>();
        deleteChildMenu(permission,idList);


        //3、最后将自己id 也加到 idList 中
        idList.add(id);

        //4、一起删除（根据id集合删除对象）
        baseMapper.deleteBatchIds(idList);

    }


    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////

    //递归删除
    public void deleteChildMenu(Permission permission ,List<String> idList) {

        //查询子菜单，封装到 childrenList 中
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();

        //坑：（之前一直栈溢出。。。）
        //传入的菜单id要匹配子菜单的pid，所以wrapper 要传入 pid
        //误区：不能查wrapper.eq("id", permission.getId()); 不能是 id，如果是id的话就总是自己查自己，查不出子菜单的！！！
        wrapper.eq("pid", permission.getId());//注：每次进来这个递归函数，都根据 pid 查出某一个级别的所有菜单！
        wrapper.select("id");
        List<Permission> childrenList = baseMapper.selectList(wrapper);


        //遍历子菜单，取出 id，加到 idList 中
        //注：childrenList在每次遍历都是变化的，因为对应不同的子菜单！
        for (Permission pmenuchild : childrenList) {
            idList.add(pmenuchild.getId());
            //核心：递归，让所有子菜单都去wrapper查它的子子菜单，然后取出id 放到 idList 中
            deleteChildMenu(pmenuchild,idList);
        }



    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
















    //=========================给角色分配菜单=======================
    @Override
    public void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds) {
        //roleId角色id
        //permissionId菜单id 数组形式
        //1 创建list集合，用于封装添加数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        //遍历所有菜单数组
        for(String perId : permissionIds) {
            //RolePermission对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(perId);
            //封装到list集合
            rolePermissionList.add(rolePermission);
        }
        //添加到角色菜单关系表
        rolePermissionService.saveBatch(rolePermissionList);
    }






    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////    递归删除菜单 wz     ///////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    //给角色分配权限
    @Override
    public void saveRolePermissionRealtionShipByWz(String roleId, String[] permissionId) {
        List<RolePermission> rpList = new ArrayList<>();
        for (String pid : permissionId){
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(pid);
            //方法一：每次循环都要操作数据库（不建议）
            //rolePermissionService.save(rp);

            //方法二：把循环生成的所有对象，都先添加到list 中，待会在循环外统一添加到数据库（更好）
            rpList.add(rp);
        }
        rolePermissionService.saveBatch(rpList);

    }





}
