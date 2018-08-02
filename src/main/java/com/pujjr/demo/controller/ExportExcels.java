package com.pujjr.demo.controller;

import com.pujjr.demo.dao.UserMapper;
import com.pujjr.demo.doman.ExcelView;
import com.pujjr.demo.doman.JsonData;
import com.pujjr.demo.doman.User;
import com.pujjr.demo.doman.UserInfoExcelView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExportExcels {

    //文件存放路径
    private static final String ExcelsPath = "E:/GitProjects/excelExercise/src/main/resources/static/Excels/";

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/toExport")
    public Object toExport(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        System.out.println("进入toExport方法了！");
        Workbook workbook = new HSSFWorkbook();
        List<User> userList = userMapper.selectAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("members", userList);
        map.put("name", "用户信息表");
        ExcelView excelView = new UserInfoExcelView();
        excelView.buildExcelDocument(map,workbook,request,response);
        return new ModelAndView(excelView, map);
    }
}
