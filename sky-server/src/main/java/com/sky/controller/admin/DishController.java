package com.sky.controller.admin;

/*
* 菜品管理
* */

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api("菜品管理相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    /*
    * 新增菜品
    * @param
    * @return
    * */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithDFlavor(dishDTO);

        //清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }
    /*
     * 菜品分页查询
     * @param
     * @return
     * */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.PageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    /*
     * 菜品删除
     * @param
     * @return
     * */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids) {

        log.info("菜品批量删除：{}",ids);
        dishService.deleteBatch(ids);

        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
     public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}",id);
        DishVO dishVo = dishService.getByIdWithFlavor(id);
        return Result.success(dishVo);
     }

     @PutMapping
     @ApiOperation("修改菜品")
     public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);

        cleanCache("dish_*");
        return Result.success();
     }

     @PostMapping("/status/{status}")
     @ApiOperation("菜品启停售")
     public Result saleOrNo(@PathVariable("status") Integer status,Long id){
        log.info("菜品状态：{}{}",id,status);
        dishService.saleOrNo(status,id);

         cleanCache("dish_*");
         return Result.success();
     }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }


    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
