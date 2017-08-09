TRSDialog = function(JSON) {
           
            
            jQuery("body").css("overflow-y","hidden");

            var h = document.documentElement.scrollHeight;
            var w = jQuery("body").width();
            
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
    		this.dlgDiv = "<div id='myDialog' style='background-color:#F0F2F3;width:"+cWidth+";height:"+cHeight+
            				";position:fixed !important;top:50%;left:50%;display:block;margin-left:"+width+
            				" !important;margin-top:"+height+" !important;z-index:999;border:4px solid #ddd;border-radius:5px'>"+
            				"<iframe scrolling='no' border='0' frameBorder='0' width='100%' height='100%' src='"+JSON.url+"'></iframe>"+
            				"</div>";
			var $el = '<div id="pop" style="position:absolute;visibility:visible;background:#000;filter:alpha(opacity=40);opacity:0.4;z-index:3;left:0;top:0;width:'
			  + w + 'px;height:' + h + 'px"></div>';
			//$("body").append($el);
			 var newNode = document.createElement("div");
			 
			//自定义弹窗
            this.open = function() {
            	//$("body").append(this.dlgDiv);
            	newNode.innerHTML = $el+this.dlgDiv;
            	document.body.appendChild(newNode);
            };
            this.close=function(){

            }
        }