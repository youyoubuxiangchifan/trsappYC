package com.trs.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.trs.dao.AppGroupDao;
import com.trs.model.AppUser;
import com.trs.service.AppUserService;
import com.trs.util.CMyString;
import com.trs.util.Global;

public class AdminLoginFilter implements Filter  {
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger=Logger.getLogger(AdminLoginFilter.class);
	//FilterConfig可用于访问Filter的配置信息
	private FilterConfig config;
	
	public void init(FilterConfig filterConfig) {
		this.config = filterConfig;
	}
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		try {				
			//如果没有登录，就返回登录页重新登录
			if (request.getSession().getAttribute(Global.CURR_LOGIN_USER) == null) {
				String notFilterJsp = config.getInitParameter("notFilterJsp");
				StringBuffer fromUrl = new StringBuffer(request.getRequestURL().toString());
				if(!CMyString.isEmpty(request.getQueryString())){
					fromUrl.append("?").append(request.getQueryString());
				}
				if(chkNotFilterJsp(fromUrl.toString(),notFilterJsp)){
					filterChain.doFilter(servletRequest, servletResponse);
				}else{
					request.getSession().setAttribute("fromUrl", fromUrl);
					//request.getRequestDispatcher("/login.do?method=login").forward(request, response);
					response.sendRedirect(request.getContextPath()+"/login.do?method=login");
					return;
				}
			} else {/** 如果已经登录 */
				// 获取当前会员信息
				AppUser loginUser = ((AppUser) request.getSession().getAttribute(Global.CURR_LOGIN_USER));
				//获取当前会员SEESIONID
				//String sessionID = request.getSession().getId();
				//
				String adminFilterPath = config.getInitParameter("adminFilterPath");
				String fromUrl = request.getRequestURL().toString()+"?"+request.getQueryString();
				if(chkNotFilterJsp(fromUrl,adminFilterPath)){
					if(loginUser.getUsertype()==Global.USER_IS_ADMIN){
						filterChain.doFilter(servletRequest, servletResponse);
					}else{
						response.sendRedirect(request.getContextPath()+"/appadmin/message.jsp");
					}
				}else{
					if(chkNotFilterJsp(fromUrl,"group.do")){
						if(chkNotFilterJsp(fromUrl,"method=getTreeJosn")){
							filterChain.doFilter(servletRequest, servletResponse);
						}else{
							String groupIds = CMyString.isEmpty(request.getParameter("groupIds"))?"0":request.getParameter("groupIds");
							String userGroupIdString = loginUser.getIsAdminGroup();
							if(loginUser.getUsertype()!=Global.USER_IS_ADMIN){
								if(userGroupIdString != null){
									if(userGroupIdString.indexOf(groupIds)>=0){
										filterChain.doFilter(servletRequest, servletResponse);
									}else{
										response.sendRedirect(request.getContextPath()+"/appadmin/message.jsp");
									}
								}else{
									response.sendRedirect(request.getContextPath()+"/appadmin/message.jsp");
								}
							}else{
								filterChain.doFilter(servletRequest, servletResponse);
							}
						}
					}else{
						filterChain.doFilter(servletRequest, servletResponse);
					}
				}
			}		
			
		} catch (Exception e) {
			logger.error("用户过滤器出错",e);
		}
		
	}
	
	/**
	 * 
	* Description:  <BR>   
	* @author jin.yu
	* @date 2014-4-5 下午04:23:06
	* @param fromUrl 请求URL地址
	* @param notFilterJsp 不需要过滤的jsp或do请求
	* @return boolean false为需要过滤，true为不需要过滤
	* @version 1.0
	 */
	public boolean chkNotFilterJsp(String fromUrl,String notFilterJsp){
		boolean checkFalg = false;
		String[] arrNotFilterJsp = notFilterJsp.split(",");
		for (String str : arrNotFilterJsp) {
			if(fromUrl.indexOf(str)>=0){
				checkFalg = true;
				break;
			}
		}
		return checkFalg;
	}
	
	}
