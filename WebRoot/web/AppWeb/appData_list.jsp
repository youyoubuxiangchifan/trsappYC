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
<title>${viewName}列表页</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/reset.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/layout.css"/>
<script src="<%=basePath%>web/images/jquery-1.7.2.min.js" type="text/javascript"></script>
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
		<c:if test="${isHasQueryNo==1}">
   		<div class="i_Gr_1"><span>信件查询</span></div>
	    <form action="" id="qForm" target="_blank" method="post">
			<ul class="i_Gr_2">
		      <li class="i_Gr_3">查询编码：</li>
		      <li><input type="text" class="i_Gr_1_ipt" id="searchCode" name="searchCode"/></li>
		      <li class="i_Gr_3">查询密码：</li>
		      <li><input type="password" class="i_Gr_1_ipt" id="searchPassWord" name="searchPassWord"/></li>
		      <li><input type="button" class="i_Gr_1_btn1" value="查 询" onclick="queryData();"/></li>
		      <%--<c:if test="${isNeedTheme == 0}">
		      <li><input type="button" class="i_Gr_1_btn1" value="新增" onclick="addPage();"/></li>
		      </c:if>
		    --%></ul>
	     	<input type="hidden" id="isNeedTheme" name="isNeedTheme" value="${isNeedTheme}"/>
	     	<input type="hidden" name="appId" value="${appId}"/>
	     	<input type="hidden" name="groupId" value="${groupId}"/>
	    </form>
	    </c:if>
    	<div class="i_Gr_4">
    		<span style="float:left;">信件列表</span>
    		<c:if test="${isNeedTheme == 0}">
		      <span style="float:right;"><input type="button" class="i_Gr_1_btn1" value="新增" onclick="addPage();"/></span>
		   	</c:if>
		</div>
    	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="i_Gr_5">
		  <tr>
		  	<c:if test="${isHasQueryNo==1}">
		    <td width="17%" class="f_w f_c cor_b">编号</td>
		    </c:if>
		    <td width="44%" class="f_w f_c cor_b">主题</td>
		    <td width="15%" class="f_w f_c cor_b">写信日期</td>
		    <td width="8%" class="f_w f_c cor_b">状态</td>
		    <td width="8%" class="f_w f_c cor_b">点击</td>
		  </tr>
		  <c:forEach items="${page.ldata}" var="data" >
			  <tr>
			   <c:if test="${isHasQueryNo==1}">
			    <td class="f_c">${data.QUERYNUMBER}</td>
			   </c:if>
			    <c:forEach items="${data}" var="valueMap" >   <!-- 遍历单条数据字段信息-->
		           
		        <c:if test="${valueMap.key eq titleFiled}"> <!-- 判断标题字段-->
		        	 <td <c:if test="${isHasQueryNo==0}">class="f_c"</c:if>>
		        	 <a href="<%=basePath%>qdetail/${groupId}/${appId}/${data.METADATAID}.html" target="_blank">${valueMap.value}</a></td>
		        </c:if>
		        
		        </c:forEach>
			    <td class="f_c">${data.CRTIME}</td>
			    <td class="f_c">
				    <c:if test="${data.STATUS eq 0}">待办理</c:if>
				    <c:if test="${data.STATUS eq 1}">办理中</c:if>
				    <c:if test="${data.STATUS eq 2}">已办理</c:if>
			    </td>
			    <td class="f_c">${data.HITCOUNTS}</td>
			  </tr>
		  </c:forEach>
		</table>
		<!-- 分页开始 -->
		<form id="pagerForm" name="pagerForm" action="appWeb.do?method=queryAppData" method="post">
			<input type="hidden" name="appId" value="${appId}"/>
    		<input type="hidden" name="groupId" value="${groupId}"/>
    		<input type="hidden" id="pageNum" name="pageNum" value="${page.startPage}"/>
		</form>
		<jsp:include page="../common/pagination_blue.jsp"></jsp:include>
		<!-- 分页结束 -->
		</div>
	<div class="clear"></div>
       <form action="" id="addForm" target="_blank" method="post">
	     	<input type="hidden" id="isNeedTheme" name="isNeedTheme" value="${isNeedTheme}"/>
	     	<input type="hidden" name="appId" value="${appId}"/>
	     	<input type="hidden" name="groupId" value="${groupId}"/>
	   		<input type="hidden" id="flag" name="flag" value="${isHasSmtDesc}"/>
	   </form>
</div>
<!-- START footer --> 
<jsp:include page="../include/webfooter.jsp"  /> 
<!-- END footer --> 
</body>
<script type="text/javascript">
    function queryData(){
    	var _url = "";
    	var _searchCode=$('#searchCode').val();
    	var _searchPassWord=$('#searchPassWord').val();
    	var _isNeedTheme = $('#isNeedTheme').val();
    	if(!_searchCode){
    		alert("查询编号不能为空！");
    		return;
    	}else if(!_searchPassWord){
    		alert("查询密码不能为空！");
    		return;
    	}else if(!_isNeedTheme){
    	   alert("传入的数据有误！");
    	   return;
    	}else if(_isNeedTheme=='0'){
    		_url = '/trsapp/search/index.html';
    	}else{
    		_url = '/trsapp/search/index.html';
    	}
    	
    	$('#qForm').attr('action',_url);
    	$('#qForm').submit();
    }
    function addPage(){
    	var _url = '/trsapp/add.html';
    	$('#addForm').attr('action',_url);
    	$('#addForm').submit();
    }
</script>
</html>
