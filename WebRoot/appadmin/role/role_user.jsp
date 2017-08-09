<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="pagerForm" method="post" action="role.do?method=findUser">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<input type="hidden" name="roleID" value="${roleID}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="role.do?method=userList&roleID=${roleID}" target="dialog" max="true"><span>添加用户</span></a></li>
			<li><a title="确实要删除这些用户吗?" target="selectedTodo" targetType="navTabAjaxDone" postType="string" rel="ids" href="role.do?method=delUser" class="delete"><span>删除用户</span></a></li>
			<%--<li><a title="确实要设置这些用户为当前角色下的管理员吗?" target="selectedTodo" rel="ids" postType="string" href="role.do?method=toAppAdminUser&flag=1" class="icon"><span>设置为应用管理员</span></a></li>
			<li><a title="确实要取消这些用户为当前角色下的管理员吗?" target="selectedTodo" rel="ids" postType="string" href="role.do?method=toAppAdminUser&flag=0" class="icon"><span>取消应用管理员</span></a></li>
		--%></ul>
	</div>
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th width="55"><input type="checkbox" name="selectAll" class="checkboxCtrl" value="0" group="ids" >全选</th>
				<th width="100" orderField="accountNo">用户名</th>
				<th orderField="accountName">真实姓名</th>
				<th width="150" orderField="accountCert">当前状态</th>
				<th width="150">设置为应用管理员</th>
			</tr>
		</thead>
		<tbody>
		   <c:forEach items="${requestScope.users}" var="user">
			<tr target="sid_user" rel="${user.roleuserId}">
				<td><input name="ids" value="${user.roleuserId}" type="checkbox"></td>
				<td>${user.username}</td>
				<td>${user.truename}</td>
				<td>
				   <c:choose>
       			      <c:when test="${user.status==0}">已开通</c:when>
                      <c:when test="${user.status==1}">已停用</c:when>
					  <c:when test="${user.status==2}">已删除</c:when>
                      <c:otherwise>其他</c:otherwise>
				   </c:choose>
				</td>
				<td>
					<a href="role.do?method=setRoleAppAdmin&roleID=${roleID }&userID=${user.userId}" target="dialog" mask="true" width="500" height="450" title="设置为应用管理员"><span>设置</span></a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value}, 'jbsxBox')">
				<option value="3" <c:if test="${page.pageSize == '3'}">selected="selected"</c:if>>3</option>
				<option value="15" <c:if test="${page.pageSize == '15'}">selected="selected"</c:if>>15</option>
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="navTab"  totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>
       
	</div>
