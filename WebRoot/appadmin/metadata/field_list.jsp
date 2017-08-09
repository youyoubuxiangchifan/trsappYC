<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form name="metaFieldPagerForm" id="pagerForm" class="pageForm" method="post" onsubmit="return navTabSearch(this);" action="meta.do?method=findFields">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<input type="hidden" name="mainTableId" value="${mainTableId}" />
	<input type="hidden" name="itemTableId" value="${itemTableId}" />
	<input type="hidden" name="tableName" value="${tableName}" />
	<input type="hidden" name="itemTableName" value="${itemTableName}" />
	<div class="searchBar">
		<select name="tableId" onchange="navTabSearch(document.metaFieldPagerForm);">
			<option value="${mainTableId}" ${tableId == mainTableId ? "selected" : "" }>主表字段列表</option>
			<c:if test="${itemTableId != 0}">
				<option value="${itemTableId}" ${tableId == itemTableId ? "selected" : "" }>从表字段列表</option>
			</c:if>
		</select>
		<INPUT type="text" name="sFiledValue" value="${sFiledValue}" class="required" maxlength="20" />&nbsp;&nbsp;
		<select name="selectFiled">   						
			<option value="fieldDesc" <c:if test="${selectFiled == 'fieldDesc'}">selected="selected"</c:if>>显示名称</option>
			<option value="fieldName" <c:if test="${selectFiled == 'fieldName'}">selected="selected"</c:if>>字段名称</option>
  		</select>
		&nbsp;&nbsp;
		<button type="submit">检索</button>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="meta.do?method=field_add&tableId=${mainTableId}&tableName=${tableName}&itemTableId=${itemTableId}&itemTableName=${itemTableName == null ? '' : itemTableName}" target="dialog" width="510" height="400"><span>添加</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="115">
		<thead>
			<tr>
				<th width="50">全选</th>
				<th>字段显示名称</th>
				<th  width="120">字段名</th>
				<th width="100">字段类型</th>
				<th width="150">创建时间</th>
				<th width="80">修改</th>
				<th width="80">删除</th>
				
			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${requestScope.fieldList}" var="field" varStatus="num">
			<tr target="sid_field" rel="${field.fieldId }">
				<td>${num.index + 1 }</td>
				<td>${field.fieldDesc }</td>
				<td>${field.fieldName }</td>
				<td>
				${field.typeDesc}
				</td>
				<td><fmt:formatDate value="${field.crtime }" pattern="yyyy-MM-dd"/></td>
				<td>
				<c:if test="${field.isReserved == 0}">
					<a title="编辑字段" target="dialog" href="meta.do?method=field_edite&id=${field.fieldId}&tbType=${tbType}" class="btnEdit" width="510" height="400" mask="true">编辑</a>&nbsp;
				</c:if>
				</td>
				<td>
				<c:if test="${field.isReserved == 0}">
					<a title="删除记录,将连同数据库表字段一起删掉?" target="ajaxTodo"  href="meta.do?method=delField&id=${field.fieldId}" class="btnDel">删除</a>
				</c:if>
				</td>
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
