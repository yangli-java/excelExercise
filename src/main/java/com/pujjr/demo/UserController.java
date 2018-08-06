package com.pujjr.demo;

import com.pujjr.demo.dao.UserMapper;
import com.pujjr.demo.doman.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
        int i = 1/0;
    }


}
