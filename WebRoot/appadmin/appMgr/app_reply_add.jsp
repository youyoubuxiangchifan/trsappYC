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
				</ul>
			</div>
		</div>
		<div class="tabsContent" style="height:60%;overflow:hidden;" >
			<div align="center">
				<form method="post" action="post.do?method=saveOrUpMetaData" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
					<div id="doc" class="pageFormContent" layoutH="96">
					<input type="hidden" name="appId" value="${requestScope.appId }" >
					<input type="hidden" name="themeId" value="${requestScope.themeId }" >
					<table id="meta_table" class="i_table" borderColor="#dcdcdc" align="center" border="1">
						<c:set var="tab_count" value="0"></c:set>
					<c:forEach items="${requestScope.appFieldRels}" var="fields"> 
							<!-- 编辑器开始 -->
						<c:if test="${fields.fieldType eq 'editor' && fields.inDetail eq 1}">
						<c:set var="tab_count" value="2"></c:set>
						<tr>
							<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
							<td width="80%" align="left" colspan="4">
								<textarea class="${fields.notNull eq '1'?'required':''} xheditor input_w" name="${fields.fieldName }" rows="15" cols="100" tools="mini"></textarea>	
							</td>
						</tr>
						</c:if>
						<!-- 简单编辑器开始 -->
						<c:if test="${fields.fieldType eq 'smeditor' && fields.inDetail eq 1}">
						<c:set var="tab_count" value="2"></c:set>
						<tr>
							<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
							<td width="80%" align="left" colspan="4">
								<textarea class="${fields.notNull eq '1'?'required':''} xheditor input_w" name="${fields.fieldName }" rows="15" cols="100" tools="mini"></textarea>	
							</td>
						</tr>
						</c:if>
						<!-- 文本域开始 -->
						<c:if test="${fields.fieldType eq 'textarea' && fields.inDetail eq 1}">
						<c:set var="tab_count" value="2"></c:set>
						<tr>
							<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
							<td width="80%" align="left" colspan="4">
									<textarea  name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''}" rows="15" cols="100"></textarea>	
							</td>
						</tr>
						</c:if>
						<!-- 文本框开始 -->
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
						<c:if test="${fields.fieldType eq 'text' && fields.inDetail eq 1}">
							<c:set var="tab_count" value="${tab_count+1}"></c:set>
							<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
							<td width="40%" align="left">
								<c:choose>
									<c:when test="${fields.fieldName eq 'CRUSER'}"><!-- 对象创建者不允许修改 -->
										<input name="${fields.fieldName }" type="text" size="50" maxlength="${fields.fieldLength }" class="required" value="${sessionScope.loginUser.username }" disabled="disabled"/>
									</c:when>
									<c:otherwise>
										<input name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''}" type="text" size="50" maxlength="${fields.fieldLength }"  value="${fields.defaultValue ne null?fields.defaultValue:"" }" />
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>	
						<!-- number开始 -->
						<input name="DELETED" type="hidden" size="50"  value="0" />
						<c:if test="${fields.fieldType eq 'number' && fields.inDetail eq 1}">
							<c:set var="tab_count" value="${tab_count+1}"></c:set>
							<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
							<td width="40%" align="left">
								<c:choose>
									<c:when test="${fields.fieldName eq 'STATUS'}"> <!-- 办理状态 -->
										未办理
										<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }"  value="0" />
									</c:when>
									<c:when test="${fields.fieldName eq 'ISPUBLIC'}"> <!-- 是否公开状态 -->
										不公开
										<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }"  value="0" />
									</c:when>
									<c:otherwise>
										<input name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} digits" type="text" size="50" maxlength="${fields.fieldLength }"  value="${fields.defaultValue ne null?fields.defaultValue:"" }" />
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>	
						<!-- 多选开始,枚举值 -->
						<c:if test="${fields.fieldType eq 'checkbox' && fields.inDetail eq 1}">
							<c:set var="tab_count" value="${tab_count+1}"></c:set>
							<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
							<td width="40%" align="left">
								<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
									<c:choose>
										<c:when test="${fields.defaultValue eq enmValue}"> 
										<!-- 默认选中 -->
											<input name="${fields.fieldName }" type="checkbox" size="30" value="${enmValue}" checked="checked"/>${enmValue}
										</c:when>
										<c:otherwise>
										 <!-- 为空 -->
											<input name="${fields.fieldName }" type="checkbox" size="30" value="${enmValue}" />${enmValue}
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
										<c:when test="${fields.defaultValue eq enmValue}"> <!-- 默认选中 -->
											<input name="${fields.fieldName }" type="radio" size="30" value="${enmValue}" checked="checked"/>${enmValue}
										</c:when>
										<c:otherwise><!-- 为空 -->
											<input name="${fields.fieldName }"  type="radio" size="30" value="${enmValue}" />${enmValue}
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
										<input type="text" name="${fields.fieldName }" class="date"  /><a class="inputDateButton" href="javascript:;">选择</a>
									</td>
								</c:otherwise>
							</c:choose>
						</c:if>	
						<!-- 隐藏域开始 -->
						<c:if test="${fields.fieldType eq 'hidden' && fields.inDetail eq 1}">
							<p class="_p">
								<label>${fields.fieldDesc }：</label>
								<input type="text" name="${fields.fieldName }" class="date" size="50"/>
							</p>	
						</c:if>	
					</c:forEach>
					</table>
					</div>
					
					<div class="formBar">
						<ul>
							<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
							<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
							<li>
								<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
							</li>
						</ul>
					</div>
				</form>
			</div>
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
	
</div>
<script>
	$('.xheditor').each(function(){
		var editorTools = $(this).attr("tools");
		$(this).xheditor({tools:editorTools,skin:'vista',upLinkUrl:'appUpload.do?method=doXheditorUpload',upLinkExt:'zip,rar,txt,doc,docx,xls,xlsx',upImgUrl:'appUpload.do?method=doXheditorUpload',upImgExt:'jpg,jpeg,gif,png',upFlashUrl:'appUpload.do?method=doXheditorUpload',upFlashExt:'swf',upMediaUrl:'appUpload.do?method=doXheditorUpload',upMediaExt:'wmv,avi,wma,mp3,mid'});
	});
	$(function(){
		$("#meta_table").find("tr").each(function(){
			if($(this).find("td").length == 2){
				$(this).find("td").eq(1).attr("colspan", "3").find("input[type=text]").addClass("input_w");
			}
			$(this).find("td:odd").addClass("td_con");
		});
	});
</script>
