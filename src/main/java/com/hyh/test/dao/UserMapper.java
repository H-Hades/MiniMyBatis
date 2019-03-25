package com.hyh.test.dao;

import com.hyh.annotation.MyInsert;
import com.hyh.annotation.MySelect;
import com.hyh.test.entity.User;

import java.util.ArrayList;

public interface UserMapper  {
    @MySelect("select * from user where id=#{id}")
    User selectById(Integer id);
    @MySelect("select * from user")
    ArrayList<User> select();
    @MyInsert("insert into user value(null,#{name},#{age},#{score})")
    int insert(String name,Integer age,Double score);
}
