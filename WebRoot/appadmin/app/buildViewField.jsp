<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style type="text/css">
.fl{
	float:left;
}
.border_gray{
	border:solid 1px #CCC;
}
.border_r_gray{
	border-right:solid 1px #CCC;
}
.h20{
	height:20px;
}
.w70{
	width:70px;
}
.w120{
	width:120px;
}
.lineh20{
	line-height:20px;
}
.sel_div{
	background-color: #7cc5e5;
}
.align_c{
	text-align:center;
}
#boxmeta{
	height:100%;width:275px;
}
#fieldmeta{
	height:100%;width:200px;
}
#relfieldmeta{
	height:100%;width:500px;
}
.rel_tit_row{
	width:100%;
	height:28px;
	font-weight:bold;
	background-color: #e8eff1;
}
.rel_tit{
	height:28px;
	line-height:29px;
}
.rel_row{
	width:100%;
	cursor: pointer;
}
.box_title{
	height:28px;
	line-height:29px;
	font-weight:bold;
	background-color: #e8eff1;
}
.rel_field{
	padding-top:5px;
	height:442px;
	overflow-x:visible;
	overflow-y:scroll;
}
</style>
<div class="pageContent">
<div id="boxmeta" layoutH="45" class="fl border_gray">
	<div class="box_title">
		&nbsp;&nbsp;元数据信息
	</div>
	<c:forEach items="${tableList}" var="map">
	<div class="h20">
		<input type="radio" value="${map.tableInfoId}" name="tableid" onclick="refFeildList(this.value,${map.itemTableId == null ? 0 : map.itemTableId})" title="${map.tableName}" itemTableId = "${map.itemTableId == null ? 0 : map.itemTableId}" itemTableName="${map.itemTableName == null ? '' : map.itemTableName}"/>
		<span>${map.anotherName}(${map.tableName})</span>
	</div>
	</c:forEach>
