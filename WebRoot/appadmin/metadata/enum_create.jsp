<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent" layoutH="30">
<div style="padding-left:5px;padding-top:5px;padding-bottom:5px;">按Enter增加新的枚举值</div>
	<div>
		<ol id="containerInner">
			
		</ol>
	</div>
   
    
   </div>
   <div class="formBar">
			<ul>
				<li><div class="button"><div class="buttonContent"><button type="button" onclick="setValue()">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
  </div>
 <script type="text/javascript">
 	 var aResult = []; //枚举值集合
     var _defValue = '';//默认值
    function addRow(labelValue){
        if(labelValue==undefined)
             labelValue='';
        var _html = '<li class="row">'
            +'<span style="line-height:20px;margin-left:5px;margin-top:0">描述：</span>'
            +'<span><input type="radio" name="defaut_value"/></span>'
            +'<span><input type="text" name="enumLabel" value="'+labelValue+'" class="label textInput" onkeydown="dealEnter();"></span>'
            +'<span style="line-height:20px;margin-left:5px;cursor:hand;" onclick="delRow(this)"><img src="${basePath}appadmin/images/delbtn.gif"></img></span>'
        +'</li>';
    	$('#containerInner').append(_html);
    	
    }
    
    function delRow(element){
    	var inputs = $("#containerInner input[name=enumLabel]");
    	if(inputs.length<2){
    	   alertMsg.info('枚举值不能为空！');
    	   return;
    	}
        var rootRow = document.getElementById('containerInner');
    	var row =  element.parentNode;
    	rootRow.removeChild(row);
    }
    function dealEnter(){
    	if(event.keyCode == 13){addRow();};
    	$("input[name=enumLabel]:last").focus();
    }
     function setValue(){
        var values = getResult();
        if(values=='false'){
        	return;
        }
        $('#defaultValue').val(_defValue);
        $('#enmValue').val(aResult.join("~"));
        $.pdialog.closeCurrent(); 
     }
    function getResult(){
       
        var isRight = true;
        var i=0;
        var _defIndex = 0;
        $("#containerInner input").each(function(){
            if(i%2==0){
               var ischeck = $(this).attr('checked');
               if(ischeck=='checked')
                 _defIndex = i+1;
            }else{
        		var sLabel = $(this).val();
				if(!sLabel){
					alertMsg.info('描述不能为空.');
					isRight = false;
				}
				var reg = /~|`/ig;

				if(sLabel.match(reg)){
				    alertMsg.info("描述或值不能包含特殊字符：~` 。");
					isRight = false;
				}
				if(i==_defIndex)
				    _defValue = sLabel;
				aResult.push(sLabel);
			 }
				i++;
        });
        if(!isRight)
            return 'false';
       // return aResult.join("~");
    }
    //初始化枚举的值
    function init() {
    		var _params = $('#enmValue').val();
 			if(_params==undefined || _params==""){
 				addRow('');
 			}else{
        		var enumValue = _params.split("~");
        		for (var i = 0; i < enumValue.length; i++){
            		
            		var sLabel = enumValue[i];
            		if(sLabel == null){
                		alertMsg.info("分析枚举值时发现枚举值不合法" + "[" + enumValue + "]");
                		break;
            		}
            		addRow(sLabel);
            	}
            }
            $("input[name=enumLabel]").first().focus();
 	}
 	 init();
 </script>