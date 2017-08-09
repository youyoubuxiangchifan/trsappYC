<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form id="pagerForm" method="post" action="flow.do?method=list">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="flow.do?method=flow_edite" target="dialog" mask="true" width="510" height="300"><span>新增工作流</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th width="50">序号</th>
				<th width="20%">流程名称</th>
				<th width="150">流程描述</th>
				<th width="150">创建时间</th>
				<th width="150" >创建人</th>
				<th width="50">修改</th>
				<th width="50">删除</th>
			</tr>
		</thead>
		<tbody>
		   <c:set var="index" value="0"></c:set>
		   <c:forEach items="${requestScope.flowList}" var="flow" varStatus="num">
			<tr target="sid" rel="${flow.flowId}">
				<td>${num.index+1}</td>
				<td>
					<a href="${basePath}flow.do?method=listNode&id=${flow.flowId}" 
					target="dialog" width="1000" height="550" 
					mask="true" rel="work_flow_editor" title="工作流设置" drawable="false"
					close="closeFlowEditor" maxable="false" minable="false" resizable="false">${flow.flowName}</a>
				</td>
				<td>${flow.flowDesc}</td>
				<td><fmt:formatDate value="${flow.crtime}" pattern="yyyy-MM-dd"/></td>
				<td>${flow.cruser}</td>
				<td><a href="flow.do?method=flow_edite&id=${flow.flowId }" target="dialog" mask="true" title="修改工作流信息"
				width="510" height="300" class="btnEdit"><span>修改</span></a></td>
				<td><a title="你确定要删除记录吗?" target="ajaxTodo"  href="flow.do?method=del&id=${flow.flowId}" class="btnDel">删除</a></td>
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
<script type="text/javascript">
	function closeFlowEditor(){
		if(appFlow.$altFlag){
			if(confirm("您已修改了工作流信息，是否保存已修改的内容?")){
				saveWorkFlow();
			}
		}
		return true;
	}
</script>
