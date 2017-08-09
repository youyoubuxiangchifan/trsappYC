<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<style type="text/css">
label.w160{
	 width:160px;
}
</style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent">
	<form name="descForm"  method="post" action="" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,myDialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
				<textarea id="smtDesc" class="xheditor" tools="mini" style="width:100%;height:350px;" name="smtDesc" rows="20" cols="100">${smtDesc}</textarea>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="stmappDesc()">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
	
</div>
 <script type="text/javascript">
	$('.xheditor').each(function(){
		var editorTools = $(this).attr("tools");
		$(this).xheditor({tools:editorTools,skin:'vista',upLinkUrl:'appUpload.do?method=doXheditorUpload',upLinkExt:'zip,rar,txt,doc,docx,xls,xlsx',upImgUrl:'appUpload.do?method=doXheditorUpload',upImgExt:'jpg,jpeg,gif,png',upFlashUrl:'appUpload.do?method=doXheditorUpload',upFlashExt:'swf',upMediaUrl:'appUpload.do?method=doXheditorUpload',upMediaExt:'wmv,avi,wma,mp3,mid'});
	});
    //设置父窗口的值
    function stmappDesc(){
    	var _smtDesc = $('#smtDesc').val();
    	var _appSmtDesc = $('#appSmtDesc').val();//获取父窗口的值
    	if(_smtDesc==""){
    	    alertMsg.info('应用提交说明不能为空!');
    	    return;
    	}
    	$('#appSmtDesc').val(_smtDesc);
    	$('#isEditeDesc').val('1');
    	$.pdialog.closeCurrent(); 
    }
   
 </script>

