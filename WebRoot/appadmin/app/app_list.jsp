<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form id="pagerForm" method="post" action="appView.do?method=listApp">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
    <input type="hidden" name="viewId" value="${viewId}" />
    <input type="hidden" name="deleted" value="0" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="appView.do?method=listApp&viewId=${viewId}&deleted=1" target="navTab" title="应用回收箱" rel="t_listApp"><span>回收箱</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="20%">应用名称</th>
				<th width="50" >应用状态</th>
				<th width="50">是否公开</th>
				<th width="50">创建时间</th>
				<th width="50">流程设置</th>
				<th width="50">wcm同步</th>
				<th width="50">修改</th>
				<th width="50">删除</th>
				<th width="50">预览</th>
			</tr>
		</thead>
		<tbody>
		   <c:set var="index" value="0"></c:set>
		   <c:forEach items="${requestScope.appList}" var="app" varStatus="num">
			<tr target="sid" rel="${app.appId}">
				<td>${num.index+1}</td>
				<td>${app.appName}</td>
				<td>${app.appStatus==0 ? "打开":"关闭"}</td>
				<td>${app.publicDesc}</td>
				<td><fmt:formatDate value="${app.crtime}" pattern="yyyy-MM-dd"/></td>
				<td>
					<c:if test="${app.isNeedTheme != '1'}">
						<a href="appView.do?method=setFlowPage&appId=${app.appId}&flowId=${app.flowId}" target="dialog" mask="true" title="设置工作流"
				width="510" height="450" class="btnEdit"><span>修改</span></a>
					</c:if>
				</td>
				<td><a href="appView.do?method=synWcmField&viewId=${app.viewId }&mainTableId=${app.mainTableId}&appId=${app.appId}" target="dialog" mask="true" title="设置同步字段"
				width="510" height="550" class="btnEdit"><span>修改</span></a></td>
				<td><a href="appView.do?method=appEdit&appId=${app.appId }" target="dialog" mask="true" title="修改应用"
				width="580" height="560" class="btnEdit"><span>修改</span></a></td>
				<td><a title="你确定要删除记录吗?" target="ajaxTodo"  href="appView.do?method=delApp&appId=${app.appId}" class="btnDel">删除</a></td>
				<td><a  target="_blank"  href="<%=basePath %>query/${app.groupId }/${app.appId}.html" class="btnLook">预览</a></td>
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
