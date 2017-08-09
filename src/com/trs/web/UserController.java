/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: UserController
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午04:52:49
 * @version 1.0
 */
package com.trs.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.trs.dbhibernate.Page;
import com.trs.model.AppUser;
import com.trs.service.AppLogService;
import com.trs.service.AppUserService;
import com.trs.util.CMyString;
import com.trs.util.Global;
import com.trs.util.MD5;
import com.trs.util.RSAUtil;


/**
 * Description: TODO(描述类的作用)<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: UserController
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-3-17 下午06:24:27
 * version 1.0
 */
@Controller
@RequestMapping("/user.do")
public class UserController {
	private static  Logger LOG =  Logger.getLogger(UserController.class);
	//AppUser LoginUser= new AppUser();
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppLogService appLogService;
	
	/** 
	 * 查询用户列表信息
	* Description: 查询用户列表信息 <BR>   
	* author liujian
	* date 2014-3-12 下午04:06:02
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=listUser")
	public String listUser(HttpServletRequest request,HttpServletResponse res){
		String userStatus = CMyString.isEmpty(request.getParameter("userStatus"))?"0":request.getParameter("userStatus");//当前用户列表状态
		String selectFiled = request.getParameter("selectFiled");	//检索字段
		String sFiledValue = request.getParameter("sFiledValue");	//检索字段值
		String weakPasswd = request.getParameter("weakPasswd");	//检索字段值
		String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?"1":request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"20":request.getParameter("numPerPage");
		String orderField = CMyString.isEmpty(request.getParameter("orderField"))?"userId":request.getParameter("orderField");
		String orderDirection = CMyString.isEmpty(request.getParameter("orderDirection"))?"desc":request.getParameter("orderDirection");		
		String sWhere ="isDeleted != 1";
		String sOrder="userId desc";
		List<Object> param = new ArrayList<Object>();
		
		if(!"-1".equals(userStatus)){//判断列表状态，为空或空对象则查询所有用户
			sWhere =sWhere+" and status = ? ";
			param.add(Integer.parseInt(userStatus));			
		}
		if(!CMyString.isEmpty(sFiledValue)){
			sWhere =sWhere+" and "+selectFiled+" like ?";
			param.add("%"+sFiledValue+"%");
		}
		if(!CMyString.isEmpty(weakPasswd)){
			sWhere =sWhere+" and weakPasswd = ? ";
			param.add(Integer.parseInt(weakPasswd));
		}
		if(!CMyString.isEmpty(orderField)&&!CMyString.isEmpty(orderDirection)){
			sOrder=orderField+" "+orderDirection;
		}
		Page page = null;
		
		try {
			//appLogService.addAppLog(Global., , appUser);
			
			page = appUserService.findPage("","AppUser",sWhere,sOrder,Integer.parseInt(currPage),Integer.parseInt(pageSize),param);
			
		} catch (Exception e) {
			LOG.error("获取用户列表异常！");
			e.printStackTrace();
		}

		request.setAttribute("page",page);
		request.setAttribute("userStatus",userStatus);
		request.setAttribute("selectFiled",selectFiled);
		request.setAttribute("sFiledValue",sFiledValue);
		request.setAttribute("orderField",orderField);
		request.setAttribute("orderDirection",orderDirection);
		request.setAttribute("weakPasswd",weakPasswd);
		
		request.setAttribute("userList", page.getLdata());
		return "appadmin/user/user_list";
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
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		switch (Integer.parseInt(pageFlag)) {
			case 0: //跳转到新建用户页面
				returnUrl="appadmin/user/user_add";
				break;
			case 1: //跳转到用户信息页面
				returnUrl="appadmin/user/user_info";
				appUser = (AppUser)appUserService.findById(AppUser.class, Long.valueOf(userIDs));
				request.setAttribute("appUser", appUser);			
				break;
			case 2: //跳转到重置密码页面
				returnUrl="appadmin/user/user_restPwd";
				appUser = (AppUser)appUserService.findById(AppUser.class, Long.valueOf(userIDs));
				request.setAttribute("appUser", appUser);			
				break;			
			case 3: //跳转到修改用户页面
				returnUrl="appadmin/user/user_eddit";
				appUser = (AppUser)appUserService.findById(AppUser.class, Long.valueOf(userIDs));
				request.setAttribute("appUser", appUser);
				request.setAttribute("loginUser", loginUser);
				break;
			case 4: //跳转到前台修改用户页面
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
	* Description: 查看用户信息 <BR>   
	* author liujian
	* date 2014-3-17 下午06:20:59
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=userInfo")
	public String userInfo(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus");//当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id
		AppUser appUser= new AppUser();
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		if(loginUser==null)
			loginUser = new AppUser();
			loginUser.setUserId(74l);
		try {
			
			appUser=(AppUser)appUserService.findById(AppUser.class,Long.valueOf(userIDs));
			appLogService.addAppLog(Global.LOGS_MODIFY, "查看用户信息", loginUser);
		} catch (NumberFormatException e) {
			LOG.error("查看用户信息时用户ID类型转换异常！");
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error("查看用户信息时转换用户对象异常！");
			e.printStackTrace();
		}	
		request.setAttribute("currUser",appUser);
		request.setAttribute("userStatus",userStatus);		
		return "forward:/user.do?method=listUser";
	}
	
	
	/** 
	* Description: 新增用户 <BR>   
	* author liujian
	* date 2014-3-17 下午06:20:36
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=addUser")
	@ResponseBody
	public String addUser(HttpServletRequest request,HttpServletResponse res){
		String userStatus = "0";//新建用户默认设置为开通状态
		String userName = request.getParameter("userName");
		String trueName = request.getParameter("trueName");
		String userPwd = request.getParameter("userPwd");
		String moblie = request.getParameter("moblie");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String zipcode = request.getParameter("zipcode");
		String userType = request.getParameter("userType");
		String idWeakPwd = CMyString.isEmpty(request.getParameter("weakPasswd"))?"0":request.getParameter("weakPasswd");
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
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("新建用户失败！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");
		try {
			appUserService.addUser(appUser);
			appLogService.addAppLog(Global.LOGS_SAVE, "新建用户", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("新建用户成功！")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_SAVE, "新建用户异常", loginUser);
			LOG.error("用户管理新建用户异常！");
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
	* Description: 逻辑删除用户 <BR>   
	* author liujian
	* date 2014-3-17 下午05:15:22
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=delUser")
	@ResponseBody
	public String delUser(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus");//当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id集合
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("删除用户失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";		
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appUserService.updateUserStatus(userIDs,Global.USER_STATUS_DEL);
			appLogService.addAppLog(Global.LOGS_DEL, "删除用户[id="+userIDs+"]", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("成功删除用户!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
		} catch (Exception e) {	
			appLogService.addAppLog(Global.LOGS_DEL, "删除用户[id="+userIDs+"]异常", loginUser);
			LOG.error("用户管理逻辑删除用户异常！");
			e.printStackTrace();
		}
		//request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}

	/** 
	* Description: 彻底删除用户 <BR>   
	* author liujian
	* date 2014-3-17 下午06:23:20
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=phyDelUser")
	@ResponseBody
	public String phyDelUser(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus");//当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id集合	
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("彻底删除用户失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";		
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appUserService.deleteUser(userIDs);
			appLogService.addAppLog(Global.LOGS_DEL, "彻底删除用户[id="+userIDs+"]", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("彻底删除用户成功!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";			
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_DEL, "彻底删除用户[id="+userIDs+"]异常", loginUser);
			LOG.error("用户管理彻底删除用户异常！");
			e.printStackTrace();
		}
		request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}	

	/** 
	* Description: 停用已开通的用户 <BR>   
	* author liujian
	* date 2014-3-17 下午06:23:53
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=stopUser")
	@ResponseBody
	public String stopUser(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus");//当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id集合	
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("停用用户失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";		
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appUserService.updateUserStatus(userIDs,Global.USER_STATUS_FORBID);
			appLogService.addAppLog(Global.LOGS_MODIFY, "停用用户[id="+userIDs+"]", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("停用用户成功!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";			
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "停用用户[id="+userIDs+"]异常", loginUser);
			LOG.error("用户管理停用用户异常！");
			e.printStackTrace();
		}

		request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}
	
	/** 
	* Description: 开通已停用的用户 <BR>   
	* author liujian
	* date 2014-3-17 下午06:24:13
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=openUser")
	@ResponseBody
	public String openUser(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus");//当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id集合
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("开通用户失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";		
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appUserService.updateUserStatus(userIDs,Global.USER_STATUS_REG);
			appLogService.addAppLog(Global.LOGS_MODIFY, "开通用户", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("开通用户成功!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";			
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "开通用户["+userIDs+"]异常", loginUser);
			LOG.error("用户管理逻辑开通用户异常！");
			e.printStackTrace();
		}
		request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}	

	/** 
	* Description: 恢复逻辑删除的用户 <BR>   
	* author liujian
	* date 2014-3-17 下午06:24:35
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=reUser")
	@ResponseBody
	public String reUser(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus");//当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id集合
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("恢复用户失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";		
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appUserService.updateUserStatus(userIDs,Global.USER_STATUS_REG);
			appLogService.addAppLog(Global.LOGS_MODIFY, "恢复用户", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("回复用户成功!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"\",\"forwardUrl\":\"\"}";
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "恢复用户异常", loginUser);
			LOG.error("用户管理恢复用户异常！");
			e.printStackTrace();
		}
		request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}

	
	/** 
	* Description: 管理员重置用户密码 <BR>   
	* author liujian
	* date 2014-3-17 下午06:24:54
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=resetPassword")
	@ResponseBody
	public String resetPassword(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus"); //当前用户列表状态
		String userIDs = request.getParameter("userIDs");	//用户id
		String newPwd = request.getParameter("newPwd");    //用户新密码
		String idWeakPwd = CMyString.isEmpty(request.getParameter("weakPasswd"))?"0":request.getParameter("weakPasswd");
		String josnStr="{\"statusCode\":\"300\",\"message\":\""+CMyString.native2Ascii("重置密码失败!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";		
		AppUser appUser=null;
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		try {
			appUser = (AppUser)appUserService.findById(AppUser.class,Long.valueOf(userIDs));
			appUserService.resetPassWord(appUser, newPwd,Integer.parseInt(idWeakPwd));
			appLogService.addAppLog(Global.LOGS_MODIFY, "重置用户密码", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("重置密码成功!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
		} catch (NumberFormatException e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "重置用户密码异常", loginUser);
			LOG.error("用户管理重置用户密码时用户ID类型转换异常！");
			e.printStackTrace();
		} catch (Exception e) {
			appLogService.addAppLog(Global.LOGS_MODIFY, "重置用户密码异常", loginUser);
			LOG.error("用户管理重置用户密码异常！");
			e.printStackTrace();
		}
		
		request.setAttribute("userStatus",userStatus);		
		return josnStr;
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
			appUserService.resetPassWord(appUser, newPwd, currPwd,Integer.parseInt(idWeakPwd));
			appLogService.addAppLog(Global.LOGS_MODIFY, "修改用户[id="+userIDs+"]密码", loginUser);
			josnStr="{\"statusCode\":\"200\",\"message\":\""+CMyString.native2Ascii("修改密码成功!")+"\",\"navTabId\":\"\",\"rel\":\"\",\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"\"}";
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
	* Description: 检查用户名唯一性 <BR>   
	* author liujian
	* date 2014-3-13 下午07:37:10
	* @param request
	* @param res
	* @return
	* version 1.0
	*/
	@RequestMapping(params = "method=checkUserName")
	@ResponseBody
	public String checkUserName(HttpServletRequest request,HttpServletResponse res){
		String userStatus = request.getParameter("userStatus");//当前用户列表状态
		String userName = request.getParameter("userName");	//用户id集合
		String josnStr="false";		
		try {
			if(!appUserService.existUser(userName)){
				josnStr="true";	
			}
						
		} catch (Exception e) {
			LOG.error("检查用户是否存在时异常！");
			e.printStackTrace();
		}
		//request.setAttribute("userStatus",userStatus);		
		return josnStr;
	}
	
