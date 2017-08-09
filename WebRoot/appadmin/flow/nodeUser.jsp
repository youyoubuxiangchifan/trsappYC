<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageHeader required-validate" style="border:1px #B8D0D6 solid">
	<form id="pagerForm" onsubmit="return divSearch(this, 'jbsxBox');" action="flow.do?method=nodeUser" method="post">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<input type="hidden" name="deptID" value="${deptID}" />
	<div class="searchBar">
					<INPUT type="text" name="sFiledValue" value="${sFiledValue}" />&nbsp;&nbsp;
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

	<table class="table" width="99%" layoutH="290" rel="jbsxBox">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" id="all" class="checkboxCtrl" value="0" onclick="selAll();"/>全选</th>
				<th width="100" orderField="number" class="asc">用户名</th>
				<th>真实姓名</th>
				<th width="100">当前状态</th>
				<th width="100">注册时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${requestScope.users}" var="user">
			<tr>
				<td><input id="u_${user.userId}" type="checkbox" name="userID" value="${user.userId }" onclick="saveSelIds(this.value)"/></td>
				<td id="td_${user.userId}">${user.username}</td>
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
				<option value="10">10</option>
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="dialog"  rel="jbsxBox" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="4" currentPage="${page.startPage}"></div>
       
	</div>
	
	 <div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent"><button type="button" onclick="addUser()">添加</button></div>
					</div>
				</li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close" onclick="closeDialog();">取消</button></div></div>
				</li>
			</ul>
	</div>
	
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
        var _cheched = $('#all').attr("checked") ;
 		if(_cheched=='checked'){//选中后向数组里添加id
 			removeAll();
    		$('input[name=userID]').each(function(){	
    			$(this).attr('checked',true);
    			var _id=$(this).attr('value');	
    			_selUsers.push(_id);
    			var _tid = '#td_'+_id;
 				var _uname = $(_tid).text();
 				_selUname.push(_uname);
 				addli(_id,_uname); //添加显示用户
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
 		var _tid = '#td_'+id;
 		var _cheched = $(_id).attr("checked"); 
 		var _uname = $(_tid).text();
 		if(_cheched=='checked'){//选中后向数组里添加id
 			_selUsers.push(id);
 			_selUname.push(_uname);
 			addli(id,_uname); //添加显示用户
 		}else{
 			remove(id);
 		}
 	}
 	//从数组中移除用户id
 	function remove(id){
 		for(i=0;i<_selUsers.length;i++){
 			if(_selUsers[i]==id){
 			        
 					_selUsers.splice(i,1);
 					_selUname.splice(i,1);
 					removeLi(id);
 				}
 		}
 	}
 	
 	
    //关闭对话框
    function closeDialog(){
    	$.pdialog.closeCurrent(); 
    }
   //计算要保存的id:向父窗口回传已选择的用户id
    function addUser(){
        var _userName = $("#userName").val();
    	var _selIds= _selUsers.toString();
    	var _selNames = _selUname.toString();
 		if(_selIds=="" && _userName!=""){
 			_selNames="";
 		}else if(_selIds == ""){
 			alertMsg.info('请选择要添加的用户！');
 			return;
 		}
 		$("#nuser").val(_selIds);
 		$("#userName").val(_selNames);
 		closeDialog();
    }
   	function initSelUser(){
   	  var _uIds = $("#nuser").val();
   	   var _uName = $("#userName").val();
   	  if(_uIds==undefined || _uIds==""){
    		return;
    	}
    	_selUsers = _uIds.split(',');
    	_selUname = _uName.split(',');
   	}
   	function removeLi(_id){
   	    $('#li_'+_id).remove();
   	}
   	//添加用户列表
   	function addli(_id,_name){
   	    var _lihtml = '<li id="li_'+_id+'"><span class="s_name">'+_name+
   	    '</span><span><img onclick="delName('+_id+')" src="${basePath}appadmin/images/delbtn.gif"></img></span></li>';
   	    $('#ol_name').append(_lihtml);
   	}
   	function addSelName(){
   	     for(i=0;i<_selUsers.length;i++){
 			addli(_selUsers[i],_selUname[i]);
 		}
   	}
   	function delName(_userid){
   	   remove(_userid);
   	   $('#u_'+_userid).attr('checked',false);
   	}
    function initAll(_flag){
    	if('1'==_flag){
    		initSelUser();
    		addSelName();
    	}
    	initCbox();//初始化已选择的用户
    }
    initAll('${flag}');
  </script>