</div>
<div id="fieldmeta" layoutH="43" class="fl border_gray">
	<div class="tabs" currentIndex="0" eventType="click">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a href="javascript:changeRelField(0);"><span>主表字段</span></a></li>
					<c:if test="${tbType == 1}">
					<li><a href="javascript:changeRelField(1);"><span>从表字段</span></a></li>
					</c:if>
				</ul>
			</div>
		</div>
		<div class="tabsContent" style="height:437px;background: #fff;">
			<div id="fContent">
				<c:forEach items="${fieldList}" var="map">
				<div class="h20">
					<div class="fl h20 lineh20">
						<input id="${map.fieldId}" type="checkbox" title="${map.fieldName }" value="${map.fieldDesc}" ${map.isReserved == 1 ? "checked disabled":"" } issys = "${map.isReserved }" onclick="showRel('relContent','${map.fieldId}','${map.fieldName }','${map.fieldDesc}','${map.fieldType }');"/>
					</div>
					<div class="fl h20 lineh20" title="${map.fieldDesc }">${fn:substring(map.fieldDesc,0,10)}<c:if test="${fn:length(map.fieldDesc)>10}">...</c:if></div>
				</div>
				</c:forEach>
			</div>
			<c:if test="${tbType == 1}">
			<div id="item_fContent">
				<c:forEach items="${itemFieldList}" var="map">
				<div class="h20 lineh">
					<div class="fl h20 lineh20">
						<input id="${map.fieldId}" type="checkbox" title="${map.fieldName }" value="${map.fieldDesc}" ${map.isReserved == 1 ? "checked disabled":"" } issys = "${map.isReserved }" onclick="showRel('item_relContent','${map.fieldId}','${map.fieldName }','${map.fieldDesc}','${map.fieldType }');"/>
					</div>
					<div class="fl h20 lineh20" title="${map.fieldDesc }">${fn:substring(map.fieldDesc,0,10)}<c:if test="${fn:length(map.fieldDesc)>10}">...</c:if></div>
				</div>
				</c:forEach>
			</div>
			</c:if>
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
</div>
<div id = "relfieldmeta" layoutH="45" class="fl border_gray">
	  <div class="rel_tit_row">
		<div class = "fl border_r_gray rel_tit" style="width:120px;">
			字段描述
		</div>
		<div class="fl w70 border_r_gray align_c rel_tit">
			概览显示
		</div>
		<div class="fl w70 border_r_gray align_c rel_tit">
			标题字段
		</div>
		<div class="fl w70 border_r_gray align_c rel_tit">
			检索字段
		</div>
		<div class="fl w70 align_c rel_tit">
			前台展现
		</div>
		<div class="fl w70 align_c rel_tit">
			分组字段
		</div>
	 </div>
	 <div id="relContent" class="rel_field">
		<c:forEach items="${fieldRelList}" var="map">
		<div id="rel_${map.fieldId}" class="h20 rel_row" onclick="selField(this.id)">
			<div class = "fl w120">
				<span id="relspan" class="h20 lineh20" title="${map.fieldDesc}">${fn:substring(map.fieldDesc,0,7)}<c:if test="${fn:length(map.fieldDesc)>7}">...</c:if></span>
			</div>
			<div class="_gl fl w70 align_c">
				<input id="gl_${map.fieldName}" type="checkbox" value="${map.fieldId}" ${map.inOutline == '1' ? "checked" : ""} ${(map.fieldType == 'editor' || map.fieldType == 'smeditor') ? "disabled" : "" }/>
			</div>
			<div class="_bt fl w70 align_c">
				<input id="bt_${map.fieldName}" type="radio" name="_bt" value="${map.fieldId}" ${map.isReserved == 1 ? "disabled":"" } ${map.titleField == '1' ? "checked" : ""} ${(map.fieldType == 'editor' || map.fieldType == 'smeditor') ? "disabled" : "" }/>
			</div>
			<div class="_js fl w70 align_c">
				<input id="js_${map.fieldName}" type="checkbox" value="${map.fieldId}" ${map.searchField == '1' ? "checked" : ""}/>
			</div>
			<div class="_web fl w70 align_c">
				<input id="web_${map.fieldName}" type="checkbox" value="${map.fieldId}" ${map.isWebShow == '1' ? "checked" : ""}/>
			</div>
			<div class="_gp fl w70 align_c">
				<input id="gp_${map.fieldName}" type="radio" name="isGrpField" value="${map.fieldId}" ${map.isReserved == 1 ? "disabled":"" } ${map.isGrpField == '1' ? "checked" : ""} ${(map.fieldType != 'radio' && map.fieldType != 'select')||tbType ==1 ? "disabled" : "" }/>
			</div>
		</div>
	</c:forEach>
	</div>
	<div id="item_relContent" class="rel_field">
		<c:forEach items="${itemFieldRelList}" var="map">
		<div id="rel_${map.fieldId}" class="h20 rel_row" onclick="selField(this.id)">
			<div class = "fl w120">
				<span id="relspan" class="h20 lineh20" title="${map.fieldDesc}">${fn:substring(map.fieldDesc,0,7)}<c:if test="${fn:length(map.fieldDesc)>7}">...</c:if></span>
			</div>
			<div class="_gl fl w70 align_c">
				<input id="gl_${map.fieldName}" type="checkbox" value="${map.fieldId}" ${map.inOutline == '1' ? "checked" : ""} ${(map.fieldType == 'editor' || map.fieldType == 'smeditor') ? "disabled" : "" }/>
			</div>
			<div class="_bt fl w70 align_c">
				<input id="bt_${map.fieldName}" type="radio" name="item_bt" value="${map.fieldId}" ${map.isReserved == 1 ? "disabled":"" } ${map.titleField == '1' ? "checked" : ""} ${(map.fieldType == 'editor' || map.fieldType == 'smeditor') ? "disabled" : "" }/>
			</div>
			<div class="_js fl w70 align_c">
				<input id="js_${map.fieldName}" type="checkbox" value="${map.fieldId}" ${map.searchField == '1' ? "checked" : ""}/>
			</div>
			<div class="_web fl w70 align_c">
				<input id="web_${map.fieldName}" type="checkbox" value="${map.fieldId}" ${map.isWebShow == '1' ? "checked" : ""}/>
			</div>
			<div class="_gp fl w70 align_c">
				<input id="gp_${map.fieldName}" type="radio" name="isGrpField" value="${map.fieldId}" ${map.isReserved == 1 ? "disabled":"" } ${map.isGrpField == '1' ? "checked" : ""} ${(map.fieldType != 'radio' && map.fieldType != 'select')||tbType ==1 ? "disabled" : "" }/>
			</div>
		</div>
	</c:forEach>
	</div>
