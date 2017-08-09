<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pageContent">
	<form name="viewForm"  method="post" action="appView.do?method=${method}" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,myDialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>视图名称：</label>
				<input type="text" id="viewName" name="viewName" style="width:150px;" <c:if test="${view.viewName != null}">readonly="readonly"</c:if>
    				 maxlength="50" class="required"
    				 value="${view.viewName}"></input>
			</p>
			<p>
				<label>是否需要主题：</label>
				<input type="radio" name="isNeedTheme" value="0" ${view.viewId > 0 && not empty view.mainTableName ? "disabled" : "" } <c:if test="${view.isNeedTheme != '1'}">checked="checked"</c:if> onclick="hiddeElem(this.value)"></input>否	 
				<input type="radio" name="isNeedTheme" value="1" ${view.viewId > 0 && not empty view.mainTableName ? "disabled" : "" } <c:if test="${view.isNeedTheme == '1'}">checked="checked"</c:if> onclick="hiddeElem(this.value)"></input>是
			</p>
			<p>
				<label>应用状态：</label>
				<input type="radio" name="appStatus" value="0" <c:if test="${view.appStatus != '1'}">checked="checked"</c:if>></input>打开
    			<input type="radio" name="appStatus" value="1" <c:if test="${view.appStatus == '1'}">checked="checked"</c:if>></input>关闭	 
			</p>
			<p>
				<label>是否公开信息：</label>
				<input type="radio" name="isPublic" value="0" checked="checked"></input>处理后公开
    			<input type="radio" name="isPublic" value="1" <c:if test="${view.isPublic == '1'}">checked="checked"</c:if>></input>不公开
    			<input type="radio" name="isPublic" value="2" <c:if test="${view.isPublic == '2'}">checked="checked"</c:if>></input>直接公开	 	 
			</p>
			<p id="isHasQueryNo">
				<label>是否有查询码：</label>
				<input type="radio" name="isHasQueryNo" value="0" <c:if test="${view.isHasQueryNo != '1'}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isHasQueryNo" value="1" <c:if test="${view.isHasQueryNo == '1'}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label>是否有评论：</label>
				<input type="radio" name="isHasComment" value="0" checked="checked" ></input>否
    			<input type="radio" name="isHasComment" value="1" <c:if test="${view.isHasComment == 1}">checked="checked"</c:if>></input>私有评论
    			<input type="radio" name="isHasComment" value="2" <c:if test="${view.isHasComment == 2}">checked="checked"</c:if>></input>共有评论
			</p>
			<p>
				<label>是否邮件预警：</label>
				<input type="radio" name="isEmailWarn" value="0" <c:if test="${view.isEmailWarn != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isEmailWarn" value="1" <c:if test="${view.isEmailWarn == 1}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label>是否短信预警：</label>
				<input type="radio" name="isSmsWarn" value="0" <c:if test="${view.isSmsWarn != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isSmsWarn" value="1" <c:if test="${view.isSmsWarn == 1}">checked="checked"</c:if>></input>是
			</p>
			
			<p>
				<label>是否短信提醒：</label>
				<input type="radio" name="isSmsRemind" value="0" <c:if test="${view.isSmsRemind != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isSmsRemind" value="1" <c:if test="${view.isSmsRemind == 1}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label class="w160">是否有提交说明：</label>
				<input type="radio" name="isHasSmtDesc" value="0" <c:if test="${view.isHasSmtDesc != 1}">checked="checked"</c:if> onclick="hiddeDescHref(this.value)"></input>否
    			<input type="radio" name="isHasSmtDesc" value="1" <c:if test="${view.isHasSmtDesc == 1}">checked="checked"</c:if> onclick="hiddeDescHref(this.value)"></input>是
				<a id="descHref" style="height:21px;line-height:21px;" href="appView.do?method=getAppSmtDesc&viewId=${view.viewId}" target="dialog" title="提交说明" rel="t_smtDesc" mask="true" width="850" height="450">
			    	点击选择
			    </a>
			</p>
			<p>
				<label>设置办理超期时限：</label>
				<input type="number" id="limitDayNum" name="limitDayNum" style="width:150px;" value="${view.limitDayNum}"/>
			</p>
			<%--<p>
				<label>是否前台显示组织：</label>
				<input type="radio" name="isShowGroup" value="0" <c:if test="${view.isShowGroup != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isShowGroup" value="1" <c:if test="${view.isShowGroup == 1}">checked="checked"</c:if>></input>是
			</p>
			--%><p  id="isSupAppendix">
				<label>是否支持附件上传：</label>
				<input type="radio" name="isSupAppendix" value="0" <c:if test="${view.isSupAppendix != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isSupAppendix" value="1" <c:if test="${view.isSupAppendix == 1}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label>是否推送到WCM：</label>
				<input id="isPush" type="checkbox" name="isPush" value="1" <c:if test="${view.isPush == 1}">checked</c:if>></input>是
			</p>
			<p>
				<label class="w160">WCM文档类型：</label>
				<input type="radio" name="wcmDocType" value="0" <c:if test="${view.wcmDocType != 1}">checked="checked"</c:if>></input>普通文档
    			<input type="radio" name="wcmDocType" value="1" <c:if test="${view.wcmDocType == 1}">checked="checked"</c:if>></input>元数据
			</p>
			<p>
			    <label>WCM栏目ID：</label>
			    <input id="wcmChnlId" type="text" name="wcmChnlId" value="${view.wcmChnlId}"></input>
			</p>
			<p>
			    <label class="w160">列表页地址：</label>
			    <input id="listAddr" type="text" name="listAddr" style="width:200px;" value="${view.listAddr}"></input>
			</p>
			<p>
			    <label class="w160">办理页地址：</label>
			    <input id="dowithAddr" type="text" name="dowithAddr" style="width:200px;" value="${view.dowithAddr}"></input>
			</p>
			<p>
			    <label>同步创建到：</label>
			    <a style="height:21px;line-height:21px;" href="appView.do?method=orgDialog&pid=0&groupType=0" target="dialog" mask="true" width="400" height="500" title="添加组织" rel="org_dialog">点击选择组织（部门）</a>
			</p>
			<input type="hidden" name="viewId" value="${view.viewId}"></input>
			<input id="groupIds" type="hidden" name="groupIds" value="${groupIds}"></input>
			<input type="hidden" id="appSmtDesc" name="smtDesc" value=""></input>
			<input type="hidden" id="isEditeDesc" name="isEditeDesc" value="${view.isHasSmtDesc}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="stmView('${view.viewName}')">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
	
