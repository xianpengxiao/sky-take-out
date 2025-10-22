package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {
    /*
    * 新增菜品和对应口味数据
    * @param dishDTO
    * */
    public void saveWithDFlavor(DishDTO dishDTO);
    /*
     * 菜品分页查询
     * @param dishPageQueryDTO
     * */
    PageResult PageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * 菜品批量删除
     * @param ids
     * */
    void deleteBatch(List<Long> ids);
}
