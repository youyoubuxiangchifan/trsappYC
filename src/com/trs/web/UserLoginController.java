/**   
* Description: TODO 
* Title: UserLoginController.java 
* @Package com.trs.web 
* @author jin.yu 
* @date 2014-3-28 下午11:07:38 
* @version V1.0   
*/
package com.trs.web;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trs.model.AppUser;
import com.trs.service.AppLogService;
import com.trs.service.AppUserService;
import com.trs.util.CrtlUtil;
import com.trs.util.DateUtil;
import com.trs.util.Global;
import com.trs.util.RSAUtil;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: UserLoginController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-28 下午11:07:38
 * @version 1.0
 */
@Controller
@RequestMapping("/login.do")
public class UserLoginController {
	private static  Logger loger =  Logger.getLogger(UserLoginController.class);
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppLogService appLogService;
	
	/**
	 * 
	* Description: 用户登入验证模块 <BR>   
	* @author jin.yu
	* @date 2014-3-28 下午11:21:17
	* @param request
	* @param res
	* @return String 跳转jsp页面
	* @version 1.0
	 */
	@RequestMapping(params = "method=loginUser")
	public String loginUser(HttpServletRequest request,HttpServletResponse res){
		String userName = request.getParameter("userName");//用户名
		String passWord = request.getParameter("passWord");	//用户密码
		String loginCaptcha = request.getParameter("loginCaptcha");	//验证码
		//判断用户session是否已经存在
		AppUser appLoginUser = CrtlUtil.getCurrentUser(request);
		if(appLoginUser != null){
			return "appadmin/appMgr";
		}
		System.out.println("密码"+passWord);
		if(userName != null && passWord != null && !"".equals(userName) && !"".equals(passWord) ){
			String errMsg = "";//错误信息
			HttpSession session = request.getSession();
			String sessionCaptcha = (String)session.getAttribute("loginCaptcha");
//			if (loginCaptcha != null && !"".equals(loginCaptcha) && sessionCaptcha != null && !"".equals(sessionCaptcha) && loginCaptcha.toUpperCase().equals(sessionCaptcha.toUpperCase())) {
//				byte[] en_result = new BigInteger(passWord, 16).toByteArray();
//				byte[] en_result = RSAUtil.hexStringToByte(passWord);
				//update by liuzhuan：修复用户密码重置后无法登录问题
			byte[] en_result = RSAUtil.hexStringToBytes(passWord);
			try {
				//对用户登录密码进行解密
				byte[] de_result = RSAUtil.decrypt(RSAUtil.getKeyPair().getPrivate(),en_result);
				StringBuffer sb = new StringBuffer();
				sb.append(new String(de_result));
				passWord = sb.reverse().toString();//对解密后的用户密码调整顺序
				//passWord = request.getParameter("passWord");	//用户密码  TODO 临时
				if(appUserService.existUser(userName)){
					AppUser appUser = appUserService.loginUser(userName, passWord);
					if (appUser != null) {
						if(appUser.getStatus() == 0){
							//取得用户的操作IP
							String loginIp = CrtlUtil.getRealIp(request);
							appUser.setIp(loginIp);//设定用户操作IP
							appUser.setIsAdminGroup(appUserService.getUserIsAdminGroupIds(appUser.getUserId()));//取得管理员组织
							//将登录用户对象保存到session中
							session.setAttribute(Global.CURR_LOGIN_USER, appUser);
							//用户角色和用户是否管理员 添加到sessoin wen
							String roleIds = appUserService.getUserRoleIds(appUser.getUserId());
							session.setAttribute("isAdmin",appUserService.isAdminUser(appUser.getUserId()));
							session.setAttribute("roleIds", roleIds);
							//记录用户登录日志
							StringBuffer msg = new StringBuffer();
							msg.append("用户").append(userName).append("于").append(DateUtil.nowFormat()).append("登录系统");
							appLogService.addAppLog(Global.LOGS_LOGIN, msg.toString(), appUser);//保存日志
							appUser.setLoginErrCount(0);
							appUserService.update(appUser);
							return "appadmin/appMgr";
						}else{
							errMsg = "该用户已被停用！";
						}
					}else {
						AppUser appUserErr = (AppUser)appUserService.findObject(AppUser.class.getName(),"username=?",userName);
						int errCount = 0;
						if(appUserErr.getLoginErrCount() == null){
							errCount = 0;
						}else{
							errCount = appUserErr.getLoginErrCount();
						}
						if(errCount>10){
							appUserErr.setStatus(Global.USER_STATUS_FORBID);//超过10次停用用户
						}else{
							errCount++;
							appUserErr.setLoginErrCount(errCount);//设定密码错误次数
						}
						
						appUserService.update(appUserErr);
						errMsg = "用户密码错误！";
					}
				}else{
					errMsg = "用户名不存在！";
				}
			} catch (Exception e) {
				loger.error("用户登录操作失败。", e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			}else{
//				errMsg = "验证码错误！";
//			}
			
			RSAPublicKey rsap;
			try {
				rsap = (RSAPublicKey) RSAUtil.getKeyPair().getPublic();
				String module = rsap.getModulus().toString(16);
				String empoent = rsap.getPublicExponent().toString(16);
				request.setAttribute("m", module);
				request.setAttribute("e", empoent);
				request.setAttribute("errMsg", errMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "appadmin/login";
		}else{
			RSAPublicKey rsap;
			try {
				rsap = (RSAPublicKey) RSAUtil.getKeyPair().getPublic();
				String module = rsap.getModulus().toString(16);
				String empoent = rsap.getPublicExponent().toString(16);
				request.setAttribute("m", module);
				request.setAttribute("e", empoent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "appadmin/login";
		}
	}
	
	/**
	 * 
	* Description: 用户登入加密参数取得 <BR>   
	* @author jin.yu
	* @date 2014-3-28 下午11:23:23
	* @param request
	* @param res
	* @return String 跳转jsp页面
	* @version 1.0
	 */
	@RequestMapping(params = "method=login")
	public String login(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = CrtlUtil.getCurrentUser(request);
		if(appUser != null){
			return "appadmin/appMgr";
			//return "appindex";
		}
		RSAPublicKey rsap;
		try {
			rsap = (RSAPublicKey) RSAUtil.getKeyPair().getPublic();
			String module = rsap.getModulus().toString(16);
			String empoent = rsap.getPublicExponent().toString(16);
			request.setAttribute("m", module);
			request.setAttribute("e", empoent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("获取系统配置信息列表失败。", e);
			e.printStackTrace();
		}
		return "appadmin/login";
	}
	
	/**
	 * 
	* Description: 用户退出功能 <BR>   
	* @author jin.yu
	* @date 2014-4-4 上午11:23:34
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=logoutUser")
	public String logoutUser(HttpServletRequest request,HttpServletResponse res){
		AppUser appUser = CrtlUtil.getCurrentUser(request);
		if(appUser != null){
			//记录用户登录日志
			StringBuffer msg = new StringBuffer();
			msg.append("用户").append(appUser.getUsername()).append("于").append(DateUtil.nowFormat()).append("退出系统");
			appLogService.addAppLog(Global.LOGS_LOGIN, msg.toString(), appUser);//保存日志
		}
		request.getSession().removeAttribute(Global.CURR_LOGIN_USER);
		RSAPublicKey rsap;
		try {
			rsap = (RSAPublicKey) RSAUtil.getKeyPair().getPublic();
			String module = rsap.getModulus().toString(16);
			String empoent = rsap.getPublicExponent().toString(16);
			request.setAttribute("m", module);
			request.setAttribute("e", empoent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "appadmin/login";
	}
	
	/**
	 * 
	* Description: 生成互动平台用户登录模块的RSA加密文件 <BR>   
	* @author jin.yu
	* @date 2014-4-16 下午05:00:40
	* @version 1.0
	 */
	@RequestMapping(params = "method=createRSAFile")
	public void createRSAFile(){
		try {
			RSAUtil.getKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("生成RSA加密文件失败。", e);
			e.printStackTrace();
		}
	}
}
