package com.pujjr.demo.controller;

import com.pujjr.demo.doman.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ExportExcels {

    @RequestMapping(value = "/toExport")
    public Object toExport(){
        System.out.println("进入toExport方法了！");
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();

            list.add(user);
        }
        return null;
    }
}
