<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <form id="pagerForm" method="post" action="${basePath}group.do?method=listGroupUser">
  	<input type="hidden" name="groupIds" value="${groupIds}" />
	<input type="hidden" name="selectFiled" value="${selectFiled}" />
	<input type="hidden" name="sFiledValue" value="${sFiledValue}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
		
	<input type="hidden" name="pageNum" value="${page.startPage}" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="<%=basePath %>group.do?method=addGroupUserList&groupIds=${groupIds }"  target="dialog" width="615" height="380" mask=true title="添加用户"><span>添加用户</span></a></li>
			<li><a class="add" href="<%=basePath %>group.do?method=edditPage&pageFlag=3&groupIds=${groupIds}"  target="dialog" width="615" height="450" mask=true><span>新建用户</span></a></li>
			<li><a class="delete" title="您要删除选中的用户吗？" target="selectedTodo" rel="grpUserIds" postType="string" href="<%=basePath %>group.do?method=delGroupUser&groupIds=${groupIds}" ><span>从组织中删除用户</span></a></li>
			<li><a class="edit" title="您要指定选中用户为管理员身份吗？" target="selectedTodo" rel="grpUserIds" postType="string" href="<%=basePath %>group.do?method=setGroupAdmin&groupIds=${groupIds}"><span>指定管理员</span></a></li>
			<li><a class="edit" title="您要取消选中用户的管理员身份吗？" target="selectedTodo" rel="grpUserIds"  postType="string" href="<%=basePath %>group.do?method=cncelGroupAdmin&groupIds=${groupIds}"><span>取消管理员</span></a></li>
			<li class="line">line</li>						
		</ul>
	</div>
	
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" name="selectAll" class="checkboxCtrl" group="grpUserIds" value="0" />全选</th>
				<th width="120" >用户名</th>
				<th width="100">真实姓名</th>
				<th width="150">是否组管理员</th>
				<th width="80" align="center">创建用户</th>
				<th width="100">添加时间</th>

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.groupUserList}" var="groupUser">
			<tr target="sid_user" rel="1">
				<td><input type="checkbox" name="grpUserIds" value="${groupUser.grpuserId }"/></td>
				<td>${groupUser.username }</td>
				<td>${groupUser.truename }</td>
				<td><c:if test="${groupUser.isAdmin == '1'}">是</c:if><c:if test="${groupUser.isAdmin == '0'}">否</c:if></td>
				<td>${groupUser.cruser }</td>
				<td>${groupUser.crtime }</td>
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
<script type="text/javascript">
	var _selUsers = new Array();//
</script>				
