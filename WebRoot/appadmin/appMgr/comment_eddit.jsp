<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent">
	<form method="post" action="${basePath}post.do?method=upComments" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="commentIds" value="${appComment.commentId}"/>
		<div class="pageFormContent" layoutH="56">
			<div class="unit">
				<label>内容：</label>
				<textarea id="roledesc" name="commentContent" rows="5" cols="30" maxlength="100" >${appComment.commentContent }</textarea>
			</div>			
			<div class="unit">
				<label>审核：</label>
				<input type="radio" name="commentStatus" value="1" <c:if test="${appComment.commentStatus==1}">checked="true"</c:if> /> 已审核    <input type="radio" name="commentStatus" value="0" <c:if test="${appComment.commentStatus==0}">checked="true"</c:if>/>待审核
			</div>						
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