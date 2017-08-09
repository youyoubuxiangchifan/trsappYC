package com.trs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.dao.AppBaseDao;
import com.trs.dbhibernate.Page;
import com.trs.model.AppFieldInfo;
import com.trs.model.AppFieldRel;
import com.trs.model.AppFlow;
import com.trs.model.AppGroup;
import com.trs.model.AppInfo;
import com.trs.model.AppRelGroup;
import com.trs.model.AppSyncField;
import com.trs.model.AppSysConfig;
import com.trs.model.AppTableInfo;
import com.trs.model.AppUser;
import com.trs.model.AppViewInfo;
import com.trs.model.TreeNode;
import com.trs.service.AppBaseService;
import com.trs.service.AppGroupService;
import com.trs.service.AppLogService;
import com.trs.service.AppSysConfigService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.Global;
import com.trs.util.jsonUtil;

/**
 * 
 * @Description: 处理应用管理的请求<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author zhangzun
 * @ClassName: AppController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-28 上午10:05:07
 * @version 1.0
 */
@Controller
@RequestMapping("/appView.do")
public class AppViewController {
	private static  Logger loger =  Logger.getLogger(RoleController.class);
	@Autowired
	AppBaseService appBaseService;
	@Autowired
	AppGroupService appGroupService;
	@Autowired
	private AppLogService logService;
	@Autowired
	private AppBaseDao appBaseDao;
	@Autowired
	private AppSysConfigService sysConfigService;
	/**
	 * 查询应用的视图信息
	 * Description:查询应用的视图信息 <BR>
	 * @author zhangzun
	 * @date 2014-3-28 上午10:58:01
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=list")
	public String list(HttpServletRequest request, HttpServletResponse res) {
		String currPage = CMyString.isEmpty(request.getParameter("pageNum")) ? Global.DEFUALT_STARTPAGE
				: request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage")) ? Global.DEFUALT_PAGESIZE
				: request.getParameter("numPerPage");
		Page page = null;
		String sWhere = "deleted=0";
		try {
			page = appBaseService.findPage("", AppViewInfo.class.getName(), sWhere,
					"crtime desc", Integer.valueOf(currPage), Integer
							.valueOf(pageSize), null);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询应用的视图信息列表失败。",e);
		}
		request.setAttribute("page", page);
		request.setAttribute("viewList", page.getLdata());
		return "appadmin/app/view_list";
	}
	/**
	  * 
	 * Description:校验视图名称的唯一性 <BR>   
	 * @author zhangzun
	 * @date 2014-3-24 下午09:16:53
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	  */
	 @RequestMapping(params = "method=check")
	 @ResponseBody
	 public String check(HttpServletRequest request,HttpServletResponse res){
		 String sViewName = CMyString.showEmpty(request.getParameter("viewName"),"");
		 String sWhere = "viewName=?";
		 boolean isExite = true;
		 try {
			isExite = appBaseService.existData(AppViewInfo.class.getName(), sWhere, sViewName);
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("校验视图名称的唯一性失败。",e);
		}
		return isExite+"";
	 }
	/**
	 *  添加视图
	 * Description:  添加视图<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=add")
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse res) {
		String sViewName = request.getParameter("viewName");
		String sIsNeedTheme = CMyString.showEmpty(request.getParameter("isNeedTheme"), "0");
		String sAppStatus = CMyString.showEmpty(request.getParameter("appStatus"), "0");
		String sIsPublic = CMyString.showEmpty(request.getParameter("isPublic"), "0");
		String sIsHasQueryNo = CMyString.showEmpty(request.getParameter("isHasQueryNo"), "0");
		String sIsHasComment = CMyString.showEmpty(request.getParameter("isHasComment"), "0");
		String sIsEmailWarn = CMyString.showEmpty(request.getParameter("isEmailWarn"), "0");
		String sIsSmsWarn = CMyString.showEmpty(request.getParameter("isSmsWarn"), "0");
		String sIsSmsRemind = CMyString.showEmpty(request.getParameter("isSmsRemind"), "0");
		String sIsPush = CMyString.showEmpty(request.getParameter("isPush"), "0");
		String sWcmDocType = CMyString.showEmpty(request.getParameter("wcmDocType"), "0");
		String sWcmChnlId = CMyString.showEmpty(request.getParameter("wcmChnlId"), "0");
		String sIsShowGroup = CMyString.showEmpty(request.getParameter("isShowGroup"), "0");
		String sLimitDayNum = CMyString.showEmpty(request.getParameter("limitDayNum"), "0");
		String sIsSupAppendix = CMyString.showEmpty(request.getParameter("isSupAppendix"), "0");
		String sIsHasSmtDesc = CMyString.showEmpty(request.getParameter("isHasSmtDesc"), "0");
		String sSmtDesc = CMyString.showEmpty(request.getParameter("smtDesc"), "");
		String sListAddr = CMyString.showEmpty(request.getParameter("listAddr"), "");
		String sDowithAddr = CMyString.showEmpty(request.getParameter("dowithAddr"), "");
		String sGroupIds = request.getParameter("groupIds");
		String sJson = "";
		try {
			AppUser user = CrtlUtil.getCurrentUser(request);
			AppViewInfo viewInfo = new AppViewInfo();
			viewInfo.setViewName(sViewName);
			viewInfo.setIsNeedTheme(Integer.valueOf(sIsNeedTheme));
			viewInfo.setAppStatus(Integer.valueOf(sAppStatus));
			viewInfo.setIsPublic(Integer.valueOf(sIsPublic));
			viewInfo.setIsHasQueryNo(Integer.valueOf(sIsHasQueryNo));
			viewInfo.setIsHasComment(Integer.valueOf(sIsHasComment));
			viewInfo.setIsEmailWarn(Integer.valueOf(sIsEmailWarn));
			viewInfo.setIsSmsWarn(Integer.valueOf(sIsSmsWarn));
			viewInfo.setIsSmsRemind(Integer.valueOf(sIsSmsRemind));
			viewInfo.setLimitDayNum(Integer.valueOf(sLimitDayNum));
			viewInfo.setIsShowGroup(Integer.valueOf(sIsShowGroup));
			viewInfo.setIsPush(Integer.valueOf(sIsPush));
			viewInfo.setCruser(user.getUsername());
			viewInfo.setIsHasSmtDesc(Integer.valueOf(sIsHasSmtDesc));
			viewInfo.setListAddr(sListAddr);
			viewInfo.setDowithAddr(sDowithAddr);
			if("1".equals(sIsPush)){
				viewInfo.setWcmDocType(Integer.parseInt(sWcmDocType));
				viewInfo.setWcmChnlId(Long.valueOf(sWcmChnlId));
			}
			if("1".equals(sIsHasSmtDesc) && !CMyString.isEmpty(sSmtDesc)){
				viewInfo.setSmtDesc(sSmtDesc);
			}
			viewInfo.setDeleted(0);
			viewInfo.setIsSupAppendix(Integer.valueOf(sIsSupAppendix));
			appBaseService.addAppViewInfo(viewInfo, sGroupIds, user);
//			String param = "&viewId="+viewInfo.getViewId();
//			mservice.createAppTable(tableInfo, user);
			sJson = jsonUtil.getJsonStr("200", "操作成功！","closeCurrent", "", "");
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功添加了名为").append(viewInfo.getViewName()).append("的视图信息。");
			logService.addAppLog(1, logMsg.toString(), user);
		} catch (Exception e) {
//			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "closeCurrent",
//					"");
			e.printStackTrace();
			loger.error("添加视图信息成功。",e);
		}
		return sJson;
	}
	/**
	 *  修改视图
	 * Description:  修改视图<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=edit")
	@ResponseBody
	public String edit(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		String sViewId = CMyString.showEmpty(request.getParameter("viewId"), "0");
		//String sViewName = request.getParameter("viewName");视图名称不允许修改
		String sIsNeedTheme = request.getParameter("isNeedTheme");
		String sAppStatus = CMyString.showEmpty(request.getParameter("appStatus"), "0");
		String sIsPublic = CMyString.showEmpty(request.getParameter("isPublic"), "0");
		String sIsHasQueryNo = CMyString.showEmpty(request.getParameter("isHasQueryNo"), "0");
		String sIsHasComment = CMyString.showEmpty(request.getParameter("isHasComment"), "0");
		String sIsEmailWarn = CMyString.showEmpty(request.getParameter("isEmailWarn"), "0");
		String sIsSmsWarn = CMyString.showEmpty(request.getParameter("isSmsWarn"), "0");
		String sIsSmsRemind = CMyString.showEmpty(request.getParameter("isSmsRemind"), "0");
		String sIsPush = CMyString.showEmpty(request.getParameter("isPush"), "0");
		String sWcmDocType = CMyString.showEmpty(request.getParameter("wcmDocType"), "0");
		String sWcmChnlId = CMyString.showEmpty(request.getParameter("wcmChnlId"), "0");
		String sIsShowGroup = CMyString.showEmpty(request.getParameter("isShowGroup"), "0");
		String sGroupIds = request.getParameter("groupIds");
		String sLimitDayNum = CMyString.showEmpty(request.getParameter("limitDayNum"), "0");
		String sIsSupAppendix = CMyString.showEmpty(request.getParameter("isSupAppendix"), "0");
		String sIsHasSmtDesc = CMyString.showEmpty(request.getParameter("isHasSmtDesc"), "0");
		String sSmtDesc = CMyString.showEmpty(request.getParameter("smtDesc"), "");
		String sListAddr = CMyString.showEmpty(request.getParameter("listAddr"), "");
		String sDowithAddr = CMyString.showEmpty(request.getParameter("dowithAddr"), "");
		AppViewInfo viewInfo = (AppViewInfo)appBaseService.findById(AppViewInfo.class, Long.valueOf(sViewId));//new AppViewInfo();
		if(viewInfo == null){
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "closeCurrent", "");
			return sJson;
		}
		//viewInfo.setViewName(sViewName);
		if(!CMyString.isEmpty(sIsNeedTheme))
			viewInfo.setIsNeedTheme(Integer.valueOf(sIsNeedTheme));
		viewInfo.setAppStatus(Integer.valueOf(sAppStatus));
		viewInfo.setIsPublic(Integer.valueOf(sIsPublic));
		viewInfo.setIsHasQueryNo(Integer.valueOf(sIsHasQueryNo));
		viewInfo.setIsHasComment(Integer.valueOf(sIsHasComment));
		viewInfo.setIsEmailWarn(Integer.valueOf(sIsEmailWarn));
		viewInfo.setIsSmsWarn(Integer.valueOf(sIsSmsWarn));
		viewInfo.setIsSmsRemind(Integer.valueOf(sIsSmsRemind));
		viewInfo.setIsShowGroup(Integer.valueOf(sIsShowGroup));
		viewInfo.setIsPush(Integer.valueOf(sIsPush));
		viewInfo.setLimitDayNum(Integer.valueOf(sLimitDayNum));
		viewInfo.setIsSupAppendix(Integer.valueOf(sIsSupAppendix));
		viewInfo.setIsHasSmtDesc(Integer.valueOf(sIsHasSmtDesc));
		viewInfo.setListAddr(sListAddr);
		viewInfo.setDowithAddr(sDowithAddr);
		if("1".equals(sIsPush)){
			viewInfo.setWcmDocType(Integer.parseInt(sWcmDocType));
			viewInfo.setWcmChnlId(Long.valueOf(sWcmChnlId));
		}
		if("1".equals(sIsHasSmtDesc) && !CMyString.isEmpty(sSmtDesc)){
			viewInfo.setSmtDesc(sSmtDesc);
		}
		AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
		try {
			appBaseService.updateAppViewInfo(viewInfo, sGroupIds, user);
//			mservice.createAppTable(tableInfo, user);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "", "", "closeCurrent", "");
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功修改了名为").append(viewInfo.getViewName()).append("的视图信息。");
			logService.addAppLog(2, logMsg.toString(),user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "closeCurrent", "");
			e.printStackTrace();
			loger.error("修改视图信息失败。",e);
		}
		return sJson;
	}
	/**
	 *  删除视图
	 * Description:  删除视图<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=del")
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		String sViewId = CMyString.showEmpty(request.getParameter("viewId"), "0");
		long nViewId = Long.valueOf(sViewId);
		if(nViewId == 0){
			sJson = jsonUtil.getJsonStr("300", "操作失败！视图编号没有传人！", "", "", "", "");
			return sJson;
		}
		AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
		try {
			AppViewInfo viewInfo = (AppViewInfo)appBaseService.findById(AppViewInfo.class, Long.valueOf(sViewId));
			appBaseService.deleteAppViewInfo(nViewId, 0, user);
//			mservice.createAppTable(tableInfo, user);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "", "", "", "");
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功删除了名为").append(viewInfo.getViewName()).append("的视图信息。");
			logService.addAppLog(3, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "",
					"");
			e.printStackTrace();
			loger.error("删除视图信息失败。",e);
		}
		return sJson;
	}
	/**
	 * 
	* Description:获取视图信息的编辑页面<BR>   
	* @author zhangzun
	* @date 2014-3-28 下午02:19:10
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=viewEdite")
	public String viewPage(HttpServletRequest request,HttpServletResponse res){
		String sId = CMyString.showEmpty(request.getParameter("id"),"0");//视图id
		AppViewInfo viewInfo = null;
		String groupIds = "";
		try {
			viewInfo = (AppViewInfo) appBaseService.findById(AppViewInfo.class, Long.valueOf(sId));
			groupIds = appBaseService.getGroupIdsByViewId(Long.valueOf(sId));
			if(viewInfo==null){
				request.setAttribute("method","add");
			}else{
				request.setAttribute("method","edit");
			}
		} catch (Exception e) {
				e.printStackTrace();
				loger.error("查询视图的所属组织失败。", e);
			}
		request.setAttribute("view", viewInfo);
		request.setAttribute("groupIds", groupIds == null ? "" : groupIds);
		return "appadmin/app/view_edite";
	}
	/**
	 * 刷新字段列表
	 * Description:  <BR>   
	 * @author liu.zhuan
	 * @date 2014-3-29 下午06:02:14
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=refFeildList")
	public String refFeildList(HttpServletRequest request,HttpServletResponse res){
		try {
			long tableId = Long.valueOf(CMyString.showEmpty(request.getParameter("tableId"), "0"));
			List<Object> fieldList = appBaseService.find("new map(fieldId as fieldId, fieldName as fieldName, fieldDesc as fieldDesc, isReserved as isReserved, fieldType as fieldType)", AppFieldInfo.class.getName(), "tableId = ?", "", tableId);
			request.setAttribute("fieldList", fieldList);
			request.setAttribute("rel", request.getParameter("rel"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("刷新视图字段列表成功。",e);
		}
		return "appadmin/app/refFeildList";
	}
	/**
	 * 初始化字段维护列表
	 * Description:  初始化字段维护<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-29 下午06:00:48
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=buildViewField")
	public String buildViewField(HttpServletRequest request, HttpServletResponse res) {
		long viewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
		int tbType = Integer.valueOf(CMyString.showEmpty(request.getParameter("tbType"), "0"));
		long mainTableId = Long.valueOf(CMyString.showEmpty(request.getParameter("tableId"), "0"));
		long tableId = 0;
		List<Object> tableList = null;
		List<Object> fieldList = null;
		List<Object> itemFieldList = null;
		List<Object> fieldRelList = null;
		List<Object> itemFieldRelList = null;
		long itemTableId = 0;
		try {
			tableList = appBaseService.getDBTableByView(viewId, tbType);
			if(tableList != null && tableList.size() > 0){
				AppTableInfo tableInfo = (AppTableInfo)tableList.iterator().next();
				if(mainTableId == 0 && tbType == 0){
					tableId = tableInfo.getTableInfoId();
				} else if(mainTableId == 0 && tbType == 1) {
					tableId = tableInfo.getTableInfoId();
					itemTableId = tableInfo.getItemTableId();
				}
			}
			fieldList = appBaseService.find("new map(fieldId as fieldId, fieldName as fieldName, fieldDesc as fieldDesc, isReserved as isReserved, fieldType as fieldType)", AppFieldInfo.class.getName(), "tableId = ?", "", tableId);
			itemFieldList = appBaseService.find("new map(fieldId as fieldId, fieldName as fieldName, fieldDesc as fieldDesc, isReserved as isReserved, fieldType as fieldType)", AppFieldInfo.class.getName(), "tableId = ?", "", itemTableId);
			
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(viewId);
			parameters.add(tableId);
			fieldRelList = appBaseService.find("new map(fieldId as fieldId, fieldName as fieldName, fieldDesc as fieldDesc, inOutline as inOutline, titleField as titleField, searchField as searchField, isWebShow as isWebShow, isReserved as isReserved, fieldType as fieldType,isGrpField as isGrpField)", AppFieldRel.class.getName(), "viewId = ? and mainTableId = ?", "fieldOrder", parameters);
			parameters.remove(1);
			parameters.add(itemTableId);
			itemFieldRelList = appBaseService.find("new map(fieldId as fieldId, fieldName as fieldName, fieldDesc as fieldDesc, inOutline as inOutline, titleField as titleField, searchField as searchField, isWebShow as isWebShow, isReserved as isReserved, fieldType as fieldType,isGrpField as isGrpField)", AppFieldRel.class.getName(), "viewId = ? and mainTableId = ?", "fieldOrder", parameters);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("获取视图的字段映射信息失败", e);
		}
		
		request.setAttribute("viewId", viewId);
		request.setAttribute("mainTableId", mainTableId);//视图实际主表id
		request.setAttribute("tableId", tableId);//查询获取的表id
		request.setAttribute("tbType", tbType);
		request.setAttribute("tableList", tableList);
		request.setAttribute("fieldList", fieldList);
		request.setAttribute("itemFieldList", itemFieldList);
		request.setAttribute("fieldRelList", fieldRelList);
		request.setAttribute("itemFieldRelList", itemFieldRelList);
		return "appadmin/app/buildViewField";
	}
	/**
	* Description:查询视图对应非系统内置字段<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-22 上午10:04:08
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=buildgrp_field")
	public String queryGrp_field(HttpServletRequest request, HttpServletResponse res) {
		try {
			long viewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
			request.setAttribute("viewId", viewId);
			List<Object> subfieldList = null;
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(0);
			parameters.add(viewId);
			//查询分组字段
			List<Object> isGrpFields = appBaseService.find(null, AppFieldRel.class.getName(), "isGrpField =1 and viewId =? ", "fieldOrder", viewId);
			//查询所有字段列表
			request.setAttribute("isGrpFields", isGrpFields);
			subfieldList = appBaseService.find("new map(fieldId as fieldId, fieldName as fieldName, fieldDesc as fieldDesc, inOutline as inOutline, titleField as titleField, searchField as searchField, isWebShow as isWebShow, isReserved as isReserved, fieldType as fieldType,isGrpField as isGrpField)", AppFieldRel.class.getName(), "fieldStyle =? and viewId = ?", "fieldOrder", parameters);
			request.setAttribute("subfieldList", subfieldList);
		} catch (Exception e) {
			loger.error("查询视图对应非系统内置字段失败:", e);
		}
		
		return "appadmin/app/sub_field_list";
	}
	/**
	 * 
	* Description:异步加载已经设置过的分组数据<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-22 下午05:50:32
	* Last Modified:
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=ajaxgrpfield")
	@ResponseBody
	public String findAJAXGrp_field(HttpServletRequest request, HttpServletResponse res) {
		long viewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
		String grpName = request.getParameter("grpName");
		List<Object> parms = new ArrayList<Object>();
		parms.add(viewId);
		parms.add(grpName);
		
		String fieldIds = null;
		try {
			List<Object> isGrpFields = appBaseService.find("fieldId", AppRelGroup.class.getName(), "viewId =? and grpName =? ", "", parms);
			fieldIds = CMyString.join((ArrayList<Object>)isGrpFields, ",");
		} catch (Exception e) {
			loger.error("异步加载已经设置过的分组数据失败:", e);
		}
		return fieldIds;
	}
	/**
	* Description:设置分组字段<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-22 下午02:40:19
	* Last Modified:
	* @param request
	* @param res
	* @return 
	* @version 1.0
	 */
	@RequestMapping(params = "method=setGrp_filed")
	@ResponseBody
	public String setGrp_field(HttpServletRequest request, HttpServletResponse res) {
		String sJson = jsonUtil.getJsonStr("200", "设置成功！", "", "", "", "");;
		try {
			long viewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
			String appFieldIds = request.getParameter("ngpids");
			String grpName = request.getParameter("grpName");
			if(CMyString.isEmpty(appFieldIds)){
				return jsonUtil.getJsonStr("300", "请选择需要设置的字段！", "", "", "", "");
			}
			if(viewId == 0){
				sJson = jsonUtil.getJsonStr("300", "操作失败！视图编号没有传入！", "", "", "", "");
				return sJson;
			}
			String[] nappFieldIds = appFieldIds.split(",");
			List<Object> appRelGroups = new ArrayList<Object>();
			//先删除已经设置的，在设置新的
			List<Object> parms = new ArrayList<Object>();
			parms.add(viewId);
			parms.add(grpName);
			List<Object> dappRelGroups = appBaseService.find(null, AppRelGroup.class.getName(), "viewId =? and grpName =? ", "", parms);
			appBaseDao.deleteAll(dappRelGroups);
			
			for(String _fieldId: nappFieldIds){
				AppRelGroup appRelGroup = new AppRelGroup();
				appRelGroup.setFieldId(Long.parseLong(_fieldId));
				appRelGroup.setViewId(viewId);
				appRelGroup.setGrpName(grpName);
				appRelGroups.add(appRelGroup);
			}
			appBaseDao.saveOrUpdateAll(appRelGroups);
		} catch (Exception e) {
			loger.error("设置分组字段失败:", e);
		}
		
		return sJson;
	}
	/**
	 * 视图字段维护-保存设置
	 * Description:  视图字段维护-保存设置<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=addEditViewField")
	@ResponseBody
	public String addEditViewField(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		try {
			long nViewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
			int nTbType = Integer.valueOf(CMyString.showEmpty(request.getParameter("tbType"), "0"));
			long nTableId = Long.valueOf(CMyString.showEmpty(request.getParameter("mainTableId"), "0"));
			String sTableName = CMyString.showEmpty(request.getParameter("tableName"), "");
			String sOutLinefields = CMyString.showEmpty(request.getParameter("outLinefields"), "");
			String sTitleFields = CMyString.showEmpty(request.getParameter("titleFields"), "");
			String sSearchFields = CMyString.showEmpty(request.getParameter("searchFields"), "");
			String sIsWebFields = CMyString.showEmpty(request.getParameter("webFields"), "");
			String sAllFields = CMyString.showEmpty(request.getParameter("allFields"), "");
			String isGrpField = CMyString.showEmpty(request.getParameter("isGrpField"), "");
			
			int isTheme = 0;
			if(nViewId == 0){
				sJson = jsonUtil.getJsonStr("300", "操作失败！视图编号没有传人！", "", "", "", "");
				return sJson;
			}
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			appBaseService.addEditViewField(nViewId, 0, nTableId, sTableName, sOutLinefields, sTitleFields, sSearchFields, sIsWebFields, sAllFields, user.getUsername(),isGrpField);
			//如果表类型为主题类型，则需要保存子表信息。
			long nItemTableId = Long.valueOf(CMyString.showEmpty(request.getParameter("itemTableId"), "0"));
			if(nTbType == 1){
				isTheme = 1;
				String sItemTableName = CMyString.showEmpty(request.getParameter("itemTableName"), "");
				String sItemOutLinefields = CMyString.showEmpty(request.getParameter("itemOutLinefields"), "");
				String sItemTitleFields = CMyString.showEmpty(request.getParameter("itemTitleFields"), "");
				String sItemSearchFields = CMyString.showEmpty(request.getParameter("itemSearchFields"), "");
				String sItemIsWebFields = CMyString.showEmpty(request.getParameter("itemWebFields"), "");
				String sItemAllFields = CMyString.showEmpty(request.getParameter("itemAllFields"), "");
				appBaseService.addEditViewField(nViewId, 1, nItemTableId, sItemTableName, sItemOutLinefields, sItemTitleFields, sItemSearchFields, sItemIsWebFields, sItemAllFields, user.getUsername(),isGrpField);
			}
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "t_vflist", "", "closeCurrent", "appView.do?method=listField&viewId="+nViewId+"&isTheme="+isTheme+"&mainTableId="+nTableId+"&itemTableId="+nItemTableId);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "",
					"");
			e.printStackTrace();
			loger.error("视图字段维护-保存设置失败",e);
		}
		return sJson;
	}
	/**
	 * 根据视图查询应用列表
	 * Description:根据视图查询应用列表 <BR>
	 * @author zhangzun
	 * @date 2014-3-28 上午10:58:01
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=listApp")
	public String listApp(HttpServletRequest request, HttpServletResponse res) {
		Long nViewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
		Integer nDeleted = Integer.valueOf(CMyString.showEmpty(request.getParameter("deleted"), "0"));
		String sWhere = "viewId = ? and deleted=?";
		List<Object> params = new ArrayList<Object>();
		params.add(nViewId);
		params.add(nDeleted);
		String currPage = CMyString.isEmpty(request.getParameter("pageNum")) ? Global.DEFUALT_STARTPAGE
				: request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage")) ? Global.DEFUALT_PAGESIZE
				: request.getParameter("numPerPage");
		Page page = null;
		try {
			page = appBaseService.findPage("", AppInfo.class.getName(), sWhere,
					"crtime desc", Integer.valueOf(currPage), Integer
							.valueOf(pageSize), params);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("根据视图查询应用列表成功。",e);
		}
		request.setAttribute("page", page);
		request.setAttribute("appList", page.getLdata());
		request.setAttribute("viewId", nViewId);
		request.setAttribute("deleted", nDeleted);
		if(0==nDeleted)
			return "appadmin/app/app_list";
		else
			return "appadmin/app/appDel_list";
	}
	/**
	 * 
	* Description:获取应用编辑页面<BR>   
	* @author zhangzun
	* @date 2014-4-2 下午03:56:17
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=appEdit")
	public String appEdite(HttpServletRequest request,HttpServletResponse res){
		String sAppId = CMyString.showEmpty(request.getParameter("appId"),"0");//视图id
		AppInfo appInfo = null;
		StringBuffer sItemGroupIds = new StringBuffer();
		try {
			appInfo = (AppInfo) appBaseService.findById(AppInfo.class, Long.valueOf(sAppId));
			if(!CMyString.isEmpty(appInfo.getItemGroupId())){
				String itemGroups[] = appInfo.getItemGroupId().split("~");
				for (String str : itemGroups) {
					sItemGroupIds.append(str.split(":")[0])
						.append(",");
				}
				sItemGroupIds = new StringBuffer(sItemGroupIds.substring(0, sItemGroupIds.length() - 1));
			}
		} catch (Exception e) {
				e.printStackTrace();
				loger.error("获取应用："+sAppId+"失败。",e);
		}
		request.setAttribute("itemGroupIds", sItemGroupIds.toString());
		request.setAttribute("app", appInfo);
		return "appadmin/app/app_edit";
	}
	/**
	 * 
	* Description:编辑应用<BR>   
	* @author zhangzun
	* @date 2014-4-2 下午06:37:17
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=editApp")
	@ResponseBody
	public String editApp(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		String sAppId = CMyString.showEmpty(request.getParameter("appId"), "0");
		String sIsNeedTheme = request.getParameter("isNeedTheme");
		String sAppStatus = CMyString.showEmpty(request.getParameter("appStatus"), "0");
		String sIsPublic = CMyString.showEmpty(request.getParameter("isPublic"), "0");
		String sIsHasQueryNo = CMyString.showEmpty(request.getParameter("isHasQueryNo"), "0");
		String sIsHasComment = CMyString.showEmpty(request.getParameter("isHasComment"), "0");
		String sIsEmailWarn = CMyString.showEmpty(request.getParameter("isEmailWarn"), "0");
		String sIsSmsWarn = CMyString.showEmpty(request.getParameter("isSmsWarn"), "0");
		String sIsSmsRemind = CMyString.showEmpty(request.getParameter("isSmsRemind"), "0");
		String sIsPush = CMyString.showEmpty(request.getParameter("isPush"), "0");
		String sWcmDocType = CMyString.showEmpty(request.getParameter("wcmDocType"), "0");
		String sWcmChnlId = CMyString.showEmpty(request.getParameter("wcmChnlId"), "0");
		int nIsSelGroup = Integer.valueOf(CMyString.showEmpty(request.getParameter("isSelGroup"),"0"));
		String sItemGroupIds = CMyString.showEmpty(request.getParameter("groupIds"),"");
		String sLimitDayNum = CMyString.showEmpty(request.getParameter("limitDayNum"), "0");
		String sIsSupAppendix = CMyString.showEmpty(request.getParameter("isSupAppendix"), "0");
		String sIsHasSmtDesc = CMyString.showEmpty(request.getParameter("isHasSmtDesc"), "0");
		String sSmtDesc = CMyString.showEmpty(request.getParameter("smtDesc"), "");
		String sListAddr = CMyString.showEmpty(request.getParameter("listAddr"), "");
		String sDowithAddr = CMyString.showEmpty(request.getParameter("dowithAddr"), "");
		String weblistAddr = CMyString.showEmpty(request.getParameter("weblistAddr"), "");
		String webdowithAddr = CMyString.showEmpty(request.getParameter("webdowithAddr"), "");
		String webdetailAddr = CMyString.showEmpty(request.getParameter("webdetailAddr"), "");
		try {
			String sItemGroupEnum = null;
			if(nIsSelGroup != 0 && !CMyString.isEmpty(sItemGroupIds)){
				sItemGroupEnum = appBaseService.getItemGroupEnum(sItemGroupIds);
			}
			AppInfo appInfo = (AppInfo)appBaseService.findById(AppInfo.class, Long.valueOf(sAppId));//new AppViewInfo();
			if(appInfo == null){
				sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "closeCurrent", "");
				return sJson;
			}
			//viewInfo.setViewName(sViewName);
			if(!CMyString.isEmpty(sIsNeedTheme))
				appInfo.setIsNeedTheme(Integer.valueOf(sIsNeedTheme));
			appInfo.setAppStatus(Integer.valueOf(sAppStatus));
			appInfo.setIsPublic(Integer.valueOf(sIsPublic));
			appInfo.setIsHasQueryNo(Integer.valueOf(sIsHasQueryNo));
			appInfo.setIsHasComment(Integer.valueOf(sIsHasComment));
			appInfo.setIsEmailWarn(Integer.valueOf(sIsEmailWarn));
			appInfo.setIsSmsWarn(Integer.valueOf(sIsSmsWarn));
			appInfo.setIsSmsRemind(Integer.valueOf(sIsSmsRemind));
			appInfo.setIsHasSmtDesc(Integer.valueOf(sIsHasSmtDesc));
			appInfo.setIsPush(Integer.valueOf(sIsPush));
			appInfo.setOperTime(new Date());
			if("1".equals(sIsPush)){
				appInfo.setWcmDocType(Integer.parseInt(sWcmDocType));
				appInfo.setWcmChnlId(Long.valueOf(sWcmChnlId));
			}
			if("1".equals(sIsHasSmtDesc) && !CMyString.isEmpty(sSmtDesc)){
				appInfo.setSmtDesc(sSmtDesc);
			}
			appInfo.setLimitDayNum(Integer.valueOf(sLimitDayNum));
			appInfo.setIsSupAppendix(Integer.valueOf(sIsSupAppendix));
			appInfo.setIsSelGroup(nIsSelGroup);
			appInfo.setItemGroupId(sItemGroupEnum);
			appInfo.setListAddr(sListAddr);
			appInfo.setDowithAddr(sDowithAddr);
			appInfo.setWeblistAddr(weblistAddr);
			appInfo.setWebdowithAddr(webdowithAddr);
			appInfo.setWebdetailAddr(webdetailAddr);
			appBaseService.update(appInfo);
//			mservice.createAppTable(tableInfo, user);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "", "", "closeCurrent", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功修改了名为").append(appInfo.getAppName()).append("的应用信息。");
			logService.addAppLog(2, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "closeCurrent",
					"");
			e.printStackTrace();
			loger.error("修改应用信息失败。",e);
		}
		return sJson;
	}
	/**
	 * 
	* Description:恢复已删除的应用 <BR>   
	* @author zhangzun
	* @date 2014-4-2 下午06:02:31
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=recoverApp")
	@ResponseBody
	public String recoverApp(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		AppInfo appInfo = null;
		Long nAppId = null;
		
		try {
			nAppId = Long.valueOf(CMyString.showEmpty(request.getParameter("appId"), "0"));
			if(nAppId==0){
				sJson = jsonUtil.getJsonStr("300", "操作失败！应用编号没有传人！", "", "", "",
				"");
						return sJson;
			}
			appInfo = (AppInfo) appBaseService.findById(AppInfo.class,nAppId);
			appInfo.setDeleted(0);
			appBaseService.update(appInfo);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "t_listApp", "t_listApp", "", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功恢复了名为").append(appInfo.getAppName()).append("的应用信息。");
			logService.addAppLog(1, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "",
					"");
			e.printStackTrace();
			loger.error("恢复应用失败。",e);
		}
		return sJson;
	}
	/**
	 *  删除应用
	 * Description:  删除应用<BR>
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=delApp")
	@ResponseBody
	public String delApp(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		Long nAppId = Long.valueOf(CMyString.showEmpty(request.getParameter("appId"), "0"));
		int nMod = Integer.valueOf(CMyString.showEmpty(request.getParameter("mod"), "0"));//0为逻辑删除，1为物理删除
		if(nAppId == 0){
			sJson = jsonUtil.getJsonStr("300", "操作失败！应用编号没有传人！", "", "", "",
			"");
					return sJson;
		}
		try {
			AppInfo appInfo = (AppInfo) appBaseService.findById(AppInfo.class,nAppId);
			appBaseService.deleteAppInfo(nAppId, nMod);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "t_listApp", "t_listApp", "", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功删除了名为").append(appInfo.getAppName()).append("的应用信息。");
			logService.addAppLog(3, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "",
					"");
			e.printStackTrace();
			loger.error("删除应用失败。",e);
		}
		return sJson;
	}
	/**
	 *  批量删除应用
	 * Description:  批量删除应用<BR>
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=delApps")
	@ResponseBody
	public String delApps(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		String sAppIds = request.getParameter("appIds");
		int nMod = Integer.valueOf(CMyString.showEmpty(request.getParameter("mod"), "0"));//0为逻辑删除，1为物理删除
		if(CMyString.isEmpty(sAppIds)){
			sJson = jsonUtil.getJsonStr("300", "操作失败！应用编号没有传人！", "", "", "", "");
			return sJson;
		}
//		AppUser user = new AppUser();//此处需要获取当前登录用户
//		user.setUsername("zhangzun");
		try {
			String appName = CMyString.join((ArrayList<Object>)appBaseService.findByIds("appName", AppInfo.class.getName(), "appId", sAppIds), ",");
			appBaseService.deleteAppInfos(sAppIds, nMod, CrtlUtil.getCurrentUser(request));
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "t_listApp", "t_listApp", "", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功删除了名为").append(appName).append("的应用信息。");
			logService.addAppLog(3, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "", "");
			e.printStackTrace();
			loger.error("批量删除应用失败。",e);
		}
		return sJson;
	}
	/**
	 *  批量恢复应用
	 * Description:  批量恢复应用<BR>
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=restoreApps")
	public String restoreApps(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		String sAppIds = request.getParameter("appIds");
		if(CMyString.isEmpty(sAppIds)){
			sJson = jsonUtil.getJsonStr("300", "操作失败！应用编号没有传人！", "", "", "closeCurrent", "");
			return sJson;
		}
//		AppUser user = new AppUser();//此处需要获取当前登录用户
//		user.setUsername("zhangzun");
		try {
			String appName = CMyString.join((ArrayList<Object>)appBaseService.findByIds("appName", AppInfo.class.getName(), "appId", sAppIds), ",");
			appBaseService.restoreAppViewInfo(sAppIds, CrtlUtil.getCurrentUser(request));
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "", "", "closeCurrent", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功恢复了名为").append(appName).append("的应用信息。");
			logService.addAppLog(1, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "closeCurrent",
					"");
			e.printStackTrace();
			loger.error("批量恢复应用失败。",e);
		}
		return sJson;
	}
	/**
	 * 
	* Description:给应用添加工作流<BR>   
	* @author zhangzun
	* @date 2014-4-3 上午10:05:39
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=addFlow")
	@ResponseBody
	public String addFlow(HttpServletRequest request, HttpServletResponse res) {

		String sJson = "";
		AppInfo appInfo = null;
		long nAppId = Long.valueOf(CMyString.showEmpty(request.getParameter("appId"), "0"));
		long nFlowId = Long.valueOf(CMyString.showEmpty(request.getParameter("flowId"), "0"));
		if(nAppId == 0){
			sJson = jsonUtil.getJsonStr("300", "操作失败！应用编号没有传人！", "", "", "", "");
			return sJson;
		}
		/*if(nFlowId == 0){
			sJson = jsonUtil.getJsonStr("300", "操作失败！没有选择工作流！", "", "", "", "");
			return sJson;
		}*/
		//应用绑定工作流验证
		/*try {
			if(!appBaseService.validateAppFlowBind(nAppId, nFlowId)){
				sJson = jsonUtil.getJsonStr("300", "操作失败！请检查应用数据接收部门下的用户是否有此应用的访问权限！", "", "", "", "");
				return sJson;
			}
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", e.getMessage(), "", "", "","");
			e.printStackTrace();
			loger.error("应用添加工作流失败。",e);
			return sJson;
		}*/
		
