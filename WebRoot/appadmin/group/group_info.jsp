<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
<!--
table.tabUserInfo{border:1px;border-color:#a0c6e5;border-collapse:collapse;}
table.tabUserInfo td{border: solid 1px #a0c6e5; height:30px; line-height:23px;padding-left:10px;}
-->
</style>
  <input type="hidden" name="userIDs" value="${appUser.userId}"/> 
   <table width="100%" class="tabUserInfo">  
   		
   			<tr>
   			    <td width="120">组织名称：</td>
   				<td>${appGroup.gname }</td>  				
   			</tr>
   			<tr>
   			    <td>组织描述：</td>
   				<td>${appGroup.gdesc }</td>  				
   			</tr>
   			<tr>
   			    <td>是否独立组织：</td>
   				<td><c:if test="${appGroup.isIndependent==1}">是 </c:if><c:if test="${appGroup.isIndependent==0}">否</c:if></td>  				
   			</tr>		   			   			  			   			    	

   </table>
 
   