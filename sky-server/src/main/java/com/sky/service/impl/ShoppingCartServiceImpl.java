package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SetmealServiceImpl setmealService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前购物车中是否有菜品/套餐
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);//DTO中是菜品id、套餐id、用户id，下方通过判断菜品/套餐id是否存在,来判断购物车中是否存在该菜品/套餐
        shoppingCart.setUserId(BaseContext.getCurrentId());//为当前购物车绑定用户id
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList != null && shoppingCartList.size() > 0) {
            //存在，数量加1
            ShoppingCart cart = shoppingCartList.get(0);//
            cart.setNumber(cart.getNumber() + 1);//数量加1
            shoppingCartMapper.updateNumberById(cart);//更新购物车数量，数量加1
        } else {
            //不存在，添加到购物车，数量默认为1

            //判断本次添加的是菜品还是套餐
            if (shoppingCartDTO.getDishId() != null) {
                //添加菜品
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());




            } else if (shoppingCartDTO.getSetmealId() != null) {
                //添加套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);


                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());



            }
            //无论是添加菜品还是套餐,数量都为1，创建时间为当前时间
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.insert(shoppingCart);
        }




    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.setUserId(BaseContext.getCurrentId());

        return shoppingCartMapper.list(shoppingCart);

    }

    /**
     * 删除购物车
     * @param shoppingCartDTO
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.setUserId(BaseContext.getCurrentId());
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        shoppingCartMapper.deleteByUserId(shoppingCart);



    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();



        shoppingCartMapper.deleteByUserId( ShoppingCart.builder()
                .userId(userId)
                .build());
    }
}
