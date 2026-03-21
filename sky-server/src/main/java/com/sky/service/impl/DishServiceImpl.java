package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorsMapper dishFlavorsMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     *
     * @param dishDTO
     */

    @Transactional//添加事务
    public void newDish(DishDTO dishDTO) {
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO,dish);
    dishMapper.addNew(dish);

    Long dishId= dish.getId();//DishMapper.xml中getGeneratedKeys方法获取数据库自动生成的主键值

    List<DishFlavor> flavors = dishDTO.getFlavors();
    if(flavors!=null && flavors.size()>0){
        flavors.forEach(flavor -> flavor.setDishId(dishId));//
        dishFlavorsMapper.insertBatch(flavors);



    }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());//
    }

    /**
     * 删除菜品(批量)
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        //1 判断当前菜品你是否可以删除
         //1.1 判断当前菜品是否正在出售
        for(long id:ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException("当前菜品正在出售，无法删除");
            }
        }

         //1.2判断当前菜品是否被包含在某套餐中
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null && setmealIds.size()>0){
            throw new DeletionNotAllowedException("当前菜品被套餐包含，无法删除");
        }

        /*//2 删除菜品dish数据
        for (long id:ids) {
            dishMapper.deleteByIds(id);

            //3 删除菜品相关的口味数据flavors
              dishFlavorsMapper.deleteByDishId(id);*/

        //2 根据dishId集合批量删除菜品
        dishMapper.deleteBatchByIds(ids);
        //3 根据dishId集合批量删除口味
        dishFlavorsMapper.deleteBatchByDishIds(ids);


        }



        }