</div>
 <script type="text/javascript">
 		//检查推送栏目id
        function viladChnl(){
           if($('#isPush').attr('checked')== 'checked' && !$('#wcmChnlId').val()){
           		 alertMsg.info('请选择要同步的WCM栏目id');
           		 return false;
           }
           return true;
        }
		function stmView(_oldName){
		    if(!viladChnl())
		    	return;
		    var _gids = $('#groupIds').val();
		    if(!_gids){
		           alertMsg.info('组织不能为空,请选择组织！');
		           return;
		    }
		    var isHasSmtDesc = $('input[name=isHasSmtDesc]:checked').val();
			if(isHasSmtDesc == '1' && $('#isEditeDesc').val() != '1'){
				alertMsg.info('请添加应用提交说明!');
				return;
			}
		    var _viewName = $('#viewName').val();
		    if(!_viewName || _oldName==_viewName){
		    	$('form[name=viewForm]').submit();
		    	return;
		    }
			var _url = "appView.do?method=check";
			var _data = {
				viewName : _viewName
			}
			
			$.post(_url,_data,function(result){
    					if(result=='true'){
    						alertMsg.info('视图名称不唯一,请修正！');
    						$('#viewName').focus();
    					}else{
    						$('form[name=viewForm]').submit();
    					}
  			});
		}
		//如果是主题类型隐藏查询编码、附件
		function hiddeElem(_value){
			if(_value==1){
				$('#isSupAppendix').hide();
				$('#isHasQueryNo').hide();
			}else if(_value==0){
				$('#isSupAppendix').show();
				$('#isHasQueryNo').show();
			}
		}
		//控制提交应用说明的显示
		function hiddeDescHref(_value){
			if(_value==1){
				$('#descHref').show();
			}else if(_value==0){
				$('#descHref').hide();
				$('#isEditeDesc').val('');
			}
		}
		$(function(){
			var isHasSmtDesc = $('input[name=isHasSmtDesc]:checked').val();
			hiddeDescHref(isHasSmtDesc);
		});
 </script>

