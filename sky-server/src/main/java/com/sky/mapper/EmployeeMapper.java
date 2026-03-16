package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
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
    void addNew(Employee employee);


    /**
     * 分页查询,涉及到动态sql,使用xml文件
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> page(EmployeePageQueryDTO employeePageQueryDTO);



    //所以修改的操作都可以又该接口来完成
    void update(Employee employee);
}
