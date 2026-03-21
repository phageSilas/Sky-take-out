package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
/**
 * 新增菜品
 * @param dishDTO
 */
@PostMapping
public Result newDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品");
        dishService.newDish(dishDTO);
    return Result.success();
 }

/**
 * 菜品分页查询
 * @param dishPageQueryDTO
 * @return
 */
@GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
    log.info("菜品分页查询");
    PageResult pageResult = dishService.page(dishPageQueryDTO);
    return Result.success(pageResult);
}
}
