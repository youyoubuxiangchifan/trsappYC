$(function () {
  //多选功能
  var singleSelect = false;
 if($("#singleSelect").length>0){
 	singleSelect = true;
 }
  $(".m_adva_list label").live("click", function(){
   // $('.m_adva_list label').click(function(){
        var $this=$(this);
        if(typeof pChannelId != "undefined"&&$this.closest(".m_adva_list").attr("channelid")==pChannelId)
        	return;
      if($this.hasClass('cur')){
          $this.removeClass('cur');
          var sLi =  $this.closest(".m_adva_list");
          var sId = sLi.attr("channelid");
          $("#"+sId).parent().remove();
      }else{
       	//单选
		if($(".f_t_b").find(".selectChnl").length>0 && singleSelect){
		var oldSel = $(".f_t_b").find(".selectChnl")[0];
		var id = $(oldSel).attr("id");
		$(oldSel).parents(".left").remove();
			$(".m_adva_list").each(function(){
				if(jQuery(this).attr("channelid")===id){
					jQuery(this).find("input").attr("checked", false);
					jQuery(this).children().removeClass("on");
					jQuery(this).find("label").removeClass('cur');
				}
			})
			}
		
          $this.addClass('cur');
          var selectId = $this.parents(".m_adva_list").attr("channelId");

			//去重
			var findDuplicate = $(".f_t_b").find("#" +selectId);
			if(findDuplicate.length===0){
 			var selectName = $this.next().html();
          	var tempHtml = ['<div class="left c mar-l10">',
                	'<div class="left t selectChnl" id="{0}">{1}</div>',
                    '<div class="right i"><img id="{0}" class="delsel" src="images/close-white.png" style="cursor: pointer;"/></div>',
               		'</div>'

          ].join("");
         
		var tempHtmls = String.format(tempHtml,selectId,selectName);
          $(".f_t_b").append(tempHtmls);
			}
      }
   });
   
   $(".delsel").live("click", function(){
	   var channelId = $(this).attr("id");
	   $(".upload_con li[channelId='"+channelId+"'] label").removeClass("cur");
   		$(this).parents(".left").remove();
   });



   $('.pl_list label').click(function(){
        var $this=$(this);
      if($this.hasClass('cur')){
          $this.removeClass('cur')
      }
       else{
          $this.addClass('cur')
      }
   });
   
   
  //高级搜索
$(".txt input").bind({focus:function(){
   var $this=$(this);
   $this.next().show();
}
})
    $('.txt i.close').bind({click:function(){
         var $this=$(this);
         $this.hide().siblings().val('')
    }
    })
//select选择
    $('.selcet_list').click(function(){
        var $this=$(this);

        $('.z_option').slideDown();
      $('.z_option li').click(function(){
            var $this=$(this);
            var txt=$('.z_option li').text();
            $('.option_val').text(txt);

        })

    })
	
//select选择
    $('.selcet_list').click(function(){
        var $this=$(this);

        $('.z_option').slideDown();
      $('.z_option li').click(function(){
            var $this=$(this);
            var txt=$('.z_option li').text();
            $('.option_val').text(txt);

        })

    })

//结束
})

/*jquery_单选*/
        $(function () {
            $(".Radio1 label").click(function () {
			$(this).siblings().removeClass('cur')
                $(this).addClass("cur"); 
            });		 
        });
		
		$(function () {
            $(".Radio2 label").click(function () {
			$(this).siblings().removeClass('cur')
                $(this).addClass("cur"); 
            });		 
        });

var status = 1;
var Menus = new DvMenuCls;

document.onclick=Menus.Clear;
function switchSysBar(){
	var switchPoint=document.getElementById("switchPoint");
	var frmTitle=document.getElementById("frmTitle");
     if (1 == window.status){
		  window.status = 0;
		  //alert(switchPoint);

          switchPoint.style.backgroundImage = 'url(images/right_jiantou.png)';
          frmTitle.style.display="none";
     }
     else{
		  window.status = 1;
          switchPoint.style.backgroundImage = 'url(images/left_jiantou.png)'; 
          frmTitle.style.display=""
     }
}

