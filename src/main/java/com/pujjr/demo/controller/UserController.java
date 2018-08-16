package com.pujjr.demo.controller;

import com.pujjr.demo.dao.UserMapper;
import com.pujjr.demo.doman.MyEcxeptionHandle;
import com.pujjr.demo.doman.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 事物管理，要么同时成功，要么同时失败
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;


    @RequestMapping("/insertUser")
    @Transactional //添加事物管理
    public void insertUser(){
        User user = new User();
        user.setUsername("范中海");
        user.setPassword("000");
        user.setSex(1);
        user.setBirthday(new Date());
        user.setAddress("成都");
        userMapper.insert(user);
        int i = 1/0;//抛异常，事物回滚，插入数据库失败
    }

    @RequestMapping("/myExceptionHandle")
    public void myExceptionHandle() {
        throw  new MyEcxeptionHandle("404","抱歉找不到页面");
    }
}
