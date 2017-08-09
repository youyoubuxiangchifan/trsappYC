var isFocus = false;
var isSubChannel = false;
var CALLSERVICEURL = "doc_metaviewdata_query.jsp?";
var globleChannel = "";

var chnTemp = [
		'<a id="{0}" title="{1}" href="javascript:void(0);" class="list01" param="{siteId:{3},channelId:{2}}" param1="{SiteId:{3},OperID:{2},Type:1}" onclick="queryChnldoc({2});saveOperationRecord({2})">',
		'<div class="left nav-icon i">',
		'<img  class="" src="../images/doc/wz-icon00{4}.png" alt="" />',
		'</div>', '<div class="left nav-c ft12">{5}</div>', '</a>' ].join("");

var childTemp = [
		'<a id="secondLayerParent_{0}"  title="{1}" href="javascript:queryChnldoc({0});" class="list02" style="width:80px;margin-right:15px;overfolow:hidden; white-space:nowrap;text-overflow:ellipsis;line-height:43px;padding-right:10px;">',
		'<span style="width:47px;padding-right:15px;margin-right:5px;display:inline-block;text-overflow:hidden;overflow:hidden;height:30px;white-space:nowrap;line-height:43px;">',
		'{2}', '</span>', '</a>' ].join("");

// 截取字符串
function SetString(str1, len) {
	var strlen = 0;
	var str = new String(str1);
	var s = "";
	for ( var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) > 128) {
			strlen += 2;
		} else {
			strlen++;
		}
		s += str.charAt(i);
		if (strlen >= len) {
			return s;
		}
	}
	return s;
}

// 获取2级栏目
function loadChildrenChannel(id, siteId, func) {
	globleChannel = id;
	$("#firstLayer,#secondLayer,#thirdLayer").hide();
	var currElement = $("#channel_article_" + id);

	queryChnldoc(id);// 添加点击报站点 2014-8-22 12:03:44

	// currElement.parent().parent().siblings("ul[class='bg4']").children("div").hide();
	var json = currElement.attr("param");
	json = eval('(' + json + ')');
	var Helper = new BasicDataHelper();
	Helper.Call("mw_channel", "query", json,
			(function(data) {
				if (data) {
					$("#channel_article_children_" + id).html("");// 清空
					var str = "";
					var channelId = "";
					var currObject = data.Channels || data.Channel;
					for ( var i = 0; i < currObject.length; i++) {
						ichannel = currObject[i];
						if (i == 0 && currObject[i].Channel) {
							ichannel = currObject[i].Channel;
						}
						channelId = ichannel.CHANNELID;
						var chnldesc = ichannel.CHNLDESC;
						var channelIdName = "curr" + channelId;
						str += String.format(chnTemp, channelIdName, chnldesc,
								channelId, siteId, json.order, SetString(
										chnldesc, 15));
					}

					$("#channel_article_children_" + id).html(str);
					$("#firstLayer").attr("parentId", "curr" + channelId);
					addRightHover("curr" + channelId);
					$("#channel_article_children_" + id).show();
					$(".list01").each(function() {
						$(this).click(function() {
							$(".list01").removeClass("onlist01");
							$(this).addClass("onlist01");
						})
					});

					if (func) {
						func();
					}
				} else {
					// alert("没有子频道!");
				}
			}), (function(msg) {
				alert(msg);
			}), "xml", "json");

	/*var rightValue = currElement.attr("rightValue");
	// 编辑文档 32
	if (rightValue.substr(32, 1) != 1) {
		$("#serviceFrame").contents().find("#editBtn").hide();
	} else {
		$("#serviceFrame").contents().find("#editBtn").show();
	}

	// 预览文档38
	if (rightValue.substr(38, 1) != 1) {
		$("#serviceFrame").contents().find("#review").hide();
	} else {
		$("#serviceFrame").contents().find("#review").show();
	}

	// 删除文档 移动 33
	if (rightValue.substr(33, 1) != 1) {
		$("#delbtn").hide();
		$("#movbtn").hide();
	} else {
		$("#delbtn").show();
		$("#movbtn").show();
	}

	// 发布 撤稿 38
	if (rightValue.substr(38, 1) != 1) {
		$("#pubbtn").hide();
		$("#cgbtn").hide();
		$("#serviceFrame").contents().find("#sendWeibo").hide();
	} else {
		$("#pubbtn").show();
		$("#cgbtn").show();
		$("#serviceFrame").contents().find("#sendWeibo").show();
	}

	// 引用 34
	if (rightValue.substr(34, 1) != 1) {
		$("#mirbtn").hide();
	} else {
		$("#mirbtn").show();
	}*/
}

