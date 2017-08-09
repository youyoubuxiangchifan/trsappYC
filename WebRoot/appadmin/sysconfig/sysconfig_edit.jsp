<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form id="configForm"  method="post" action="sysconfig.do?method=${method}" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<div class="unit">
				<label>名称：</label>
				<input type="text" id="configname" name="configname" style="width:150px;"
    				 maxlength="50" class="required" value="${sysconfig.configName}"></input>
			</div>
			
			<div class="unit">
				<label>内容：</label>
				<textarea id="configvalue" name="configvalue" rows="4" cols="32" class="required" maxlength="200">${sysconfig.configValue}</textarea>
			</div>

			<div class="unit">
				<label>描述：</label>
				<textarea id="configdesc" name="configdesc" rows="4" cols="32" maxlength="200">${sysconfig.configDesc}</textarea>
			</div>
			
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="subNodeForm('${sysconfig.configName}');">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		<input type="hidden" name="configID" value="${sysconfig.configId}"></input>
	</form>
</div>
<script type="text/javascript">
<!--
function subNodeForm(oldconfigname){
	var _configname = $("#configname").val();
	
	if(oldconfigname==_configname){
		$('#configForm').submit();
		return;
	}
	var _url = "sysconfig.do?method=checkSysConfig";
	
	var _data = {
		configname : _configname
	}
	$.post(_url,_data,function(result){
		if(result=='true'){
			alertMsg.info('系统配置名称不唯一,请修正！');
			$('#configname').focus();
		}else{
			$('#configForm').submit();
		}
	});
}
//-->
</script>
