<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <form id="pagerForm" method="post" action="${basePath}appMgr.do?method=getAppendixs">
  	<input type="hidden" name="appId" value="${appId}">
	<input type="hidden" name="metadataId" value="${metadataId}">
</form>
<div class="pageContent">
	<table class="table" width="100%" layoutH="56">
		<thead>
			<tr>
				<th width="60">序号</th>
				<th width="60">原文件名</th>
				<th width="60">系统文件名</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.appendixs}" var="appendixs" varStatus="i">
			<tr target="sid_user" rel="1">
				<td>${i.index+1 }</td>
				<td>${appendixs.fileName}</td>
				<td>${appendixs.srcfile}</td>
				<td><a href="appWeb.do?method=downLoadFile&appendixId=${appendixs.appendixId}&flag=1" target="_blank">下载</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
		<div class="panelBar">
		</div>	
	</div>	
