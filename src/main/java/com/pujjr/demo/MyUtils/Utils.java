package com.pujjr.demo.MyUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.pujjr.demo.enums.EIntervalMode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.itextpdf.text.pdf.BaseFont;

import net.sf.cglib.beans.BeanMap;
import org.springframework.web.multipart.MultipartFile;

public class Utils {

	public static int seq = 0;

	/**
	 * 从身份证号获取年龄
	 * 
	 * @param idNo
	 *            身份证号
	 * @return 年龄
	 */
	public static int getAgeFromIdno(String idNo) {
		int birthYear = Integer.parseInt(idNo.substring(6, 10));
		int birthMonth = Integer.parseInt(idNo.substring(10, 12));
		int birthDayOfMonth = Integer.parseInt(idNo.substring(12, 14));
		Calendar cl = Calendar.getInstance();
		int currYear = cl.get(Calendar.YEAR);
		int currMonth = cl.get(Calendar.MONTH) + 1;
		int currDayOfMonth = cl.get(Calendar.DAY_OF_MONTH);
		int age = 0;
		if (birthYear == currYear) {
			age = 0;
		} else if (birthYear < currYear) {
			if (birthMonth < currMonth) {
				age = currYear - birthYear;
			} else if (birthMonth == currMonth) {
				if (birthDayOfMonth <= currDayOfMonth) {
					age = currYear - birthYear;
				} else {
					age = currYear - birthYear - 1;
				}
			} else {
				age = currYear - birthYear - 1;
			}
		}
		return age;
	}

	/**
	 * 从身份证号获取性别
	 * 
	 * @param idNo
	 *            身份证号
	 * @return 性别：男、女
	 */
	public static String getSexFromIdno(String idNo) {
		int sexNum = 0;
		String sex = "xb01";// 男：xb01 女：xb02
		if (idNo.length() == 18) {
			sexNum = Integer.parseInt(idNo.substring(16, 17));
		} else {
			sexNum = Integer.parseInt(idNo.substring(idNo.length() - 1, idNo.length()));
		}
		if (sexNum % 2 == 0) {
			sex = "xb02";
		}
		return sex;
	}
	/**
	 * 从身份证号获取性别
	 * 
	 * @param idNo
	 *            身份证号
	 * @return 性别：男、女
	 */
	public static String getSexNmFromIdno(String idNo) {
		int sexNum = 0;
		String sex = "先生";// 男：xb01 女：xb02
		if (idNo.length() == 18) {
			sexNum = Integer.parseInt(idNo.substring(16, 17));
		} else {
			sexNum = Integer.parseInt(idNo.substring(idNo.length() - 1, idNo.length()));
		}
		if (sexNum % 2 == 0) {
			sex = "女士";
		}
		return sex;
	}

	/**
	 * 获取set方法名
	 * 
	 * @param attrName
	 *            属性名
	 * @return 对应set方法
	 */
	public static String getSetMethodName(String attrName) {
		String methodName = "";
		if (attrName.length() > 0)
			methodName = "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());
		return methodName;
	}

