<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form id="msgTempForm"  method="post" action="sysconfig.do?method=${method}" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<div class="unit">
				<label>提醒分类：</label>
				<select name="remindType" id="remindType" style="width:155px;">
					<option value="1" <c:if test="${msgTemp.remindType == 1}">selected="selected"</c:if>>办理提醒</option>
					<option value="2" <c:if test="${msgTemp.remindType == 2}">selected="selected"</c:if>>退回提醒</option>
					<option value="3" <c:if test="${msgTemp.remindType == 3}">selected="selected"</c:if>>超期提醒</option>
					<option value="4" <c:if test="${msgTemp.remindType == 4}">selected="selected"</c:if>>催办提醒</option>
				</select>
			</div>
			<div class="unit">
				<label>插入变量：</label>
				<select id="setContent" style="width:155px;" onchange="getOpers()">
					<option value="">--请选择--</option>
					<option value="{@title}">主题</option>
					<%--<option value="{@cruser}">创建者</option>
					<option value="{@crtime}">创建时间</option>
				--%></select>
			</div>
			
			<div class="unit">
				<label>插入对象：</label>
				<input type="radio" name="setObj" value="1" checked="checked"></input>模板主题
    			<input type="radio" name="setObj" value="2"></input>模板内容	 
			</div>
			
			<div class="unit">
				<label>模板类型：</label>
				<input type="radio" name="tempType" value="2" <c:if test="${msgTemp.tempType != 1}">checked="checked"</c:if>></input>短信
    			<input type="radio" name="tempType" value="1" <c:if test="${msgTemp.tempType == 1}">checked="checked"</c:if>></input>邮件	 
			</div>
			
			<div class="unit">
				<label>模板主题：</label>
				<input type="text" id="tempTitle" name="tempTitle" style="width:150px;"
    				 maxlength="50"  class="required" value="${msgTemp.tempTitle}" onclick="getTiCursorPosition()"></input>
			</div>
			
			<div class="unit">
				<label>模板内容：</label>
				<textarea id="elm1" class="editor" style="width:100%;height:350px;float:left;" tools="simple" id="tempContent" name="tempContent" rows="6" cols="32"  class="required" >${msgTemp.tempContent}</textarea>
			</div>
			
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="subNodeForm('${msgTemp.remindType}','${msgTemp.tempType}');">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		<input type="hidden" name="msgTempID" value="${msgTemp.tempId}"></input>
	</form>
</div>

<script type="text/javascript">
var titleSize = 0;
var contentSize = 0;
//提交表单
function subNodeForm(oldremindType,oldtempType){
	var _remindType = $("#remindType").val();
	var _tempType = $("input[name='tempType']:checked").val();
			
	if(oldremindType==_remindType && oldtempType==_tempType){
		$('#msgTempForm').submit();
		return;
	}
	var _url = "sysconfig.do?method=checkMsgTemp";
	
	var _data = {
		remindType : _remindType,
		tempType : _tempType
	}
	$.post(_url,_data,function(result){
		if(result=='true'){
			alertMsg.info('提醒分类和模板类型不唯一,请修正！');
			$('#remindType').focus();
		}else{
			$('#msgTempForm').submit();
		}
	});
}

function getTiCursorPosition(){
	titleSize = getCursorPosition("tempTitle");
}

function getCoCursorPosition(){
	contentSize = getCursorPosition("tempContent");
}

function getCursorPosition(obj){
    var evt =window.event?window.event:getTa1CursorPosition.caller.arguments[0];
    var oTa1 = document.getElementById(obj);
    if(oTa1.value=="") return 0;
    var cursurPosition=-1;
    if(oTa1.selectionStart){//非IE浏览器
         cursurPosition= oTa1.selectionStart;
    }else{//IE
	     var rngSel = document.selection.createRange();//建立选择域
	     var rngTxt = oTa1.createTextRange();//建立文本域
	     var flag = rngSel.getBookmark();//用选择域建立书签
	     rngTxt.collapse();//瓦解文本域到开始位,以便使标志位移动
	     rngTxt.moveToBookmark(flag);//使文本域移动到书签位
	     rngTxt.moveStart('character',-oTa1.value.length);//获得文本域左侧文本
	     cursurPosition = rngTxt.text.replace(/\r\n/g,'').length;//替换回车换行符
    }
    return cursurPosition;
}

function getOpers(){
	var _setContent = $("#setContent").val();
	var _tempTitle = $("#tempTitle").val();
	var _tempContent = $("#tempContent").val();
	var _setObj = $("input[name='setObj']:checked").val();
	if(_setContent!=""){
		if(_setObj == '1'){
			_tempTitle = _tempTitle.substring(0,titleSize) + _setContent + _tempTitle.substring(titleSize);
			document.getElementById("tempTitle").value = _tempTitle;
		}else if(_setObj == '2'){
			//_tempContent = _tempContent.substring(0,contentSize) + _setContent + _tempContent.substring(contentSize);
			//document.getElementById("tempContent").value = _tempContent;
			var editor = $('#elm1').xheditor();//方式1
			editor.pasteHTML(_setContent);
		}
	}
}

</script>
