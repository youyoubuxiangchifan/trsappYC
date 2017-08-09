<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent">
	<form name="formGroup" method="post" action="${basePath}group.do?method=addGroup" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
		<INPUT type="hidden" name="parentId" id="parentId" value="${groupIds}" class="required" />
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>父组织：</label>
				<INPUT type="text" name="parentName" value="${gName }"  class="required" disabled/>
			</p>
			<p>
				<label>组织名称：</label>
				<INPUT type="text"  name="gname" id="gname" value=""  class="required" minlength="3"  maxlength="100"/>
			</p>
			<p style="height:80px;">
				<label>组织描述：</label>
				<textarea id="roledesc" name="gdesc" rows="5" maxlength="200" ></textarea>				
			</p>
			<c:if test="${groupIds != 0}"> <!-- 如果是一级组织，默认为独立组织-->		
			<p>
				<label>是否独立组织：</label>
				<input type="radio" name="isIndependent" value="1" /> 是    <input type="radio" name="isIndependent" value="0" checked="true"/>否
			</p>
			</c:if>								
		</div>
		<div class="formBar">
			<ul>
				<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="checkOlny()">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">
<!--
function checkOlny() {
			var _gName = $.trim($('#gname').val());
			var _groupIds = $('#parentId').val();
 			var _url = "group.do?method=checkGroupName";
			var _data = {
				gname : _gName,
				groupIds : _groupIds
			}
			
				$.post(_url,_data,function(result){
    					if(result=='false'){
    						alertMsg.info('组织名称不唯一,请修正！');
    						$('#gname').focus();
    					}else{
    						$('form[name=formGroup]').submit();
    					}
  				});			
}

//-->
</script>
   