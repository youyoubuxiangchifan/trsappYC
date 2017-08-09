<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <form id="pagerForm" method="post" action="${basePath}post.do?method=commentList">
  	<input type="hidden" name="appId" value="${appId}">
  	<input type="hidden" name="dataId" value="${dataId}">
	<input type="hidden" name="commentStatus" value="${commentStatus}">
	<input type="hidden" name="deleted" value="${deleted}">
	<input type="hidden" name="selectFiled" value="${selectFiled}" />
	<input type="hidden" name="sFiledValue" value="${sFiledValue}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
		
	<input type="hidden" name="pageNum" value="${page.startPage}" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<c:if test="${commentStatus == '0'}"><li><a class="add" href="<%=basePath%>post.do?method=goAddComment&appId=${appId}&dataId=${dataId }"  target="dialog" mask="true"><span>添加</span></a></li></c:if>
			<li><a class="icon" href="<%=basePath%>post.do?method=commentList&appId=${appId }&dataId=${dataId }&commentStatus=0" target="navTab"><span>待审核</span></a></li>
			<li><a class="icon" href="<%=basePath%>post.do?method=commentList&appId=${appId }&dataId=${dataId }&commentStatus=1" target="navTab"><span>已审核</span></a></li>
			<li class="line">line</li>
			<c:if test="${commentStatus == '0'}"><li><a class="delete" title="您要删除选中的数据吗？" target="selectedTodo" rel="commentIds" postType="string" href="<%=basePath%>post.do?method=delComments&commentStatus=2" ><span>删除</span></a></li></c:if>
			<c:if test="${commentStatus == '2'}"><li><a class="edit" title="您要恢复选中的数据吗？" target="selectedTodo" rel="commentIds" postType="string" href="<%=basePath%>post.do?method=delComments&commentStatus=0" ><span>还原</span></a></li></c:if>
			<c:if test="${commentStatus == '0'}"><li><a class="icon" href="<%=basePath%>post.do?method=commentList&appId=${appId}&dataId=${dataId }&commentStatus=2" target="navTab"><span>垃圾箱</span></a></li></c:if>
			<li><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId}" target="navTab"><span>返回主题列表</span></a></li>		
		</ul>
	</div>	
	<table class="table" width="100%" layoutH="73">
		<thead>
			<tr>
				<th width="60"><input type="checkbox" name="selectAll" class="checkboxCtrl" group="commentIds" value="0" class="checkboxCtrl"/>全选</th>				
				<th width="100">提交人</th>
				<th width="100">状态</th>
				<th width="100">创建时间</th>
				<c:if test="${commentStatus == '0'}">				
				<th width="60">操作</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.dataList}" var="comment" >
			<tr target="sid_user" rel="1">
				<td><input type="checkbox" name="commentIds" value="${comment.commentId}"/></td>				
				<td>${comment.commentUser }</td>
				<td><c:if test="${comment.commentStatus eq 1}">已审核</c:if><c:if test="${comment.commentStatus eq 0}">待审核</c:if></td>
				<td><fmt:formatDate value="${comment.crtime }" pattern="yyyy-MM-dd"/></td>
				<c:if test="${commentStatus == '0'}">
				<td><a href="###" target="dialog" ><a href="<%=basePath%>post.do?method=getCommentInfo&appId=${appId}&dataId=${dataId }&commentIds=${comment.commentId}" target="dialog"  mask="true" close="closedialog" param="{appId:'${appId}',mesid:'${comment.commentId }'}">审核</a></td>						
				</c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>	
		<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="30" <c:if test="${page.pageSize == '30'}">selected="selected"</c:if>>30</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>
		</div>	
	</div>	
	<script type="text/javascript">
	var statusUlock = 0;
		function closedialog(obj){
			 $.get("<%=basePath%>appMgr.do?method=uLockObject", {Action:"get",appId:obj.appId,mesid:obj.mesid}, function (data, textStatus){
					this; 
					//alert(data);
				});
			return true;
		}
		function errorULock(){
			  $.get("<%=basePath%>appMgr.do?method=ULockErrorObject", {Action:"get",appId:1}, function (data, textStatus){
					this; 
					//alert(data);
				});
		}
		
   		//window.onbeforeunload = function (e) {
   		window.onunload = function (e) {
			 errorULock();
		};
</script>
