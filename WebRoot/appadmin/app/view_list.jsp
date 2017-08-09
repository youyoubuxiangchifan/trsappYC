<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form id="pagerForm" method="post" action="appView.do?method=list">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="appView.do?method=viewEdite" target="dialog" mask="true" width="520" height="540"><span>创建视图</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th width="50">序号</th>
				<th width="20%">视图名称</th>
				<th width="150" >应用状态</th>
				<th width="100">是否公开</th>
				<th width="150">创建时间</th>
				<th width="100">管理应用</th>
				<th width="100">字段管理</th>
				<th width="50">修改</th>
				<th width="50">删除</th>
			</tr>
		</thead>
		<tbody>
		   <c:set var="index" value="0"></c:set>
		   <c:forEach items="${requestScope.viewList}" var="view" varStatus="num">
			<tr target="sid" rel="${view.viewId}">
				<td>${num.index+1}</td>
				<td>${view.viewName}</td>
				<td>${view.appStatus==0 ? "打开":"关闭"}</td>
				<td>${view.publicDesc}</td>
				<td><fmt:formatDate value="${view.crtime}" pattern="yyyy-MM-dd"/></td>
				<td><a href="appView.do?method=listApp&viewId=${view.viewId}" target="navTab" rel="t_listApp" title="${view.viewName}应用列表">管理</a></td>
				<td><a href="appView.do?method=listField&viewId=${view.viewId}"
				 target="navTab"  title="${view.viewName}" rel="t_vflist">管理</a></td>
				<td><a href="appView.do?method=viewEdite&id=${view.viewId }" target="dialog" mask="true" title="修改视图" 
				width="520" height="580" class="btnEdit"><span>修改</span></a></td>
				<td><a title="你确定要删除记录吗?" target="ajaxTodo"  href="appView.do?method=del&viewId=${view.viewId}" class="btnDel">删除</a></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="3" <c:if test="${page.pageSize == '3'}">selected="selected"</c:if>>3</option>
				<option value="15" <c:if test="${page.pageSize == '15'}">selected="selected"</c:if>>15</option>
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>

	</div>

</div>
