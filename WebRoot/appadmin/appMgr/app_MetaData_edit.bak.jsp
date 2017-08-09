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
					<c:if test="${requestScope.appInfo.flowId != 0}">
						<li><a href="javascript:;"><span>工作流</span></a></li>
					</c:if>
				</ul>
			</div>
		</div>
	<form method="post" action="appMgr.do?method=saveOrUpMetaData" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="tabsContent" style="height:60%;overflow:hidden;" >
		<!-- 信息内容开始 -->
			<div>
				<div class="pageFormContent" layoutH="96">
					<input type="hidden" name="appId" value="${requestScope.appId }" >
					<input type="hidden" name="metadataid" value="${requestScope.metaDataMap.METADATAID }" >
					<!-- 开启组织(部门)选择 -->
					<c:if test="${appInfo.isSelGroup eq 1}">
						<c:forEach items="${fn:split(appInfo.itemGroupId,'~')}" var="appInfoGroup" varStatus="g">
							<c:if test="${g.first}">
								<div class="unit">
								<label>提交部门：</label>
								<select class="required combox" name="sub_dep">
							</c:if>
								<c:set var="app_grpids" value="${fn:split(appInfoGroup,':')}"></c:set>
								<option value="${app_grpids[0]}">${app_grpids[1]}</option>
							<c:if test="${g.last}">
								</select>
								</div>
							</c:if>
						</c:forEach>
					</c:if>
					<c:forEach items="${requestScope.appFieldRels}" var="fields"> 
					<!-- 编辑器开始 -->
						<c:if test="${fields.fieldType eq 'editor' && fields.inDetail eq 1}">
							<div class="unit">
								<label>${fields.fieldDesc }：</label>
								<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
									<c:if test="${fields.fieldName eq valueMap.key}">
										<textarea class="xheditor" name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''}" rows="30" cols="100" tools="mini">${valueMap.value }</textarea>
									</c:if>
								</c:forEach>
							</div>
						</c:if>
							<!-- 简单编辑器开始 -->
						<c:if test="${fields.fieldType eq 'smeditor' && fields.inDetail eq 1}">
							<div class="unit">
								<label>${fields.fieldDesc }：</label>
								<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
									<c:if test="${fields.fieldName eq valueMap.key}">
										<textarea class="xheditor" name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''}" rows="30" cols="100" tools="mini">${valueMap.value }</textarea>
									</c:if>
								</c:forEach>
							</div>
						</c:if>
						<!-- 文本框开始 -->
						<c:if test="${fields.fieldType eq 'text' && fields.inDetail eq 1}">
							<c:if test="${fields.fieldName ne 'CRUSER'}">
							<div class="unit">
								<label>${fields.fieldDesc }：</label>
								<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
									<c:if test="${fields.fieldName eq valueMap.key}">
										<c:choose>
											<c:when test="${valueMap.key eq 'CRUSER'}"><!-- 对象创建者不允许修改 -->
												<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }" value="${valueMap.value }"  />${valueMap.value }
											</c:when>
											<c:otherwise>
												<input name="${fields.fieldName }" type="text" size="50" ${fields.notEdit eq '1'?'readonly=readonly':''} class="${fields.notNull eq '1'?'required':''}" maxlength="${fields.fieldLength }" value="${valueMap.value }"  />
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
							</div>	
							</c:if>	
						</c:if>	
							<!-- 文本域开始 -->
						<c:if test="${fields.fieldType eq 'textarea' && fields.inDetail eq 1}">
							<div style="width:100%;float:left;margin:5px 0;">
							<label style="width:120px;float:left">${fields.fieldDesc }：</label>
							<div style="width:70%;float:left">								
								<textarea  name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} textarea" rows="15" cols="60">${valueMap.value }</textarea>
								</div>
							</div>
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
							<div class="unit">
								<label>${fields.fieldDesc }：</label>
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
												<input name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} digits" ${fields.notEdit eq '1'?'readonly=readonly':''} type="text" size="50" maxlength="${fields.fieldLength }"  value="${valueMap.value }" />
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
							</div>	
						</c:if>	
						<!-- 多选开始,枚举值 -->
						<c:if test="${fields.fieldType eq 'checkbox' && fields.inDetail eq 1}">
							<div class="unit"> 
								<label>${fields.fieldDesc }： </label>  
								 <c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" > 
									<c:set var="test" value="1"></c:set>
									<c:forEach items="${requestScope.metaDataMap}" var="valueMap" > 
											<c:if test="${fields.fieldName eq valueMap.key}">
												<c:forEach items="${fn:split(valueMap.value,',')}" var="chkValue" varStatus="i"> 
													<c:choose> 
														<c:when test="${chkValue eq enmValue}"> 
															<input name="${fields.fieldName }" type="checkbox" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30" value="${enmValue}" checked="checked" /> ${enmValue}
															<c:set var="test" value="2"></c:set> 
														</c:when>
														<c:otherwise>
														</c:otherwise>
													</c:choose>
												</c:forEach>
												<c:if test="${test!=2}">
													<input name="${fields.fieldName }" type="checkbox" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30" value="${enmValue}" />${enmValue}
												</c:if>
											</c:if>
									</c:forEach>
								</c:forEach>
							</div>	
						</c:if>
						<!-- 单选开始,枚举值 -->
						<c:if test="${fields.fieldType eq 'radio' && fields.inDetail eq 1}">
							<div class="unit">
								<label>${fields.fieldDesc }：</label>
								<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
									<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
										<c:if test="${fields.fieldName eq valueMap.key}">
											<c:choose>
												<c:when test="${valueMap.value eq enmValue}"><!-- 默认选中 -->
													<input name="${fields.fieldName }" style="position: relative;z-index: 99" type="radio" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30" value="${enmValue}" checked="checked"/>${enmValue}
												</c:when>
												<c:otherwise><!-- 为空 -->
													<input name="${fields.fieldName }" style="position: relative;z-index: 199" type="radio" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30" value="${enmValue}" />${enmValue}
												</c:otherwise>
											</c:choose>
											</c:if>
									</c:forEach>
								</c:forEach>
							</div>
						</c:if>
						<!-- 下拉框开始,枚举值-->
						<c:if test="${fields.fieldType eq 'select' && fields.inDetail eq 1 }">
							<div class="unit">
								<label>${fields.fieldDesc }：</label>
								<select name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} combox" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30">
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
							</div>	
						</c:if>	
						<!-- 时间开始 -->
						<c:if test="${fields.fieldType eq 'date' && fields.inDetail eq 1}">
							<div class="unit">
								<label>${fields.fieldDesc }:</label>
								<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >	
									<c:if test="${fields.fieldName eq valueMap.key}">
										<c:choose>
											<c:when test="${valueMap.key eq 'REPLYTIME'}"><!-- 回复时间 -->
												<input type="hidden" name="${fields.fieldName }" value="${valueMap.value }" />${valueMap.value }
												<c:if test="${fn:length(valueMap.value) gt 0 }">
													<input type="hidden" name="hfbsstat" value="1"/>
												</c:if>
												<c:if test="${fn:length(valueMap.value) lt 1 }">
													<input type="hidden" name="hfbsstat" value="0"/>
												</c:if>
											</c:when>
											<c:when test="${valueMap.key eq 'CRTIME'}">
												<input type="hidden" name="${fields.fieldName }" value="${valueMap.value }" />${valueMap.value }
											</c:when>
											<c:otherwise>
												<input type="text" name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} date"  ${fields.notEdit eq '1'?'readonly=readonly':''} value="${fn:length(valueMap.value) gt 10 ?fn:substring(valueMap.value,0,10):valueMap.value }"/><a class="inputDateButton" href="javascript:;">选择</a>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
							</div>
						</c:if>	
						<!-- 隐藏域开始 -->
						<c:if test="${fields.fieldType eq 'hidden' && fields.inDetail eq 1}">
							<div class="unit">
								<label>${fields.fieldDesc }：</label>
								<c:forEach items="${requestScope.metaDataMap}" var="valueMap" >
									<c:if test="${field.fieldName eq valueMap.key}">
										<input type="hidden" name="${fields.fieldName }" class="date" size="${valueMap.value }" /><a class="inputDateButton" href="javascript:;">选择</a>
									</c:if>
								</c:forEach>
							</div>	
						</c:if>	
			   		</c:forEach>
		   			<div class="unit">
							<label>附件管理：</label>
							<a href="${basePath}appMgr.do?method=getAppendixs&appId=${requestScope.appId }&metadataId=${requestScope.metaDataMap.METADATAID }" target="dialog" rel="dlg_page10" mask="true" title="附件管理">点此查看附件</a>
					</div>	
				</div>
			</div>
			<!-- 信息内容结束 -->
			<!-- 工作流 -->
			<c:if test="${requestScope.appInfo.flowId != 0}">
				<div>
					<div id="doc" class="pageFormContent" layoutH="86">
						<!-- 流转轨迹 -->
						<div class="w">
						   <ul class="Rt">
						   <c:set var="bs" value="-100"></c:set>
						   	<c:forEach items="${requestScope.allResult.flowDoclist}" var="appFlowdocs" varStatus="i">
						   		<c:if test="${appFlowdocs.worked eq 1 and appFlowdocs.isOwnerWork eq 1}"><!-- 已处理的 -->
						   				<c:forEach items="${requestScope.allResult.appFlowNodes}" var="appFlowNodes" >
						   					<c:if test="${appFlowdocs.nodeId eq appFlowNodes.nodeId and appFlowdocs.prenodeid ne bs}">
							   	 				<li>
										        	<img src="<%=basePath%>appadmin/images/r1.gif"/>
									        		<div class="Rt2" title="单击查看当前节点详细信息">
								        				<a href="appMgr.do?method=getMetaFlowInfo&appId=${requestScope.appId }&metadataId=${requestScope.metaDataMap.METADATAID }&nodeId=${appFlowNodes.nodeId}&preflowdocid=${appFlowdocs.preflowdocid}" target="dialog" rel="dlg_page9" mask="true"  title="${appFlowNodes.nodeName}" >
								        					<span >${appFlowNodes.nodeName}</span>
								        				</a>
									        			<span>会签状态：${(appFlowNodes.together eq 0)?"否":"是"}</span>
									        			<c:if test="${appFlowNodes.together eq 1}">
									        				<c:set var="bs" value="${appFlowdocs.prenodeid}"></c:set>
									        			</c:if>
									        		</div>
										        </li>
									        	  <c:if test="${!i.last}">
											       	 		<li><img src="<%=basePath%>appadmin/images/R2.gif"/></li>
											      </c:if>
									        </c:if>
								        </c:forEach>
							   	 	</c:if>
							    </c:forEach>
						   </ul>
						</div>
						<!-- 流转轨迹结束-->
						<!-- 处理意见 -->
						<div class="flow_change_parent">
						<div class="flow_left">
							<c:if test="${requestScope.allResult.isShowNode ne 'N'}"><!-- 没有下一个节点，就没有任何操作了 -->
								<div class="unit">
									<label>办理意见：</label>
									<textarea rows="10" cols="50" name="czyj" class="required textInput"></textarea>
									<input type="hidden" name="flowDocId" value="${requestScope.allResult.appFlowDoc.flowDocId }">
									<input type="hidden" id="_f_nodeId" name="nextNodeId" value="${requestScope.allResult.nextNodeId }">
									<input type="hidden" name="nodeId" value="${requestScope.allResult.appFlowNode.nodeId }">
									<input type="hidden" name="isShowNode" value="${requestScope.allResult.isShowNode }">
								</div>
								<div class="unit">
									<label>办理方式：${requestScope.allResult.appFlowNode.isFinish}</label>
									<select class="required"  name="operateType">
										<option value="">请选择办理方式</option>
										<c:if test="${requestScope.allResult.appFlowNode.isaccept eq 1}">
											<option value="0">签收</option>
										</c:if>
										<c:if test="${requestScope.allResult.appFlowNode.isFinish eq 1}">
											<option value="1">直接办理</option>
										</c:if>
										<option value="2">退回</option>
										<option value="3">办理</option>
										<option value="4">交办</option>
									</select>
								</div>
							</c:if>
						</div>
							<div class="flow_left">
							<c:if test="${requestScope.allResult.isShowNode eq 'HY'}">
									<div class="unit">
										<label>请选择节点：</label>
										<select class="required combox" onchange="_loadNodeJson(${requestScope.appId },${requestScope.metaDataMap.METADATAID },this.value);">
											<c:forEach items="${requestScope.allResult.nextAppFlowNodes}" var="nextAppFlowNodes">
												<option value="${nextAppFlowNodes.nodeId }">${nextAppFlowNodes.nodeName }</option>
											</c:forEach>
										</select>
									</div>
								
										<div class="unit" id="w_users" style="display: ${requestScope.allResult.appFlowUsers ne null?'block':'none'}">
											<label>用户：</label>
											<div id="f_users" >
												<c:forEach items="${requestScope.allResult.appFlowUsers}" var="appFlowUsers">
													<input type="checkbox" name="flowToUserIds" value="${appFlowUsers.userId}"/>${appFlowUsers.username }
												</c:forEach>
											</div>
										</div>
									
										<div class="unit" id="w_groups" style="display: ${requestScope.allResult.appFlowGroups ne null?'block':'none'}">
											<label>部门：</label>
											<div id="f_groups" >
												<c:forEach items="${requestScope.allResult.appFlowGroups}" var="appFlowGroups">
													<input type="checkbox" name="flowToGroupIds" value="${appFlowGroups.groupId }" />${appFlowGroups.gname }
												</c:forEach>
											</div>
										</div>
									<div class="unit" style="display: none">
									<label>部门：</label>
										<div id="f_groups_users">
											<c:forEach items="${requestScope.allResult.flowGroupAndUsers}" var="curr_flowGroupAndUsers" varStatus="i">
											<div style="padding-left: 130px;"  >
												<input type="checkbox" name="flowToGroupIds" value="${curr_flowGroupAndUsers.groupId }" />${curr_flowGroupAndUsers.groupName }<br/>
												<c:forEach items="${curr_flowGroupAndUsers.users}" var="curr_flow_users">
													<input type="checkbox" name="flowToUserIds" value="${curr_flow_users.userId}"/>${curr_flow_users.username }
												</c:forEach>
											</div>
											</c:forEach>
										</div>
									</div>
									
								</c:if> 
								<c:if test="${requestScope.allResult.isShowNode eq 'HN'}">
									<div class="unit">
										<label>请选择节点：</label>
										<select class="combox">
											<option value="" selected="selected">进行会签</option>
										</select>
									</div>
								</c:if>
								<style type="text/css" rel="stylesheet">
									.flow_left{ float:left;}
									.flow_left{ width:49%;}
									.unit{line-height: 24px;}
								</style>
							</div>
						</div>
						<!-- 处理意见结束 -->
					</div>
				</div>
			</c:if>
			<!--  工作流结束 -->
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
	function _loadNodeJson(appId,objId,nodeId){
		$("#_f_nodeId").val(nodeId);
			$("#f_users").html("数据加载中……");
			 $.get("appMgr.do?method=queryFlowNodeAjax", {Action:"get",appId:appId,nodeId:nodeId,metedataId:objId}, function (data, textStatus){
			 //转化string为json格式
			  $("#f_users").html("");
			  $("#f_groups").html("");
			  $("#f_groups_users").html("");
			 
			var auser_json = eval("("+data+")");
			//判断用户集合不为空,加载用户
			if(auser_json.users.use0!=undefined){
				var _user_json =auser_json.users;
			 	$.each(_user_json, function (index, term) {
			 		   $("#w_users").css("display","block");
                       $("#f_users").html($("#f_users").html()+"<input type='checkbox' name='flowToUserIds' value='"+term.userId+"'/>"+term.userName);
                 });
			  }else{
			  	//该工作流节点下没有用户，或者设置的用户没有权限访问这条数据!
			  		$("#f_users").html("无记录!");
			  }
			//组织集合不为空，加载组织
			
			if(auser_json.groups.grp0!=undefined){
				var _group_json =auser_json.groups;
			 	$.each(_group_json, function (index, term) {
			 		   $("#w_groups").css("display","block");
                       $("#f_groups").html($("#f_groups").html()+"<input type='checkbox' name='flowToGroupIds' value='"+term.groupId+"'/>"+term.groupName);
                 });
			  }else{
			  //该工作流节点下没有组织，或者设置的组织下的用户没有权限访问这条数据!
			  	//$("#f_groups").html("无记录!");
			  }
			  //自定义规则
			if(auser_json.grpus.grp0!=undefined){
				var _group_json =auser_json.grpus;
				  $("#w_users").css("display","none");
				  $("#w_groups").css("display","none");
			 	$.each(_group_json, function (index, term) {
                       $("#f_groups_users").html($("#f_groups_users").html()+"<input type='checkbox' name='flowToGroupIds' value='"+term.groupId+"'/>"+term.groupName+"<br/>");
                   	if(term.us.us0!=undefined){
                   		var _us_json = term.us;
	                     $.each(_us_json, function (index, terms) {
	                       $("#f_groups_users").html($("#f_groups_users").html()+"<input type='checkbox' name='flowToUserIds' value='"+terms.userId+"'/>"+terms.userName);
	                	 });
	                	 $("#f_groups_users").html($("#f_groups_users").html()+"<br/>");
                	 }
                 });
			  }else{
			  //该工作流节点下没有组织，或者设置的组织下的用户没有权限访问这条数据!
			  	$("#f_groups_users").html("无记录!");
			  }
			});
		}
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
#f_groups_users input{ margin:5px;}
</style>
