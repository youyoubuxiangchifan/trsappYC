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
<title>TRS-互动表单提交</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/reset.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>web/images/layout.css"/>
<script language="javascript" type="text/javascript" src="<%=basePath%>web/js/My97DatePicker/WdatePicker.js"></script>
<script src="<%=basePath%>web/images/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="<%=basePath%>web/images/jquery.validate.js" type="text/javascript"></script>
<script src="<%=basePath%>web/images/jquery.validate.mesage_cn.js" type="text/javascript"></script>
<script src="<%=basePath%>web/images/jquery.metadata.js" type="text/javascript"></script>
</head>

<body>
<!-- START header --> 
<jsp:include page="../include/webheader.jsp"/> 
<!-- END header -->
<script type="text/javascript">
$(document).ready(function(){
	$("#formData").validate();
});
</script>
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
    <div class="i_Gr_6 mb10">信件提交</div>
    <c:if test="${requestScope.fieldList!=null}">
    <form name="formData" id="formData" action="appWeb.do?method=saveAppData" method="post" enctype="multipart/form-data" onsubmit="setCboxValue();">
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
       			      <c:otherwise>
          					 <td width="21%" align="right">${field.fieldDesc}：</td>
					         <td width="79%">
					         <textarea name="${field.fieldName}" cols="" rows="" 
					         class="i_Gr_1_ipt4 {${validateJson }}" 
					         ></textarea>
					         <c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
					         </td>
       			      </c:otherwise>
				</c:choose>
          		
        	</tr>
         	<c:set var="required" value="" />
       		<c:set var="maxlength" value="" />
       		<c:set var="formFieldType" value="" />
       		<c:set var="validateJson" value="" />
         </c:forEach>
                 <!-- 开启组织(部门)选择 -->
     	<c:if test="${appInfo.isSelGroup eq 1 and appInfo.itemGroupId!=null}">
         <tr>
          <td width="21%" align="right" valign="top">提交部门：</td>
          <td width="79%">
		       <c:forEach items="${fn:split(appInfo.itemGroupId,'~')}" var="appInfoGroup" varStatus="g">
		       <c:if test="${g.first}">
		        <select class="required combox" name="sub_dep">
		       </c:if>
		        <c:set var="app_grpids" value="${fn:split(appInfoGroup,':')}"></c:set>
		        <option value="${app_grpids[0]}">${app_grpids[1]}</option>
		       <c:if test="${g.last}">
		        </select>
		       </c:if>
		      </c:forEach>
          </td>
        </tr>


     </c:if>
     <c:if test="${appInfo.isSupAppendix eq 1}">
        <tr>
          <td width="21%" align="right" valign="top">相关附件：</td>
          <td width="79%" id="appendix">
          	<span id="fileBox">
	          	<input type="file"  name="file" value="浏览" class="i_Gr_1_ipt2 i_iB"/>&nbsp;
	          	<input type="button" class="i_Gr_1_btn1 i_Gr_1_ipt5" value="添加附件" onclick="addFileBox();"/>&nbsp;
	          	支持zip,rar,txt
          	</span>
          	
          </td>
        </tr>
      </c:if> 
        <tr>
        	 <td width="21%" align="right" valign="top">验证码：</td>
             <td width="79%">
             <input type="text" id="checkCode" name="checkCode"/>
             <img name="captchaImg" id="captchaImg" src="<%=basePath%>appadmin/image.jsp?randName=smt_${appId}&"+Math.random()
              width="55" height="20" onclick="loadimage();" title="点击刷新验证码"/>
             </td>
        </tr>
        <tr>
          <td width="21%" align="right"></td>
          <td width="79%">
          <input type="hidden" name="appId" value="${appId}"/>
          <input type="hidden" name="groupId" value="${groupId}"/>
          <input type="hidden" name="isNeedTheme" value="${isNeedTheme}"/>
          <input type="hidden" name="themeId" value="${themeId}"/>
          <input type="submit" class="i_Gr_1_btn1" value="发送"/>&nbsp;<input type="reset" class="i_Gr_1_ipt6" value="重 写"/>
          </td>
        </tr>  
        
    </table>
    </form>
    </c:if>
  </div>
  <div class="clear"></div>
</div>
<!-- START footer --> 
<jsp:include page="../include/webfooter.jsp"  /> 
<!-- END footer --> 
<script type="text/javascript">
	function delFileBox(n){
		//alert(obj);
		//$('#appendix').remove(obj);
		$('#fileBox' + n).remove();
	}
	function addFileBox(){
		var n = $('#appendix span').length;
		//alert(n);
		if(n < 5){
			var shtml = '<span id="fileBox'+(n+1)+'"><br/><br/><input type="file" name="file" value="浏览" class="i_Gr_1_ipt2 i_iB"/>&nbsp;&nbsp;<input type="button" class="i_Gr_1_btn1 i_Gr_1_ipt5" value="删除附件" onclick="delFileBox(' + (n+1) + ')"/>&nbsp;</span>';
			$('#fileBox').append(shtml);
		}else{
			alert('最多只能上传5个附件！');
		}
	}
	function loadimage(){
		document.getElementById("captchaImg").src = "<%=basePath%>appadmin/image.jsp?randName=smt_${appId}&"+Math.random();
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
</body>
</html>
