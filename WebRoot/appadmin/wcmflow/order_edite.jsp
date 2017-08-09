<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
	<form id="orderForm"  method="post" action="flow.do?method=changeOrder" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label style="width:auto;">为<font color="#0000ff">${nodeName}节点</font>调整顺序：</label>
			</p>
			<p>
				<label style="width:auto;">让${nodeName}节点出于：</label>
				<select id="selOrder" name="selOrder" style="width:100px;" onchange="showOrder(this.value)">
					<option value="1">起始节点后</option>
					<option value="2">结束节点前</option>
					<option value="3">指定位置</option>
				</select>
			</p>
			
			<p id="p_order" style="display: none;">
				<label style="width:auto;">调整至：</label>
				<input type="text" id="newOrder" name="order" style="width:100px;" class="digits required"
    				 maxlength="5" value=""></input>
    				 <label style="width:auto;">（总数为：<font color="red">${maxOrder}</font>）</label>
			</p>
		</div>
		<input type="hidden" id="maxOrder" name="maxOrder" value="${maxOrder}"></input>
		<input type="hidden" name="flowId" value="${flowId}"></input>
		<input type="hidden" name="id" value="${id}"></input>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="smtOderForm();">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">
	function showOrder(_value){
		if(_value=='3'){
			$('#p_order').show();
		}else{
			$('#p_order').hide();
		}
	}
	function smtOderForm(){
		 var _maxorder = $('#maxOrder').val()-1;
		
	    var _value = $('#selOrder').val();
	    if(_value=="1"){
	    	$('#newOrder').val("2");
	    }else if(_value=="2"){
	    	$('#newOrder').val(_maxorder);
	    }else if(_value=="3"){
	       var _order = $('#newOrder').val();
	       if(!_order){
	           alertMsg.info('调整顺序不能为空！');
	           return;
	       }
	       if(_order<2)
	    		$('#newOrder').val("2");
	       else if(_order>_maxorder)
	       		$('#newOrder').val(_maxorder);
	    }
		$('#orderForm').submit();
	}
</script>
