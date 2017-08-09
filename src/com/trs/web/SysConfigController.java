/**   
* Description: TODO 
* Title: SysConfigController.java 
* @Package com.trs.web 
* @author jin.yu 
* @date 2014-3-26 下午04:26:35 
* @version V1.0   
*/
package com.trs.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.dbhibernate.Page;
import com.trs.model.AppDatastatus;
import com.trs.model.AppFlowNode;
import com.trs.model.AppLog;
import com.trs.model.AppMsgTemp;
import com.trs.model.AppOper;
import com.trs.model.AppSysConfig;
import com.trs.model.AppUser;
import com.trs.service.AppLogService;
import com.trs.service.AppSysConfigService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.Global;
import com.trs.util.jsonUtil;

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
@RequestMapping("/sysconfig.do")
public class SysConfigController {
	private static  Logger loger =  Logger.getLogger(SysConfigController.class);
	@Autowired
	private AppSysConfigService appSysConfigService;
	@Autowired
	private AppLogService appLogService;
	
	/**
	 * 
	* Description:  根据前台条件查询所有的系统配置信息<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:32:13
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=listSysConfig")
	public String listSysConfig(HttpServletRequest request,HttpServletResponse res){
		String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
		String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
		Page page = null;
		String sWhere = "";
		List<Object> param = null;
		if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
			   sWhere = selectFiled + " like ?";
			   param = new ArrayList<Object>();
			   param.add("%"+sFiledValue+"%");
		}
		try {
			page = appSysConfigService.findPage("", AppSysConfig.class.getName(), sWhere, "crtime desc", 
					Integer.valueOf(currPage),Integer.valueOf(pageSize), param);
		} catch (NumberFormatException e) {
			loger.error("获取系统配置信息列表失败。", e);
			e.printStackTrace();
		} catch (Exception e) {
			loger.error("获取系统配置信息列表失败。", e);
			e.printStackTrace();
		}//查询所有的系统配置
		request.setAttribute("configList",page.getLdata());
		request.setAttribute("page",page);
		request.setAttribute("searchValue",sFiledValue);
		request.setAttribute("searchKey",selectFiled);
		return "appadmin/sysconfig/sysconfig_list";
	}
	
	/**
	 * 
	* Description:新增一个系统配置 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:51
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=addSysConfig")
	@ResponseBody
	public String addSysConfig(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String configName = request.getParameter("configname"); //系统配置名称
		String configValue = request.getParameter("configvalue"); //系统配置内容
		String configDesc = request.getParameter("configdesc"); //系统配置描述
		AppSysConfig appSysConfig = new AppSysConfig();//创建系统配置对象
		appSysConfig.setConfigName(configName);
		appSysConfig.setConfigValue(configValue);
		appSysConfig.setConfigDesc(configDesc);
		appSysConfig.setCruser(appUser.getUsername());
		String sJson = "";
		try {
			appSysConfigService.addAppSysConfig(appSysConfig);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_SAVE, "新增系统配置", appUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("新增系统配置操作失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	/**
	 * 
	* Description:删除选择的系统配置<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:44
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=delSysConfig")
	@ResponseBody
	public String delSysConfig(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String sconfigIds = request.getParameter("configIDs");
		String sJson = "";
		try {
			appSysConfigService.deleteAll(AppSysConfig.class.getName(), "configId", sconfigIds); //删除系统配置信息
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
			appLogService.addAppLog(Global.LOGS_DEL, "删除系统配置", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			loger.error("删除系统配置操作失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	
	/**
	 * 
	* Description:修改一个系统配置<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:37
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=upSysConfig")
	@ResponseBody
	public String upSysConfig(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String configID = request.getParameter("configID");//系统配置ID
		String configName = request.getParameter("configname"); //系统配置名称
		String configValue = request.getParameter("configvalue"); //系统配置内容
		String configDesc = request.getParameter("configdesc"); //系统配置描述
		AppSysConfig appSysConfig = new AppSysConfig();//创建系统配置对象
		appSysConfig = (AppSysConfig) appSysConfigService.findById(AppSysConfig.class,Long.valueOf(configID));
		appSysConfig.setConfigName(configName);
		appSysConfig.setConfigValue(configValue);
		appSysConfig.setConfigDesc(configDesc);
		String sJson = "";
		try {
			appSysConfigService.updateAppSysConfig(appSysConfig);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改系统配置", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("修改系统配置操作失败。", e);
			e.printStackTrace();
		}  //更新系统配置的相关信息
		return sJson;
	}
	
	/**
	 * 
	* Description: 编辑系统配置信息，根据传入的系统配置ID来判断是需要更新还是新建 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午11:48:15
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=editSysConfig")
	public String editSysConfig(HttpServletRequest request,HttpServletResponse res){
		String configID =  
		CMyString.showEmpty(request.getParameter("configID"),"0");//系统配置ID
		AppSysConfig appSysConfig = null;
		
		if("0".equals(configID)){
			request.setAttribute("method", "addSysConfig");
		}else{
			appSysConfig = (AppSysConfig) appSysConfigService.findById(AppSysConfig.class,Long.valueOf(configID));//根据id获取角色
			request.setAttribute("method", "upSysConfig");
		}
		request.setAttribute("sysconfig", appSysConfig);
		return "appadmin/sysconfig/sysconfig_edit";
	}
	
	/**
	 * 
	* Description: 判断系统中系统配置是否已存在 <BR>   
	* @author jin.yu
	* @date 2014-3-27 上午09:46:31
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=checkSysConfig")
	@ResponseBody
	public String checkSysConfig(HttpServletRequest request,HttpServletResponse res){
		String configName = request.getParameter("configname"); //系统配置名称
		boolean isExite = true;
		try {
			isExite = appSysConfigService.existSysConfigCon(configName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("校验系统配置信息唯一性失败。", e);
			e.printStackTrace();
		}
		return isExite+"";
	}
	
	/**
	 * 
	* Description:  根据前台条件查询所有的应用操作信息<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:32:13
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=listOper")
	public String listOper(HttpServletRequest request,HttpServletResponse res){
		String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
		String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
		Page page = null;
		String sWhere = "";
		List<Object> param = null;
		if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
			   sWhere = selectFiled + " like ?";
			   param = new ArrayList<Object>();
			   param.add("%"+sFiledValue+"%");
		}
		try {
			page = appSysConfigService.findPage("", AppOper.class.getName(), sWhere, "operFlag asc", 
					Integer.valueOf(currPage),Integer.valueOf(pageSize), param);
		} catch (NumberFormatException e) {
			loger.error("获取应用操作信息列表失败。", e);
			e.printStackTrace();
		} catch (Exception e) {
			loger.error("获取应用操作信息列表失败。", e);
			e.printStackTrace();
		}//查询所有的系统配置
		request.setAttribute("operList",page.getLdata());
		request.setAttribute("page",page);
		request.setAttribute("searchValue",sFiledValue);
		request.setAttribute("searchKey",selectFiled);
		return "appadmin/sysconfig/oper_list";
	}
	
	/**
	 * 
	* Description:新增一个应用操作 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:51
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=addOper")
	@ResponseBody
	public String addOper(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String opname = request.getParameter("opname"); //应用操作名称
		String opdesc = request.getParameter("opdesc"); //应用操作描述
		String operFlag = request.getParameter("operFlag"); //应用操作标识
		AppOper appOper = new AppOper();//创建应用操作对象
		appOper.setOpname(opname);
		appOper.setOpdesc(opdesc);
		appOper.setOperFlag(operFlag);
		appOper.setCruser(appUser.getUsername());
		String sJson = "";
		try {
			appSysConfigService.addAppOper(appOper);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_SAVE, "新增应用操作", appUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("新增应用操作操作失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	/**
	 * 
	* Description:删除选择的应用操作<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:44
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=delOper")
	@ResponseBody
	public String delOper(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String soperIDs = request.getParameter("operIDs");
		String sJson = "";
		try {
			appSysConfigService.deleteAll(AppOper.class.getName(), "operid", soperIDs); //删除应用操作信息
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
			appLogService.addAppLog(Global.LOGS_DEL, "删除应用操作", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			loger.error("删除应用操作操作失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	
	/**
	 * 
	* Description:修改一个应用操作<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:37
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=upOper")
	@ResponseBody
	public String upOper(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String operID = request.getParameter("operID");//系统配置ID
		String opname = request.getParameter("opname"); //应用操作名称
		String opdesc = request.getParameter("opdesc"); //应用操作描述
		String operFlag = request.getParameter("operFlag"); //应用操作标识
		AppOper appOper = new AppOper();//创建应用操作对象
		appOper = (AppOper) appSysConfigService.findById(AppOper.class,Long.valueOf(operID));
		appOper.setOpname(opname);
		appOper.setOpdesc(opdesc);
		appOper.setOperFlag(operFlag);
		String sJson = "";
		try {
			appSysConfigService.updateAppOper(appOper);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改应用配置", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("修改应用操作操作失败。", e);
			e.printStackTrace();
		}  //更新应用操作的相关信息
		return sJson;
	}
	
	/**
	 * 
	* Description: 编辑应用操作信息，根据传入的应用操作ID来判断是需要更新还是新建 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午11:48:15
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=editOper")
	public String editOper(HttpServletRequest request,HttpServletResponse res){
		String operID =  
		CMyString.showEmpty(request.getParameter("operID"),"0");//应用操作ID
		AppOper appOper = null;
		
		if("0".equals(operID)){
			request.setAttribute("method", "addOper");
		}else{
			appOper = (AppOper) appSysConfigService.findById(AppOper.class,Long.valueOf(operID));//根据id获取应用操作
			request.setAttribute("method", "upOper");
		}
		request.setAttribute("oper", appOper);
		return "appadmin/sysconfig/oper_edit";
	}
	
	/**
	 * 
	* Description: 判断系统中应用操作是否已存在 <BR>   
	* @author jin.yu
	* @date 2014-3-27 上午09:46:31
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=checkOper")
	@ResponseBody
	public String checkOper(HttpServletRequest request,HttpServletResponse res){
		String operFlag = request.getParameter("operFlag"); //应用操作标识
		boolean isExite = true;
		try {
			isExite = appSysConfigService.existOper(operFlag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("校验应用操作信息唯一性失败。", e);
			e.printStackTrace();
		}
		
		return isExite+"";
	}
	
	/**
	 * 
	* Description:  根据前台条件查询所有的节点状态信息<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:32:13
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=listDataStatus")
	public String listDataStatus(HttpServletRequest request,HttpServletResponse res){
		String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
		String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
		Page page = null;
		String sWhere = "";
		List<Object> param = null;
		if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
			   sWhere = selectFiled + " like ?";
			   param = new ArrayList<Object>();
			   param.add("%"+sFiledValue+"%");
		}
		try {
			page = appSysConfigService.findPage("", AppDatastatus.class.getName(), sWhere, "type asc", 
					Integer.valueOf(currPage),Integer.valueOf(pageSize), param);
		} catch (NumberFormatException e) {
			loger.error("获取节点状态信息列表失败。", e);
			e.printStackTrace();
		} catch (Exception e) {
			loger.error("获取节点状态信息列表失败。", e);
			e.printStackTrace();
		}//查询所有的节点状态
		request.setAttribute("dataStatusList",page.getLdata());
		request.setAttribute("page",page);
		request.setAttribute("searchValue",sFiledValue);
		request.setAttribute("searchKey",selectFiled);
		return "appadmin/sysconfig/datastatus_list";
	}
	
	/**
	 * 
	* Description:新增一个节点状态 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:51
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=addDataStatus")
	@ResponseBody
	public String addDataStatus(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String statusname = request.getParameter("statusname"); //节点状态名称
		String statusdesc = request.getParameter("statusdesc"); //节点状态描述
		AppDatastatus appDatastatus = new AppDatastatus();//创建节点状态对象
		appDatastatus.setStatusname(statusname);
		appDatastatus.setStatusdesc(statusdesc);
		appDatastatus.setCruser(appUser.getUsername());
		String sJson = "";
		try {
			appSysConfigService.addAppDataStatus(appDatastatus);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_SAVE, "新增工作流节点状态", appUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("新增节点状态操作失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	
	/**
	 * 
	* Description:删除选择的节点状态<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:44
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=delDataStatus")
	@ResponseBody
	public String delDataStatus(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String dataStatusIDs = request.getParameter("dataStatusIDs");
		String sJson = "";
		try {
			appSysConfigService.deleteAll(AppDatastatus.class.getName(), "datastatusId", dataStatusIDs); //删除节点状态信息
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
			appLogService.addAppLog(Global.LOGS_DEL, "删除工作流节点状态", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			loger.error("删除节点状态操作失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	
	/**
	 * 
	* Description:修改一个节点状态<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:37
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=upDataStatus")
	@ResponseBody
	public String upDataStatus(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String dataStatusID = request.getParameter("dataStatusID");//系统配置ID
		String statusname = request.getParameter("statusname"); //节点状态名称
		String statusdesc = request.getParameter("statusdesc"); //节点状态描述
		AppDatastatus appDatastatus = new AppDatastatus();//创建应用操作对象
		appDatastatus = (AppDatastatus) appSysConfigService.findById(AppDatastatus.class,Long.valueOf(dataStatusID));
		appDatastatus.setStatusname(statusname);
		appDatastatus.setStatusdesc(statusdesc);
		String sJson = "";
		try {
			appSysConfigService.updateAppDataStatus(appDatastatus);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改工作流节点状态", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("修改节点状态操作失败。", e);
			e.printStackTrace();
		}  //更新应用操作的相关信息
		return sJson;
	}
	
	/**
	 * 
	* Description: 编辑节点状态信息，根据传入的节点状态ID来判断是需要更新还是新建 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午11:48:15
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=editDataStatus")
	public String editDataStatus(HttpServletRequest request,HttpServletResponse res){
		String dataStatusID =  
		CMyString.showEmpty(request.getParameter("dataStatusID"),"0");//节点状态ID
		AppDatastatus appDatastatus = null;
		
		if("0".equals(dataStatusID)){
			request.setAttribute("method", "addDataStatus");
		}else{
			appDatastatus = (AppDatastatus) appSysConfigService.findById(AppDatastatus.class,Long.valueOf(dataStatusID));//根据id获取节点状态
			request.setAttribute("method", "upDataStatus");
		}
		request.setAttribute("dataStatus", appDatastatus);
		return "appadmin/sysconfig/datastatus_edit";
	}
	
	/**
	 * 
	* Description: 判断系统中节点状态是否已存在 <BR>   
	* @author jin.yu
	* @date 2014-3-27 上午09:46:31
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=checkDataStatus")
	@ResponseBody
	public String checkDataStatus(HttpServletRequest request,HttpServletResponse res){
		String statusname = request.getParameter("statusname"); //节点状态标识
		boolean isExite = true;
		try {
			isExite = appSysConfigService.existDataStatus(statusname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("校验节点状态信息唯一性失败。", e);
			e.printStackTrace();
		}

		return isExite+"";
	}
	
	/**
	 * 
	* Description:  根据前台条件查询所有的信息提醒模板<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:32:13
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=listMsgTemp")
	public String listMsgTemp(HttpServletRequest request,HttpServletResponse res){
		String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
		String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
		Page page = null;
		String sWhere = "";
		List<Object> param = null;
		if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
			   sWhere = selectFiled + " like ?";
			   param = new ArrayList<Object>();
			   param.add("%"+sFiledValue+"%");
		}
		try {
			page = appSysConfigService.findPage("", AppMsgTemp.class.getName(), sWhere, "crtime desc", 
					Integer.valueOf(currPage),Integer.valueOf(pageSize), param);
		} catch (NumberFormatException e) {
			loger.error("获取信息提醒模板信息列表失败。", e);
			e.printStackTrace();
		} catch (Exception e) {
			loger.error("获取信息提醒模板信息列表失败。", e);
			e.printStackTrace();
		}//查询所有的系统配置
		request.setAttribute("msgTempList",page.getLdata());
		request.setAttribute("page",page);
		request.setAttribute("searchValue",sFiledValue);
		request.setAttribute("searchKey",selectFiled);
		return "appadmin/sysconfig/msgtemp_list";
	}
	
	/**
	 * 
	* Description:新增一个信息提醒模板<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:51
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=addMsgTemp")
	@ResponseBody
	public String addMsgTemp(HttpServletRequest request,HttpServletResponse res){
		//从session中取得登录用户
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String remindType = request.getParameter("remindType"); //提醒分类
		String tempTitle = request.getParameter("tempTitle"); //模板主题
		String tempContent = request.getParameter("tempContent"); //模板内容
		String tempType = request.getParameter("tempType"); //模板类型
		//String appId = request.getParameter("appId"); //应用ID
		AppMsgTemp appMsgTemp = new AppMsgTemp();//创建信息提醒模板对象
		appMsgTemp.setRemindType(Integer.valueOf(remindType));
		appMsgTemp.setTempTitle(tempTitle);
		appMsgTemp.setTempContent(tempContent);
		appMsgTemp.setTempType(Integer.valueOf(tempType));
		//appMsgTemp.setAppId(Long.valueOf(appId));
		appMsgTemp.setCruser(appUser.getUsername());
		String sJson = "";
		try {
			//保存信息提醒模板
			appSysConfigService.addMsgTemp(appMsgTemp);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_SAVE, "新增信息提醒模板", appUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("新增信息提醒模板操作失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	
	/**
	 * 
	* Description:修改一个信息提醒模板<BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:34:37
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=upMsgTemp")
	@ResponseBody
	public String upMsgTemp(HttpServletRequest request,HttpServletResponse res){
		//从session中取得登录用户
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String msgTempID = request.getParameter("msgTempID");//系统配置ID
		String remindType = request.getParameter("remindType"); //提醒分类
		String tempTitle = request.getParameter("tempTitle"); //模板主题
		String tempContent = request.getParameter("tempContent"); //模板内容
		String tempType = request.getParameter("tempType"); //模板类型
		//String appId = request.getParameter("appId"); //应用ID
		AppMsgTemp appMsgTemp = new AppMsgTemp();//创建信息提醒模板对象
		appMsgTemp = (AppMsgTemp) appSysConfigService.findById(AppMsgTemp.class,Long.valueOf(msgTempID));
		appMsgTemp.setRemindType(Integer.valueOf(remindType));
		appMsgTemp.setTempTitle(tempTitle);
		appMsgTemp.setTempContent(tempContent);
		appMsgTemp.setTempType(Integer.valueOf(tempType));
		//appMsgTemp.setAppId(Long.valueOf(appId));
		String sJson = "";
		try {
			appSysConfigService.updateMsgTemp(appMsgTemp);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改信息提醒模板", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("修改信息提醒模板操作失败。", e);
			e.printStackTrace();
		}  //更新信息提醒模板的相关信息
		return sJson;
	}
	
	/**
	 * 
	* Description: 删除选择的提醒模板信息 <BR>   
	* @author jin.yu
	* @date 2014-4-2 下午02:22:41
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=delMsgTemp")
	@ResponseBody
	public String delMsgTemp(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
		String msgTempIDs = request.getParameter("msgTempIDs");
		String sJson = "";
		try {
			appSysConfigService.deleteAll(AppMsgTemp.class.getName(), "tempId", msgTempIDs); //删除提醒模板信息
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
			appLogService.addAppLog(Global.LOGS_DEL, "删除提醒模板信息", appUser);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			loger.error("删除信息提醒模板操作失败。", e);
			e.printStackTrace();
		}
		appLogService.addAppLog(Global.LOGS_DEL, "删除提醒模板信息", appUser);
		return sJson;
	}
	
	/**
	 * 
	* Description: 编辑提醒模板信息，根据传入的提醒模板ID来判断是需要更新还是新建 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午11:48:15
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=editMsgTemp")
	public String editMsgTemp(HttpServletRequest request,HttpServletResponse res){
		String msgTempID =  
		CMyString.showEmpty(request.getParameter("msgTempID"),"0");//提醒模板ID
		AppMsgTemp appMsgTemp = null;
		
		if("0".equals(msgTempID)){
			request.setAttribute("method", "addMsgTemp");
		}else{
			appMsgTemp = (AppMsgTemp) appSysConfigService.findById(AppMsgTemp.class,Long.valueOf(msgTempID));//根据id获取提醒模板
			request.setAttribute("method", "upMsgTemp");
		}
		request.setAttribute("msgTemp", appMsgTemp);
		return "appadmin/sysconfig/msgtemp_edit";
	}
	
	/**
	 * 
	* Description: 判断提醒模板信息是否已存在 <BR>   
	* @author jin.yu
	* @date 2014-4-3 上午11:09:18
	* @param request
	* @param res
	* @return String true为已存在，false为不存在
	* @version 1.0
	 */
	 @RequestMapping(params = "method=checkMsgTemp")
	 @ResponseBody
	 public String checkMsgTemp(HttpServletRequest request,HttpServletResponse res){
		 String remindType = CMyString.showEmpty(request.getParameter("remindType"),"");
		 String tempType = CMyString.showEmpty(request.getParameter("tempType"),"");
		 boolean isExite = true;
		 try {
			isExite = appSysConfigService.existMsgTemp(Integer.valueOf(remindType), Integer.valueOf(tempType));
		} catch (Exception e) {
			loger.error("校验信息提醒模板信息唯一性失败。", e);
			e.printStackTrace();
		}
		return isExite+"";
	 }
	 
