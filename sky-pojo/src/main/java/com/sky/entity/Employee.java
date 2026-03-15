package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//告诉MyBatis，这个字段需要按照这个格式进行转换,若不加该注释，则MyBatis会默认使用这个字段的默认构造方法,即一个数组
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;


    /**
     * 若不使用@Builder注解，则需要使用如下代码
     */
    /*private Employee(Builder builder) {
        this.username = builder.username;
        this.name = builder.name;
        this.password = builder.password;
        this.phone = builder.phone;
        this.sex = builder.sex;
        this.idNumber = builder.idNumber;
        this.status = builder.status;
        this.createTime = builder.createTime;
        this.updateTime = builder.updateTime;
        this.createUser = builder.createUser;
        this.updateUser = builder.updateUser;
        this.id = builder.id;


    }

    // 3. 提供一个静态的 builder() 方法，作为外界点餐的“入口”
    public static Builder builder() {
        return new Builder();
    }

    // ==========================================
    // 4. 重点：在内部定义一个静态的 Builder 助手类
    // ==========================================
    public static class Builder {


        // Builder 类里面要有一模一样的属性，用来做“暂存”
        private String username;
        private String name;
        private String password;
        private String phone;
        private String sex;
        private String idNumber;
        private Integer status;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private Long createUser;
        private Long updateUser;
        private Long id;

        // 【赋值方法】对应前面的 .username(...)
        public Builder username(String username) {
            this.username = username;
            return this; // ⬅️ 魔法的核心：把当前的 Builder 对象自己返回出去
        }

        // 【赋值方法】对应前面的 .name(...)
        public Builder name(String name) {
            this.name = name;
            return this; // ⬅️ 再次返回自己，实现链式调用
        }

        public Builder password(String password) {
            this.password = password;
            return this; // ⬅️ 链式调用
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this; // ⬅️ 链式调用
        }

        public Builder sex(String sex) {
            this.sex = sex;
            return this; // ⬅️ 链式调用
        }

        public Builder idNumber(String idNumber) {
            this.idNumber = idNumber;
            return this; // ⬅️ 链式调用
        }

        public Builder status(Integer status) {
            this.status = status;
            return this; // ⬅️ 链式调用
        }

        public Builder createTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this; // ⬅️ 链式调用
        }

        public Builder updateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this; // ⬅️ 链式调用
        }

        public Builder createUser(Long createUser) {
            this.createUser = createUser;
            return this; // ⬅️ 链式调用
        }

        public Builder updateUser(Long updateUser) {
            this.updateUser = updateUser;
            return this; // ⬅️ 链式调用
        }

        public Builder id(Long id) {
            this.id = id;
            return this; // ⬅️ 链式调用
        }

        // 【收尾方法】对应前面的 .build()
        public Employee build() {
            // 把自己（this，也就是填满属性的 Builder）传给 Employee 的私有构造方法
            return new Employee(this);
        }
    }*/
}
