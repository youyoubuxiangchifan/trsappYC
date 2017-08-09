/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: GroupController
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午06:11:39
 * @version 1.0
 */
package com.trs.web;

import java.io.FileOutputStream;
import java.io.IOException;
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
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.trs.dbhibernate.Page;
import com.trs.model.AppGroup;
import com.trs.model.AppUser;
import com.trs.service.AppGroupService;
import com.trs.service.AppLogService;
import com.trs.service.AppUserService;
import com.trs.util.CMyString;
import com.trs.util.Global;
import com.trs.model.TreeNode;

/**
 * @Description: 组织管理Controller<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liujian
 * @ClassName: GroupController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午06:11:39
 * @version 1.0
 */

@Controller
@RequestMapping("/group.do")
public class GroupController {
	private static  Logger LOG =  Logger.getLogger(GroupController.class);
	@Autowired
	private AppGroupService appGroupService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppLogService appLogService;

	/** 
	* Description: 查询组织列表 <BR>   
	* author liujian
	* date 2014-3-17 下午08:23:17
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=listGroup")
	public String listGroup(HttpServletRequest request,HttpServletResponse res){
		String groupIds = CMyString.isEmpty(request.getParameter("groupIds"))?"0":request.getParameter("groupIds");
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"20":request.getParameter("numPerPage");		
		String sOrder ="grouporder asc";
		Page page = null;
		AppUserService appUserService = null;

			try {
				page = appGroupService.findChildGroup(Long.valueOf(groupIds), sOrder, Integer.parseInt(currPage),Integer.parseInt(pageSize));
				
			} catch (NumberFormatException e) {
				LOG.error("取组织列表时组织ID类型转换异常");
				e.printStackTrace();
			} catch (Exception e) {
				LOG.error("获取组织列表时异常");
				e.printStackTrace();
			}

		request.setAttribute("groupIds",groupIds);
		request.setAttribute("page",page);
		request.setAttribute("groupList", page.getLdata());
		return "appadmin/group/group_list";
	}
	/** 
	* Description: 页面跳转判断 标识 0-跳转到新建组织页面、1-跳转到组织信息页面、2-跳转到修改组织页面
	* author liujian
	* date 2014-3-13 下午02:31:04
	* @param request 
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=edditPage")
	public String edditPage(HttpServletRequest request,HttpServletResponse res){
		String returnUrl="appadmin/group/group_add";
		String groupIds = request.getParameter("groupIds");//获取组织ID
		String pageFlag = CMyString.isEmpty(request.getParameter("pageFlag"))?"0":request.getParameter("pageFlag");//获取用户ID
		AppGroup appGroup = null;
		switch (Integer.parseInt(pageFlag)) {
			case 0: //跳转到新建组织页面
				returnUrl="appadmin/group/group_add";
				appGroup = (AppGroup)appGroupService.findById(AppGroup.class, Long.valueOf(groupIds));
				request.setAttribute("groupIds", groupIds);
				if("0".equals(groupIds)){
					request.setAttribute("gName", "根组织");
				}else{
					request.setAttribute("gName",appGroup.getGname());
				}
				
				break;
			case 1: //跳转到组织信息页面
				returnUrl="appadmin/group/group_info";
				appGroup = (AppGroup)appGroupService.findById(AppGroup.class, Long.valueOf(groupIds));
				request.setAttribute("appGroup", appGroup);			
				break;		
			case 2: //跳转到修改组织页面
				returnUrl="appadmin/group/group_eddit";
				appGroup = (AppGroup)appGroupService.findById(AppGroup.class, Long.valueOf(groupIds));
				request.setAttribute("appGroup", appGroup);			
				break;
			case 3: //跳转到新建组织用户页面
				returnUrl="appadmin/group/group_createUser";
				request.setAttribute("groupIds", groupIds);			
				break;
			default:
				break;
		}
		
		return returnUrl;
	}	
	
	/** 
	* Description: 新建组织<BR>   
	* author liujian
	* date 2014-3-17 下午08:23:35
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=addGroup")
	@ResponseBody
	public String addGroup(HttpServletRequest request,HttpServletResponse res){
		String gname = request.getParameter("gname");
		String parentId = CMyString.isEmpty(request.getParameter("parentId"))?"0":request.getParameter("parentId");
		String gdesc = request.getParameter("gdesc");
		String isIndependent = CMyString.isEmpty(request.getParameter("isIndependent"))?"0":request.getParameter("isIndependent");
		if("0".equals(parentId)){
			isIndependent = "1";
		}
		AppGroup appGroup = new AppGroup();
		appGroup.setGname(gname);
		appGroup.setParentId(Long.valueOf(parentId));
		appGroup.setGdesc(gdesc);
		appGroup.setIsIndependent(Integer.parseInt(isIndependent));	
		String josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("添加组织成功！")+"\",\"target\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\",\"treeOpr\":\"1\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		try {
			appGroup.setCruser(loginUser.getUsername());
			appGroupService.addGroup(appGroup);
			appLogService.addAppLog(Global.LOGS_SAVE, "新建组织", loginUser);
			//josnStr="{\"statusCode\":\"200\",\"message\":\"sucessed!\",\"target\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\",\"treeOpr\":\"1\"}";
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_SAVE, "新建组织异常", loginUser);
			josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("添加组织失败！")+"\",\"target\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\",\"treeOpr\":\"1\"}";
			LOG.error("添加组织时异常");
			e.printStackTrace();
		}
		return josnStr;
	}
	
	/** 
	* @Description: TODO(修改组织信息)    
	* @author liujian
	* @date 2014-3-10 下午05:55:04
	* @param request
	* @param res
	* @return
	* @version 1.0
	*/	
	@RequestMapping(params = "method=upGroup")
	@ResponseBody
	public String upGroup(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");//获取组织ID
		String gname = request.getParameter("gname");
		String gdesc = request.getParameter("gdesc");
		String isIndependent = CMyString.isEmpty(request.getParameter("isIndependent"))?"0":request.getParameter("isIndependent");
		AppGroup appGroup = (AppGroup)appGroupService.findById(AppGroup.class, Long.valueOf(groupIds));
		appGroup.setGname(gname);
		appGroup.setGdesc(gdesc);
		appGroup.setIsIndependent(Integer.parseInt(isIndependent));
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("修改组织信息失败！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appGroupService.updateGroup(appGroup);
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改组织[id="+groupIds+"]信息", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("修改组织信息成功！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改组织[id="+groupIds+"]信息异常", loginUser);
			LOG.error("修改组织时异常");
			e.printStackTrace();
		}
		request.setAttribute("appGroup", appGroup);
		return josnStr;
	}
	/** 
	* Description: 组织调整顺序 <BR>   
	* author liujian
	* date 2014-3-18 下午08:55:25
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=orderGroup")
	@ResponseBody
	public String orderGroup(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");//获取组织ID
		String grouporder = request.getParameter("grouporder");//当前组织顺序
		String orderFlag = request.getParameter("orderFlag");//移动标识，2：向下下移，1:向上移动
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("调整组织顺序失败！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		if(!CMyString.isEmpty(groupIds) && !CMyString.isEmpty(orderFlag)){
			try {
				
				appGroupService.orderByGroup(Long.valueOf(groupIds), Integer.parseInt(orderFlag));
				appLogService.addAppLog(Global.LOGS_MODIFY, "调整组织[id="+groupIds+"]顺序", loginUser);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("调整组织顺序成功！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"fresh\"}";
			} catch (Exception e) {
				appLogService.addAppLog(Global.LOGS_MODIFY, "调整组织[id="+groupIds+"]顺序异常", loginUser);
				LOG.error("移动组织时异常");
				e.printStackTrace();
			}
		}
		return josnStr;
	}
	/** 
	* @Description: TODO(删除组织)    
	* @author liujian
	* @date 2014-3-10 下午05:55:04
	* @param request
	* @param res
	* @return
	* @version 1.0
	*/	
	@RequestMapping(params = "method=delGroup")
	@ResponseBody
	public String delGroup(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");//获取选中的组织ID
		String parentId = request.getParameter("parentId");//获取选中组织的父组织ID
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("删除组织失败！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		if(!CMyString.isEmpty(groupIds) && !CMyString.isEmpty(parentId)){
			try {				
				Boolean delFlag = appGroupService.deleteGroup(groupIds, Long.valueOf(parentId));
				if(delFlag){
					appLogService.addAppLog(Global.LOGS_DEL, "删除组织["+groupIds+"]", loginUser);
					josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("删除组织成功！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
				}else{
					josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("删除的组织有子组织！删除组织失败！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
				}

			} catch (Exception e){
				appLogService.addAppLog(Global.LOGS_DEL, "删除组织["+groupIds+"]异常", loginUser);
				LOG.error("删除组织时异常");
				e.printStackTrace();
			}
		}
		return josnStr;
	}
	

	
	/** 
	* Description:  生成系统左侧树<BR>   
	* author liujian
	* date 2014-3-20 下午08:26:09
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=getTreeJosn")
	@ResponseBody
	public String getTreeJosn(HttpServletRequest request,HttpServletResponse res){
		String flag = request.getParameter("flag");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String target = request.getParameter("target");
		String rel = request.getParameter("rel");
		String sOrder ="grouporder asc";
		
		String josnStr = "[{id:-3,pId:-10,name:\""+CMyString.native2Ascii("应用管理")+"\",url:\"appView.do?method=list\",target:\"navTab\",open:false,isParent:false}," +
						"{id:-4,pId:-10,name:\""+CMyString.native2Ascii("元数据管理")+"\",url:\"meta.do?method=list\",target:\"navTab\",open:false,isParent:false},"+
						"{id:-5,pId:-10,name:\""+CMyString.native2Ascii("流程管理")+"\",url:\"flow.do?method=list\",target:\"navTab\",open:false,isParent:false},"+
						"{id:-2,pId:-10,name:\""+CMyString.native2Ascii("用户管理")+"\",url:\"user.do?method=listUser\",target:\"navTab\",open:false,isParent:true}," +
						"{id:0,pId:-10,name:\""+CMyString.native2Ascii("组织管理")+"\",url:\"group.do?method=listGroup\",target:\"navTab\",open:false,isParent:true}," +
						"{id:-1,pId:-10,name:\""+CMyString.native2Ascii("角色管理")+"\",url:\"role.do?method=listRole\",target:\"navTab\",open:false,isParent:false}," +
						"{id:-6,pId:-10,name:\""+CMyString.native2Ascii("系统管理")+"\",url:\"sysconfig.do?method=listSysConfig\",target:\"navTab\",open:false,isParent:true},"+
						"{id:-7,pId:-10,name:\""+CMyString.native2Ascii("统计分析")+"\",url:\"report.do?method=reportgrp\",target:\"navTab\",open:false,isParent:true}"+
						"]";
		
		if(!CMyString.isEmpty(id)){
			List<TreeNode> list = new ArrayList<TreeNode>();
			List<Object> listData = new ArrayList<Object>();
			switch (Integer.parseInt(id)) {
			case -2://用户管理
				josnStr = "[{id:-11,pId:-2,name:\""+CMyString.native2Ascii("已开通")+"\",url:\"user.do?method=listUser&userStatus=0\",target:\"navTab\",open:false,isParent:false}," +
						"{id:-12,pId:-2,name:\""+CMyString.native2Ascii("已停用")+"\",url:\"user.do?method=listUser&userStatus=1\",target:\"navTab\",open:false,isParent:false}," +
								"{id:-13,pId:-2,name:\""+CMyString.native2Ascii("已删除")+"\",url:\"user.do?method=listUser&userStatus=2\",target:\"navTab\",open:false,isParent:false}]";
				break;	
			case -3://应用管理
				josnStr = "[]";
				break;
			case -6://系统管理
				josnStr = "[{id:-14,pId:-6,name:\""+CMyString.native2Ascii("系统配置")+"\",url:\"sysconfig.do?method=listSysConfig\",target:\"navTab\",open:false,isParent:false}," +
				"{id:-15,pId:-6,name:\""+CMyString.native2Ascii("操作管理")+"\",url:\"sysconfig.do?method=listOper\",target:\"navTab\",open:false,isParent:false}," +
				"{id:-16,pId:-6,name:\""+CMyString.native2Ascii("节点状态管理")+"\",url:\"sysconfig.do?method=listDataStatus\",target:\"navTab\",open:false,isParent:false}," +
				"{id:-17,pId:-6,name:\""+CMyString.native2Ascii("提醒模板管理")+"\",url:\"sysconfig.do?method=listMsgTemp\",target:\"navTab\",open:false,isParent:false}," +
				"{id:-18,pId:-6,name:\""+CMyString.native2Ascii("系统日志管理")+"\",url:\"sysconfig.do?method=listLog\",target:\"navTab\",open:false,isParent:false}," +
				"{id:-19,pId:-6,name:\""+CMyString.native2Ascii("工作日设置")+"\",url:\"sysconfig.do?method=holiadayPage\",target:\"navTab\",open:false,isParent:false}]";
				break;
			case -7://系统管理
				josnStr = "[{id:-20,pId:-7,name:\""+CMyString.native2Ascii("按机构统计")+"\",url:\"report.do?method=reportgrp\",target:\"navTab\",open:false,isParent:false}," +
				"{id:-21,pId:-7,name:\""+CMyString.native2Ascii("按处理人统计")+"\",url:\"report.do?method=reportus\",target:\"navTab\",open:false,isParent:false}," +
				"{id:-22,pId:-7,name:\""+CMyString.native2Ascii("按办件分类统计")+"\",url:\"report.do?method=reportty\",target:\"navTab\",open:false,isParent:false}]";
				break;
			case -1://角色管理
				josnStr = "[]";
				break;				
			default://组织管理
				try {
					listData=appGroupService.findChildGroup(Long.valueOf(id), sOrder);
				} catch (NumberFormatException e) {	
					LOG.error("获取子组织树时组织ID类型转换异常");
					e.printStackTrace();
				} catch (Exception e) {	
					LOG.error("获取子组织树时异常");
					e.printStackTrace();
				}
				for(int i=0;i<listData.size();i++){
					TreeNode treeNode = new TreeNode();
					AppGroup appGroup = (AppGroup)listData.get(i);
					treeNode.setId(appGroup.getGroupId().toString());
					treeNode.setPId(appGroup.getParentId().toString());

					treeNode.setName(CMyString.native2Ascii(appGroup.getGname()));
					treeNode.setUrl("group.do?method=listGroup&groupIds="+appGroup.getGroupId().toString());
					treeNode.setIsParent(appGroup.getContainChild()==1?true:false);
					treeNode.setTarget(target);
					treeNode.setRel(rel);
					list.add(treeNode);
				}
				josnStr = TreeNode.getTreeJsonStr(list);
								
				break;
			}
			

		}
		

		return josnStr;
	}
	/** 
	* Description: 查询组织用户列表 <BR>   
	* author liujian
	* date 2014-3-17 下午08:23:17
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=listGroupUser")
	public String listGroupUser(HttpServletRequest request,HttpServletResponse res){
		String groupIds = CMyString.isEmpty(request.getParameter("groupIds"))?"0":request.getParameter("groupIds");
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"20":request.getParameter("numPerPage");
		Page page = null;
			try {
				page = appGroupService.findGroupUser(Long.valueOf(groupIds), Integer.parseInt(currPage), Integer.parseInt(pageSize));
			} catch (NumberFormatException e){
				LOG.error("获取组织用户列表时组织ID类型转换异常");
				e.printStackTrace();
			} catch (Exception e) {
				LOG.error("获取组织用户列表时异常");
				e.printStackTrace();
			}			
		request.setAttribute("groupIds",groupIds);
		request.setAttribute("page",page);
		request.setAttribute("groupUserList", page.getLdata());
		return "appadmin/group/group_userMgr";
	}
	/** 
	* Description: 根据条件取已开通用户列表 <BR>   
	* author liujian
	* date 2014-3-17 下午08:23:17
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=addGroupUserList")
	public String addGroupUserList(HttpServletRequest request,HttpServletResponse res){
		String groupIds = CMyString.isEmpty(request.getParameter("groupIds"))?"0":request.getParameter("groupIds");
		String userStatus = CMyString.isEmpty(request.getParameter("userStatus"))?"0":request.getParameter("groupIds");//当前用户列表状态
		String selectFiled = request.getParameter("selectFiled");	//检索字段
		String sFiledValue = request.getParameter("sFiledValue");	//检索字段值
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"20":request.getParameter("numPerPage");
		String sWhere ="isDeleted != 1";
		String sOrder="userId desc";
		List<Object> param = new ArrayList<Object>();		
		if(!CMyString.isEmpty(userStatus)){//判断列表状态，为空或空对象则查询所有用户
			sWhere =sWhere+" and status = ? ";
			param.add(Integer.parseInt(userStatus));			
		}
		if(!CMyString.isEmpty(sFiledValue)){
			sWhere =sWhere+" and "+selectFiled+" like ?";
			param.add("%"+sFiledValue+"%");
		}

		Page page = null;
			try {

				page = appUserService.findPage("","AppUser",sWhere,sOrder,Integer.parseInt(currPage),Integer.parseInt(pageSize),param);
				String GrpUserSelIds = appGroupService.findGroupUser(Long.valueOf(groupIds));
				if(!CMyString.isEmpty(GrpUserSelIds)){
					request.setAttribute("GrpUserSelIds", GrpUserSelIds);
				}
				
			} catch (NumberFormatException e){
				LOG.error("获取组织用户时组织ID类型转换异常");
				e.printStackTrace();
			} catch (Exception e) {
				LOG.error("添加组织用户时获取用户列表异常");
				e.printStackTrace();
			}
			
		request.setAttribute("groupIds",groupIds);
		request.setAttribute("page",page);
		request.setAttribute("groupUserList", page.getLdata());
		return "appadmin/group/group_seluser_list";
	}
	/** 
	* Description: 向组织中添加已选择的用户 <BR>   
	* author liujian
	* date 2014-3-17 下午08:23:17
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=addGroupUser")
	@ResponseBody
	public String addGroupUser(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");
		String userIds = CMyString.isEmpty(request.getParameter("userIds"))?"":request.getParameter("userIds");
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("添加用户失败！")+"!\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		if(!CMyString.isEmpty(groupIds)){
			try {
				appGroupService.addGroupUser(Long.valueOf(groupIds), userIds, loginUser);
				appLogService.addAppLog(Global.LOGS_SAVE, "添加组织用户[id="+userIds+"]", loginUser);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("添加用户成功！")+"!\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			} catch (NumberFormatException e) {
				appLogService.addAppLog(Global.LOGS_SAVE, "添加组织用户[id="+userIds+"]异常", loginUser);
				LOG.error("添加组织用户时组织ID类型转换异常");
				e.printStackTrace();
			} catch (Exception e) {
				LOG.error("添加组织用户时异常");
				e.printStackTrace();
			}
		}
		
		return josnStr;
			
	}
	/** 
	* Description: 向组织中新建用户 <BR>   
	* author liujian
	* date 2014-3-17 下午08:23:17
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=creatGroupUser")
	@ResponseBody
	public String creatGroupUser(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");
		String userName = request.getParameter("userName");
		String trueName = request.getParameter("trueName");
		String userPwd = request.getParameter("userPwd");
		String moblie = request.getParameter("moblie");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String zipcode = request.getParameter("zipcode");
		String userType = request.getParameter("userType");
		String idWeakPwd = CMyString.isEmpty(request.getParameter("newPwd"))?"0":request.getParameter("newPwd");
		AppUser appUser=new AppUser();
		appUser.setUsername(userName);
		appUser.setTruename(trueName);
		appUser.setPassword(userPwd);
		appUser.setAddress(address);
		appUser.setMoblie(moblie);
		appUser.setEmail(email);
		appUser.setZipcode(zipcode);
		appUser.setUsertype(Integer.parseInt(userType));
		appUser.setStatus(Global.USER_STATUS_REG);
		appUser.setCrtime(new Date());
		appUser.setWeakPasswd(Integer.parseInt(idWeakPwd));
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");

		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("新建组织用户失败！")+"!\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		if(!CMyString.isEmpty(groupIds)){
			try {
				appUser.setCruser(loginUser.getUsername());
				appGroupService.addGroupUser(Long.valueOf(groupIds), appUser, loginUser);
				appLogService.addAppLog(Global.LOGS_SAVE, "新建组织用户["+appUser.getUsername()+"]", loginUser);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("新建组织用户成功！")+"!\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
			} catch (NumberFormatException e) {
				appLogService.addAppLog(Global.LOGS_SAVE, "新建组织用户异常", loginUser);
				LOG.error("新建组织用户时组织ID类型转换异常");
				e.printStackTrace();
			} catch (Exception e) {
				appLogService.addAppLog(Global.LOGS_SAVE, "新建组织用户异常", loginUser);
				LOG.error("新建组织用户时异常");
				e.printStackTrace();
			}
		}
		
		return josnStr;
			
	}	
	/** 
	* @Description: 删除组织 用户   
	* @author liujian
	* @date 2014-3-10 下午05:55:04
	* @param request
	* @param res
	* @return
	* @version 1.0
	*/	
	@RequestMapping(params = "method=delGroupUser")
	@ResponseBody
	public String delGroupUser(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");//获取当前组织ID
		String grpUserIds = request.getParameter("grpUserIds");//获取当前组织用户关联关系ID
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("删除组织用户失败！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		if(!CMyString.isEmpty(grpUserIds)){
			try {				
				appGroupService.delGroupUser(grpUserIds);
				appLogService.addAppLog(Global.LOGS_DEL, "删除组织用户["+grpUserIds+"]", loginUser);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("删除组织用户成功！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
			} catch (Exception e){
				appLogService.addAppLog(Global.LOGS_DEL, "删除组织用户["+grpUserIds+"]异常", loginUser);
				LOG.error("删除组织用户时异常");
				e.printStackTrace();
			}
		}
		return josnStr;
	}	
	/** 
	* @Description: 设置组织管理员  
	* @author liujian
	* @date 2014-3-10 下午05:55:04
	* @param request
	* @param res
	* @return
	* @version 1.0
	*/	
	@RequestMapping(params = "method=setGroupAdmin")
	@ResponseBody
	public String setGroupAdmin(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");//获取选中的组织ID
		String grpUserIds = request.getParameter("grpUserIds");//获取当前组织用户关联关系ID
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("设置组织管理员失败！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		System.out.println(grpUserIds);
		if(!CMyString.isEmpty(groupIds) && !CMyString.isEmpty(grpUserIds)){
			try {				
				appGroupService.setGrpUserAdmin(grpUserIds);
				appLogService.addAppLog(Global.LOGS_MODIFY, "设置组织用户["+grpUserIds+"]为管理员", loginUser);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("设置组织管理员成功！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
			} catch (Exception e){
				appLogService.addAppLog(Global.LOGS_MODIFY, "设置组织用户["+grpUserIds+"]为管理员异常", loginUser);
				LOG.error("设置组织管理员时异常");
				e.printStackTrace();
			}
		}
		return josnStr;
	}
	/** 
	* @Description: 设置组织管理员  
	* @author liujian
	* @date 2014-3-10 下午05:55:04
	* @param request
	* @param res
	* @return
	* @version 1.0
	*/	
	@RequestMapping(params = "method=cncelGroupAdmin")
	@ResponseBody
	public String cncelGroupAdmin(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");//获取选中的组织ID
		String grpUserIds = request.getParameter("grpUserIds");//获取当前组织用户关联关系ID
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("取消组织管理员失败！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		if(!CMyString.isEmpty(groupIds) && !CMyString.isEmpty(grpUserIds)){
			try {				
				appGroupService.cancelGrpUserAdmin(grpUserIds);
				appLogService.addAppLog(Global.LOGS_MODIFY, "取消组织用户["+grpUserIds+"]为管理员", loginUser);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("取消组织管理员成功！")+"\",\"\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\",\"treeOpr\":\"0\"}";
			} catch (Exception e){
				appLogService.addAppLog(Global.LOGS_MODIFY, "取消组织用户["+grpUserIds+"]为管理员异常", loginUser);
				LOG.error("取消组织管理员时异常");
				e.printStackTrace();
			}
		}
		return josnStr;
	}
	/** 
	* Description: txt文件形式导入组织 <BR>   
	* author liujian
	* date 2014-3-17 下午06:25:24
	* @param request
	* @param res
	* version 1.0
	*/
	@RequestMapping(params = "method=groupUpload")
	//@ResponseBody	
	public void groupUpload(MultipartHttpServletRequest request,HttpServletResponse res){
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("导入组织失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
		String groupId = request.getParameter("groupId");
		MultipartFile file = request.getFile("file");
		FileOutputStream foStream  = null;
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		String fileName = file.getOriginalFilename();
		String type =fileName.substring(fileName.lastIndexOf("."), fileName.length());
		if(file!=null && ".txt".equals(type)){
			try {				
				java.io.File nFile = new java.io.File(ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+"/appadmin/"+file.getOriginalFilename());				
				foStream  = new FileOutputStream(nFile);
				foStream.write(file.getBytes());
				appGroupService.importGroup(Long.valueOf(groupId), nFile, loginUser);
				josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("成功导入组织!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
				appLogService.addAppLog(Global.LOGS_MODIFY, "批量导入组织", loginUser);
			} catch (IOException e) {
				appLogService.addAppLog(Global.LOGS_MODIFY, "批量导入组织异常", loginUser);
				LOG.error("批量导入组织异常！");
				e.printStackTrace();
			} catch (Exception e) {
				appLogService.addAppLog(Global.LOGS_MODIFY, "批量导入组织异常", loginUser);
				LOG.error("批量导入组织异常！");
				e.printStackTrace();
			}finally{
				try {
					if(foStream!=null){
						foStream.close();
					}
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}			
			
		}
		try {
			res.getWriter().write(josnStr);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	/** 
	* @Description: 检查同级组织是否已存在 传入的组织名
	* @author liujian
	* @date 2014-3-10 下午05:55:04
	* @param request
	* @param res
	* @return
	* @version 1.0
	*/	
	@RequestMapping(params = "method=checkGroupName")
	@ResponseBody
	public String checkGroupName(HttpServletRequest request,HttpServletResponse res){
		String groupIds = request.getParameter("groupIds");//获取选中的组织ID
		String gname = request.getParameter("gname");//获取当前组织用户关联关系ID
		String josnStr="false";
		if(!CMyString.isEmpty(groupIds)&&!CMyString.isEmpty(gname)){
			try {
				if(appGroupService.existGroup(gname, Long.valueOf(groupIds))){
					josnStr="false";
				}else{
					josnStr="true";
				}
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				LOG.error("校验组织名唯一性时类型转换异常！");
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block				
				LOG.error("校验组织名唯一性异常！");
				e.printStackTrace();
			}	
		}

		return josnStr;
	}
	/**
	 * 
	* Description: 从WCM中抽取所有的组织结构到互动平台中 <BR>   
	* @author jin.yu
	* @date 2014-4-16 下午05:07:46
	* @version 1.0
	 */
	@RequestMapping(params = "method=importWcmGroup")
	public void importWcmGroup(MultipartHttpServletRequest request,HttpServletResponse res){
		try {
			AppUser loginUser = (AppUser)request.getSession().getAttribute("loginUser");//获取当前登陆用户
			appGroupService.importWcmGroup(loginUser);
		} catch (Exception e) {
			LOG.error("导入WCM组织结构失败！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
