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
	<textarea class="xheditor"
	rows="12" cols="80" style="width: 80%;height:300px;"></textarea>
	<textarea class="xheditor"
	rows="12" cols="80" style="width: 80%;height:300px;"></textarea>
</div>
<script>
	$('.xheditor').xheditor({skin:'vista',upLinkUrl:'appUpload.do?method=doXheditorUpload',upLinkExt:'zip,rar,txt',upImgUrl:'appUpload.do?method=doXheditorUpload&flag=img',upImgExt:'jpg,jpeg,gif,png',upFlashUrl:'appUpload.do?method=doXheditorUpload',upFlashExt:'swf',upMediaUrl:'appUpload.do?method=doXheditorUpload',upMediaExt:'wmv,avi,wma,mp3,mid'});
</script>