// 获取三级栏目
function loadChildrenChannelList(id, subChannel) {
	globleChannel = id;
	var json = $("#" + id).attr("param");
	json = eval('(' + json + ')');
	var Helper = new BasicDataHelper();
	var str = "";
	Helper.Call("mw_channel", "query", json, (function(data) {
		isSuccess = true;
		if (data) {
			str += "<ul>";
			var channelId = "";
			var currObject = data.Channel || data.Channels;
			for ( var i = 0; i < currObject.length; i++) {
				schannel = currObject[i];
				if (i == 0 && currObject[i].Channel) {
					schannel = currObject[i].Channel;
				}
				channelId = schannel.CHANNELID;
				var chnldesc = schannel.CHNLDESC;
				str += String.format(childTemp, channelId, chnldesc, SetString(
						chnldesc, 15));
			}
			str += "</ul>"
		}

		if (str != null && str != "") {
			subChannel.html(str);
			subChannel.show();
		} else {
			subChannel.html("");
			subChannel.hide();
		}
		$(".list02").each(function() {
			$(this).click(function() {
				$(".list02").removeClass("onlist02");
				$(".list01").removeClass("onlist01");
				$(this).addClass("onlist02");
			})
		});
	}), (function(msg) {
		alert(msg);
	}), "xml", "json");
	
}

function queryChnldoc(_channelId) {
	var url = "doc_document_common_query.jsp?ChannelId=";
	$("#serviceFrame").attr("src", url + _channelId);
	
	//权限设定
	var Helper = new BasicDataHelper();
	
	var json = {
	        "OBJTYPE": 101,
	        "OBJID": _channelId
	      }
	
	Helper.Call("mw_extAuth", "queryObjectRights", json,
    function(data) {
		var rightValue = data
		// 编辑文档 32
		if (rightValue.substr(32, 1) != 1) {
			$("#serviceFrame").contents().find("#editBtn").hide();
		} else {
			$("#serviceFrame").contents().find("#editBtn").show();
		}

		// 预览文档38
		if (rightValue.substr(38, 1) != 1) {
			$("#serviceFrame").contents().find("#review").hide();
		} else {
			$("#serviceFrame").contents().find("#review").show();
		}

		// 删除文档 移动 33
		if (rightValue.substr(33, 1) != 1) {
			$("#delbtn").hide();
			$("#movbtn").hide();
		} else {
			$("#delbtn").show();
			$("#movbtn").show();
		}

		// 发布 撤稿 38
		if (rightValue.substr(38, 1) != 1) {
			$("#pubbtn").hide();
			$("#cgbtn").hide();
			$("#serviceFrame").contents().find("#sendWeibo").hide();
		} else {
			$("#pubbtn").show();
			$("#cgbtn").show();
			$("#serviceFrame").contents().find("#sendWeibo").show();
		}

		// 引用 34
		if (rightValue.substr(34, 1) != 1) {
			$("#mirbtn").hide();
		} else {
			$("#mirbtn").show();
		}
	},
    function(msg){
    	alert(msg);
    });
}

function addSubChannelMouseOver(parentDom, currDom) {
	currDom.hover(function() {
		// mouseOver
		isFocus = true;
		isSubChannel = true;
		parentDom.mouseover();
	}, function() {
		// mouseOut
		isFocus = false;
		isSubChannel = false;
		parentDom.mouseout();
	});
}

function addRightHover(id) {
	var subChannel = $("body").find("div[parentId='" + id + "']");
	$("#" + id).parent().children().hover(function() {// 传进来的任意ID都添加hover事件
		isFocus = true;
		var currId = $(this).attr("id");
		addSubChannelMouseOver($(this), subChannel);
		if (!isSubChannel) {
			loadChildrenChannelList(currId, subChannel);
		}
	}, function() {
		setInterval(function() {
			if (!isFocus) {
				isSubChannel = false;
				subChannel.hide();
			}
		}, 1000);
	});
}

// 删除文章
function deleteByObjectId() {
	var str = getMetaViewIds(true);
	var strArr = "";
	strArr = str.split(",");
	if (strArr.length > 0) {
		var Helper = new BasicDataHelper();
		var json = {
			"OBJECTIDS" : str
		};
		Helper.Call("mw_viewdocument", "delete", json, (function(data) {
			$("iframe")[0].contentWindow.location.reload();
		}), (function(msg) {
			alert(msg);
		}), "xml", "json");
	} else {
		alert("选择为空,请重新选择需要删除的内容!");
	}
	window.parent.window.removeDialog();
}

// 引用/移动文章到指定栏目
function mirrorOrMoveByObjectId(method) {
	var obIds = getMetaViewIds(true), channelIds = getChannelIds();
	var strArr1 = "", strArr2 = "";
	strArr1 = obIds.split(",");
	strArr2 = channelIds.split(",");

	if (strArr1.length > 0 && strArr2.length > 0) {
		var Helper = new BasicDataHelper(), mName = "mirror";
		var json = {
			"OBJECTIDS" : obIds,
			"ToChannelIds" : channelIds
		};
		if (method) {
			mName = "move";
			json = {
				"OBJECTIDS" : obIds,
				"ToChannelId" : channelIds
			};
		}
		Helper.Call("mw_viewdocument", mName, json, (function(data) {
			window.location.reload();
		}), (function(msg) {
			alert(msg);
		}), "xml", "json");
	} else {
		alert("选择为空,请重新选择需要引用/移动的内容!");
	}
	window.parent.window.removeDialog();
}

