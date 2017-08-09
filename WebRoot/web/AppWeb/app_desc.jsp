<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>信件详情</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/reset.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/layout.css"/>
<script src="<%=basePath%>web/images/jquery-1.7.2.min.js" type="text/javascript"></script>
<style> 
body,div,ul,li,p{margin:0;padding:0;}
body{color:#666;font:12px/1.5 Arial;}
ul{list-style-type:none;}
.star{position:relative;width:600px;margin:10px auto;margin-left:0px;}
.star ul,.star span{float:left;display:inline;height:19px;line-height:19px;}
.star ul{margin:0 10px;}
.star li{float:left;width:24px;cursor:pointer;text-indent:-9999px;background:url(<%=basePath%>appadmin/images/star.png) no-repeat;}
.star strong{color:#f60;padding-left:10px;}
.star li.on{background-position:0 -28px;}
.star p{position:absolute;top:20px;width:159px;height:30px;display:none;background:url(<%=basePath%>appadmin/images/icon.gif) no-repeat;padding:7px 10px 0;}
.star p em{color:#f60;display:block;font-style:normal;}
</style>
</head>

<body>
<!-- START header --> 
<jsp:include page="../include/webheader.jsp"/> 
<!-- END header -->
<div class="w">
  <div class="fl mr10 mb10 B i_GL">
    <div class="i_GL_1"><span>公众参与</span></div>
    <ul class="i_GL_2">
      <c:forEach items="${requestScope.appList}" var="app">
     	 <li <c:if test="${app.appId==appId}">class="i_GL_2H"</c:if>>
     	 		<%@ include file="../include/left.jsp"%> 
     	 </li>
     </c:forEach>
    </ul>
  </div>
  <div class="fr B mb10 i_Gr">
    <div class="i_Gr_6 mb10">应用提交说明</div>
    	<span style="float:right;"><input type="button" class="i_Gr_1_btn1" value="新增" onclick="addPage('${type}');"/></span>
	<div align="center" style="margin-top: 50px;">
		${appInfo.smtDesc}
		<c:if test="${appInfo.smtDesc==null}">
			抱歉，应用没有设置提交说明。
		</c:if>
	</div>
	
  </div>

  <div class="clear"></div>
   <form action="" id="addForm" method="post">
	     	<input type="hidden" id="isNeedTheme" name="isNeedTheme" value="${isNeedTheme}"/>
	     	<input type="hidden" name="appId" value="${appId}"/>
	     	<input type="hidden" name="groupId" value="${groupId}"/>
	     	<input type="hidden" name="dataId" value="${dataId}"/>
   </form>
</div>
<!-- START footer --> 
<jsp:include page="../include/webfooter.jsp"  /> 
<!-- END footer --> 
</body>
<script type="text/javascript">
    function addPage(_type){
    	var _url = '';
    	if(_type=='1'){
    		_url = 'appWeb.do?method=appDataDetail';
    	}else{
    		_url = 'appWeb.do?method=addPage';
    	}
    	$('#addForm').attr('action',_url);
    	$('#addForm').submit();
    }
</script>
</html>
