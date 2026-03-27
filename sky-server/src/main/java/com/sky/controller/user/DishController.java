package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController{
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;





    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        String key = "dish_" + categoryId;

        //从redis中查询缓存
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);//获取缓存数据
        if (list != null) {
            return Result.success(list);//缓存中有数据，直接返回
        }




        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        list = dishService.listWithFlavor(dish);//查询数据库
        redisTemplate.opsForValue().set(key, list);//缓存中没有数据，将数据放入缓存

        return Result.success(list);
    }


}
