<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
	<form id="nodeForm"  method="post" action="workflow.do?method=${method}" class="pageForm required-validate" 
	onsubmit="return validateCallback(this,dialogAjaxDonetest)" >
		<div class="pageFormContent" layoutH="56">
			<p id="flow_nodeType">
				<label>节点类型：</label>
				<c:if test="${method == 'addNode'}">
					<select id="nodeOrder" name=nodeOrder style="width:155px;" onchange="setNode()">
					<c:if test="${startFlag == false}">
						<option value="0" ${node.nodeOrder == 0 ? "selected" : ""}>开始节点</option>
					</c:if>
						<option value="-1" ${node.nodeOrder == -1 ? "selected" : ""}>任务节点</option>
					<c:if test="${endFlag == false}">
						<option value="1" ${node.nodeOrder == 1 ? "selected" : ""}>结束节点</option>
					</c:if>
					</select>
				</c:if>
				<c:if test="${method == 'editeNode'}">
					<select id="nodeOrder" name=nodeOrder style="width:155px;" disabled="true">
						<option value="0" ${node.nodeOrder == 0 ? "selected" : ""}>开始节点</option>
						<option value="-1" ${node.nodeOrder == -1 ? "selected" : ""}>任务节点</option>
						<option value="1" ${node.nodeOrder == 1 ? "selected" : ""}>结束节点</option>
					</select>
				</c:if>
			</p>
			<%--<p id="beforName">
				<label>前一节点名称：</label>
				<input type="text"  value="${beforName}" readonly="readonly" style="width:150px;"/>
			</p>
			--%>
			<p id="flow_nodeName">
				<label>节点名称：</label>
				<input id="fnodeName" name="fnodeName" style="width:150px;" class="required" type="text" maxlength="50" value="${node.nodeName}"/>
			</p>
			<%--<p id="flow_limitDayNum">
				<label>处理时限：</label>
				<input type="text" name="limitDayNum" style="width:150px;" class="digits required" alt="请输入数字"
    				 maxlength="50" value="${node.limitDayNum}"></input>
			</p>
			--%><p id="flow_nodeDocStatus">
				<label>数据状态：</label>
				<select name="nodeDocStatus" style="width:155px;">
				 <c:forEach items="${statuts}" var="statut">
					<option value="${statut.datastatusId}" <c:if test="${node.nodeDocStatus == statut.datastatusId}">selected="selected"</c:if>>${statut.statusname}</option>
				</c:forEach>
				</select>
			</p>
			<p id="flow_nodeDesc" style="height:150px;">
				<label>节点描述：</label>
				<textarea id="fnodeDesc" name="fnodeDesc" rows="8" maxlength="100">${node.nodeDesc}</textarea>
			</p>
			<p id="flow_option1">
				<label>节点操作限定</label>
				<input type="checkbox" name="isFinish" value="1" <c:if test="${node.isFinish == '1'}">checked="checked"</c:if>/>直接处理
				<input type="checkbox" name="isassign" value="1" <c:if test="${node.isassign == '1'}">checked="checked"</c:if>/>开启交办
				<input type="checkbox" name="together" value="1" <c:if test="${node.together == '1'}">checked="checked"</c:if>/>会签
			</p>
			<p id="flow_option2">
				<label>&nbsp;</label>
				<input type="checkbox" name="isaccept" value="1" <c:if test="${node.isaccept == '1'}">checked="checked"</c:if>/>开启签收
				<input type="checkbox" name="isemail" value="1" <c:if test="${node.isemail == '1'}">checked="checked"</c:if>/>邮件提醒
				<input type="checkbox" name="ismessage" value="1" <c:if test="${node.ismessage == '1'}">checked="checked"</c:if>/>短信提醒
			</p>
			<p id="flow_operRuleName">
				<label>指定操作规则：</label>
				<select id="operRuleName" name="operRuleName" style="width:155px;">
				    <option value="">--请指定操作规则--</option>
					<c:forEach items="${autoUserRule}" var="guid">
					<option value="${guid.className}" <c:if test="${node.operRuleName == guid.className}">selected="selected"</c:if>>${guid.guidDesc}</option>
				    </c:forEach>
				</select>
			</p>
			<p id="flow_showRule">
				<label>指定接收规则</label>
			    <input id="showRule" type="checkbox" name="showRule" value="1" onclick="changeDiv()"/>
			    <span><font color="red" >提示：指定规则将不能指定用户或组织。</font></span><br>
			</p>
			<div id="u_div" style="overflow:hidden; width:380px; height:100px;">
			    <p>
					<label>接收用户：</label>
					<input id="userName" type="text" id="userName" name="userName" style="width:150px;" value="${userNames}" readonly="readonly"></input>
					<span style="line-height: 20px;margin-left:5px;margin-top:5px;">
    					<a target="dialog" width="850" height="550" mask="true" rel="udialog" href="workflow.do?method=userDialog&id=${node.nodeId}" title="用户列表"><img src="${basePath}appadmin/images/persons.gif"></img></a>
					</span>
				</p>
				<p>
					<label>接收组织：</label>
					<input id="depName" type="text" id="depName" name="depName" style="width:150px;"  value="${orgNames}" readonly="readonly"></input>
					<span style="line-height: 20px;margin-left:5px;margin-top:5px;">
					<a target="dialog" width="400" height="550" mask="true" rel="udialog" href="workflow.do?method=orgDialog" title="添加组织"><img src="${basePath}appadmin/images/persons.gif"></img></a>
					</span>
				</p>
			<!--   
			<p>
				<label>指定角色：</label>
				<input id="nrole" type="text" id="roleName" name="roleName" style="width:150px;" value="${roleName}" readonly="readonly"></input>
			</p>
			-->
		</div>
		<div id="r_div"  style="overflow:hidden; width:380px;display:none;height:50px; ">
			<p>
				<label>指定规则：</label>
				<select id="rule_sel" name="ruleName" style="width:155px;">
				    <option value="">--请指定规则--</option>
					<c:forEach items="${userRule}" var="guid">
					<option value="${guid.className}" <c:if test="${node.ruleName == guid.className}">selected="selected"</c:if>>${guid.guidDesc}</option>
				    </c:forEach>
				</select>
			</p>
		</div>
		<%--<div style="border:solid 1px #CCC; line-height:21px; background:#fff;width:150px;height:50px;padding-top:10px;">
		    <span><font color="red" >提示：指定规则将不能指定用户或组织。</font></span><br>
		    <input id="showRule" type="checkbox" name="showRule" value="1" onclick="changeDiv()"/>指定接收规则
		</div>
		--%><input type="hidden" name="flowId" value="${flowId}"/>
		<input type="hidden" name="id" value="${node.nodeId}"></input>
		<input id="nuser" type="hidden" id="nodeUser" name="nodeUser" style="width:150px;" value="${node.nodeUser}" ></input>
		<input id="ndep" type="hidden" id="nodeDep" name="nodeDep" style="width:150px;" value="${node.nodeDep}"></input>
		<input id="nrole" type="hidden" id="nodeRole" name="nodeRole" style="width:150px;" value="${node.nodeRole}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="subNodeForm('${node.nodeName}');">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">
	//当节点为结束时隐藏一些设置
	function setNode(){
		var nodeType = $('#nodeOrder option:selected').val();
		//alert(nodeType);
		$('#flow_operRuleName').hide();
		if(nodeType == '1'){
			//$('#flow_nodeName').hide();
			//$('#flow_limitDayNum').hide();
			//$('#flow_limitDayNum input').removeClass('required');
			$('#flow_option1').hide();
			$('#flow_option2').hide();
			$('#flow_showRule').hide();
			$('#u_div').hide();
			$('#r_div').hide();
			//$('#flow_operRuleName').hide();
			if($('#fnodeName').val() == '')
				$('#fnodeName').val('结束');
			//$('#fnodeName').attr("readonly",true);
		} else {
			$('#flow_nodeName').show();
			//$('#flow_limitDayNum').show();
			//$('#flow_limitDayNum input').addClass('required');
			$('#flow_option1').show();
			$('#flow_option2').show();
			$('#flow_showRule').show();
			$('#u_div').show();
			if(nodeType == '-1'){
				//$('#flow_operRuleName').hide();
				$('#fnodeName').val('${node.nodeName}');
				$('#fnodeName').attr("readonly",false);
			}else if(nodeType == '0'){
				$('#flow_operRuleName').show();
				if($('#fnodeName').val() == '')
					$('#fnodeName').val('开始');
				//$('#fnodeName').attr("readonly",true);
				if($('#operRuleName').val() != ''){
					$('#flow_showRule').hide();
					$('#u_div').hide();
				}else{
					$('#flow_showRule').show();
					$('#u_div').show();
				}
			}
			var _ruleid = $('#rule_sel').val();
			if (_ruleid) {
				$('#r_div').show();
				$('#u_div').hide();
				$('#showRule').attr("checked", "checked");
			}
			//$('#r_div').show();
		}
	}
	//初始化规则显示
	function initNodeForm(bname) {
		if (!bname || bname == 'null')
			$('#beforName').hide();
		var _ruleid = $('#rule_sel').val();
		if (_ruleid) {
			$('#r_div').show();
			$('#u_div').hide();
			$('#showRule').attr("checked", "checked");
		}
		if($('#operRuleName').val() != ''){
			$('#flow_showRule').hide();
			$('#u_div').hide();
		}else{
			$('#flow_showRule').show();
			$('#u_div').show();
		}
	}
	//更换div
	function changeDiv() {
		var _cheched = $('input[name=showRule]').attr("checked");
		if (_cheched == 'checked') {//选中后向数组里添加id
			$('#r_div').show();
			$('#u_div').hide();
		} else {
			$('#r_div').hide();
			$('#u_div').show();
		}
	}
	function check() {
		var nodeType = $('#nodeOrder option:selected').val();
		if(nodeType == '1'){
			return true;
		}
		var _cheched = $('input[name=showRule]').attr("checked");
		if (_cheched == 'checked') {//选中后向数组里添加id
			var _rule = $('#rule_sel').val();
			if (!_rule) {
				alertMsg.info('请为节点指定规则！');
				return false;
			}
		} else {
			var _user = $('#nuser').val();
			var _dep = $('#ndep').val();
			var _operRule = $('#operRuleName').val();
			//var _role = $('#nrole').val();
			if (!_user && !_dep && _operRule == '') {
				alertMsg.info('请为节点指定接收人员或组织！');
				return false;
			}
		}
		return true;
	}
	//提交表单
	function subNodeForm(oldName) {
		var _nodeName = $('#fnodeName').val();
			if (!check())
				return;
			if (oldName!='' && oldName == _nodeName) {
				$('#nodeForm').submit();
				return;
			}
			var _url = "workflow.do?method=checkNode";
			var _data = {
				flowId : '${flowId}',
				nodeName : _nodeName
			}
			$.post(_url, _data, function(result) {
				if (result == 'true') {
					alertMsg.info('节点名称不唯一,请修正！');
					$('#fnodeName').focus();
				} else {
					$('#nodeForm').submit();
				}
			});
	}
	
	$(function(){
		$('#operRuleName').change(function(){
			if($(this).val() != ''){
				$('#flow_showRule').hide();
				$('#u_div').hide();
				$('#userName').val("");
				$('#depName').val("");
				$('#r_div').hide();
				$('#showRule').attr("checked", false);
			}else{
				$('#flow_showRule').show();
				$('#u_div').show();
			}
		});
		setNode();
		//initNodeForm('${beforName}');
	});
</script>
