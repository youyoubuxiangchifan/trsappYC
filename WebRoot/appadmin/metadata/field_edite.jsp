<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pageContent">
	<form name="roleForm"  method="post" action="meta.do?method=editeFiled" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>显示名称：</label>
				<input type="text" id="fieldDesc" name="fieldDesc" style="width:150px;"
    				 maxlength="50" class="required"
    				 value="${field.fieldDesc}"></input>
			</p>

			<p>
				<label>字段名：</label>
				<input type="text" id="fieldName" name="fieldName" style="width:150px;"
    				 minlength="3" maxlength="50" readonly="readonly" class="alphanumeric"
    				 value="${field.fieldName}"></input>
			</p>
			<p>
				<label>字段类型：</label>
				<select id="fieldType" name="fieldType"  disabled="disabled" style="width:155px;margin-top:2px;">
    					<option value="text" <c:if test="${fieldType == 'text'}">selected="selected"</c:if>>文本</option>
    					<option value="textarea" <c:if test="${fieldType == 'textarea'}">selected="selected"</c:if>>多行文本</option>
    					<option value="password" <c:if test="${fieldType == 'password'}">selected="selected"</c:if>>密码文本</option>
    					<option value="hidden" <c:if test="${fieldType == 'hidden'}">selected="selected"</c:if>>隐藏文本</option>
    					<option value="editor" <c:if test="${fieldType == 'editor'}">selected="selected"</c:if>>复杂编辑器</option>
    					<option value="smeditor" <c:if test="${fieldType == 'smeditor'}">selected="selected"</c:if>>简易编辑器</option>
    					<option value="date" <c:if test="${fieldType == 'date'}">selected="selected"</c:if>>日期</option>
    					<option value="select" <c:if test="${fieldType == 'select'}">selected="selected"</c:if>>下拉框</option>
    					<option value="checkbox" <c:if test="${fieldType == 'checkbox'}">selected="selected"</c:if>>多选框</option>
    					<option value="radio" <c:if test="${fieldType == 'radio'}">selected="selected"</c:if>>单选按钮</option>
    					<option value="number" <c:if test="${fieldType == 'number'}">selected="selected"</c:if>>数值</option>
    					<option value="number,digits" <c:if test="${fieldType == 'number,digits'}">selected="selected"</c:if>>整数</option>
    					<option value="text,email" <c:if test="${fieldType == 'text,email'}">selected="selected"</c:if>>邮箱地址</option>
    					<option value="text,url" <c:if test="${fieldType == 'text,url'}">selected="selected"</c:if>>web地址</option>
    					<option value="text,mobile" <c:if test="${fieldType == 'text,mobile'}">selected="selected"</c:if>>手机号码</option>
    					<option value="text,tel" <c:if test="${fieldType == 'text,tel'}">selected="selected"</c:if>>电话号码</option>
    					<option value="text,phone" <c:if test="${fieldType == 'text,phone'}">selected="selected"</c:if>>手机、电话</option>
    					<option value="text,zipCode" <c:if test="${fieldType == 'text,zipCode'}">selected="selected"</c:if>>邮政编码</option>
    					<option value="text,idCardNo" <c:if test="${fieldType == 'text,idCardNo'}">selected="selected"</c:if>>身份证号</option>
    			</select>
			</p>
			<p id="enume" style="display:none;width:480px;">
				<label>枚举数值：</label>
				<input type="text" id="enmValue" name="enmValue" style="width:150px;"
    				 maxlength="200" ondblclick=addEnumValue()
    				 value="${field.enmValue}"></input>
    			<span style="color:red;font-size:10px;">双击打开枚举值构造页面，枚举格式为value~value，如：男~女</span>
			</p>
			<p>
				<label>字段长度：</label>
				<input type="text" id="dbLength" name="dbLength" style="width:150px;"
    				 maxlength="50"  min="1" max="4000" class="digits"  value="${field.dblength}"></input>
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
    				 maxlength="50" value="${field.defaultValue}"></input>
			</p>
			<p>
				<label>&nbsp;&nbsp;</label>
				<input type="checkbox" name="notNull" value="1" <c:if test="${field.notNull== '1'}">checked="checked"</c:if>/>不能为空
				<input type="checkbox" name="notEdit" value="1" <c:if test="${field.notEdit== '1'}">checked="checked"</c:if>/>不能编辑
				<!-- 
				<input type="checkbox" name="hiddenField" value="1" <c:if test="${field.hiddenField== '1'}">checked="checked"</c:if>/>隐藏字段
				-->
			</p>
			<input type="hidden" name="id" value="${field.fieldId}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
	
</div>
 <script type="text/javascript">
 		 //禁用长度
 		function crtDefLen(_value){
 		    //禁止非文本字段
 		    if(_value=='editor' || _value=='date'){
 		          $('#dbLength').attr('readonly','readonly');
 		          $('#dbLength').attr('class','readonly');
 		    } 
 		}
 	    //隐藏枚举值
 		function showEnum(_value){
 			if(_value=='checkbox' || _value=='select' || _value=='radio'){
 				$('#enume').show();
 				$('#defaultValue').attr('readonly','readonly');
 				$('#defaultValue').attr('class','readonly');
 			}
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
 		showEnum('${field.fieldType}');
 		crtDefLen('${field.fieldType}');
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