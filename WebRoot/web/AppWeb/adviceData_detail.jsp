<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>信件详情</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/reset.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/layout.css"/>
<script language="javascript" type="text/javascript" src="<%=basePath%>web/js/My97DatePicker/WdatePicker.js"></script>
<script src="<%=basePath%>web/images/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="<%=basePath%>web/images/jquery.validate.js" type="text/javascript"></script>
<script src="<%=basePath%>web/images/jquery.validate.mesage_cn.js" type="text/javascript"></script>
<script src="<%=basePath%>web/images/jquery.metadata.js" type="text/javascript"></script>
</head>

<body>
<script type="text/javascript">
$(document).ready(function(){
	$("#formData").validate();
});
</script>
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
    <div class="i_Gr_9"><span>${viewName}</span></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="i_Gr_5">
        <c:forEach items="${dataDetail}" var="data">
        <c:choose>
        	 <c:when test="${data[3]=='textarea' or data[3]=='editor' or data[3]=='smeditor'}">
        	 	<tr>
          			<td>${data[0]}</td>
         			<td>
            		<div class="i_Xx_5 i_Xx_O" style="height:auto !important;max-height:300px;">${data[1]}</div>          
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
          				   ${data[1]}
          				   </c:otherwise>
          				</c:choose>
          			</td>
        		</tr>
        	 </c:otherwise>
        </c:choose>
        </c:forEach>
      </table>
      <c:choose>
	      <c:when test="${page!=null and fn:length(page.ldata[0])>0}">
	      <ul class="s_Rli">
	  	    <c:forEach items="${page.ldata}" var="advice"> 
			 <li>
			   <c:forEach items="${itemList}" var="fieldRel"> 
			   <c:if test="${fieldRel.fieldName!='CRTIME'}">
			   		<p><span class="i_GrP_1">${fieldRel.fieldDesc}：</span>${advice[fieldRel.fieldName]}</h6>
			   </c:if>
			   </c:forEach>
			   <span class="s_Rtime">${advice['CRTIME']}</span>
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
	      </c:when>
	      <c:otherwise>
	      	<div class="cor_b i_Xx_4">暂时没有匹配的意见</div>
	      </c:otherwise>
	   </c:choose>
	 <c:choose>
	 <c:when test="${requestScope.fieldList[0]!=null and isOld>0}">
      <form id="formData" name="formData" action="appWeb.do?method=saveAppData" method="post" enctype="multipart/form-data">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="i_Gr_7 mb10">
       	<c:set var="required" value="" />
       	<c:set var="maxlength" value="" />
       	<c:set var="formFieldType" value="" />
       	<c:set var="validateJson" value="" />
       	<c:set var="cboxName" value="" />
       <c:forEach items="${requestScope.fieldList}" var="field">
       <c:if test="${field.notNull == 1}">
       	<c:set var="required" value="required:true,"/>
       	</c:if>
       	<c:if test="${field.fieldLength > 0}">
       		<c:set var="maxlength">maxlength:${field.fieldLength},</c:set>
       	</c:if>
       	<c:if test="${fn:length(field.formFieldType) > 0}">
       		<c:set var="formFieldType">${field.formFieldType}:true,</c:set>
       	</c:if>
       	<c:set var="validateJson" value="${required}${maxlength }${formFieldType }" />
       	<c:if test="${fn:length(validateJson) > 0}">
       		<c:set var="validateJson" value="${fn:substring(validateJson, 0, (fn:length(validateJson) - 1))}" />
       	</c:if>
             <tr>
             	<c:choose>
       			      <c:when test="${field.fieldType == 'text' or field.fieldType == 'password'
       			       or field.fieldType == 'date' or field.fieldType == 'number'}">
       			      		<td width="21%" align="right">${field.fieldDesc}：</td>
          					<td width="79%">
          						<input type="${field.fieldType == 'password'?'password':'text'}" 
          							name="${field.fieldName}" 
          							class="i_Gr_1_ipt2 i_iB {${validateJson }}"
          							<c:if test="${field.fieldType == 'date'}">  onClick="WdatePicker();" </c:if>
          						/>
          						<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
          					</td>
       			      </c:when>
                      <c:when test="${field.fieldType == 'select'}">
       			      		<td width="21%" align="right">${field.fieldDesc}：</td>
          					<td width="79%">
          						<select name="${field.fieldName}" class="i_iB {${validateJson }}">
          						<c:forEach items="${field.enmList}" var="enm">
            							<option value="${enm}" 
            							<c:if test="${field.defaultValue==enm}"> selected="selected" </c:if>
            							>${enm}</option>
            					</c:forEach>		
          						</select>
          						<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
          					</td>
       			      </c:when>
       			      <c:when test="${field.fieldType == 'checkbox'}">
       			      		<td width="21%" align="right">${field.fieldDesc}：</td>
          					<td width="79%">
          						<span>
          						<c:forEach items="${field.enmList}" var="enm">
            							<input type="checkbox" class="{${validateJson }}" name="${field.fieldName}_cbox" value="${enm}" <c:if test="${field.defaultValue==enm}">checked="checked"</c:if>/>${enm}
            					</c:forEach>
            					</span>		
          						<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
          						<input id="${field.fieldName}" name="${field.fieldName}" type="hidden" />
          						<c:set var="cboxName" value="${field.fieldName},${cboxName}" />
          					</td>
       			      </c:when>
       			      <c:when test="${field.fieldType == 'radio'}">
       			      		<td width="21%" align="right">${field.fieldDesc}：</td>
          					<td width="79%">
          						<span>
          						<c:forEach items="${field.enmList}" var="enm">
            							<input type="radio" class="{${validateJson }}" name="${field.fieldName}" value="${enm}" <c:if test="${field.defaultValue==enm}">checked="checked"</c:if>/>${enm}
            					</c:forEach>
            					</span>		
          						<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
          					</td>
       			      </c:when>
       			      <c:when test="${field.fieldType == 'editor' || field.fieldType == 'smeditor' || field.fieldType == 'textarea'}">
          					 <td width="21%" align="right">${field.fieldDesc}：</td>
					         <td width="79%">
					         <textarea name="${field.fieldName}" cols="" rows="" 
					         class="i_Gr_1_ipt4 {${validateJson }}" 
					         ></textarea>
					         <c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
					         </td>
       			      </c:when>
				</c:choose>
        	</tr>
        	<c:set var="required" value="" />
     		<c:set var="maxlength" value="" />
     		<c:set var="formFieldType" value="" />
     		<c:set var="validateJson" value="" />
         </c:forEach>
     	<tr>
        	 <td width="21%" align="right">验证码：</td>
             <td width="79%">
             <input type="text" id="checkCode" name="checkCode"/>
             <img name="captchaImg" id="captchaImg" src="<%=basePath%>appadmin/image.jsp?randName=smt_${appId}&"+Math.random()
              width="55" height="20" onclick="loadimage();" title="点击刷新验证码"/>
             </td>
        </tr>
        <tr>
          <td width="21%" align="right"></td>
          <td width="79%">
          <input type="hidden" name="groupId" value="${groupId}"/>
          <input type="hidden" name="appId" value="${appId}"/>
          <input type="hidden" name="isNeedTheme" value="1"/>
          <input type="hidden" name="themeId" value="${dataId}"/>
          <input type="submit" class="i_Gr_1_btn1" value="发送"/>&nbsp;<input type="reset" class="i_Gr_1_ipt6" value="重 写"/>
          </td>
        </tr>  
    </table>
    </form>
    </c:when>
    <c:otherwise>
    <!-- 
       <div style="text-align:center">抱歉，主题没有要提交的字段。</div>
    -->
    </c:otherwise>
    </c:choose>
    </div>
  <div class="clear"></div>
 </div>
<!--star footer-->
<jsp:include page="../include/webfooter.jsp"  /> 
<!--end footer-->
</body>
<script type="text/javascript">
	function loadimage(){
		document.getElementById("captchaImg").src = "<%=basePath%>appadmin/image.jsp?randName=loginCaptcha&"+Math.random();
	}
	var _cboxName= '${cboxName}'; //checkbox类型的字段名 
	//提交时设置checkbox类型的值
	function setCboxValue(){
	    if(_cboxName){
			var _cboxArry = _cboxName.split(',');
			for(var i=0;i<_cboxArry.length;i++){
			    if(_cboxArry[i]=='')
			         continue;
			    var chk_value = new Array();
			    var _value = $('input[name="'+_cboxArry[i]+'_cbox"]:checked').each(function(){    
                 		chk_value.push($(this).val());    
                   });  
                $('#'+_cboxArry[i]).val(chk_value.join(","));
			}
		}
		return true;
	}
</script>
</html>
