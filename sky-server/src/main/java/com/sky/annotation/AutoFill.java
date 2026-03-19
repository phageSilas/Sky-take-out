package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//方法级别,指定该注解只能用在方法上
@Retention(RetentionPolicy.RUNTIME)//运行时，注解保留
public @interface AutoFill {

OperationType value();//操作类型
    //完整写法@AutoFill(value = OperationType.INSERT),由于枚举类有默认值，所以可以简写为@AutoFill(OperationType.INSERT)

}
