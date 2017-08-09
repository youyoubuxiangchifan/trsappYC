<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="pageContent">
	<div class="tabs" currentIndex="0" eventType="click" style="width:100%">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a href="javascript:;"><span>新建/修改信息 </span></a></li>
				</ul>
			</div>
		</div>
	<form method="post" action="post.do?method=saveOrUpMetaData" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="tabsContent" style="height:60%;overflow:hidden;" >
		<!-- 信息内容开始 -->
			<div align="center">
				<div class="pageFormContent" layoutH="96">
					<input type="hidden" name="appId" value="${requestScope.appId }" >
					<input type="hidden" name="metadataid" value="${requestScope.metaDataMap.METADATAID }" >
					<input type="hidden" name="themeId" value="${requestScope.themeId }" >
					<table id="meta_table" class="i_table" width="1024px;" borderColor="#dcdcdc" align="center" border="1">
					<c:set var="tab_count" value="0"></c:set>
						<c:forEach items="${requestScope.appFieldRels}" var="fields"> 
						<!-- 编辑器开始 -->
							<c:if test="${fields.fieldType eq 'editor' && fields.inDetail eq 1}">
								<c:set var="tab_count" value="2"></c:set>
								<tr>
									<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
									<td width="80%" align="left" colspan="4">
										<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
											<c:if test="${fields.fieldName eq valueMap.key}">
												<textarea class="xheditor ${fields.notNull eq '1'?'required':''} input_w" name="${fields.fieldName }" rows="30" cols="100">${valueMap.value }</textarea>
											</c:if>
										</c:forEach>	
									</td>
								</tr>
							</c:if>
								<!-- 简单编辑器开始 -->
							<c:if test="${fields.fieldType eq 'smeditor' && fields.inDetail eq 1}">
								<c:set var="tab_count" value="2"></c:set>
								<tr>
									<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
									<td width="80%" align="left" colspan="4">
										<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
											<c:if test="${fields.fieldName eq valueMap.key}">
												<textarea class="${fields.notNull eq '1'?'required':''} xheditor input_w" name="${fields.fieldName }" rows="30" cols="100" tools="simple">${valueMap.value }</textarea>
											</c:if>
										</c:forEach>	
									</td>
								</tr>
							</c:if>
							<!-- 文本域开始 -->
							<c:if test="${fields.fieldType eq 'textarea' && fields.inDetail eq 1}">
							<c:set var="tab_count" value="2"></c:set>
							<tr>
								<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
								<td width="80%" align="left" colspan="4">
									<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
										<c:if test="${fields.fieldName eq valueMap.key}">
											<textarea  name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} input_w" rows="15" cols="100">${valueMap.value }</textarea>	
										</c:if>
									</c:forEach>
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
									<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
										<c:if test="${fields.fieldName eq valueMap.key}">
											<c:choose>
												<c:when test="${valueMap.key eq 'CRUSER'}"><!-- 对象创建者不允许修改 -->
													<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }" value="${valueMap.value }"  />${valueMap.value }
												</c:when>
												<c:otherwise>
													<input name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} " type="text" size="50" maxlength="${fields.fieldLength }" value="${valueMap.value }"  />
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
								</td>	
							</c:if>	
							<!-- number开始 -->
							<c:if test="${fields.fieldType eq 'number'}"> <!-- 删除状态 -->
								<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
									<c:if test="${fields.fieldName eq valueMap.key and valueMap.key eq 'DELETED'}">
										<input type="hidden" name="${valueMap.key }" value="${valueMap.value}"/>
									</c:if>
								</c:forEach>
							</c:if>	
							<c:if test="${fields.fieldType eq 'number' && fields.inDetail eq 1}">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
								<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
								<td width="40%" align="left">
									<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
										<c:if test="${fields.fieldName eq valueMap.key}">
											<c:choose>
												<c:when test="${valueMap.key eq 'STATUS'}"><!-- 办理状态 -->
												<input type="hidden" name="${valueMap.key }" value="${valueMap.value}"/>
													${valueMap.value eq '0'?"未办理":""}
													${valueMap.value eq '1'?"办理中":""}
													${valueMap.value eq '2'?"已办理":""}
												</c:when>
												<c:when test="${valueMap.key eq 'ISPUBLIC'}"><!-- 是否公开状态 -->
													<c:if test="${valueMap.value eq 0}">
														<input name="${fields.fieldName }" type="radio" size="30" value="0" checked="checked"/>不公开
														<input name="${fields.fieldName }" type="radio" size="30" value="1" />公开
													</c:if>
													<c:if test="${valueMap.value eq 1}">
														<input name="${fields.fieldName }" type="radio" size="30" value="0" />不公开
														<input name="${fields.fieldName }" type="radio" size="30" value="1" checked="checked"/>公开
													</c:if>
												</c:when>
												<c:otherwise>
													<input name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} digits"  type="text" size="50" maxlength="${fields.fieldLength }"  value="${valueMap.value }" />
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
								</td>	
							</c:if>	
							<!-- 多选开始,枚举值 -->
							<c:if test="${fields.fieldType eq 'checkbox' && fields.inDetail eq 1}">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
								<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
								<td width="40%" align="left">
									<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" ><c:set var="test" value="1"></c:set>
										<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
												<c:if test="${fields.fieldName eq valueMap.key}">
													<c:forEach items="${fn:split(valueMap.value,',')}" var="chkValue" varStatus="i">
														<c:choose>
															<c:when test="${chkValue eq enmValue}"> <!-- 选中 -->
																<input name="${fields.fieldName }" type="checkbox" size="30" value="${enmValue}" checked="checked"/>${enmValue}
																<c:set var="test" value="2"></c:set>
															</c:when>
															<c:otherwise>
															</c:otherwise>
														</c:choose>
													</c:forEach>
													<c:if test="${test!=2}"><!-- 未选中 -->
														<input name="${fields.fieldName }" type="checkbox" size="30" value="${enmValue}"/>${enmValue}
													</c:if>
												</c:if>
										</c:forEach>
									</c:forEach>
								</td>	
							</c:if>
							<!-- 单选开始,枚举值 -->
							<c:if test="${fields.fieldType eq 'radio' && fields.inDetail eq 1}">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
								<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
								<td width="40%" align="left">
									<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
										<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
											<c:if test="${fields.fieldName eq valueMap.key}">
												<c:choose>
													<c:when test="${valueMap.value eq enmValue}"><!-- 默认选中 -->
														<input name="${fields.fieldName }" type="radio" size="30" value="${enmValue}" checked="checked"/>${enmValue}
													</c:when>
													<c:otherwise> <!-- 为空 -->
														<input name="${fields.fieldName }" type="radio" size="30" value="${enmValue}" />${enmValue}
													</c:otherwise>
												</c:choose>
												</c:if>
										</c:forEach>
									</c:forEach>
								</td>
							</c:if>
							<!-- 下拉框开始,枚举值-->
							<c:if test="${fields.fieldType eq 'select' && fields.inDetail eq 1 }">
								<c:set var="tab_count" value="${tab_count+1}"></c:set>
								<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
								<td width="40%" align="left">
									<select name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} combox"  size="30">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
											<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
												<c:if test="${fields.fieldName eq valueMap.key}">
													<c:choose>
														<c:when test="${valueMap.value eq enmValue}"><!-- 默认选中 -->
															<option value="${enmValue}" selected="selected">${enmValue}</option>
														</c:when>
														<c:otherwise><!-- 为空 -->
															<option value="${enmValue}">${enmValue}</option>
														</c:otherwise>
													</c:choose>
												</c:if>
											</c:forEach>
										</c:forEach>
									</select>
								</td>	
							</c:if>	
							<!-- 时间开始 -->
							<c:if test="${fields.fieldType eq 'date' && fields.inDetail eq 1}">
								<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >	
									<c:if test="${fields.fieldName eq valueMap.key}">
										<c:choose>
											<c:when test="${valueMap.key eq 'CRTIME'}"><!-- 对象创建时间 -->
												<c:set var="tab_count" value="${tab_count+1}"></c:set>
												<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
												<td width="40%" align="left">
													<input type="hidden" name="${fields.fieldName }" value="${valueMap.value }" />${valueMap.value }
												</td>
											</c:when>
											<c:when test="${valueMap.key eq 'REPLYTIME'}"><!-- 回复时间 -->
												<c:set var="tab_count" value="${tab_count+1}"></c:set>
												<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
												<td width="40%" align="left">
													<input type="hidden" name="${fields.fieldName }" value="${valueMap.value }" />${valueMap.value }
												</td>
												<c:if test="${fn:length(valueMap.value) gt 0 }">
													<input type="hidden" name="hfbsstat" value="1"/>
												</c:if>
												<c:if test="${fn:length(valueMap.value) lt 1 }">
													<input type="hidden" name="hfbsstat" value="0"/>
												</c:if>
											</c:when>
											<c:otherwise>
												<c:set var="tab_count" value="${tab_count+1}"></c:set>
												<td width="10%" class="i_std" align="left">${fields.fieldDesc }：</td>
												<td width="40%" align="left">
													<input type="text" name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} date"   value="${fn:length(valueMap.value) gt 10 ?fn:substring(valueMap.value,0,10):valueMap.value }"/><a class="inputDateButton" href="javascript:;">选择</a>
												</td>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
							</c:if>	
							<!-- 隐藏域开始 -->
							<c:if test="${fields.fieldType eq 'hidden' && fields.inDetail eq 1}">
								<div class="unit">
									<label>${fields.fieldDesc }：</label>
									<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
										<c:if test="${field.fieldName eq valueMap.key}">
											<input type="text" name="${fields.fieldName }" size="50" value="${requestScope.metaDataMap[fields.fieldName] }"/>
										</c:if>
									</c:forEach>
								</div>	
							</c:if>	
				   		</c:forEach>
				   		<tr>
				   			<td width="10%" class="i_std" align="left">附件管理：</td>
							<td width="40%" align="left" colspan="4">
								<a href="${basePath}appMgr.do?method=getAppendixs&appId=${requestScope.appId }&metadataId=${requestScope.metaDataMap.METADATAID }" target="dialog" rel="dlg_page10" mask="true" title="附件管理">点此查看附件</a>
							</td>	
						</tr>
					</table>
				</div>
			</div>
			<!-- 信息内容结束 -->
		</div>
			<c:if test="${requestScope.islock ne 'Y'}">
				<div class="formBar">
					<ul>
						<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
						<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
						<li>
							<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
						</li>
					</ul>
				</div>
			</c:if>
	</form>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
