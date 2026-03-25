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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorsMapper dishFlavorsMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishFlavorsMapper dishFlavorMapper;


    /**
     * @param dishDTO
     */

    @Transactional//添加事务
    public void newDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addNew(dish);

        Long dishId = dish.getId();//DishMapper.xml中getGeneratedKeys方法获取数据库自动生成的主键值

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));//
            dishFlavorsMapper.insertBatch(flavors);


        }

    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());//
    }

    /**
     * 删除菜品(批量)
     *
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        //1 判断当前菜品你是否可以删除
        //1.1 判断当前菜品是否正在出售
        for (long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException("当前菜品正在出售，无法删除");
            }
        }

        //1.2判断当前菜品是否被包含在某套餐中
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
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

    /**
     * 根据
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavors(Long id) {
        //1.根据id查询菜品
        Dish dish = dishMapper.getById(id);

        //2.根据id查询口味
        List<DishFlavor> dishFlavors = dishFlavorsMapper.getByDishId(id);

        //3.把菜品属性和口味属性封装到VO中
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;


    }

    @Override
    public void updateWithFlavors(DishDTO dishDTO) {
        //1.更新菜品基本数据
        //dishMapper.update(dishDTO);//传dishDTO其实也对,不过DTO包含了口味Flavors,涉及到重复传递,所以这里直接传dish基本类即可
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //2.更新菜品口味数据
        //由于直接修改口味涉及增加,删除,修改多种可能,所以这里采用先全部删除所有口味,在单独把修改后的口味增加上去
        dishFlavorsMapper.deleteByDishId(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();

        log.info("口味所对应的dishId为null:{}", flavors);
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));
            dishFlavorsMapper.insertBatch(flavors);//若不加本段代码,则dishid为空,口味无法绑定到相应的dish
            log.info("口味所对应的dishId为:{}", flavors);
        }


    }
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
}








