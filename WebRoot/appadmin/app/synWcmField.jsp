<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form name="viewFieldPagerForm" id="pagerForm" class="pageForm" method="post"  action="appView.do?method=listField">
		<div class="searchBar">
			应用字段：&nbsp;
			<select id="appFieldName" name="appFieldName" style="width:100px;margin-top:2px;" onchange="$('#toFieldName').val('');">
    					<option value="">请选择字段</option>
    					<option value="METADATAID">TRSAPP数据ID</option>
    					<c:forEach items="${fieldList}" var="item">
    						<option value="${item.fieldName }">${item.fieldDesc }</option>
    					</c:forEach>
    		</select>
    		&nbsp;&nbsp;目标字段：&nbsp;
			<INPUT type="text" id="toFieldName" name="toFieldName"  maxlength="50" class="required"/>
			<INPUT type="hidden" id="appId" name="appId" value="${appId}"/>
			<INPUT type="hidden" id="toAppName" name="toAppName" value="${toAppName}"/>
			&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="addRel()">添加</button>
		</div>
	</form>
</div>
<div class="pageContent">
 <div  id="sysFieldList" layoutH="160">
 
 </div>
 	<form id="synConditionForm" name="synConditionFieldForm"  method="post" action="appView.do?method=saveSynCondition" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
	 <fieldset style="width:95%;margin:10px auto;">
	 	<legend>数据过滤条件</legend>
	 	<p style="padding:5px;">
	 	deleted = 0 and APPID = ? and (syncflag != 1 or syncflag is null) 
	 	<input style="width:80%" name="synCondition" id="synCondition" value="${synCondition }"/>
	 	<INPUT type="hidden" id="appId" name="appId" value="${appId}"/>
	 	<br/>
	 	</p>
	 </fieldset>
 	<div class="formBar">
		<ul>
			<li>
				<div class="button"><div class="buttonContent"><button type="submit">确定</button></div></div>
			</li>
			<li>
				<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
			</li>
		</ul>
	</div>
 	</form>
</div>
<script>
	function addRel(){
		var _url = "appView.do?method=addSynField";
		var _appFieldName = $('#appFieldName').val();
		var _appFieldDesc = $("#appFieldName").find("option:selected").text();
		var _toFieldName = $('#toFieldName').val();
		var _appId = $('#appId').val();
		var _toAppName = $('#toAppName').val();
		if(_appFieldName==''){
			alertMsg.info('请选择应用字段！');
			return;
		}else if( _toFieldName==''){
			alertMsg.info('同步对应字段不能为空！');
			return;
		}
		var _data = {
			appFieldName:_appFieldName,
			appFieldDesc:_appFieldDesc,
			appId:_appId,
			toAppName:_toAppName,
			toFieldName:_toFieldName
		}
		$.post(_url,_data,function(_isSuc){
			if(_isSuc=='true'){
				refreshTab(_appId,_toAppName);
			}else{
				alertMsg.info('操作失败！');
			}
		});
		
	}
	function delRel(_syncFieldId){
		var _data = {
			syncFieldId : _syncFieldId
		}
		var _appId = $('#appId').val();
		var _toAppName = $('#toAppName').val();
		var _url = "appView.do?method=delSynField";
		$.post(_url,_data,function(_isSuc){
			if(_isSuc=='true'){
				refreshTab(_appId,_toAppName);
			}else{
				alertMsg.info('操作失败！');
			}
		});
	}
	//根据部门id来局部刷新用户列表，部门id==0刷出所有的用户
    function refreshTab(_appId,_toAppName){
    	var _url = "appView.do?method=listSynField&appId="+_appId+"&toAppName="+_toAppName;
    	$("#sysFieldList").loadUrl(_url,"", "");
    }
    refreshTab('${appId}','${toAppName}');
</script>
