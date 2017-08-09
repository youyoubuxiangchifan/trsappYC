<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pageContent">
    <div  layoutH="40">
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
</div>

<script  type="text/javascript">
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
				url:"", //设置异步获取节点的 URL 地址
				dataFilter: ajaxJsonFilter

		},  
        callback: {   
            onExpand: zTreeRegisterTarget,
            onAsyncSuccess:zTreeRegisterTargetx
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
    function zTreeOnClick(event, treeId, treeNode) {   
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
        //var _groupType = $('input[name=groupType]:checked').val(); 
        setting.async.url = 'appView.do?method=getGroupTree&pid=${pid}&groupType=${groupType}';//'+_groupType  
        var isHasGroup = '${isHasGroup}';
        if(isHasGroup == 'true'){
			$.fn.zTree.init($("#orgTree"), setting, zNodes);
        }else{
			$("#orgTree").html("系统组织管理中还未添加任何组织！");
        }
		
    });
        
		//获取所有选中节点的值
    function GetCheckedAll() {
        var _orgIds = new Array();
        var treeObj = $.fn.zTree.getZTreeObj("orgTree");
        var nodes = treeObj.getCheckedNodes(true);
        for (var i = 0; i < nodes.length; i++) {
              _orgIds.push(nodes[i].id);
        }
        return  _orgIds.toString();
    } 
     //选中指定的节点
    function AssignCheck() {
         var treeObj = $.fn.zTree.getZTreeObj("orgTree");
		var _idArr = new Array();
		var _orgIds = $('#groupIds').val();
		if(_orgIds==undefined || _orgIds=="" ){
		    return;
		}else{
			_idArr = _orgIds.split(",");
		}
		for(i=0;i<_idArr.length;i++){
		    var _node = treeObj.getNodeByParam("id",_idArr[i], null);
			if(_node!=null)
			treeObj.checkNode(_node, true, true);
		}
       
    }
    //将选择的节点回传给父页面的隐藏域
    function setSelectId(){
        var _ids = GetCheckedAll();
        if(!_ids){
        	alertMsg.info('请选择组织！');
 			return;
        }else{
        	$('#groupIds').val(_ids);
        	 $.pdialog.closeCurrent(); 
        }
    }   
</script>
