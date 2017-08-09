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
import com.trs.model.AppComment;
import com.trs.model.AppFieldRel;
import com.trs.model.AppInfo;
import com.trs.model.AppRecordLock;
import com.trs.model.AppUser;
import com.trs.service.AppCommentService;
import com.trs.service.PublicAppBaseService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.DateUtil;
import com.trs.util.Global;

/**
 * Description: 用于评论和回帖信息处理<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: AppCommmentController
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-4-18 下午03:51:32
 * version 1.0
 */
@Controller
@RequestMapping("post.do")
public class AppCommentController {
	private static  Logger LOG =  Logger.getLogger(AppCommentController.class);
	@Autowired
	private AppCommentService appCommentService;
	@Autowired
	private PublicAppBaseService publicAppBaseService;
	/** 
	* Description: 获取评论数据列表 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=appPostList")
	public String appPostList(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");//应用ID--必传字段
		String themeId = request.getParameter("themeId");//主题ID--根据主表中的主题Id查询相关从表数据
		String status = CMyString.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");//数据状态 0:待处理，1处理中，2已处理
		String isPublic = CMyString.isEmpty(request.getParameter("isPublic"))?"0":request.getParameter("isPublic");//数据状态 0:待审核，1：已审核
		String deleted = CMyString.isEmpty(request.getParameter("deleted"))?"0":request.getParameter("deleted");//数据状态 0:未删除，1-已删除
		String selectFiled = request.getParameter("selectFiled");	//检索字段
		String sFiledValue = request.getParameter("sFiledValue");	//检索字段值
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");//当前页数
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"20":request.getParameter("numPerPage");//每页显示条数
		String orderField = CMyString.isEmpty(request.getParameter("orderField"))?"":request.getParameter("orderField");//排序字段
		String orderDirection = CMyString.isEmpty(request.getParameter("orderDirection"))?"desc":request.getParameter("orderDirection");//排序字段顺序标识
		String tableType = CMyString.isEmpty(request.getParameter("tableType"))?"1":request.getParameter("tableType");//表类型：0-主表，1-回帖表 ***数据相关必传字段
		
		String sWhere ="1 = 1";//拼接检索条件
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
			return "/appadmin/appMgr/postData_List";
		}
		
		try {
			appInfo = (AppInfo)publicAppBaseService.findById(AppInfo.class, Long.valueOf(appId));
			searchFieldList = publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.valueOf(appId), 2, Integer.parseInt(tableType));//取出检索字段
			fieldList = publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.valueOf(appId), 1, Integer.parseInt(tableType));//取出概览字段
			String fieldListStr = splitListFields(fieldList,"am.METADATAID");//以逗号分隔的字符串,默认无工作流
			
			tableName = publicAppBaseService.getMetaTableName(Long.valueOf(appId), Integer.parseInt(tableType));
			request.setAttribute("tableName",tableName);
			tableName = tableName+" am";
			
			sWhere = sWhere+" and am.appId = ?";
			param.add(appId);
			if(!CMyString.isEmpty(themeId) && "1".equals(tableType)){
				sWhere =sWhere+" and am.themeId = ? ";
				param.add(themeId);
			}
				
			if(!CMyString.isEmpty(isPublic)&&"0".equals(deleted)){
				sWhere =sWhere+" and am.ISPUBLIC = ?";
				param.add(isPublic);
			}

			if(!CMyString.isEmpty(deleted)){
				sWhere =sWhere+" and am.DELETED = ?";
				param.add(deleted);
			}
			if(!CMyString.isEmpty(sFiledValue)){
				sWhere =sWhere+" and am."+selectFiled+" like ?";
				param.add("%"+sFiledValue+"%");
			}

			if(!CMyString.isEmpty(orderField)&&!CMyString.isEmpty(orderDirection)){
				sOrder = "am."+orderField+" "+orderDirection;
			}
			page = publicAppBaseService.queryAppInfos(tableName, fieldListStr, sWhere, param, sOrder, Integer.parseInt(currPage),Integer.parseInt(pageSize));

				
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
		request.setAttribute("isPublic",isPublic);
		request.setAttribute("deleted",deleted);
		request.setAttribute("appInfo",appInfo);
		request.setAttribute("themeId", themeId);
		request.setAttribute("page",page);
		request.setAttribute("dataList", page.getLdata());
		request.setAttribute("selectFiled",selectFiled);
		request.setAttribute("sFiledValue",sFiledValue);
		request.setAttribute("orderField",orderField);
		request.setAttribute("orderDirection",orderDirection);
		return "/appadmin/appMgr/postData_List";
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
		String returnUrl = "/appadmin/appMgr/app_reply_add";
		try {
			String themeId = request.getParameter("themeId");//主题编号
			request.setAttribute("themeId", themeId);
			AppUser loginUser = (AppUser)	CrtlUtil.getCurrentUser(request);
			int reply = 1;
			AppInfo appInfo = null;
			String metadataid = request.getParameter("metadataid");
			String appId = request.getParameter("appId");
			request.setAttribute("appId", appId);
			appInfo = (AppInfo)publicAppBaseService.findById(AppInfo.class, Long.valueOf(appId));
			request.setAttribute("appInfo", appInfo);
			if(CMyString.isEmpty(metadataid)){//新建
				//从表
				List<Object>  appFieldRels =  publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.parseLong(appId), -1, reply);
				request.setAttribute("appFieldRels", appFieldRels);
			}else{//修改
				//从表
				List<Object>  appFieldRels =  publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.parseLong(appId), -1, reply);
				request.setAttribute("appFieldRels", appFieldRels);
				//如果appid 和 metadataid 为空或不是一个有效的整数则需要抛出异常
				String tableName = publicAppBaseService.getMetaTableName(Long.parseLong(appId), reply);//元数据表名
				//元数据ID对应的单个信息
				Map<String, Object> metaDataMap = publicAppBaseService.getAppMedataInfo(Long.parseLong(metadataid), tableName);
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
				request.setAttribute("metaDataMap", metaDataMap);
				returnUrl="appadmin/appMgr/app_reply_edit";
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnUrl;
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
			int reply = 1;
		AppUser loginUser = (AppUser)	CrtlUtil.getCurrentUser(request);
		Map<String, Object> metaDataAppInfo = new HashMap<String, Object>();
		String metadataid = request.getParameter("metadataid");
		String themeId = request.getParameter("themeId");//主题编号
		String appId = request.getParameter("appId");
			if(CMyString.isEmpty(metadataid)){//新建
				//查询主表字段
				List<Object>  fieldList =  publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.parseLong(appId), -1, reply);
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
				metaDataAppInfo.put("THEMEID", themeId);
				//查询元数据表名
				String tableName = publicAppBaseService.getMetaTableName(Long.parseLong(appId), reply);
//					Long curr_metaid = publicAppBaseService.saveOrUpdateMetadata(metaDataAppInfo, tableName);//获取刚保存的文档ID
					publicAppBaseService.saveOrUpdateMetadata(metaDataAppInfo, tableName);
				
			}else{//修改
				//查询主表字段
				List<Object>  fieldList =  publicAppBaseService.queryAppFields(loginUser.getUserId(), Long.parseLong(appId), -1, reply);
				metaDataAppInfo.put("METADATAID", metadataid);
				for(Object field : fieldList){
					AppFieldRel appFieldRel =(AppFieldRel)field;
					if(appFieldRel.getFieldType().equals(Global.FIELD_TYPE_CHECKBOX)){//多选框特殊处理
						String [] checks = request.getParameterValues(appFieldRel.getFieldName());
						metaDataAppInfo.put(appFieldRel.getFieldName(), splitChecks(checks, appFieldRel.getDefaultValue()));
					}else{
						if(appFieldRel.getFieldType().equals("date")){
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
//				metaDataAppInfo.remove("CRUSER");
//				metaDataAppInfo.put("CRUSER", loginUser.getUsername());
				metaDataAppInfo.put("THEMEID", themeId);

				//查询元数据表名
				String tableName = publicAppBaseService.getMetaTableName(Long.parseLong(appId), reply);
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
	/** 
	* Description: 删除或恢复选中的回帖数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=delPosts")
	@ResponseBody
	public String delPosts(HttpServletRequest request,HttpServletResponse res){
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
	* Description: 公开或不公开选中的回帖数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=isPublic")
	@ResponseBody
	public String isPublic(HttpServletRequest request,HttpServletResponse res){
		String isPublic = CMyString.isEmpty(request.getParameter("isPublic"))?"0":request.getParameter("isPublic");//公开或不公开的标识：1-公开  ，0-不公开
		String tableName = request.getParameter("tableName");//元数据表名
		String dataIds = request.getParameter("dataIds");//数据ID字符串，以","隔开
		String arrIds[] = CMyString.split(dataIds, ",");
		String msg = "不公开";
		
		if(!"0".equals(isPublic)){
			msg = "公开";
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
			appinfo.put("ISPUBLIC", isPublic);
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
	* Description: 取评论列表信息 <BR>   
	* author liujian
	* date 2014-4-18 下午05:07:01
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=commentList")
	public String commentList(HttpServletRequest request,HttpServletResponse res) {
		String appId = request.getParameter("appId");//应用ID--必传字段
		String dataId = request.getParameter("dataId");//数据ID--必传字段
		String commentStatus = CMyString.isEmpty(request.getParameter("commentStatus"))?"0":request.getParameter("commentStatus");//数据状态 0:待处理，1已处理，2已删除
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"20":request.getParameter("numPerPage");//每页显示条数
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");//当前页数
		String sOrder="crtime desc"; //排序条件
		Page page = null;
		if(!CMyString.isEmpty(appId) && !CMyString.isEmpty(dataId)){
			try {
				page = appCommentService.getAppToComments(Long.valueOf(appId),Long.valueOf(dataId),Integer.parseInt(commentStatus),sOrder,Integer.parseInt(currPage),Integer.parseInt(pageSize));
			} catch (NumberFormatException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("page", page);
		request.setAttribute("dataList", page.getLdata());
		request.setAttribute("appId", appId);
		request.setAttribute("dataId", dataId);
		request.setAttribute("commentStatus", commentStatus);
		
		
		return "/appadmin/appMgr/comment_List";
	}
	/** 
	* Description: 跳转到新建评论页 <BR>   
	* author liujian
	* date 2014-4-18 下午05:07:01
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=goAddComment")
	public String addComment(HttpServletRequest request,HttpServletResponse res) {
		String appId = request.getParameter("appId");//应用ID
		String dataId = request.getParameter("dataId");//信件编号
		request.setAttribute("appId", appId);
		request.setAttribute("dataId", dataId);
		return "/appadmin/appMgr/comment_add";
	}
	/** 
	* Description: 更新(审核)的评论数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=addComments")
	@ResponseBody
	public String addComments(HttpServletRequest request,HttpServletResponse res){
		String appId = request.getParameter("appId");//应用ID
		String dataId = request.getParameter("dataId");//信件编号
		String commentContent = request.getParameter("commentContent");//评论内容
		String commentScore = request.getParameter("commentScore");//评分数
		String commentUser = CMyString.isEmpty(request.getParameter("commentUser"))?"匿名":request.getParameter("commentUser");
		System.out.println(commentScore);
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("新建信息失败！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		AppComment appComment = new AppComment();
			try {
					appComment.setAppId(Long.valueOf(appId));
					appComment.setDataId(Long.valueOf(dataId));
					appComment.setCommentContent(commentContent);
					appComment.setCommentUser(commentUser);
					appComment.setCruser(commentUser);
					appComment.setCommentScore(Short.valueOf(commentScore));
					appComment.setCommentStatus(0);
					appCommentService.saveAppComment(appComment);
					josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("新建信息成功！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

		return josnStr;
	}
	/** 
	* Description: 删除或恢复选中的评论数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=delComments")
	@ResponseBody
	public String delComments(HttpServletRequest request,HttpServletResponse res){
		String commentStatus = CMyString.isEmpty(request.getParameter("commentStatus"))?"0":request.getParameter("commentStatus");//删除或恢复的标识：2-删除  ，0-恢复
		String commentIds = request.getParameter("commentIds");//数据ID字符串，以","隔开
		String arrIds[] = CMyString.split(commentIds, ",");
		String msg = "删除";
		
		if(!"2".equals(commentStatus)){
			msg = "恢复";
		}
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii(msg+"数据失败！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
		List<Object> list = new ArrayList<Object>();
		if(CMyString.isEmpty(commentIds)){
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii(msg+"数据失败！没有传入数据ID！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			LOG.error(msg+"数据时没有传入数据ID！");
			return josnStr;
		}
		AppComment appComment = null;
				
			try {
				for(int i=0;i<arrIds.length;i++){
					appComment = appCommentService.getComment(Long.valueOf(arrIds[i]));
					appComment.setCommentStatus(Integer.parseInt(commentStatus));
					list.add(appComment);
				}
				appCommentService.updateAppComments(list);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii(msg+"数据成功！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		return josnStr;
	}
	/** 
	* Description: 更新(审核)的评论数据 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=upComments")
	@ResponseBody
	public String upComments(HttpServletRequest request,HttpServletResponse res){
		
		String commentIds = request.getParameter("commentIds");//数据ID
		String commentContent = request.getParameter("commentContent");//评论内容
		String commentStatus = CMyString.isEmpty(request.getParameter("commentStatus"))?"0":request.getParameter("commentStatus");//审核状态：2-删除  ，0-待审核   1-已审核

		
		
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("审核信息失败！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		List<Object> list = new ArrayList<Object>();
		if(CMyString.isEmpty(commentIds)){
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("审核信息失败,缺少相关参数！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			LOG.error("审核数据时没有传入数据ID！");
			return josnStr;
		}
		AppComment appComment = null;
								
			try {
					appComment = appCommentService.getComment(Long.valueOf(commentIds));
					appComment.setCommentStatus(Integer.parseInt(commentStatus));
					appComment.setCommentContent(commentContent);
					list.add(appComment);
					appCommentService.updateAppComments(list);
					josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("审核信息成功！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

		return josnStr;
	}
	/** 
	* Description: 取的评论数据信息 <BR>   
	* author liujian
	* date 2014-3-27 下午04:36:38
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=getCommentInfo")
	public String getCommentInfo(HttpServletRequest request,HttpServletResponse res){
		String commentIds = request.getParameter("commentIds");//数据ID字符串，以","隔开
		
		AppComment appComment = null;
		if(!CMyString.isEmpty(commentIds)){
			try {
				appComment = appCommentService.getComment(Long.valueOf(commentIds));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}					
		request.setAttribute("appComment", appComment);
		return "/appadmin/appMgr/comment_eddit";
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
		str.append("crtime");
		return str.toString().length()>0?str.toString().substring(0,str.toString().length()):"";
	}

}
