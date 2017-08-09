<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
span.error{
	width:200px;
}
</style>
<div class="pageContent">
	<form name="metaForm"  method="post" action="meta.do?method=add" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>中文名称：</label>
				<input type="text" id="anotherName" name="anotherName" style="width:150px;"
    				 class="required"
    				 maxlength="30" 
    				 value=""/>

			<p>
				<label>英文名称：</label>
				<input type="text" id="tableName" name="tableName" style="width:150px;"
    				 class="required alphanumeric"
    				 minlength="3" maxlength="18"
    				 value=""/>
			</p>
			
			
			
			<p>
				<label>数据类型：</label>
				<select id="tableType" name="tableType" style="width:155px;margin-top:2px;"  onchange="showMT(this.value)">
    					<option value="0">通用类型</option>
    					<option value="1">主题类型</option>
    			</select>
			</p>
			<!--<p id="tm"  style="display:none;">
				<label>选择主题：</label>
				<select id="mainTable" name="mainTable" style="width:155px;margin-top:2px;">
    					<c:forEach items="${requestScope.mateList}" var="meta">
    						<option value="${meta.tableInfoId}">${meta.anotherName}</option>
    					</c:forEach>
    			</select>
			</p>
			<p>
				<label>添加系统预留字段：</label>
				<input type="radio" name="useSysField" value="0" checked="checked">否</input>
				<input type="radio" name="useSysField" value="1">是</input>
			</p>-->
			<p>
				<label>数据描述：</label>
				<textarea id="tableDesc" name="tableDesc" rows="8" maxlength="100"></textarea>
			</p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="smtForm()">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		<input type="hidden" name="roleID" value="${role.roleId}"></input>
	</form>
	
</div>
 <script type="text/javascript">
 	    //隐藏选择主题
 		function showMT(_value){
 			if(_value=='1'){
 				$('#tm').show();
 			}else if(_value=='0'){
 				$('#tm').hide();
 			}
 		}
 		function smtForm(){
 			var _tableName = $('#tableName').val();
 			if(_tableName==''){
 				$('form[name=metaForm]').submit();
 				return;
 			}
 			var _url = "meta.do?method=check";
			var _data = {
				tableName : _tableName
			}
			
			$.post(_url,_data,function(result){
    					if(result=='true'){
    						alertMsg.info('元数据名称不唯一,请修正！');
    						$('#tableName').focus();
    					}else{
    						$('form[name=metaForm]').submit();
    					}
  			});
 		}
 	</script>