package com.trs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.trs.cache.CacheRoleOper;
import com.trs.dbhibernate.Page;
import com.trs.model.AppAppendix;
import com.trs.model.AppInfo;
import com.trs.model.AppOper;
import com.trs.model.AppRole;
import com.trs.model.AppRoleOper;
import com.trs.model.AppUser;
import com.trs.service.AppBaseService;
import com.trs.service.AppGroupService;
import com.trs.service.AppLogService;
import com.trs.service.AppRoleService;
import com.trs.service.CommonService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.Global;
import com.trs.util.UploadFileUtil;
import com.trs.util.jsonUtil;
/**
 * 
 * @Description: TODO(用于角色的请求处理)<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author zhangzun
 * @ClassName: RollController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-8 上午10:55:16
 * @version 1.0
 */
@Controller
@RequestMapping("/role.do")
public class RoleController {
	private static  Logger loger =  Logger.getLogger(RoleController.class);
	@Autowired
	private AppRoleService rService;
	@Autowired
	private AppGroupService appGroupService; 
	@Autowired
	private AppLogService logService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AppBaseService appBaseService;
	/**
	 * 
	* @Description: TODO(查询所有的角色信息) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=listRole")
	public String listRole(HttpServletRequest request,HttpServletResponse res){
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
		//List<AppRole> list = null;
		try {
			page = rService.findPage("", AppRole.class.getName(), sWhere, "crtime desc", 
					Integer.valueOf(currPage),Integer.valueOf(pageSize), param);
		} catch (NumberFormatException e) {
			loger.error("传入的参数不合法", e);
			e.printStackTrace();
		} catch (Exception e) {
			loger.error("获取角色列表失败", e);
			e.printStackTrace();
		}//查询所有的角色
		request.setAttribute("roleList",page.getLdata());
		request.setAttribute("page",page);
		request.setAttribute("sFiledValue",sFiledValue);
		request.setAttribute("selectFiled",selectFiled);
		return "appadmin/role/role_list";
	}
	/**
	    * 
	   * Description:为互动应用创建角色 <BR>   
	   * @author zhangzun
	   * @date 2014-3-8 下午12:01:18
	   * @param request
	   * @param res
	   * @return String
	   * @version 1.0
	 */
	@RequestMapping(params = "method=addRole")
	@ResponseBody
	public String addRole(HttpServletRequest request,HttpServletResponse res){
		
		String srolename = request.getParameter("rolename"); //角色名称
		String sroledesc = request.getParameter("roledesc"); //角色描述
		String sJson = "";
		try {
			AppUser user = CrtlUtil.getCurrentUser(request);
			AppRole role = new AppRole();//创建角色对象
			role.setRolename(srolename);
			role.setRoledesc(sroledesc);
			role.setCruser(user.getUsername());
			role.setCrtime(new Date());
			rService.addRole(role);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			logService.addAppLog(1, "添加角色成功", user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			loger.error("添加角色失败。", e);
			e.printStackTrace();
		}
		return sJson;
	}
	/**
	 * 
	* @Description: TODO(删除角色) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色列表页
	* @version 1.0
	 */
	@RequestMapping(params = "method=delRole")
	@ResponseBody
	public String delRole(HttpServletRequest request,HttpServletResponse res){
		
		String sroleID = request.getParameter("roleIDs");
		String sJson = "";
		try {
			rService.deleteRole(sroleID); //删除角色及角色关联信息
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
			logService.addAppLog(3, "删除角色成功", CrtlUtil.getCurrentUser(request));
		} catch (Exception e) {
			e.printStackTrace();
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			loger.error("删除角色失败。",e);
		} 
		return sJson;
	}
	/**
	 * 
	* Description:(修改角色的基本信息) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色列表页
	* @version 1.0
	 */
	@RequestMapping(params = "method=upRole")
	@ResponseBody
	public String upRole(HttpServletRequest request,HttpServletResponse res){
		
		String sroleID = request.getParameter("roleID");	//角色id
		String srolename = request.getParameter("rolename"); //角色名称
		String sroledesc = request.getParameter("roledesc"); //角色描述
		AppRole role = new AppRole();
		role = (AppRole) rService.findById(AppRole.class,Long.valueOf(sroleID));
		role.setRolename(srolename);
		role.setRoledesc(sroledesc);
		String sJson = "";
		try {
			rService.updateRole(role);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			logService.addAppLog(2, "更新角色成功", CrtlUtil.getCurrentUser(request));
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			e.printStackTrace();
			loger.error("更新角色失败。",e);
		}  //更新角色的相关信息
		return sJson;
	}
	/**
	 * 
	* Description:检查角色是否已经存在 <BR>   
	* @author zhangzun
	* @param request
	* @param res
	* @date 2014-3-8 下午12:01:18
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=checkRole")
	@ResponseBody
	public String checkRole(HttpServletRequest request,HttpServletResponse res){
		String srolename = request.getParameter("rolename"); //角色名称
		boolean isExite = true;
		try {
			isExite = rService.existRole(srolename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("校验角色名称唯一性失败。",e);
		}
		if(isExite){
			return "true";
		}else{
			return "false";
		}
	}
	/**
	 * 
	* @Description: TODO(向角色中添加用户与角色建立关联关系) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=addUser")
	@ResponseBody
	public String addUser(HttpServletRequest request,HttpServletResponse res){
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"),"0"); //角色ID
		String suserIDs = CMyString.showEmpty(request.getParameter("userIDs"),"0"); //角色ID用,隔开
		try {
			AppUser user = CrtlUtil.getCurrentUser(request);
			rService.addRoleUser(Long.valueOf(sroleID), suserIDs, user);
			logService.addAppLog(3, "添加角色中的用户成功。",user);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			loger.error("角色中添加用户数据不合法。",e);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("角色中添加用户失败。",e);
		}
		String sJson = "";
		sJson = jsonUtil.getJsonStr("200","操作成功！", "t_roleUser","", "closeCurrent", "");
		return sJson;
	}
	/**
	 * 
	* Description: 根据角色查询相关的用户 <BR>   
	* @author zhangzun
	* @date 2014-3-14 下午12:01:18
	* @param request
	* @param res
	* @return 返回角色用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=findUser")
	public String findUser(HttpServletRequest request,HttpServletResponse res){
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"), "0"); //角色ID
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
		Page page = null;
		try {
			page = rService.findRoleUser(Long.valueOf(sroleID), "username", Integer.valueOf(currPage), Integer.valueOf(pageSize));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			loger.error("查询角色用户信息参数不合法。",e);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询角色用户信息失败。",e);
		}
		request.setAttribute("roleID",sroleID);
		request.setAttribute("page",page);
		request.setAttribute("users",page.getLdata());
		return "appadmin/role/role_user";
	}
	/**
	 * 
	* Description: 查询所有的组织 <BR>   
	* @author zhangzun
	* @date 2014-3-14 下午12:01:18
	* @param request
	* @param res
	* @return 返回角色添加用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=userList")
	public String userList(HttpServletRequest request,HttpServletResponse res){
		String sroleID = request.getParameter("roleID"); //角色ID
		List dept = null;//部门集合
		request.setAttribute("roleID", sroleID);
		request.setAttribute("dept", dept);
		return "appadmin/role/user_list";
	}
	/**
	 * 
	* Description: 根据部门和角色查询角色没有关联的用户 <BR>   
	* @author zhangzun
	* @date 2014-3-14 下午12:01:18
	* @param request
	* @param res
	* @return 用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=deptUser")
	public String deptUser(HttpServletRequest request,HttpServletResponse res){
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"), "0"); //角色ID
		String sdeptID = CMyString.showEmpty(request.getParameter("deptID"),"0"); //部门ID为null就查所有的用户
		String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
		String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
		String sAct = CMyString.showEmpty(request.getParameter("act"),"0"); //查询角色用户的标示字段
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
		Page page = null;
		String selID = "";//已选择的用户id
		String sField = null;
		if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
			sField = selectFiled;
		}
		
		try {
			if(!"1".equals(sAct)){
				selID = rService.findRoleUser(Long.valueOf(sroleID));
				request.setAttribute("selID", selID);
			}
			page = appGroupService.findGrpUserSel(Long.valueOf(sdeptID),sField,sFiledValue,Integer.valueOf(currPage),
					Integer.valueOf(pageSize));
			request.setAttribute("users",page.getLdata());
			request.setAttribute("page", page);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询组织用户信息参数不合法。",e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询组织用户信息失败。",e);
		}
		
		request.setAttribute("roleID", sroleID);
		request.setAttribute("deptID", sdeptID);
		request.setAttribute("sFiledValue", sFiledValue);
		request.setAttribute("selectFiled", selectFiled);
		
		return "appadmin/role/users";
	}
	/**
	 * 
	* @Description: TODO(删除角色与用户建立关联关系) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @param request
	* @param res
	* @return 返回到角色用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=delUser")
	@ResponseBody
	public String delUser(HttpServletRequest request,HttpServletResponse res){
		String roleUserIds = request.getParameter("ids"); //角色ID用,隔开
		String sJson = "";
		try {
			rService.delRoleUser(roleUserIds);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "t_roleUser","t_roleUser", "", "");
			logService.addAppLog(3, "删除角色下的用户成功。", CrtlUtil.getCurrentUser(request));
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			e.printStackTrace();
			loger.error("删除角色下的用户失败。",e);
		}
		return sJson;
	}
	/**
	 * 
	* @Description: TODO(删除角色与操作的关联关系) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=delOper")
	public String delOper(HttpServletRequest request,HttpServletResponse res){
		String sroleID = request.getParameter("roleID"); //角色ID
		String soperIDs = request.getParameter("operIDs"); //操作ID用,隔开
		//roleService.delOper(sroleID,soperIDs);//删除角色与操作的关联关系
		List<AppOper> list = null;
		//list = operService.findByRoleID(Long.valueOf(sroleID));
		request.setAttribute("list",list);
		request.setAttribute("roleID",sroleID);
		return "role_oper_list";
	}
	/**
	 * 
	* @Description: TODO(添加角色与操作的关联关系) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=addOper")
	@ResponseBody
	public String addOper(HttpServletRequest request,HttpServletResponse res){
		String sroleID = request.getParameter("roleID"); //角色ID
		String saddIDs = request.getParameter("addIDs"); //添加操作ID用,隔开
		String sappID = request.getParameter("appID"); //应用ID
		String sJson = "";
		try {
			AppUser user = CrtlUtil.getCurrentUser(request);
			rService.addRoleAppOper(Long.valueOf(sroleID), Long.valueOf(sappID), saddIDs,user);
			logService.addAppLog(1, "向应用中添加操作成功。", user);
			refreshCache();
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
		} catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300","传输的参数个数有误！", "","", "closeCurrent", "");
			e.printStackTrace();
			loger.error("参数不合法。",e);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			e.printStackTrace();
			loger.error("向应用中添加操作失败。",e);
		}
		return sJson;
	}
	/**
	 * 
	* Description:(查找所有的操作，及角色的关联信息) <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色列表页
	* @version 1.0
	 */
	@RequestMapping(params = "method=findOpers")
	public String findOpers(HttpServletRequest request,HttpServletResponse res){
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"),"0");	//角色id
		List<Object> operList = null;
		List<Object> appList = null;
		try {
			appList = rService.getRoleAppMap(Long.valueOf(sroleID));
			operList = rService.find("", AppOper.class.getName(),"", "crtime desc", null);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询应用操作信息失败。",e);
		}
		request.setAttribute("operList", operList);
		request.setAttribute("appList", appList);
		request.setAttribute("roleID", sroleID);
		return "appadmin/role/role_oper";
	}
	/**
	 * 
	* Description:查找所有的应用，以及与角色的关联信息。 <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回到角色列表页
	* @version 1.0
	 */
	@RequestMapping(params = "method=findApps")
	public String findApps(HttpServletRequest request,HttpServletResponse res){
		String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
		String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"),"0");	//角色id
		String selIDs = "";
		String sWhere = "";
		List<Object> param = null;
		List<Object> appList=null;
		try {
			sWhere = "deleted=?";
			param = new ArrayList<Object>();
			param.add(0);
			if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
				   sWhere += " and " +selectFiled + " like ?";
				   param = new ArrayList<Object>();
				   param.add("%"+sFiledValue+"%");
			}
			selIDs = rService.getRoleApp(Long.valueOf(sroleID));
			appList = rService.find("", AppInfo.class.getName(),sWhere, "crtime desc", param);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询角色应用信息失败。",e);
		}
		request.setAttribute("sFiledValue", sFiledValue);
		request.setAttribute("selectFiled", selectFiled);
		request.setAttribute("appList", appList);
		request.setAttribute("selIDs", selIDs);
		request.setAttribute("roleID", sroleID);
		return "appadmin/role/app_list";
	}
	/**
	 * 
	* Description:查找应用关联的操作id <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @return 返回操作id用，号隔开
	* @version 1.0
	 */
	@RequestMapping(params = "method=findAppOpers")
	@ResponseBody
	public String findAppOpers(HttpServletRequest request,HttpServletResponse res){
		
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"),"0");	//角色id
		String sappID = CMyString.showEmpty(request.getParameter("appID"),"0");	//应用id
		String selIDs = "";
		try {
			selIDs = rService.getRoleAppOper(Long.valueOf(sroleID), Long.valueOf(sappID));
			if(selIDs==null)
		    	selIDs = "";
		} catch (Exception e) {
			selIDs = "error";
			e.printStackTrace();
			loger.error("查询角色应用下的操作id失败。",e);
		}
		return selIDs;
	}
	/**
	 * 
	* Description:添加角色与应用的关联关系 <BR>   
	* @author zhangzun
	* @date 2014-3-8 下午12:01:18
	* @param request
	* @param res
	* @return 返回到角色信息列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=addApp")
	@ResponseBody
	public String addApp(HttpServletRequest request,HttpServletResponse res){
		String sroleID = request.getParameter("roleID"); //角色ID
		String saddIDs = request.getParameter("addIDs"); //添加应用ID用,隔开
		String sJson = "";
		try {
			AppUser user = CrtlUtil.getCurrentUser(request);
			rService.addRoleApp(Long.valueOf(sroleID), saddIDs,user);
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			logService.addAppLog(1, "向角色中添加应用成功", user);
		} catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			e.printStackTrace();
			loger.error("参数不合法",e);
			
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			e.printStackTrace();
			loger.error("向角色中添加应用失败。",e);
		}
		return sJson;
	}
	@RequestMapping(params = "method=editePage")
	public String editePage(HttpServletRequest request,HttpServletResponse res){
		String sroleID =  
		CMyString.showEmpty(request.getParameter("roleID"),"0");//角色ID
		AppRole role = null;
		
		if("0".equals(sroleID)){
			request.setAttribute("method", "addRole");
		}else{
			role = (AppRole) rService.findById(AppRole.class,Long.valueOf(sroleID));//根据id获取角色
			request.setAttribute("method", "upRole");
		}
		request.setAttribute("role", role);
		return "appadmin/role/role_edite";
	}
	@RequestMapping(params = "method=upFile")
	public String editePage(MultipartHttpServletRequest req,HttpServletResponse res){
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		List<MultipartFile> files = req.getFiles("file");
		
		try {
			commonService.SaveUploadFiles(files, 10200l, 10l);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	/**
	 * 刷新缓存
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-7-29 下午06:07:30
	* Last Modified:
	* @throws Exception
	* @version 1.0
	 */
	public void refreshCache() throws Exception{
		HashMap<Long, String> rolemap = new HashMap<Long, String>();
    	HashMap<Long, String> opermap = new HashMap<Long, String>();
    	List<Object> roleOpers= appBaseService.getCacheAppRoleOper();//加载角色与操作的对应关系
    	
    	for(Object objRoleOper : roleOpers){
    		StringBuffer str = new StringBuffer();
    		StringBuffer operStr = new StringBuffer();
    		AppRoleOper roleOper = (AppRoleOper)objRoleOper;
    		if(rolemap.containsKey(roleOper.getAppId())){
    			str.append(rolemap.get(roleOper.getAppId())).append(",");
    			if(str.indexOf(roleOper.getRoleId()+"")<1){ //TODO 这里有重复的值
    				str.append(roleOper.getRoleId());
    			}
    			rolemap.remove(roleOper.getAppId());
    			rolemap.put(roleOper.getAppId(), str.toString());
    		}else{
    			rolemap.put(roleOper.getAppId(), roleOper.getRoleId().toString());
    		}
    		if(opermap.containsKey(roleOper.getRoleId())){
    			operStr.append(opermap.get(roleOper.getRoleId())).append(",").append(roleOper.getOperId());
    			opermap.remove(roleOper.getRoleId());//TODO 这里有重复的值
    			opermap.put(roleOper.getRoleId(), operStr.toString());
    		}else{
    			opermap.put(roleOper.getRoleId(), roleOper.getOperId().toString());
    		}
    	}
    	CacheRoleOper.ROLEMAP = rolemap;
    	CacheRoleOper.OPERMAP = opermap;
	}
	/**
	 * Description: 设置应用管理员 <BR>   
	 * @author liu.zhuan
	 * @date 2014-12-16 下午06:10:20
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=setRoleAppAdmin")
	public String setRoleAppAdmin(HttpServletRequest request,HttpServletResponse res){
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"),"0");	//角色id
		String suserID = CMyString.showEmpty(request.getParameter("userID"),"0");	//用户id
		String selIDs = "";
		List<Object> appList = null;
		try {
			selIDs = rService.getRoleSysUser(Long.valueOf(sroleID), Long.valueOf(suserID));
			appList = rService.getRoleAppMap(Long.valueOf(sroleID));
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询角色应用信息失败。",e);
		}
		request.setAttribute("appList", appList);
		request.setAttribute("selIDs", selIDs);
		request.setAttribute("roleID", sroleID);
		request.setAttribute("userID", suserID);
		return "appadmin/role/role_app_list";
	}
	/**
	 * 
	* @Description:设置选择用户为角色下应用的管理员 <BR>   
	* @author liu.zhuan
	* @date 2014-3-8 下午12:01:18
	* @param request
	* @param res
	* @return 返回到角色用户列表
	* @version 1.0
	 */
	@RequestMapping(params = "method=toAppAdminUser")
	@ResponseBody
	public String toAppAdminUser(HttpServletRequest request,HttpServletResponse res){
		String sroleID = CMyString.showEmpty(request.getParameter("roleID"),"0");	//角色id
		String suserID = CMyString.showEmpty(request.getParameter("userID"),"0");	//用户id
		String roleAppIds = request.getParameter("addIDs"); //角色ID用,隔开
		//String flag = CMyString.showEmpty(request.getParameter("flag"), "0");//0表示添加为管理员，1表示取消为管理员
		String sJson = "";
		try {
			rService.setUserToAppAdmin(roleAppIds, Long.valueOf(suserID), Long.valueOf(sroleID));
			sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			logService.addAppLog(3, "设置角色下应用管理员成功。", CrtlUtil.getCurrentUser(request));
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", "");
			e.printStackTrace();
			loger.error("设置角色下应用管理员失败。",e);
		}
		return sJson;
	}
}
