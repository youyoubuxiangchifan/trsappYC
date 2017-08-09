 <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
<div class="pageContent">
	<form method="post" action="${basePath}user.do?method=userUpload" enctype="multipart/form-data" class="pageForm required-validate" onsubmit="return iframeCallback(this,impDialogAjaxDone);">

<div class="pageFormContent" layoutH="56">
			<div>
				<label>excel文件：</label>
				<input name="file" type="file" accept="application/vnd.ms-excel"/>
			</div>
			<div>
			</br>
			<H2>excel文件格式:</H2>
			</br>
			<img alt="" src="${basePath }appadmin/user/excelformat.jpg" width="550" height="130">
		   </div>

</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">

function impDialogAjaxDone(json){
	if("200"==json.statusCode){
		$.pdialog.reload("user.do?method=returnUploadInfo&succeedUser="+json.succeedUser+"&failUser="+json.failUser+"","")
	}else{
		alertMsg.correct(json.message); 
	}
	
}
</script>