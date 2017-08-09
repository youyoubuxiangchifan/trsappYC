<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form name="roleForm"  method="post" action="meta.do?method=edite" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>中文名称：</label>
				<input type="text" id="anotherName" name="anotherName" style="width:150px;"
    				 maxlength="50" class="required"
    				 value="${meta.anotherName}"></input>
			</p>

			<p>
				<label>英文名称：</label>
				<input type="text" id="tableName" name="tableName" style="width:150px;"
    				 readonly="readonly" maxlength="50" value="${meta.tableName}"></input>
			</p>
			<p>
				<label>数据描述：</label>
				<textarea id="tableDesc" name="tableDesc" rows="8" maxlength="100">${meta.tableDesc}</textarea>
			</p>
			<input type="hidden" name="id" value="${meta.tableInfoId}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		
	</form>
</div>
