package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealmapper;

    @Autowired
    private SetmealDishMapper  setmealdishMapper;


    /*
     * 新增套餐
     * @param setmealdto
     * */
    @Override
    public void insert(SetmealDTO setmealdto) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealdto, setmeal);

        //插入套餐
        setmealmapper.insert(setmeal);

        //获取套餐主键值
        Long setmealId = setmeal.getId();

        //查询套餐中的菜品
        List<SetmealDish> dishs = setmealdto.getSetmealDishes();
        if (dishs != null && dishs.size() > 0) {
            dishs.forEach(dish -> {
                dish.setSetmealId(setmealId);
            });

            //插入套餐菜品
            setmealdishMapper.insertBatch(dishs);
        }


    }

    /*
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * */
    @Override
    public PageResult PageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealmapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }
    /*
     * 套餐删除
     * @param
     * @return
     * */
    @Override
    public void deleteBatch(List<Long> ids) {

//      查询套餐是否为售卖状态
        for (Long id : ids) {
            Setmeal setmeal = setmealmapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }


//        优化
        setmealmapper.deleteByIds(ids);

        setmealdishMapper.deleteByIds(ids);

//        dishFlavorMapper.deleteByDishIds(ids);

    }

    @Override
    public SetmealVO getByIdWithDishes(Long id) {
        //根据id查询菜品数据
        Setmeal setmeal = setmealmapper.getById(id);

        //根据菜品id查询菜品数据
        List<SetmealDish> setmealDishes = setmealdishMapper.getByMealId(id);

        //将查询到的数据封装到VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public void updateWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //修改套餐表基本信息
        setmealmapper.update(setmeal);
        //删除原有的菜品数据
        setmealdishMapper.delectByMealId(setmeal.getId());
        //重新插入菜品数据
        List<SetmealDish> setmealdishes = setmealDTO.getSetmealDishes();
        if (setmealdishes != null && setmealdishes.size() > 0) {
            setmealdishes.forEach(dish -> {
                dish.setSetmealId(setmeal.getId());
            });
            setmealdishMapper.insertBatch(setmealdishes);
        }
    }

    @Override
    public void saleOrNo(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder().status(status).id(id).build();
        setmealmapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealmapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealmapper.getDishItemBySetmealId(id);
    }
}
