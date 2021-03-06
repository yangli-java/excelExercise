package com.pujjr.demo.doman;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserInfoExcelView extends ExcelView  {
    @Override
    public void setRow(Sheet sheet, Map<String, Object> map) {
        //设置表头
        Row header = sheet.createRow(0);
        String[] UserSheet = {"编码","用户名","密码","地址","生日","性别"};
        List<String> titles = getTitlesByClass(User.class);
        for (int i = 0; i < UserSheet.length; i++) {
            header.createCell(i).setCellValue(UserSheet[i]);
        }
        @SuppressWarnings("unchecked")
        List<User> list = (List<User>) map.get("members");
        //给每一个单元格赋值
        for (int i = 0; i < list.size() ; i++) {
            int rowCount = i+1;
            Row userRow = sheet.createRow(rowCount);
            userRow.createCell(0).setCellValue(list.get(i).getId());
            userRow.createCell(1).setCellValue(list.get(i).getUsername());
            userRow.createCell(2).setCellValue(list.get(i).getPassword());
            userRow.createCell(3).setCellValue(list.get(i).getAddress());
            Date birthday = list.get(i).getBirthday();
            String textValue = null;
            //时间格式的转换
            if (birthday instanceof Date)
            {
                Date date =  birthday;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                textValue = sdf.format(date);
            }
            HSSFRichTextString richString = new HSSFRichTextString(textValue);
//            userRow.createCell(4).setCellValue(list.get(i).getBirthday());
            userRow.createCell(4).setCellValue(richString);
            userRow.createCell(5).setCellValue(list.get(i).getSex());
        }
    }
}
