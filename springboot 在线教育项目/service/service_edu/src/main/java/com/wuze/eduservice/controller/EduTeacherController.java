package com.wuze.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuze.commonutils.R;
import com.wuze.eduservice.entity.EduTeacher;
import com.wuze.eduservice.entity.vo.TeacherQuery;
import com.wuze.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-01-03
 */
@Api("讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
@CrossOrigin //跨域
public class EduTeacherController {

    //把service注入
    @Autowired
    private EduTeacherService eduTeacherService;

    //rest 风格（查询用 “ @GetMapping ”）
    //注意：这里 /findall 或者 findall 都可以（加不加 / 都行）
    @GetMapping("/findall")
    @ApiOperation(value = "所有讲师列表")
    public R list(){
        //return eduTeacherService.list(null); //原来的，返回的是 list
        List<EduTeacher> list = eduTeacherService.list(null);

        /*2021.01.05 测试自定义异常
        * */
      /*  try{
            int i=1/0;
        }catch (Exception e){
            throw new WzException(20001,"执行了自定义异常处理~");
        }*/

        return R.ok().data("items",list);
    }


    //根据id删除讲师
    @DeleteMapping("{id}") //表示id值需要通过路径传递
    @ApiOperation(value = "逻辑删除讲师")
    public R removeById(@PathVariable String id){//表示获取路径中的id值 （注意：因为数据库中id的字段是char，所以这里String）
        //return eduTeacherService.removeById(id);//将获取的id传进去方法参数（调用mp的删除方法）

        //现在：（2021.01.04）(设置统一返回结果 “R”)
        boolean flag = eduTeacherService.removeById(id);
        if(flag){
            return R.ok();
        }
        else{
            return R.error();
        }

    }

/*分页查询问题记录：
刚开始在swagger测试分页一直报错：Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested excepti
最后才知道是因为
*@ApiParam 标注参数 的缘故，因为在这里面没有指定 dataType="Long" 和 ParamType="path"，但是指定了又爆红，所以果断删去。
* */


    //分页查询
    //currentpage：当前页码
    //limit：每页记录数
    @ApiOperation(value = "分页查询")
    @GetMapping("pageTeacher/{currentpage}/{limit}")
    public R pageList( @PathVariable Long currentpage,
                      @PathVariable Long limit){

        Page<EduTeacher> pageTeacher=new Page<>(currentpage,limit);

        //调用方法实现分页
        //调用方法的时候，底层封装，把分页所有数据封装pageTeacher对象里面
        eduTeacherService.page(pageTeacher,null);

        List<EduTeacher> records=pageTeacher.getRecords();//返回所有记录
        long total = pageTeacher.getTotal();//返回总记录条数


    //也可以写成
   /* Map map = new HashMap<>();
    map.put("total",total);
    map.put("rows",records);
    return R.ok().data(map);*/

        return R.ok().data("total",total).data("rows",records);

    }





    //多条件组合查询 带分页
    //currentpage：当前页码
    //limit：每页记录数
    //teacherQuery 查询条件（模糊查询）
    //注：改成@RequestBody 需要使用 Post 提交（RequestBody表示使用json传递数据，我们需要把json数据封装到对应对象里面）
    @ApiOperation(value = "多条件组合查询（带分页）")
    @PostMapping("pageTeacherCondition/{currentpage}/{limit}")
    public R pageTeacherCondition( @PathVariable Long currentpage,
                       @PathVariable Long limit,
                   @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<EduTeacher> pageTeacher=new Page<>(currentpage,limit);


        //在分页查询基础上增加teacherQuery参数给wrapper作为查询条件
        //在自定义方法pageQuery()中配置wrapper，间接调用 ".page()"
        eduTeacherService.pageQuery(pageTeacher,teacherQuery);


        List<EduTeacher> records=pageTeacher.getRecords();//返回所有记录
        long total = pageTeacher.getTotal();//返回总记录条数

        return R.ok().data("total",total).data("rows",records);

    }






    //新增讲师
    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R save(@RequestBody EduTeacher teacher){
        boolean save = eduTeacherService.save(teacher);//boolean类型
        return save?R.ok():R.error();//添加成功，返回ok，失败返回error（三目运算符）
    }


    //根据id查询讲师（注意，id在数据表和实体类都是String类型）
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("get/{id}")
    public R GetbyId(@PathVariable String id){
        EduTeacher teacher = eduTeacherService.getById(id);//根据id查询讲师信息
        return R.ok().data("item",teacher).message("查询成功");
    }



    /*
    注意：（修改讲师信息功能）
    方法一：put提交（与@RequestBody一起，需要再次设置id值）
    方法二：post提交（与@RequestBody一起，不需要再设置id值）（但是注意前端在讲师信息的json数据中也是要包含 id 信息的）
    （下面演示的是方法一：put提交）
    * */
    //还要传入需要修改的讲师的信息（json格式传递）！！！所以参数还有teacher对象(@RequestBody 注解)
    @ApiOperation(value = "根据id修改讲师信息")
    @PutMapping("update/{id}")
    public R updateById(@PathVariable String id,@RequestBody  EduTeacher teacher){

        //绑定 "前端页面传入的想要修改的信息值" 与 "传入的讲师id"（put提交才需要绑定（/设置），因为@RequestBody需要和 Post提交搭配才能用）
        teacher.setId(id);//如果使用post提交就不需要这条语句

        eduTeacherService.updateById(teacher);//执行修改（注意传入的是teacher对象）
        return R.ok();
    }


    //讲师修改2 ( post 提交)
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody  EduTeacher teacher){
        eduTeacherService.updateById(teacher);//执行修改（注意传入的是teacher对象）
        return R.ok();
    }

}

