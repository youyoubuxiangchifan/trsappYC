/**
 * 
 */
package com.trs.web;

import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trs.infra.util.CMyString;
import com.trs.model.AppUser;
import com.trs.service.AppLogService;
import com.trs.service.AppUserAuthService;
import com.trs.service.AppUserService;
import com.trs.util.CMySign;
import com.trs.util.CrtlUtil;
import com.trs.util.DateUtil;
import com.trs.util.Global;
import com.trs.util.RSAUtil;


/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: UserAuthLoginController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2015-3-26 下午04:22:01
 * @version 1.0
 */
@Controller
@RequestMapping("/authlogin.do")
public class UserAuthLoginController {
	private static  Logger loger =  Logger.getLogger(UserLoginController.class);
	@Autowired
	private AppUserAuthService userAuthService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppLogService appLogService;
	/**
	 * 
	* Description: wcm用户登录 <BR>   
	* @author jin.yu
	* @date 2014-3-28 下午11:21:17
	* @param request
	* @param res
	* @return String 跳转jsp页面
	* @version 1.0
	 */
	@RequestMapping(params = "method=wcmAuthLogin")
	public String loginUser(HttpServletRequest request,HttpServletResponse response){
		String errMsg = "";//错误信息
		HttpSession session = CrtlUtil.getSession(request);
		String authInfo = request.getParameter("RandomInfo");//加密的验证字符串
		//判断用户session是否已经存在
		AppUser appLoginUser = CrtlUtil.getCurrentUser(request);
		if(appLoginUser != null){
			return "appadmin/appMgr";
		}
		if(CMyString.isEmpty(authInfo)){
			errMsg = "第三方非法登录！";
			request.setAttribute("errMsg", errMsg);
			return "appadmin/login";
		}
		System.out.println("加密字符串："+authInfo);
		AppUser appUser = userAuthService.checkEncodeInfoFromKey(authInfo);
		try {
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
					msg.append("用户").append(appUser.getUsername()).append("于").append(DateUtil.nowFormat()).append("通过WCM登录系统");
					appLogService.addAppLog(Global.LOGS_LOGIN, msg.toString(), appUser);//保存日志
					appUserService.update(appUser);
					return "appadmin/appMgr";
				}else{
					errMsg = "该用户已被停用！";
				}
			}else {
				errMsg = "通过WCM登录失败！";
			}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("用户从WCM登录操作失败。", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("errMsg", errMsg);
		return "appadmin/login";
	}
	
}
