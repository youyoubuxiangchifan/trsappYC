<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form name="roleForm"  method="post" action="role.do?method=${method}" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>角色名称：</label>
				<input type="text" id="rolename" name="rolename" style="width:150px;"
    				 maxlength="50"  class="required"
    				 value="${role.rolename}"></input>
			</p>
			
			<p>
				<label>角色描述：</label>
				<textarea id="roledesc" name="roledesc" rows="8" maxlength="100">${role.roledesc}</textarea>
			</p>
			
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="smtForm('${role.rolename}')">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		<input type="hidden" name="roleID" value="${role.roleId}"></input>
	</form>
</div>
<script type="text/javascript">
 	   
 		function smtForm(_oldName){
 			var _rolename = $('#rolename').val();
 			
 			if(!_rolename || _oldName==_rolename){
 				$('form[name=roleForm]').submit();
 				return;
 			}
 			var _url = "role.do?method=checkRole";
			var _data = {
				rolename : _rolename
			}
			
			$.post(_url,_data,function(result){
    					if(result=='true'){
    						alertMsg.info('角色名称不唯一,请修正！');
    						$('#rolename').focus();
    					}else{
    						$('form[name=roleForm]').submit();
    					}
  			});
 		}
 	</script>