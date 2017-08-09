<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="sysconfig.do?method=listLog" method="post">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<div class="searchBar">
			<INPUT type="text" name="sFiledValue" value="${searchValue}" class="required" minlength="3" maxlength="20" />&nbsp;&nbsp;
			<select name="selectFiled">   						
				<option value="logdesc" <c:if test="${selectFiled == 'logdesc'}">selected="selected"</c:if>>日志描述</option>
				<option value="cruser" <c:if test="${selectFiled == 'cruser'}">selected="selected"</c:if>>创建用户</option>
   			</select>&nbsp;&nbsp;
   			<select name="selectlogType">   						
				<option value="">—日志类型—</option>
				<option value="1" <c:if test="${selectlogType == '1'}">selected="selected"</c:if>>保存</option>
				<option value="2" <c:if test="${selectlogType == '2'}">selected="selected"</c:if>>修改</option>
				<option value="3" <c:if test="${selectlogType == '3'}">selected="selected"</c:if>>删除</option>
				<option value="4" <c:if test="${selectlogType == '4'}">selected="selected"</c:if>>用户登录</option>
				<option value="5" <c:if test="${selectlogType == '5'}">selected="selected"</c:if>>用户退出</option>
   			</select>
			&nbsp;&nbsp;
			<button type="submit">检索</button>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="logIDs" postType="string" href="${basePath}sysconfig.do?method=delLog" class="delete"><span>删除</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="110">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" class="checkboxCtrl" group="logIDs" value="0"/>全选</th>
				<th width="120" align="center">日志类型</th>
				<th>日志描述</th>
				<th width="100" align="center">操作用户IP</th>
				<th width="100" align="center">操作用户</th>
				<th width="100" align="center">操作时间</th>
			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${requestScope.logList}" var="log" varStatus="num">
			<tr target="sid_log" rel="${log.logid }">
				<td><input type="checkbox" name="logIDs" value="${log.logid }"/></td>
				<td>${log.logTypeName }</td>
				<td>${log.logdesc }</td>
				<td>${log.loguserip }</td>
				<td>${log.loguser }</td>
				<td>${log.logopTime }</td>
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
  