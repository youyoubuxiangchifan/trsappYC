<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
		<c:forEach items="${fieldList}" var="map">
			<div style="height:20px;width:100%;line-height:20px;">
				<span>
					<input id="${map.fieldId}" type="checkbox" title="${map.fieldName }" value="${map.fieldDesc}" ${map.isReserved == 1 ? "checked disabled":"" } issys = "${map.isReserved }" onclick="showRel('${rel }','${map.fieldId}','${map.fieldName }','${map.fieldDesc}')"/>
				</span>
				<span style="height:20px;line-height:20px;">${map.fieldDesc}</span>
			</div>
	   </c:forEach>
	