	/** 
	* Description: Excel文件形式导入用户 <BR>   
	* author liujian
	* date 2014-3-17 下午06:25:24
	* @param request
	* @param res
	* version 1.0
	*/
	@RequestMapping(params = "method=userUpload")
	//@ResponseBody	
	public void userUpload(MultipartHttpServletRequest request,HttpServletResponse res){
		String josnStr="{\"succeedUser\":\"0\",\"navTabId\":\"navTab\",\"message\":\""+CMyString.native2Ascii("文件格式不正确！")+"\",\"succeedUser\":\"0\",\"failUser\":\"0\"}";
		MultipartFile file = request.getFile("file");
		FileOutputStream foStream  = null;
		String statusCode = "300";
		AppUser loginUser = (AppUser)	request.getSession().getAttribute("loginUser");//获取当前登陆用户
		String fileName = file.getOriginalFilename();
		String type =fileName.substring(fileName.lastIndexOf("."), fileName.length());

		Map<String, Object> returnMsg = new HashMap<String, Object>();
		if(file!=null &&(".xls".equals(type) || ".xlsx".equals(type))){
			try {				
				java.io.File nFile = new java.io.File(ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+"/appadmin/"+file.getOriginalFilename());				
				foStream  = new FileOutputStream(nFile);
				foStream.write(file.getBytes());
								
				returnMsg = appUserService.importUser(nFile,loginUser);
				appLogService.addAppLog(Global.LOGS_MODIFY, "批量导入用户", loginUser);
				statusCode ="203";
			} catch (IOException e) {
				appLogService.addAppLog(Global.LOGS_MODIFY, "批量导入用户异常", loginUser);
				josnStr="{\"statusCode\":\""+statusCode+"\",\"navTabId\":\"navTab\",\"message\":\""+CMyString.native2Ascii("文件为空或格式不正确！")+"\",\"succeedUser\":\""+returnMsg.get("successUser")+"\",\"failUser\":\""+returnMsg.get("failUser")+"\"}";
				e.printStackTrace();
			} catch (Exception e) {
				appLogService.addAppLog(Global.LOGS_MODIFY, "批量导入用户异常", loginUser);
				josnStr="{\"statusCode\":\""+statusCode+"\",\"navTabId\":\"navTab\",\"message\":\""+CMyString.native2Ascii("文件为空或格式不正确！")+"\",\"succeedUser\":\""+returnMsg.get("successUser")+"\",\"failUser\":\""+returnMsg.get("failUser")+"\"}";
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
			josnStr="{\"statusCode\":\""+statusCode+"\",\"navTabId\":\"navTab\",\"message\":\""+CMyString.native2Ascii("成功导入用户！")+"\",\"succeedUser\":\""+returnMsg.get("successUser")+"\",\"failUser\":\""+returnMsg.get("failUser")+"\"}";
		}
		try {
			res.getWriter().write(josnStr);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	/** 
	* Description: Excel文件形式导入用户 <BR>   
	* author liujian
	* date 2014-3-17 下午06:25:24
	* @param request
	* @param res
	* version 1.0
	*/
	@RequestMapping(params = "method=returnUploadInfo")	
	public String returnUploadInfo(HttpServletRequest request,HttpServletResponse res){		
		String succeedUser = CMyString.isEmpty(request.getParameter("succeedUser"))?"0":request.getParameter("succeedUser");
		String failUser = CMyString.isEmpty(request.getParameter("failUser"))?"0":request.getParameter("failUser");
		request.setAttribute("succeedUser", succeedUser);
 		request.setAttribute("failUser", failUser); 		
		return "appadmin/user/returnUploadInfo";
	}
	
	/**
	 * 
	* Description: 从WCM中抽取所有已开通的用户到互动平台中 <BR>   
	* @author jin.yu
	* @date 2014-4-16 下午05:05:04
	* @version 1.0
	 */
	@RequestMapping(params = "method=importWcmUser")	
	public void importWcmUser(HttpServletRequest request,HttpServletResponse res){
		try {
			AppUser loginUser = (AppUser)request.getSession().getAttribute("loginUser");//获取当前登陆用户
			appUserService.importWcmUser(loginUser);
		} catch (Exception e) {
			LOG.error("导入WCM用户失败！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
