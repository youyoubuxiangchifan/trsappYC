<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageHeader" style="border:1px #B8D0D6 solid">
<div class="searchBar">
            <label style="width:50px;text-align: right;">年份：</label>
			<select id="wyear" name="year" onchange="getWdate(this.value)" style="width:100px;">
			   <c:forEach var="year" begin="${year-5}" end="${year+5}" step="1"> 

         			<option value="${year}">${year}</option>

				</c:forEach> 
			   
				
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="smtWdate()">保存</button>
</div>
</div>
<div class="pageContent">
<div id="div1" ></div>
</div>
<script language="javascript" type="text/javascript">
     var _specialDates = new Array();
   
      var param = {
			eCont:'div1',
			firstDayOfWeek:1,
			doubleCalendar:false,
			specialDates:_specialDates,
			onpicked:setDate
	  }
	 function setDate(dp){
	     var _selDate = dp.cal.getDateStr();
		 param.startDate = _selDate;
		 var _ishas = false;
		 for(i=0;i<_specialDates.length;i++){
 			if(_specialDates[i]==_selDate){
 					_specialDates.splice(i,1);
					_ishas = true;
 				}
 		}
		if(!_ishas)
			_specialDates.push(_selDate);
		 WdatePicker(param);
	 }
	 function setDateParam(_year,_result){
	 	param.minDate = _year+'-01-01';
	    param.maxDate = _year+'-12-31';
	    param.startDate = _year+'-01-01';
	    if(_result==""){
	 		    _specialDates = new Array();
	 			_specialDates.push(_year+'-05-01');
	 			_specialDates.push(_year+'-10-01');
	 		}else{ 
	 			_specialDates = _result.split(',');
	       }
	   param.specialDates = _specialDates;    
	 }
	 //根据年份查询节假日安排
	 function getWdate(_value){
	 	var _year = _value;
	 	var _param = {
	 		year : _year
	 	}
	 	$.post('sysconfig.do?method=findWday',_param,function(_result){
	 		if(_result=='false'){
    			alertMsg.info('查询操作失败。');
	 		}else{
	 		   setDateParam(_year,_result);
	 		   WdatePicker(param);
	 		}
	 		
	 	});
	 }
	 function smtWdate(){
	 	var _year = $('#wyear').val();
	 	var _dates = _specialDates.toString();
	 	var _param = {
	 		year : _year,
	 		dates :_dates
	 	}
	 	$.post('sysconfig.do?method=saveWday',_param,function(_result){
	 		if(_result=='false'){
    			alertMsg.info('工作日设置失败。');
	 		}else{
	 		  alertMsg.info('保存成功。');
	 		}
	 	});
	 }
	 function init(_year,_result){
	   
	    $('#wyear').val(_year);
	 	if(_result=='false'){
	 		alertMsg.info('查询操作失败。');
	 	}else{
	 		setDateParam(_year,_result);
	 		WdatePicker(param);
	 	}
	 }
	 init('${year}','${result}');
	 //WdatePicker(param);
</script>