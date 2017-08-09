<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

   
 <div class="pageContent">
	<form method="post" action="${basePath}group.do?method=upGroup" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="groupIds" value="${appGroup.groupId }"/>
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>组织名称：</label>
				<INPUT type="text"  name="gname" value="${appGroup.gname }" class="required"  maxlength="100"/>
			</p>
			<p style="height:80px;">
				<label>组织描述：</label>
				<textarea id="roledesc" name="gdesc" rows="5" cols="30" maxlength="200" >${appGroup.gdesc }</textarea>				
			</p>
					
			<p>
				<label>是否独立组织：</label>
				<input type="radio" name="isIndependent" value="1" <c:if test="${appGroup.isIndependent==1}">checked="true"</c:if> /> 是    <input type="radio" name="isIndependent" value="0" <c:if test="${appGroup.isIndependent==0}">checked="true"</c:if> />否
			</p>								
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
   