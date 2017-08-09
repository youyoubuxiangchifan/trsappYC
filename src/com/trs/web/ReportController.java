/**   
* Description: TODO 
* Title: SysConfigController.java 
* @Package com.trs.web 
* @author jin.yu 
* @date 2014-3-26 下午04:26:35 
* @version V1.0   
*/
package com.trs.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trs.service.AppReportService;
import com.trs.service.AppSysConfigService;
import com.trs.util.Global;
/**
 * Description: 统计分析报表生成<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: SysConfigController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-26 下午04:26:35
 * @version 1.0
 */
@Controller
@RequestMapping("/report.do")
public class ReportController {
	private static  Logger loger =  Logger.getLogger(ReportController.class);
	@Autowired
	private AppReportService appReportService;
	
	
	@RequestMapping(params = "method=reportty")
	public String reportty(HttpServletRequest request,HttpServletResponse res){
		String type= Global.DB_TYPE;
		if(type.equals("oracle")){
			try {
				List<Object> results = new ArrayList<Object>();
				List<Object> tableNames  = appReportService.queryTableName();
				request.setAttribute("tableNames", tableNames);
				for(Object tabs : tableNames){
					HashMap<String, String> tableName = (HashMap<String, String>) tabs;
					List<Object> resu = 	appReportService.queryGroupOralce(tableName.get("MAIN_TABLE_NAME"),Long.parseLong(tableName.get("APP_ID")));
					results.add(resu);
				}
			request.setAttribute("results",results);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "appadmin/report/reportty_list";
	}
	/**
	 * 按用户统计
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-2 下午05:38:15
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=reportus")
	public String reportus(HttpServletRequest request,HttpServletResponse res){
		String type= Global.DB_TYPE;
		if(type.equals("oracle")){
			try {
		List<Object> results = new ArrayList<Object>();
				List<Object> tableNames  = appReportService.queryTableName();
				for(Object tabs : tableNames){
					HashMap<String, String> tableName = (HashMap<String, String>) tabs;
					try {
						List<Object> resu = appReportService.queryUsOracle(tableName.get("MAIN_TABLE_NAME"),Long.parseLong(tableName.get("APP_ID")));
						results.add(resu);
					} catch (SQLException e) {
						loger.info("当前主题表不支持按用户统计"+e);
					} 
				}
			request.setAttribute("results",results);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "appadmin/report/reportus_list";
	}
	/**
	 * 按机构统计
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-2 下午05:38:02
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=reportgrp")
	public String findSqlReport(HttpServletRequest request,HttpServletResponse res){
		String type= Global.DB_TYPE;
		if(type.equals("oracle")){
			try {
		List<Object> results = new ArrayList<Object>();
				List<Object> tableNames  = appReportService.queryTableName();
				request.setAttribute("tableNames", tableNames);
				for(Object tabs : tableNames){
					HashMap<String, String> tableName = (HashMap<String, String>) tabs;
					List<Object> resu  = appReportService.queryGroupOralce(tableName.get("MAIN_TABLE_NAME"),Long.parseLong(tableName.get("APP_ID")));
					results.add(resu);
				}
			request.setAttribute("results",results);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "appadmin/report/reportGroup_list";
	}
	
}
