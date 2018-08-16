package com.pujjr.demo.controller;

import com.pujjr.demo.doman.JsonData;
import com.pujjr.demo.doman.MyEcxeptionHandle;
import com.pujjr.demo.doman.Students;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 关于全局异常的demo
 * 在类上需要用到的注解RestController，ControllerAdvice；或者使用RestControllerAdvice,均表示返回Json格式
 * 在方法上需要用到的注解
 */
//@RestControllerAdvice
@RestController //需要返回Json格式则用RestController
@ControllerAdvice  //会去找映射的html网页
public class GlobalExceptionsController {

    //这是正常的情况
    @RequestMapping("/normalHandle")
    public Object normalHandle(){
        Students student = new Students();
        student.setName("哼哈中的哼");
        student.setAge(50);
        return student;
    }

    //一旦项目中出现异常就会进入到这个全局异常方法中，因为Exception是所有异常的父接口
    //一般全局异常时处理一些未能想到的异常。
    @ExceptionHandler(value = Exception.class)  //此处的value可以是自定义的异常类
    public Object exceptionHandle(Exception e, HttpServletRequest request){//还可以通过request获取到更多的请求信息
        JsonData jsonData = new JsonData("1", "失败原因: " + e.getMessage() + ";失败路径: " + request.getRequestURI());
        return jsonData;
    }

    //自定义异常处理
    @ExceptionHandler(value = MyEcxeptionHandle.class)
    public Object myExceptionHandle(MyEcxeptionHandle e){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error.html");
        modelAndView.addObject("ResultCode",e.getCode());
        modelAndView.addObject("Message",e.getResponseMsg());
        return modelAndView;
    }


}
