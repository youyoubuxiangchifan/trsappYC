<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<c:forEach items="${requestScope.appFieldRels}" var="fields"> 
<c:if test="${fields.fieldStyle ==1}">
	<!-- 编辑器开始 -->
	<c:if test="${fields.fieldType eq 'editor' && fields.inDetail eq 1}">
		<c:set var="tab_count" value="2"></c:set>
			<tr>
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
				<td width="90%" align="left" colspan="4">
						<textarea class="${fields.notNull eq '1'?'required':''} xheditor input_w" name="${fields.fieldName }"  rows="15" cols="60" tools="mini">${requestScope.metaDataMap[fields.fieldName] }</textarea>
				</td>
			</tr>
	</c:if>
		<!-- 简单编辑器开始 -->
	<c:if test="${fields.fieldType eq 'smeditor' && fields.inDetail eq 1}">
		<c:set var="tab_count" value="2"></c:set>
			<tr>
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
				<td width="90%" align="left" colspan="4">
						<textarea class="${fields.notNull eq '1'?'required':''} xheditor input_w" name="${fields.fieldName }"  rows="15" cols="60" tools="mini">${requestScope.metaDataMap[fields.fieldName] }</textarea>
				</td>
			</tr>
	</c:if>
	<!-- 文本域开始 -->
	<c:if test="${fields.fieldType eq 'textarea' && fields.inDetail eq 1}">
		<c:set var="tab_count" value="2"></c:set>
			<tr>
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
				<td width="90%" align="left" colspan="4">
						<textarea  name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} textarea input_w" rows="15" cols="60">${requestScope.metaDataMap[fields.fieldName] }</textarea>
				</td>
			</tr>
	</c:if>
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
	<!-- 文本框开始 -->
	<c:if test="${fields.fieldType eq 'text' && fields.inDetail eq 1}">
		<c:if test="${fields.fieldName ne 'CRUSER'}">
			<c:set var="tab_count" value="${tab_count+1}"></c:set>
			<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
			<td width="40%" align="left">
				<c:choose>
					<c:when test="${fields.fieldName eq 'CRUSER'}"><!-- 对象创建者不允许修改 -->
						<input name="${fields.fieldName }" type="hidden" size="50" maxlength="${fields.fieldLength }" value="${requestScope.metaDataMap[fields.fieldName] }"  />${requestScope.metaDataMap[fields.fieldName] }
					</c:when>
					<c:otherwise>
						<input name="${fields.fieldName }" type="text" size="50" ${fields.notEdit eq '1'?'readonly=readonly':''} class="${fields.notNull eq '1'?'required':''}" maxlength="${fields.fieldLength }" value="${requestScope.metaDataMap[fields.fieldName] }"  />
					</c:otherwise>
				</c:choose>
			</td>	
		</c:if>	
	</c:if>
	<!-- number开始 -->
	<c:if test="${fields.fieldType eq 'number'}"> <!-- 删除状态 -->
		<c:if test="${fields.fieldName eq 'DELETED'}">
			<input type="hidden" name="${fields.fieldName }" value="${requestScope.metaDataMap[fields.fieldName]}"/>
		</c:if>
	</c:if>	
	<c:if test="${fields.fieldType eq 'number' && fields.inDetail eq 1}">
		<c:set var="tab_count" value="${tab_count+1}"></c:set>
		<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
		<td width="40%" align="left">
			<c:choose>
				<c:when test="${fields.fieldName eq 'STATUS'}">
				<input type="hidden" name="${fields.fieldName }" value="${requestScope.metaDataMap[fields.fieldName]}"/>
					${requestScope.metaDataMap[fields.fieldName] eq '0'?"未办理":""}
					${requestScope.metaDataMap[fields.fieldName] eq '1'?"办理中":""}
					${requestScope.metaDataMap[fields.fieldName] eq '2'?"已办理":""}
				</c:when>
				<c:when test="${fields.fieldName eq 'ISPUBLIC'}">
					<c:if test="${requestScope.metaDataMap[fields.fieldName] eq 0}">
						<input name="${fields.fieldName }" type="radio" size="30" value="0" checked="checked"/>不公开
						<input name="${fields.fieldName }" type="radio" size="30" value="1" />公开
					</c:if>
					<c:if test="${requestScope.metaDataMap[fields.fieldName] eq 1}">
						<input name="${fields.fieldName }" type="radio" size="30" value="0" />不公开
						<input name="${fields.fieldName }" type="radio" size="30" value="1" checked="checked"/>公开
					</c:if>
				</c:when>
				<c:otherwise>
					<input name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} digits" ${fields.notEdit eq '1'?'readonly=readonly':''} type="text" size="50" maxlength="${fields.fieldLength }"  value="${requestScope.metaDataMap[fields.fieldName] }" />
				</c:otherwise>
			</c:choose>
		</td>
	</c:if>	
	<!-- 多选开始,枚举值 -->
	<c:if test="${fields.fieldType eq 'checkbox' && fields.inDetail eq 1}">
		<c:set var="tab_count" value="${tab_count+1}"></c:set>
		<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
		<td width="40%" align="left">
			 <c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" > 
				<c:set var="test" value="1"></c:set>
				<c:forEach items="${fn:split(requestScope.metaDataMap[fields.fieldName],',')}" var="chkValue" varStatus="i"> 
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
			</c:forEach>
		</td>	
	</c:if>
	<!-- 单选开始,枚举值 -->
	<c:if test="${fields.fieldType eq 'radio' && fields.inDetail eq 1}">
		<c:set var="tab_count" value="${tab_count+1}"></c:set>
		<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
		<td width="40%" align="left">
			<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
				<c:choose>
					<c:when test="${requestScope.metaDataMap[fields.fieldName] eq enmValue}"><!-- 默认选中 -->
						<input name="${fields.fieldName }" style="position: relative;z-index: 99" type="radio" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30" value="${enmValue}" checked="checked"/>${enmValue}
					</c:when>
					<c:otherwise><!-- 为空 -->
						<input name="${fields.fieldName }" style="position: relative;z-index: 199" type="radio" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30" value="${enmValue}" />${enmValue}
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</td>
	</c:if>
	<!-- 下拉框开始,枚举值-->
	<c:if test="${fields.fieldType eq 'select' && fields.inDetail eq 1 }">
		<c:set var="tab_count" value="${tab_count+1}"></c:set>
		<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
		<td width="40%" align="left">
			<select name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} combox" ${fields.notEdit eq '1'?'readonly=readonly':''} size="30">
				<option value="" selected="selected">请选择</option>
				<c:forEach items="${fn:split(fields.enmValue,'~')}" var="enmValue" varStatus="i">
					<c:choose>
						<c:when test="${requestScope.metaDataMap[fields.fieldName] eq enmValue}"><!-- 默认选中 -->
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
		<c:set var="tab_count" value="${tab_count+1}"></c:set>
		<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
		<td width="40%" align="left">
			<c:choose>
				<c:when test="${fields.fieldName eq 'REPLYTIME'}"><!-- 回复时间 -->
					<input type="hidden" name="${fields.fieldName }" value="${requestScope.metaDataMap[fields.fieldName] }" />${requestScope.metaDataMap[fields.fieldName] }
					<c:if test="${fn:length(requestScope.metaDataMap[fields.fieldName]) gt 0 }">
						<input type="hidden" name="hfbsstat" value="1"/>
					</c:if>
					<c:if test="${fn:length(requestScope.metaDataMap[fields.fieldName]) lt 1 }">
						<input type="hidden" name="hfbsstat" value="0"/>
					</c:if>
				</c:when>
				<c:when test="${fields.fieldName eq 'CRTIME'}">
					<input type="hidden" name="${fields.fieldName }" value="${requestScope.metaDataMap[fields.fieldName] }" />${requestScope.metaDataMap[fields.fieldName] }
				</c:when>
				<c:otherwise>
					<input type="text" name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} date"  ${fields.notEdit eq '1'?'readonly=readonly':''} value="${fn:length(requestScope.metaDataMap[fields.fieldName]) gt 10 ?fn:substring(requestScope.metaDataMap[fields.fieldName],0,10):requestScope.metaDataMap[fields.fieldName] }"/><a class="inputDateButton" href="javascript:;">选择</a>
				</c:otherwise>
			</c:choose>
		</td>
	</c:if>	
	<!-- 隐藏域开始 -->
	<c:if test="${fields.fieldType eq 'hidden' && fields.inDetail eq 1}">
		<c:set var="tab_count" value="${tab_count+1}"></c:set>
		<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
		<td width="40%" align="left">
			<input type="hidden" name="${fields.fieldName }" size="50" value="${requestScope.metaDataMap[fields.fieldName] }"/>
		</td>	
	</c:if>	
	</c:if>
</c:forEach>

