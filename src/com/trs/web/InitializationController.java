/**   
* Description: TODO 
* Title: SysConfigController.java 
* @Package com.trs.web 
* @author jin.yu 
* @date 2014-3-26 下午04:26:35 
* @version V1.0   
*/
package com.trs.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.trs.service.AppSysConfigService;
import com.trs.service.AppUserService;
import com.trs.util.Global;
/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: SysConfigController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-26 下午04:26:35
 * @version 1.0
 */
@Controller
@RequestMapping("/init.do")
public class InitializationController {
	private static  Logger loger =  Logger.getLogger(InitializationController.class);
	@Autowired
	private AppSysConfigService appSysConfigService;
	@Autowired
	private AppUserService appUserService;
	@RequestMapping(params = "method=initialization")
	public String initialization(HttpServletRequest request,HttpServletResponse res){
			try {
				if(appUserService.existUser("admin")){
					return "redirect:/login.do?method=loginUser";
				}
				StringBuffer path =  new StringBuffer(request.getRealPath("/")).append("WEB-INF\\init\\");
				String type= Global.DB_TYPE;
				List<String> sqlList = null;
				if(type.equals("oracle")){
					path.append("oracle.txt");
					sqlList = initloadSql(path.toString());
					appSysConfigService.saveBaseSql(sqlList);
				}
				if(type.equals("sqlserver")){
					path.append("sqlserver.txt");
					sqlList = initloadSql(path.toString());
					appSysConfigService.saveBaseSql(sqlList);
				}
				if(type.equals("mysql")){
					path.append("mysql.txt");
					sqlList = initloadSql(path.toString());
					appSysConfigService.saveBaseSql(sqlList);
				}
			} catch (SQLException e) {
				loger.error("系统初始化错误"+e);
			} catch (IOException e) {
				loger.error("系统初始化错误"+e);
			} catch (Exception e) {
				loger.error("系统初始化错误"+e);;
			}
		return null;
	}
	/**
	* Description:返回对应地址的数据库初始化脚本<BR>  
	* @author wen.junhui
	* @date Create: 2014-10-13 下午01:50:46
	* Last Modified:
	* @param sqlFile sql 文件地址
	* @return List<String> 
	* @throws Exception
	* @version 1.0
	 */
	public List<String> initloadSql(String sqlFile) throws Exception {
		List<String> sqlList = new ArrayList<String>();
		FileInputStream fis=new FileInputStream(sqlFile);   
		InputStreamReader isr =null;
		BufferedReader br = null;
        try {     
             isr=new InputStreamReader(fis,"UTF-8");                     
             br =new BufferedReader(isr);     
            String line;     
            while((line=br.readLine()) != null){     
            	sqlList.add(line);     
            }     
        } catch (Exception e) {     
            // TODO Auto-generated catch block     
            e.printStackTrace();     
        }finally{
        	isr.close();
        	br.close();
        	fis.close();
        }
		return sqlList;                               
	} 
	
}
