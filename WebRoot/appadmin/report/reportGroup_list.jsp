<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="report.do?method=reportgrp" method="post">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<div class="searchBar">
		当前应用
		<select name="selectFiled">   						
			<c:forEach items="${requestScope.tableNames}" var="tableNames" varStatus="num">
				<c:forEach items="${tableNames}" var="valueNames" >
					<c:if test="${'APP_NAME' eq valueNames.key}">
					<option value="${APP_ID }">	${valueNames.value }</option>
					</c:if>
				</c:forEach>
			</c:forEach>
		</select>
		开始时间：<input type="text" class="date" readonly="true" value="2014-01-01"/>
		结束时间：<input type="text" class="date" readonly="true" value="2014-09-04"/>
		&nbsp;&nbsp;
		<button type="submit">统计</button>
	</div>
	</form>
</div>
<div class="pageContent">
<%--	<div class="panelBar">--%>
<%--		<ul class="toolBar">--%>
<%--		</ul>--%>
<%--	</div>--%>
	<table class="table" width="100%" layoutH="110">
		<thead>
			<tr>
<%--				<th width="120" align="center">应用名称</th>--%>
				<th width="80" align="center">机构名称</th>
<%--				<th width="100" align="center">机构编号</th>--%>
				<th width="80" align="center">信件总数</th>
				<th width="80" align="center">平均办理天数</th>
				<th width="80" align="center">办理率(%)</th>
				<th width="80" align="center">超期件</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.results}" var="results" varStatus="num">
				<c:forEach items="${results}" var="listMap" >
				<tr target="sid_dataStatus" >
					<c:forEach items="${listMap}" var="valueMaps" >
						<c:if test="${'GNAME' eq valueMaps.key}">
							<td>
								${valueMaps.value }
							</td>
						</c:if>
					</c:forEach>
					<c:forEach items="${listMap}" var="valueMaps" >
						<c:if test="${'RECORDCOUNT' eq valueMaps.key}">
							<td>
								${valueMaps.value }
							</td>
						</c:if>
					</c:forEach>
					<c:forEach items="${listMap}" var="valueMaps" >
						<c:if test="${'RECORDCOUNT' eq valueMaps.key}">
							<td>
								<fmt:parseNumber var="intVar4" integerOnly="true" value="${valueMaps.value/7}" />
								${intVar4 }
							</td>
						</c:if>
					</c:forEach>
					<c:forEach items="${listMap}" var="valueMaps" >
						<c:if test="${'RECORDCOUNT' eq valueMaps.key}">
							<td>
								<fmt:parseNumber var="intVar4" integerOnly="true" value="${valueMaps.value/(valueMaps.value/100)}" />
								${intVar4 }%
							</td>
						</c:if>
					</c:forEach>
					<c:forEach items="${listMap}" var="valueMaps" >
						<c:if test="${'RECORDCOUNT' eq valueMaps.key}">
							<td>
								<fmt:parseNumber var="intVar4" integerOnly="true" value="${valueMaps.value/30}" />
								${intVar4 }
							</td>
						</c:if>
					</c:forEach>
				</tr>
				</c:forEach>
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
  