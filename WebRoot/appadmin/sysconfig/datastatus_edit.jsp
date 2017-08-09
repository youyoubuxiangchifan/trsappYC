<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form id="dataStatusForm"  method="post" action="sysconfig.do?method=${method}" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<div class="unit">
				<label>状态名称：</label>
				<input type="text" id="statusname" name="statusname" style="width:150px;"
    				 maxlength="50"  class="required" value="${dataStatus.statusname}"></input>
			</div>
			
			<div class="unit">
				<label>状态描述：</label>
				<textarea id="statusdesc" name="statusdesc" rows="7" cols="32"  maxlength="200">${dataStatus.statusdesc}</textarea>
			</div>
			
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="subNodeForm('${dataStatus.statusname}');">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		<input type="hidden" name="dataStatusID" value="${dataStatus.datastatusId}"></input>
	</form>
</div>
<script type="text/javascript">
<!--
function subNodeForm(oldstatusname){
	var _statusname = $("#statusname").val();
			
	if(oldstatusname==_statusname){
		$('#dataStatusForm').submit();
		return;
	}
	var _url = "sysconfig.do?method=checkDataStatus";
	
	var _data = {
		statusname : _statusname
	}
	$.post(_url,_data,function(result){
		if(result=='true'){
			alertMsg.info('状态名称不唯一,请修正！');
			$('#statusname').focus();
		}else{
			$('#dataStatusForm').submit();
		}
	});
}
//-->
</script>