var isDeleteSuccess = false;
function OpenDeleteConfirmation(arg, prame, width, height) {
	var channelIds = getMetaViewIds(false)
	var w = 608;
	var h = 570;
	if (channelIds.length > 0) {
		var URL = arg + getMetaViewIds(false);
		if (prame) {
			var str = "ChannelId=" + globleChannel + "&ChnlDocIds="
					+ getMetaViewIds(true);
			w = 730;
			h = 710;
			URL = arg + str;
		}
		var mDialog = new TRSDialog({
			url : URL,
			width : width ? width : w,
			height : height ? height : h
		});
		// 打开窗体
		mDialog.open();
	} else {
		alert("请正确选择!");
	}
}

function OpenChoice(arg) {
	// /wcm/app/application/core/com.trs.ui/mw_publishmedia/mediaTypeSelect.jsp?channletreeid=PublishChannel
	var ChnlDocIds = getMetaViewIds(true);
	if (ChnlDocIds.length > 0) {
		var URL = arg + "&chnldoc=" + ChnlDocIds;
		var mDialog = new TRSDialog({
			url : URL,
			width : "885",
			height : "584"
		});
		// 打开窗体
		mDialog.open();
	} else {
		alert("请正确选择!");
	}
}

function OpenConfirmation(arg, width, height) {
	var channelIds = getMetaViewIds(true)
	if (channelIds.length > 0) {
		var URL = arg + channelIds;
		var mDialog = new TRSDialog({
			url : URL,
			width : width,
			height : height
		});
		// 打开窗体
		mDialog.open();
	} else {
		alert("请正确选择!");
	}

}

function removeDialog() {
	if ($("#pop").length > 0) {
		$("#pop").remove();
	}
	$("#myDialog").remove();
	jQuery("body").css("overflow-y","");
}

function closeDialog() {
	if ($("#pop").length > 0) {
		$("#pop").remove();
	}
	$("#myDialog").remove();
	jQuery("body").css("overflow-y","");
}

// 获取所选记录id
function getMetaViewIds(type) {
	var str = "";
	if (type) {
		$("#serviceFrame").contents().find("label[class='cur']").each(
				function() {
					if ($(this).parent("li").attr("class") != "selectAll")
						str += "," + $(this).prev("input").val();
				});
		str = str.replace(",", "");
	} else {
		$("#serviceFrame").contents().find("label[class='cur']").each(
				function() {
					if ($(this).parent("li").attr("class") != "selectAll")
						str += "," + $(this).prev("input").attr("metadataid");
				});
		str = str.replace(",", "");
	}
	return str;
}

// 获取所选栏目id
function getChannelIds() {
	var str = "";
	$("#myDialog").children("iframe").contents().find("a[class='on']").each(
			function() {
				str += "," + $(this).parent("li").attr("channelid");
			});
	str = str.replace(",", "");
	return str;
}

function reinitIframe() {
	$("#serviceFrame")
			.height(
					$(window.frames["serviceFrame"].document).find("body")
							.height() + 20 + 208);
	if ($(".wz-con").height() > 800) {
		$(".nav-left").css("min-height", $(".wz-con").height());
	}

}

// 保存最近操作对象
function saveOperationRecord(_channel) {
	var json = $("#curr" + _channel).attr("param1");
	json = eval('(' + json + ')');
	var Helper = new BasicDataHelper();
	Helper.Call("mw_operationrecord", "save", json, function(data) {
	}, function(msg) {
		alert(msg);
	});
}

//显示/隐藏新建其他
$(".addother").live("click", function(){
	$(".upload-anothers").toggleClass("hide");
	return false;
});

$(".a-nav-btn01,.upload-div").live("click", function(){//新建
	var sType = $(this).attr("_addType");
	var nChnlID = $("#serviceFrame").contents().find("#nChnlID").val();
	var nViewId = $("#originViewId").val();
	var defaultViewID = $("#webSiteViewId").val();
	var defaultChnlId = $("#defaultChannelID").val();
	var viewId = nViewId || defaultViewID;
	var url ="";
	if(sType == 2){
		var url="../" + nViewId + "/metaviewdata_addedit_document.jsp?Type="+ sType+ "&toChannelId="+nChnlID+ "&ChannelId="+ defaultChnlId+"&DocId=0&ObjectId=0&isFromDocument=true";
	}
	if(sType == 1){
		var url="../" + nViewId + "/metaviewdata_addedit_image.jsp?Type="+ sType+ "&toChannelId="+nChnlID+ "&ChannelId="+ defaultChnlId + "&DocId=0&ObjectId=0&isFromDocument=true";
	}
	var click_a = $("<a id='url_click' href='"+url+"' target='_blank'></a>");
	$("body").append(click_a);
	document.getElementById("url_click").click(); 
	$("#url_click").remove();
});
$(document).live("click",function(e){
	var click_obj = $(e.target);
	
	//新建稿件弹出层
	if($(".upload-anothers").is(":visible")){
		if(click_obj.closest(".upload-anothers,.addother").length == 0){
			$(".upload-anothers").addClass("hide");
		}
	}
	})