		try {
			appInfo = (AppInfo) appBaseService.findById(AppInfo.class,nAppId);
			appInfo.setFlowId(nFlowId == 0 ? null : nFlowId);
			appBaseService.update(appInfo);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "t_listApp", "t_listApp", "closeCurrent", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功给名为“").append(appInfo.getAppName());
			if(nFlowId != 0){
				AppFlow appFlow = (AppFlow)appBaseService.findById(AppFlow.class, appInfo.getFlowId());
				logMsg.append("”的应用设定了名字为“").append(appFlow.getFlowName()).append("”的工作流。");
			} else {
				logMsg.append("”的应用取消了工作流。");
			}
			logService.addAppLog(1, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "","");
			e.printStackTrace();
			loger.error("应用添加工作流失败。",e);
		}
		return sJson;
	}
	/**
	 * 
	* Description:工作流设置页面<BR>   
	* @author zhangzun
	* @date 2014-4-12 下午04:27:51
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=setFlowPage")
	public String setFlowPage(HttpServletRequest request, HttpServletResponse res) {
		String sFlowId = CMyString.showEmpty(request.getParameter("flowId"),"0");
		String sAppId = CMyString.showEmpty(request.getParameter("appId"),"0");
		List<Object> flowList = null;
		try {
			flowList = appBaseService.find("", AppFlow.class.getName(), "", "crtime desc", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询工作流列表失败。",e);
		}
		request.setAttribute("flowList", flowList);
		request.setAttribute("flowId", sFlowId);
		request.setAttribute("appId", sAppId);
		return "appadmin/app/setFlowPage";
	}
	/**
	 * 根据视图编号查询字段列表
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-3-28 下午08:46:20
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=listMgr")
	public String listMgr(HttpServletRequest request, HttpServletResponse res) {
		long viewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
		int nIsTheme = Integer.valueOf(CMyString.showEmpty(request.getParameter("isTheme"), "0"));
		request.setAttribute("isTheme", nIsTheme);//0表示不是主题类型，1表示是主题类型
		request.setAttribute("viewId", viewId);
		return "appadmin/app/field_mgr";
	}
	/**
	 * 根据视图编号查询字段列表
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-3-28 下午08:46:20
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=listField")
	public String listField(HttpServletRequest request, HttpServletResponse res) {
		try {
			long nViewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
			long nTableId = Long.valueOf(CMyString.showEmpty(request.getParameter("tableId"), "0"));
			AppViewInfo viewInfo = (AppViewInfo)appBaseService.findById(AppViewInfo.class, nViewId);
			long nMainTableId = viewInfo.getMainTableId() == null ? 0 : viewInfo.getMainTableId();//Long.valueOf(CMyString.showEmpty(request.getParameter("mainTableId"), "0"));
			long nItemTableId = viewInfo.getItemTableId() == null ? 0 : viewInfo.getItemTableId();//Long.valueOf(CMyString.showEmpty(request.getParameter("itemTableId"), "0"));
			//String sViewName = CMyString.showEmpty(request.getParameter("viewName"), "");
			int nTbType = Integer.valueOf(CMyString.showEmpty(request.getParameter("tbType"), "0"));
			int nIsTheme = viewInfo.getIsNeedTheme() == null ? 0 : viewInfo.getIsNeedTheme();//Integer.valueOf(CMyString.showEmpty(request.getParameter("isTheme"), "0"));
			String currPage = CMyString.isEmpty(request.getParameter("pageNum")) ? Global.DEFUALT_STARTPAGE
					: request.getParameter("pageNum");
			String pageSize = CMyString.isEmpty(request.getParameter("numPerPage")) ? Global.DEFUALT_PAGESIZE
					: request.getParameter("numPerPage");
			String selectFiled  = request.getParameter("selectFiled");//查询字段
			String sFiledValue  = request.getParameter("sFiledValue");//查询的值
			int tableType = 0;//表类型，供字段修改时判断字段类别使用，0通用，1主题，2意见表
			if(nItemTableId != 0 && nItemTableId == nTableId){
				tableType = 2;
			} else if(nItemTableId != 0 && nMainTableId == nTableId){
				tableType = 1;
			}
			String sWhere = "viewId = ? and mainTableId = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(nViewId);
			if(nTableId == 0)
				nTableId = nMainTableId;
			param.add(nTableId);
			if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
				sWhere += " and " + selectFiled + " like ?";
				param.add("%"+sFiledValue+"%");
			}
			//page = appBaseService.getViewFieldList(nViewId, Integer.valueOf(currPage), Integer.valueOf(pageSize), nTableType);
			Page page = appBaseService.findPage("", AppFieldRel.class.getName(), sWhere, "fieldOrder", Integer.valueOf(currPage),Integer.valueOf(pageSize), param);
			request.setAttribute("selectFiled", selectFiled);
			request.setAttribute("sFiledValue", sFiledValue);
			request.setAttribute("tbType", nTbType);//0表示通用类型，1表示主题类型
			request.setAttribute("tableType", tableType);//0表示通用类型，1表示主题类型,2表示意见类型
			request.setAttribute("isTheme", nIsTheme);//0表示不是主题类型，1表示是主题类型
			request.setAttribute("viewId", nViewId);
			request.setAttribute("tableId", nTableId);
			request.setAttribute("mainTableId", nMainTableId);
			request.setAttribute("itemTableId", nItemTableId);
			//request.setAttribute("viewName", sViewName);
			request.setAttribute("page", page);
			request.setAttribute("fieldList", page.getLdata());
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("根据视图编号查询字段列表失败。",e);
		}
		return "appadmin/app/field_list";
	}
	/**
	 *  删除视图字段
	 * Description:  删除视图字段<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=delField")
	@ResponseBody
	public String delField(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		long nFieldId = Long.valueOf(CMyString.showEmpty(request.getParameter("fieldId"), "0"));
		if(nFieldId == 0){
			sJson = jsonUtil.getJsonStr("300", "操作失败！字段编号没有传人！", "", "", "", "");
			return sJson;
		}
//		AppUser user = new AppUser();//此处需要获取当前登录用户
//		user.setUsername("zhangzun");
		try {
			AppFieldRel appFieldRel = (AppFieldRel)appBaseService.findById(AppFieldRel.class,nFieldId);
			AppViewInfo appViewInfo = (AppViewInfo)appBaseService.findById(AppViewInfo.class, appFieldRel.getViewId());
			appBaseService.delete(AppFieldRel.class, nFieldId);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "t_vflist", "", "", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功删除了“").append(appViewInfo.getViewName()).append("”视图中名字为").append(appFieldRel.getFieldName()).append("的字段。");
			logService.addAppLog(3, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "",
					"");
			e.printStackTrace();
			loger.error("删除视图字段失败。",e);
		}
		return sJson;
	}
	
	/**
	 *  修改视图字段
	 * Description:  修改视图字段<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=editField")
	@ResponseBody
	public String editField(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		long nFieldId = Long.valueOf(CMyString.showEmpty(request.getParameter("appFieldId"), "0"));
		if(nFieldId == 0){
			sJson = jsonUtil.getJsonStr("300", "操作失败！字段编号没有传人！", "", "", "", "");
			return sJson;
		}
		String sFieldDesc = request.getParameter("fieldDesc");
		int nFieldStyle = Integer.valueOf(CMyString.showEmpty(request.getParameter("fieldStyle"), "-1"));
		String sEnmValue = request.getParameter("enmValue");
		String sDefaultValue = request.getParameter("defaultValue");
		int nNotNull = Integer.valueOf(CMyString.showEmpty(request.getParameter("notNull"), "0"));
		int nNotEdit = Integer.valueOf(CMyString.showEmpty(request.getParameter("notEdit"), "0"));
		int nHiddenField = Integer.valueOf(CMyString.showEmpty(request.getParameter("hiddenField"), "0"));
		//long nFieldLength = Long.valueOf(CMyString.showEmpty(request.getParameter("fieldLength"), "0"));
//		AppUser user = new AppUser();//此处需要获取当前登录用户
//		user.setUsername("zhangzun");
		try {
			AppFieldRel fieldRel = (AppFieldRel)appBaseService.findById(AppFieldRel.class, nFieldId);
			fieldRel.setFieldDesc(sFieldDesc);
			if(fieldRel.getIsReserved() == 0)//当字段类别不为系统字段的时候才可以修改。if(!CMyString.isEmpty(sFieldStyle))
				fieldRel.setFieldStyle(nFieldStyle);
			if(!CMyString.isEmpty(sEnmValue))
				fieldRel.setEnmValue(sEnmValue);
			fieldRel.setDefaultValue(sDefaultValue);
			fieldRel.setNotNull(nNotNull);
			fieldRel.setNotEdit(nNotEdit);
			fieldRel.setHiddenField(nHiddenField);
			//fieldRel.setFieldLength(nFieldLength);
			appBaseService.update(fieldRel);
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "t_vflist", "", "closeCurrent", "");
			AppViewInfo appViewInfo = (AppViewInfo)appBaseService.findById(AppViewInfo.class, fieldRel.getViewId());
			AppUser user = CrtlUtil.getCurrentUser(request);//此处需要获取当前登录用户
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功修改了“").append(appViewInfo.getViewName()).append("”视图中名字为").append(fieldRel.getFieldName()).append("的字段的信息。");
			logService.addAppLog(2, logMsg.toString(), user);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "", "");
			e.printStackTrace();
			loger.error("修改视图字段失败。",e);
		}
		return sJson;
	}
	/**
	 *  获取组织树结构
	 * Description: 获取组织树结构<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=getGroupTree")
	@ResponseBody
	public String getGroupTree(HttpServletRequest request, HttpServletResponse res) {
		long pid = Long.valueOf(CMyString.showEmpty(request.getParameter("pid"), "0"));
		long id = Long.valueOf(CMyString.showEmpty(request.getParameter("id"), "0"));
		String groupType = request.getParameter("groupType");
		String rel = request.getParameter("rel");
		String sWhere = "parentId = ?";
//		if("0".equals(groupType)){
//			sWhere += "and isIndependent=1";
//		}
		if(pid != 0 && id == 0){
			id = pid;
			sWhere = "groupId = ?";
		}
		//String sJson = "[{id:0,pId:-10,name:\""+CMyString.native2Ascii("组织管理")+"\",url:\"group.do?method=listGroup\",target:\"navTab\",open:false,isParent:true}]";
		String sJson = "";
		List<TreeNode> list = new ArrayList<TreeNode>();
		List<Object> listData = null;
		try {
			listData=appBaseService.find("", AppGroup.class.getName(), sWhere,"grouporder asc", id);//appGroupService.findChildGroup(Long.valueOf(id), "grouporder asc");
		} catch (NumberFormatException e) {	
			e.printStackTrace();
			loger.error("参数不合法。",e);
		} catch (Exception e) {	
			e.printStackTrace();
			loger.error("查询子组织失败。",e);
		}
		if(!listData.isEmpty()){
			for(int i=0;i<listData.size();i++){
				TreeNode treeNode = new TreeNode();
				AppGroup appGroup = (AppGroup)listData.get(i);
				treeNode.setId(appGroup.getGroupId().toString());
				treeNode.setPId(appGroup.getParentId().toString());
	
				treeNode.setName(CMyString.native2Ascii(appGroup.getGname()));
				//treeNode.setUrl("group.do?method=listGroup&groupIds="+appGroup.getGroupId().toString());
				treeNode.setIsParent(appGroup.getContainChild()==1?true:false);
				//treeNode.setTarget("_blank");
				treeNode.setRel(rel);
				if("0".equals(groupType) && appGroup.getIsIndependent() == 0){
					treeNode.setChkDisabled(true);
				}
				list.add(treeNode);
			}
		}
		sJson = TreeNode.getTreeJsonStr(list);
		//System.out.println(sJson);
		return sJson;
	}
	/**
	 *  获取有默认父节点的组织树结构
	 * Description: 获取组织树结构<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=getParGroupTree")
	@ResponseBody
	public String getParGroupTree(HttpServletRequest request, HttpServletResponse res) {
		long id = Long.valueOf(CMyString.showEmpty(request.getParameter("id"), "0"));
		String josnStr = "[{id:0,pId:-10,name:\""+CMyString.native2Ascii("组织机构")+"\",open:true,isParent:true,children:&_&}]";
		String sJson = "";
		List<TreeNode> list = new ArrayList<TreeNode>();
		List<Object> listData = null;
		try {
			listData=appBaseService.find("", AppGroup.class.getName(), "parentId = ?","grouporder asc", id);//appGroupService.findChildGroup(Long.valueOf(id), "grouporder asc");
		} catch (NumberFormatException e) {	
			e.printStackTrace();
			loger.error("数据不合法。",e);
		} catch (Exception e) {	
			e.printStackTrace();
			loger.error("查询子组织失败。",e);
		}
		for(int i=0;i<listData.size();i++){
			TreeNode treeNode = new TreeNode();
			AppGroup appGroup = (AppGroup)listData.get(i);
			treeNode.setId(appGroup.getGroupId().toString());
			treeNode.setPId(appGroup.getParentId().toString());

			treeNode.setName(CMyString.native2Ascii(appGroup.getGname()));
			//treeNode.setUrl("group.do?method=listGroup&groupIds="+appGroup.getGroupId().toString());
			treeNode.setIsParent(appGroup.getContainChild()==1?true:false);
			//treeNode.setTarget("_blank");
			//treeNode.setRel(rel);
			list.add(treeNode);
		}
		sJson = TreeNode.getTreeJsonStr(list);
		if(id==0){
			sJson = josnStr.replace("&_&", sJson);
		}
		return sJson;
	}
	/**
	 * 获取组织机构的数
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-3-28 下午08:46:20
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=orgDialog")
	public String orgDialog(HttpServletRequest request, HttpServletResponse res) {
		//判断组织管理是否有数据
		boolean isHasGroup = false;
		try {
			long groupCount = appBaseService.count(AppGroup.class.getName(), "", null);
			if(groupCount > 0){
				isHasGroup = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("pid", request.getParameter("pid"));
		request.setAttribute("groupType", request.getParameter("groupType"));
		request.setAttribute("isHasGroup", isHasGroup);
		return "appadmin/app/org_dialog";
	}
	/**
	 * 
	* Description:返回视图字段 <BR>   
	* @author zhangzun
	* @date 2014-3-31 上午10:00:54
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=fieldEdite")
	public String fieldEdite(HttpServletRequest request, HttpServletResponse res) {
		long nAppFieldId = Long.valueOf(CMyString.showEmpty(request.getParameter("appFieldId"), "0"));
		if(nAppFieldId==0){
			//跳转到错误提示页面
		}
		AppFieldRel fieldRel = (AppFieldRel)appBaseService.findById(AppFieldRel.class, nAppFieldId);
		request.setAttribute("field", fieldRel);
		request.setAttribute("tbType", request.getParameter("tableType"));
		return "appadmin/app/field_edite";
	}
	/**
	 * 
	* Description:获取wcm同步设置页面 <BR>   
	* @author zhangzun
	* @date 2014-4-11 上午11:42:35
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=synWcmField")
	public String synWcmField(HttpServletRequest request, HttpServletResponse res) {
		String appId = request.getParameter("appId");
		long lviewId = Long.valueOf(CMyString.showEmpty(request.getParameter("viewId"), "0"));
		long mainTableId = Long.valueOf(CMyString.showEmpty(request.getParameter("mainTableId"), "0"));
		if(lviewId==0 || mainTableId==0){
			//跳转到错误提示页面
		}
		String sWhere="viewId=? and mainTableId=?";
		List<Object> params = new ArrayList<Object>();
		params.add(lviewId);
		params.add(mainTableId);
		List<Object> fieldList = null;
		String synCondition = null;
		try {
			fieldList = appBaseService.find("", AppFieldRel.class.getName(),
					sWhere, "fieldOrder", params);
			synCondition = sysConfigService.findSysConfigCon("WCM_SYN_CONDITION_" + appId, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询视图字段列表失败。",e);
		}
		request.setAttribute("synCondition", synCondition);
		request.setAttribute("fieldList", fieldList);
		request.setAttribute("toAppName", "wcm");
		request.setAttribute("appId", appId);
		return "appadmin/app/synWcmField";
	}
	/**
	 * Description: 保存数据过滤条件 <BR>   
	 * @author liu.zhuan
	 * @date 2014-12-8 下午04:12:51
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=saveSynCondition")
	@ResponseBody
	public String saveSynCondition(HttpServletRequest request, HttpServletResponse res) {
		String appId = CMyString.showEmpty(request.getParameter("appId"), "0");
		String synCondition = CMyString.showEmpty(request.getParameter("synCondition"));
		String configName = "WCM_SYN_CONDITION_" + appId;
		try {
			AppSysConfig sysConfig = (AppSysConfig)sysConfigService.findObject(AppSysConfig.class.getName(), "configName = ?", configName);;
			if(sysConfig == null){
				sysConfig = new AppSysConfig();
				sysConfig.setConfigName("WCM_SYN_CONDITION_" + appId);
				sysConfig.setConfigValue(synCondition);
				sysConfig.setConfigDesc("此配置项为应用和wcm数据同步数据过滤条件，请勿删除！！!");
				sysConfigService.save(sysConfig);
			}else{
				sysConfig.setConfigValue(synCondition);
				sysConfigService.update(sysConfig);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("保存wcm数据同步数据查询条件失败。",e);
			return jsonUtil.getJsonStr("300", "操作失败！", "", "", "", "");
		}
		return jsonUtil.getJsonStr("200", "操作成功！", "t_listApp", "", "closeCurrent", "");
	}
	/**
	 * 
	* Description:更新wcm同步字段<BR>   
	* @author zhangzun
	* @date 2014-4-12 下午04:12:32
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=addSynField")
	@ResponseBody
	public String addSynField(HttpServletRequest request, HttpServletResponse res) {
		String isSuc = "false";
		long lAppId = Long.valueOf(CMyString.showEmpty(request.getParameter("appId"), "0"));
		String appFieldName = request.getParameter("appFieldName");
		String appFieldDesc = request.getParameter("appFieldDesc");
		String toAppName = request.getParameter("toAppName");
		String toFieldName = request.getParameter("toFieldName");
		String sWhere="appId=? and appFieldName=? and toAppName=?";
		List<Object> params = new ArrayList<Object>();
		params.add(lAppId);
		params.add(appFieldName);
		params.add(toAppName);
		AppSyncField field = null;
		List<Object> list = null;
		AppUser user = CrtlUtil.getCurrentUser(request);
		try {
			list = appBaseService.find("", AppSyncField.class.getName(), sWhere, "", params);
			if(list==null || list.size()==0){//添加新的对应关系
				field = new AppSyncField();
				field.setAppId(lAppId);
				field.setAppFieldName(appFieldName);
				field.setAppFieldDesc(appFieldDesc);
				field.setToAppName(toAppName);
				field.setToFieldName(toFieldName);
				field.setCrtime(new Date());
				field.setCruser(user.getUsername());
				appBaseService.save(field);
				StringBuffer logMsg = new StringBuffer("用户");
				AppInfo appInfo = (AppInfo) appBaseService.findById(AppInfo.class,lAppId);
				logMsg.append(user.getUsername()).append("成功给名为“").append(appInfo.getAppName()).append("”的应用添加了").append(appFieldName).append("字段的wcm同步信息。");
				logService.addAppLog(1, logMsg.toString(), user);
			}else{ //更新已有的对应关系
				field = (AppSyncField)list.get(0);
				field.setToFieldName(toFieldName);
				appBaseService.update(field);
				StringBuffer logMsg = new StringBuffer("用户");
				AppInfo appInfo = (AppInfo) appBaseService.findById(AppInfo.class,lAppId);
				logMsg.append(user.getUsername()).append("成功给名为“").append(appInfo.getAppName()).append("”的应用更新了").append(appFieldName).append("字段的wcm同步信息。");;
				logService.addAppLog(1, logMsg.toString(), user);
			}
			isSuc = "true";	
			
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("更新wcm同步字段失败。",e);
		}
		return isSuc;
	}
	/**
	 * 
	* Description:查询wcm同步字段列表 <BR>   
	* @author zhangzun
	* @date 2014-4-12 下午04:10:34
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=listSynField")
	public String listSynField(HttpServletRequest request, HttpServletResponse res) {
		long lAppId = Long.valueOf(CMyString.showEmpty(request.getParameter("appId"), "0"));
		String toAppName = request.getParameter("toAppName");
		String sWhere="appId=? and toAppName=?";
		List<Object> params = new ArrayList<Object>();
		params.add(lAppId);
		params.add(toAppName);
		List<Object> list = null;
		try {
			list = appBaseService.find("", AppSyncField.class.getName(), sWhere, "crtime", params);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询wcm同步字段列表失败。",e);
		}
		request.setAttribute("fieldList", list);
		request.setAttribute("appId", lAppId);
		return "appadmin/app/synFieldList";
	}
	/**
	 * 
	* Description:删除wcm同步字段 <BR>   
	* @author zhangzun
	* @date 2014-4-12 下午04:08:06
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=delSynField")
	@ResponseBody
	public String delSynField(HttpServletRequest request, HttpServletResponse res) {
		long syncFieldId = Long.valueOf(CMyString.showEmpty(request.getParameter("syncFieldId"), "0"));
		String issuc = "false";
		try {
			AppSyncField appSyncField = (AppSyncField)appBaseService.findById(AppSyncField.class, syncFieldId);
			appBaseService.delete(AppSyncField.class, syncFieldId);
			issuc = "true";
			AppUser user = CrtlUtil.getCurrentUser(request);
			StringBuffer logMsg = new StringBuffer("用户");
			AppInfo appInfo = (AppInfo) appBaseService.findById(AppInfo.class,appSyncField.getAppId());
			logMsg.append(user.getUsername()).append("成功删除了“").append(appInfo.getAppName()).append("”应用下").append(appSyncField.getAppFieldName()).append("字段的wcm同步信息。");;
			logService.addAppLog(3, logMsg.toString(), user);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("删除wcm同步字段失败。",e);
		}
		return issuc;
	}
	/**
	 * 
	* Description:获取视图或应用的提交说明页面 <BR>   
	* @author zhangzun
	* @date 2014-5-14 上午10:06:00
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=getAppSmtDesc")
	public String getAppSmtDesc(HttpServletRequest request, HttpServletResponse res) {
		String viewId  = CMyString.showEmpty(request.getParameter("viewId"), "0");//视图id
		String appId  = CMyString.showEmpty(request.getParameter("appId"), "0");//视图id
		String smtDesc = "";//应用提交说明
		Long lViewId = Long.valueOf(viewId);
		Long lappId = Long.valueOf(appId);
		try {
			if(lViewId>0){
				AppViewInfo viewInfo = (AppViewInfo)appBaseService.findById(AppViewInfo.class, lViewId);
				smtDesc = viewInfo.getSmtDesc();
			}else if(lappId>0){
				AppInfo appInfo = (AppInfo)appBaseService.findById(AppInfo.class, lappId);
				smtDesc = appInfo.getSmtDesc();
			}
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询应用信息失败。",e);
		}
		
		request.setAttribute("smtDesc", smtDesc);
		return "appadmin/app/appSmtDesc_edit";
	}
	
}
