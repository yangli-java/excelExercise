package com.pujjr.demo.controller;

import com.pujjr.demo.doman.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentsController {

    @Autowired
    private Students students;

    @RequestMapping("/getStudent")
    public Object getStudent(){

        return  students;
    }

}
