package com.pujjr.demo.controller;

import com.pujjr.demo.dao.UserMapper;
import com.pujjr.demo.doman.ExcelView;
import com.pujjr.demo.doman.JsonData;
import com.pujjr.demo.doman.User;
import com.pujjr.demo.doman.UserInfoExcelView;
import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ExportExcels {

    //文件存放路径
    private static final String ExcelsPath = "E:/GitProjects/excelExercise/src/main/resources/static/Excels/";

    @Autowired
    private UserMapper userMapper;

    /**
     * 此种方法poi只能在3.14版本一下的才得行
     * 参考https://blog.csdn.net/wang124454731/article/details/73850645
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toExport")
    public ModelAndView toExport(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        System.out.println("进入toExport方法了！");
//        Workbook workbook = new HSSFWorkbook();
        List<User> userList = userMapper.selectAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("members", userList);
        map.put("name", "用户信息表");
        ExcelView excelView = new UserInfoExcelView();
//        excelView.buildExcelDocument(map,workbook,request,response);
        ModelAndView modelAndView = new ModelAndView();
        return new ModelAndView(excelView, map);
    }

    /**
     * 正确的文件导出
     * 参考https://blog.csdn.net/panpan96/article/details/76566475
     * @param request
     * @param response
     * @throws IOException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/download")
    public void downstudents(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException {  //我这是根据前端传来的起始时间来查询数据库里的数据，如果没有输入变量要求，保留前两个就行}
        String[] headers = {"编码","用户名","密码","地址","生日","性别"};
        List<User> userList = userMapper.selectAll();
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 18);
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //遍历集合数据，产生数据行
        Iterator it = userList.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            //创建第i+1行
            row = sheet.createRow(index);
            User user = (User) it.next();
            //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = user.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    Class tCls = user.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[]{});
                    Object value = getMethod.invoke(user, new Object[]{});
                    String textValue = null;

                    //如果是属于日期格式则进行格式化，否则按字符串处理
                    if (value instanceof Date)
                    {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        textValue = sdf.format(date);
                    }
                    else
                    {
                        //其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }

                    HSSFRichTextString richString = new HSSFRichTextString(textValue);
                    HSSFFont font3 = workbook.createFont();
                    font3.setColor(HSSFColor.BLUE.index);//定义Excel数据颜色
                    richString.applyFont(font3);
                    cell.setCellValue(richString);

                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=createList.xls");//默认Excel名称
        response.flushBuffer();
        workbook.write(response.getOutputStream());


    }
}
