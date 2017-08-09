<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="role.do?method=listRole" method="post">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<div class="searchBar">
					<INPUT type="text" name="sFiledValue" value="${sFiledValue}"/>&nbsp;&nbsp;
			<select name="selectFiled">   						
   						<option value="rolename" <c:if test="${selectFiled == 'rolename'}">selected="selected"</c:if>>角色名称</option>
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
			<li><a class="add" href="role.do?method=editePage" target="dialog" mask="true" width="510" height="300"><span>添加</span></a></li>
			<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="roleIDs" postType="string" href="${basePath}role.do?method=delRole" class="delete"><span>删除</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="110">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" class="checkboxCtrl" group="roleIDs" value="0"/>全选</th>
				<th width="120">角色名称</th>
				<th>简单描述</th>
				<th width="100">创建时间</th>
				<th width="150">创建用户</th>
				<th width="80" align="center">应用设置</th>
				<th width="80">权限设置</th>
				<th width="80">用户设置</th>
				<th width="80">修改</th>
			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${requestScope.roleList}" var="role" varStatus="num">
			<tr target="sid_role" rel="${role.roleId }">
				<td><input type="checkbox" name="roleIDs" value="${role.roleId }"/></td>
				<td>${role.rolename }</td>
				<td>${role.roledesc }</td>
				<td>${role.crData }</td>
				<td>${role.cruser }</td>
				<td>
					<a href="role.do?method=findApps&roleID=${role.roleId }" target="dialog" mask="true" width="500" height="450" title="添加应用" rel="doper"><span>设置</span></a>
				</td>
				<td>
					<a href="role.do?method=findOpers&roleID=${role.roleId }" target="dialog" mask="true" width="500" height="300" title="添加操作" rel="doper"><span>设置</span></a>
				</td>
				<td>
				<a href="role.do?method=findUser&roleID=${role.roleId }" target="navTab" rel="t_roleUser" title="添加用户"><span>设置</span></a>
				</td>
				<td>
				<a href="role.do?method=editePage&roleID=${role.roleId }" target="dialog" mask="true" class="btnEdit"
				width="510" height="300" ><span>修改</span></a>
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
  