</div>
	<form id="fieldRel"  method="post" action="appView.do?method=addEditViewField" class="pageForm required-validate"
	  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
	  	<input type="hidden" name="viewId" value="${viewId}"/>
	    <input type="hidden" name="tbType" value="${tbType}"/>
	    
	    <input id="mainTableId" type="hidden" name="mainTableId" value="${mainTableId }"/>
	    <input id="tableName" type="hidden" name="tableName" value=""/>
	    <input id="outLinefields" type="hidden" name="outLinefields" value=""/>
	    <input id="titleFields" type="hidden" name="titleFields" value=""/>
	    <input id="searchFields" type="hidden" name="searchFields" value=""/>
	    <input id="webFields" type="hidden" name="webFields" value=""/>
	    <input id="allFields" type="hidden" name="allFields" value=""/>
		<input id="isGrpField" type="hidden" name="isGrpField" value=""/>
	    <c:if test="${tbType == 1}">
		    <input id="itemTableId" type="hidden" name="itemTableId" value=""/>
		    <input id="itemTableName" type="hidden" name="itemTableName" value=""/>
		    <input id="itemOutLinefields" type="hidden" name="itemOutLinefields" value=""/>
		    <input id="itemTitleFields" type="hidden" name="itemTitleFields" value=""/>
		    <input id="itemSearchFields" type="hidden" name="itemSearchFields" value=""/>
		    <input id="itemWebFields" type="hidden" name="itemWebFields" value=""/>
		    <input id="itemAllFields" type="hidden" name="itemAllFields" value=""/>
		</c:if>
	</form>
	<div class="formBar" >
		<ul>
			<li style="width:200px;">
				<input id="sel_all" type="checkbox" onclick="selectAll(this);"/>全选
			</li>
			<li style="width:320px;">
				<div class="buttonActive">
					<div class="buttonContent">
						<button type="button" onclick="fieldSort(0);">上移</button>
					</div>
				</div>
				<div class="buttonActive">
					<div class="buttonContent">
						<button type="button" onclick="fieldSort(1);">下移</button>
					</div>
				</div>
			</li>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="stmRel()">保存</button></div></div></li>
			<li>
				<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
			</li>
		</ul>
	</div>
