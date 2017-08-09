<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
	.arr_line{position:absolute;border-bottom:5px solid #005aab;height:10px;width:95px;left:10px;top:60px;}
</style>
<script>
  $(document).ready(function(){
   $("li.dh").mouseover(function(){
   		$(".arr_line").stop(true,false);
 		var thisLeft = $(this).offset().left;
 		var preLeft = $(".xx").offset().left;
 		var left = thisLeft-preLeft;
 		$(".arr_line").animate({"left": left+"px"}, "700","swing",function(){
 			$(".arr_line").stop(true,false);
 			
 		});
 		//$(".xx").mouseleave(function(){
 			//$("#app_${appId}").trigger("mouseover");
 		//});
 	});
 	$("#app_${appId}").trigger("mouseover");	
 	
  });
</script>
<div class="i_top">
  <ul class="w">
    <li><a href="#">网站首页</a></li>     
    <li><a href="#">设为首页</a></li>
    <li><a href="#">加入收藏</a></li>
  </ul>
</div>
<div class="i_banner w">
  <div class="fl i_logo"><a href="<%=basePath%>${groupId }/index.html"><img src="<%=basePath%>web/images/hd_logo.jpg"/></a></div>
  <div class="fr i_link xx" style="position:relative;">
    <ul>
    <c:forEach items="${appList}" var="appinfo" begin="0" end="7" varStatus="num"> <!--遍历应用 -->
       	<c:choose>
		 	<c:when test="${num.index eq -1}"><!--默认第一个选中-->
       		<li class="i_lM_H dh"><a href="<%=basePath%>/query/${groupId }/${appinfo.appId }.html">${appinfo.viewName}</a></li>
       		</c:when>
       		<c:otherwise><!--其它的不选中-->
			<li class="i_lM dh" id="app_${appinfo.appId }"><a href="<%=basePath%>query/${groupId }/${appinfo.appId }.html">${appinfo.viewName}</a></li>
			</c:otherwise>
       </c:choose>
     </c:forEach>
    </ul>
    <div class="arr_line">&nbsp;</div>
  </div>
  <div class="clear"></div>
</div>
