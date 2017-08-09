<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="table" width="100%">
		<thead>
			<tr>
				<th width="50">序号</th>
				<th>应用字段</th>
				<th width="100">wcm字段名称</th>
				<th width="100">删除</th>
			</tr>
		</thead>
		<tbody>
		 <c:forEach items="${requestScope.fieldList}" var="field" varStatus="num">
		   <tr>
		       <td>${num.index+1}</td>
		       <td>${field.appFieldDesc}[${field.appFieldName}]</td>
		       <td>${field.toFieldName}</td>
		       <td><span><img onclick="delRel('${field.syncFieldId}')" src="${basePath}appadmin/images/delbtn.gif"></img></span></td>
		   </tr>
		  </c:forEach>
		</tbody>
</table>