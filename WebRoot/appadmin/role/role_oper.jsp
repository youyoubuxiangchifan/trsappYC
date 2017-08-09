<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent" layoutH="30">
	 <form id="operForm"  method="post" action="role.do?method=addOper" class="pageForm required-validate"
  	   	onsubmit="return validateCallback(this,dialogAjaxDone)" >
    	<input type="hidden" id="roleID" name="roleID" value="${requestScope.roleID}"/>
    	<input type="hidden" name="addIDs" value=""/>
    	<input type="hidden" name="appID" value=""/>
    </form>
    <div class="panelBar" style="height:30px;">
		<ul class="toolBar" >
			<p>
				<label>所属应用：</label>
				<select id="appID" name="appID" style="width:155px;margin-top:2px;" onchange="getOpers('${roleID}')">
    					<option value="">--请选择--</option>
    					<c:forEach items="${requestScope.appList}" var="app">
    						<option value="${app.appId}">${app.appName}</option>
    					</c:forEach>
    			</select>
			</p>
		</ul>
	</div>
	<table class="table" width="100%" height="100%">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" name="selectAll" value="0" class="checkboxCtrl" group="operIDs"/>全选</th>
				<th width="100">操作名称</th>
				<th>操作说明</th>
				<th width="100">创建时间</th>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${requestScope.operList}" var="oper">
    			<tr>
    				<td>
    					<input type="checkbox" name="operIDs" value="${oper.operFlag }"/>
    				</td>
    				<td>${oper.opname }</td>
    				<td>${oper.opdesc }</td>
    				<td>${oper.crtime }</td>
    			</tr>
    		</c:forEach>
			
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
   	var _appID = "";
    //默认选择已选的操作
 	function selectOper(oldOpers){
 	   if(oldOpers==undefined || oldOpers=="")
 	   		return;
 		 	var arrOpers= new Array();   
        	arrOpers=oldOpers.split(",");      
    	 $('input[name=operIDs]').each(function(){		
    	 	$(this).attr('checked',false);
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
 		$('input[name=operIDs]:checked').each(function(){		
			_selIds=_selIds+$(this).attr('value')+",";		
 		});
 		if(_selIds.length>0){
 			_selIds = _selIds.substring(0,_selIds.length-1);
 		}
 		return _selIds;
 	}
 	//计算要保存的id
    function saveOper(){
        _appID = $("#appID").val();
    	if(_appID==undefined || _appID==""){
    		alertMsg.info('请选择应用。');
    		return;
    	}
        var _selID = getSelIDs();
        if(_selID==undefined){
    		_selID==""
    	}
        $('input[name=appID]').val(_appID);
    	$('input[name=addIDs]').val(_selID);
    	$('#operForm').submit();
    }
    
    //获取应用的相关操作id,并设置复选框的值
    function getOpers(roleID){
    	_appID = $("#appID").val();
		$("input:checkbox").attr("checked", false);//所有的单选框选择全部清空
    	if(_appID==""){	  //应用为"" 反选所有的操作
    	    $('input[name=operIDs]').each(function(){		
     	   	$(this).attr('checked',false);
 		});
    	}else{ //获取相关操作并选择
    	    var data = {
    	    	roleID:roleID,
    	    	appID:_appID
    	    }
    		$.post("role.do?method=findAppOpers",data,function(result){
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