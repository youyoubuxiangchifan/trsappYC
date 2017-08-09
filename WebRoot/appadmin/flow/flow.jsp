<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="flowForm" method="post" action="flow.do?method=saveWorkFlow" onsubmit="return validateCallback(this)">
	<input type="hidden" name = "flowId" value = "${appFlow.flowId}"/>
	<input type="hidden" name = "flowJsonData" value = ""/>
	<input type="hidden" name = "delData" value = ""/>
</form>
<div class="pageContent">
	<div id="appFlow"></div>
	<!--<input type="button" onclick="saveWorkFlow();" value="保存"/>
	--><div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="saveWorkFlow();">保存</button></div></div></li>
			<li>
				<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
			</li>
		</ul>
	</div>
</div>	
	
<script type="text/javascript">
var property={
	width:1000,
	height:550,
	toolBtns:["task"],//"start","end",
	haveHead:true,
	headBtns:[],//如果haveHead=true，则定义HEAD区的按钮
	haveTool:true,
	haveGroup:false,
	useOperStack:true
};
var remark={
	cursor:"选择指针",
	direct:"转换连线",
	task:"任务结点",
	start:"开始结点",
	end:"结束结点"
	
};
var appFlow;
//$(document).ready(function(){
	appFlow=$.createGooFlow($("#appFlow"),property);
	appFlow.setNodeRemarks(remark);
	appFlow.onItemDel=function(id,type){
		//alert(id+"--"+type);
		if(type == 'node'){
			if(confirm("确定要删除该节点吗?")){
				var _nodeName = this.$nodeData[id].name;
				//alert(_nodeName);
				//_delNode('${appFlow.flowId}', '${appFlow.flowName}', _nodeName);
				//return true;
				if(_nodeName == '开始' || _nodeName == '结束'){
					alertMsg.info('开始或结束节点不可以删除！');
					return false;
				}
				return true;
			}
		} else if(type == 'line'){
			return confirm("确定要删除该连线吗?");
		} else {
			return confirm("确定要删除该单元吗?");
		}
	}
	var jsondata = eval('(${appFlow.flowJsonData == null ? "null" : appFlow.flowJsonData})');
	//alert(jsondata);
	if(jsondata == null){
		jsondata={
			title:"${appFlow.flowName}",
			nodes:{
				//appFlow_node_1:{name:"node_1",left:67,top:69,type:"start",width:24,height:24},
				//appFlow_node_2:{name:"node_2",left:219,top:71,type:"task",width:86,height:24}
			},
			lines:{
				//appFlow_line_3:{type:"sl",from:"appFlow_node_1",to:"appFlow_node_2",name:"",marked:false}
			}
			
		};
	}
	jsondata.title = "${appFlow.flowName}";
	//appFlow.$max = parseInt('${nodeCount}'); //该属性是防止ID重复，新建工作流不需要，但是编辑已有工作流的时候，节点的编号必须设置，否则会和原来的重复
	//jsondata.initNum = parseInt('${nodeCount}');
	appFlow.loadData(jsondata);
	//alert($.pdialog.getCurrent().width());
	appFlow.reinitSize($.pdialog.getCurrent().width() - 15,$.pdialog.getCurrent().height() - 70);
//});
//新增节点双击后打开节点编辑页面
var obj_this;//全局变量用于获取当前节点this
var obj_id;//全局变量用于获取当前节点的ID
function _addNodes(objthis,objId){
	obj_this = objthis;
	obj_id = objId;
	var nodeName = obj_this.$nodeData[obj_id].name;
	$.pdialog.open("/trsapp/flow.do?method=node_edite&flowId=${appFlow.flowId}&nodeName=" + encodeURIComponent(encodeURIComponent(nodeName)),"node_editor","创建/编辑节点",{mask:true,width:'624',height:'572'});
}
function _delNode(_flowId, _flowName, _nodeName){
	//alert(_flowId+" "+_flowName);
	var _url = "flow.do?method=delNode";
	var _data = {
		flowId : _flowId,
		flowName : _flowName,
		nodeName : _nodeName
	}
	$.post(_url, _data, function(result) {
		//alert(result);
		if (result == 'true') {
			alertMsg.info('节点删除成功！');
		}
	});
}
//节点保存后回调函数
function dialogAjaxDonetest(json){
	if ("closeCurrent" == json.callbackType) {
			$.pdialog.closeCurrent();
	}
	//设置节点名称
	obj_this.setName(obj_id,json.message,"node");
}
function saveWorkFlow(){
	//alert(JSON.stringify(appFlow.exportData()));
	var flowJsonData = JSON.stringify(appFlow.exportData());
	//var alterJsonData = JSON.stringify(appFlow.exportAlter());
	var delNodeData = appFlow.$delNodeData;
	var delLineData = appFlow.$delLineData;
	var delData = {"delNodeData":delNodeData,"delLineData":delLineData};
	//alert(JSON.stringify(delData));
	//alert(appFlow.$altFlag);
	$('#flowForm input[name=delData]').val(JSON.stringify(delData));
	$('#flowForm input[name=flowJsonData]').val(flowJsonData);
	if(appFlow.$altFlag){
		//alert(getJsonLength(appFlow.$nodeData));
		if(getJsonLength(appFlow.$nodeData) == 0){
			alertMsg.info('工作流中没有需要保存的内容！');
			return;
		}
		$('#flowForm').submit();
		appFlow.$delNodeData = {};
		appFlow.$delLineData = {};
		appFlow.$altFlag = false;
	}
}
function getJsonLength(jsonData){
	var jsonLength = 0;
	for(var item in jsonData){
		jsonLength++;
	}
	return jsonLength;
}
</script>
