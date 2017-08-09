<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>信件详情</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/reset.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/layout.css"/>
<script src="<%=basePath%>web/images/jquery-1.7.2.min.js" type="text/javascript"></script>
<style> 
body,div,ul,li,p{margin:0;padding:0;}
body{color:#666;font:12px/1.5 Arial;}
ul{list-style-type:none;}
.star{position:relative;width:600px;margin:10px auto;margin-left:0px;}
.star ul,.star span{float:left;display:inline;height:19px;line-height:19px;}
.star ul{margin:0 10px;}
.star li{float:left;width:24px;cursor:pointer;text-indent:-9999px;background:url(<%=basePath%>appadmin/images/star.png) no-repeat;}
.star strong{color:#f60;padding-left:10px;}
.star li.on{background-position:0 -28px;}
.star p{position:absolute;top:20px;width:159px;height:30px;display:none;background:url(<%=basePath%>appadmin/images/icon.gif) no-repeat;padding:7px 10px 0;}
.star p em{color:#f60;display:block;font-style:normal;}
</style>
</head>

<body>
<!-- START header --> 
<jsp:include page="../include/webheader.jsp"/> 
<!-- END header -->
<div class="w">
  <div class="fl mr10 mb10 B i_GL">
    <div class="i_GL_1"><span>公众参与</span></div>
    <ul class="i_GL_2">
      <c:forEach items="${requestScope.appList}" var="app">
     	 <li <c:if test="${app.appId==appId}">class="i_GL_2H"</c:if>>
     	 	<%@ include file="../include/left.jsp"%> 
     	 </li>
     </c:forEach>
    </ul>
  </div>
  <div class="fr B mb10 i_Gr">
    <div class="i_Gr_6 mb10">信件详细</div>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="i_Gr_5">
        <c:forEach items="${dataDetail}" var="data">
        <c:choose>
        	 <c:when test="${data[2]=='textarea' or data[2]=='editor' or data[2]=='smeditor'}">
        	 	<tr>
          			<td>${data[0]}</td>
         			<td>
            		<div class="i_Xx_5 i_Xx_O" style="height:auto !important;max-height:300px;">
            		 ${data[1]}
            		</div>          
          			</td>
        		</tr>
        	 	
        	 </c:when>
        	 <c:otherwise>
        	 	<tr>
          			<td width="11%">${data[0]}</td>
          			<td width="89%">
          				<c:choose>
          					
          				   <c:when test="${data[2]=='date'}">
          				   		${fn:substring(data[1], 0, 10)}
          				   </c:when>
          				   <c:otherwise>
          			 <c:if test="${data[0] == '办理状态'}">
            		 <c:if test="${data[1] == '0'}">待办理</c:if>
            		 <c:if test="${data[1] == '1'}">办理中</c:if>
            		 <c:if test="${data[1] == '2'}">已办理</c:if>           		
            		 </c:if>
            		 <c:if test="${data[0] != '办理状态'}">
            		 ${data[1]}
            		 </c:if>
          				   
          				   </c:otherwise>
          				</c:choose>
          			</td>
        		</tr>
        	 </c:otherwise>
        </c:choose>
        </c:forEach>
        <c:if test="${adixList!=null and  fn:length(adixList)>0}">
        	<tr>
          			<td width="11%">附件：</td>
          			<td width="89%">
          			     <c:forEach items="${adixList}" var="adix">
                             <a href="<%=basePath%>appWeb.do?method=downLoadFile&appendixId=${adix.appendixId}&flag=0" target="_blank">${adix.fileName}</a>&nbsp;&nbsp;
        				</c:forEach>
          			</td>
            </tr>			
        </c:if>
        
      </table>
      
      <c:choose>
      		<c:when test="${isHasComment==1 and page!=null}">
      			<ul class="s_Rli">
      				<c:forEach items="${page.ldata}" var="comment" begin="0" end ="0"> 

					 <li>
					   <p><span class="i_GrP_1">姓名：</span>${comment.commentUser }</h6>
					   <p><span class="i_GrP_1">满意度：</span>
					   			<c:choose>
							   		<c:when test="${comment.commentScore==1}">很不满意</c:when>
							   		<c:when test="${comment.commentScore==2}">不满意</c:when>    
                                    <c:when test="${comment.commentScore==3}">一般</c:when>   
                                    <c:when test="${comment.commentScore==4}">满意</c:when>  
                                    <c:when test="${comment.commentScore==5}">非常满意</c:when>   
							   </c:choose>
					   </p>
					   <p><span class="i_GrP_1">评论内容：</span>${comment.commentContent}</p>
					   <span class="s_Rtime"><fmt:formatDate value="${comment.crtime}" pattern="yyyy-MM-dd"/> </span>
					 </li>
					 </c:forEach>
	  			</ul>
      		</c:when>
      		<c:when test="${isHasComment==2 and page!=null and fn:length(page.ldata)>0}">
      			<ul class="s_Rli">
      				<c:forEach items="${page.ldata}" var="comment"> 

						 <li>
						   <p><span class="i_GrP_1">姓名：</span>${comment.commentUser }</h6>
						   <p><span class="i_GrP_1">满意度：</span>
							   <c:choose>
							   		<c:when test="${comment.commentScore==1}">很不满意</c:when>
							   		<c:when test="${comment.commentScore==2}">不满意</c:when>    
                                    <c:when test="${comment.commentScore==3}">一般</c:when>   
                                    <c:when test="${comment.commentScore==4}">满意</c:when>  
                                    <c:when test="${comment.commentScore==5}">非常满意</c:when>   
							   </c:choose>
						   </p>
						   <p><span class="i_GrP_1">评论内容：</span>${comment.commentContent}</p>
						   <span class="s_Rtime"><fmt:formatDate value="${comment.crtime}" pattern="yyyy-MM-dd"/> </span>
						 </li>
					 </c:forEach>
	  			</ul>
	  			<!-- 分页开始 -->
				<form id="pagerForm" name="pagerForm" action="appWeb.do?method=appDataDetail" method="post">
					<input type="hidden" name="dataId" value="${dataId}"/>
					<input type="hidden" name="appId" value="${appId}"/>
		    		<input type="hidden" name="groupId" value="${groupId}"/>
		    		<input type="hidden" id="pageNum" name="pageNum" value="${page.startPage}"/>
				</form>
				<jsp:include page="../common/pagination_blue.jsp"></jsp:include>
				<!-- 分页结束 -->
			    <c:if test="${page ==null or fn:length(page.ldata)<1}">
			    	<div class="cor_b i_Xx_4">暂时没有匹配的意见</div>
			    </c:if>
      	</c:when>
      	
      </c:choose>
     <c:if test="${isHasComment==2 or (isSerach==1 and isHasComment==1 and page!=null and fn:length(page.ldata)==0)}">
      <form id="comForm" action="appWeb.do?method=comment" method="post">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="i_Gr_7 mb10">
        <tr>
         <td colspan="2">
         	<div id="star" class="star">
			    <span>点击星星打分：</span>
			    <ul>
			        <li><a href="javascript:;">1</a></li>
			        <li><a href="javascript:;">2</a></li>
			        <li><a href="javascript:;">3</a></li>
			        <li><a href="javascript:;">4</a></li>
			        <li><a href="javascript:;">5</a></li>
			    </ul>
			    <span></span>
			    <p></p>
				<input id="commentScore" type="hidden" name="commentScore" />
			</div>
         </td>
         <!--  
          <td width="15%" height="30" align="right" class="i_Gr_8">满意度评价</td>
          <td width="85%">
          	<input name="commentScore" type="radio" value="100" checked="checked"/>&nbsp;非常满意&nbsp;
          	<input name="commentScore" type="radio" value="80" />&nbsp;满意&nbsp;
          	<input name="commentScore" type="radio" value="60" />&nbsp;一般&nbsp;
          	<input name="commentScore" type="radio" value="40" />&nbsp;不满意
          </td>
          -->
        </tr>
        <tr>
          <td height="30" class="i_Gr_8" align="right">评论内容</td>
          <td><textarea id="commentContent" name="commentContent" cols="" rows="" class="i_Gr_1_ipt4"></textarea></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td><input type="button" class="i_Gr_1_btn1" value="发送" onclick="subComment()"/></td>
        </tr>
      </table>
      <input type="hidden" name="appId" value="${appId}"/>
      <input type="hidden" name="dataId" value="${dataId}"/>
      </form>
      </c:if>
  </div>
  <div class="clear"></div>
</div>
<!-- START footer --> 
<jsp:include page="../include/webfooter.jsp"  /> 
<!-- END footer --> 
</body>
<script src="<%=basePath%>appadmin/js/oStar.js" type="text/javascript"></script>
<script type="text/javascript">
    function subComment(){
    	var _commentScore = $('#commentScore').val();
    	var _commentContent = $('#commentContent').val();
    	if(!_commentContent){
    	   alert('评论内容不能为空！');
    	   return;
    	}
    	var data = {
    		method : 'comment',
    		dataId : ${dataId==null?0:dataId},
    		appId : ${appId==null?0:appId},
    		commentScore : _commentScore,
    		commentContent :_commentContent
    	}
    	$.post("appWeb.do",data,function(text){
    	        if(text){
    	        	alert("评论提交成功。");
    	        }else{
    	        	alert("评论提交失败。");
    	        }
    	});
    }
    new oStar("star");
</script>
</html>
