<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageHeader">
  		<form  class="pageForm" id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="${basePath}appMgr.do?method=appDataList">
   			<input type="hidden" name="appId" value="${appId}">
			<input type="hidden" name="status" value="${status}">
			<input type="hidden" name="worked" value="${worked}">
			<input type="hidden" name="IsOwnerWork" value="${isOwnerWork}">
			<input type="hidden" name="themeStatus" value="${themeStatus}">
			<input type="hidden" name="deleted" value="${deleted}">
			<input type="hidden" name="orderField" value="${orderField}" />
			<input type="hidden" name="orderDirection" value="${orderDirection}" />
				
			<input type="hidden" name="pageNum" value="${page.startPage}" />
			<input type="hidden" name="numPerPage" value="${page.pageSize}" />
   			<div class="searchBar">
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
		<%@ include file="./include/app_menu_include.jsp"%> 
	</div>
	
	<table class="table" width="100%" layoutH="113">
		<thead>
			<tr>
				<th width="60"><input type="checkbox" name="selectAll" class="checkboxCtrl" group="dataIds" value="0" class="checkboxCtrl"/>全选</th>
				<c:forEach items="${requestScope.fieldList}" var="field">
				<th width="100">${field.fieldDesc}</th>
				</c:forEach>
				<c:if test="${appInfo.flowId != 0 && status != -1}">
				<th width="60">是否签收</th>
				</c:if>
				<%if(!isAdmin){ %>
					<c:if test="${appInfo.isNeedTheme eq 0}">
					<%if(opers.toString().indexOf(Global.BLINFO)!=-1){ %>
						<th width="60">操作</th>
					<%} %>
					<%if(opers.toString().indexOf(Global.COMINFO)!=-1){ %>
						<c:if test="${appInfo.isHasComment != 0}">
							<th width="60">评论</th>
						</c:if>
					<%} %>
					</c:if>
				<%}else{ %>
					<c:if test="${appInfo.isNeedTheme eq 0}">
						<th width="60">操作</th>
						<c:if test="${appInfo.isHasComment != 0}">
							<th width="60">评论</th>
						</c:if>
					</c:if>
				<%} %>
				<c:if test="${appInfo.isNeedTheme eq 1}">
				<th width="60">主题管理</th>
				<th width="60">回帖管理</th>
				</c:if>
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
									<td><a href="appMgr.do?method=getMetaDataToAddEdit&appId=${appId}&metadataid=${dataMap.METADATAID }&worked=${worked}&opt=0" target="dialog"  max="true"  close="closedialog" param="{appId:'${appId}',mesid:'${dataMap.METADATAID }'}">${valueMap.value }</a></td>
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
				<c:if test="${appInfo.flowId != 0 && status != -1}">
				<td><c:if test="${dataMap.ACCEPTED eq 0}">否</c:if><c:if test="${dataMap.ACCEPTED eq 1}">是</c:if></td>
				</c:if>
				<%if(!isAdmin){ %>
				<c:if test="${appInfo.isNeedTheme eq 0}">
					<%if(opers.toString().indexOf(Global.BLINFO)!=-1){ %>
						<td><a href="<%=basePath%>appMgr.do?method=getMetaDataToAddEdit&appId=${appId}&metadataid=${dataMap.METADATAID}&worked=${worked}&opt=${status == -1 ? 1 : 2}" target="dialog"  max="true"  close="closedialog" param="{appId:'${appId}',mesid:'${dataMap.METADATAID }'}">${status == -1 ? "修改" : "办理"}</a></td>
					<%} %>
					<%if(opers.toString().indexOf(Global.COMINFO)!=-1){ %>
						<c:if test="${appInfo.isHasComment != 0}">
							<td><a href="<%=basePath%>post.do?method=commentList&appId=${appId}&dataId=${dataMap.METADATAID}" target="navTab" title="评论管理">管理</a></td>
						</c:if>
					<%} %>
				</c:if>
				<%}else{ %>
					<c:if test="${appInfo.isNeedTheme eq 0}">
						<td><a href="<%=basePath%>appMgr.do?method=getMetaDataToAddEdit&appId=${appId}&metadataid=${dataMap.METADATAID}&worked=${worked}&opt=${status == -1 ? 1 : 2}" target="dialog"  max="true"  close="closedialog" param="{appId:'${appId}',mesid:'${dataMap.METADATAID }'}">${status == -1 ? "修改" : "办理"}</a></td>
						<c:if test="${appInfo.isHasComment != 0}">
							<td><a href="<%=basePath%>post.do?method=commentList&appId=${appId}&dataId=${dataMap.METADATAID}" target="navTab" title="评论管理">管理</a></td>
						</c:if>
					</c:if>
				<%} %>
				<c:if test="${appInfo.isNeedTheme eq 1}">
					<td><a href="<%=basePath%>appMgr.do?method=getMetaDataToAddEdit&appId=${appId}&metadataid=${dataMap.METADATAID}" target="dialog"  max="true"  close="closedialog" param="{appId:'${appId}',mesid:'${dataMap.METADATAID }'}">主题管理</a></td>
					<td><a href="<%=basePath%>post.do?method=appPostList&appId=${appId}&themeId=${dataMap.METADATAID}&tableType=1" target="navTab" title="回帖管理">回帖管理</a></td>
				</c:if>				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="2" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>2</option>
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
			 $.get("<%=basePath%>appMgr.do?method=uLockObject", {Action:"get",appId:obj.appId,mesid:obj.mesid,tableType:0,timeStamp:new Date().getTime()}, function (data, textStatus){
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
