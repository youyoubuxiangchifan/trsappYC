<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="sysconfig.do?method=listMsgTemp" method="post">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<div class="searchBar">
					<INPUT type="text" name="sFiledValue" value="${searchValue}" class="required" minlength="3" maxlength="20" />&nbsp;&nbsp;
			<select name="selectFiled">   						
   						<option value="tempTitle" <c:if test="${selectFiled == 'tempTitle'}">selected="selected"</c:if>>模板主题</option>
   						<option value="cruser" <c:if test="${selectFiled == 'cruser'}">selected="selected"</c:if>>创建用户</option>
   			</select>
			&nbsp;&nbsp;
			<button type="submit">检索</button>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="sysconfig.do?method=editMsgTemp" target="dialog" mask="true" width="600" height="620"><span>添加</span></a></li>
			<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="msgTempIDs" postType="string" href="${basePath}sysconfig.do?method=delMsgTemp" class="delete"><span>删除</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="110">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" class="checkboxCtrl" group="msgTempIDs" value="0"/>全选</th>
				<th width="130" align="center">提醒分类</th>
				<th width="100" align="center">模板类型</th>
				<th>模板主题</th>
				<th width="100" align="center">创建时间</th>
				<th width="110" align="center">创建用户</th>
				<th width="90" align="center">修改</th>
			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${requestScope.msgTempList}" var="msgTemp" varStatus="num">
			<tr target="sid_msgTemp" rel="${msgTemp.tempId }">
				<td><input type="checkbox" name="msgTempIDs" value="${msgTemp.tempId }"/></td>
				<td>${msgTemp.remindTypeName }</td>
				<td>${msgTemp.tempTypeName }</td>
				<td>${msgTemp.tempTitle }</td>
				<td>${msgTemp.crData }</td>
				<td>${msgTemp.cruser }</td>
				<td>
				<a href="sysconfig.do?method=editMsgTemp&msgTempID=${msgTemp.tempId }" target="dialog" 
				width="510" height="400" ><span>修改</span></a>
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
  