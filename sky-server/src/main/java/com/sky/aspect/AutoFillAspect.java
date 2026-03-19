package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) &&  @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段数据填充");
        //1.获取当前被拦截的方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取方法签名(包含方法名,参数,返回类型)//注意导入的是AspectJ的包
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取带有AutoFill注解的方法
        OperationType value = autoFill.value();//获取注解的value值

        //2.获取当前被拦截的方法参数--实体对象
        Object[] args = joinPoint.getArgs();//获取方法参数
        if (args == null || args.length == 0) {//参数不存在，直接返回
            return;
        }
        Object object = args[0];//获取当前方法参数对象

        LocalDateTime now = LocalDateTime.now();
        long currentId = BaseContext.getCurrentId();


        //3.根据对应的操作类型，为对应的字段赋值
        if (value == OperationType.INSERT) {
            try {
                object.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(object, now);
                object.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(object, currentId);
                object.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(object, now);
                object.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(object, currentId);
            } catch (Exception e) {
                log.error("反射调用方法失败", e);

            }

        } else if (value == OperationType.UPDATE) {
            try {
                object.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(object, now);
                object.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(object, currentId);
            } catch (Exception e) {
                log.error("反射调用方法失败", e);
            }


        }


    }
}
