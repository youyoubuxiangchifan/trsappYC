<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form id="pagerForm" onsubmit="return dialogSearch(this);" action="role.do?method=findApps" method="post">
	<!--  
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	-->
	<div class="searchBar">
		
		<INPUT type="text" name="sFiledValue" value="${sFiledValue}"/>&nbsp;&nbsp;
			<select name="selectFiled">   						
   						<option value="appName" <c:if test="${selectFiled == 'appName'}">selected="selected"</c:if>>应用名称</option>
   						<option value="cruser"  <c:if test="${selectFiled == 'cruser'}">selected="selected"</c:if>>创建用户</option>
   			</select>
			&nbsp;&nbsp;
			<input type="hidden" id="roleID" name="roleID" value="${requestScope.roleID}"/>
			<button type="submit">检索</button>
	</div>
	</form>
</div>
<div class="pageContent"  layoutH="75">
	 <form id="appForm"  method="post" action="role.do?method=addApp" class="pageForm required-validate"
  	   	onsubmit="return validateCallback(this,dialogAjaxDone)" >
    		
    		<input type="hidden" id="roleID" name="roleID" value="${requestScope.roleID}"/>
    		<input type="hidden" name="addIDs" value=""/>
    </form>
	<table class="table" width="100%" height="100%">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" name="selectAll" value="0" class="checkboxCtrl" group="appIDs"/>全选</th>
				<th>应用名称</th>
				<th width="100">创建日期</th>
				<th width="100">创建用户</th>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${requestScope.appList}" var="app">
    			<tr>
    				<td>
    					<input type="checkbox" name="appIDs" value="${app.appId }"/>
    				</td>
    				<td>${app.appName }</td>
    				<td><fmt:formatDate value="${app.crtime }" pattern="yyyy-MM-dd"/></td>
    				<td>${app.cruser }</td>
    			</tr>
    		</c:forEach>
			
		</tbody>
	</table>
	
</div>

<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent"><button type="button" onclick="saveOper()">添加</button></div>
					</div>
				</li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
</div>

   <script type="text/javascript">
    //默认选择已选的操作
 	function selectOper(oldOpers){
 	   if(oldOpers==undefined || oldOpers=="")
 	   		return;
 		 	var arrOpers= new Array();   
        	arrOpers=oldOpers.split(",");      
    	 $('input[name=appIDs]').each(function(){		
     	     for (i=0;i<arrOpers.length ;i++ )   
     	{   
           if($(this).attr('value')==arrOpers[i]){
           		$(this).attr('checked',true);
           }
     	}   
 		});
 	}
 	//获取已选择的操作id
 	function getSelIDs(){
 		var _selIds="";
 		$('input[name=appIDs]:checked').each(function(){		
			_selIds=_selIds+$(this).attr('value')+",";		
 		});
 		if(_selIds.length>0){
 			_selIds = _selIds.substring(0,_selIds.length-1);
 		}
 		return _selIds;
 	}
 	//计算要保存的id
    function saveOper(){
    	var _selID = getSelIDs();
    	$('input[name=addIDs]').val(_selID);
    	$('#appForm').submit();
    }
    
 	selectOper('${requestScope.selIDs}');//选中已选的操作
 	
  </script>