<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.trs.util.Global"%>
<%
	if(session.getAttribute(Global.CURR_LOGIN_USER) == null){
		//用户未登录或者登录超时
		response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+"login.do?method=login");
	}
%>
