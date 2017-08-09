<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<title>错误提示页</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/reset.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/layout.css"/>
<script src="<%=basePath%>web/images/jquery-1.7.2.min.js" type="text/javascript"></script>
</head>

<body>
<!--star header-->
<jsp:include page="../include/webheader.jsp" />
<!--end header-->
<div class="w">
  <div class="fr B mb10 i_Gall">
    <div class="i_Gr_6 mb10">提交失败</div>
    <div class="i_GrP_2"><p><img src="<%=basePath%>web/images/hd_ico8.gif"/></p>
    <c:if test="${empty Msg}"> <!-- 判断是否有提示信息-->
	<p>对不起您提交的数据不正确！请核对后再提交！</p>
	</c:if>
	<c:if test="${!empty Msg}"> <!-- 判断是否有提示信息-->
	<p>${Msg}</p>
	</c:if>
	</div>
  </div>
  <div class="clear"></div>
</div>
<!--star footer-->
<jsp:include page="../include/webfooter.jsp"  /> 
<!--end footer-->
</body>
</html>
