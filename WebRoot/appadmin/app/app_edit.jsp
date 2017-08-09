<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<style type="text/css">
label.w160{
	 width:160px;
}
</style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent">
	<form name="appForm"  method="post" action="appView.do?method=editApp" class="pageForm required-validate"
  	onsubmit="return validateCallback(this,myDialogAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<p>
				<label class="w160">应用名称：</label>
				<input type="text" id="appName" name="appName" style="width:150px;" readonly="readonly"
    				 maxlength="50" class="required" value="${app.appName}"></input>
			</p>
			<p>
				<label class="w160">是否需要主题：</label> 
				<input type="radio" name="isNeedTheme" value="0" ${app.appId > 0 && not empty app.mainTableName ? "disabled" : "" } <c:if test="${app.isNeedTheme != '1'}">checked="checked"</c:if> onclick="hiddeElem(this.value)"></input>否	 
				<input type="radio" name="isNeedTheme" value="1" ${app.appId > 0 && not empty app.mainTableName ? "disabled" : "" }<c:if test="${app.isNeedTheme == '1'}">checked="checked"</c:if> onclick="hiddeElem(this.value)"></input>是
			</p>
			<p>
				<label class="w160">应用状态：</label>
				<input type="radio" name="appStatus" value="0" <c:if test="${app.appStatus == '0'}">checked="checked"</c:if>></input>打开
    			<input type="radio" name="appStatus" value="1" <c:if test="${app.appStatus != '0'}">checked="checked"</c:if>></input>关闭	 
			</p>
			<p style="width:500px;">
				<label class="w160">是否公开信息：</label>
				<input type="radio" name="isPublic" value="0" checked="checked"></input>处理后公开
    			<input type="radio" name="isPublic" value="1" <c:if test="${app.isPublic == '1'}">checked="checked"</c:if>></input>不公开
    			<input type="radio" name="isPublic" value="2" <c:if test="${app.isPublic == '2'}">checked="checked"</c:if>></input>直接公开	 	 
			</p>
			<p  id="isHasQueryNo">
				<label class="w160">是否有查询码：</label>
				<input type="radio" name="isHasQueryNo" value="0" <c:if test="${app.isHasQueryNo != '1'}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isHasQueryNo" value="1" <c:if test="${app.isHasQueryNo == '1'}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label class="w160">是否有评论：</label>
				<input type="radio" name="isHasComment" value="0" checked="checked" ></input>否
    			<input type="radio" name="isHasComment" value="1" <c:if test="${app.isHasComment == 1}">checked="checked"</c:if>></input>私有评论
    			<input type="radio" name="isHasComment" value="2" <c:if test="${app.isHasComment == 2}">checked="checked"</c:if>></input>共有评论
			</p>
			<p>
				<label class="w160">是否邮件预警：</label>
				<input type="radio" name="isEmailWarn" value="0" <c:if test="${app.isEmailWarn != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isEmailWarn" value="1" <c:if test="${app.isEmailWarn == 1}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label class="w160">是否短信预警：</label>
				<input type="radio" name="isSmsWarn" value="0" <c:if test="${app.isSmsWarn != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isSmsWarn" value="1" <c:if test="${app.isSmsWarn == 1}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label class="w160">是否短信提醒：</label>
				<input type="radio" name="isSmsRemind" value="0" <c:if test="${app.isSmsRemind != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isSmsRemind" value="1" <c:if test="${app.isSmsRemind == 1}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label class="w160">是否有提交说明：</label>
				<input type="radio" name="isHasSmtDesc" value="0" <c:if test="${app.isHasSmtDesc != 1}">checked="checked"</c:if> onclick="hiddeDescHref(this.value)"></input>否
    			<input type="radio" name="isHasSmtDesc" value="1" <c:if test="${app.isHasSmtDesc == 1}">checked="checked"</c:if> onclick="hiddeDescHref(this.value)"></input>是
				<a id="descHref" style="height:21px;line-height:21px;" href="appView.do?method=getAppSmtDesc&appId=${app.appId}" target="dialog" title="提交说明" rel="t_smtDesc" mask="true" width="850" height="450">
			    	点击编辑
			    </a>
			</p>
			<p>
				<label class="w160">设置办理超期时限：</label>
				<input type="number" id="limitDayNum" name="limitDayNum" style="width:150px;" value="${app.limitDayNum}"/>
			</p>
			
			<%--<p>
				<label class="w160">是否前台显示组织(部门)：</label>
				<input type="radio" name="isShowGroup" value="0" <c:if test="${app.isShowGroup != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isShowGroup" value="1" <c:if test="${app.isShowGroup == 1}">checked="checked"</c:if>></input>是
			</p>
			--%><p id="isSupAppendix">
				<label class="w160">是否支持附件上传：</label>
				<input type="radio" name="isSupAppendix" value="0" <c:if test="${app.isSupAppendix != 1}">checked="checked"</c:if>></input>否
    			<input type="radio" name="isSupAppendix" value="1" <c:if test="${app.isSupAppendix == 1}">checked="checked"</c:if>></input>是
			</p>
			<p>
				<label class="w160">是否推送到WCM：</label>
				<input id="isPush" type="checkbox" name="isPush" value="1" <c:if test="${app.isPush == 1}">checked</c:if>></input>是
			</p>
			<p>
				<label class="w160">WCM文档类型：</label>
				<input type="radio" name="wcmDocType" value="0" <c:if test="${app.wcmDocType != 1}">checked="checked"</c:if>></input>普通文档
    			<input type="radio" name="wcmDocType" value="1" <c:if test="${app.wcmDocType == 1}">checked="checked"</c:if>></input>元数据
			</p>
			<p>
			    <label class="w160">WCM栏目ID：</label>
			    <input id="wcmChnlId" type="text" name="wcmChnlId" value="${app.wcmChnlId}" style="width:80px;"/>
			</p>
			<p>
				<label class="w160">是否开启组织（部门）选择：</label>
				<input type="radio" name="isSelGroup" value="0" <c:if test="${app.isSelGroup != 1}">checked="checked"</c:if> onclick="showItemSel();"></input>否
    			<input type="radio" name="isSelGroup" value="1" <c:if test="${app.isSelGroup == 1}">checked="checked"</c:if> onclick="showItemSel();"></input>是
			</p>
			<p id = "item_group_sel">
			    <label class="w160">选择子组织（部门）：</label>
			    <a style="height:21px;line-height:21px;" href="appView.do?method=orgDialog&pid=${app.groupId }&groupType=1" target="dialog" mask="true" width="400" height="500" title="添加组织" rel="org_dialog">
			    	点击选择
			    </a>
			</p>
			<p>
			    <label class="w160">列表页地址：</label>
			    <input id="listAddr" type="text" name="listAddr" style="width:200px;" value="${app.listAddr}"></input>
			</p>
			<p>
			    <label class="w160">办理页地址：</label>
			    <input id="dowithAddr" type="text" name="dowithAddr" style="width:200px;" value="${app.dowithAddr}"></input>
			</p>
			<p>
			    <label class="w160">前台列表页地址：</label>
			    <input id="weblistAddr" type="text" name="weblistAddr" style="width:200px;" value="${app.weblistAddr}"></input>
			</p>
			<p>
			    <label class="w160">前台提交页地址：</label>
			    <input id="webdowithAddr" type="text" name="webdowithAddr" style="width:200px;" value="${app.webdowithAddr}"></input>
			</p>
			<p>
			    <label class="w160">前台详细页地址：</label>
			    <input id="webdetailAddr" type="text" name="webdetailAddr" style="width:200px;" value="${app.webdetailAddr}"></input>
			</p>
			<input id="groupIds" type="hidden" name="groupIds" value="${itemGroupIds}"></input>
			<input type="hidden" name="appId" value="${app.appId}"></input>
			<input type="hidden" id="appSmtDesc" name="smtDesc" value=""></input>
			<input type="hidden" id="isEditeDesc" name="isEditeDesc" value="${app.isHasSmtDesc}"></input>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="stmapp()">保存</button></div></div></li>
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
		function stmapp(){
			if($('#limitDayNum').val() < 0){
				alertMsg.info('办理超期时限必须为大于或等于0的整数!');
				return;
			}
			if(!viladChnl())
		    	return;
			var isSelGroup = $('input[name=isSelGroup]:checked').val();
			if(isSelGroup == '1' && $('#groupIds').val() == ''){
				alertMsg.info('请选择子组织（部门）!');
				return;
			}
			var isHasSmtDesc = $('input[name=isHasSmtDesc]:checked').val();
			if(isHasSmtDesc == '1' && $('#isEditeDesc').val() != '1'){
				alertMsg.info('请添加应用提交说明!');
				return;
			}
		    $('form[name=appForm]').submit();
		    	return;
		}
		$(function(){
			showItemSel();
		});
		//隐藏、显示选择按钮
		function showItemSel(){
			var isSelGroup = $('input[name=isSelGroup]:checked').val();
			var isHasSmtDesc = $('input[name=isHasSmtDesc]:checked').val();
			//alert(isSelGroup);
			if(isSelGroup == '0'){
				$('#item_group_sel').hide();
			} else {
				$('#item_group_sel').show();
			}
			hiddeDescHref(isHasSmtDesc);
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
 </script>

