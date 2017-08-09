<%@page pageEncoding="GBK" %>
<script type="text/javascript">
	//表单提交
	function submit_(startPage) {
		document.getElementById("startPage").value=startPage;
		document.getElementById("myForm").submit();
	}
	//表单提交
	function submit_2(startPage,myFormId) {
	
		document.getElementById("startPage").value=startPage;
		document.getElementById(myFormId).submit();
	}
</script>


<div class="quotes">  ${startPage }/${totalPage }
	<span class="disabled">
		<c:choose>
		<c:when test="${requestScope.startPage > 1}">
			<a href="javascript:submit_(${requestScope.startPage-1});"><</a>
		</c:when>
		<c:otherwise>
			<
		</c:otherwise>
		</c:choose>
	</span>
	<c:forEach var="i" begin="${requestScope.pager[0]}"
	end="${requestScope.pager[1]}">
	<c:choose>
		<c:when test="${i eq requestScope.startPage}"><span class="current">${i}</span></c:when>
		<c:otherwise>
			<a href="javascript:submit_(${i})">${i}</a>

		</c:otherwise>
		</c:choose>
	</c:forEach>
<c:choose>
	<c:when test="${requestScope.startPage < requestScope.totalPage}">
		<a href="javascript:submit_(${requestScope.startPage+1});">></a>
	</c:when>
	<c:otherwise><a>></a></c:otherwise>
</c:choose>
	 <input type="text" id="startPage1" value="" size="5" maxlength="9" onblur="this.value=this.value.replace(/\D/g,'');" onkeyup="this.value=this.value.replace(/\D/g,'');" >页<input type="button" value="GO" id="button" onclick="insert_startpage()">
<script>
function insert_startpage(){
if(document.getElementById("startPage1").value!=""){
	submit_(document.getElementById("startPage1").value);
}else{
submit_(1);
}

}</script>
</div>
