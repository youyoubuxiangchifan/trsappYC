<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageContent"  layoutH="75">
	 <form id="appForm"  method="post" action="role.do?method=toAppAdminUser" class="pageForm required-validate"
  	   	onsubmit="return validateCallback(this,dialogAjaxDone)" >
    		<input type="hidden" id="roleID" name="roleID" value="${requestScope.roleID}"/>
    		<input type="hidden" id="userID" name="userID" value="${requestScope.userID}"/>
    		<input type="hidden" name="addIDs" value=""/>
    </form>
	<table class="table" width="100%" height="100%">
		<thead>
			<tr>
				<th width="60"><input type="checkbox" name="selectAll" value="0" class="checkboxCtrl" group="appIDs"/>全选</th>
				<th>应用名称</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${requestScope.appList}" var="app">
    			<tr>
    				<td>
    					<input type="checkbox" name="appIDs" value="${app.appId }"/>
    				</td>
    				<td>${app.appName }</td>
    			</tr>
    		</c:forEach>
			
		</tbody>
	</table>
	
</div>

<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent"><button type="button" onclick="saveOper()">保存</button></div>
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