	/**
	 * 获取get方法名
	 * 
	 * @param attrName
	 *            属性名
	 * @return 对应get方法名
	 */
	public static String getGetMethodName(String attrName) {
		String methodName = "";
		if (attrName.length() > 0)
			methodName = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());
		return methodName;
	}

	/**
	 * 获取当前年 tom 2017年3月17日
	 * 
	 * @return
	 */
	public static String getCurrYear() {
		Calendar currCl = Calendar.getInstance();
		currCl.setTime(new Date());
		return currCl.get(Calendar.YEAR) + "";
	}

	/**
	 * 获取当前月 tom 2017年3月17日
	 * 
	 * @return
	 */
	public static String getCurrMonth() {
		Calendar currCl = Calendar.getInstance();
		currCl.setTime(new Date());
		return Utils.leftPadding(currCl.get(Calendar.MONTH) + 1, "0", 2);
	}

	/**
	 * 获取当前日 tom 2017年3月17日
	 * 
	 * @return
	 */
	public static String getCurrDay() {
		Calendar currCl = Calendar.getInstance();
		currCl.setTime(new Date());
		return Utils.leftPadding(currCl.get(Calendar.DAY_OF_MONTH), "0", 2);

	}

	/**
	 * 字符串右补位 tom 2017年3月3日
	 * 
	 * @param src
	 *            待补位对象(整型、字符串)
	 * @param padding
	 *            补位字符
	 * @param length
	 *            补位后字符串长度
	 * @return 补位后字符串
	 */
	public static String rightPadding(Object src, String padding, int length) {
		String strRet = src + "";
		int len = strRet.length();
		if (len < length) {
			for (int i = 0; i < length - len; i++) {
				strRet = strRet + padding;
			}
		}
		return strRet;
	}

	/**
	 * 字符串左补位 tom 2017年3月3日
	 * 
	 * @param src
	 *            待补位对象(整型、字符串)
	 * @param padding
	 *            补位字符
	 * @param length
	 *            补位后字符串长度
	 * @return 补位后字符串
	 */
	public static String leftPadding(Object src, String padding, int length) {
		String strRet = src + "";
		int len = strRet.length();
		if (len < length) {
			for (int i = 0; i < length - len; i++) {
				strRet = padding + strRet;
			}
		}
		return strRet;
	}

	/**
	 * 获取微软雅黑字体对象(pdf打印) tom 2017年2月27日
	 * 
	 * @param contextPath
	 * @return
	 */
	public static BaseFont getYH(String contextPath) {
		BaseFont bf = null;
		try {
			// 使用普通字体
			// bf = BaseFont.createFont(contextPath+File.separator+"resources"+File.separator+"font"+File.separator+"MSYH.ttf",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			// bf = BaseFont.createFont("MHei-Medium", "UniGB-UCS2-H", true);
			// bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", true);
			// bf = BaseFont.createFont("STSongStd-Light","UniGB-UTF32-H",BaseFont.NOT_EMBEDDED);
			// bf = BaseFont.createFont(contextPath+File.separator+"resources"+File.separator+"font"+File.separator+"MSYH.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bf;
	}

	/**
	 * 获取家庭住址全称 tom 2017年2月9日
	 * 
	 * @param addrProvinceName
	 *            省
	 * @param addrCityName
	 *            市
	 * @param addrCountyName
	 *            区
	 * @param addrExt
	 *            地址明细
	 * @return
	 */
	public static String getAddrFullName(String addrProvinceName, String addrCityName, String addrCountyName, String addrExt) {
		String addrFullName = "";// 地址全称
		if (addrProvinceName == null)
			addrProvinceName = "";
		if (addrCityName == null)
			addrCityName = "";
		if (addrCountyName == null)
			addrCountyName = "";
		if (addrExt == null)
			addrExt = "";
		addrFullName = StringUtils.trimAllWhitespace(addrProvinceName + " " + addrCityName + " " + addrCountyName + " " + addrExt);
		return addrFullName;

	}

	/**
	 * 格式化对象中的Double对象 tom 2016年11月23日
	 * 
	 * @param obj
	 *            待转换Double成员属性对象
	 * @param scale
	 *            转换后Double成员属性小数位数
	 * @return 转换后对象（当前对象）
	 */
	public static Object formateDoubleOfObject(Object obj, int scale) {
		Class objClass = obj.getClass();
		Field[] fields = objClass.getDeclaredFields();
		Method[] methods = objClass.getMethods();
		List<Field> fieldList = Utils.getFieldList(objClass);
		for (Field field : fieldList) {
			if (field.getType().getName().equals("double") || field.getType().getName().equals("java.lang.Double")) {// 目前仅支持转double、Double对象数据
				try {
					// System.out.println(field.getName());
					String getMethodStr = Utils.field2GetMethod(field.getName());
					String setMethodStr = Utils.field2SetMethod(field.getName());
					Method getMethod = objClass.getMethod(getMethodStr);
					Method setMethod = null;
					try {
						setMethod = objClass.getMethod(setMethodStr, Double.class);
					} catch (Exception e) {
						setMethod = objClass.getMethod(setMethodStr, double.class);
					}
					Double score = (Double) getMethod.invoke(obj, null);
					if (score != null)
						setMethod.invoke(obj, Utils.formateDouble2Double(score, scale));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	/**
	 * 获取当前日期 tom 2016年11月14日
	 * 
	 * @return 当前日期
	 */
	public static Date getDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * 属性拷贝(拷贝list成员变量) tom 2016年11月7日
	 * 
	 * @param source
	 *            源对象
	 * @param dest
	 *            目标对象
	 */
	/*
	 * public static void copyPropertiesDeep(Object source,Object dest){ Class srcCls = source.getClass(); Class destCls = dest.getClass(); List<Field> srcFieldList = Utils.getFieldList(srcCls); List<Field> destFieldList = Utils.getFieldList(destCls); Method[] srcMethods = srcCls.getMethods(); Method[] destMethods = destCls.getMethods(); List destList = new ArrayList(); for (int i = 0; i < srcFieldList.size(); i++) { Field srcField = srcFieldList.get(i); String srcFieldName = srcField.getName(); Class
	 * srcFieldType = srcField.getType(); String srcFieldTypeName = srcFieldType.getName(); Object srcFieldValue = null; try {//处理source中list成员变量 srcFieldValue = srcField.get(source); // System.out.println(srcFieldType.getName().equals(double.class.getName())+"*********|"+srcFieldValue); for (Field destField : destFieldList) { String destFieldName = destField.getName(); Class destFieldType = destField.getType(); if(srcFieldName.equals(destFieldName)){
	 * if(srcFieldTypeName.equals(List.class.getName())){//判断list变量 // System.out.println(srcFieldType+"|"+srcFieldName+"|"+srcField.getGenericType()); List tempSrcFieldValue = (List) srcField.get(source); Type gType = destField.getGenericType(); ParameterizedType pType = (ParameterizedType) gType; Type[] types = pType.getActualTypeArguments(); //
	 * System.out.println("****types[0]:"+types[0]+"|"+"types[0].getTypeName():"+types[0].getTypeName()+"|"+types[0].getTypeName().equals(RepayScheduleDetailPo.class.getTypeName())+"|"+types[0].getClass()); // System.out.println("ttt:"+srcField.get(source)); for (Object object : tempSrcFieldValue) { // Object rsdv = Class.forName(types[0].getTypeName()).newInstance();//目前仅仅拷贝list泛型中含有一个参数的情况，如：List<String> Object rsdv = Class.forName(types[0].toString().split(" ")[1]).newInstance();
	 * Utils.copyProperties(object, rsdv); destList.add(rsdv); } } } } } catch (Exception e) { e.printStackTrace(); } //处理resource中普通成员变量 for (Field destField : destFieldList) { String destFieldName = destField.getName(); Class destFieldType = destField.getType(); // System.out.println(destFieldType+"|"+destFieldName); if(destFieldName.equals(srcFieldName)){ for (int j = 0; j < destMethods.length; j++) { Method destMethod = destMethods[j]; String methodName = destMethod.getName();
	 * if(("set"+destFieldName).toLowerCase().equals(methodName.toLowerCase())){ try { if(srcFieldTypeName.equals(List.class.getName())){ destMethod.invoke(dest, destList); }else if(srcFieldValue != null){ if(srcFieldTypeName.equals(Date.class.getName())){ SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd"); destMethod.invoke(dest, formater.format(srcFieldValue)); }else if(srcFieldTypeName.equals(Double.class.getName()) || srcFieldTypeName.equals(double.class.getName())){ destMethod.invoke(dest,
	 * Utils.formateDouble2String((double)srcFieldValue, 2)); // destMethod.invoke(dest, srcFieldValue); }else if(srcFieldTypeName.equals(Integer.class.getName()) || srcFieldTypeName.equals(int.class.getName())){ destMethod.invoke(dest, srcFieldValue+""); // destMethod.invoke(dest, srcFieldValue); }else if(!srcFieldType.isPrimitive()){ destMethod.invoke(dest, srcFieldValue); } //
	 * System.out.println("***********************srcFieldType.isMemberClass():"+srcFieldType.getName()+"|destFieldType:"+destFieldType.getName()+"|"+srcFieldType.getSuperclass()); } } catch (Exception e) { e.printStackTrace(); } } } } } } }
	 */

	/**
	 * 双精度浮点数转制定格式字符串 tom 2016年11月2日
	 * 
	 * @param number
	 *            数据源
	 * @param scale
	 *            小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1 scale=3 输出："123.100"）
	 */
	public static String formateDouble2String(double number, int scale) {
		String formateDouble = "";
		BigDecimal formater = new BigDecimal(number);
		// new Double("");
		formateDouble = formater.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		return formateDouble;
	}

	public static String formateDouble2String(Double number, int scale) {
		if (number != null) {
			return formateDouble2String(number.doubleValue(), scale);
		}
		return null;
	}

	/**
	 * 双精度浮点数转指定格式双进度浮点数 tom 2016年11月2日
	 * 
	 * @param scale
	 *            小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1对应BigDecimal对象 scale=3 输出：123.1）
	 */
	public static Double formateDouble2Double(BigDecimal bigDecimal, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 双精度浮点数转指定格式双精度浮点数 tom 2016年11月2日
	 * 
	 * @param number
	 *            数据源
	 * @param scale
	 *            小数位数
	 * @return 格式化后双精度浮点数（输入：number=123.1 scale=3 输出：123.1）
	 */
	public static Double formateDouble2Double(double number, int scale) {
		Double formateDouble = 0.00;
		try {
			BigDecimal formater = new BigDecimal(number);
			formateDouble = formater.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception e) {
		}

		return formateDouble;
	}

	/**
	 * 获取时间间隔 tom 2016年11月8日
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            截止日期
	 * @param intervalMode
	 *            间隔模式
	 * @return 时间间隔
	 */
	public static long getTimeInterval(Date beginDate, Date endDate, EIntervalMode intervalMode) {
		long interval = 0;
		Calendar beginCl = Calendar.getInstance();
		Calendar endCl = Calendar.getInstance();
		beginCl.setTime(beginDate);
		endCl.setTime(endDate);
		switch (intervalMode.name()) {
		case "YEARS":
			interval = endCl.get(Calendar.YEAR) - beginCl.get(Calendar.YEAR);
			break;
		case "MONTHS":
			interval = (endCl.get(Calendar.YEAR) - beginCl.get(Calendar.YEAR)) * 12 + endCl.get(Calendar.MONTH) - beginCl.get(Calendar.MONTH);
			break;
		case "DAYS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis()) / (24 * 60 * 60 * 1000);
			break;
		case "HOURS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis()) / (60 * 60 * 1000);
			break;
		case "MINUTES":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis()) / (60 * 1000);
			break;
		case "SECONDS":
			interval = (endCl.getTimeInMillis() - beginCl.getTimeInMillis()) / (1000);
		case "MIllISECCONDS":
			interval = endCl.getTimeInMillis() - beginCl.getTimeInMillis();
			break;
		}
		return interval;
	}

	/**
	 * 日期格式化 tom 2016年11月7日
	 * 
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static Date formateDate(Date date, String formateStr) {
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		Date dateRet = null;
		try {
			dateRet = formate.parse(formate.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateRet;
	}

	/**
	 * 字符串转日期 tom 2016年11月7日
	 * 
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static Date formateString2Date(String date, String formateStr) {
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		Date dateRet = null;
		try {
			dateRet = formate.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateRet;
	}

	/**
	 * 日期转字符串 tom 2016年11月7日
	 * 
	 * @param date
	 * @param formateStr
	 * @return
	 */
	public static String formateDate2String(Date date, String formateStr) {
		SimpleDateFormat formate = new SimpleDateFormat(formateStr);
		String dateRet = "";
		try {
			dateRet = formate.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateRet;
	}

	/**
	 * @param fieldName
	 *            属性名
	 * @return 属性对应get方法
	 */
	public static String field2GetMethod(String fieldName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("get");
		buffer.append(fieldName.substring(0, 1).toUpperCase());
		buffer.append(fieldName.substring(1, fieldName.length()));
		return buffer.toString();
	}

	/**
	 * @param fieldName
	 *            属性名
	 * @return 属性对应set方法
	 */
	public static String field2SetMethod(String fieldName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("set");
		buffer.append(fieldName.substring(0, 1).toUpperCase());
		buffer.append(fieldName.substring(1, fieldName.length()));
		return buffer.toString();
	}

	/**
	 * 获取指定日期所在当月天 tom 2016年10月28日
	 */
	public static String getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) + "";
	}

	/**
	 * 数字金额转中文大写
	 * 
	 * @param money
	 *            数字金额
	 * @return 中文大写金额
	 */
	public static String number2Chn(double money) {
		if (money == 0)
			return "零元整";
		String number = money + "";
		/**
		 * 人民币大写单位制
		 */
		HashMap<Integer, String> dws = new HashMap<Integer, String>();
		dws.put(-2, "分");
		dws.put(-1, "角");
		dws.put(0, "元");
		dws.put(1, "拾");
		dws.put(2, "佰");
		dws.put(3, "仟");
		dws.put(4, "万");//
		dws.put(5, "拾");
		dws.put(6, "佰");
		dws.put(7, "仟");
		dws.put(8, "亿");//
		dws.put(9, "拾");
		dws.put(10, "佰");
		dws.put(11, "仟");
		dws.put(12, "万");
		/**
		 * 数字对应的中文
		 */
		String[] jes = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

		StringBuffer su = new StringBuffer();
		// 整数部分
		number = delInvalidZero(number);
		String str = null;
		// 小数部分
		String decimal = null;
		if (number.contains(".")) {
			// 截取整数位
			str = number.split("\\.")[0];
			decimal = number.split("\\.")[1];
		} else {
			str = number;
		}
		// 判断是否存在整数位
		if (str.length() > 0) {
			for (int i = 0; i < str.length(); i++) {
				String context = str.substring(i, i + 1);
				int pow = str.length() - i - 1;
				Integer val = Integer.parseInt(context.toString());
				// 获取中文单位
				String sign = dws.get(pow);
				// 获取中文数字
				String name = jes[Integer.parseInt(context)];
				if (val == 0) {
					if (pow % 4 != 0) {// 删除单位
						sign = "";
					}
					if (i < str.length() - 1) {
						Integer val1 = Integer.parseInt(str.substring(i + 1, i + 2));
						if (val == 0 && val == val1) {
							name = "";
						}
					} else if (i == str.length() - 1) {
						name = "";
					}
				}
				su.append(name + sign);
			}
		}
		// 判断是否存在小数位
		// System.out.println(decimal);
		if (decimal != null && (Integer.parseInt(decimal) != 0)) {
			str = decimal.substring(0, 1);
			if (!"0".equals(str)) {
				su.append(jes[Integer.parseInt(str)] + dws.get(-1));
			}
			if (decimal.length() == 2) {
				str = decimal.substring(1, 2);
				if (!"0".equals(str)) {
					su.append(jes[Integer.parseInt(str)] + dws.get(-2));
				}
			}
		}
		String target = su.toString();
		if (target.length() >= 5) {
			String index2 = target.substring(1, 2);
			if (index2.equals("拾")) {
				String index3 = target.substring(2, 3);
				String index4 = target.substring(3, 4);
				if (index3.equals("零") && index4.equals("万")) {
					target = target.substring(0, 2) + index4 + index3 + target.substring(4, target.length());
				}
			}
		}
		return target;
	}

	public static String number2ChnV2(double money) {
		if (money == 0)
			return "零";
		String number = money + "";
		/**
		 * 人民币大写单位制
		 */
		HashMap<Integer, String> dws = new HashMap<Integer, String>();
		dws.put(-2, "分");
		dws.put(-1, "角");
		dws.put(0, "元");
		dws.put(1, "拾");
		dws.put(2, "佰");
		dws.put(3, "仟");
		dws.put(4, "万");//
		dws.put(5, "拾");
		dws.put(6, "佰");
		dws.put(7, "仟");
		dws.put(8, "亿");//
		dws.put(9, "拾");
		dws.put(10, "佰");
		dws.put(11, "仟");
		dws.put(12, "万");
		/**
		 * 数字对应的中文
		 */
		String[] jes = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

		StringBuffer su = new StringBuffer();
		// 整数部分
		number = delInvalidZero(number);
		String str = null;
		// 小数部分
		String decimal = null;
		if (number.contains(".")) {
			// 截取整数位
			str = number.split("\\.")[0];
			decimal = number.split("\\.")[1];
		} else {
			str = number;
		}
		// 判断是否存在整数位
		if (str.length() > 0) {
			for (int i = 0; i < str.length(); i++) {
				String context = str.substring(i, i + 1);
				int pow = str.length() - i - 1;
				Integer val = Integer.parseInt(context.toString());
				// 获取中文单位
				String sign = dws.get(pow);
				// 获取中文数字
				String name = jes[Integer.parseInt(context)];
				if (val == 0) {
					if (pow % 4 != 0) {// 删除单位
						sign = "";
					}
					if (i < str.length() - 1) {
						Integer val1 = Integer.parseInt(str.substring(i + 1, i + 2));
						if (val == 0 && val == val1) {
							name = "";
						}
					} else if (i == str.length() - 1) {
						name = "";
					}
				}
				su.append(name + sign);
			}
		}
		// 判断是否存在小数位
		// System.out.println(decimal);
		if (decimal != null && (Integer.parseInt(decimal) != 0)) {
			str = decimal.substring(0, 1);
			if (!"0".equals(str)) {
				su.append(jes[Integer.parseInt(str)] + dws.get(-1));
			}
			if (decimal.length() == 2) {
				str = decimal.substring(1, 2);
				if (!"0".equals(str)) {
					su.append(jes[Integer.parseInt(str)] + dws.get(-2));
				}
			}
		}
		String target = su.toString();
		target = target.replace("元", "");
		if (target.length() >= 5) {
			String index2 = target.substring(1, 2);
			if (index2.equals("拾")) {
				String index3 = target.substring(2, 3);
				String index4 = target.substring(3, 4);
				if (index3.equals("零") && index4.equals("万")) {
					target = target.substring(0, 2) + index4 + index3 + target.substring(4, target.length());
				}
			}
		}
		return target;
	}

	/**
	 * 清理数字特殊字符
	 * 
	 * @param str
	 * @return
	 */
	private static String delInvalidZero(String str) {
		if ("0".equals(str.substring(0, 1))) {
			return delInvalidZero(str.substring(1, str.length()));
		} else if (str.contains(",")) {
			return delInvalidZero(str.replaceAll(",", ""));
		} else {
			return str;
		}
	}

	/**
	 * 递归所有父类field
	 * 
	 * @param obj
	 *            当前递归对象
	 * @param fieldList
	 *            所有field列表
	 */
	public static void getField(Class obj, List<Field> fieldList) {
		Field[] fields = obj.getDeclaredFields();
		if (!obj.getName().equals("java.lang.Object")) {
			for (Field field : fields) {
				field.setAccessible(true);
				fieldList.add(field);
			}
			Utils.getField(obj.getSuperclass(), fieldList);
		}
	}

	/**
	 * 获取对象所有field
	 * 
	 * @param obj
	 * @return
	 */
	public static List<Field> getFieldList(Class obj) {
		List<Field> fieldList = new LinkedList<Field>();
		Utils.getField(obj, fieldList);
		return fieldList;
	}

	/**
	 * @param date
	 *            给定日期
	 * @param interval
	 *            间隔天数，示例：5：5天以后;-6:6天以前
	 * @return 间隔后日期
	 */
	public static Date getDateAfterDay(Date date, int interval) {
		String afterYear = "";
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_MONTH, interval);
		return calender.getTime();
	}

	/**
	 * @param date
	 *            给定日期
	 * @param interval
	 *            间隔月份，示例：5：5个月以后;-6:6个月以前
	 * @return 间隔后日期
	 */
	public static Date getDateAfterMonth(Date date, int interval) {
		String afterYear = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, interval);
		return calendar.getTime();
	}

	/**
	 * @param date
	 *            给定日期
	 * @param interval
	 *            间隔年份，示例：5：5年以后;-6:6年以前
	 * @return 间隔后日期
	 */
	public static Date getDateAfterYear(Date date, int interval) {
		String afterYear = "";
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.YEAR, interval);
		return calender.getTime();
	}

	/**
	 * 对象属性拷贝
	 * 
	 * @param source
	 *            数据源对象
	 * @param target
	 *            目标对象
	 * @author pujjr 2016-10-09
	 */
	public static void copyProperties(Object source, Object target) {
		if (source != null)
			BeanUtils.copyProperties(source, target);
		else
			target = null;
	}

	public static String convertStr2Utf8(String value) throws UnsupportedEncodingException {
		if (value != null) {
			value = new String(value.getBytes("ISO8859-1"), "UTF-8");
		}
		return value;
	}

	/**
	 * 过滤null对象
	 * 
	 * @param obj
	 * @return
	 */
	public static String nullFilter(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 对应数据表列名转对象属性
	 * 
	 * @param colName
	 *            输入格式："my_col_name"
	 * @return 返回格式：：myColName
	 */
	public static String col2Field(String colName) {
		StringBuffer fieldNameBuf = new StringBuffer();
		String[] colNames = colName.split("_");
		for (int i = 0; i < colNames.length; i++) {
			String temp = colNames[i];
			if (i == 0)
				fieldNameBuf.append(temp);
			else {
				fieldNameBuf.append(temp.substring(0, 1).toUpperCase());
				fieldNameBuf.append(temp.substring(1, temp.length()));
			}
		}
		return fieldNameBuf.toString();
	}

	/**
	 * 对象属性转换为对应数据表列名
	 * 
	 * @param propName
	 *            输入格式："myUserName"
	 * @return 返回格式：：MY_USER_NAME
	 */
	public static String field2Col(String propName) {
		// System.out.println("对象属性转换前："+propName);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < propName.length(); i++) {
			char c = propName.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_" + Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 获取日期天数
	 **/
	public static int getSpaceDay(Date beginDate, Date endDate) {
		Date fBeginDate = Utils.formateDate(beginDate, "yyyyMMdd");
		Date fEndDate = Utils.formateDate(endDate, "yyyyMMdd");
		return (int) ((fEndDate.getTime() - fBeginDate.getTime()) / (24 * 60 * 60 * 1000));
	}

	/**
	 * 比较日期大小
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return 大于0则beginDate<endDate 等于0则beginDate=endDate 小于0则beginDate>endDate
	 */
	public static int compareDate(Date beginDate, Date endDate) {
		Date fBeginDate = Utils.formateDate(beginDate, "yyyyMMdd");
		Date fEndDate = Utils.formateDate(endDate, "yyyyMMdd");
		return (int) ((fEndDate.getTime() - fBeginDate.getTime()) / (24 * 60 * 60 * 1000));
	}

	/** 比较时间大小 **/
	public static long compareDateTime(Date beginDate, Date endDate) {
		Long space = (endDate.getTime() - beginDate.getTime());
		return Long.compare(space, 0);
	}

	/**
	 * 按照指定格式获取当前日期
	 **/
	public static String getCurrentTime(String format) {
		if (format == null || format == "" || format.length() == 0) {
			format = "yyyyMMddHHmmss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
		return df.format(new Date());
	}

	/**
	 * 根据日期获取年份
	 **/
	public static String getYear(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");// 设置日期格式
		return df.format(date);
	}

	/**
	 * 根据日期获取月份
	 **/
	public static String getMonth(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("MM");// 设置日期格式
		return df.format(date);
	}

	/**
	 * 日期转字符串
	 **/
	public static String getFormatDate(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
		return df.format(date);
	}

	/**
	 * 根据年份获取当年天数
	 **/
	public static int getYearDays(String year) {
		if ((Integer.valueOf(year) % 4 == 0 && Integer.valueOf(year) / 100 != 0) || (Integer.valueOf(year) / 400 == 0)) {
			return 366;
		} else {
			return 365;
		}
	}

	/**
	 * 8日期字符串转日期格式
	 * 
	 * @throws ParseException
	 **/
	public static Date str82date(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		return df.parse(date);
	}

	public static Timestamp str2time(String time) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");// 设置日期格式
		return new Timestamp((df.parse(time)).getTime());
	}

	public static String get16UUID() {
		String uuid = UUID.randomUUID().toString();
		byte[] outputByteArray;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] inputByteArray = uuid.getBytes();
			messageDigest.update(inputByteArray);
			outputByteArray = messageDigest.digest();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < outputByteArray.length; offset++) {
			int i = outputByteArray[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString().substring(8, 24);
	}
	
	/**
	 * 获取文件后缀名，如.xlsx等等
	 * 
	 * @param fileName
	 *            
	 * @return fileName
	 */
	public static String getFileSuffix(String fileName) {
		int indexSuffix;
		if ((indexSuffix = fileName.lastIndexOf(".")) != -1) {
			return fileName.substring(indexSuffix, fileName.length());
		}
		return fileName;
	}

	public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (".xls".equals(fileType)) {
			wb = new HSSFWorkbook(inStr);  //2003-
		} else if (".xlsx".equals(fileType)) {
			wb = new XSSFWorkbook(inStr);  //2007+
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}
	/**
	 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 克隆List
	 * 
	 * @param src
	 *            源List
	 **/
	public static List<Object> cloneList(List<Object> src) {
		List<Object> dest = new ArrayList<Object>(Arrays.asList(new Object[src.size()]));
		Collections.copy(dest, src);
		return dest;
	}

	/**
	 * 金额元转分
	 * 
	 * @param amount
	 * @return
	 */
	public static String convertY2F(double amount) {
		NumberFormat nf = new DecimalFormat("#");
		return nf.format(amount * 100);
	}

	/**
	 * HTML时间转换为JAVA时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date htmlTime2Date(String time, String format) throws ParseException {
		String tmp = time.replace("Z", " UTC");// 注意是空格+UTC
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");// 注意格式化的表达式
		tmp = Utils.getFormatDate(sd.parse(tmp), format);
		return Utils.formateString2Date(tmp, format);
	}

	public static int getDateDay(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		return a.get(Calendar.DATE);
	}

	public static boolean doubleIsZero(double val) {
		if (Math.abs(val - 0.00) < 0.001) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * bean转换为map
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = new HashMap<>();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	/**
	 * map转换为string
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToString(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		for (Object key : map.keySet()) {
			if (map.get(key) != null) {
				if (map.get(key) instanceof Date) {
					if (sb.toString().equals("")) {
						sb.append(key + "=" + Utils.formateDate2String((Date) map.get(key), "yyyy-MM-dd HH:mm:ss").replace(" ", "T") + ".000Z");
					} else {
						sb.append("&" + key + "=" + Utils.formateDate2String((Date) map.get(key), "yyyy-MM-dd HH:mm:ss").replace(" ", "T") + ".000Z");
					}
				} else {
					if (sb.toString().equals("")) {
						sb.append(key + "=" + map.get(key));
					} else {
						sb.append("&" + key + "=" + map.get(key));
					}
				}

			}
		}
		return sb.toString();
	}

	/**
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 *
	 * @param file
	 *            读取数据的源Excel
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @return 读出的Excel中数据的内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[][] getExcelData(File file, int ignoreRows) throws FileNotFoundException, IOException {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		// 打开HSSFWorkbook
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			HSSFSheet st = wb.getSheetAt(sheetIndex);
			// 第一行为标题，不取
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = row.getLastCellNum() + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasValue = false;
				for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
					String value = "";
					cell = row.getCell(columnIndex);
					if (cell != null) {
						// 注意：一定要设成这个，否则可能会出现乱码,后面版本默认设置
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								Date date = cell.getDateCellValue();
								if (date != null) {
									value = new SimpleDateFormat("yyyy-MM-dd").format(date);
								} else {
									value = "";
								}
							} else {
								value = new DecimalFormat("0").format(cell

										.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (!cell.getStringCellValue().equals("")) {
								value = cell.getStringCellValue();
							} else {
								value = cell.getNumericCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = "";
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = (cell.getBooleanCellValue() == true ? "Y"

									: "N");
							break;
						default:
							value = "";
						}
					}
					if (columnIndex == 0 && value.trim().equals("")) {
						break;
					}
					values[columnIndex] = rightTrim(value);
					hasValue = true;
				}
				if (hasValue) {
					result.add(values);
				}
			}
		}
		in.close();
		String[][] returnArray = new String[result.size()][rowSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = result.get(i);
		}
		return returnArray;
	}

	/**
	 * 读取excel 支持2003 2007 2010
	 * 
	 * @param file
	 *            文件
	 * @param ignoreRows
	 *            开始读取的行数
	 * @return
	 */
	public static String[][] readExcel(File file, int ignoreRows) {
		int rowSize = 0;
		List<String[]> result = new ArrayList<String[]>();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 同时支持Excel 2003、2007
			FileInputStream is = new FileInputStream(file); // 文件流
			Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel 2003/2007/2010 都是可以处理的
			int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
			// 遍历每个Sheet
			for (int s = 0; s < 1; s++) {
				Sheet sheet = workbook.getSheetAt(s);
				int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数
				// 遍历每一行
				for (int r = ignoreRows; r < rowCount; r++) {
					Row row = sheet.getRow(r);
					int cellCount = row.getPhysicalNumberOfCells(); // 获取总列数
					String[] rowValue = new String[cellCount];
					// 遍历每一列
					for (int c = 0; c < cellCount; c++) {
						Cell cell = row.getCell(c);
						if (cell == null) {
							continue;
						}
						int cellType = cell.getCellType();
						String cellValue = null;
						switch (cellType) {
						case Cell.CELL_TYPE_STRING: // 文本
							cellValue = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC: // 数字、日期
							if (DateUtil.isCellDateFormatted(cell)) {
								cellValue = fmt.format(cell.getDateCellValue()); // 日期型
							} else {
								cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
							}
							break;
						case Cell.CELL_TYPE_BOOLEAN: // 布尔型
							cellValue = String.valueOf(cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_BLANK: // 空白
							cellValue = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_ERROR: // 错误
							cellValue = "错误";
							break;
						case Cell.CELL_TYPE_FORMULA: // 公式
							cellValue = "错误";
							break;
						default:
							cellValue = "错误";
						}
						rowValue[c] = cellValue;
					}

					if (cellCount < 21) {
						String[] rowValue17 = new String[21];
						System.arraycopy(rowValue, 0, rowValue17, 0, cellCount);
						for (int i = cellCount - 1; i < 21; i++) {
							rowValue17[i] = "";
						}
						result.add(rowValue17);
					} else {
						result.add(rowValue);
					}

				}
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[][] returnArray = new String[result.size()][rowSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = result.get(i);
		}
		return returnArray;
	}

	/**
	 * 去掉字符串右边的空格
	 *
	 * @param str
	 *            要处理的字符串
	 * @return 处理后的字符串
	 */

	public static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}

	// 阿拉伯数字转小写中文
	public static String convertNumber2Chn(int number) {
		String sd = "";
		switch (number) {
		case 1:
			sd = "一";
			break;
		case 2:
			sd = "二";
			break;
		case 3:
			sd = "三";
			break;
		case 4:
			sd = "四";
			break;
		case 5:
			sd = "五";
			break;
		case 6:
			sd = "六";
			break;
		case 7:
			sd = "七";
			break;
		case 8:
			sd = "八";
			break;
		case 9:
			sd = "九";
			break;
		default:
			break;
		}
		return sd;
	}

	/**
	 * 字符串转换为日期，调用方式：DateOperationUtil.stringToDate("2015-9-9", "yyyy-MM-dd HH:mm:ss");
	 * 
	 * @param time     要转换的字符串
	 * @param pattern  转换格式，默认为yyyy-MM-dd
	 * @return         返回转换后的日期，如果值为null表示转换异常
	 */
	public static Date stringToDate(String time, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(getPattern(pattern));
		try {
			return dateFormat.parse(time);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 日期转换为字符串，调用方式：DateOperationUtil.dateToString("2015-9-9", "yyyy-MM-dd HH:mm:ss");
	 * 
	 * @param time     要转换的日期
	 * @param pattern  转换格式，默认为yyyy-MM-dd
	 * @return 转换后的字符串
	 */
	public static String dateToString(Date time, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(getPattern(pattern));
		return dateFormat.format(time);
	}
	
	/**
	 * 设置转换格式
	 * 
	 * @param pattern  转换格式，默认为yyyy-MM-dd
	 * @return 返回匹配格式
	 */
	private static String getPattern(String pattern) {
		if (pattern == null || pattern.trim() == "") {
			pattern = "yyyy-MM-dd";
		}
		return pattern;
	}
}