</div>
<script type="text/javascript">
  var _selids = new Array();//选择的字段id
  var _selgl = new Array();//选择的概览id
  var _seljs = new Array();//选择的检索id
  var _selweb = new Array();//选择的前台展现字段
  
  var item_selids = new Array();//选择的子表字段id
  var item_selgl = new Array();//选择的选择的子表概览id
  var item_seljs = new Array();//选择的选择的子表检索id
  var item_selweb = new Array();//选择的子表前台展现字段
  //刷新字段列表
  function refFeildList(_tableId, _itemTableId){
       //$("#relContent").html('');
       //$("#item_relContent").html('');
       var _url = 'appView.do?method=refFeildList&rel=relContent&tableId='+_tableId;
  	   $("#fContent").loadUrl(_url,"", function(){$("#relContent").html('');setSysField();});
       if(_itemTableId != 0){
	   		_url = 'appView.do?method=refFeildList&rel=item_relContent&tableId='+_itemTableId;
	  	   $("#item_fContent").loadUrl(_url, "", function(){$("#item_relContent").html('');setSysField();});
       }
       //setSysField();
  }
  //添加删除视图字段
  function showRel(_rel,_fid,_name,_desc,_type){
     var _id = '#'+_fid;
     var _cheched = $(_id).attr("checked") ;
 	 if(_cheched=='checked'){//选中后向数组里添加id
 	    addRel(_rel,_fid,_name,_desc, 0,_type);
 	 }else{
 	 	delRel(_fid);
 		$("#sel_all").attr("checked",false);
 	 }
 	 disabledTableRadio();
 	 setSelBtn();
  }
  //添加映射方法
  function addRel(_rel,_id,_name,_desc, _isSys,_type){
	  //alert(_id);
  	var _rel_fid = "rel_"+_id;
  	if($('#'+_rel+' #'+_rel_fid).html() != null){
		return;
  	}
  	var _bt = '_bt';
  	if(_rel == 'item_relContent'){
  		_bt = 'item_bt'
  	}
  	var fieldDesc = _desc;
  	if(fieldDesc.length > 7){
  		fieldDesc = _desc.substring(0, 7) + "...";
  	}
   	var _html = '<div id="'+_rel_fid+'" class="h20 rel_row" onclick="selField(this.id)">'
		+'<div class = "fl w120">'
			+'<span id="relspan" class="h20 lineh20" title=' + _desc + '>'+fieldDesc+'</span>'
		+'</div>'
		+'<div class="_gl fl w70 align_c">'
			+ '<input id="gl_'+_name+'" type="checkbox" value="'+_id+'"/>'
		+'</div>'
		+'<div class="_bt fl w70 align_c">'
			+'<input id="bt_'+_name+'" type="radio" name="' + _bt + '" value="'+_id+'"/>'
		+'</div>'
		+'<div class="_js fl w70 align_c">'
			+'<input id="js_'+_name+'" type="checkbox"  value="'+_id+'"/>'
		+'</div>'
		+'<div class="_web fl w70 align_c">'
			+'<input id="web_'+_name+'" type="checkbox"  value="'+_id+'"/>'
		+'</div>'
		+'<div class="_gp fl w70 align_c">'
			+'<input id="gp_'+_name+'" type="radio" name="isGrpField" value="'+_id+'"/>'
		+'</div>'
	+'</div>';
	var relID = '#' + _rel; 
	$(relID).prepend(_html);
	if(_isSys == 1){
		$(relID + ' #bt_' + _name).attr('disabled',true);
	}
	$(relID + ' #gl_CRTIME').attr('checked','checked').attr('disabled',true);
	$(relID + ' #bt_CRTIME').attr('disabled',true);

	$(relID + ' #gl_ISPUBLIC').attr('checked','checked').attr('disabled',true);
	$(relID + ' #bt_ISPUBLIC').attr('disabled',true);
	
	$(relID + ' #gl_STATUS').attr('disabled',true);
	$(relID + ' #bt_STATUS').attr('disabled',true);
	$(relID + ' #js_STATUS').attr('disabled',true);
	if(_type == 'editor' || _type == 'smeditor'){//大文本字段不可以作为概览字段或者标题字段
		//alert($('#'+_rel+' #'+_rel_fid + ' div._gl').html());
		$('#'+_rel+' #'+_rel_fid + ' div._gl input').attr('disabled',true);
		$('#'+_rel+' #'+_rel_fid + ' div._bt input').attr('disabled',true);
	}
	if(_type != 'radio' && _type != 'select'){//大文本字段不可以作为概览字段或者标题字段
		//alert($('#'+_rel+' #'+_rel_fid + ' div._gl').html());
		$('#'+_rel+' #'+_rel_fid + ' div._gp input').attr('disabled',true);
	}
  }
  //删除映射
  function delRel(_id){
  	var _rleid = "#rel_"+_id;
  	$(_rleid).remove();
  }
  
  //获取映射id,选择已选择过的字段
  function selectSelField(){
     //var _seltem = new Array();
     $('._gl input').each(function(){
        var _id = $(this).val();
     	//_seltem.push(_id);
    	$('#'+_id).attr('checked','checked');
     });
    /*for(i=0;i<_seltem.length;i++){
    	var _fid = '#'+_seltem[i];
    	$(_fid).attr('checked','checked');
    }*/
  }
  //全选
  function selectAll(element){
	  var currTab = $('.tabsContent div:visible');
   	  var inputObj = currTab.find('input');
	  if(element.checked){
	   	  inputObj.attr('checked','checked');
	   	  inputObj.each(function(){
		   	  if(currTab.attr('id') == 'fContent'){
		   		addRel('relContent',$(this).attr('id'),$(this).attr('title'),$(this).val(),0);
		   	  }else{
		   		addRel('item_relContent',$(this).attr('id'),$(this).attr('title'),$(this).val(),0);
		   	  }
		  });
	  }else{
		  inputObj.each(function(){
			  var _issys = $(this).attr("issys");
			  if(_issys != '1'){
			  	$(this).attr('checked', false);
			  	delRel($(this).attr('id'));
			  }
		  });
	  }
	  disabledTableRadio();
  }
  //根据已选字段判断设置全选按钮
  function setSelBtn(){
   	  var flag = true;
   	  $('.tabsContent div:visible').find('input').each(function(){
  	  	//alert($(this).attr('checked'));
	  	if($(this).attr('checked') != 'checked'){
			flag = false;
			return;
	  	}
  	  });
  	  //alert(flag);
  	  if(flag){
  		$("#sel_all").attr("checked",'checked');
  	  }else{
  		$("#sel_all").attr("checked",false);
  	  }
  }
  //禁用元数据选择
  function disabledTableRadio(){
	  //var n = $('#fContent input[type=checkbox]:checked').length;
	  var n = 0;
	  $('div.tabsContent').find('input').each(function(){
        var _issys = $(this).attr("issys");
        //alert(_issys);
        if(_issys != '1' && $(this).attr('checked') == 'checked'){
            n = n + 1;
        	//$(this).attr('checked', false);
        }
	  });
	  //alert(n);
	  var mainTableId = $('#mainTableId').val();
	  if(n > 0 || mainTableId != '0'){
		$('#boxmeta input[type = radio]').attr('disabled',true);
	  } else {
		$('#boxmeta input[type = radio]').attr('disabled',false);
	  }
  }
  //遍历所有系统预留字段，添加到右侧视图字段
  function setSysField(){
	  //alert();item_fContent
     $('#fContent input').each(function(){
        var _id = $(this).attr("id");
        var _name = $(this).attr('title');
        var _desc = $(this).val();
        var _issys = $(this).attr("issys");
        //alert(_id);
        if(_issys == '1'){
        	addRel('relContent',_id,_name, _desc, 1);
        }
     });
     if($('#item_fContent').html() != null){
    	 $('#item_fContent input').each(function(){
   	        var _id = $(this).attr("id");
   	        var _name = $(this).attr('title');
   	        var _desc = $(this).val();
   	        var _issys = $(this).attr("issys");
   	        //alert(_id);
   	        if(_issys == '1'){
   	        	addRel('item_relContent',_id,_name, _desc, 1);
   	        }
   	     });
     }
  }
  //切换视图字段flag=0显示主表flag=1显示从表
  function changeRelField(flag){
	 if(flag == 0){
  		$('#item_relContent').hide();
		$('#relContent').show();
	 } else {
	 	$('#relContent').hide();
		$('#item_relContent').show();
	 }
	 setSelBtn();
  }
  //获取设置的视图字段编号，添加到对应的数组中去。
  function setRelIds(){
     $('#relContent ._gl input').each(function(){
        var _id = $(this).val();
     	_selids.push(_id);
     	var _cheched = $(this).attr("checked"); 
 		if(_cheched=='checked'){//选中后向数组里添加i
 			_selgl.push(_id);
 		}
     });
     $('#relContent ._js input').each(function(){
     	var _cheched = $(this).attr("checked"); 
     	var _id = $(this).val();
 		if(_cheched=='checked'){//选中后向数组里添加i
 			_seljs.push(_id);
 		}
     });
     $('#relContent ._web input').each(function(){
      	var _cheched = $(this).attr("checked"); 
      	var _id = $(this).val();
  		if(_cheched=='checked'){//选中后向数组里添加i
  			_selweb.push(_id);
  		}
      });
     if($('#item_relContent').html() != null){
         $('#item_relContent ._gl input').each(function(){
            var _id = $(this).val();
            item_selids.push(_id);
          	var _cheched = $(this).attr("checked"); 
      		if(_cheched=='checked'){//选中后向数组里添加i
      			item_selgl.push(_id);
      		}
          });
          $('#item_relContent ._js input').each(function(){
          	var _cheched = $(this).attr("checked"); 
          	var _id = $(this).val();
      		if(_cheched=='checked'){//选中后向数组里添加i
      			item_seljs.push(_id);
      		}
          });
          $('#item_relContent ._web input').each(function(){
           	var _cheched = $(this).attr("checked"); 
           	var _id = $(this).val();
       		if(_cheched=='checked'){//选中后向数组里添加i
       			item_selweb.push(_id);
       		}
          });
     }
  }
  //设置视图字段值
  function stmRel(){
	//先清空数组
	_selids = new Array();//选择的字段id
	_selgl = new Array();//选择的概览id
	_seljs = new Array();//选择的检索id
	_selweb = new Array();//选择的前台展现字段
	  
	item_selids = new Array();//选择的子表字段id
	item_selgl = new Array();//选择的选择的子表概览id
	item_seljs = new Array();//选择的选择的子表检索id
	item_selweb = new Array();//选择的子表前台展现字段
    
    setRelIds();
  	var _glids = _selgl.toString();
  	if(!_glids){
  		alertMsg.info('请选择主表概览字段！');
  		return;
  	}
  	//获取主题id
  	var _bt = $('input[name=_bt]:checked').val();
  	
  	//if(_bt==undefined) _bt="";
  	if(_bt == undefined || _bt == ''){
  		alertMsg.info('请选择主表标题字段！');
		return;
  	}
  	
  	
    var _tableId = $('input[name=tableid]:checked').val();
    var _tableName = $('input[name=tableid]:checked').attr("title");
  	var item_tableId = $('input[name=tableid]:checked').attr("itemTableId");
  	var item_tableName = $('input[name=tableid]:checked').attr("itemTableName");
    $('#mainTableId').val(_tableId);
    $('#tableName').val(_tableName);
    $('#allFields').val(_selids.toString());
    $('#outLinefields').val(_selgl.toString());
    $('#searchFields').val(_seljs.toString());
    $('#webFields').val(_selweb.toString());
    $('#isGrpField').val($('input[name=isGrpField]:checked').val());
    $('#titleFields').val(_bt);
    if(item_tableId != '0'){
        var item_glids = item_selgl.toString();
    	if(!item_glids){
      		alertMsg.info('请选择从表概览字段！');
      		return;
      	}
    	_bt = $('input[name=item_bt]:checked').val();
    	if(_bt == undefined || _bt == ''){
      		alertMsg.info('请选择从表标题字段！');
    		return;
      	}
    	$('#itemTableId').val(item_tableId);
  	    $('#itemTableName').val(item_tableName);
  	    $('#itemAllFields').val(item_selids.toString());
  	    $('#itemOutLinefields').val(item_glids);
  	    $('#itemSearchFields').val(item_seljs.toString());
  		$('#itemWebFields').val(item_selweb.toString());
  	    $('#itemTitleFields').val(_bt);
    }
	$('#fieldRel').submit();
  }
  //排序
  function fieldSort(sort){
	var currRow = $('div.rel_field:visible div.sel_div');
	if(currRow.html() != null){
		if(sort == 0){
			currRow.prev().before(currRow);
		} else {
			currRow.next().after(currRow);
		}
		//currRow.remove();
	}
  }
  //选择字段
  function selField(id){
  	 $('div.rel_field:visible div').removeClass('sel_div');
	 $('#' + id).addClass('sel_div');
  }
  //初始化列表
  function init(){
     $('input[name=tableid]').first().attr('checked','checked');
	 setSysField();
     selectSelField();
     disabledTableRadio();
     changeRelField(0);
  }
  init();
  $(function(){
	 $('div.rel_field').each(function(){
		 var relID = '#' + $(this).attr('id');
		 $(relID + ' #gl_CRTIME').attr('checked','checked').attr('disabled',true);
		 $(relID + ' #bt_CRTIME').attr('disabled',true);

		 $(relID + ' #gl_ISPUBLIC').attr('checked','checked').attr('disabled',true);
		 $(relID + ' #bt_ISPUBLIC').attr('disabled',true);
		
		 $(relID + ' #gl_STATUS').attr('disabled',true);
		 $(relID + ' #bt_STATUS').attr('disabled',true);
		 $(relID + ' #js_STATUS').attr('disabled',true);
	 });
	
  });
</script>
