<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>工作流编辑</title>
<!--[if lt IE 8]>
<?import namespace="v" implementation="#default#VML" ?>
<![endif]-->
<!--[if lt IE 9]>
<?import namespace="v" implementation="#default#VML" ?>
<![endif]-->
<!--[if lt IE 10]>
<?import namespace="v" implementation="#default#VML" ?>
<![endif]-->

<LINK href="http://cmwcm.trs.cn/wcm/app/application/common/doc_docment.css" rel="stylesheet" type="text/css"/>
<link href="appadmin/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="appadmin/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="appadmin/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
<link href="appadmin/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>


<script src="appadmin/js/jquery-1.7.2.js" type="text/javascript"></script>
<script src="appadmin/js/jquery.cookie.js" type="text/javascript"></script>
<script src="appadmin/js/jquery.validate.js" type="text/javascript"></script>
<script src="appadmin/js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="appadmin/xheditor/xheditor-1.1.14-zh-cn.min.js" type="text/javascript"></script>
<script src="appadmin/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>


<script src="appadmin/js/dwz.core.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.util.date.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.validate.method.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.regional.zh.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.barDrag.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.drag.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.tree.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.accordion.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.ui.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.theme.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.switchEnv.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.alertMsg.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.contextmenu.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.navTab.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.tab.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.resize.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.dialog.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.dialogDrag.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.sortDrag.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.cssTable.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.stable.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.taskBar.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.ajax.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.pagination.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.database.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.datepicker.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.effects.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.panel.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.checkbox.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.history.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.combox.js" type="text/javascript"></script>
<script src="appadmin/js/dwz.print.js" type="text/javascript"></script>

<script src="appadmin/js/dwz.regional.zh.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript" src="appadmin/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="appadmin/js/wcmflow.js"></script>
<script type="text/javascript" src="appadmin/js/GooFunc.js"></script>
<script type="text/javascript" src="appadmin/js/json.js"></script>
<link href="appadmin/css/wcmflow.css" rel="stylesheet" type="text/css" media="screen"/>

<script type="text/javascript">
$(function(){
	DWZ.init("appadmin/js/dwz.frag.workflow.xml", {
		loginUrl:"login_dialog.html", loginTitle:"登录",	// 弹出登录对话框
//		loginUrl:"login.html",	// 跳到登录页面
		statusCode:{ok:200, error:300, timeout:301}, //【可选】
		pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
		debug:false,	// 调试模式 【true|false】
		callback:function(){
			initEnv();
		}
	});
});

</script>

<SCRIPT src="http://cmwcm.trs.cn/wcm/trsplugins/communicatByAjax/js/AjaxRequestHandler.js" type="text/javascript"></SCRIPT>
<SCRIPT src="http://cmwcm.trs.cn/wcm/app/application/common/nav.js" type="text/javascript"></SCRIPT>
<SCRIPT src="http://cmwcm.trs.cn/wcm/app/application/common/doc_document.js" type="text/javascript"></SCRIPT>
<SCRIPT src="http://cmwcm.trs.cn/wcm/app/application/common/TRSDialog.js" type="text/javascript"></SCRIPT>
<style type="text/css">
.zd-nav ul li:hover a {
	background: none repeat scroll 0 0 #777;
	border-top-left-radius: 5px;
	border-top-right-radius: 5px;
	color: white;
	display: block;
	font-weight: bold;
	height: 49px;
	line-height: 49px;
	margin-top: 15px;
	width: 100%
}

.zd-nav ul {
	margin-left: 16px;
}