function DvMenuCls(){
	var MenuHides = new Array();
	this.Show = function(obj,depth){
		var childNode = this.GetChildNode(obj);
		if (!childNode){return ;}
		if (typeof(MenuHides[depth])=="object"){
			this.closediv(MenuHides[depth]);
			MenuHides[depth] = '';
		};
		if (depth>0){
			if (childNode.parentNode.offsetWidth>0){
				childNode.style.left= childNode.parentNode.offsetWidth+'px';
				
			}else{
				childNode.style.left='100px';
			};
			
			childNode.style.top = '-2px';
		};

		childNode.style.display ='none';
		MenuHides[depth]=childNode;
	
	};
	this.closediv = function(obj){
		if (typeof(obj)=="object"){
			if (obj.style.display!='none'){
			obj.style.display='none';
			}
		}
	}
	this.Hide = function(depth){
		var i=0;
		if (depth>0){
			i = depth
		};
		while(MenuHides[i]!=null && MenuHides[i]!=''){
			this.closediv(MenuHides[i]);
			MenuHides[i]='';
			i++;
		};
	
	};
	this.Clear = function(){
		for(var i=0;i<MenuHides.length;i++){
			if (MenuHides[i]!=null && MenuHides[i]!=''){
				MenuHides[i].style.display='none';
				MenuHides[i]='';
			}
		}
	}
	this.GetChildNode = function(submenu){
		for(var i=0;i<submenu.childNodes.length;i++)
		{
			if(submenu.childNodes[i].nodeName.toLowerCase()=="div")
			{
				var obj=submenu.childNodes[i];
				break;
			}
		}
		return obj;
	}

}


function getleftbar(obj){
	var leftobj;
	var titleobj=obj.getElementsByTagName("a");
	leftobj = document.all ? frames["frmleft"] : document.getElementById("frmleft").contentWindow;
	if (!leftobj){return;}
	var menubar = leftobj.document.getElementById("menubar")
	if (menubar){
			if (titleobj[0]){
				document.getElementById("leftmenu_title").innerHTML = titleobj[0].innerHTML;
			}
			var a=obj.getElementsByTagName("ul");
			for(var i=0;i<a.length;i++){
				menubar.innerHTML = a[i].innerHTML;
				//alert(a[i].innerHTML);
			}
	}
}

/*
滑动
*/
function setTab(name,num,n){
for(i=1;i<=n;i++){
   var menu=document.getElementById(name+i);
   var con=document.getElementById(name+"_"+"con"+i);
   menu.className=i==num?"cur":"";
   con.style.display=i==num?"block":"none";
}
}

/*弹出层*/
function showDiv(){
document.getElementById('sjpc_box').style.display='block';
document.getElementById('popIframe').style.display='block';
document.getElementById('bg').style.display='block';

$(document).ready(function(){
 $(".m_adva_lw698").jscroll({
   W:"4px"
  ,BgUrl:"url(images/scrool_bar.png)"
  ,Bg:"-51px 0 repeat-y"
  ,Bar:{Pos:""
        ,Bd:{Out:"#f00",Hover:"0"}
        ,Bg:{Out:"-63px 0 repeat-y",Hover:"-63px 0 repeat-y",Focus:"-63px 0 repeat-y"}}
        ,Btn:{btn:false
              ,uBg:{Out:"0 0",Hover:"-15px 0",Focus:"-30px 0"}
              ,dBg:{Out:"0 -15px",Hover:"-15px -15px",Focus:"-30px -15px"}}
  ,Fn:function(){}
 });
});

}
function closeDiv(){
document.getElementById('sjpc_box').style.display='none';
document.getElementById('bg').style.display='none';
document.getElementById('popIframe').style.display='none';
}
