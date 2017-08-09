<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form id="vFieldForm" name="vFieldForm"  method="post" action="appView.do?method=editField" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>显示名称：</label>
				<input type="text" id="fieldDesc" name="fieldDesc" style="width:150px;"
    				 maxlength="50"
    				 value="${field.fieldDesc}"></input>
			</p>

			<p>
				<label>字段名：</label>
				<input type="text" id="fieldName" name="fieldName" style="width:150px;" readonly="readonly"
    				 minlength="3" maxlength="50" value="${field.fieldName}"></input>
			</p>
			
			<p>
				<label>字段类型：</label>
				<select id="fieldType" name="fieldType" disabled="true" onchange="setDefLen(this.value,'change')" style="width:155px;margin-top:2px;">
    					<option value="text" <c:if test="${field.fieldType == 'text'}">selected="selected"</c:if>>文本</option>
    					<option value="textarea" <c:if test="${field.fieldType == 'textarea'}">selected="selected"</c:if>>多行文本</option>
    					<option value="password" <c:if test="${field.fieldType == 'password'}">selected="selected"</c:if>>密码文本</option>
    					<option value="hidden" <c:if test="${field.fieldType == 'hidden'}">selected="selected"</c:if>>隐藏文本</option>
    					<option value="editor" <c:if test="${field.fieldType == 'editor'}">selected="selected"</c:if>>复杂编辑器</option>
    					<option value="smeditor" <c:if test="${field.fieldType == 'smeditor'}">selected="selected"</c:if>>简易编辑器</option>
    					<option value="date" <c:if test="${field.fieldType == 'date'}">selected="selected"</c:if>>日期</option>
    					<option value="select" <c:if test="${field.fieldType == 'select'}">selected="selected"</c:if>>下拉框</option>
    					<option value="checkbox" <c:if test="${field.fieldType == 'checkbox'}">selected="selected"</c:if>>多选框</option>
    					<option value="radio" <c:if test="${field.fieldType == 'radio'}">selected="selected"</c:if>>单选按钮</option>
    					<option value="number" <c:if test="${field.fieldType == 'number'}">selected="selected"</c:if>>数值</option>
    			</select>
			</p>
			<p id="enume"  style="display:none; width:480px;">
				<label>枚举数值：</label>
				<input type="text" id="enmValue" name="enmValue" style="width:150px;"
    				 maxlength="200" value="${field.enmValue}" ondblclick=addEnumValue()></input>
				<span style="color:red;font-size:10px;">双击打开枚举值构造页面，枚举格式为value~value，如：男~女</span>
			</p>
			<p>
				<label>字段长度：</label>
				<input type="text" id="fieldLength" name="fieldLength" readonly style="width:150px;"
    				 maxlength="50" class="digits"   min="1" max="${field.fieldLength}"  value="${field.fieldLength}"></input>
			</p>
			<p>
				<label>所属类别：</label>
    				 <select id="fieldStyle" name="fieldStyle" style="width:155px;margin-top:2px;">
    				 	<%--<option value="-1">--请选择--</option>
    					--%><option value="0" <c:if test="${field.fieldStyle == '0'}">selected="selected"</c:if>>提交类型</option>
    					<option value="1" <c:if test="${field.fieldStyle == '1'}">selected="selected"</c:if>>回复类型</option>
    					<option value="2" <c:if test="${field.fieldStyle== '2'}">selected="selected"</c:if>>主题类型</option>
    				</select>
			</p>
			<p>
				<label>字段默认值：</label>
				<input type="text" id="defaultValue" name="defaultValue" style="width:150px;"
    				 maxlength="100" value="${field.defaultValue}"></input>
			</p>
			<p>
				<label>&nbsp;&nbsp;</label>
				<input type="checkbox" name="notNull" value="1" <c:if test="${field.notNull== '1'}">checked="checked"</c:if>/>不能为空
				<input type="checkbox" name="notEdit" value="1" <c:if test="${field.notEdit== '1'}">checked="checked"</c:if>/>不能编辑
				<%--<input type="checkbox" name="inDetail" value="1" <c:if test="${field.inDetail== '1'}">checked="checked"</c:if>/>细览显示
			--%></p>
			<input type="hidden" name="appFieldId" value="${field.appFieldId}"></input>
			<input type="hidden" name="isReserved" value="${field.isReserved}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button id="smtbut" type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
	
</div>
 <script type="text/javascript">
 	    //隐藏枚举值
 		function initEnum(_value,_isReserved){
 		    //禁止非文本字段
 		    if(_value=='editor' || _value=='date'){
 		          $('#fieldType').attr('disabled','disabled');
 		          $('#fieldLength').attr('readonly','readonly');
 		    } else if(_value=='number'){
 		    	$('#fieldType').attr('disabled','disabled');
 		    }else{//如果是文本字段，删除非文本字段
 		    	$('option[value=editor]').remove();
 		    	$('option[value=date]').remove();
 		    	$('option[value=number]').remove();
 		    }
 		    //如果是枚举值显示枚举值并禁用默认字段
 			crtEnum(_value,'');
 		}
 		function addEnumValue(){
 			var options = {
 			    mask : true,
 				width:400,
 				height:300
 			}
 			var _value = $('#enmValue').val();
 			var _url = "${basePath}appadmin/metadata/enum_create.jsp?enmValue="+_value;
 			$.pdialog.open(_url,"d_enum", "添加/修改枚举值", options);　
 		}
 		//设置默认长度
 		function setDefLen(_value){
 			crtEnum(_value);
 			if(_value=='smeditor')
 		    	$('#fieldLength').val('1000');
 		    else{
 		    	$('#fieldLength').val('200');
 		    }
 		}
 		//控制枚举及相关的默认值
 		function crtEnum(_value,_act){
 			if(_value=='checkbox' || _value=='select' || _value=='radio'){
 				$('#enume').show();
 				$('#defaultValue').attr('readonly','readonly');
 				$('#defaultValue').attr('class','readonly');
 				if(_act=='change'){
 					$('#defaultValue').val('');
 				}
 			}else{
 				$('#enume').hide();
 				$('#defaultValue').attr('readonly',false);
 				$('#defaultValue').attr('class','');
 			}
 		}
 		function crtIsReserved(_isReserved){
 		 	if(_isReserved=='1'){
 		 	    $('#vFieldForm select').attr('disabled','disabled');
 		 		//$('#vFieldForm input').attr('disabled','disabled');
 		 		$('#vFieldForm input[type=text]').attr('readonly','readonly').attr('class','readonly');
 		 		//$('#vFieldForm .buttonActive').hide();
 		 	}
 		}
 		initEnum('${field.fieldType}');
 		crtIsReserved('${field.isReserved}');
 		$(function(){
 	 		//判断表类型，更新字段类别下拉框
 	 		var tbType = '${tbType}';
 	 		//alert(tbType);
 	 		var fieldStyleSel = $('#fieldStyle');
 	 		fieldStyleSel.empty();
 	 		//alert(isTheme);
 	 		//fieldStyleSel.append("<option value='-1'>--请选择--</option>");
			if(tbType == '0'){
				fieldStyleSel.append("<option value='0'>提交类型</option><option value='1'>回复类型</option>");
			}else if(tbType == '1'){
				fieldStyleSel.append("<option value='2'>主题类型</option>");
				//$('#fieldStyle').find('option').eq(0).show().siblings().hide();
			}else{
				fieldStyleSel.append("<option value='0'>提交类型</option>");
			}
			fieldStyleSel.find("option[value='${field.fieldStyle}']").attr("selected",true);  
 		});
 	</script>