<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="pageContent">
	<table class="table" width="100%" layoutH="26">
		<thead>
			<tr>
				<th width="120">节点名称</th>
				<th width="120">处理人员</th>
				<th width="120">处理时间</th>
				<th width="120">流转意见</th>
				<th width="120">当前节点处理意见</th>
				<th width="120">当前节点处理方式</th>
<!--				<th >是否签收</th>-->
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.flowsMap.flowDoclist}" var="flowDoclist" varStatus="i">
			<c:forEach items="${requestScope.flowsMap.appFlowNodes}" var="appFlowNodes" >
				<c:if test="${appFlowNodes.nodeId eq flowDoclist.nodeId}">
					<tr target="sid_user" rel="${i }">
						<td>${appFlowNodes.nodeName}</td>
						<td>${flowDoclist.postUser}</td>
						<td>${flowDoclist.postTime}</td>
						<td>${flowDoclist.postDesc}</td>
						<td>${flowDoclist.togetherusers}</td>
						<td>${flowDoclist.operateType}</td>
<!--						<td>${(flowDoclist.accepted eq 0) ?"未签收":"已签收"}</td>-->
					</tr>
				</c:if>
			</c:forEach>
		</c:forEach>
		</tbody>
	</table>
</div>
