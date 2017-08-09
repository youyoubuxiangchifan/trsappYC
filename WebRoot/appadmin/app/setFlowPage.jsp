<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pageContent">
	<form id="setFlowForm" name="flowForm"  method="post" action="appView.do?method=addFlow" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,myDialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56" style="margin:0 auto">
		    <c:forEach items="${requestScope.flowList}" var="flow" varStatus="flowIndex">
		         <c:if test="${flowIndex.index %3== 0}"> <p></c:if>
		         	<input type="radio" name="flowId" value="${flow.flowId}" <c:if test="${flow.flowId==flowId}">checked</c:if>/>${flow.flowName}
		         <c:if test="${flowIndex.index %3== 2 or flowIndex.last}"> </p></c:if>
		    </c:forEach>
			<input type="hidden" name="appId" value="${appId}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="unChecked()">取消选择</button></div></div></li>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="stmapp()">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
	
</div>
 <script type="text/javascript">
       
		function stmapp(){
		    /*var _flowId = $('#setFlowForm input[name=flowId]:checked').val();
		    if(!_flowId){
		    	alertMsg.info('请为应用选择工作流?');
		           return;
		    }*/
		    $('form[name=flowForm]').submit();
		    	return;
		}
		//取消选择
		function unChecked(){
			$('#setFlowForm input[name=flowId]:checked').attr('checked', false);
		}
 </script>