</div>
<input type="hidden" value="${requestScope.appRecordLock.lockUser}" id="lockuserstr"/>
<c:if test="${requestScope.islock eq 'Y'}">
	<script type="text/javascript">
			//var lockuserstr = ${requestScope.appRecordLock.lockUser};
			//alert("123");
			alertMsg.info("当前对象已经被锁定，锁定用户为:"+$("#lockuserstr").val()+"您只能查看当前信息");
			function errorULock(){ //解除对象锁定
				  $.get("<%=basePath%>appMgr.do?method=ULockErrorObject", {Action:"get",appId:1,timeStamp:new Date().getTime()}, function (data, textStatus){
						this; 
						//alert(data);
					});
				}
				   		//window.onbeforeunload = function (e) {
				 window.onunload = function (e) {//当用户刷新页面的时候解除锁定
					 errorULock();
				};
	</script>
</c:if>
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
<style>
body,div,span,h1,h2,h3,h4,h5,h6,p,img,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td,input{
  margin: 0;
  padding:0;
}
.Rt li span{
  font-family:微软雅黑;
  color:#404040;
  line-height:1.5;
  font-size:12px;
  font-style:normal;
  font-variant:normal;
  font-weight:normal;
  font-size-adjust:none;
  font-stretch:normal;
}
ol, ul{list-style:none}
img { border: 0 none;}
span img,a img{ vertical-align:middle}
button,input,select,label {	
	vertical-align: middle;
	border:0;
	background:#fff;
}
em, i{
	 font-weight:normal; font-style:normal
}
li{
	 list-style-type:none
 }
.clear { clear: both; font-size:0px;}
.w{ width:1000px; margin:0 auto; overflow:hidden;}
.Rt{ padding-left:60px;}
.Rt li{ float:left; height:90px; position:relative;}
.Rt li img{vertical-align:top;}
.Rt li span{ display:block; text-align:center; }
.Rt2{ position:absolute; top:30px; left:-30px; width:85px; text-align:center;}
.Rt3{color:#8c8e8d}
</style>
