<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	//表单提交
	function goPage(startPage) {
		$("#pageNum").val(startPage);
		$("#pagerForm").submit();
	}
</script>
<div class="mt10 mb10 i_Page">
	<c:choose>
		<c:when test="${page.startPage > 1}">
			<a href="javascript:goPage(1)">首页</a>
			<a href="javascript:goPage(${page.startPage - 1});">上一页</a>
		</c:when>
		<c:otherwise>
			<a href="javascript:void(0);">首页</a>
			<a href="javascript:void(0);">上一页</a>
		</c:otherwise>
	</c:choose>
	<c:forEach var="i" begin="${page.pager[0]}" end="${page.pager[1]}">
	<c:choose>
		<c:when test="${i eq page.startPage}"><a class="on">${i}</a></c:when>
		<c:otherwise>
			<a href="javascript:goPage(${i})">${i}</a>
		</c:otherwise>
	</c:choose>
	</c:forEach>
	<c:choose>
		<c:when test="${page.startPage < page.totalPages}">
			<a href="javascript:goPage(${page.startPage + 1});">下一页</a>
			<a href="javascript:goPage(${page.totalPages})">末页</a>
		</c:when>
		<c:otherwise>
			<a href="javascript:void(0);">下一页</a>
			<a href="javascript:void(0);">末页</a>
		</c:otherwise>
	</c:choose>
</div>
