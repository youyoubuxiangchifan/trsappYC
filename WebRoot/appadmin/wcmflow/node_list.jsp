<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form id="pagerForm" method="post" action="flow.do?method=listNode&id=${flowId}">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="flow.do?method=node_edite&flowId=${flowId}" target="dialog" width="600" height="550" ><span>新增节点</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th width="50">序号</th>
				<th width="100">节点名称</th>
				<th>节点描述</th>
				<th width="150">创建时间</th>
				<th width="100" >创建人</th>
				<th width="100">修改</th>
				<th width="100">删除</th>
				<th width="100">调整顺序</th>
			</tr>
		</thead>
		<tbody>
		   <c:set var="index" value="0"></c:set>
		   <c:forEach items="${requestScope.nodeList}" var="node" varStatus="num">
			<tr target="sid" rel="${node.nodeId}">
				<td>${num.index+1}</td>
				<td>${node.nodeName}</td>
				<td>${node.nodeDesc}</td>
				<td><fmt:formatDate value="${node.crtime}" pattern="yyyy-MM-dd"/></td>
				<td>${node.cruser}</td>
				<td><a  href="flow.do?method=node_edite&id=${node.nodeId}&flowId=${flowId}" target="dialog" title="修改工作流节点"
				width="600" height="550" mask="true" class="btnEdit"><span>修改</span></a></td>
				<td>
					<c:choose>
				   <c:when test="${num.index==0 or num.index==page.totalResults-1}">
				   		<a id='c_${node.nodeId}' title="禁用结束,开始节点" href="javascript:void(0);" disabled="disabled"  class="btnDel">删除节点</a>
				   </c:when>
				   <c:otherwise>
				   		<a id='d_${node.nodeId}' title="你确定要删除记录吗?" target="ajaxTodo"  mask="true" href="flow.do?method=delNode&id=${node.nodeId}&flowId=${flowId}" class="btnDel">删除</a>
				   </c:otherwise>
				</c:choose>
					
				</td>
				<td>
				<c:choose>
				   <c:when test="${num.index==0 or num.index==page.totalResults-1}">
				   		<a id='c_${node.nodeId}' title="调整节点顺序" href="javascript:void(0);" disabled="disabled">调整顺序</a>
				   </c:when>
				   <c:otherwise>
				   		<a id='c_${node.nodeId}' title="调整节点顺序" target="dialog" mask="true" width="450" height="200" href="flow.do?method=order_edite&id=${node.nodeId}&flowId=${flowId}">调整顺序</a>
				   </c:otherwise>
				</c:choose>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="3" <c:if test="${page.pageSize == '3'}">selected="selected"</c:if>>3</option>
				<option value="15" <c:if test="${page.pageSize == '15'}">selected="selected"</c:if>>15</option>
				<option value="20" <c:if test="${page.pageSize == '20'}">selected="selected"</c:if>>20</option>
				<option value="50" <c:if test="${page.pageSize == '50'}">selected="selected"</c:if>>50</option>
				<option value="100" <c:if test="${page.pageSize == '100'}">selected="selected"</c:if>>100</option>
			</select>
			<span>条，共${page.totalResults}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalResults}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.startPage}"></div>

	</div>

</div>
<script type="text/javascript">
	//禁用a标签
	function disTabA(id){
		var _href=$(id).attr('href');
                $(id).css("cursor", "default");
                $(id).attr('href', '#');     //修改<a>的 href属性值为 #  这样状态栏不会显示链接地址  
                $(id).click(function (event) {
                    event.preventDefault();   // 如果<a>定义了 target="_blank“ 需要这句来阻止打开新页面
                });
	}
	//禁止开始、结束的删除和顺序调整
	function disStartAndEnd(sid,eid){
		
		var _dsid = '#d_'+ sid;
		var _deid = '#d_'+ eid;
		var _csid = '#c_'+ sid;
		var _ceid = '#c_'+ eid;
		disTabA(_dsid);
		disTabA(_deid);
		disTabA(_csid);
		disTabA(_ceid);
    }
    //disStartAndEnd('${startId}','${endId}');
</script>