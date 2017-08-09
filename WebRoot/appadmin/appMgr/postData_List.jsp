<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <form id="pagerForm" method="post" action="${basePath}post.do?method=appPostList">
  	<input type="hidden" name="appId" value="${appId}">
	<input type="hidden" name="isPublic" value="${isPublic}">
	<input type="hidden" name="deleted" value="${deleted}">
	<input type="hidden" name="selectFiled" value="${selectFiled}" />
	<input type="hidden" name="sFiledValue" value="${sFiledValue}" />
	<input type="hidden" name="orderField" value="${orderField}" />
	<input type="hidden" name="themeId" value="${themeId}" />
	<input type="hidden" name="orderDirection" value="${orderDirection}" />
		
	<input type="hidden" name="pageNum" value="${page.startPage}" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>
<div class="pageHeader">
  			<form  class="pageForm required-validate" method="post" onsubmit="return navTabSearch(this);" action="${basePath}post.do?method=appPostList">
   			<div class="searchBar">
   			<input type="hidden" name="appId" value="${appId}">
   			<input type="hidden" name="status" value="${status}">
			<INPUT type="text" name="sFiledValue" value="${sFiledValue}" class="required" minlength="3" maxlength="20" />&nbsp;&nbsp;
			<select name="selectFiled">  
				<c:forEach items="${requestScope.searchFieldList}" var="searchField" > 						
   						<option value="${searchField.fieldName}" <c:if test="${selectFiled == searchField.fieldName}">selected="selected"</c:if>>${searchField.fieldDesc}</option>
   				</c:forEach>

   			</select>
			&nbsp;&nbsp;
			<button type="submit" style="padding-top:3px;padding-left:3px;padding-right:3px;">检索</button>
			</div>
			</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<c:if test="${deleted == '0'}"><li><a class="add" href="<%=basePath%>post.do?method=getMetaDataToAddEdit&appId=${appId}&themeId=${requestScope.themeId}" max="true" target="dialog" title="添加"><span>添加</span></a></li></c:if>
			<li><a class="icon" href="<%=basePath%>post.do?method=appPostList&appId=${appId }&themeId=${themeId}&isPublic=0" target="navTab"><span>待审核</span></a></li>
			<li><a class="icon" href="<%=basePath%>post.do?method=appPostList&appId=${appId }&themeId=${themeId}&isPublic=1" target="navTab"><span>已审核</span></a></li>
			<li class="line">line</li>
			<c:if test="${deleted == '0'}"><li><a class="delete" title="您要删除选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>post.do?method=delPosts&tableName=${tableName}&flag=1" ><span>删除</span></a></li></c:if>
			<c:if test="${deleted == '1'}"><li><a class="edit" title="您要恢复选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>post.do?method=delPosts&tableName=${tableName}&flag=0" ><span>还原</span></a></li></c:if>
			<c:if test="${deleted == '0'}"><li><a class="icon" href="<%=basePath%>post.do?method=appPostList&appId=${appId}&themeId=${themeId}&deleted=1" target="navTab"><span>垃圾箱</span></a></li></c:if>
			<li><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId}" target="navTab"><span>返回主题列表</span></a></li>		
		</ul>
	</div>
	
	<table class="table" width="100%" layoutH="113">
		<thead>
			<tr>
				<th width="60"><input type="checkbox" name="selectAll" class="checkboxCtrl" group="dataIds" value="0" class="checkboxCtrl"/>全选</th>
				<c:forEach items="${requestScope.fieldList}" var="field">
				<th width="100">${field.fieldDesc}</th>
				</c:forEach>
				<th width="60">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${requestScope.dataList}" var="dataMap" >
			<tr target="sid_user" rel="1">
				<td><input type="checkbox" name="dataIds" value="${dataMap.METADATAID}"/></td>
					<c:forEach items="${requestScope.fieldList}" var="field">
						<c:forEach items="${dataMap}" var="valueMap" >
							<c:if test="${field.fieldName eq valueMap.key}">
								<!-- 判断是否标题字段 -->
								<c:choose>
									<c:when test="${field.titleField eq 1}"><!-- 标题字段 -->
									<td><a href="<%=basePath%>post.do?method=getMetaDataToAddEdit&appId=${appId}&metadataid=${dataMap.METADATAID}&themeId=${requestScope.themeId}" target="dialog"  max="true"  close="closedialog" param="{appId:'${appId}',mesid:'${dataMap.METADATAID }'}">${valueMap.value }</a></td>
									</c:when>
									<c:when test="${field.fieldName == 'ISPUBLIC'}"><!-- 标题字段 -->
									<td><c:if test="${valueMap.value eq 1}">是</c:if><c:if test="${valueMap.value eq 0}">否</c:if></td>
									</c:when>
									<c:otherwise><!-- 非标题 -->
										<td>${valueMap.value }</td>
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:forEach>
				</c:forEach>

				<td><a href="<%=basePath%>post.do?method=getMetaDataToAddEdit&appId=${appId}&metadataid=${dataMap.METADATAID}&themeId=${requestScope.themeId}" target="dialog"  max="true"  close="closedialog" param="{appId:'${appId}',mesid:'${dataMap.METADATAID }'}">审核</a></td>

						
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
			 $.get("<%=basePath%>appMgr.do?method=uLockObject", {Action:"get",appId:obj.appId,mesid:obj.mesid,tableType:1,timeStamp:new Date().getTime()}, function (data, textStatus){
					this; 
					//alert(data);
				});
			return true;
		}
		function errorULock(){
			  $.get("<%=basePath%>appMgr.do?method=ULockErrorObject", {Action:"get",appId:1,timeStamp:new Date().getTime()}, function (data, textStatus){
					this; 
					//alert(data);
				});
		}
		
   		//window.onbeforeunload = function (e) {
   		window.onunload = function (e) {
			 errorULock();
		};
</script>
