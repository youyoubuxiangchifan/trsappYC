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
    <script type="text/javascript">
	/** 
	* Description: 比较两个时间的大小，用于判断主题类应用是否正在进行   
	* author liujian
	* date 2014-4-24 下午15:44:05
	* @param fDate 第一个时间参数
	* @param sDate 第二个时间参数
	* @return 第一个时间大于等于第二个时间返回"进行中";否则返回"已结束"
	* version 1.0
	*/
	function dateCompare(fDate,sDate){   
		var arr=fDate.split("-");    
		var fDateTime=new Date(arr[0],arr[1],arr[2]);    
		var fDateTimes=fDateTime.getTime();   
		var arrs=sDate.split("-");    
		var sDateTime=new Date(arrs[0],arrs[1],arrs[2]);    
		var sDateTimes=sDateTime.getTime();   
		if(fDateTimes>=sDateTimes){   
			document.write("进行中");   
		}else  
		document.write("已结束");   
	  }
   </script>
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
    <div class="i_Gr_9"><span>${viewName}</span></div>
       <ul class="i_list1">
       <c:forEach items="${page.ldata}" var="data" >
        <li>
        	<span>
	        	<script type="text/javascript">
	        		dateCompare("${data.ENDTIME}","${now}");
	        	</script>
        	</span>
        		    <c:forEach items="${data}" var="valueMap" >   <!-- 遍历单条数据字段信息-->
           
        <c:if test="${valueMap.key eq titleFiled}"> <!-- 判断标题字段-->
            <!-- 
        	 <a href="appWeb.do?method=addPage&groupId=${groupId}&appId=${appId}&themeId=${data.METADATAID}">${valueMap.value}</a>
       		 -->
       		  <a href="/trsapp/zdetail/${groupId}/${appId}/${data.METADATAID}f${isHasSmtDesc}.html" target="_blank">${valueMap.value}</a>
        </c:if>
        
        </c:forEach>
        	
        
        </li>
       
      </c:forEach>
      </ul> 
     <%--<div class="mt10 mb10 i_Page">
					<a href="#">首页</a>
					<a href="#">上一页</a>
					<a href="#">1</a>
					<a href="#" class="on">2</a>
					<a href="#">3</a>
					<a href="#">4</a>
					<a href="#">下一页</a>
					<a href="#">末页</a>
	</div>
  --%>
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
  
  </div>
<!-- START footer --> 
<jsp:include page="../include/webfooter.jsp"/> 
<!-- END footer --> 
  </body>
</html>
