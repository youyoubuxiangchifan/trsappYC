TRSDialog = function(JSON) {
			$("body").css("overflow-y","hidden");
		    var h = $(window).height();
		    var w = $(window).width(); 
			var h1 = parseInt($(document).scrollTop()+$(window).height()/2 - JSON.height/2) + "px";
//			var h = window.parent.document.body.scrollHeight;
//          var w = jQuery(window.parent).width();
			
            var width,height,cWidth,cHeight;
            var wPencent=""+JSON.width;
            var hPercent=""+JSON.height;
            if(wPencent.indexOf("%")>=0){
                width="-"+(w/2)+"px";
                cWidth=JSON.width;
            }else{
                width="-"+(JSON.width/2)+"px";
                cWidth=JSON.width+"px";
            }

            if(hPercent.indexOf("%")>=0){
                height="-"+(h/2)+"px";
                cHeight=JSON.height;   
            }else{
                height="-"+(JSON.height/2)+"px";
                cHeight=JSON.height+"px";   
            }
            
            var mytop = h / 2 - JSON.height / 2;
            var myleft = w / 2 - JSON.width / 2;
            
            this.dlgDiv = "<div id='myDialog' style='width:"+JSON.width+"px;"+
						";position:fixed;top:"+mytop + 'px' +";left:"+myleft+"px;display:block;"+
						"z-index:999;height:"+JSON.height+"px;overflow:hidden;'>"+
						"<iframe id='myDialog_frame' scrolling='no' border='0' frameBorder='0' width='100%' height='100%' src='"+JSON.url+"'></iframe>"+
						"</div>";
			
            if($("#pop").length>0)return;
			
			var $el = '<div id="pop" style="position:fixed;visibility:visible;background:rgba(0,0,0,.4);filter:alpha(opacity=40);z-index:3;left:0;top:0;width:'
				+ w + 'px;height:' + h + 'px;">'+this.dlgDiv +'</div>';

			//自定义弹窗
            this.open = function() {
            	$($el).appendTo("body");  
            	$("#myDialog_frame").bind("load",this.onload);
            	$("#myDialog").show();	
            	$(window).unbind("resize");
            	$(window).resize(function(){
            		if( $("#pop")&& $("#pop").attr("id")){
            			var h2 = $(window).height();
            		    var w2 = $(window).width(); 
            		    $("#pop").width(w2);
            		    $("#pop").height(h2);
            		}
            	})
            };
        	this.onload = function(){
				var _h = $(this.contentWindow.document.body).height();
				$(this.contentWindow.document.body).css("background-color","transparent");
				this.contentWindow.closeDialog = function(){
					$(this.parent.document.body).css("overflow-y","auto");
					$(this.parent.document.body).find("#pop").remove();
				};
				$(this).height(_h);
			};
            this.close=function(){

            }
        }

