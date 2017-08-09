<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
	<div class="tabs">
		
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><span>用户列表</span></li>
				</ul>
			</div>
		</div>
	
		<div class="tabsContent">
			<div>
	
				<div layoutH="50" style="float:left; display:block; overflow:auto; width:240px; border:solid 1px #CCC; line-height:21px; background:#fff">
				    <ul id="orgTree" class="ztree"></ul>
				</div>
				
				<div id="jbsxBox" class="unitBox" style="margin-left:246px;">
					
				</div>
	
			</div>
			
			
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
<script type="text/javascript">
    var _selUsers = new Array();//用于存放已选的用户id
	var setting = { 
 
        data: {   
            simpleData: {   
                enable: true  
            }   
        },async: {
				enable: true,//开启异步加载
				autoParam:["id","name","url","target","rel"],//设置父节点数据需要自动提交的参数
				url:"appView.do?method=getParGroupTree", //设置异步获取节点的 URL 地址
				dataFilter: ajaxJsonFilter

		},  
        callback: {   
            onExpand: zTreeRegisterTarget,
            onAsyncSuccess:zTreeRegisterTargetx,
            onClick: zTreeOnClick
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
    }
    function zTreeOnClick(event, treeId, treeNode) { 
        refreshTab(treeNode.id);
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
	//根据部门id来局部刷新用户列表，部门id==0刷出所有的用户
    function refreshTab(_depId){
    	var _url = "role.do?method=deptUser&roleID=${roleID}&deptID="+_depId;
    	$("#jbsxBox").loadUrl(_url,"", "");
    }
    $(document).ready(function(){   
		$.fn.zTree.init($("#orgTree"), setting, zNodes);
		refreshTab(0);
    });
</script>
