package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    @Insert("insert into employee (username, password, name, sex, id_number, phone, create_time, update_time, create_user, update_user, status) " +
            "values (#{username}, #{password}, #{name}, #{sex}, #{idNumber}, #{phone}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
    @AutoFill(value = OperationType.INSERT)//插入操作
    void addNew(Employee employee);


    /**
     * 分页查询,涉及到动态sql,使用xml文件
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> page(EmployeePageQueryDTO employeePageQueryDTO);


    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    //所以修改的操作都可以又该接口来完成
    @AutoFill(value = OperationType.UPDATE)//更新操作
    void update(Employee employee);


}
