/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: FileUtil
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午03:06:56
 * @version 1.0
 */
package com.trs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.text.DecimalFormat;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:文件操作类，对外提供对不同文件的操作（读取）方法
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liujian
 * @ClassName: FileUtil
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午03:06:56
 * @version 1.0
 */
public class FileUtil {
	public File uploadFile(MultipartFile file) throws Exception {
		FileOutputStream foStream  = null;
		java.io.File nFile = new java.io.File(ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+"/appadmin/"+file.getOriginalFilename());				
		foStream  = new FileOutputStream(nFile);
		foStream.write(file.getBytes());
		return nFile;
	}
	/**
	 * @Description: 对外提供读取properties文件
	 * @author liujian
	 * @date 2014-3-10 下午03:46:25
	 * @param sFile
	 *            文件全路径字符串
	 * @return Map对象（属性和相应值的键值对）
	 * @throws IOException
	 * @version 1.0
	 */
	public static Map<String, String> readProperties(InputStream in)
			throws IOException {
		//InputStream in = new FileInputStream(sFile); 
		//FileUtil.class.getClassLoader().getResourceAsStream(sFile);
		Properties properties = new Properties();
		properties.load(in);
		if (properties.isEmpty()) {
			return null;
		}
		Map<String, String> data = new HashMap<String, String>();
		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (key != null && value != null) {
				data.put(key.toString(), value.toString());
			}
			/*
			 * System.out.println("key   :" + key); System.out.println("value :"
			 * + value); System.out.println("---------------");
			 */
		}
		in.close();
		return data;
	}

	/**
	 * @Description: 对外提供读取properties文件 <BR>
	 * @author liujian
	 * @date 2014-3-10 下午03:46:37
	 * @param InputStream
	 *            文件全路径字符串
	 * @param key
	 *            属性名
	 * @return 属性名对应的值
	 * @throws IOException
	 * @version 1.0
	 */
	public static String getProperty(InputStream in, String key)
			throws IOException {
		//InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(sFile);
		Properties properties = new Properties();
		properties.load(in);
		if (properties.isEmpty()) {
			return null;
		}
		in.close();
		return properties.getProperty(key);
	}
	
	public static Properties getProperties(InputStream in) throws IOException{
		Properties properties = new Properties();
		properties.load(in);
		if (properties.isEmpty()) {
			return null;
		}
		in.close();
		return properties;
	}
	
	/**
	 * @Description: 对外提供读取Excel文件的方法
	 * @author liujian
	 * @date 2014-3-10 下午03:28:20
	 * @param file
	 *            文件对象参数
	 * @return
	 * @throws IOException
	 * @version 1.0
	 */
	public static List<List<Object>> readExcel(File file) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(file);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	/**
	 * @Description: 读取 office 2003 excel
	 * @author liujian
	 * @date 2014-3-10 下午03:36:02
	 * @param file
	 *            文件对象参数
	 * @return
	 * @throws IOException
	 * @version 1.0
	 */
	private static List<List<Object>> read2003Excel(File file)
			throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = hwb.getSheetAt(0);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		for (int i = sheet.getFirstRowNum(); i <= sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
															// 字符
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println(i + "行" + j + " 列 is String type");
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out.println(i + "行" + j
							+ " 列 is Number type ; DateFormt:"
							+ cell.getCellStyle().getDataFormatString());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println(i + "行" + j + " 列 is Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println(i + "行" + j + " 列 is Blank type");
					value = "";
					break;
				default:
					System.out.println(i + "行" + j + " 列 is default type");
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					continue;
				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}

	/**
	 * @Description: 读取Office 2007 excel <BR>
	 * @author liujian
	 * @date 2014-3-10 下午03:36:44
	 * @param file
	 * @return
	 * @throws IOException
	 * @version 1.0
	 */
	private static List<List<Object>> read2007Excel(File file)
			throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		for (int i = sheet.getFirstRowNum(); i <= sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
															// 字符
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println(i + "行" + j + " 列 is String type");
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out.println(i + "行" + j
							+ " 列 is Number type ; DateFormt:"
							+ cell.getCellStyle().getDataFormatString());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println(i + "行" + j + " 列 is Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println(i + "行" + j + " 列 is Blank type");
					value = "";
					break;
				default:
					System.out.println(i + "行" + j + " 列 is default type");
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					continue;
				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}
	
	/**
	 * 
	* Description: 循环单行读取TXT文件内容 <BR>   
	* @author jin.yu
	* @date 2014-3-19 下午08:52:08
	* @param file TXT文件
	* @return List
	* @throws IOException
	* @version 1.0
	 */
	public static List<Object> readTxtFile(File file) throws IOException{
		List<Object> list = new ArrayList<Object>();
		InputStreamReader read = new InputStreamReader( new FileInputStream(file),"utf-8");//考虑到编码格式 
        BufferedReader bufferedReader = new BufferedReader(read); 
        String lineTxt = null; 
        while((lineTxt = bufferedReader.readLine()) != null){ 
        	list.add(lineTxt);
        }
        read.close();
        return list;
	}
	
	/**
	 * 
	* Description: 设置EXCEL的格式为黄底红字 <BR>   
	* @author jin.yu
	* @date 2014-3-25 下午02:01:46
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public static WritableCellFormat getDataCellFormatRed() throws Exception {  
		WritableCellFormat wcf = null;  
		WritableFont wf = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false);  
		// 字体颜色   
		wf.setColour(Colour.RED);  
		wcf = new WritableCellFormat(wf);  
		// 对齐方式   
		wcf.setAlignment(Alignment.CENTRE);  
		wcf.setVerticalAlignment(VerticalAlignment.CENTRE);  
		// 设置上边框   
		wcf.setBorder(Border.TOP, BorderLineStyle.THIN);  
		// 设置下边框   
		wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);  
		// 设置左边框   
		wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);  
		// 设置右边框   
		wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		// 设置背景色 
		wcf.setBackground(Colour.YELLOW);
		// 自动换行
		wcf.setWrap(true);
		return wcf;  
	}
	
	/**
	 * 
	* Description: 设置EXCEL的格式为黄底黑字 <BR>   
	* @author jin.yu
	* @date 2014-3-25 下午02:01:46
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public static WritableCellFormat getDataCellFormatBlack() throws Exception {  
		WritableCellFormat wcf = null;  
		WritableFont wf = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false);  
		// 字体颜色   
		wf.setColour(Colour.BLACK);  
		wcf = new WritableCellFormat(wf);  
		// 对齐方式   
		wcf.setAlignment(Alignment.CENTRE);  
		wcf.setVerticalAlignment(VerticalAlignment.CENTRE);  
		// 设置上边框   
		wcf.setBorder(Border.TOP, BorderLineStyle.THIN);  
		// 设置下边框   
		wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);  
		// 设置左边框   
		wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);  
		// 设置右边框   
		wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		// 设置背景色 
		wcf.setBackground(Colour.YELLOW);
		// 自动换行
		wcf.setWrap(true);
		return wcf;  
	}
	/**
	 * Description: 获取自定义日志格式适配器 <BR>   
	 * @author liu.zhuan
	 * @date 2014-10-28 下午04:57:19
	 * @param conversionPattern 日志格式
	 * @param filePath 日志生成文件目录
	 * @return
	 * @version 1.0
	 */
	public static FileAppender getLogAppender(String conversionPattern, String filePath){
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern(conversionPattern);//"[TRSAPPLOG] %d - %-5p %x - %m %n"
		FileAppender appender = null;
		try
		{
			//把输出端配置到out.txt
			appender = new FileAppender(layout, filePath, false);//"d://szxxdbl" + CMyDateTime.now().toString("yyyyMMddhhmm") + ".log"
		}catch(Exception e){
			e.printStackTrace();
		}
		return appender;
	}
}
