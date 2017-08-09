<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<c:forEach items="${requestScope.appFieldRels}" var="fields">
	<c:if test="${fields.fieldStyle ==1}">
		<c:if test="${fields.fieldType eq 'editor' && fields.inDetail eq 1}">
			<c:set var="tab_count" value="2"></c:set>
			<tr>
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
				<td width="90%" align="left" colspan="4">
					<textarea class="${fields.notNull eq '1'?'required':''} xheditor input_w" name="${fields.fieldName }"  rows="15" cols="60" tools="mini"></textarea>
				</td>
			</tr>
		</c:if>
	<!-- 简单编辑器开始 -->
		<c:if test="${fields.fieldType eq 'smeditor' && fields.inDetail eq 1}">
			<c:set var="tab_count" value="2"></c:set>
			<tr>
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
				<td width="90%" align="left" colspan="4">
					<textarea class="${fields.notNull eq '1'?'required':''} xheditor input_w" name="${fields.fieldName }"  rows="30" cols="60" tools="mini"></textarea>
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
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
				<td width="90%" align="left" colspan="4">
					<textarea  name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} textarea input_w" rows="15" cols="100"></textarea>
				</td>
			</tr>
		</c:if>
		<!-- 文本框开始 -->
		<c:if test="${fields.fieldType eq 'text' && fields.inDetail eq 1}">
			<c:set var="tab_count" value="${tab_count+1}"></c:set>
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
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
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
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
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
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
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
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
				<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
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
					<td width="10%" class="i_ctd" align="left">${fields.fieldDesc }</td>
					<td width="40%" align="left">
						<input type="text" name="${fields.fieldName }" class="${fields.notNull eq '1'?'required':''} date"  /><a class="inputDateButton" href="javascript:;">选择</a>
					</td>
				</c:otherwise>
			</c:choose>
		</c:if>	
		<!-- 隐藏域开始 -->
		<c:if test="${fields.fieldType eq 'hidden' && fields.inDetail eq 1}">
			<p class="_p">
				<label>${fields.fieldDesc }</label>
				<input type="hidden" name="${fields.fieldName }" class="date" size="50" maxlength="${fields.fieldLength }"/><a class="inputDateButton" href="javascript:;">选择</a>
				</p>	
			</c:if>	
	</c:if>
</c:forEach>
				