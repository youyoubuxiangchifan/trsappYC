<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form id="pagerForm" onsubmit="return divSearch(this, 'jbsxBox');" action="role.do?method=deptUser" method="post">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<input type="hidden" name="roleID" value="${roleID}" />
	<input type="hidden" name="deptID" value="${deptID}" />
	<input type="hidden" name="act" value="1" />
	<div class="searchBar">
					<INPUT type="text" name="sFiledValue" value="${sFiledValue}"/>&nbsp;&nbsp;
			<select name="selectFiled">   						
   						<option value="username" <c:if test="${selectFiled == 'username'}">selected="selected"</c:if>>用户名</option>
   						<option value="truename" <c:if test="${selectFiled == 'truename'}">selected="selected"</c:if>>真实姓名</option>
   						<option value="moblie" <c:if test="${selectFiled == 'moblie'}">selected="selected"</c:if>>电话</option>
   			</select>
			&nbsp;&nbsp;
			<button type="submit">检索</button>
	</div>
	</form>
</div>

<div class="pageContent" style="border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid;">

	<table class="table" width="99%" layoutH="170" rel="jbsxBox">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" id="all" class="checkboxCtrl" value="0" onclick="selAll();"/>全选</th>
				<th width="120" orderField="number" class="asc">用户名</th>
				<th>真实姓名</th>
				<th width="150">当前状态</th>
				<th width="150">注册时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${requestScope.users}" var="user">
			<tr>
				<td><input id="u_${user.userId}" type="checkbox" name="userID" value="${user.userId }" onclick="saveSelIds(this.value)"/></td>
				<td>${user.username}</td>
				<td>${user.truename}</td>
				<td>
					<c:choose>
       			      <c:when test="${user.status==0}">已开通</c:when>
                      <c:when test="${user.status==1}">已停用</c:when>
					  <c:when test="${user.status==2}">已删除</c:when>
                      <c:otherwise>其他</c:otherwise>
				   </c:choose>
				</td>
				<td><fmt:formatDate value="${user.crtime }" pattern="yyyy-MM-dd"/></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="dialogPageBreak({numPerPage:this.value}, 'jbsxBox')">
				<option value="15">15</option>
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="dialog"  rel="jbsxBox" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>
       
	</div>
	
	 <div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent"><button type="button" onclick="addUser('${requestScope.roleID }')">添加</button></div>
					</div>
				</li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close" onclick="closeDialog();">取消</button></div></div>
				</li>
			</ul>
	</div>
	<!--  添加角色用户的form -->
	<form name="userForm"  method="post" action="role.do?method=addUser" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
  		<input id="roleID" type="hidden" name="roleID" value="${requestScope.roleID}"/>
  		<input id="userIDs" type="hidden" name="userIDs" value=""/>
  	</form>
</div>

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
 		$("#userIDs").val(_selIds);
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
   initSelUser('${selID}');
   initCbox();//初始化已选择的用户
 	
  </script>