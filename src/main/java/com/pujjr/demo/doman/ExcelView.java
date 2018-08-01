package com.pujjr.demo.doman;

import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        String excelName = map.get("name").toString() + ".xls";
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(excelName,"utf-8"));
        response.setContentType("application/ms-excel; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        @SuppressWarnings("unchecked")
        List<User> list = (List<User>) map.get("members");
        Sheet sheet = workbook.createSheet("用户信息表");
        //设置每一列可装的字符长度
        sheet.setDefaultColumnWidth(10);
        //获取列的风格和字体对象，并设置字体样式
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        //设置单元格前景颜色
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        //setFillPattern是设置单元格填充样式，SOLID_FOREGROUND纯色使用前景颜色填充
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //可以为宋体，微软雅黑等
        font.setFontName("Arial");
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        //设置表头
        Row header = sheet.createRow(0);
//        String[] UserSheet = {"编码","用户名","密码","地址","生日","性别"};
        List<String> titles = getTitlesByClass(User.class);
        for (int i = 0; i < titles.size(); i++) {
            header.createCell(i).setCellValue(titles.get(i));
        }
        //给每一个单元格赋值
        for (int i = 0; i < list.size() ; i++) {
            int rowCount = i+1;
            Row userRow = sheet.createRow(rowCount);
            userRow.createCell(0).setCellValue(list.get(i).getId());
            userRow.createCell(1).setCellValue(list.get(i).getUsername());
            userRow.createCell(2).setCellValue(list.get(i).getPassword());
            userRow.createCell(3).setCellValue(list.get(i).getAddress());
            userRow.createCell(4).setCellValue(list.get(i).getBirthday());
            userRow.createCell(5).setCellValue(list.get(i).getSex());
        }

    }

    /**
     * 通过class获取对应的String中文表头数组
     *
     * @return
     */
    public static List<String> getTitlesByClass(Class clz) {
        List<String> list = new ArrayList<>();
        if (clz != null) {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ApiModelProperty.class)) {
                    String value = field.getAnnotation(ApiModelProperty.class).value();
                    list.add(value);
                }
            }
        }
        return list;
    }
}

