package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /*
     * 新增套餐
     * @param setmealdto
     * */
    void insert(SetmealDTO setmealdto);

    /*
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * */
    PageResult PageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /*
     * 套餐删除
     * @param
     * @return
     * */
    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDishes(Long id);

    void updateWithDishes(SetmealDTO setmealDTO);

    void saleOrNo(Integer status, Long id);
}
