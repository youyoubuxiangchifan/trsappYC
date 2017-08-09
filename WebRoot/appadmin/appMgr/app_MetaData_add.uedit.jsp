<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pageContent">
	<div class="tabs" currentIndex="0" eventType="click" style="width:100%">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a href="javascript:;"><span>新建/修改信息</span></a></li>
					<c:if test="${fn:length(requestScope.metaDataMap.METADATAID) gt 0} ">
						<li><a href="javascript:;"><span>工作流</span></a></li>
					</c:if>
				</ul>
			</div>
		</div>
		<div class="tabsContent">
			<div align="center" >
				<form id="js_dataform" method="post" action="appMgr.do?method=saveOrUpMetaData"  class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
					<div id="doc" class="pageFormContent" layoutH="96" >
					<input type="hidden" name="appId" value="${requestScope.appId }" >
					<input type="hidden" name="DELETED" value="0" >
					<table class="i_table" width="1024px;" borderColor="#dcdcdc" align="center" border="1">
						<c:set var="tab_count" value="0"></c:set>
							<c:if test="${appInfo.isSelGroup eq 1}">
							<tr>
								<td width="10%" class="i_std" align="left">${fields.fieldDesc }提交部门</td>
								<td width="80%" align="left" colspan="4">
									<c:forEach items="${fn:split(appInfo.itemGroupId,'~')}" var="appInfoGroup" varStatus="g">
										<c:if test="${g.first}">
											<div class="unit">
											<select class="required combox" name="sub_dep">
										</c:if>
											<c:set var="app_grpids" value="${fn:split(appInfoGroup,':')}"></c:set>
											<option value="${app_grpids[0]}">${app_grpids[1]}</option>
										<c:if test="${g.last}">
											</select>
											</div>
										</c:if>
									</c:forEach>
								</td>
							</tr>
							</c:if>
						<c:forEach items="${requestScope.appFieldRels}" var="fields">
							<c:if test="${fields.fieldStyle ==0 || fields.fieldStyle ==2}">
								<c:if test="${fields.fieldType eq 'editor' && fields.inDetail eq 1}">
									<c:set var="tab_count" value="2"></c:set>
									<tr class="css_edit_warp">
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="80%" align="left" colspan="4" valign="top">
											<div class="css_edit_warp">
												<textarea id="${fields.fieldName }" name="${fields.fieldName }" class="js_uedit css_textarea" cols="40" rows="30"></textarea>
											</div>
										</td>
									</tr>
								</c:if>
							<!-- 简单编辑器开始 -->
								<c:if test="${fields.fieldType eq 'smeditor' && fields.inDetail eq 1}">
									<c:set var="tab_count" value="2"></c:set>
									<tr>
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="80%" align="left" colspan="4">
											<div class="css_edit_warp">
												<textarea id="${fields.fieldName }" name="${fields.fieldName }"  class="js_uedit css_textarea" cols="40" rows="30"></textarea>
											</div>
										</td>
									</tr>
								</c:if>
								<!-- 文本域开始 -->
								<c:choose>
									<c:when test="${tab_count==0}">
										<tr>
									</c:when>
									<c:when test="${tab_count==2}">
										<c:set var="tab_count" value="0"></c:set>
										</tr>
									</c:when>
									<c:otherwise> 
									</c:otherwise>
								</c:choose>
								<c:if test="${fields.fieldType eq 'textarea' && fields.inDetail eq 1}">
									<c:set var="tab_count" value="2"></c:set>
									<tr>
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="80%" align="left" colspan="4">
											<div style="width:70%;float:left">								
												<textarea  name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} textarea" rows="15" cols="100"></textarea>
											</div>
										</td>
									</tr>
								</c:if>
								<!-- 文本框开始 -->
								<c:if test="${fields.fieldType eq 'text' && fields.inDetail eq 1}">
									<c:set var="tab_count" value="${tab_count+1}"></c:set>
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="40%" align="left">
											<c:choose>
												<c:when test="${fields.fieldName eq 'CRUSER'}"><!-- 对象创建者不允许修改 -->
													<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }" value="${sessionScope.loginUser.username }" />${sessionScope.loginUser.username }
												</c:when>
												<c:otherwise>
													<input name="${fields.fieldName }" type="text" size="50" maxlength="${fields.fieldLength }" class="${fields.notNull eq '1'?'required':''}" value="${fields.defaultValue ne null?fields.defaultValue:"" }" />
												</c:otherwise>
											</c:choose>
										</td>
								</c:if>	
								<!-- number开始 -->
								<c:if test="${fields.fieldType eq 'number' && fields.inDetail eq 1}">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="40%" align="left">
											<c:choose>
												<c:when test="${fields.fieldName eq 'STATUS'}"><!-- 办理状态 -->
													未办理
													<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }"  value="0" />
												</c:when>
												<c:when test="${fields.fieldName eq 'ISPUBLIC'}"><!-- 是否公开状态 -->
													不公开
													<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }"  value="0" />
												</c:when>
												<c:otherwise>
													<input name="${fields.fieldName }" type="text" size="50" maxlength="${fields.fieldLength }" class="${fields.notNull eq '1'?'required':''} digits" value="${fields.defaultValue ne null?fields.defaultValue:"" }" />
												</c:otherwise>
											</c:choose>
										</td>
								</c:if>	
								<!-- 多选开始,枚举值 -->
								<c:if test="${fields.fieldType eq 'checkbox' && fields.inDetail eq '1'}">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="40%" align="left">
											<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
												<c:choose>
													<c:when test="${fields.defaultValue eq enmValue}">
														<input name="${fields.fieldName }" type="checkbox" class="${fields.notNull eq '1'?'required':''} checkbox" size="30" value="${enmValue}" checked="checked"/>${enmValue}
													</c:when>
													<c:otherwise> <!-- 为空 -->
														<input name="${fields.fieldName }" type="checkbox" class="${fields.notNull eq '1'?'required':''} checkbox" size="30" value="${enmValue}" />${enmValue}
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</td>
								</c:if>
								<!-- 单选开始,枚举值 -->
								<c:if test="${fields.fieldType eq 'radio' && fields.inDetail eq 1}">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="40%" align="left">
											<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
												<c:choose>
													<c:when test="${fields.defaultValue eq enmValue}"><!-- 默认选中 -->
														<input name="${fields.fieldName }" type="radio" class="${fields.notNull eq '1'?'required':''} radio" size="30" value="${enmValue}" checked="checked"/>${enmValue}
													</c:when>
													<c:otherwise><!-- 为空 -->
														<input name="${fields.fieldName }" type="radio" class="${fields.notNull eq '1'?'required':''} radio" size="30" value="${enmValue}" />${enmValue}
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</td>
								</c:if>
								<!-- 下拉框开始,枚举值-->
								<c:if test="${fields.fieldType eq 'select' && fields.inDetail eq 1 }">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
										<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
										<td width="40%" align="left">
											<select name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} combox" size="30">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
														<c:choose>
															<c:when test="${fields.defaultValue eq enmValue}"><!-- 默认选中 -->
																<option value="${enmValue}" selected="selected">${enmValue}</option>
															</c:when>
															<c:otherwise><!-- 为空 -->
																<option value="${enmValue}">${enmValue}</option>
															</c:otherwise>
														</c:choose>
												</c:forEach>
											</select>
										</td>
								</c:if>	
								<!-- 时间开始 -->
								<c:if test="${fields.fieldType eq 'date' && fields.inDetail eq 1}">
									<c:choose>
										<c:when test="${fields.fieldName eq 'CRTIME'}"><!-- 对象创建时间默认新增不显示 -->
										</c:when>
										<c:when test="${fields.fieldName eq 'REPLYTIME'}"><!-- 回复时间不显示 -->
										</c:when>
										<c:otherwise>
											<c:set var="tab_count" value="${tab_count+1}"></c:set>
											<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
											<td width="40%" align="left">
												<input type="text" name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} date"  /><a class="inputDateButton" href="javascript:;">选择</a>
											</td>
										</c:otherwise>
									</c:choose>
								</c:if>	
								<!-- 隐藏域开始 -->
								<c:if test="${fields.fieldType eq 'hidden' && fields.inDetail eq 1}">
									<p class="_p">
										<label>${fields.fieldDesc }：</label>
										<input type="hidden" name="${fields.fieldName }" class="date" size="50" maxlength="${fields.fieldLength }"/><a class="inputDateButton" href="javascript:;">选择</a>
									</p>	
								</c:if>	
						</c:if>
					</c:forEach>
					<!-- 引入回复字段 -->
						<%@ include file="./app_MetaData_add_include.jsp"%> 
					</table>
					</div>
					
					<div class="formBar">
						<ul>
							<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
							<li><div class="buttonActive"><div class="buttonContent"><button type="submit" class="submit">保存</button></div></div></li>
							<li>
								<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
							</li>
						</ul>
					</div>
				</form>
			</div>
			<!-- 工作流 -->
			<c:if test="${fn:length(requestScope.metaDataMap.METADATAID) gt 0} ">
				<div>
					<form method="post" action="appMgr.do?method=saveOrUpMetaData" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
							<div id="doc" class="pageFormContent" layoutH="86">
							</div>
						</form>
						<div class="formBar">
					<ul>
						<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
						<li><div class="buttonActive"><div class="buttonContent" ><button type="submit" class="submit">保存</button></div></div></li>
						<li>
							<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
						</li>
					</ul>
				</div>
				</div>
			</c:if>
			<!--  工作流结束 -->
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
	
<script>

	
var editor = new UE.ui.Editor({
    toolbars: [
   	        ['fullscreen', 'source', 'undo', 'redo', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc']
   	    ],
   	    autoHeightEnabled: false,
   	    autoFloatEnabled: true
   	});
	$('.js_uedit').each(function(){
		var editorTools = $(this).attr("id");
		editor.render(editorTools);
	});
$("form").submit(function(e){
		alert(123);
	sync("js_dataform");
});


</script>
<style>
.i_table{BORDER-COLLAPSE: collapse;width: 1024px; }
.i_table tr td{line-height: 24px; height: 30px; padding: 2px;}
.i_std{background-color: #f5f5f5; }
.i_ctd{background-color: #ccc}
/*.style_edit_ifrme{width:70%;float:left; ;position: absolute; top: 0; left: 0;}
.css_edit_warp{width: 100%; height: 230px; position: relative} 
.css_edit_warp{width: 100%; height:280px; } 
.css_textarea{width: 700px;height: 200px; };*/
</style>