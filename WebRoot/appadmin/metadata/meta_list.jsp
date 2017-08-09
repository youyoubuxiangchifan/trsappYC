<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form id="pagerForm" method="post" action="meta.do?method=list">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="meta.do?method=meta_add" target="dialog" height="400"><span>新增元数据</span></a></li>
			<%--<li><a title="删除记录,将连同数据库表一起删掉?" target="selectedTodo" rel="ids" href="meta.do?method=del" class="delete"><span>删除</span></a></li>
		--%></ul>
	</div>
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<%--<th width="55"><input type="checkbox" group="ids" class="checkboxCtrl">全选</th>
				--%>
				<th width="55">序号</th>
				<th width="20%">中文名称</th>
				<th width="150">英文名称</th>
				<th width="150">元数据类型</th>
				<th width="150">创建时间</th>
				<th width="150" >创建人</th>
				<th width="50">修改</th>
				<th width="50">删除</th>
			</tr>
		</thead>
		<tbody>
		   <c:forEach items="${requestScope.metaList}" var="meta" varStatus="num">
			<tr target="sid_user" rel="${meta.tableInfoId}">
				<%--<td><input name="ids" value="${meta.tableInfoId}" type="checkbox"></td>
				--%>
				<td>${num.index + 1 }</td>
				<td><a href="${basePath}meta.do?method=findFields&mainTableId=${meta.tableInfoId}&tableName=${meta.tableName}&itemTableId=${meta.itemTableId == null ? 0 : meta.itemTableId}&itemTableName=${meta.itemTableName == null ? '' : meta.itemTableName}" target="navTab" rel="t_listField" title="元数据字段列表">${meta.anotherName}</a></td>
				<td>${meta.tableName}</td>
				<td>${meta.tableType == 0 ? "通用类型" : "主题类型"}</td>
				<td><fmt:formatDate value="${meta.crtime}" pattern="yyyy-MM-dd"/></td>
				<td>${meta.cruser}</td>
				<td><a href="meta.do?method=meta_edite&id=${meta.tableInfoId }" target="dialog" 
				width="510" height="300" class="btnEdit"><span>修改</span></a></td>
				<td><a title="删除记录,将连同数据库表一起删掉?" target="ajaxTodo"  href="meta.do?method=del&id=${meta.tableInfoId}" class="btnDel">删除</a></td>
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
