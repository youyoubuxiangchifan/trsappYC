<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
<style> 
body,div,ul,li,p{margin:0;padding:0;}
body{color:#666;font:12px/1.5 Arial;}
ul{list-style-type:none;}
.star{position:relative;width:600px;margin:10px auto;}
.star ul,.star span{float:left;display:inline;height:19px;line-height:19px;}
.star ul{margin:0 10px;}
.star li{float:left;width:24px;cursor:pointer;text-indent:-9999px;background:url(<%=basePath%>appadmin/images/star.png) no-repeat;}
.star strong{color:#f60;padding-left:10px;}
.star li.on{background-position:0 -28px;}
.star p{position:absolute;top:20px;width:159px;height:30px;display:none;background:url(<%=basePath%>appadmin/images/icon.gif) no-repeat;padding:7px 10px 0;}
.star p em{color:#f60;display:block;font-style:normal;}
.content span{float:left;display:inline;height:19px;line-height:19px;}
</style>
</head>
<body>
<form id="commentForm" method="post" action="${basePath}appWeb.do?method=comment">
<div id="star" class="star">
    <span>点击星星就能打分：</span>
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
<div class="content">
<span>评论内容：</span>
<textarea rows="10" cols="60" name="commentContent"></textarea>
</div>
</form>
<script src="<%=basePath%>appadmin/js/oStar.js" type="text/javascript"></script>
<script type="text/javascript">

new oStar("star");

</script>
</body>
</html>
