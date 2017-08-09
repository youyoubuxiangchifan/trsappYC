<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pageContent">
	<form name="flowForm"  method="post" action="flow.do?method=${method}" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>流程名称：</label>
				<input type="text" id="flowName" name="flowName" style="width:150px;"
    				 maxlength="50" class="required"
    				 value="${flow.flowName}"></input>
			</p>

			<p>
				<label>流程描述：</label>
				<textarea id="flowDesc" name="flowDesc" rows="8" maxlength="100">${flow.flowDesc}</textarea>
			</p>
			<input type="hidden" name="id" value="${flow.flowId}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="checkName('${flow.flowName}')">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
	
</div>
 <script type="text/javascript">
		function checkName(oldName){
		    
		    var _flowName = $('#flowName').val();
		    if(oldName==_flowName){
		    	$('form[name=flowForm]').submit();
		    	return;
		    }
			var _url = "flow.do?method=check";
			var _data = {
				flowName : _flowName
			}
			
			$.post(_url,_data,function(result){
    					if(result=='true'){
    						alertMsg.info('流程名称不唯一,请修正！');
    						$('#flowName').focus();
    					}else{
    						$('form[name=flowForm]').submit();
    					}
  			});
		}
 </script>

