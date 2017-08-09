/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: appMgrController
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-27 下午02:38:46
 * @version 1.0
 */
package com.trs.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.dbhibernate.Page;
import com.trs.model.AppFieldRel;
import com.trs.model.AppFlow;
import com.trs.model.AppFlowDoc;
import com.trs.model.AppGroup;
import com.trs.model.AppGrpuser;
import com.trs.model.AppInfo;
import com.trs.model.AppRecordLock;
import com.trs.model.AppUser;
import com.trs.model.AppViewInfo;
import com.trs.model.FlowGroupAndUsers;
import com.trs.model.TreeNode;
import com.trs.service.AppFlowService;
import com.trs.service.AppLogService;
import com.trs.service.AppUserService;
import com.trs.service.PublicAppBaseService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.DateUtil;
import com.trs.util.Global;

/**
 * Description: 应用信息办理后台controller<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: appMgrController
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-3-27 下午02:38:46
 * version 1.0
 */
@Controller
@RequestMapping("appMgr.do")
public class AppMgrController {
	private static  Logger LOG =  Logger.getLogger(AppMgrController.class);
	@Autowired
	private PublicAppBaseService publicAppBaseService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppFlowService appFlowService;
	@Autowired
	private AppLogService appLogService;
	/** 
	* Description: 获取应用列表信息 <BR>   
	* author liujian
	* date 2014-3-27 下午04:31:10
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=getAppTreeJson")
	@ResponseBody
	public String getAppTreeJson(HttpServletRequest request,HttpServletResponse res){
		//AppUser appUser = (AppUser)request.getSession().getAttribute("loginUser");
		//TODO test
		//appUser.setUserId(74l);

		String appId = request.getParameter("id");//appID应用ID
		String url = request.getParameter("url");
		String target = request.getParameter("target");
		String rel = request.getParameter("rel");
		String appTreeJson ="[]";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");

		List<TreeNode> list = new ArrayList<TreeNode>();
		List<Object> idsList = new ArrayList<Object>();

		if(CMyString.isEmpty(appId)){//在视图表中查出所有视图类型，初始化第一层树结构
			try {
				String viewIds = publicAppBaseService.getAppInfoFields(loginUser.getUserId(), "viewId",0l);
				if(CMyString.isEmpty(viewIds)){
					return appTreeJson;
				}
				idsList = publicAppBaseService.getAppViewInfos(viewIds);
				for(int i=0;i<idsList.size();i++){
					TreeNode treeNode = new TreeNode();
					AppViewInfo appInfo = (AppViewInfo)idsList.get(i);
					
					treeNode.setId(appInfo.getViewId().toString());
					treeNode.setPId("-10");
					treeNode.setName(CMyString.native2Ascii(appInfo.getViewName()));
					//treeNode.setUrl("group.do?method=listGroup&groupIds="+appInfo.getViewId().toString());
					treeNode.setUrl("javascript:;");
					treeNode.setIsParent(true);
					treeNode.setTarget("_self");
					//treeNode.setRel(rel);
					list.add(treeNode);
				}
				appTreeJson = TreeNode.getTreeJsonStr(list);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else{//根据接收的视图ID取出同样视图的应用
			try {
				idsList = publicAppBaseService.getViewToAppInfo(loginUser.getUserId(), Long.valueOf(appId));
				for(int i=0;i<idsList.size();i++){
					TreeNode treeNode = new TreeNode();
					AppInfo appInfo = (AppInfo)idsList.get(i);
					
					treeNode.setId(appInfo.getAppId().toString());
					treeNode.setPId(appId);
					treeNode.setName(CMyString.native2Ascii(appInfo.getAppName()));
					treeNode.setUrl(request.getContextPath()+"/appMgr.do?method=appDataList&appId="+appInfo.getAppId().toString());
					treeNode.setIsParent(false);
					treeNode.setTarget("navTab");
					//treeNode.setRel(rel);
					list.add(treeNode);
				}
				appTreeJson = TreeNode.getTreeJsonStr(list);
			} catch (Exception e) {
				
				e.printStackTrace();
			}	
			
		}
		
		return appTreeJson;
	}
	/** 
	* Description: 获取应用数据列表 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=appDataList")
	public String getAppDataList(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");//应用ID--必传字段
		//String themeId = request.getParameter("themeId");//主题ID--根据主表中的主题Id查询相关从表数据
		String status = CMyString.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");//数据状态 0:待处理，1处理中，2已处理
		String themeStatus = CMyString.isEmpty(request.getParameter("themeStatus"))?"1":request.getParameter("themeStatus");;//主题时候进行中标识：1-进行中， 0-主题已结束
		String deleted = CMyString.isEmpty(request.getParameter("deleted"))?"0":request.getParameter("deleted");//数据状态 0:未删除，1-已删除
		String selectFiled = request.getParameter("selectFiled");	//检索字段
		String sFiledValue = request.getParameter("sFiledValue");	//检索字段值
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");//当前页数
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"20":request.getParameter("numPerPage");//每页显示条数
		String orderField = CMyString.isEmpty(request.getParameter("orderField"))?"":request.getParameter("orderField");//排序字段
		String orderDirection = CMyString.isEmpty(request.getParameter("orderDirection"))?"desc":request.getParameter("orderDirection");//排序字段顺序标识
		String tableType = CMyString.isEmpty(request.getParameter("tableType"))?"0":request.getParameter("tableType");//表类型：0-主表，1-回帖表 ***数据相关必传字段
		
		String worked = CMyString.isEmpty(request.getParameter("worked"))?"0":request.getParameter("worked");//工作流表当前用户数据处理状态，0-未处理，1-已处理
		String isOwnerWork = CMyString.isEmpty(request.getParameter("IsOwnerWork"))?"0":request.getParameter("IsOwnerWork");//工作流表是否被当前用户数据处理过，0-不是，1-是
		String listAddr = "/appadmin/appMgr/appData_List";
		StringBuffer sWhere = new StringBuffer("1 = 1");//拼接检索条件
		String sOrder="am.crtime desc"; //排序条件
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		List<Object> fieldList = new ArrayList<Object>();
		List<Object> searchFieldList = new ArrayList<Object>();
		List<Object> param = new ArrayList<Object>();
		String tableName = "";
		Page page = null;
		AppInfo appInfo = null;
		if(CMyString.isEmpty(appId)){
			LOG.error("取应用数据列表时没有传入应用ID！");
			return listAddr;
		}
		try {
			appInfo = (AppInfo)publicAppBaseService.findById(AppInfo.class, Long.valueOf(appId));
			if(!CMyString.isEmpty(appInfo.getListAddr())){
				listAddr = appInfo.getListAddr();
			}			
			searchFieldList = publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.valueOf(appId), 2, Integer.parseInt(tableType));//取出检索字段
			fieldList = publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.valueOf(appId), 1, Integer.parseInt(tableType));//取出概览字段
			String fieldListStr = splitListFields(fieldList,"am.METADATAID");//以逗号分隔的字符串,默认无工作流
			
			tableName = publicAppBaseService.getMetaTableName(Long.valueOf(appId), Integer.parseInt(tableType));
			request.setAttribute("tableName",tableName);
			tableName = tableName+" am";
			
			if(appInfo.getFlowId() != null && appInfo.getFlowId()!=0 && "0".equals(deleted) && !"-1".equals(status)){
				tableName = tableName+", APP_FLOW_DOC af";
				fieldListStr = splitListFields(fieldList,"distinct af.Objid,am.METADATAID,af.ACCEPTED");//以逗号分隔的字符串，有工作流
				sWhere.append(" and am.appId = af.app_id and am.metadataid = af.objid and af.worked =? and af.tousers=? and af.is_owner_work=?");
				param.add(worked);
				param.add(loginUser.getUsername());
				param.add(isOwnerWork);
			}else if(appInfo.getFlowId() != null && appInfo.getFlowId()!=0 && "0".equals(deleted) && "-1".equals(status)){
				AppGrpuser grpuser = (AppGrpuser)publicAppBaseService.findObject(AppGrpuser.class.getName(), "userId = ?", loginUser.getUserId());
				long loginUserGroupId = 0;
				if(grpuser != null){//获取登陆用户所在部门，一个用户只能属于一个部门，属于多个部门只取一个。
					loginUserGroupId = grpuser.getGroupId();
				}
				int isAppAdmin = publicAppBaseService.isAppAdminUser(Long.valueOf(appId), loginUser.getUserId());
				boolean isAdmin = (Boolean)request.getSession().getAttribute("isAdmin");
				if(isAppAdmin == 0 && !isAdmin){//非应用管理员只能查看所在部门数据。
					sWhere.append(" and am.submitdept = ?");
					param.add(loginUserGroupId);
				}
			}else if(!"-1".equals(status) && appInfo.getIsNeedTheme() == 1 && "0".equals(deleted)){
				if("1".equals(themeStatus)){
					sWhere.append(" and am.ENDTIME > ?");
					param.add(DateUtil.nowSqlDate());
					request.setAttribute("themeStatus", themeStatus);
				}else if("0".equals(themeStatus)){
					sWhere.append(" and am.ENDTIME <= ?");
					param.add(DateUtil.nowSqlDate());
					request.setAttribute("themeStatus", themeStatus);
				}
			}else if(!"-1".equals(status) && "0".equals(deleted)){
				sWhere.append(" and am.STATUS = ?");
				param.add(status);
			}
			sWhere.append(" and am.appId = ?");
			param.add(appId);
			if(!CMyString.isEmpty(deleted)){
				sWhere.append(" and am.DELETED = ?");
				param.add(deleted);
			}
			if(!CMyString.isEmpty(sFiledValue)){
				sWhere.append(" and am."+selectFiled+" like ?");
				param.add("%"+sFiledValue+"%");
			}

			if(!CMyString.isEmpty(orderField)&&!CMyString.isEmpty(orderDirection)){
				sOrder = "am."+orderField+" "+orderDirection;
			}
			
			page = publicAppBaseService.queryAppInfos(tableName, fieldListStr, sWhere.toString(), param, sOrder, Integer.parseInt(currPage),Integer.parseInt(pageSize));

				
		} catch (NumberFormatException e) {			
			LOG.error("获取应用数据列表时字符转数字类型异常！");
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error("获取应用数据列表异常！");
			e.printStackTrace();
		}
			
		
		request.setAttribute("appId",appId);
		
		request.setAttribute("fieldList",fieldList);
		request.setAttribute("searchFieldList",searchFieldList);
		request.setAttribute("status",status);
		request.setAttribute("worked",worked);
		request.setAttribute("isOwnerWork", isOwnerWork);
		request.setAttribute("deleted",deleted);
		request.setAttribute("appInfo",appInfo);
		
		request.setAttribute("page",page);
		request.setAttribute("dataList", page.getLdata());
		request.setAttribute("selectFiled",selectFiled);
		request.setAttribute("sFiledValue",sFiledValue);
		request.setAttribute("orderField",orderField);
		request.setAttribute("orderDirection",orderDirection);
		
		return listAddr;
	}	
	/** 
	* Description: 页面跳转判断 标识 0-跳转到新建用户页面、1-跳转到用户信息页面、2-跳转到重置密码页面、3-跳转到修改用户页面
	* author liujian
	* date 2014-3-13 下午02:31:04
	* @param request 
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=edditPage")
	public String edditPage(HttpServletRequest request,HttpServletResponse res){
		String returnUrl="appadmin/user/user_add";
		String userIDs = request.getParameter("userIDs");//获取用户ID
		String pageFlag = CMyString.isEmpty(request.getParameter("pageFlag"))?"0":request.getParameter("pageFlag");//获取用户ID
		AppUser appUser = null;
		switch (Integer.parseInt(pageFlag)) {
			case 0: //跳转到重置密码页面
				returnUrl="appadmin/user/webUser_restPwd";
				appUser = (AppUser)appUserService.findById(AppUser.class, Long.valueOf(userIDs));
				request.setAttribute("appUser", appUser);			
				break;			
			case 1: //跳转到前台修改用户页面
				returnUrl="appadmin/user/webUser_eddit";
				appUser = (AppUser)appUserService.findById(AppUser.class, Long.valueOf(userIDs));
				request.setAttribute("appUser", appUser);			
				break;	
			default:
				break;
		}
		
		return returnUrl;
	}
	/** 
	* Description: 登陆用户修改自己密码 <BR>   
	* author liujian
	* date 2014-3-17 下午06:25:10
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=upPassword")
	@ResponseBody
	public String upPassword(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus"); //当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id
		String currPwd = request.getParameter("currPwd");  //用户当前密码
		String newPwd = request.getParameter("newPwd");   //用户新密码
		String idWeakPwd = CMyString.isEmpty(request.getParameter("weakPasswd"))?"0":request.getParameter("weakPasswd");
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("修改密码失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		AppUser appUser=null;
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appUser = (AppUser)appUserService.findById(AppUser.class,Long.valueOf(userIDs));
			boolean restFlag = appUserService.resetPassWord(appUser, newPwd, currPwd,Integer.parseInt(idWeakPwd));
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改用户[id="+userIDs+"]密码", loginUser);
			if(restFlag == true){
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("修改密码成功!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			}else{
				josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("原密码不正确，请核对后重新输入!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			}			
		} catch (NumberFormatException e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改用户[id="+userIDs+"]密码异常", loginUser);
			LOG.error("当前登陆用户修改密码时用户ID类型转换异常！");
			e.printStackTrace();
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改用户[id="+userIDs+"]密码异常", loginUser);
			LOG.error("当前登陆用户修改密码异常！");
			e.printStackTrace();
		}
		request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}
	/** 
	* Description: 修改用户信息 <BR>   
	* author liujian
	* date 2014-3-17 下午06:20:13
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=upUser")
	@ResponseBody
	public String upUser(HttpServletRequest request,HttpServletResponse res){		
		String userStatus = request.getParameter("userStatus"); //当前用户列表状态
		String userIDs = request.getParameter("userIDs");	  //用户id
		String userName = request.getParameter("userName");
		String trueName = request.getParameter("trueName");
		String userPwd = request.getParameter("userPwd");
		String moblie = request.getParameter("moblie");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String zipcode = request.getParameter("zipcode");
		String userType = request.getParameter("userType");
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		AppUser appUser=(AppUser)appUserService.findById(AppUser.class, Long.valueOf(userIDs));
		appUser.setTruename(trueName);
		appUser.setAddress(address);
		appUser.setMoblie(moblie);
		appUser.setEmail(email);
		appUser.setZipcode(zipcode);
		if(!CMyString.isEmpty(userType)){
			appUser.setUsertype(Integer.parseInt(userType));
		}				
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("修改用户信息失败！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		try {
			appUserService.update(appUser);
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改用户[id="+userIDs+"]信息成功", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("修改用户信息成功！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改用户[id="+userIDs+"]信息异常", loginUser);
			LOG.error("用户管理修改用户异常！");
			e.printStackTrace();
		}
		
		
		request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}

	/** 
	* Description: 删除选中的数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=delDatas")
	@ResponseBody
	public String delDatas(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");//应用ID
		String flag = CMyString.isEmpty(request.getParameter("flag"))?"0":request.getParameter("flag");//删除或恢复的标识：1-删除  ，0-恢复
		String tableName = request.getParameter("tableName");//元数据表名
		String dataIds = request.getParameter("dataIds");//数据ID字符串，以","隔开
		String arrIds[] = CMyString.split(dataIds, ",");
		String msg = "删除";
		
		if(!"1".equals(flag)){
			msg = "恢复";
		}
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii(msg+"数据失败！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(CMyString.isEmpty(dataIds)){
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii(msg+"数据失败！没有传入数据ID！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			LOG.error(msg+"数据时没有传入数据ID！");
			return josnStr;
		}
		
		for(int i=0;i<arrIds.length;i++){		
			Map<String, Object> appinfo = new HashMap<String, Object>();
			appinfo.put("METADATAID", arrIds[i]);
			appinfo.put("DELETED", flag);
			list.add(appinfo);
		}
		
		if(!CMyString.isEmpty(tableName)){
			try {
				publicAppBaseService.saveOrUpdateMetadatas(list, tableName);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("成功"+msg+"数据！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			} catch (SQLException e) {
				josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii(msg+"数据失败！"+msg+"数据时出现异常！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
				LOG.error(msg+"数据时异常！");
				e.printStackTrace();
			}
		}else{
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii(msg+"数据失败！没有传入元数据表！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			LOG.error(msg+"数据时没有传入元数据表名！");
		}


		return josnStr;
	}

	/** 
	* Description: 允许公开选中的数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=openDatas")
	@ResponseBody
	public String openDatas(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");//应用ID
		String tableName = request.getParameter("tableName");//元数据表名
		String dataIds = request.getParameter("dataIds");//数据ID字符串，以","隔开
		String arrIds[] = CMyString.split(dataIds, ",");
		String josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("允许数据公开成功！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(CMyString.isEmpty(dataIds)){
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("允许数据公开失败！没有传入数据ID！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			LOG.error("公开数据时没有传入数据ID！");
			return josnStr;
		}
		
		for(int i=0;i<arrIds.length;i++){		
			Map<String, Object> appinfo = new HashMap<String, Object>();
			appinfo.put("METADATAID", arrIds[i]);
			appinfo.put("ISPUBLIC", "1");
			list.add(appinfo);
		}
		
		if(!CMyString.isEmpty(tableName)){
			try {
				publicAppBaseService.saveOrUpdateMetadatas(list, tableName);
				
			} catch (SQLException e) {
				josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("允许数据公开失败！修改数据信息时出现异常！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
				LOG.error("公开数据信息时异常！");
				e.printStackTrace();
			}
		}else{
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("允许数据公开失败！没有传入元数据表！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			LOG.error("公开数据时没有传入元数据表名！");
		}

		System.out.println("tableName:"+tableName);
		request.setAttribute("appId",appId);

		return josnStr;
	}
	/** 
	* Description: 允许公开选中的数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=hideDatas")
	@ResponseBody
	public String hideDatas(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");//应用ID
		String tableName = request.getParameter("tableName");//元数据表名
		String dataIds = request.getParameter("dataIds");//数据ID字符串，以","隔开
		String arrIds[] = CMyString.split(dataIds, ",");
		String josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("数据不允许公开成功！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(CMyString.isEmpty(dataIds)){
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("数据不允许公开失败！没有传入数据ID！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			LOG.error("数据不允许时没有传入数据ID！");
			return josnStr;
		}
		
		for(int i=0;i<arrIds.length;i++){		
			Map<String, Object> appinfo = new HashMap<String, Object>();
			appinfo.put("METADATAID", arrIds[i]);
			appinfo.put("ISPUBLIC", "0");
			list.add(appinfo);
		}
		
		if(!CMyString.isEmpty(tableName)){
			try {
				publicAppBaseService.saveOrUpdateMetadatas(list, tableName);
				
			} catch (SQLException e) {
				josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("允许数据公开失败！修改数据信息时出现异常！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
				LOG.error("数据不允许公开信息时异常！");
				e.printStackTrace();
			}
		}else{
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("数据不允许公开失败！没有传入元数据表！")+"\",\"\":\"\",\"\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			LOG.error("数据不允许公开时没有传入元数据表名！");
		}

		System.out.println("appId:"+appId);
		request.setAttribute("appId",appId);

		return josnStr;
	}	
	/**
	 * 返回到新建/修改元数据信息页面
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-29 下午04:43:11
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=getMetaDataToAddEdit")
	public String appMetaDataToAddEdit(HttpServletRequest request,HttpServletResponse res){
		String returnUrl = "/appadmin/appMgr/app_MetaData_add";
		try {
			String worked = request.getParameter("worked");//TODO 根据办理状态确定是否当前点击是查看还是办理  
			String opt = CMyString.showEmpty(request.getParameter("opt"), "0");
			request.setAttribute("worked",worked);
			AppUser loginUser = (AppUser)CrtlUtil.getCurrentUser(request);
			String loginUserGname = "";
			AppGroup loginUserGroup = publicAppBaseService.getUserGroup(loginUser.getUserId());
			if(loginUserGroup != null){
				loginUserGname = loginUserGroup.getGname();
			}
			AppInfo appInfo = null;
			String metadataid = request.getParameter("metadataid");
			String appId = request.getParameter("appId");
			request.setAttribute("appId", appId);
			appInfo = (AppInfo)publicAppBaseService.findById(AppInfo.class, Long.valueOf(appId));
			request.setAttribute("appInfo", appInfo);
			if(CMyString.isEmpty(metadataid)){//新建
				//查询主表
				List<Object>  appFieldRels =  publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.parseLong(appId), -1, 0);
				request.setAttribute("appFieldRels", appFieldRels);
			}else{
				if(!CMyString.isEmpty(appInfo.getDowithAddr())){
					returnUrl = appInfo.getDowithAddr();
				}else{
					returnUrl="appadmin/appMgr/app_MetaData_edit";
				}
				
				//修改
				//TODO 如果appid 和 metadataid 为空或不是一个有效的整数则需要抛出异常
				String tableName = appInfo.getMainTableName();//publicAppBaseService.getMetaTableName(Long.parseLong(appId), 0);//元数据表名
				//元数据ID对应的单个信息
				Map<String, Object> metaDataMap = publicAppBaseService.getAppMedataInfo(Long.parseLong(metadataid), tableName);
				//获取字段列表
				List<Object>  appFieldRels =  null;
				//判断是否分组
				List<Object> isGrpFields=  publicAppBaseService.isgrp(appInfo.getViewId());
				if(isGrpFields != null && isGrpFields.size() > 0){
					AppFieldRel grp_appfieldRel = (AppFieldRel)isGrpFields.get(0);
					String grpFieldName = grp_appfieldRel.getFieldName();
					request.setAttribute("grpFieldName", grpFieldName);
					appFieldRels = publicAppBaseService.queryGrpFields(appInfo.getViewId(), (String)metaDataMap.get(grpFieldName));
				}else{
					appFieldRels = publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.parseLong(appId), -1, 0);
				}
				request.setAttribute("appFieldRels", appFieldRels);
				if(metaDataMap.size()>0){//锁定当前对象
					if(publicAppBaseService.isLockObject(Long.parseLong(metadataid), tableName)){
						request.setAttribute("islock", "Y");//已经被锁定
						AppRecordLock appRecordLock =(AppRecordLock)publicAppBaseService.getisLockAppRelock(Long.parseLong(metadataid), tableName).get(0);
						request.setAttribute("appRecordLock", appRecordLock);
					}else{
						publicAppBaseService.LockObject(Long.parseLong(metadataid), tableName, loginUser);
						request.setAttribute("islock", "N"); //未锁定，锁定后返回
					}
				}
				if(CMyString.isEmpty((String)metaDataMap.get("REPLYUSER"))){
					metaDataMap.put("REPLYUSER", loginUser.getUsername());
				}
				if(CMyString.isEmpty((String)metaDataMap.get("REPLYDEPT"))){
					metaDataMap.put("REPLYDEPT", loginUserGname);
				}
				request.setAttribute("metaDataMap", metaDataMap);
				//查询工作流信息
				//AppInfo  appInfo = (AppInfo)publicAppBaseService.findById(AppInfo.class, Long.parseLong(appId));
				//String _flowId = publicAppBaseService.getAppInfoFields(loginUser.getUserId(), "flowId", Long.parseLong(appId));
				if(appInfo.getFlowId()!=null&&appInfo.getFlowId()>0){//有工作流 isAppFlow
					Map<String,Object> allResult = 	publicAppBaseService.
					getAppFlowDocs(Long.parseLong(metadataid), Long.parseLong(appId), loginUser);
					request.setAttribute("allResult", allResult);
				}
				request.setAttribute("opt", opt);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnUrl;
	}
	/**
	 * 流程处理页面，异步返回节点的用户和组织josn串
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-12 下午01:58:12
	* Last Modified:
	* @param request
	* @param res
	* @return String 串，前台需要转换为json串
	* @version 1.0
	 */
	@RequestMapping(params = "method=queryFlowNodeAjax")
	@ResponseBody
	public String queryFlowNodeAjax(HttpServletRequest request,HttpServletResponse res){
		AppUser loginUser = (AppUser)	CrtlUtil.getCurrentUser(request);

		String appId = request.getParameter("appId");
		String metedataId = request.getParameter("metedataId");
		String nodeId = request.getParameter("nodeId");
		StringBuffer strGrpOrUserJson = new StringBuffer("{");
		
		try {
			Map<String, Object> flowGrpOrUsers = new HashMap<String, Object>();
			try {
				flowGrpOrUsers = publicAppBaseService.getChangeNextFlowNode(Long.parseLong(appId), Long.parseLong(metedataId), Long.parseLong(nodeId),loginUser);
			} catch (Exception e) {
				// 获取异常，这里必须单独对上面方法捕获异常
				//e.printStackTrace();
				LOG.debug(e);
			}
			strGrpOrUserJson.append("\"groups\":{");
			if(flowGrpOrUsers.get("appFlowGroups")!=null){
				List<Object> appGroups = (List)flowGrpOrUsers.get("appFlowGroups");
				for(int i=0;i< appGroups.size();i++){
					AppGroup appGroup = (AppGroup) appGroups.get(i);
					strGrpOrUserJson.append("\"").append("grp").append(i).append("\":{");
					strGrpOrUserJson.append(getJson("groupId", String.valueOf(appGroup.getGroupId()))).append(",");
					strGrpOrUserJson.append(getJson("groupName", CMyString.native2Ascii(appGroup.getGname())));
					strGrpOrUserJson.append("}");
					if(i<appGroups.size()-1){
						strGrpOrUserJson.append(",");
					}
				}
			}
			strGrpOrUserJson.append("}");
			strGrpOrUserJson.append(",");
			strGrpOrUserJson.append("\"users\":{");
			if(flowGrpOrUsers.get("appFlowUsers")!=null){
				List<Object> appUsers = (List)flowGrpOrUsers.get("appFlowUsers");
				for(int i=0;i< appUsers.size();i++){
					AppUser appUser = (AppUser) appUsers.get(i);
					strGrpOrUserJson.append("\"").append("use").append(i).append("\":{");
					strGrpOrUserJson.append(getJson("userId", String.valueOf(appUser.getUserId()))).append(",");
					strGrpOrUserJson.append(getJson("userName", CMyString.native2Ascii(appUser.getUsername())));
					strGrpOrUserJson.append("}");
					if(i<appUsers.size()-1){
						strGrpOrUserJson.append(",");
					}
				}
			}
			strGrpOrUserJson.append("}");
			strGrpOrUserJson.append(",");
			strGrpOrUserJson.append("\"grpus\":{");
			if(flowGrpOrUsers.get("flowGroupAndUsers")!=null){
				List<Object> appUsers = (List)flowGrpOrUsers.get("flowGroupAndUsers");
				String grpid=null;
				for(int i=0;i< appUsers.size();i++){
					FlowGroupAndUsers  maps = (FlowGroupAndUsers)appUsers.get(i);
					strGrpOrUserJson.append("\"").append("grp").append(i).append("\":{");
					strGrpOrUserJson.append(getJson("groupId", String.valueOf(maps.getGroupId()))).append(",");
					strGrpOrUserJson.append(getJson("groupName", CMyString.native2Ascii(maps.getGroupName())));
					strGrpOrUserJson.append(",");
					strGrpOrUserJson.append("\"us\":{");
						List<AppUser> _cur_Users =maps.getUsers();
						for(int j=0;j< _cur_Users.size();j++){
								AppUser _cur_user = (AppUser)_cur_Users.get(j);
								strGrpOrUserJson.append("\"").append("us").append(j).append("\":{");
								strGrpOrUserJson.append(getJson("userId", String.valueOf(_cur_user.getUserId()))).append(",");
								strGrpOrUserJson.append(getJson("userName", CMyString.native2Ascii(_cur_user.getUsername())));
								strGrpOrUserJson.append("}");
								if(j<_cur_Users.size()-1){
									strGrpOrUserJson.append(",");
								}
						}
						strGrpOrUserJson.append("}");
						strGrpOrUserJson.append("}");
						if(i<appUsers.size()-1){
							strGrpOrUserJson.append(",");
						}
				}
			}
			strGrpOrUserJson.append("}");
			strGrpOrUserJson.append("}");
		} catch (NumberFormatException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			
		}
		return strGrpOrUserJson.toString();
	}
	public String getJson(String key,String value){
		StringBuffer json = new StringBuffer();
		json.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"");
		return json.toString();
	}
	/**
	 * 保存修改一条元数据信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-29 下午04:44:03
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=saveOrUpMetaData")
	@ResponseBody
	public String saveOrUpMetaData(HttpServletRequest request,HttpServletResponse res){
		String josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("保存信息成功！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		try {
		AppUser loginUser = (AppUser)	CrtlUtil.getCurrentUser(request);
		Map<String, Object> metaDataAppInfo = new HashMap<String, Object>();
		String loginUserGname = "";
		AppGroup loginUserGroup = publicAppBaseService.getUserGroup(loginUser.getUserId());
		if(loginUserGroup != null){
			loginUserGname = loginUserGroup.getGname();
		}
		String opt = CMyString.showEmpty(request.getParameter("opt"), "");
		String sub_dep = request.getParameter("sub_dep");//开始节点自定义操作自动跳转参数
		String metadataid = request.getParameter("metadataid");
		Long appId = Long.parseLong(request.getParameter("appId"));
		
			if(CMyString.isEmpty(metadataid)){//新建
				//查询主表字段
				List<Object>  fieldList =  publicAppBaseService.queryAppFields(loginUser.getUserId(), appId, -1, 0);
				for(Object field : fieldList){
					AppFieldRel appFieldRel =(AppFieldRel)field;
					if(appFieldRel.getFieldType().equals(Global.FIELD_TYPE_CHECKBOX)){//多选框特殊处理
						String [] checks = request.getParameterValues(appFieldRel.getFieldName());
						metaDataAppInfo.put(appFieldRel.getFieldName(), splitChecks(checks, appFieldRel.getDefaultValue()));
					}else{
						if(appFieldRel.getFieldType().equals("date")){//日期类型特殊处理
							String currdate = request.getParameter(appFieldRel.getFieldName());
							if(!CMyString.isEmpty(currdate))
								metaDataAppInfo.put(appFieldRel.getFieldName(),currdate.length()>10?DateUtil.nowSqlDate(currdate):DateUtil.nowSqlToDate(currdate));
							else
								metaDataAppInfo.put(appFieldRel.getFieldName(),request.getParameter(appFieldRel.getFieldName()));
						}else{
							metaDataAppInfo.put(appFieldRel.getFieldName(),request.getParameter(appFieldRel.getFieldName()));
						}
					}
				}
				metaDataAppInfo.remove("APPID");
				metaDataAppInfo.put("APPID",appId);//内置应用的ID
				metaDataAppInfo.remove("CRUSER");
				metaDataAppInfo.put("CRUSER", loginUser.getUsername());
				metaDataAppInfo.remove("CRTIME");
				metaDataAppInfo.put("CRTIME", DateUtil.nowSqlDate());
				metaDataAppInfo.remove("DELETED");
				metaDataAppInfo.put("DELETED", 0);
				metaDataAppInfo.remove("STATUS");
				metaDataAppInfo.put("STATUS", 0);
				metaDataAppInfo.remove("ISPUBLIC");
				metaDataAppInfo.put("ISPUBLIC", 0);
				metaDataAppInfo.remove("HITCOUNTS");
				metaDataAppInfo.put("HITCOUNTS", 0);
				metaDataAppInfo.put("SYNCFLAG", 0);
				//查询元数据表名
				String tableName = publicAppBaseService.getMetaTableName(appId, 0);
			
				if(publicAppBaseService.isAppFlow(appId)){//有工作流
					
					String code= publicAppBaseService.isSaveFlow(appId,Long.parseLong(metadataid!=null?metadataid:"0"),loginUser,sub_dep);//判断工作流初始节点是否配置了正确的用户和组织
					if(!code.equals("11000")){
						return josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("当前应用工作流配置的用户和组织不正确！请检查1.应用是否设置角色2.角色是否设置用户或应用组织下是否存在用户!ERRORCODE:"+code)+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
					}
					//TODO wenjunhui
					Long curr_metaid = publicAppBaseService.saveOrUpdateMetadata(metaDataAppInfo, tableName);//获取刚保存的文档ID
					String czyj = request.getParameter("czyj");
					String czStatus = request.getParameter("czStatus");
					Long flowDocId =Long.parseLong(CMyString.showNull(request.getParameter("flowDocId"),"0"));
					Long nextNodeId =Long.parseLong(CMyString.showNull(request.getParameter("nextNodeId"),"0"));
					Long nodeId =Long.parseLong(CMyString.showNull(request.getParameter("nodeId"),"0"));
					String [] flowToUserIds = request.getParameterValues("flowToUserIds");
					String [] flowToGroupIds = request.getParameterValues("flowToGroupIds");
					publicAppBaseService.appMetaFlowSave(loginUser, curr_metaid, czyj, czStatus, flowDocId,appId,
							nextNodeId,nodeId
							,splitChecks(flowToUserIds,""), splitChecks(flowToGroupIds,""),sub_dep);
				}else{
					//无工作流
//					Long curr_metaid = publicAppBaseService.saveOrUpdateMetadata(metaDataAppInfo, tableName);//获取刚保存的文档ID
					publicAppBaseService.saveOrUpdateMetadata(metaDataAppInfo, tableName);
				}
				
			}else{//修改
				//查询元数据表名
				String tableName = publicAppBaseService.getMetaTableName(appId, 0);
				//查询主表字段
				List<Object>  fieldList =  publicAppBaseService.queryAppFields(loginUser.getUserId(), appId, -1, 0);
//				String tableName = publicAppBaseService.getMetaTableName(appId, 0);//元数据表名
//				//元数据ID对应的单个信息
				 metaDataAppInfo = publicAppBaseService.getAppMedataInfo(Long.parseLong(metadataid), tableName);
				//metaDataAppInfo.put("METADATAID", metadataid);
				for(Object field : fieldList){
					AppFieldRel appFieldRel =(AppFieldRel)field;
					if(appFieldRel.getFieldName().equals("REPLYDEPT")||appFieldRel.getFieldName().equals("REPLYUSER")||appFieldRel.getFieldName().equals("CRUSER")||appFieldRel.getFieldName().equals("DELETED")||appFieldRel.getFieldName().equals("ISPUBLIC")){
						continue;
					}
					if(appFieldRel.getFieldName().equals("CRTIME")){
						metaDataAppInfo.put("CRTIME",DateUtil.nowSqlDate((String)metaDataAppInfo.get("CRTIME")));
						continue;
					}
					if(appFieldRel.getFieldName().equals("REPLYTIME")&&!CMyString.isEmpty((String)metaDataAppInfo.get("REPLYTIME"))){
						metaDataAppInfo.put("REPLYTIME",DateUtil.nowSqlDate((String)metaDataAppInfo.get("REPLYTIME")));
						continue;
					}
					if(appFieldRel.getFieldType().equals(Global.FIELD_TYPE_CHECKBOX) && appFieldRel.getNotEdit() == 0){//多选框特殊处理
						String [] checks = request.getParameterValues(appFieldRel.getFieldName());
						metaDataAppInfo.remove(appFieldRel.getFieldName());
						metaDataAppInfo.put(appFieldRel.getFieldName(), splitChecks(checks, appFieldRel.getDefaultValue()));
					}else if(appFieldRel.getNotEdit() == 0){
						String value = request.getParameter(appFieldRel.getFieldName());
						if(appFieldRel.getFieldType().equals("date") && !CMyString.isEmpty(value)){
							metaDataAppInfo.remove(appFieldRel.getFieldName());
							metaDataAppInfo.put(appFieldRel.getFieldName(), value.length()>10?DateUtil.nowSqlDate(value):DateUtil.nowSqlToDate(value));
						}else if(value != null){
							metaDataAppInfo.remove(appFieldRel.getFieldName());
							metaDataAppInfo.put(appFieldRel.getFieldName(), value);
						}
					}
				}
				String operateType = request.getParameter("operateType");//办理方式
				String replycontent = request.getParameter("REPLYCONTENT");
				if(!CMyString.isEmpty(replycontent)){//回复内容不为空保存回复时间和回复部门
					String hfbsstat =request.getParameter("hfbsstat");
					if(!CMyString.isEmpty(hfbsstat)&&hfbsstat.equals("1")){
						//已经存在回复部门了，如果是交办则修改回复部门和时间。
						if(!CMyString.isEmpty(operateType)&&operateType.equals("4")){
							metaDataAppInfo.remove("REPLYTIME");
							metaDataAppInfo.put("REPLYTIME", DateUtil.nowSqlDate());//答复时间
							metaDataAppInfo.remove("REPLYUSER");//答复人
							metaDataAppInfo.put("REPLYUSER", loginUser.getUsername());
							metaDataAppInfo.remove("REPLYDEPT");
							metaDataAppInfo.put("REPLYDEPT", loginUserGname);
							if(loginUserGroup != null){
								metaDataAppInfo.put("SUBMITDEPT", loginUserGroup.getGroupId());
								metaDataAppInfo.put("SUBMITDEPTNAME", loginUserGname);
							}
						}
					}else{
						metaDataAppInfo.remove("REPLYTIME");
						metaDataAppInfo.put("REPLYTIME", DateUtil.nowSqlDate());//答复时间
						metaDataAppInfo.remove("REPLYUSER");//答复人
						metaDataAppInfo.put("REPLYUSER", loginUser.getUsername());
						metaDataAppInfo.remove("REPLYDEPT");
						metaDataAppInfo.put("REPLYDEPT", loginUserGname);
						if(loginUserGroup != null){
							metaDataAppInfo.put("SUBMITDEPT", loginUserGroup.getGroupId());
							metaDataAppInfo.put("SUBMITDEPTNAME", loginUserGname);
						}
					}
				}else{
					metaDataAppInfo.remove("REPLYTIME");
				}
				metaDataAppInfo.remove("OPERTIME");
				metaDataAppInfo.put("OPERTIME", DateUtil.nowSqlDate());//操作时间
				metaDataAppInfo.remove("OPERUSER");//操作人
				metaDataAppInfo.put("OPERUSER", loginUser.getUsername());
				if("1".equals((String)metaDataAppInfo.get("SYNCFLAG"))){
					metaDataAppInfo.remove("SYNCFLAG");
					metaDataAppInfo.put("SYNCFLAG", 2);
				}
				if(Global.METADATA_OPT_HANDDLE.equals(opt)){
					String isShowNode= 	CMyString.showEmpty(request.getParameter("isShowNode"),"N"); //无下一个节点
					
					if(publicAppBaseService.isAppFlow(appId)&&!isShowNode.equals("N")){//有工作流
						String czyj = request.getParameter("czyj");
						Long flowDocId =Long.parseLong(CMyString.showEmpty(request.getParameter("flowDocId"),"0"));
						Long nextNodeId =Long.parseLong(CMyString.showEmpty(request.getParameter("nextNodeId"),"0"));
						Long nodeId =Long.parseLong(CMyString.showEmpty(request.getParameter("nodeId"),"0"));
						String [] flowToUserIds = request.getParameterValues("flowToUserIds");
						String [] flowToGroupIds = request.getParameterValues("flowToGroupIds");
						if(operateType.equals("1")&&!isShowNode.equals("HN")){
							//直接办理
							AppFlow appFlow =	publicAppBaseService.getAppFlow(appId);
							nextNodeId = appFlow.getEnodeId();
							if(loginUserGroup != null){
								metaDataAppInfo.put("SUBMITDEPT", loginUserGroup.getGroupId());
								metaDataAppInfo.put("SUBMITDEPTNAME", loginUserGname);
							}
						}else if(operateType.equals("2")&&!isShowNode.equals("HN")){
							//退回
							 Map<String,Object>  backList  = publicAppBaseService.backFlow(flowDocId, Long.parseLong(metadataid));
							
							nextNodeId= Long.parseLong(String.valueOf(backList.get("nodeId")));
							flowToUserIds = ((String)backList.get("userids")).split(",");
							
						}else if(!operateType.equals("0")){
							if(appFlowService.isLastNode(nextNodeId)){//结束节点需要更新当前信息的状态为以完成
								metaDataAppInfo.remove("STATUS");
								metaDataAppInfo.put("STATUS", 2);
							}else{
								//如果没有选择用户或者组织则抛出异常,会签节点不走此流程
								if(flowToUserIds==null&&flowToGroupIds==null&&!isShowNode.equals("HN")){
									josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("请选择下一个节点的接收用户或组织！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
									return josnStr;
								}
								metaDataAppInfo.remove("STATUS");
								metaDataAppInfo.put("STATUS", 1);
							}
						}
						//保存工作流信息
						publicAppBaseService.appMetaFlowSave(loginUser, Long.parseLong(metadataid), czyj, isOperateType(operateType), flowDocId,appId,
								nextNodeId,nodeId
								,splitChecks(flowToUserIds,""), splitChecks(flowToGroupIds,""),sub_dep);
						
					}else if(!publicAppBaseService.isAppFlow(appId)&&!CMyString.isEmpty(replycontent)){
							metaDataAppInfo.remove("STATUS");
							metaDataAppInfo.put("STATUS", 2);
					}
				}
				publicAppBaseService.saveOrUpdateMetadata(metaDataAppInfo, tableName);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("修改信息成功！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			}
			request.setAttribute("appId", appId);
		} catch (NumberFormatException e) {
			 josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("保存修改信息失败！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			e.printStackTrace();
		} catch (Exception e) {
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("保存修改信息失败！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			e.printStackTrace();
		}
		return josnStr; 
	}
	public String isOperateType(String operateType){
		String opt = "签收";
			if(operateType.equals("0")){
				opt= "签收";
			}else if(operateType.equals("1")){
				opt= "直接办理";
			}else if(operateType.equals("2")){
				opt= "退回";
			}else if(operateType.equals("3")){
				opt= "办理";
			}else if(operateType.equals("4")){
				opt= "交办";
			}
			return opt;
	}
	/**
	 * 返回某个工作流节点的详细处理轨迹信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-8 下午07:57:04
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=getMetaFlowInfo")
	public String getMetaFlowInfo(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");
		String metadataId = request.getParameter("metadataId");
		String nodeId = request.getParameter("nodeId");
		String preflowdocid = request.getParameter("preflowdocid");
		
		try {
		Map<String, Object> flowsMap = publicAppBaseService.getMetadataFlowDoc(Long.parseLong(appId),Long.parseLong(metadataId),Long.parseLong(nodeId),Long.parseLong(preflowdocid));
		request.setAttribute("flowsMap",flowsMap);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "appadmin/appMgr/app_MetaData_view_flow";
	}
	/**
	 * 返回前台提交的附件信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-8 下午07:57:04
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=getAppendixs")
	public String getAppAppendixs(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");
		String metadataId = request.getParameter("metadataId");
		request.setAttribute("appId", appId);
		request.setAttribute("metadataId", metadataId);
		try {
		if(CMyString.isEmpty(appId) || CMyString.isEmpty(metadataId)){
			return "appadmin/appMgr/app_appendixs";
		}
			List<Object> appendixs = publicAppBaseService.getAppAppendixs(Long.parseLong(appId), Long.parseLong(metadataId));
			request.setAttribute("appendixs", appendixs);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "appadmin/appMgr/app_appendixs";
	}
	/**
	 * 正常关闭页面，解除对象锁定
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-1 下午01:53:48
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=uLockObject")
	@ResponseBody
	public String uLockObject(HttpServletRequest request,HttpServletResponse res){
		String appId =	request.getParameter("appId");
		String mesid =	request.getParameter("mesid");
		String tableType = request.getParameter("tableType");
			//查询元数据表名
			try {
				String tableName = publicAppBaseService.getMetaTableName(Long.parseLong(appId), Integer.parseInt(tableType));
				AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
				
				publicAppBaseService.ULockObject(Long.parseLong(mesid), tableName, loginUser);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	/**
	 * 异常关闭，解除当前用户锁定的所有对象
	* Description: 
	* @author wen.junhui
	* @date Create: 2014-4-1 下午01:55:04
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=ULockErrorObject")
	@ResponseBody
	public String ULockErrorObject(HttpServletRequest request,HttpServletResponse res){
			//查询元数据表名
			try {
				AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
				

				publicAppBaseService.ULockErrorObject(loginUser);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

	/** 
	* Description: 拼接字段名称为以","隔开的字符串 <BR>   
	* author liujian
	* date 2014-3-29 上午10:59:20
	* @param fieldList
	* @return
	* version 1.0
	*/
	public static String splitListFields(List<Object> fieldList,String field){
		StringBuffer str = new StringBuffer();
		if(!CMyString.isEmpty(field)){
			str = new StringBuffer(field+" ,");
		}
		
		for(Object obj : fieldList){
			AppFieldRel appFieldInfo =(AppFieldRel)obj;
			str.append("am."+appFieldInfo.getFieldName()).append(",");
		}
		//str.append("am.CRTIME");
		return str.toString().length()>0?str.toString().substring(0,str.toString().length()-1):"";
	}
	/** 
	* Description: 拼接字段名称为以","隔开的字符串 <BR>   
	* author liujian
	* date 2014-3-29 上午10:59:20
	* @param fieldList
	* @return
	* version 1.0
	*/
	public static String splitList(List<Object> fieldList){
		
		StringBuffer str = new StringBuffer("METADATAID ,");
		for(Object obj : fieldList){
			AppFieldRel appFieldInfo =(AppFieldRel)obj;
			str.append(appFieldInfo.getFieldName()).append(",");
		}
		str.append("crtime");
		return str.toString().length()>0?str.toString().substring(0,str.toString().length()):"";
	}
	

	/**
	 * 多选框特殊处理 
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-29 下午07:28:07
	* Last Modified:
	* @param checks
	* @param defultValue  为空的默认值
	* @return
	* @version 1.0
	 */
	public String splitChecks(String[] checks,String defultValue){
		if(checks==null)
			return defultValue;
		StringBuffer str = new StringBuffer();
		for(String check :checks){
			str.append(check).append(",");
		}
		return str.length()<1?defultValue:str.toString().substring(0,str.toString().length()-1);
	}
}
