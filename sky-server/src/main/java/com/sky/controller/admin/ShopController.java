package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")//创建一个Controller，命名为adminController,防止和user冲突
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 修改店铺营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    public Result get(@PathVariable Integer status){
        log.info("获取店铺营业状态 {}", status==1?"营业中":"打烊中");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }


    @GetMapping("/status")
    public Result<Integer> get(){
        log.info("获取店铺营业状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);//返回的是Object类型,所以需要强转
        log.info("获取店铺营业状态 {}", status==1?"营业中":"打烊中");
        return Result.success(status);
    }

}