.hid {
	width: 120px;
	height: 45px;
	line-height: 45px;
	margin: 0px;
	padding: 0px;
	overflow: hidden;
}
.a-nav-btn01{
line-height: 48px;
text-indent: 52px;
color: #fff;
height: 48px;
float: left;
background: url(http://cmwcm.trs.cn/wcm/app/application/images/common/gy-btn11.png)no-repeat;
width: 195px;
margin-left: 25px;
}
.a-nav-btn01:hover{color: #fff;background:url(http://cmwcm.trs.cn/wcm/app/application/images/common/gy-btn11-hover.png) no-repeat;}
element.style {
}
.top-btn .a-top-btn-draft {
font-size: 14px;
display: block;
float: left;
width: 100px;
height: 48px;
line-height: 48px;
padding-left: 110px;
font-weight: bold;
background: url(http://cmwcm.trs.cn/wcm/app/application/images/common/gy-btn01.png) no-repeat;
color: #fff;
}
</style>
</head>

<body scroll="no">
<div class="s-header">
  <div class="zd-logo"><IMG style="margin: 5px 0px 0px 10px; float: left;" alt="" src="http://cmwcm.trs.cn/wcm/app/application/common/images/logo.png" border="0">
    <div style="height: 40px; line-height: 40px; font-size: 18px; font-weight: 600; margin-left: 10px; float: left;"><a style="font-size:18px;" href="http://cmwcm.trs.cn/pub/cmmgc/" target="_blank">浙江在线</a></div>
  </div>
  <div class="right ft12">
    <P class="closebox"><A class="close" href="javascript:void(0);"></A></P>
    <P class="filebox"><A class="none" href="javascript:void(0);"></A></P>
    <P class="mailbox"><A class="point" href="javascript:void(0);"></A></P>
    <P class="voicebox"><A class="point" href="javascript:void(0);"></A></P>
    <!-- <a href="" class="top-f">切换网站</a> <a href="" class="top-f">最近操作</a>  -->
    <SPAN style="font-size: 12px; font-weight: normal;">欢迎登陆，您好：</SPAN><A class="ftblue top-f" 
style="font-weight: normal;" href="javascript:void(0);">caohui</A></div>
</div>
<%--<div class="zd-nav">
  <UL>
    <LI>&nbsp;</LI>
    <LI><A class="on" href="javascript:void(0);">工作流编辑</A></LI>
  </UL>
</div>
--%>
<div class="nav-left">
  <ul>
  <c:if test="${not empty flowList}">
  	<c:forEach items="${flowList}" var="flow">
  		<ul class="bg4">
	      <a title="浙江即时报" class="list01 ${flow[1] eq appFlow.flowDesc ? 'onlist01' : ''}" href="workflow.do?method=listNode&id=${flow[1] }">
	      <div class="left nav-icon"><IMG alt="" src="http://cmwcm.trs.cn/wcm/app/application/images/doc/wz-icon01.png"> </div>
	      <div class="left nav-c" style="text-align:left;">
	        <div class="hid">${flow[0] }</div>
	      </div>
	      </a>
	      <div class="nav-left-sub" id="channel_article_children_1850" style="display: none;"></div>
	    </ul>
  	</c:forEach>
  </c:if>
    <%--<ul class="bg4">
      <a title="浙江即时报" class="list01" href="workflow.do?method=listNode&id=123">
      <div class="left nav-icon"><IMG alt="" src="http://cmwcm.trs.cn/wcm/app/application/images/doc/wz-icon01.png"> </div>
      <div class="left nav-c">
        <div class="hid">浙江即时报</div>
      </div>
      </a>
      <div class="nav-left-sub" id="channel_article_children_1850" style="display: none;"></div>
    </ul>
    <ul class="bg4">
      <a title="浙江即时报" class="list01" href="workflow.do?method=listNode&id=1611754">
      <div class="left nav-icon"><IMG alt="" src="http://cmwcm.trs.cn/wcm/app/application/images/doc/wz-icon01.png"> </div>
      <div class="left nav-c">
        <div class="hid">浙江即时报22</div>
      </div>
      </a>
      <div class="nav-left-sub" id="channel_article_children_1850" style="display: none;"></div>
    </ul>
    <ul class="bg4">
      <a title="浙江即时报" class="list01" href="workflow.do?method=listNode&id=1611351">
      <div class="left nav-icon"><IMG alt="" src="http://cmwcm.trs.cn/wcm/app/application/images/doc/wz-icon01.png"> </div>
      <div class="left nav-c">
        <div class="hid">浙江即时报333</div>
      </div>
      </a>
      <div class="nav-left-sub" id="channel_article_children_1850" style="display: none;"></div>
    </ul>
  --%></ul>
</div>
<div class="nav-center" id="firstLayer" style="height: 1210px; display: none;"></div>
<div class="nav-center nav-bg01" id="secondLayer" style="display: none;"></div>
<div class="nav-center nav-bg02" id="thirdLayer" style="display: none;"></div>
<div class="wz-con">
<div id="navTab" class="left">
	<div class="navTab-tab">
		<div class="formBar" style="align:left;">
				<ul>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button style="width:80px;height:30px;" type="button" onclick="saveWorkFlow();">保存</button>
							</div>
						</div>
					</li>
					<%--<li>
						<div class="button"><div class="buttonContent"><button type="button" onclick="closeFlowEditor();" class="close">取消</button></div></div>
					</li>
				--%></ul>
			</div>
		<div class="pageContent">
			<form id="flowForm" method="post" action="workflow.do?method=saveWorkFlow" onsubmit="return validateCallback(this)">
				<input type="hidden" name = "flowId" value = "${appFlow.flowId}"/>
				<input type="hidden" name = "flowJsonData" value = ""/>
				<input type="hidden" name = "delData" value = ""/>
			</form>
			<div id="appFlow"></div>
			<!--<input type="button" onclick="saveWorkFlow();" value="保存"/>
			-->
		</div>	
	</div>
</div>
<div class="table"></div>
</div>

<script type="text/javascript">
$(function(){
	$(".nav-left").height($(window).height() - $(".s-header").height());
	$(".nav-left").find(".bg4").find("a").hover(function(){
		$("#firstLayer,#secondLayer,#thirdLayer").hide();
	});

	// 点击左侧列表栏时为选中栏目/频道着色
	$(".list01").each(function(){
		$(this).click(function(){
			if($(this).next()){
				if($(this).next().is(":hidden")){
					$(".list01").removeClass("onlist01");
					$(this).addClass("onlist01");
					//var json = $(this).attr("param");
					//json = eval('('+json+')');
					//loadChildrenChannel(json.channelId,json.siteId,onSecond);
				}else{
					if($(this).is(".onlist01")){
						$(this).next().hide();
					}else{
						$(".list01").removeClass("onlist01");
						$(this).addClass("onlist01");
						//var json = $(this).attr("param");
						//json = eval('('+json+')');
						//loadChildrenChannel(json.channelId,json.siteId,onSecond);
					}
				}
			}else{
				$(".list01").removeClass("onlist01");
				$(this).addClass("onlist01");
				//var json = $(this).attr("param");
				//json = eval('('+json+')');
				//loadChildrenChannel(json.channelId,json.siteId,onSecond);
			}
		})
	});

	String.format = String.prototype.format = function(format){
		var args = Array.prototype.slice.call(arguments, 1);
		return format.replace(/\{(\d+)\}/g, function(m, i){
			return args[i];
		});
	};
	
});


var _width = $(window).width() - $(".nav-left").width();
var _height = $(window).height() - $(".s-header").height() - 36;
var property={
	width:_width,
	height:_height,
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
	appFlow.reinitSize(_width,_height);
//});
//新增节点双击后打开节点编辑页面
var obj_this;//全局变量用于获取当前节点this
var obj_id;//全局变量用于获取当前节点的ID
function _addNodes(objthis,objId){
	obj_this = objthis;
	obj_id = objId;
	var nodeName = obj_this.$nodeData[obj_id].name;
	$.pdialog.open("/trsapp/workflow.do?method=node_edite&flowId=${appFlow.flowId}&nodeName=" + encodeURIComponent(encodeURIComponent(nodeName)),"node_editor","创建/编辑节点",{mask:true,width:'624',height:'572'});
}
function _delNode(_flowId, _flowName, _nodeName){
	//alert(_flowId+" "+_flowName);
	var _url = "workflow.do?method=delNode";
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
//关闭时确认
$(window).unload(function(){
	closeFlowEditor();
});
function closeFlowEditor(){
	if(appFlow.$altFlag){
		if(confirm("您已修改了工作流信息，是否保存已修改的内容?")){
			saveWorkFlow();
		}
	}
	return true;
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
	}else{
		alertMsg.info('工作流中没有需要保存的内容！');
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
</body>	
</html>