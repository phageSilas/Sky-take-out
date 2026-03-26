package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    /**
     *修改购物车菜品/套餐数量
     * @param cart
     */

    void updateNumberById(ShoppingCart cart);

    /**
     * 添加购物车
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 删除购物车中某个商品/或清空
     * @param shoppingCart
     */
    void deleteByUserId(ShoppingCart shoppingCart);


}
