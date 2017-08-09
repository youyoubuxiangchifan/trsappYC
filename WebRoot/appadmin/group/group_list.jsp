<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style type="text/css">
	ul.rightTools {float:right; display:block;}
	ul.rightTools li{float:left; display:block; margin-left:5px}
</style>
  <form id="pagerForm" method="post" action="${basePath}group.do?method=listGroup">
    <input type="hidden" name="groupIds" value="${groupIds }" />	
	<input type="hidden" name="pageNum" value="${page.startPage}" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>
	
<div class="pageContent">

	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="<%=basePath %>group.do?method=edditPage&groupIds=${groupIds }&pageFlag=0"  target="dialog" mask=true title="添加子组织"><span>添加子组织</span></a></li>
			<li><a class="delete" title="您要删除选中的组织吗？" target="selectedTodo" rel="groupIds" targetType="navTabAjaxDone" postType="string" href="<%=basePath %>group.do?method=delGroup&parentId=${groupIds}" ><span>删除组织</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="<%=basePath %>appadmin/group/group_upload.jsp?groupId=${groupIds }"  target="dialog" mask="true" ><span>组织导入</span></a></li>
			<c:if test="${groupIds !='0'}"><li><a class="icon" href="<%=basePath %>group.do?method=listGroupUser&groupIds=${groupIds }" title="组织用户管理" target="navTab" ><span>组织用户管理</span></a></li>	</c:if>		
		</ul>
	</div>	
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" name="selectAll" class="checkboxCtrl" group="groupIds" value="0" class="checkboxCtrl"/>全选</th>
				<th width="120">组织名</th>
				<th>是否独立组织</th>
				<th width="100">创建用户</th>
				<th width="150">创建时间</th>
				<th width="80">调整顺序</th>
				<th width="80">修改</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.groupList}" var="appGroup">
			<tr target="sid_user" rel="1">
				<td><input type="checkbox" name="groupIds" value="${appGroup.groupId }"/></td>
				<td><a href="<%=basePath %>group.do?method=edditPage&groupIds=${appGroup.groupId}&pageFlag=1" target="dialog">${appGroup.gname }</a></td>
				<td><c:if test="${appGroup.isIndependent =='0'}">否</c:if><c:if test="${appGroup.isIndependent =='1'}">是</c:if></td>
				<td>${appGroup.cruser }</td>
				<td><fmt:formatDate value="${appGroup.crtime }" pattern="yyyy-MM-dd"/></td>
				<td><a href="<%=basePath %>group.do?method=orderGroup&groupIds=${appGroup.groupId}&orderFlag=1" target="ajaxTodo" targetType="navTabAjaxDone" postType="string">上移</a>&nbsp;&nbsp;<a href="<%=basePath %>group.do?method=orderGroup&groupIds=${appGroup.groupId}&orderFlag=2" target="ajaxTodo" targetType="navTabAjaxDone" postType="string">下移</a></td>
				<td><a href="<%=basePath %>group.do?method=edditPage&groupIds=${appGroup.groupId}&pageFlag=2" target="dialog" title="修改">修改</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20" <c:if test="${page.pageSize == '2'}">selected="selected"</c:if>>2</option>
				<option value="30" <c:if test="${page.pageSize == '3'}">selected="selected"</c:if>>3</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>
		</div>	
	</div>	
	



<script>
  /*** 
		function refreshNode() {
			var zTree = $.fn.zTree.getZTreeObj("ztreedemo"),
			nodes = zTree.getSelectedNodes();
			if (nodes.length == 0) {
				alert("请先选择一个父节点");
			}
			zTree.reAsyncChildNodes(nodes[0], "refresh");
			zTree.selectNode(nodes[0]);

		}
***/
</script>
				
