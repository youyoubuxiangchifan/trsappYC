<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument.Include"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>互动平台首页</title>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/reset.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/layout.css"/>
<script src="<%=basePath%>web/images/jquery-1.7.2.min.js" type="text/javascript"></script>
<style type="text/css">
	.arr_line{position:absolute;border-bottom:5px solid #005aab;height:10px;width:95px;left:10px;top:60px;}
	.go_top{display:block;width:50px;height:50px;background-image:url(<%=basePath%>web/images/go_top.png);bottom: 115px;cursor:pointer;display:block;right:20px;position:fixed;display:none}
	.go_bottom{display:block;width:50px;height:50px;background-image:url(<%=basePath%>web/images/go_bottom.png);background-position: 50px 0px;bottom:65px;cursor:pointer;display:block;right:20px;position:fixed;display:none}
	.go_top:hover{background-position: 50px 0px;}
	.go_bottom:hover{background-position: 0px 0px;}
</style>
<script type="text/javascript">
	/** 
	* Description: 比较两个时间的大小，用于判断主题类应用是否正在进行   
	* author liujian
	* date 2014-4-24 下午15:44:05
	* @param fDate 第一个时间参数
	* @param sDate 第二个时间参数
	* @return 第一个时间大于等于第二个时间返回"进行中";否则返回"已结束"
	* version 1.0
	*/
function dateCompare(fDate,sDate){   
	var arr=fDate.split("-");    
	var fDateTime=new Date(arr[0],arr[1],arr[2]);    
	var fDateTimes=fDateTime.getTime();   
	var arrs=sDate.split("-");    
	var sDateTime=new Date(arrs[0],arrs[1],arrs[2]);    
	var sDateTimes=sDateTime.getTime();   
	if(fDateTimes>=sDateTimes){   
		document.write("进行中");   
	}else  
	document.write("已结束");   
  }

 
</script>
</head>

<body>

<!--star header-->
<jsp:include page="../include/webheader.jsp" /> 
<!--end header-->
<div class="w">
  <div class="fl mr10 i_L">
  <c:forEach items="${appDataMap}" var="dataMap" > <!--遍历应用 -->
  	<c:choose>
		<c:when test="${appTypeMap[dataMap.key] eq 0}"><!--非主题应用-->
    <div class="B mb10 i_Lm1">
         <div class="i_Lm1_t1"></div> 
         <div class="i_Lm1_t2"></div>
         <div class="i_Lm1_t3"></div>
         <div class="i_Lm1_t4"></div>  
      <div class="i_Lm1T"><span class="i_Lm1T_bt">${vaiewName[dataMap.key]}</span><span class="i_Lm1T_bt2"></span></div>
       <div class="w95 hDR_C4T">
          <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
              <td width="50%" align="center" class="f_w">主题</td>
              <td width="11%" align="center" class="f_w">状态</td>
              <td width="12%" align="center" class="f_w">回复部门</td>
              <td width="13%" align="center" class="f_w">写信日期</td>
              <td width="14%" align="center" class="f_w">点击率</td>
            </tr>
          </table>
       </div>
      <div class="hDR_C4">      
      
      <c:forEach items="${dataMap.value}" var="data" > <!-- 遍历应用 数据List-->
        <ul>
        <c:forEach items="${data}" var="valueMap" >   <!-- 遍历单条数据字段信息-->          
        <c:if test="${valueMap.key eq titleFiledMap[dataMap.key]}"> <!-- 判断标题字段-->
          <li class="hDR_C4_L1"><a href="<%=basePath%>appWeb.do?method=appDataDetail&groupId=${groupId }&appId=${dataMap.key }&dataId=${data.METADATAID}">${fn:substring(valueMap.value, 0, 25)}</a></li>
        </c:if>      
        </c:forEach>
        
          <li class="hDR_C4_L2"><c:if test="${data.STATUS eq 0}">待办理</c:if><c:if test="${data.STATUS eq 1}">办理中</c:if><c:if test="${data.STATUS eq 2}">已办理</c:if></li>
          <li class="hDR_C4_L3">${data.REPLYDEPT}</li>
          <li class="hDR_C4_L4">${data.CRTIME}</li>
          <li class="hDR_C4_L5">${data.HITCOUNTS}</li>
        </ul>
        </c:forEach>
        
      </div>
    </div>
   	</c:when>
   </c:choose>
   </c:forEach>   
  </div>
  
  
  <div class="fr i_R">
   <c:forEach items="${appDataMap}" var="dataMap" > <!-- 遍历应用 -->
  	<c:choose>
		<c:when test="${appTypeMap[dataMap.key] eq 1}"><!-- 主题应用-->   
    <div class="B mb10 i_Rm2">
         <div class="i_Lm1_t1"></div> 
         <div class="i_Lm1_t2"></div>
         <div class="i_Lm1_t3"></div>
         <div class="i_Lm1_t4"></div>  
      <div class="i_Rm2T">${vaiewName[dataMap.key]}</div>
      <ul class="i_list">
      	<c:forEach items="${dataMap.value}" var="data" > <!-- 遍历应用 数据List-->
      	 <c:forEach items="${data}" var="valueMap" >   <!-- 遍历单条数据字段信息-->
           
        <c:if test="${valueMap.key eq titleFiledMap[dataMap.key]}"> <!-- 判断标题字段-->
        <li><span><script type="text/javascript">dateCompare("${data.ENDTIME}","${now}");</script></span><a href="<%=basePath%>appWeb.do?method=appDataDetail&groupId=${groupId }&appId=${dataMap.key }&dataId=${data.METADATAID}&flag=1">${fn:substring(valueMap.value, 0, 15)} </a></li>
        </c:if>
        </c:forEach>
        </c:forEach>

      </ul> 
    </div>
   	</c:when>
   </c:choose>
   </c:forEach> 
  </div>
  <div class="clear"></div>
</div>
<!--star footer-->
<jsp:include page="../include/webfooter.jsp"  /> 
<!--end footer-->
<span id="go_top" class="dib go_top">&nbsp;</span>
<span id="go_bottom" class="dib go_bottom">&nbsp;</span>
    <script type="text/javascript">
(function($,doc,win){
	var 
	$doc       = $(document),
	$win       = $(window),
	$gotop	   = $("#go_top"),
	$gobottom	   = $("#go_bottom"),
	_setDisplay = function(){
		var 
		h  = $win.height(),
		st = $doc.scrollTop();
		sth = h+st;
		doch = $doc.height();

		if(st==0){
			$gotop.fadeOut(100);
			$gobottom.fadeOut(100);
		}
		if(sth<doch && st>0){
			$gotop.fadeIn(200);
			$gobottom.fadeIn(200);
		}
		if(sth==doch){
			$gotop.fadeIn(300);
			$gobottom.fadeOut(100);
		}

	},

	_setScrollTop = function(flag){
		var val = 0;
		if(flag == "bottom"){//底部
			val = $doc.height() - $win.height();			
		}
		$("html,body").animate({ "scrollTop": val}, 500);//默认返回到顶部
	};

	//返回顶部绑定事件
	$gotop.bind("click",function(){
		_setScrollTop("top");
		//$gotop.hide();
	});
	$gobottom.bind("click",function(){
		_setScrollTop("bottom");
		//$gobottom.hide();
	});

	//绑定滚动事件
	$(win).scroll(function(e){
		_setDisplay();		
	});

})(jQuery,document,window);

</script>
</body>
</html>

