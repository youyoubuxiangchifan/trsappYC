<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pageContent">
	<form name="fieldForm"  method="post" action="meta.do?method=addFiled" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,dialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>选择元数据表：</label>
				<input type="radio" name="tableId" checked onclick="changeStyle(0);$('#tableName').val('${tableName}');" value="${tableId}"/>主表
				<c:if test="${itemTableId > 0}">
					<input type="radio" name="tableId" onclick="changeStyle(1);$('#tableName').val('${itemTableName}');" value="${itemTableId}"/>从表
				</c:if>
				<input type="hidden" id="tableName" name="tableName" value="${tableName}"/>
			</p>
			<p>
				<label>显示名称：</label>
				<input type="text" id="fieldDesc" name="fieldDesc" style="width:150px;"
    				 maxlength="50" class="required"
    				 value=""></input>
			</p>

			<p>
				<label>字段名：</label>
				<input type="text" id="fieldName" name="fieldName" style="width:150px;"
    				 minlength="3" maxlength="20"  class="required alphanumeric"
    				 value=""></input>
			</p>
			<p>
				<label>字段类型：</label>
				<select id="fieldType" name="fieldType" style="width:155px;margin-top:2px;" onchange="crtInput(this.value)">
    					<option value="text">文本</option>
    					<option value="textarea">多行文本</option>
    					<option value="password">密码文本</option>
    					<option value="hidden">隐藏文本</option>
    					<option value="editor">复杂编辑器</option>
    					<option value="smeditor">简易编辑器</option>
    					<option value="date">日期</option>
    					<option value="select">下拉框</option>
    					<option value="checkbox">多选框</option>
    					<option value="radio">单选按钮</option>
    					<option value="number">数值</option>
    					<option value="number,digits">整数</option>
    					<option value="text,email">邮箱地址</option>
    					<option value="text,url">web地址</option>
    					<option value="text,mobile">手机号码</option>
    					<option value="text,tel">电话号码</option>
    					<option value="text,phone">手机、电话</option>
    					<option value="text,zipCode">邮政编码</option>
    					<option value="text,idCardNo">身份证号</option>
    			</select>
			</p>
			<p id="enume"  style="display:none;width:420px;">
				<label>枚举数值：</label>
				<input type="text" id="enmValue" name="enmValue" style="width:150px;"
    				 maxlength="200" value="" ondblclick=addEnumValue()></input>
    			<span style="color:red;">枚举格式为value~value，如：男~女</span>
			</p>
			<p>
				<label>字段长度：</label>
				<input type="text" id="dbLength" name="dbLength" style="width:150px;"
    				 maxlength="50" class="digits" min="1" max="4000" value="200"></input>
			</p>
			<p>
				<label>所属类别：</label>
    				 <select id="fieldStyle" name="fieldStyle" style="width:155px;margin-top:2px;">
    					<%--<option value="-1">--请选择--</option>
    					--%><option value="0">提交类型</option>
   						<option value="1">回复类型</option>
   						<option value="2">主题类型</option>
    				</select>
			</p>
			<p>
				<label>字段默认值：</label>
				<input type="text" id="defaultValue" name="defaultValue" style="width:150px;"
    				 maxlength="50" value=""></input>
			</p>
			<p>
				<label>&nbsp;&nbsp;</label>
				<input type="checkbox" name="notNull" value="1"/>不能为空
				<input type="checkbox" name="notEdit" value="1"/>不能编辑
				<!--  
				<input type="checkbox" name="hiddenField" value="1"/>隐藏字段
				-->
			</p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="smtForm()">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
		
	</form>
	
</div>
 <script type="text/javascript">
 
 	    //隐藏枚举值
 		function crtInput(_value){
 			if(_value=='checkbox' || _value=='select' || _value=='radio'){
 				$('#enume').show();
 				$('#defaultValue').attr('readonly','readonly');
 				$('#defaultValue').addClass('readonly');
 			}else{
 				$('#enume').hide();
 				$('#defaultValue').attr('readonly',false);
 				$('#defaultValue').removeClass('readonly');
 			}
 			crtDefLen(_value);
 		}
 		 //禁用长度
 		function crtDefLen(_value){
 		    //禁止非文本字段
 		    if(_value=='smeditor' || _value=='editor' || _value=='date'){
 		    	  $('#dbLength').val('');
 		          $('#dbLength').attr('readonly','readonly');
 		          $('#dbLength').addClass('readonly');
 		    }else if(_value=='number'){
 				$('#dbLength').val(5);
 			}else{
 		    	$('#dbLength').val('200');
		        $('#dbLength').attr('readonly',false);
		        $('#dbLength').removeClass('readonly');
 		    	
 		    } 
 		}
 		//添加枚举值
 		function addEnumValue(){
 			var options = {
 				width:400,
 				height:300
 			}
 			var _value = $('#enmValue').val();
 			var _url = "${basePath}appadmin/metadata/enum_create.jsp?enmValue="+_value;
 			$.pdialog.open(_url,"d_enum", "添加/修改枚举值", options);　
 		}
 		//校验字段 提交form
 		function smtForm(){
 			var _fieldName = $('#fieldName').val();
 			if(_fieldName==''){
 				$('form[name=fieldForm]').submit();
 				return;
 			}
 			var _url = "meta.do?method=checkField&tableId=${tableId}";
			var _data = {
				fieldName : _fieldName
			}
			
			$.post(_url,_data,function(result){
    					if(result=='true'){
    						alertMsg.info('字段名称不唯一,请修正！');
    						$('#fieldName').focus();
    					}else{
    						$('form[name=fieldForm]').submit();
    					}
  			});
 		}
 		function changeStyle(_type){
 	 		var isTheme = '${itemTableId == 0 ? 0 : 1}';
 	 		var fieldStyleSel = $('#fieldStyle');
 	 		fieldStyleSel.empty();
 	 		//alert(isTheme);
 	 		//fieldStyleSel.append("<option value='-1'>--请选择--</option>");
			if(_type == '0'){
				if(isTheme == '0'){
					fieldStyleSel.append("<option value='0'>提交类型</option><option value='1'>回复类型</option>");
				}else{
					fieldStyleSel.append("<option value='2'>主题类型</option>");
					//$('#fieldStyle').find('option').eq(2).show().siblings().hide();
				}
			}else{
				fieldStyleSel.append("<option value='0'>提交类型</option>");
				//$('#fieldStyle').find('option').eq(0).show().siblings().hide();
			}
			fieldStyleSel.find("option[value='${field.fieldStyle}']").attr("selected",true);
 		}
 		changeStyle(0);
 	</script>