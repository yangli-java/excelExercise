package com.pujjr.demo.controller;

import com.pujjr.demo.MyUtils.Utils;
import com.pujjr.demo.doman.JsonData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ImportExcels {

    //文件存放路径
    private static final String ExcelsPath = "E:/GitProjects/excelExercise/src/main/resources/static/Excels/";

    @RequestMapping("/index")
    public Object index(){
        return "index";
    }

    @PostMapping("/toImport")
    @ResponseBody
    public Object toImport(MultipartFile file) throws Exception {
        String fileSuffix = Utils.getFileSuffix(file.getOriginalFilename());
        if(!fileSuffix.equals(".xlsx") && !fileSuffix.equals(".xls") ){
            return new JsonData("1","提示信息：上传文件格式不正确！");
        }
        List<List<Object>> excelList = this.getBankListByExcel(file);
        String realFileName = UUID.randomUUID() + file.getOriginalFilename();
        File dest = new File(ExcelsPath+realFileName);
        try {
            file.transferTo(dest);
            return new JsonData("0","提示信息：上传成功！");
        }catch(Exception e){
            e.printStackTrace();
        }

        return new JsonData("1","提示信息：上传失败！");
    }

    public List<List<Object>> getBankListByExcel(MultipartFile file) throws Exception {
        //创建Excel工作薄
        Workbook workbook = Utils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
        //Sheet  sheet = workbook.createSheet(“工作表名”)；
        //Row row = sheet.createRow(0)，//0 表示行的索引，从0开始；
        //Cell cell = row.createCell(0)，//0 表示单元格的索引，从0开始。
        int number = workbook.getNumberOfSheets();
        //表格
        Sheet sheet = null;
        //行
        Row row = null;
        //列
        Cell cell = null;
        //遍历所有的sheet，并装入到集合里面
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum()+1; j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                //遍历所有的列
                List<Object> li = new ArrayList<>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(this.getCellValue(cell));
                }
                list.add(li);
            }
        }
        return list;
    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    public Object getCellValue(Cell cell) {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.0");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }
}
