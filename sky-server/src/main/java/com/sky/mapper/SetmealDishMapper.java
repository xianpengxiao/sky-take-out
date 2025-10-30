package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    //根据菜品id查询对应套餐id
    List<Long> getSetmealIdsByDishIDS(List<Long> dishIds);

    /*
     * 批量插入套餐菜品
     * @param flavors
     * */
    void insertBatch(List<SetmealDish> dishs);
    /*
     * 根据菜品id集合批量删除套餐菜品
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    @Select("select * from setmeal_dish where setmeal_id = #{mealId}")
    List<SetmealDish> getByMealId(Long mealId);

    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void delectByMealId(Long setmealId);
}
