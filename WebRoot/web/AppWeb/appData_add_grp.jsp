<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<c:if test="${fstep.first}" >
<tr>
	<td width="21%" align="right">${isGrpFields[0].fieldDesc}：</td>
	<td width="79%">
	<c:choose>
		<c:when test="${isGrpFields[0].fieldType=='select' }">
			<select id="ngrpName" name="ngrpName"  style="width:155px;margin-top:2px;" class="i_iB " onchange="js_changetab(this.value)">
				<c:forEach items="${fn:split(isGrpFields[0].enmValue,'~')}" var="enmValue" varStatus="i">
					<c:if test="${i.first}" ><c:set var="firstfield" >${enmValue}</c:set></c:if>
				 	<option value="${enmValue}"  ${i.last ? "" : "selected"}>${enmValue}</option>
				</c:forEach>
			</select>
		</c:when>
		<c:otherwise>
			<c:forEach items="${fn:split(isGrpFields[0].enmValue,'~')}" var="enmValue" varStatus="i">
				<c:if test="${i.first}" ><c:set var="firstfield" >${enmValue}</c:set></c:if>
			 	<input type="radio" name="ngrpName" ${i.last ? "" : "checked"} value="${enmValue}" onclick="js_changetab(this.value)"/>${enmValue}
			</c:forEach>
		</c:otherwise>
	</c:choose>
	</td>
</tr>
<c:forEach items="${requestScope.fieldMap}" var="valueMap" varStatus="vfield">
<tr>
	<td colspan="2">
		<table width="100%" id="${valueMap.key }" class="i_Gr_7 mb10 js_tab">
			<c:forEach items="${valueMap.value}" var="field" varStatus="i">
			<c:if test="${field.notNull == 1}">
	       		<c:set var="required" value="required:true,"/>
	       	</c:if>
	       	<c:if test="${field.fieldLength > 0}">
	       		<c:set var="maxlength">maxlength:${field.fieldLength},</c:set>
	       	</c:if>
	       	<c:if test="${fn:length(field.formFieldType) > 0}">
	       		<c:set var="formFieldType">${field.formFieldType}:true,</c:set>
	       	</c:if>
	       	<c:set var="validateJson" value="${required}${maxlength }${formFieldType }" />
	       	<c:if test="${fn:length(validateJson) > 0}">
	       		<c:set var="validateJson" value="${fn:substring(validateJson, 0, (fn:length(validateJson) - 1))}" />
	       	</c:if>
				<tr id="${valueMap.key }" style="display: ${vfield.last?'block':'none'}">
					<c:choose>
					   <c:when test="${field.fieldType == 'text' or field.fieldType == 'password'
					    or field.fieldType == 'date' or field.fieldType == 'number'}">
					   		<td width="21%" align="right">${field.fieldDesc}：</td>
							<td width="79%">
								<input type="${field.fieldType == 'password'?'password':'text'}" 
									name="${field.fieldName}" ${vfield.last ? "" : "disabled"} 
									class="i_Gr_1_ipt2 i_iB {${validateJson }}"
									<c:if test="${field.fieldType == 'date'}">  onClick="WdatePicker();" </c:if>
								/>
								<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
							</td>
					   </c:when>
					         <c:when test="${field.fieldType == 'select' && field.fieldId ne isGrpFields[0].fieldId}">
					   		<td width="21%" align="right">${field.fieldDesc}：</td>
							<td width="79%">
								<select name="${field.fieldName}" class="i_iB {${validateJson }}" ${vfield.last ? "" : "disabled"} >
								<c:forEach items="${field.enmList}" var="enm">
											<option value="${enm}" 
											<c:if test="${field.defaultValue==enm}"> selected="selected" </c:if>
											>${enm}</option>
									</c:forEach>		
								</select>
								<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
							</td>
					   </c:when>
					   <c:when test="${field.fieldType == 'checkbox'}">
					   		<td width="21%" align="right">${field.fieldDesc}：</td>
							<td width="79%">
								<span>
									<c:forEach items="${field.enmList}" var="enm">
												<input type="checkbox" class="{${validateJson }}" ${vfield.last ? "" : "disabled"}  name="${field.fieldName}_cbox" value="${enm}" <c:if test="${field.defaultValue==enm}">checked="checked"</c:if>/>${enm}
										</c:forEach>
									</span>		
								<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
								<input id="${field.fieldName}" name="${field.fieldName}" type="hidden" />
								<c:set var="cboxName" value="${field.fieldName},${cboxName}" />
							</td>
					   </c:when>
					   <c:when test="${field.fieldType == 'radio' && field.fieldId ne isGrpFields[0].fieldId}">
					   		<td width="21%" align="right">${field.fieldDesc}：</td>
							<td width="79%">
								<span>
								<c:forEach items="${field.enmList}" var="enm">
											<input type="radio" class="{${validateJson }}" name="${field.fieldName}" ${vfield.last ? "" : "disabled"}  value="${enm}" <c:if test="${field.defaultValue==enm}">checked="checked"</c:if>/>${enm}
									</c:forEach>	
									</span>	
								<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
							</td>
					   </c:when>
					   <c:when test="${field.fieldType == 'editor' || field.fieldType == 'smeditor' || field.fieldType == 'textarea'}">
							<c:if test="${field.fieldId ne isGrpFields[0].fieldId}"> 
								 <td width="21%" align="right">${field.fieldDesc}：</td>
								 <td width="79%">
								 	<textarea name="${field.fieldName}" ${vfield.last ? "" : "disabled"}  cols="" rows="" class="i_Gr_1_ipt4 {${validateJson }}" ></textarea>
								 	<c:if test="${field.notNull=='1'}"> &nbsp;<span class="i_red">*</span> </c:if>
								 </td>
							</c:if>
					  </c:when>
					</c:choose>
				</tr>
			<c:set var="required" value="" />
       		<c:set var="maxlength" value="" />
       		<c:set var="formFieldType" value="" />
       		<c:set var="validateJson" value="" />
			</c:forEach>
		</table>
	</td>
</tr>
</c:forEach>
</c:if>