		/**
		 * 
		* Description:  根据前台条件查询所有的日志信息<BR>   
		* @author jin.yu
		* @date 2014-3-26 下午04:32:13
		* @param request
		* @param res
		* @return String
		* @version 1.0
		 */
		@RequestMapping(params = "method=listLog")
		public String listLog(HttpServletRequest request,HttpServletResponse res){
			String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
			String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
			String selectlogType = CMyString.showEmpty(request.getParameter("selectlogType"),"");	//检索日志类型
			String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
			String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
			Page page = null;
			String sWhere = "";
			List<Object> param = new ArrayList<Object>();
			if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
				   sWhere = selectFiled + " like ?";
				   param.add("%"+sFiledValue+"%");
			}
			if(!CMyString.isEmpty(selectlogType) && !CMyString.isEmpty(selectlogType)){
					if (!"".equals(sWhere)) {
						sWhere = sWhere + " and ";
					}
				   sWhere = sWhere + " logtype = ?";
				   param.add(selectlogType);
			}
			try {
				page = appSysConfigService.findPage("", AppLog.class.getName(), sWhere, "crtime desc", 
						Integer.valueOf(currPage),Integer.valueOf(pageSize), param);
			} catch (NumberFormatException e) {
				loger.error("获取操作日志信息列表失败。", e);
				e.printStackTrace();
			} catch (Exception e) {
				loger.error("获取操作日志信息列表失败。", e);
				e.printStackTrace();
			}//查询所有的系统配置
			request.setAttribute("logList",page.getLdata());
			request.setAttribute("page",page);
			request.setAttribute("searchValue",sFiledValue);
			request.setAttribute("searchKey",selectFiled);
			request.setAttribute("selectlogType",selectlogType);
			return "appadmin/sysconfig/log_list";
		}
		
		/**
		 * 
		* Description: 删除选择的日志信息 <BR>   
		* @author jin.yu
		* @date 2014-4-4 上午10:23:37
		* @param request
		* @param res
		* @return
		* @version 1.0
		 */
		@RequestMapping(params = "method=delLog")
		@ResponseBody
		public String delLog(HttpServletRequest request,HttpServletResponse res){
			AppUser appUser = (AppUser)CrtlUtil.getCurrentUser(request);
			String sLogIDs = request.getParameter("logIDs");
			String sJson = "";
			try {
				appSysConfigService.deleteAll(AppLog.class.getName(), "logid", sLogIDs); //删除系统配置信息
				sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
			} catch (Exception e) {
				loger.error("删除操作日志操作失败。", e);
				sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
				e.printStackTrace();
			}
			return sJson;
		}
		/**
		 * 
		* Description:获取当年节假日安排 <BR>   
		* @author zhangzun
		* @date 2014-4-8 下午11:08:44
		* @param request
		* @param res
		* @return
		* @version 1.0
		 */
		@RequestMapping(params = "method=holiadayPage")
		public String holiadayPage(HttpServletRequest request,HttpServletResponse res){
			String result = "";
			int year = Calendar.getInstance().get(Calendar.YEAR);
			try {
				result = appSysConfigService.findWday(year);
			}catch (Exception e) {
				e.printStackTrace();
				result="false";
			}
			request.setAttribute("year", year);
			request.setAttribute("result", result);
			return "appadmin/sysconfig/holiaday_set";
		}
		/**
		 * 
		* Description:查询一年的节假日安排 <BR>   
		* @author zhangzun
		* @date 2014-4-8 下午11:08:44
		* @param request
		* @param res
		* @return
		* @version 1.0
		 */
		@RequestMapping(params = "method=findWday")
		@ResponseBody
		public String findWday(HttpServletRequest request,HttpServletResponse res){
			String result = "";
			String year = request.getParameter("year");
			try {
				result = appSysConfigService.findWday(Integer.valueOf(year));
			}catch (Exception e) {
				e.printStackTrace();
				result="false";
			}
			
			return result;
		}
		/**
		 * 
		* Description:设置一年的节假日安排 <BR>   
		* @author zhangzun
		* @date 2014-4-8 下午11:08:13
		* @param request
		* @param res
		* @return
		* @version 1.0
		 */
		@RequestMapping(params = "method=saveWday")
		@ResponseBody
		public String saveWday(HttpServletRequest request,HttpServletResponse res){
			String result = "true";
			String dates = request.getParameter("dates");
			String year = request.getParameter("year");
			try {
				appSysConfigService.saveWday(dates, Integer.valueOf(year));
				//appLogService.addAppLog(Global.LOGS_SAVE, "新增信息提醒模板", appUser);
			}catch (Exception e) {
				loger.error("保存节假日安排操作失败。", e);
				e.printStackTrace();
				result="false";
			}
			
			return result;
		}
}
