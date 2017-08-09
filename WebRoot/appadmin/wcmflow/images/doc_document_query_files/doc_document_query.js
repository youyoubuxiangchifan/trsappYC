$.request = (function () { 
	var apiMap = {}; 
	function request(queryStr) { 
		var api = {}; 
		if (apiMap[queryStr]) { return apiMap[queryStr]; } 
		api.queryString = (function () { 
			var urlParams = {}; 
			var e, 
			d = function (s) { return decodeURIComponent(s.replace(/\+/g, " ")); }, 
			q = queryStr.substring(queryStr.indexOf('?') + 1), 
			r = /([^&=]+)=?([^&]*)/g; 
			while (e = r.exec(q)) 	urlParams[d(e[1])] = d(e[2]);
			return urlParams; 
		})(); 
		api.getUrl = function () { 
			var url = queryStr.substring(0, queryStr.indexOf('?') + 1); 
			for (var p in api.queryString) { url += p + '=' + api.queryString[p] + "&";} 
			if (url.lastIndexOf('&') == url.length - 1) { return url.substring(0, url.lastIndexOf('&')); } 
			return url; 
		} 
		apiMap[queryStr] = api; 
		return api; 
	} 
	$.extend(request, request(window.location.href)); 
	return request; 
})();

$(function(){
	var GY_STATUS_ALL = "";			//全部
	var GY_STATUS_SENT = "10";		//已发
	var GY_STATUS_REFUSED = "15"   //已否
	var GY_STATUS_NEEDREVIEW = "2"	//待审

	var GY_METAVIEWDATA_NORMAL = 2;	//新闻稿件
	var GY_METAVIEWDATA_PICS = 1;	//组图稿件
	var GY_METAVIEWDATA_BOTH = 0;	//全部

	var siteids = $("#siteids").val();
	//全部、待审、已审
	//
	// 根据系统参数确定显示状态
	var $statusTypeLi;
	var docstatus =$.request.queryString["docstatus"];
	if(docstatus == GY_STATUS_SENT){
		$statusTypeLi = $("#sent");
		$("#check").hide();
	}else if(docstatus == GY_STATUS_REFUSED){
		$statusTypeLi = $("#refused");
		$("#check").hide();
	}else if(docstatus == GY_STATUS_NEEDREVIEW){
		$statusTypeLi = $("#needreview");
		$("#check").show();
	}else{
		$statusTypeLi = $("#all-review");
		$("#check").hide();
	}
	
	$statusTypeLi.parent().parent().find(".on").removeClass("on");
	$statusTypeLi.addClass("on");

	$("#all-review").bind("click", function(){
		var json = {
			"SiteId" : siteids,
			"docstatus": GY_STATUS_ALL
		}
		queryPage(json);
	});
	$("#sent").bind("click", function(){
		var json = {
			"SiteId" : siteids,
			"docstatus": GY_STATUS_SENT
		}
		queryPage(json);
	});
	$("#needreview").bind("click", function(){
		var json = {
			"SiteId" : siteids,
			"docstatus": GY_STATUS_NEEDREVIEW
		}
		queryPage(json);
	});
	$("#refused").bind("click", function(){
		var json = {
			"SiteId" : siteids,
			"docstatus": GY_STATUS_REFUSED
		}
		queryPage(json);
	});

	//绑定编辑、预览、置顶
	$("#review").bind("click",function(){//预览
    	PageContext.review($(this));
    })
    $("#check").bind("click",function(){//
    	PageContext.check($(this));
    })
	$("#editBtn").bind("click",function(){//编辑
    	PageContext.edit($(this));
    })
	$("#setTop").bind("click",function(){//置顶
    	PageContext.setTop($(this));
    })
	$(".metaTitle").click(function(){//文本链接
    	PageContext.view($(this));
    	return false;
    })
	$("#sendWeibo").bind("click",function(){//发微博
    	PageContext.sendWeibo($(this));
    })
    $("#publish").bind("click",function(){//发布
    	PageContext.publish($(this));
    })

	//检索
	$("#search_button").bind("click", function(){
		var json = {
			"SiteId" : siteids,
			"DocTitle" : $("#textfield").val()
		}
		queryPage(json);
	});

	//绑定全选
   $("#selectAll").click(function(){
   		$(this).toggleClass("cur");
		if($(this).attr("class") == 'cur'){
			$("label").each(function(){
				$(this).addClass("cur");
			})
		}else{
			$("label").each(function(){
				$(this).removeClass("cur");
			})
		}
  });

   var sort = $.request.queryString["SORT"];
   if(null==sort){
   		sort = "DESC";
   }

   //绑定排序  
   $(".table-title").children(".t4").bind("click",function(){
   		var url = window.location.href;
   		if(url.indexOf("SORT")>0){
	   		url = url.substring(0,url.indexOf("SORT")-1);
   		}
   		if(sort == "DESC"){
   			sort = "ASC";
   			window.location.href = url + "&SORT=ASC";
   		}else{
   			sort = "DESC";
   			window.location.href = url + "&SORT=DESC";
   		}
   		return false;
   })

	var t8_img_select;
	//绑定操作下拉的事件
	$(".t6 img").live("click", function(event){
		$(".select-div").each(function(){
			$(this).children("img").eq(1).hide();
			$(this).children("img").eq(0).show();
			$(this).removeClass().addClass("t-mar");
		});
			var $this = $(this);
		/*$(".t01 ul li label").each(function(){
			$(".t01 ul li label").removeClass("cur");
			$this.parent().parent().parent().find("label").addClass("cur");
		})*/

		var x = event.pageX - 100;
		var y = event.pageY + 5;
		if(t8_img_select&&t8_img_select != this){
			//$(t8_img_select).attr("src", "../images/doc/wz-icon11.png");
			$(t8_img_select).parent().removeClass().addClass("t-mar");
			$(".operation").addClass("hide");
		}
		var sta=0;
		var _sDocStatus = $(this).attr("sDocStatus");
		if(_sDocStatus&& _sDocStatus=="已编"){
			$("#check").show();
			sta=1;
		}else{
			$("#check").hide();
		}
		
		if((_sDocStatus&& _sDocStatus=="正审" )){
			$("#publish").show();
			sta=1;
		}else{
			$("#publish").hide();
		}
		
		var chirdren=jQuery("#operDiv").children();
		
		for(var j=0;j<chirdren.length;j++){
			var ele=chirdren[j];
			if(ele.style.display==""){
				sta=1;
			}
		}
		if(sta==0){
			return;
		}
		
		$(".operation").toggleClass("hide").css("left",x).css("top",y);
		// type
		$(".operation").attr("_type", $(this).closest(".table_tr").attr("_type"));
		// docid
		$(".operation").attr("_docid", $(this).closest(".table_tr").attr("_docid"));
		$(".operation").attr("_chnlDocId", $(this).closest(".table_tr").attr("_chnlDocId"));
		$(".operation").attr("_mediaType", $(this).closest(".table_tr").attr("_mediaType"));
		$(".operation").attr("_viewId", $(this).closest(".table_tr").attr("_viewId"));
		$(".operation").attr("_channelId", $(this).closest(".table_tr").attr("_channelId"));
		
		if($(".operation").is(":hidden")){
			t8_img_select = null;
		}else{
			$(this).parent().addClass("select-div");
			t8_img_select = this;
		}
		return false;
	});

	/**
	$(".operation").live("mouseout", function(){
		if($(this).is(":not(.hide)")){
			$(this).addClass("hide");
		}
	});
	*/
	$(".t6").hover(function(){
		$(this).children("div").children("img").eq(0).hide();
		$(this).children("div").children("img").eq(1).show();
		$(this).children("div").removeClass("t-mar").addClass("t-mar1");
	}, function(){
		if($(".operation").is(":hidden")||(t8_img_select&&t8_img_select!=this)&&!$(this).children("div").is(".select-div")){
			$(this).children("div").children("img").eq(1).hide();
			$(this).children("div").children("img").eq(0).show();
			$(this).children("div").removeClass().addClass("t-mar");
		}
	});

	$(window.parent.document).live("click",function(e){
		var click_obj = $(e.target);
		
		//列表操作弹出层
		if($(".operation").is(":visible")){
			if(click_obj.closest(".operation,.t6 img").length == 0){
				$(".operation").addClass("hide");
//				$(".t6 img").attr("src", "../images/doc/wz-icon09.png");
				$(".t6 div").each(function(){
					$(this).find("img:eq(1)").hide();
					$(this).find("img:eq(0)").show();
				});
				$(".t6 img").parent().removeClass().addClass("t-mar");
				t8_img_select = null;
			}
		}
		//新建稿件弹出层
		if($(".upload-anothers").is(":visible")){
			if(click_obj.closest(".upload-anothers,.addother").length == 0){
				$(".upload-anothers").addClass("hide");
			}
		}
	});

	$(document).live("click",function(e){
		var click_obj = $(e.target);
		
		//列表操作弹出层
		if($(".operation").is(":visible")){
			if(click_obj.closest(".operation,.t6 img").length == 0){
				$(".operation").addClass("hide");
				$(".t6 div").each(function(){
					$(this).find("img:eq(1)").hide();
					$(this).find("img:eq(0)").show();
				});
				$(".t6 img").parent().removeClass().addClass("t-mar");
				t8_img_select = null;
			}
		}
		//新建稿件弹出层
		if($(".upload-anothers").is(":visible")){
			if(click_obj.closest(".upload-anothers,.addother").length == 0){
				$(".upload-anothers").addClass("hide");
			}
		}	
	});



  //点击一行选定checkbox
  $(".table-tr01,.table-tr02").each(function(){
	  $(this).click(function(e){
		 var click_obj = $(e.target);
		 if(!click_obj.is(".t6 img")){
				$(this).find("label").toggleClass("cur");
		 }
	  });
  });
});

