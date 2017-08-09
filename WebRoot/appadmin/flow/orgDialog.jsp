<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <style type="text/css">
        #ol_name li{
           float: left;
           display: inline;
           width: 120px;
           margin: 2px 0px;
           font-size: 20;
           height:20px;
           line-height:20px;
        }
        .s_name{
           height:20px;
           line-height:20px;
           margin-right:5px;
           display:block;
           float:left;
        }
   </style>
    <div class="pageContent">
    <div style="height:300px;overflow:scroll;">
    	<ul id="orgTree" class="ztree"></ul>
	</div>
	<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="setSelectId();">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
	</div>
	<div class="panel" defH="130" style="margin:0 0;">
		<h1>已选择组织</h1>
		<div>
			<ol id="ol_name">
			   
			</ol>
			
		</div>
   </div>
</div>
<script  type="text/javascript">
	var _orgIds = new Array();//用于存放组织id
	var _orgNames = new Array();//用于存放组织名称
	var setting = { 
 		check: {
			enable: true,
			chkboxType: { "Y": "", "N": "" }
		},  
        data: {   
            simpleData: {   
                enable: true  
            }   
        },async: {
				enable: true,//开启异步加载
				autoParam:["id","name","url","target","rel"],//设置父节点数据需要自动提交的参数
				url:"appView.do?method=getGroupTree", //设置异步获取节点的 URL 地址
				dataFilter: ajaxJsonFilter

		},  
        callback: {   
            onExpand: zTreeRegisterTarget,
            onAsyncSuccess:zTreeRegisterTargetx,
            onCheck: zTreeOnCheck
        } 
    };	                             
        var zNodes ;
        var currNodeId ;
	function ajaxJsonFilter(treeId, parentNode, childNodes){
		var zTree = $.fn.zTree.getZTreeObj("orgTree");
		if (!childNodes) return null;
		var l=childNodes.length;
		if(l<1){
			zTree.reAsyncChildNodes(parentNode.getParentNode(), "refresh");			
		}
		return childNodes;
	}
    function zTreeRegisterTarget(event, treeId, treeNode) {   
        initUI($('#'+treeId));    
    }
    //取消链接，避免刷新页面
    function delLink(){
    	   $('#orgTree a').removeAttr('target');
	       $('#orgTree a').attr('href','javascript:;')
    }
    function zTreeRegisterTargetx(event, treeId, treeNode) {   
        //initUI($('#'+treeId));
        var treeObj = $.fn.zTree.getZTreeObj("orgTree");
        var node = treeObj.getNodeByParam("id", currNodeId, null);
        treeObj.selectNode(node);
        var $p = $(document);
		delLink();
		AssignCheck();
    }
    //响应树的选中事件
    function zTreeOnCheck(event, treeId, treeNode) {
    	if(treeNode.checked){
    		addOrg(treeNode.id,treeNode.name);
    	}else{
    		removeOrg(treeNode.id);
    	}
	}
    //从数组中移除组织信息
 	function removeOrg(id){
 		for(i=0;i<_orgIds.length;i++){
 			if(_orgIds[i]==id){
 					_orgIds.splice(i,1);
 					_orgNames.splice(i,1);
 					removeLi(id);
 			}
 		}
 	}
 	//从数组中添加组织信息
 	function addOrg(orgId,orgName){
 		_orgIds.push(orgId);
 		_orgNames.push(orgName);
 		addli(orgId,orgName);
 	}
	function refreshNode() {
		var zTree = $.fn.zTree.getZTreeObj("orgTree"),
		nodes = zTree.getSelectedNodes();
		if (nodes.length == 0) {
			alert("请先选择一个父节点");
		}
		var parentNode = nodes[0].getParentNode();
		currNodeId = nodes[0].id;
		var isParent = nodes[0].isParent;		
 		if(isParent==false){
			zTree.reAsyncChildNodes(parentNode, "refresh");			
		}else{		
			zTree.reAsyncChildNodes(nodes[0], "refresh");
		}
	} 
        
    $(document).ready(function(){   
		$.fn.zTree.init($("#orgTree"), setting, zNodes);
		
    });
        
		//获取所有选中节点的值
    function GetCheckedAll() {
        var treeObj = $.fn.zTree.getZTreeObj("orgTree");
        var nodes = treeObj.getCheckedNodes(true);
        for (var i = 0; i < nodes.length; i++) {
              _orgIds.push(nodes[i].id);
        }
        return  _orgIds.toString();
    } 
    
    //将选择的节点回传给父页面的隐藏域
    function setSelectId(){
        var _depName = $("#depName").val();//获取父窗口已选的组织名
     	var _gIds= _orgIds.toString();
    	var _gName = _orgNames.toString();
 		if(_gIds=="" && _depName!=""){
 			_gName = "";
 		}else if(_gIds==""){
 			alertMsg.info('请选择组织！');
 			return;
 		}
 		$("#ndep").val(_gIds);
 		$("#depName").val(_gName);
 		$.pdialog.closeCurrent(); 
       
    }
     //选中指定的节点
    function AssignCheck() {
        var treeObj = $.fn.zTree.getZTreeObj("orgTree");
		for(i=0;i<_orgIds.length;i++){
		    var _node = treeObj.getNodeByParam("id",_orgIds[i], null);
			if(_node!=null)
			treeObj.checkNode(_node, true, true);
		}
       
    }
    //初始化已选择的信息
    function initSelOrg(){
   	  var _gIds = $("#ndep").val();
   	   var _gName = $("#depName").val();
   	  if(_gIds==undefined || _gIds==""){
    		return;
    	}
    	_orgIds = _gIds.split(',');
    	_orgNames = _gName.split(',');
    	for(i=0;i<_orgIds.length;i++){
			addli(_orgIds[i],_orgNames[i]);
		}
   	}
   	//添加用户列表
   	function addli(_id,_name){
   	    var _lihtml = '<li id="li_'+_id+'"><span class="s_name">'+_name
   	    +'</span><span><img onclick="delName('+_id+')" style="margin-left:10px;" src="${basePath}appadmin/images/delbtn.gif"></img></span></li>';
   	    $('#ol_name').append(_lihtml);
   	}
   	function removeLi(_id){
   	    $('#li_'+_id).remove();
   	}
   	function delName(_orgId){
   		removeOrg(_orgId);
   		var treeObj = $.fn.zTree.getZTreeObj("orgTree");
   		var _node = treeObj.getNodeByParam("id",_orgId, null);
		if(_node!=null)
			treeObj.checkNode(_node, false, true);
   	}
   	initSelOrg();   
</script>
