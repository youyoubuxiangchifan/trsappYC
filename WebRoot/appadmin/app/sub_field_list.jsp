<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="pageContent" layoutH="30">
	 <form id="operForm"  method="post" action="<%=basePath%>appView.do?method=setGrp_filed&viewId=${viewId }" class="pageForm required-validate"
  	   	onsubmit="return validateCallback(this,dialogAjaxDone)" >
    	<input type="hidden" id="viewId" name="roleID" value="${requestScope.viewId}"/>
    	<input type="hidden" name="ngpids" value=""/>
    	<input type="hidden" name="grpName" value=""/>
    </form>
    <div class="panelBar" style="height:30px;">
		<ul class="toolBar" >
			<p>
				<label>分组字段：</label>
				<c:forEach items="${requestScope.isGrpFields}" var="isGrpFields" varStatus="i">
					<select id="ngrpName" name="ngrpName"  style="width:155px;margin-top:2px;" onchange="getOpers('${requestScope.viewId}')">
						<option value="">请选择</option>
						<c:forEach items="${fn:split(isGrpFields.enmValue,'~')}" var="enmValue" varStatus="i">
						 	<option value="${enmValue}">${enmValue}</option>
						</c:forEach>
					</select>
				</c:forEach>
			</p>
		</ul>
	</div>
	<table class="table" width="100%" height="100%">
		<thead>
			<tr>
				<th width="60"><input type="checkbox"  class="checkboxCtrl" group="gpids" value="0" />全选</th>
				<th width="50">序号</th>
				<th width="150" >字段名</th>
				<th>字段显示名称</th>
			</tr>
		</thead>
		<tbody>
  			<c:choose>
				<c:when test="${requestScope.isGrpFields[0] eq null}">
					<tr><td colspan="4" align="center">抱歉!该应用没有设置分组字段!</td></tr>
				</c:when>
				<c:otherwise> 
					<c:set var="index" value="0"></c:set>
				  	<c:forEach items="${requestScope.subfieldList}" var="field" varStatus="num">
					<tr >
						<td><input type="checkbox" name="gpids" value="${field.fieldId}"/> </td>
						<td>${num.index+1}</td>
						<td>${field.fieldName}</td>
						<td>${field.fieldDesc}</td>
					</tr>
				</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	
</div>
<div class="formBar" layoutH="0">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent"><button type="button" onclick="saveOper('')">添加</button></div>
					</div>
				</li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
</div>

   <script type="text/javascript">
   	var _oldIDs = "";
   	var grpName = "";
    //默认选择已选的操作
 	function selectOper(oldOpers){
 	   if(oldOpers==undefined || oldOpers=="")
 	   		return;
 		 	var arrOpers= new Array();   
        	arrOpers=oldOpers.split(",");      
    	 $('input[name=gpids]').each(function(){
    	 	$(this).attr('checked',false);
     	     for (i=0;i<arrOpers.length ;i++ ){   
	           if($(this).val()==arrOpers[i]){
	           		$(this).attr('checked',true);
	           }
     		}   
 		});
 	}
 	//获取已选择的操作id
 	function getSelIDs(){
 		var _selIds="";
 		$('input[name=gpids]:checked').each(function(){		
			_selIds=_selIds+$(this).val()+",";		
 		});
 		if(_selIds.length>0){
 			_selIds = _selIds.substring(0,_selIds.length-1);
 		}
 		return _selIds;
 	}
 	//计算要保存的id
    function saveOper(){
    	grpName = $("#ngrpName").val();
    	if(grpName==undefined || grpName==""){
    		alertMsg.info('请选择分组字段。');
    		return;
    	}
        var _selID = getSelIDs();
        if(_selID==undefined){
    		_selID==""
    	}
        $('input[name=grpName]').val(grpName);
    	$('input[name=ngpids]').val(_selID);
    	$('#operForm').submit();
    }
    
    //获取应用的相关操作id,并设置复选框的值
    function getOpers(viewId){
    	grpName = $("#ngrpName").val();
		$("input:checkbox").attr("checked", false);//所有的单选框选择全部清空
    	if(grpName==""){	  //应用为"" 反选所有的操作
    	    $('input[name=gpids]').each(function(){		
     	   	$(this).attr('checked',false);
 		});
    	}else{ //获取相关操作并选择
    	    var data = {
    	    	grpName:grpName,
    	    	viewId:viewId
    	    }
    		$.post("appView.do?method=ajaxgrpfield&random="+Math.random(),data,function(result){
    					if(result=='error'){
    						alertMsg.info('查询操作失败。');
    					}else{
    					    _oldIDs = result;
    						selectOper(_oldIDs);
    					}
  			});
    	}
    	
    }
 	//selectOper('${requestScope.operIDs}');//选中已选的操作
 	
  </script>