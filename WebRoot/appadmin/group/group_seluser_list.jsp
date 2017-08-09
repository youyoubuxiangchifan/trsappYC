<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <form id="pagerForm" method="post" action="${basePath}group.do?method=addGroupUserList">
  <input type="hidden" name="groupIds" value="${groupIds}">
	<input type="hidden" name="userStatus" value="${userStatus}">	
	<input type="hidden" name="selectFiled" value="${selectFiled}" />
	<input type="hidden" name="sFiledValue" value="${sFiledValue}" />		
	<input type="hidden" name="pageNum" value="${page.startPage}" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>
<div class="pageHeader">
  			<form  class="pageForm required-validate" method="post" onsubmit="return dialogSearch(this);" action="${basePath}group.do?method=addGroupUserList">
   			<div class="searchBar">
   			<input type="hidden" name="groupIds" value="${groupIds}">
   			<input type="hidden" name="userStatus" value="${userStatus}">
			<INPUT type="text" name="sFiledValue" value="${sFiledValue}" class="required" minlength="3" maxlength="20" />&nbsp;&nbsp;
			<select name="selectFiled">   						
   						<option value="username" <c:if test="${selectFiled == 'username'}">selected="selected"</c:if>>用户名</option>
   						<option value="truename" <c:if test="${selectFiled == 'truename'}">selected="selected"</c:if>>真实姓名</option>   						
   			</select>
			&nbsp;&nbsp;
			<button type="submit" style="padding-top:3px;padding-left:3px;padding-right:3px;">检索</button>
			</div>
			</form>
</div>
<div class="pageContent">
	
	<table class="table" width="100%" layoutH="123">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" id="all" class="checkboxCtrl" value="0" onclick="selAll();"/>全选</th>
				<th width="120" >用户名</th>
				<th width="100">真实姓名</th>				
				<th width="80" align="center">创建时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.groupUserList}" var="appUser">
			<tr target="sid_user" rel="1">
				<td><input id="u_${appUser.userId}" type="checkbox" name="userID" value="${appUser.userId}" onclick="saveSelIds(this.value)"/></td>
				<td>${appUser.username }</td>
				<td>${appUser.truename }</td>				
				<td><fmt:formatDate value="${appUser.crtime }" pattern="yyyy-MM-dd"/></td>				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="dialogPageBreak({numPerPage:this.value})">
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="30" <c:if test="${page.pageSize == '30'}">selected="selected"</c:if>>30</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="dialog" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>
		</div>
		<div class="formBar">
			<ul>
				<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="addUser('${requestScope.groupIds }')">添加</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>				
	</div>
		<!--  添加角色用户的form -->
	<form name="userForm"  method="post" action="group.do?method=addGroupUser" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
  		<input id="groupIds" type="hidden" name="groupIds" value="${requestScope.groupIds}"/>
  		<input id="userIds" type="hidden" name="userIds" value=""/>
  	</form>	
   <script type="text/javascript">
  
    function initCbox(){
        if(_selUsers.length>0){
            var _id = "";
        	$('input[name=userID]').each(function(){		
				_id=$(this).attr('value');	
				for(i=0;i<_selUsers.length;i++){
					if(_selUsers[i]==_id){
						$(this).attr('checked','checked');
					}
				}	
 			});
        }
    }
    
    function selAll(){
        var _cheched = $('#all').attr("checked") 
 		if(_cheched=='checked'){//选中后向数组里添加id
 			removeAll();
    		$('input[name=userID]').each(function(){	
    			$(this).attr('checked',true);
    			var _id=$(this).attr('value');	
    			_selUsers.push(_id);
    		});
    	}else{
    		removeAll();
    	}
    }
    function removeAll(){
    	$('input[name=userID]').each(function(){	
    		$(this).attr('checked',false);
    		var _id=$(this).attr('value');	
    		for(i=0;i<_selUsers.length;i++){
    			if(_selUsers[i]==_id){
						remove(_id);
			     }
    			
    		}
    	});
    }
 	//获取已选择的用户id
 	function getSelIDs(){
 		var _selIds="";
 		$('input[name=userID]:checked').each(function(){		
			_selIds=_selIds+$(this).attr('value')+",";		
 		});
 		if(_selIds.length>0){
 			_selIds = _selIds.substring(0,_selIds.length-1);
 		}
 		return _selIds;
 	}
 	function saveSelIds(id){
 		var _id = '#u_'+id;
 		var _cheched = $(_id).attr("checked"); 
 		if(_cheched=='checked'){//选中后向数组里添加id
 			_selUsers.push(id);
 		}else{
 			remove(id);
 		}
 	}
 	//从数组中移除用户id
 	function remove(id){
 		for(i=0;i<_selUsers.length;i++){
 			if(_selUsers[i]==id)
 				_selUsers.splice(i,1);
 		}
 	}
 	
 	//计算要保存的id
    function addUser(roleID){
    	var _selIds= _selUsers.toString();
 		if(_selIds==""){
 			alertMsg.info('请选择要添加的用户！');
 			return;
 		}
 		$("#userIds").val(_selIds);
 		$("[name=userForm]").submit();
    }
    //关闭对话框
    function closeDialog(){
    	$.pdialog.closeCurrent(); 
    }
   
   	function initSelUser(uIds){
   	  if(uIds==undefined || uIds==""){
    		return;
    	}
    	_selUsers = uIds.split(',');
   	}
   initSelUser('${GrpUserSelIds}');
   initCbox();//初始化已选择的用户
 	
  </script>				
