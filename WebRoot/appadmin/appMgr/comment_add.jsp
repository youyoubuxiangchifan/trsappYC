<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent">
	<form method="post" action="${basePath}post.do?method=addComments" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="appId" value="${appId}">
		<input type="hidden" name="dataId" value="${dataId}">
		<div class="pageFormContent" layoutH="56">
		
			<div id="star" class="star unit">
    		<label>点击星星就能打分：</label>
    		<ul>
        		<li><a href="javascript:;">1</a></li>
        		<li><a href="javascript:;">2</a></li>
        		<li><a href="javascript:;">3</a></li>
       	 		<li><a href="javascript:;">4</a></li>
       	 		<li><a href="javascript:;">5</a></li>
    		</ul>
    		<span></span>
    		<p></p>
			<input type="hidden" name="commentScore" />
			</div>
		
			<div class="unit">
				<label>内容：</label>
				<textarea id="roledesc" name="commentContent" rows="5" cols="30" maxlength="100" ></textarea>
			</div>			
			<div class="unit">
				<label>审核：</label>
				<input type="radio" name="commentStatus" value="1"  /> 已审核    <input type="radio" name="commentStatus" value="0" />待审核
			</div>						
		</div>
		
		<div class="formBar">
			<ul>
				<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div> 
<style> 
body,div,ul,li,p{margin:0;padding:0;}
body{color:#666;font:12px/1.5 Arial;}
ul{list-style-type:none;}
.star{position:relative;width:500px;margin:10px auto;}
.star ul,.star span{float:left;display:inline;height:29px;line-height:29px;}
.star ul{margin:0 10px;}
.star li{float:left;width:24px;height:29px;line-height:29px;cursor:pointer;text-indent:-9999px;background:url(<%=basePath%>appadmin/images/star.png) no-repeat;}
.star strong{color:#f60;padding-left:10px;}
.star li.on{background-position:0 -28px;}
.star p{position:absolute;top:20px;width:159px;height:30px;display:none;background:url(<%=basePath%>appadmin/images/icon.gif) no-repeat;padding:7px 10px 0;}
.star p em{color:#f60;display:block;font-style:normal;}

</style>
<script src="<%=basePath%>appadmin/js/oStar.js" type="text/javascript"></script>
<script type="text/javascript">

new oStar("star");

</script> 