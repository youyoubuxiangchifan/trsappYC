<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent">
	<form method="post" action="${basePath}user.do?method=upUser" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="userIDs" value="${appUser.userId}"/>
		<div class="pageFormContent" layoutH="56">
			<div class="unit">
				<label>用户名：</label>
				<INPUT type="text" name="userName" value="${appUser.username}" disabled="disabled" style="width:260px;"/>
			</div>			
			<div class="unit">
				<label>真实姓名：</label>
				<INPUT type="text"  name="trueName" value="${appUser.truename}" class="required" style="width:260px;"/>
			</div>
			<div class="unit">
				<label>联系电话：</label>
				<INPUT type="text"  name="moblie" value="${appUser.moblie}" class="phone"  style="width:260px;"/>
			</div>			
			<div class="unit">
				<label>电子邮件：</label>
				<INPUT type="text"  name="email" value="${appUser.email}" class="required email" style="width:260px;"/>
			</div>
			<div class="unit">
				<label>联系地址：</label>
				<INPUT type="text" name="address" value="${appUser.address}" style="width:260px;"/>
			</div>						
			<div class="unit">
				<label>邮编：</label>
				<INPUT type="text"  name="zipcode" value="${appUser.zipcode}" style="width:260px;"/>
			</div>
			<c:if test="${ appUser.userId != loginUser.userId}"> <!-- 判断是否是当前登陆用户-->
				<div class="unit">
					<label>是否管理员：</label>
					<input type="radio" name="userType" value="1" <c:if test="${appUser.usertype==1}">checked="true"</c:if> /> 是    <input type="radio" name="userType" value="0" <c:if test="${appUser.usertype==0}">checked="true"</c:if>/>否
				</div>
			</c:if>						
		</div>
		
		<div class="formBar">
			<ul>
				<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>



   