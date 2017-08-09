<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form id="operForm"  method="post" action="sysconfig.do?method=${method}" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<div class="unit">
				<label>操作名称：</label>
				<input type="text" id="opname" name="opname" style="width:150px;"
    				 maxlength="50" class="required" value="${oper.opname}"></input>
			</div>
			
			<div class="unit">
				<label>操作描述：</label>
				<textarea id="opdesc" name="opdesc" rows="6" cols="32" maxlength="200">${oper.opdesc}</textarea>
			</div>

			<div class="unit">
				<label>操作标识：</label>
				<input type="text" id="operFlag"  class="required" name="operFlag" style="width:150px;"
    				 maxlength="50" value="${oper.operFlag}"></input>
			</div>
			
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="subNodeForm('${oper.operFlag}');">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		<input type="hidden" name="operID" value="${oper.operid}"></input>
	</form>
</div>
<script type="text/javascript">
<!--
function subNodeForm(oldoperFlag){
	var _operFlag = $("#operFlag").val();
			
	if(oldoperFlag==_operFlag){
		$('#operForm').submit();
		return;
	}
	var _url = "sysconfig.do?method=checkOper";
	
	var _data = {
		operFlag : _operFlag
	}
	$.post(_url,_data,function(result){
		if(result=='true'){
			alertMsg.info('操作标识不唯一,请修正！');
			$('#operFlag').focus();
		}else{
			$('#operForm').submit();
		}
	});
}
//-->
</script>