function queryPage(_param){
	//需考虑当前传入参数，取交集
	//..to do

	var searchValue = "?";
	for (var el in _param) {
		searchValue += el + "=" +_param[el] + "&";
	}
	searchValue = searchValue.substring(0,searchValue.length-1);
	if(!searchValue) {
		return;
	}
	var url = window.location.href;
	if(url.indexOf("?")>-1) {
		url = url.substring(0,url.indexOf("?"));
	}
	window.location.href = url + searchValue;
}

var PageContext = {
	m_channelId : $.request.queryString["ChannelId"],
	m_addedit : {
		documentUrl : "metaviewdata_addedit.jsp",
	},
	m_view : {
		documentUrl : "viewdata_detail.jsp",
	},
	
	availHeight : window.screen.availHeight-60,
	availWidth  : window.screen.availWidth-10,
	
	check : function(_$this){
		var $operation = _$this.closest(".operation");
		var docIds = $operation.attr("_docid");
		var viewId= $operation.attr("_viewId");
		if(!docIds) {
			return false;
		}
		var url = "../" +viewId + "/" + "metaviewdata_check.jsp" + "?DocIds=" + docIds;
		var mDialog = new parent.TRSDialog({
			url : url,
			width : "608",
			height : "499"
		});

		mDialog.open();

		if($operation.is(":not(.hide)")){
			$operation.addClass("hide");
		}

	},
	edit : function(_$this){
		var $operation = _$this.closest(".operation");
		var docId = $operation.attr("_docid");
		var type = $operation.attr("_type");
		var mediaType = $operation.attr("_mediaType");
		var viewId= $operation.attr("_viewId");
		var channelId = $operation.attr("_channelId");
		var url="";
		url = PageContext.m_addedit.documentUrl + "?Type=" + type + "&ChannelId=" + channelId + "&DocId=" + docId + "&ObjectId=" + docId;
		var click_a = $("<a id='url_click' href='../" +viewId + "/" +url+"' target='_blank'></a>");
		$("body").append(click_a);
		document.getElementById("url_click").click(); 
		$("#url_click").remove();
		if(jQuery(".operation").is(":visible")){
			$(".operation").addClass("hide");
		}
//		window.open(url, "_blank","height="+this.availHeight+",width="+this.availWidth+",toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes");
	},
	review : function(_$this){
		var $operation = _$this.closest(".operation");
		var chndocIds = $operation.attr("_chnldocid");
		var Helper=new BasicDataHelper();
		var chndocId = chndocIds.substring(chndocIds.lastIndexOf(",")+1);
		var json={"OBJECTIDS":chndocId};
		var url = window.location.href;
		url = url.substring(0,url.indexOf("/wcm"));
		if(jQuery(".operation").is(":visible")){
			$(".operation").addClass("hide");
		}
		Helper.Call("mw_viewdocument","preview",json,(function(data){
			 data = eval('('+data+')');
			 subUrl = data.DATA[0].URLS[0];
			 if(typeof(subUrl)=="undefined"){
				alert("栏目未配置细缆模版");
			 }else{
				url = url + subUrl;
				window.open(url, "_blank","toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes");
			 }
		}),(function(msg){
	    	alert(msg);
	    }));
	},
	
	setTop:function(_$this){
		var $operation = _$this.closest(".operation");
		var channelId = $operation.attr("_channelId");
		var docId = $operation.attr("_docid");
		var viewId=0;
		   $.ajax({
				async: false,
				type:'get',
				url:'../common/getMetaviewdataId.jsp?ChannelId=' + channelId,
				dataType:'json',//服务器返回的数据类型
				success:function(msg){
					viewId = msg;
				}
			});
		var url = "../" +viewId + "/" + "metaviewdata_settop.jsp" + "?channelId=" + channelId + '&docId='+docId;
		var mDialog = new TRSDialog({
			url : url,
			width : "610",
			height : "385"
		});
		// 打开窗体
		mDialog.open();
		if(jQuery(".operation").is(":visible")){
			$(".operation").addClass("hide");
		}
	},
	
	getSelecteddocIds : function(){
		var chnldocIds = "";
		$("label[class='cur']").each(function(){
			var $el = $(this);
			if($el.attr("id") == "selectAll") {
				return true;
			}
			var chnldocId = $el.prev("input").val();
			chnldocIds += "," + chnldocId;
		});
		if(chnldocIds.length > 0) {
			chnldocIds = chnldocIds.substring(1);
		}
		return chnldocIds;
	},
	view : function(_$this){
		var parentDiv = _$this.parent().parent().find("input");
		var type = parentDiv.attr("_type");
		var docId = parentDiv.attr("docid");
		var mediaType = parentDiv.attr("_mediatype");
		var chnldocId = parentDiv.val();
		var viewId= _$this.attr("viewId");
		var channelId = _$this.attr("_channelId");
		var url;
		url = this.m_view.documentUrl+"?DocIds="+docId+"&ChannelId="+channelId+"&type="+type;
		var click_a = $("<a id='url_click' href='../" +viewId + "/"+url+"' target='_blank'></a>");
		$("body").append(click_a);
		document.getElementById("url_click").click(); 
		$("#url_click").remove();
		if(jQuery(".operation").is(":visible")){
			$(".operation").addClass("hide");
		}
//		window.open(url, "_blank","height="+this.availHeight+",width="+this.availWidth+",toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes");
	},
	sendWeibo : function(_$this){
		var $operation = _$this.closest(".operation");
		var docId = $operation.attr("_docid");
		var channelId = $operation.attr("_channelId");
		var url = "../../../mw/scm/microblog_addedit.jsp?docId="+docId+"&channelId=" + channelId;
//		window.open(url, "_blank","height="+this.availHeight+",width="+this.availWidth+",toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes");
		var click_a = $("<a id='url_click' href='"+url+"' target='_blank'></a>");
		$("body").append(click_a);
		document.getElementById("url_click").click(); 
		$("#url_click").remove();
		if(jQuery(".operation").is(":visible")){
			$(".operation").addClass("hide");
		}
	},
	publish:function(_$this){
		var $operation = _$this.closest(".operation");
		var _chnlDocId = $operation.attr("_chnlDocId");
		var url = "../common/doc_viewdata_publish.jsp?ChnlDocIds=" + _chnlDocId;
		var mDialog = new parent.TRSDialog({
			url : url,
			width : "610",
			height : "339"
		});
		// 打开窗体
		mDialog.open();
		if(jQuery(".operation").is(":visible")){
			$(".operation").addClass("hide");
		}
	}
}
window.onload = function(){
   var sort = $.request.queryString["SORT"];
   if(null==sort){
   		sort = "DESC";
   }
      if(sort == "DESC"){
          $(".t4").children("img").eq(1).addClass("hide");
          $(".t4").children("img").eq(0).removeClass("hide");
      }else{
          $(".t4").children("img").eq(0).addClass("hide");
          $(".t4").children("img").eq(1).removeClass("hide");
      }
}