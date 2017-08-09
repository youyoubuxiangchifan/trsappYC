<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <form id="pagerForm" method="post" action="${basePath}user.do?method=listUser">
	<input type="hidden" name="userStatus" value="${userStatus}">
	<input type="hidden" name="weakPasswd" value="${weakPasswd}" />
	<input type="hidden" name="selectFiled" value="${selectFiled}" />
	<input type="hidden" name="sFiledValue" value="${sFiledValue}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
		
	<input type="hidden" name="pageNum" value="${page.startPage}" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>
<div class="pageHeader">
  			<form  class="pageForm required-validate" method="post" onsubmit="return navTabSearch(this);" action="${basePath}user.do?method=listUser">
   			<div class="searchBar">
   			<input type="hidden" name="userStatus" value="${userStatus}">
			<input type="hidden" name="weakPasswd" value="${weakPasswd}" />
			<INPUT type="text" name="sFiledValue" value="${sFiledValue}" class="required" minlength="3" maxlength="20" />&nbsp;&nbsp;
			<select name="selectFiled">   						
   						<option value="username" <c:if test="${selectFiled == 'username'}">selected="selected"</c:if>>用户名</option>
   						<option value="truename" <c:if test="${selectFiled == 'truename'}">selected="selected"</c:if>>真实姓名</option>
   						<option value="moblie" <c:if test="${selectFiled == 'moblie'}">selected="selected"</c:if>>电话</option>
   			</select>
			&nbsp;&nbsp;
			<button type="submit" style="padding-top:3px;padding-left:3px;padding-right:3px;">检索</button>
			</div>
			</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<c:if test="${userStatus == '0' or userStatus == '-1'}"><li><a class="add" href="<%=basePath %>user.do?method=edditPage&pageFlag=0"  target="dialog" mask="true" width="550" height="470" title="添加用户"><span>添加用户</span></a></li></c:if>
			<c:if test="${userStatus != '2'}"><li><a class="delete" title="您要删除选中的用户吗？" target="selectedTodo" rel="userIDs" postType="string" href="<%=basePath %>user.do?method=delUser" ><span>删除</span></a></li></c:if>
			<li><a class="delete" title="您要彻底删除选中的用户吗？" target="selectedTodo" rel="userIDs" postType="string" href="<%=basePath %>user.do?method=phyDelUser" ><span>彻底删除</span></a></li>
			<c:if test="${userStatus == '0'}"><li><a class="edit" title="您要停用选中的用户吗？" target="selectedTodo" rel="userIDs" postType="string" href="<%=basePath %>user.do?method=stopUser" ><span>停用用户</span></a></li></c:if>
			<c:if test="${userStatus == '1' or userStatus == '2'}"><li><a class="edit" title="您要开通选中的用户吗？" target="selectedTodo" rel="userIDs" postType="string" href="<%=basePath %>user.do?method=openUser" ><span>开通用户</span></a></li></c:if>
			<li><a class="edit" href="<%=basePath %>user.do?method=listUser&weakPasswd=0&userStatus=${userStatus}" target="navTab" rel="weakPasswdUser"><span>弱密码</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="<%=basePath %>appadmin/user/user_upload.jsp"  target="dialog" mask="true" title="用户导入"><span>用户导入</span></a></li>			
		</ul>
	</div>
	
	<table class="table" width="100%" layoutH="113">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" name="selectAll" class="checkboxCtrl" group="userIDs" value="0" class="checkboxCtrl"/>全选</th>
				<th width="120"  orderField="username" class="${orderDirection}">用户名</th>
				<th>当前状态</th>
				<th width="100">真实姓名</th>
				<th width="150">联系电话</th>
				<th width="80" align="center">添加时间</th>
				<th width="100">是否为管理员</th>
				<th width="80">重置密码</th>
				<th width="80">修改</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.userList}" var="appUser">
			<tr target="sid_user" rel="1">
				<td><input type="checkbox" name="userIDs" value="${appUser.userId }"/></td>
				<td><a href="<%=basePath %>user.do?method=edditPage&userIDs=${appUser.userId}&pageFlag=1" title="${appUser.username }&nbsp;&nbsp;详细信息" target="dialog" mask="true">${appUser.username }</a></td>
				<td><c:if test="${appUser.status == '0'}">已开通</c:if><c:if test="${appUser.status == '1'}">已停用</c:if><c:if test="${appUser.status == '2'}">已删除</c:if></td>
				<td>${appUser.truename }</td>
				<td>${appUser.moblie }</td>
				<td><fmt:formatDate value="${appUser.crtime }" pattern="yyyy-MM-dd"/></td>
				<td><c:if test="${appUser.usertype == '1'}">是</c:if><c:if test="${appUser.usertype == '0'}">否</c:if></td>
				<td><a href="<%=basePath %>user.do?method=edditPage&userIDs=${appUser.userId}&pageFlag=2" title="重置密码" target="dialog" mask="true">重置密码</a></td>
				<td><a href="<%=basePath %>user.do?method=edditPage&userIDs=${appUser.userId}&pageFlag=3" target="dialog" mask="true" width="500" height="320">修改</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="30" <c:if test="${page.pageSize == '30'}">selected="selected"</c:if>>30</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>
		</div>	
	</div>	
				
