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
   			    <td width="120">用户名：</td>
   				<td>${appUser.username}</td>  				
   			</tr>
   			<tr>
   			    <td>真实姓名：</td>
   				<td>${appUser.truename}</td>  				
   			</tr>
   			<tr>
   			    <td>联系电话：</td>
   				<td>${appUser.moblie}</td>  				
   			</tr>
   			<tr>
   			    <td>联系地址：</td>
   				<td>${appUser.address}</td>  				
   			</tr> 
   			<tr>
   			    <td>电子邮件：</td>
   				<td>${appUser.email}</td>  				
   			</tr>
   			<tr>
   			    <td>邮编：</td>
   				<td>${appUser.zipcode}</td>  				
   			</tr>
   			<tr>
   			    <td>是否管理员：</td>
   				<td><c:if test="${appUser.usertype==1}">是 </c:if><c:if test="${appUser.usertype==0}">否</c:if>  				
   			</tr>   			   			   			  			   			    	

   </table>
 
   