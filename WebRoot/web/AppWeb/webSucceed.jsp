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
<title>提交成功提示页</title>
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
    <div class="i_Gr_6 mb10">提交成功</div>
    <div class="i_GrP_3">
		<p><img src="<%=basePath%>web/images/hd_ico9.gif"/></p>
		<c:if test="${isHasQueryNo == 0}"> <!-- 判断是否有查询密码-->
       <p>恭喜您！您的信息已成功提交！</p>
    	</c:if>
		<c:if test="${isHasQueryNo == 1}"> <!-- 判断是否有查询密码-->
       <p>恭喜您！您的信息已成功提交！请牢记以下查询编码和密码！</p>
       <p>查询编码：<span class="mr20 i_GrP_4">${QUERYNUMBER}</span>查询密码：<span class="i_GrP_4">${QUERYPWD}</span></p>
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
