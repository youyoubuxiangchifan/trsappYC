/**
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: AppWebController
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-11 上午11:09:17
 * @version 1.0
 */
package com.trs.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.trs.dbhibernate.Page;
import com.trs.model.AppAppendix;
import com.trs.model.AppComment;
import com.trs.model.AppFieldRel;
import com.trs.model.AppGroup;
import com.trs.model.AppInfo;
import com.trs.model.AppRelGroup;
import com.trs.model.AppSysConfig;
import com.trs.service.AppWebService;
import com.trs.service.CommonService;
import com.trs.service.PublicAppBaseService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.DateUtil;
import com.trs.util.Global;
import com.trs.util.UploadFileUtil;

/**
 * Description: 外网提交controller<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: AppWebController
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-4-11 上午11:09:17
 * version 1.0
 */
@Controller
@RequestMapping("appWeb.do")
public class AppWebController {
	private static  Logger LOG =  Logger.getLogger(AppWebController.class);
	@Autowired
	private AppWebService appWebService;
	@Autowired
	private CommonService commonservice;
	@Autowired
	private PublicAppBaseService publicAppBaseService;
	/** 
	* Description: 获取首页应用数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=appIndexData")
	public String appIndexData(HttpServletRequest request,HttpServletResponse res){
		String groupId = CMyString.showEmpty(request.getParameter("groupId"), "1118659");
		String status = CMyString.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");//数据状态 0:待处理，1处理中，2已处理
		String deleted = CMyString.isEmpty(request.getParameter("deleted"))?"0":request.getParameter("deleted");//数据状态 0:未删除，1-已删除
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");//当前页数
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"5":request.getParameter("numPerPage");//每页显示条数
		String tableType = CMyString.isEmpty(request.getParameter("tableType"))?"0":request.getParameter("tableType");//表类型：0-主表，1-回帖表 ***数据相关必传字段

		List<Object> fieldList = new ArrayList<Object>();		
		Map<String, List<Object>> appDataMap = new HashMap<String, List<Object>>(); //存放应用数据信息
		Map<String, String> titleFiledMap = new HashMap<String, String>(); //存放应用标题字段名称
		Map<String, String> viewNameMap = new HashMap<String, String>(); // 存放应用对应的视图名称
		Map<String, Integer> appTypeMap = new HashMap<String, Integer>(); //存放应用类型（是否是主题类型）
		String tableName = "";
		String sWhere ="";//拼接检索条件
		String sOrder=""; //排序条件
		AppInfo appInfo = null;
		if(CMyString.isEmpty(groupId)){
			LOG.error("取应用数据列表时没有传入组织ID！");
			return "web/AppWeb/webIndex";
		}
		List<Object> appList = new ArrayList<Object>();
		try {
			appList = appWebService.getApps(Long.valueOf(groupId));
		} catch (NumberFormatException e1) {
			LOG.error("获取应用应用集合转换数据类型异常！");
			e1.printStackTrace();
		} catch (Exception e1) {
			LOG.error("获取应用应用集合异常！");
			e1.printStackTrace();
		}		
		String appId =""; //应用编号
		String titleFiledName = null;
		try {
			for(int i=0;i<appList.size();i++){
				
			List<Object> param = new ArrayList<Object>();
			sWhere ="1 = 1";//拼接检索条件
			sOrder="crtime desc"; //排序条件
			appInfo = (AppInfo)appList.get(i); //取得应用对象
			appId = appInfo.getAppId().toString();
			tableName = appWebService.getMetaTableName(Long.valueOf(appId), Integer.parseInt(tableType));
			appTypeMap.put(appId, appInfo.getIsNeedTheme());//设置应用的类型
			viewNameMap.put(appId, appInfo.getViewName());//设置应用的视图名称
			fieldList = appWebService.queryAppFields(appInfo.getViewId(), 3,tableName );//取出标题字段			
			AppFieldRel appFieldRel = (AppFieldRel)fieldList.get(0);
			titleFiledName = appFieldRel.getFieldName();//标题字段名 
			String fieldListStr = null;//要查询的字段
			if(appInfo.getIsNeedTheme()==0){
				fieldListStr = titleFiledName+",METADATAID,STATUS,APPID,REPLYDEPT,CRTIME,HITCOUNTS";
			}else{
				fieldListStr = titleFiledName+",METADATAID,STARTTIME,ENDTIME";
				pageSize = "4";
			}
			
			titleFiledMap.put(appId, titleFiledName);
			request.setAttribute("titleFiledMap", titleFiledMap);//设置应用标题字段

			sWhere = sWhere+" and appId = ?"; //应用编号
			param.add(appId);
			sWhere =sWhere+" and ISPUBLIC = 1"; //查询不公开的信息
			if(!CMyString.isEmpty(status)&&"0".equals(deleted)){
				sWhere =sWhere+" and STATUS in (0,1,2)"; //数据状态条件
				//param.add(status);
			}

			if(!CMyString.isEmpty(deleted)){
				sWhere =sWhere+" and DELETED = ?"; //数据删除状态
				param.add(deleted);
			}
			if(appInfo.getIsPublic()!=1){//判断应用是否公开信息
				Page page = null;
				page = appWebService.queryAppMetadatas(tableName, fieldListStr, sWhere, param, sOrder, Integer.parseInt(currPage),Integer.parseInt(pageSize));
				appDataMap.put(appId, page.getLdata());//设置应用的数据列表	
			}
			
			}
			
			request.setAttribute("appTypeMap", appTypeMap); //设置应用类型
			request.setAttribute("vaiewName", viewNameMap); //设置视图名称
			request.setAttribute("appDataMap", appDataMap); //设置数据列表
		} catch (NumberFormatException e) {			
			LOG.error("获取应用数据列表时字符转数字类型异常！");
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error("获取应用数据列表异常！");
			e.printStackTrace();
		}
		String now = DateUtil.nowFormat();
		request.setAttribute("now", now.substring(0, 10));
		request.setAttribute("appList", appList);
		request.setAttribute("groupId", CMyString.filterForHTMLValue(groupId));
		return "web/AppWeb/webIndex";
	}
	/** 
	* Description: 评论信息提交处理：保存评论信息 <BR>   
	* author liujian
	* date 2014-4-14 下午05:07:05
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=comment")
	@ResponseBody
	public String comment(HttpServletRequest request,HttpServletResponse res){ 
		String appId = request.getParameter("appId");//应用ID--必传字段
		String dataId = request.getParameter("dataId");//数据ID--必传字段
		String commentContent = request.getParameter("commentContent");//评论内容--必传字段
		String commentScore = request.getParameter("commentScore");//评论评分
		String conmentUser = CMyString.showEmpty(request.getParameter("conmentUser"), "匿名");//评论用户
		String saveFlag = "true";
		Date dateNow = new Date();
		if(CMyString.isEmpty(appId) || CMyString.isEmpty(dataId)){
			saveFlag = "false";
			return saveFlag;
		}
		AppComment appComment = new AppComment();
		appComment.setCommentContent(commentContent);
		appComment.setCommentScore(Short.parseShort(commentScore));
		appComment.setDataId(Long.valueOf(dataId));
		appComment.setAppId(Long.valueOf(appId)); //设置应用ID
		appComment.setCommentUser(conmentUser);
		appComment.setCommentStatus(0);
		appComment.setCrtime(dateNow);
		try {
			appWebService.saveObj(appComment);
		} catch (Exception e) {
			saveFlag = "false";
			LOG.error("保存评论信息异常！");
			e.printStackTrace();
		}		
		
		return saveFlag;
	}

	/** 
	* Description: 保存前台提交信息 <BR>   
	* author liujian
	* date 2014-4-25 下午07:13:01
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=saveAppData")
	public String saveAppData(MultipartHttpServletRequest request,HttpServletResponse res){
		String groupId = CMyString.showEmpty(request.getParameter("groupId"), "1118659");
		String appId = CMyString.showEmpty(request.getParameter("appId"), "0");
		String isNeedTheme = CMyString.showEmpty(request.getParameter("isNeedTheme"), "0");
		String themeId = request.getParameter("themeId");
		String checkCode = request.getParameter("checkCode");
		String sessionCode = CrtlUtil.getObjFromSession(request, "smt_"+appId);
		String sub_dep = CMyString.showEmpty(request.getParameter("sub_dep"),"");
		//String selGrpNameString = request.getParameter("SQLX");
		Long submitDept = null;
		String submitDeptName = null;
		if(!CMyString.isEmpty(sub_dep)){
			submitDept = Long.parseLong(sub_dep);
			AppGroup subDept = (AppGroup)publicAppBaseService.findById(AppGroup.class, submitDept);
			submitDeptName = subDept.getGname();
		}
		String errorCode = "10000";
		request.setAttribute("groupId",groupId);
		if(CMyString.isEmpty(checkCode) || CMyString.isEmpty(sessionCode)){
			request.setAttribute("Msg", "验证码不能为空！");
			return "web/AppWeb/webError"; //验证码不正确，跳转到提交失败页
		}
		if(!sessionCode.equalsIgnoreCase(checkCode)){
			request.setAttribute("Msg", "验证码不正确！");
			return "web/AppWeb/webError"; //验证码不正确，跳转到提交失败页
		}
		//清空验证码
		request.getSession().removeAttribute("smt_" + appId);
		
		Long lappId = 0l;
		
		List<Object> appList = new ArrayList<Object>();
		try {
			appList = appWebService.getApps(Long.valueOf(groupId));
			request.setAttribute("appList", appList);
			request.setAttribute("appId", appId);
		} catch (NumberFormatException e1) {
			LOG.error("获取应用应用集合转换数据类型异常！");
			e1.printStackTrace();
			request.setAttribute("Msg", "系统错误，请联系管理员！");
			return "web/AppWeb/webError";
		} catch (Exception e1) {
			LOG.error("获取应用应用集合异常！");
			e1.printStackTrace();
			request.setAttribute("Msg", "系统错误，请联系管理员！");
			return "web/AppWeb/webError";
		}
		List<Object> fieldList = null;
		List<MultipartFile> fileList = null;
		Map<String,Object> fieldMap = new HashMap<String, Object>();
		fileList = request.getFiles("file");

		try {
			lappId = Long.valueOf(appId);
			AppInfo appInfo = (AppInfo) appWebService.findById(AppInfo.class,lappId);
			String tableName = appInfo.getMainTableName();
			Long tableId = appInfo.getMainTableId();
			String queryNumber =UploadFileUtil.getFileName(); //查询编码
			String queryPwd = CMyString.generateNumStr(6);
			if("1".equals(isNeedTheme) && CMyString.isEmpty(themeId)){
				return "web/AppWeb/webError"; //主题字段为空，跳转到提交失败页
			}
//			if("0".equals(isNeedTheme) && appInfo.getIsSelGroup() == 1){
//				fieldMap.put("SUB_DEP","sub_dep");
//			}
			if("1".equals(isNeedTheme)){
				tableName = appInfo.getItemTableName();
				tableId = appInfo.getItemTableId();
				fieldMap.put("THEMEID",themeId);
			}			
			fieldList = appWebService.getAppFields(appInfo.getViewId(),tableId,0);//取提交字段
			AppFieldRel appFieldRel = null;
			List<Object> isGrpFields = isgrp(appInfo.getViewId());
			if(isGrpFields.size()>0){ //如果存在分组字段，则加载分组字段对应的字段信息
				AppFieldRel grp_appfieldRel = 	(AppFieldRel)isGrpFields.get(0);
				String grp_name= grp_appfieldRel.getFieldName();
				String selGrpName = request.getParameter(grp_name);
				List<Object> findTjGrpfields=  findTjGrpfields(appInfo.getViewId(),selGrpName);
				
				for(int j=0;j<findTjGrpfields.size();j++){
					AppRelGroup appRelGroup = (AppRelGroup)findTjGrpfields.get(j);
					for(int i=0;i<fieldList.size();i++){
						appFieldRel = (AppFieldRel)fieldList.get(i);
						if(appFieldRel.getNotNull()==1 && CMyString.isEmpty(request.getParameter(appFieldRel.getFieldName())) && appRelGroup.getFieldId()==appFieldRel.getFieldId()){
							request.setAttribute("groupId",appInfo.getGroupId());
							request.setAttribute("Msg", "["+appFieldRel.getFieldName()+"] 不能为空，请核对信息后重新提交！");
							return "web/AppWeb/webError"; //必填字段为空，跳转到提交失败页
						}
						fieldMap.put(appFieldRel.getFieldName(), CMyString.filterForHTMLValue(request.getParameter(appFieldRel.getFieldName())));								
						
					}
				}
			}else{
				for(int i=0;i<fieldList.size();i++){
					appFieldRel = (AppFieldRel)fieldList.get(i);
					if(appFieldRel.getNotNull()==1 && CMyString.isEmpty(request.getParameter(appFieldRel.getFieldName()))){
						request.setAttribute("groupId",appInfo.getGroupId());
						request.setAttribute("Msg", "["+appFieldRel.getFieldName()+"] 不能为空，请核对信息后重新提交！");
						return "web/AppWeb/webError"; //必填字段为空，跳转到提交失败页
					}
					fieldMap.put(appFieldRel.getFieldName(), CMyString.filterForHTMLValue(request.getParameter(appFieldRel.getFieldName())));								
					
				}
			}
			if("0".equals(isNeedTheme) && appInfo.getIsHasQueryNo()==1){
					fieldMap.put("QUERYNUMBER",queryNumber);
					fieldMap.put("QUERYPWD",queryPwd);
					request.setAttribute("QUERYNUMBER", queryNumber);
					request.setAttribute("QUERYPWD", queryPwd);
			}
			request.setAttribute("isHasQueryNo", appInfo.getIsHasQueryNo());
			fieldMap.remove("APPID");
			fieldMap.put("APPID",appId);//内置应用的ID
			fieldMap.remove("CRTIME");
			fieldMap.put("CRTIME", DateUtil.nowSqlDate());
			fieldMap.put("SYNCFLAG", 0);
			fieldMap.put("IPADDRESS", CrtlUtil.getRealIp(request));
			fieldMap.put("SUBMITDEPT", submitDept);
			fieldMap.put("SUBMITDEPTNAME", submitDeptName);
			Long metaDataId = appWebService.saveOrUpdateMetadata(fieldMap, tableName);
			//工作流触发 
			if(appInfo.getFlowId() != null && appInfo.getFlowId() > 0){
				errorCode= publicAppBaseService.isSaveFlow(lappId,metaDataId,null,sub_dep);//判断工作流初始节点是否配置了正确的用户和组织
				if(!errorCode.equals("11000")){
					//这里有错误,工作流设置有问题。不能正确提交数据
					request.setAttribute("Msg", "系统错误，请联系管理员！ERRORCODE:"+errorCode);
					return "web/AppWeb/webError"; //保存数据异常，跳转到提交失败页
				}
				//工作流保存
				errorCode = publicAppBaseService.saveBeforeFirstNode(String.valueOf(appInfo.getFlowId()), lappId, "", metaDataId, sub_dep);
				//request.setAttribute("status", status);
				if(!"10000".equals(errorCode)){
					request.setAttribute("Msg", "系统错误，请联系管理员！ERRORCODE:"+errorCode);
					//工作流保存出错则删除该来信
					publicAppBaseService.delMetadatas(Long.valueOf(appId), tableName, String.valueOf(metaDataId));
				}
				
				LOG.debug(errorCode);
				
			}
			if(fileList.size()>0 && metaDataId!=null){
				commonservice.SaveUploadFiles(fileList, metaDataId, lappId);
			}
		} catch (Exception e) {		
			e.printStackTrace();
			LOG.error("应用编号为："+appId+"前台提交元数据信息错误 ,错误原因:"+e);
			request.setAttribute("groupId",groupId);
			return "web/AppWeb/webError"; //保存数据异常，跳转到提交失败页
		}
		
		return "web/AppWeb/webSucceed";//保存数据成功，跳转到提交成功页
	}
	/**
     * 
    * Description:获取部门应用的查询页面<BR>   
    * @author zhangzun
    * @date 2014-4-25 上午09:18:15
    * @param request
    * @param res
    * @return
    * @version 1.0
     */
	@RequestMapping(params = "method=appDataQuery")
	public String appDataQuery(HttpServletRequest request,HttpServletResponse res){
		String groupId = CMyString.showEmpty(request.getParameter("groupId"), "0");
		Long appId = 0l;
		List<Object> appList = null;
		try {
			appList = appWebService.getApps(Long.valueOf(groupId));
			if(appList!=null && appList.size()>0){
				appId = ((AppInfo)appList.get(0)).getAppId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("appList",appList);
		request.setAttribute("appId",appId);
		
		return "web/AppWeb/appData_query";
	}
	 /**
     * 
    * Description:获取部门应用的提交页面 <BR>   
    * @author zhangzun
    * @date 2014-4-25 上午09:18:15
    * @param request
    * @param res
    * @return
    * @version 1.0
     */
	@RequestMapping(params = "method=addPage")
	public String getAddPage(HttpServletRequest request,HttpServletResponse res){
		String groupId = CMyString.showEmpty(request.getParameter("groupId"), "0");
		String appId = CMyString.showEmpty(request.getParameter("appId"), "0");//应用id
		String themeId = CMyString.showEmpty(request.getParameter("themeId"), "0");//主题id
		String flag = request.getParameter("flag");//标示是否是应用提交说明请求
		List<Object> fieldList = null;
		List<Object> appList = null;
		Long tableId = 0l;
		Long lThemeId = 0l;
		AppInfo appInfo = null;
		String returnUrl = "web/AppWeb/appData_add";
		try {
			if(CMyString.isEmpty(groupId)){
				return "web/AppWeb/webError";
			}
			appList = appWebService.getApps(Long.valueOf(groupId));
			appInfo = (AppInfo) appWebService.findById(AppInfo.class,Long.valueOf(appId));
			//如果有设置前台自定义地址，则跳转
			if(!CMyString.isEmpty(appInfo.getWebdowithAddr())){
				returnUrl = appInfo.getWebdowithAddr();
			}
			if(appInfo==null){
				throw new Exception("应用不存在");
			}else{
				if(appInfo.getIsNeedTheme()==0){
					tableId = appInfo.getMainTableId();
				}else{
					tableId = appInfo.getItemTableId();
					lThemeId = Long.valueOf(themeId);
					if(lThemeId<0){
						throw new Exception("主题id不合法！");
					}
				}
			}
			if(!"1".equals(flag)){//非应用提交说明请求
				fieldList = appWebService.getAppFields(appInfo.getViewId(),tableId,0);
				//应用分组字段查询，通过试图编号查询字段表中是否有分组字段  wjh 2014-09-23。
				List<Object> isGrpFields=  isgrp(appInfo.getViewId());
				request.setAttribute("isGrpFields",isGrpFields);
				if(isGrpFields.size()>0){ //如果存在分组字段，则加载分组字段对应的字段信息
					List<Object> appRelGroups =   findGrpfields(appInfo.getViewId());
					request.setAttribute("appRelGroups",appRelGroups);
					
					Map<String,List<AppFieldRel>> fieldMap= findGrpfields(appRelGroups,fieldList,isGrpFields);
					request.setAttribute("fieldMap", fieldMap);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询应用提交字段",e);
			return returnUrl; //保存数据异常，跳转到提交失败页
		}
		request.setAttribute("groupId",CMyString.filterForHTMLValue(groupId));
		request.setAttribute("appId",CMyString.filterForHTMLValue(appId));
		request.setAttribute("appList",appList);
		request.setAttribute("themeId",lThemeId);
		request.setAttribute("fieldList",fieldList);
		request.setAttribute("appInfo",appInfo);
		if("1".equals(flag)){
			return "web/AppWeb/app_desc";
		}else{
			return returnUrl;
		}
	}
	
	public Map<String,List<AppFieldRel>> findGrpfields(List<Object> appRelGroups , List<Object> fieldList , List<Object> isGrpFields ){
		Map<String,List<AppFieldRel>> fieldMap = new HashMap<String, List<AppFieldRel>>();
		AppFieldRel grp_appfieldRel = 	(AppFieldRel)isGrpFields.get(0);
		List<String> enmList=grp_appfieldRel.getEnmList();
		for(String enmValue : enmList){
			List<AppFieldRel> fieldLists = new ArrayList<AppFieldRel>();
			for(Object obj_field : fieldList){
				AppFieldRel   nAppFieldRel	= (AppFieldRel)obj_field;
				for(Object obj_relGrp : appRelGroups){
					AppRelGroup  appRelGroup = (AppRelGroup)obj_relGrp;
					if(appRelGroup.getGrpName().equals(enmValue)){
						if(nAppFieldRel.getFieldId().toString().equals(appRelGroup.getFieldId().toString())){
							fieldLists.add(nAppFieldRel);
						}
					}
				}
			}
			fieldMap.put(enmValue, fieldLists);
		}
		return fieldMap;
	}
	/**
	 * 
	* Description:获取组织下应用的数据列表<BR>   
	* @author zhangzun
	* @date 2014-4-28 下午02:10:06
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=queryAppData")
	public String queryAppData(HttpServletRequest request,HttpServletResponse res){
		String groupId = request.getParameter("groupId");
		String appId = CMyString.showEmpty(request.getParameter("appId"), "0");
		String currPage = CMyString.isEmpty(request.getParameter("pageNum")) ? Global.DEFUALT_STARTPAGE
				: request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage")) ? Global.DEFUALT_PAGESIZE
				: request.getParameter("numPerPage");
		Long lAppId = 0l;
		List<Object> appList = null;
		String sWhere ="1 = 1";//拼接检索条件
		String sOrder="crtime desc"; //排序条件
		Page page = null;
		List<Object> param = new ArrayList<Object>();
		AppInfo appInfo = null;
		String fieldListStr = ""; //应用查询字段
		String titleFiled = ""; //标题字段
		String returnUrl = null;
		try{
			if(CMyString.isEmpty(groupId)){
				return "web/AppWeb/webError";
			}
			appList = appWebService.getApps(Long.valueOf(groupId));
			if(CMyString.isEmpty(appId) && appList!=null && appList.size()>0){
				lAppId = ((AppInfo)appList.get(0)).getAppId();
			}else{
				lAppId = Long.valueOf(appId);
			}
			appInfo = (AppInfo) appWebService.findById(AppInfo.class,lAppId);
			returnUrl = appInfo.getWeblistAddr();//自定义列表跳转地址，不考虑主题的情况，如果主题情况需要自己定制
			if(appInfo!=null){
				sWhere = sWhere+" and appId = ?"; //应用编号
				param.add(appInfo.getAppId());
				param.add(1);
				param.add(0);
				sWhere =sWhere+" and ISPUBLIC = ? and DELETED = ?"; //查询不公开的信息
				
				List<Object> list = appWebService.queryAppFields(appInfo.getViewId(), 3, appInfo.getMainTableName());
			    if(list==null || list.size()==0)
			    	throw new Exception("应用没有标题字段");
			    titleFiled = ((AppFieldRel)list.get(0)).getFieldName();
			    if(appInfo.getIsNeedTheme()==0){
					fieldListStr = titleFiled+",METADATAID,QUERYNUMBER,STATUS,REPLYDEPT,REPLYTIME,CRTIME,HITCOUNTS";
				}else{
					fieldListStr = titleFiled+",METADATAID,STARTTIME,ENDTIME";
					//回传系统时间
					String now = DateUtil.nowFormat();
					request.setAttribute("now", now.substring(0, 10));
				}
			    page = appWebService.queryAppMetadatas(appInfo.getMainTableName(), fieldListStr,
						sWhere, param, sOrder, Integer.parseInt(currPage),Integer.parseInt(pageSize));
			}else{
				throw new Exception("应用不存在！");
			}
		}catch(Exception e){
			e.printStackTrace();
			LOG.error("查询应用数据发生异常",e);
			return "web/AppWeb/webError"; //保存数据异常，跳转到提交失败页
		}
		request.setAttribute("titleFiled", titleFiled.toUpperCase());
		request.setAttribute("appList",appList);
		request.setAttribute("appId",lAppId);
		request.setAttribute("groupId",groupId);
		request.setAttribute("page",page);
		request.setAttribute("viewName",appInfo.getViewName());
		request.setAttribute("isNeedTheme",appInfo.getIsNeedTheme());
		request.setAttribute("isHasQueryNo",appInfo.getIsHasQueryNo());
		request.setAttribute("isHasSmtDesc",appInfo.getIsHasSmtDesc());
		if(appInfo.getIsNeedTheme()==1)
			
			return CMyString.showEmpty(returnUrl, "web/AppWeb/adviceData_list");
		else
			return CMyString.showEmpty(returnUrl, "web/AppWeb/appData_list");
	}
	
	/**
	 * 
	* Description: 前台细览数据取得 <BR>   
	* @author jin.yu
	* @date 2014-4-28 下午02:47:20
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=appDataDetail")
	public String appDataDetail(HttpServletRequest request,HttpServletResponse res){
		String returnUrl = null;
		String appId = CMyString.showEmpty(request.getParameter("appId"), "0");//应用ID
		String dataId = request.getParameter("dataId");//数据ID
		String groupId = request.getParameter("groupId");//部门ID
		String flag = request.getParameter("flag");//标示是否是应用提交说明请求
		String currPage = CMyString.isEmpty(request.getParameter("pageNum")) ? Global.DEFUALT_STARTPAGE
				: request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage")) ? "5"
				: request.getParameter("numPerPage");
		List<Object> reDataList = new ArrayList<Object>();//返回结果List
		//List<Object> reCommentList = new ArrayList<Object>();//评论结果List
		String appInfoType = "";//应用类型
		List<Object> fieldList = null;;//建议类提交字段
		Long lAppId = 0l;//要取得数据的应用ID
		Page page = null;
		Map<String, Object> dataDetail = null;
		List<Object> appList = null;//组织应用列表
		List<Object> adixList = null;
		AppInfo appInfo = null;//应用对象
		int isHasComment = 0; //应用是否有评论
		String endTime = ""; //应用结束日期
		if(CMyString.isEmpty(groupId)){
			return "web/AppWeb/webError";
		}
		try {
			//组织应用列表取得
			appList = appWebService.getApps(Long.valueOf(groupId));
			if(CMyString.isEmpty(appId) && appList!=null && appList.size()==0){
				lAppId = ((AppInfo)appList.get(0)).getAppId();
			}else{
				lAppId = Long.valueOf(appId);
			}
			appInfo = (AppInfo) appWebService.findById(AppInfo.class,lAppId);//应用对象取得
			if("1".equals(flag) && appInfo.getIsNeedTheme()==1 && appInfo.getIsHasSmtDesc()==1){
				request.setAttribute("appId",lAppId);
				request.setAttribute("groupId",CMyString.filterForHTMLValue(groupId));
				request.setAttribute("dataId", dataId);
				request.setAttribute("type", "1");
				request.setAttribute("appInfo", appInfo);
				request.setAttribute("appList", appList);
				return "web/AppWeb/app_desc";
			}
			isHasComment = appInfo.getIsHasComment();
			List<Object> mainList = appWebService.queryAppFields(appInfo.getViewId(), 4, appInfo.getMainTableName());//查询主表前台展示字段
		    if(mainList==null || mainList.size()==0)
		    	throw new Exception("应用没有前台细览显示字段");
		    //fieldListStr = CMyString.join(((ArrayList<Object>)list), ",");
		    //数据的详细信息取得
		    dataDetail = appWebService.getAppMetadata(Long.valueOf(dataId), appInfo.getMainTableName());
		    if(dataDetail == null){
		    	request.setAttribute("Msg", "对不起，您查看的信件不存在！");
		    	return "web/AppWeb/webError"; //查询数据异常，跳转到提交失败页
		    }
		    if("1".equals(dataDetail.get("DELETED")) || "0".equals(dataDetail.get("ISPUBLIC"))){
		    	request.setAttribute("Msg", "对不起，您查看的信件不存在！");
		    	return "web/AppWeb/webError"; //查询数据异常，跳转到提交失败页
		    }
		    appWebService.addHitCount(appInfo.getMainTableName(), Long.valueOf(dataId));
		    String filedName = "";
			for (int i = 0; i < mainList.size(); i++) {
				List<Object> dataList = new ArrayList<Object>();
		    	filedName = ((AppFieldRel)mainList.get(i)).getFieldName();
		    	dataList.add(((AppFieldRel)mainList.get(i)).getFieldDesc());
		    	dataList.add(dataDetail.get(filedName));
		    	dataList.add(((AppFieldRel)mainList.get(i)).getFieldType());
		    	reDataList.add(dataList);
			}
			appInfoType = String.valueOf(appInfo.getIsNeedTheme());
			if("1".equals(appInfoType)){
				//获取意见的细览字段
				String swhere= " isWebShow=?  and inDetail =? ";
				List<Object> parameters = new ArrayList<Object>();
				parameters.add(1);
				parameters.add(1);
				List<Object> itemList = appWebService.queryAppFields(appInfo.getViewId(), appInfo.getItemTableName(),swhere,parameters);//查询从表的字段<概览字段+前台显示>
				//建议类信息取得
				page = getitemAppMetadata(appInfo, currPage, pageSize, Long.valueOf(dataId),itemList);
				fieldList = appWebService.getAppFields(appInfo.getViewId(),appInfo.getItemTableId(),0);
				request.setAttribute("fieldList",fieldList);
				request.setAttribute("itemList",itemList);
			}else if(isHasComment>0){
				//评论信息取得
				List<Object> commentParam = new ArrayList<Object>();
				String comWhere = " dataId = ? and appId = ? ";
				commentParam.add(dataId);
				commentParam.add(appId);
				if(isHasComment==2){
					comWhere+=" and commentStatus=?";
					commentParam.add(1);
				}
				page = appWebService.findPage(null, AppComment.class.getName(),comWhere, " crtime desc ", Integer.valueOf(currPage), Integer.valueOf(pageSize), commentParam);
			}
			//获取附件信息
			adixList = appWebService.getAppendixs(Long.valueOf(appId),Long.valueOf(dataId)); 
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("groupId",CMyString.filterForHTMLValue(groupId));
			return "web/AppWeb/webError"; //查询数据异常，跳转到提交失败页
		}
		request.setAttribute("adixList",adixList);
		request.setAttribute("appList",appList);
		request.setAttribute("dataDetail",reDataList);
		request.setAttribute("viewName",appInfo.getViewName());
		request.setAttribute("isHasComment",isHasComment==2?2:0);//是否显示共有评论
		request.setAttribute("appId",lAppId);
		request.setAttribute("groupId",CMyString.filterForHTMLValue(groupId));
		request.setAttribute("page",page);
		request.setAttribute("dataId", dataId);
		returnUrl = appInfo.getWebdetailAddr();
		if("1".equals(appInfoType)){
			endTime = (String) dataDetail.get("ENDTIME");
			if(CMyString.isEmpty(endTime)){
				request.setAttribute("isOld", 0);
			}else{
				request.setAttribute("isOld",DateUtil.compareNow(endTime));
			}
			//建议类信息
			return CMyString.showEmpty(returnUrl, "web/AppWeb/adviceData_detail");
		}else{
			//普通类信息
			return CMyString.showEmpty(returnUrl, "web/AppWeb/appData_detail");
		}
		
	}
	
	/**
	 * 
	* Description: 前台细览数据取得 <BR>   
	* @author jin.yu
	* @date 2014-4-28 下午02:47:20
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=appDataDetailSearch")
	public String appDataDetailSearch(HttpServletRequest request,HttpServletResponse res){
		String returnUrl = null;
		String appId = CMyString.showEmpty(request.getParameter("appId"), "0");//应用ID
		String searchCode = request.getParameter("searchCode");//查询编号
		String searchPassWord = request.getParameter("searchPassWord");//查询密码
		String groupId = request.getParameter("groupId");//部门ID
		String currPage = CMyString.isEmpty(request.getParameter("pageNum")) ? Global.DEFUALT_STARTPAGE
				: request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage")) ? "5"
				: request.getParameter("numPerPage");
		String sWhere = "";//查询条件
		String dataId = "";//数据ID
		Page page = null;
		List<Object> reDataList = new ArrayList<Object>();//返回结果List
		Long lAppId = 0l;//要取得数据的应用ID
		String fieldListStr = ""; //应用查询字段
		Map<String, Object> dataDetail = null;
		List<Object> appList = null;//组织应用列表
		List<Object> adixList = null;
		AppInfo appInfo = null;//应用对象
		int isHasComment = 0; //应用是否有评论
		List<Object> param = new ArrayList<Object>();//查询条件
		if(CMyString.isEmpty(groupId)){
			return "web/AppWeb/webError";
		}
		try {
			//组织应用列表取得
			appList = appWebService.getApps(Long.valueOf(groupId));
			if(CMyString.isEmpty(appId) && appList!=null && appList.size()>0){
				lAppId = ((AppInfo)appList.get(0)).getAppId();
			}else{
				lAppId = Long.valueOf(appId);
			}
			appInfo = (AppInfo) appWebService.findById(AppInfo.class,lAppId);//应用对象取得
			isHasComment = appInfo.getIsHasComment();
			List<Object> list = appWebService.queryAppFields(appInfo.getViewId(), 4, appInfo.getMainTableName());//查询前台展示字段
		    if(list==null || list.size()==0)
		    	throw new Exception("应用没有前台细览显示字段");
		    List<Object> fieldList = new ArrayList<Object>();
		    for (Object str : list) {
		    	fieldList.add(((AppFieldRel)str).getFieldName());
			}
		    fieldListStr = "METADATAID,"+CMyString.join(((ArrayList<Object>)fieldList), ",");
		    //查询条件设定
		    sWhere = " QUERYNUMBER = ? and QUERYPWD = ?"; 
		    param.add(searchCode);
		    param.add(searchPassWord);
		    //数据的详细信息取得
		    dataDetail = appWebService.getAppMetadata(appInfo.getMainTableName(), fieldListStr, sWhere, param);
		    if(dataDetail == null){
		    	request.setAttribute("Msg", "对不起，您查看的信件不存在！");
		    	return "web/AppWeb/webError"; //查询数据异常，跳转到提交失败页
		    }
		    if("1".equals(dataDetail.get("DELETED")) || "0".equals(dataDetail.get("ISPUBLIC"))){
		    	request.setAttribute("Msg", "对不起，您查看的信件不存在！");
		    	return "web/AppWeb/webError"; //查询数据异常，跳转到提交失败页
		    }
		    String filedName = "";
			dataId = (String)dataDetail.get("METADATAID");
			for (int i = 0; i < list.size(); i++) {
				List<Object> dataList = new ArrayList<Object>();
		    	filedName = ((AppFieldRel)list.get(i)).getFieldName();
		    	dataList.add(((AppFieldRel)list.get(i)).getFieldDesc());
		    	dataList.add(dataDetail.get(filedName));
		    	dataList.add(((AppFieldRel)list.get(i)).getFieldType());
		    	reDataList.add(dataList);
			}
			if(isHasComment>0){
				//评论信息取得
				List<Object> commentParam = new ArrayList<Object>();
				String comWhere = " dataId = ? and appId = ? ";
				commentParam.add(dataId);
				commentParam.add(appId);
				if(isHasComment==2){
					comWhere+=" and commentStatus=?";
					commentParam.add(1);
				}
				page = appWebService.findPage(null, AppComment.class.getName(),comWhere, " crtime desc ",
						Integer.valueOf(currPage), Integer.valueOf(pageSize), commentParam);
			}
			//获取附件信息
			adixList = appWebService.getAppendixs(Long.valueOf(appId),Long.valueOf(dataId)); 
		} catch (Exception e) {
			e.printStackTrace();
			return "web/AppWeb/webError"; //查询数据异常，跳转到提交失败页
		}
		request.setAttribute("adixList",adixList);
		request.setAttribute("appList",appList);
		request.setAttribute("dataDetail",reDataList);
		request.setAttribute("viewName",appInfo.getViewName());
		request.setAttribute("isSerach",1);//是否显示私有评论
		request.setAttribute("isHasComment",isHasComment);//是否显示私有评论
		request.setAttribute("appId",lAppId);
		request.setAttribute("groupId",groupId);
		request.setAttribute("dataId", dataId);
		request.setAttribute("page", page);
		returnUrl = appInfo.getWebdetailAddr();
		return CMyString.showEmpty(returnUrl, "web/AppWeb/appData_detail");
	}
	
	/** 
	* Description: 取标题字段 <BR>   
	* author liujian
	* date 2014-3-29 上午10:59:20
	* @param fieldList
	* @return
	* version 1.0
	*/
	public static String getTitleField(List<Object> fieldList){
		String str = null;		
		for(Object obj : fieldList){
			AppFieldRel appFieldInfo =(AppFieldRel)obj;
			if(appFieldInfo.getTitleField()==1){
				str = appFieldInfo.getFieldName();
				break;
			}
		}
		
		return str;
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
		
		return str.toString().length()>0?str.toString().substring(0,str.toString().length()-1):"";
	}
	
	/**
	 * 
	* Description: 根据建议类应用的数据Id取得建议类的意见信息 <BR>   
	* @author jin.yu
	* @date 2014-4-28 下午08:18:56
	* @param appInfo 应用ID	
	* @param currPage 开始页数
	* @param pageSize 每页几条
	* @param theneId  主题id
	* @param itemList  意见细览显示字段列表
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public Page getitemAppMetadata(AppInfo appInfo ,String currPage ,String pageSize ,Long theneId,List<Object> itemList) throws Exception{
		Page page = null;
		String fieldListStr = ""; //建议类应用查询字段

		if(itemList==null || itemList.size()==0)
	    	throw new Exception("建议类应用没有设置前台细览显示字段");
	    List<Object> fieldList = new ArrayList<Object>();
	    for (Object str : itemList) {
	    	fieldList.add(((AppFieldRel)str).getFieldName());
		}
	    fieldListStr = CMyString.join(((ArrayList<Object>)fieldList), ",");
	    List<Object> param = new ArrayList<Object>();
	    param.add(theneId);
	    param.add(1);
	    page = appWebService.queryAppMetadatas(appInfo.getItemTableName(), fieldListStr,
				" THEMEID = ? and ISPUBLIC=?", param, "crtime desc", Integer.parseInt(currPage),Integer.parseInt(pageSize));
	    return page;
	}
	/**
	 * 
	* Description:下载文件 <BR>   
	* @author zhangzun
	* @date 2014-5-5 下午05:23:11
	* @param request
	* @param res
	* @version 1.0
	 */
	@RequestMapping(params = "method=downLoadFile")
	public String downLoadFile(HttpServletRequest request,HttpServletResponse res){
		String flag = request.getParameter("flag"); 
		String appendixId = request.getParameter("appendixId");
		String srcfile = "";   //文件名
		String downLoadName = ""; //下载名称
		String basePath = "";      //附件保存的根路径
		String fileFullPath = ""; //附件全路径名 
		String sMsg = "";
		// 打开指定文件的流信息 
		FileInputStream fileInputStream = null;
		if(CMyString.isEmpty(appendixId)){
			sMsg = "附件下载传入的文件id不能为空！";
		}
		if(CMyString.isEmpty(downLoadName))
			downLoadName = srcfile;
		//获取附件保存的环境变量
		try{
			AppAppendix adix = (AppAppendix) appWebService.findById(AppAppendix.class,Long.valueOf(appendixId));
			srcfile = adix.getSrcfile();
			downLoadName = adix.getFileName();
			AppSysConfig sysConfig= (AppSysConfig)appWebService.findObject(AppSysConfig.class.getName(),
					"configName=?",Global.APP_UPLOAD_PATH);
			basePath = sysConfig.getConfigValue();
			//basePath = "E:\\";
			fileFullPath = UploadFileUtil.getFileFullPath(basePath, srcfile);
			downLoadName = new String(downLoadName.getBytes("GB2312"), "ISO-8859-1");
			fileInputStream = new FileInputStream(fileFullPath);
			// 设置响应头和下载保存的文件名
			res.reset();
			res.setContentType("APPLICATION/OCTET-STREAM");
			res.setHeader("Content-Disposition", "attachment; filename=\"" + downLoadName + "\"");
			FileCopyUtils.copy(fileInputStream, res.getOutputStream());
		}catch(Exception ex){
			LOG.error("下载附件发生异常！",ex);
			sMsg = "下载附件失败！";
		}
		if(!CMyString.isEmpty(sMsg) && "0".equals(flag)){
			 request.setAttribute("Msg", sMsg);
			 return "web/AppWeb/webError"; //参数异常，跳转到错误页
		}else if(!CMyString.isEmpty(sMsg) && "1".equals(flag)){
			res.setCharacterEncoding("UTF-16");  
			try {
				res.getWriter().print(sMsg);
				res.getWriter().flush();  
				res.getOutputStream().close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}  
		}
		return null;
	}
	/**
	 *
	* Description: 查询应用的分组字段<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-23 上午11:07:07
	* Last Modified:
	* @param viewId 试图ID
	* @return  List<Object> isGrpFields  分组字段 AppFieldRel 对象
	* @version 1.0
	 */
	public List<Object> isgrp(Long viewId){
		//查询分组字段
		List<Object> isGrpFields = new ArrayList<Object>();
		try {
			isGrpFields = appWebService.find(null, AppFieldRel.class.getName(), "isGrpField =1 and viewId =? ", "fieldOrder", viewId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isGrpFields;
	}
	
	/**
	* Description:返回当前视图对应的提交分组后的字段信息<BR>  
	* @author jin.yu
	* @date Create: 2014-9-23 上午11:25:58
	* Last Modified:
	* @param viewId  视图编号
	* @param grpName  分组ming
	* @return  List<Object>  appRelGroups 返回对象为  AppRelGroup 
	* @version 1.0
	 */
	public List<Object>  findTjGrpfields(Long  viewId,String grpName){
		List<Object> whereList = new ArrayList<Object>();
		List<Object> appRelGroups = new ArrayList<Object>();
		whereList.add(viewId);
		whereList.add(grpName);
		try {
			appRelGroups = appWebService.find(null, AppRelGroup.class.getName(), "viewId =? and grpName = ? ", "", whereList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appRelGroups;
	}
	
	/**
	* Description:返回当前视图对应的所有分组后的字段信息<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-23 上午11:25:58
	* Last Modified:
	* @param viewId  视图编号
	* @return  List<Object>  appRelGroups 返回对象为  AppRelGroup 
	* @version 1.0
	 */
	public List<Object>  findGrpfields(Long  viewId){
		List<Object> appRelGroups = new ArrayList<Object>();
		try {
			appRelGroups = appWebService.find(null, AppRelGroup.class.getName(), "viewId =? ", "", viewId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appRelGroups;
	}
	/** 
	* Description: 跳转到不通的应用页面 <BR>   
	* author liujian
	* date 2014-5-23 上午10:08:49
	* @param request
	* @param res
	* @return
	* @throws Exception
	* version 1.0
	*/
	@RequestMapping(params = "method=goAppPage")
	public String goAppPage(HttpServletRequest request,HttpServletResponse res) throws Exception{
		
		return "";
	}
}
