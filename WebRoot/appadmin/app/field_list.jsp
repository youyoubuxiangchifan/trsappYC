<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form name="viewFieldPagerForm" id="pagerForm" class="pageForm" method="post" onsubmit="return navTabSearch(this);" action="appView.do?method=listField">
		<input type="hidden" name="pageNum" value="1" />
		<input type="hidden" name="numPerPage" value="${page.pageSize}" />
		<input type="hidden" name="viewId" value="${viewId}" />
		<input type="hidden" name="tbType" value="${tbType}" />
		<%--<input type="hidden" name="isTheme" value="${isTheme}" />
		<input type="hidden" name="mainTableId" value="${mainTableId}" />
		<input type="hidden" name="itemTableId" value="${itemTableId}" />
		--%><div class="searchBar">
			<select name="tableId" onchange="navTabSearch(document.viewFieldPagerForm);">
				<option value="${mainTableId}" ${tableId == mainTableId ? "selected" : "" }>主表字段列表</option>
				<c:if test="${isTheme != 0}">
					<option value="${itemTableId}" ${(tableId == itemTableId && tableId != 0) ? "selected" : "" }>从表字段列表</option>
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
			<li><a class="edit" href="appView.do?method=buildViewField&viewId=${viewId}&tbType=${isTheme}&tableId=0" mask="true" target="dialog" width="1000" height="560"><span>字段维护</span></a></li>
			<li>
				<a class="edit" href="appView.do?method=buildgrp_field&viewId=${viewId}&tbType=${isTheme}&tableId=0" target="dialog" mask="true" width="1000" height="560"><span>分组维护</span></a>
			</li>
		</ul>
	</div>
	<%--<div class="panelBar">
		<ul>
			<c:if test="${isTheme == 1}">
			<li>
				<div class="buttonActive">
					<div class="buttonContent">
						<button type="button" onclick="refFieldList(0)">通用类型</button>
					</div>
				</div>
			</li>
			<li>
				<div class="buttonActive">
					<div class="buttonContent">
						<button type="button" onclick="refFieldList(1)">意见类型</button>
					</div>
				</div>
			</li>
			</c:if>
			<li>
				<div class="buttonActive">
					<div class="buttonContent">
						<button type="button" onclick="buildViewField();">字段维护</button>
					</div>
				</div>
			</li>
			<li><a class="add" href="appView.do?method=list" target="navTab" title="应用管理"><span>返回</span></a></li>
		</ul>
		<ul class="toolBar">
			<li><a class="add" href="appView.do?method=buildViewField&viewId=${viewId}&tbType=0&tableId=0" target="dialog" mask="true" width="800" height="500"><span>维护字段</span></a></li>
			<li><a class="add" href="appView.do?method=list" target="navTab" title="应用管理"><span>返回</span></a></li>
		</ul>
	</div>
	--%>
	<table class="table" width="100%" layoutH="115">
		<thead>
			<tr>
				<th width="50">序号</th>
				<th width="150" >字段名</th>
				<th>字段显示名称</th>
				<th width="100">字段类型</th>
				<th width="150">创建时间</th>
				<th width="150">修改</th>
				<th width="150">删除</th>
			</tr>
		</thead>
		<tbody>
		   <c:set var="index" value="0"></c:set>
		   <c:forEach items="${requestScope.fieldList}" var="field" varStatus="num">
			<tr target="sid" rel="${field.appFieldId}">
				<td>${num.index+1}</td>
				<td>${field.fieldName}</td>
				<td>${field.fieldDesc}</td>
				<td>${field.typeDesc}</td>
				<td><fmt:formatDate value="${field.crtime}" pattern="yyyy-MM-dd"/></td>
				<td>
				<%--<c:if test="${field.isReserved == 0}">
					--%>
					<a href="appView.do?method=fieldEdite&appFieldId=${field.appFieldId }&tableType=${tableType}" target="dialog" mask="true" title="修改视图字段"
				width="510" height="400" class="btnEdit"><span>修改</span></a>
				<%--</c:if>
				--%></td>
				<td>
				<c:if test="${field.isReserved == 0}">
					<a title="你确定要删除记录吗?" target="ajaxTodo"  href="appView.do?method=delField&fieldId=${field.appFieldId}" class="btnDel">删除</a>
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
<script>
	function buildViewField(){
		var url = "appView.do?method=buildViewField&viewId=${viewId}&tbType=${isTheme}&tableId=0";
		var options = {
				mask:true,
				width:'920',
				height:'500'
			};
		$.pdialog.open(url, "字段维护", "字段维护",options);
	}
	function refFieldList(tbType){
		$('#pagerForm input[name=tbType]').val(tbType);
		//alert(tbType);
		navTabPageBreak();
		//navTabSearch($('#pagerForm'),'t_vflist');
	}
</script>