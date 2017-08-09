Pages=function(){
	var def={
		container:"",
		pageCount:1,
		curIndex:1,
		pageSize:20,
		previous:"上一页",
		next:"下一页",
		delay:50
	},
	handle,
	pageContext={container:"",curIndex:1,pageSize:20};//回调处理刷新数据及处理当前页样式
	return{
		init:function(json){
			if(json.pageCount==0)return;
			this.setStyles();
			var nPreIndex,
				nNextIndex,
				nCurrIndex=json.curIndex || def.curIndex,
				_nPageCount=json.pageCount,
				flag=true,
				handle=json.handle,
				$el="";

			json.curIndex=json.curIndex || def.curIndex;
			json.pageSize=json.pageSize || def.pageSize;
			json.previous=json.previous || def.previous;
			json.next=json.next ||def.next;
			json.delay=json.delay || def.delay;
			pageContext.container=json.container;

			//是否输出“上一页”
			if(json.curIndex ==1){
				$el="<a class='on' href='#' pNo='1'>1</a>";
			}else{
				nPreIndex = nCurrIndex - 1;
				$el+="<a href='#' pNo='"+(nCurrIndex-1)+"' class='previous'>"+json.previous+"</a>";
				$el+="<a href='#' pNo='1'>1</a>";
			}

			for(var i=2; i<_nPageCount; i++){
				if(Math.abs(i-nCurrIndex)<3 || i<3 || _nPageCount-i<3 || _nPageCount<9){
					if(nCurrIndex == i){
						$el+="<a class='on' href='#' pNo='"+(i)+"'>"+(i)+"</a>";
					}else{
						$el+="<a href='#' pNo='"+(i)+"'>"+(i)+"</a>";
					}
					flag=true;
				}else if(Math.abs(i-nCurrIndex)>=3 && flag){
					flag=false;
					$el+="<span>...</span>";
				}
			}


			//是否输出“下一页”
			if(json.curIndex ==_nPageCount){
				$el+="<a class='on' href='#' pNo='"+_nPageCount+"'>"+_nPageCount+"</a>";
			}else{
				nNextIndex = nCurrIndex + 1;
				$el+="<a href='#' pNo='"+_nPageCount+"'>"+_nPageCount+"</a>";
				$el+="<a href='#' pNo='"+(nNextIndex)+"' class='previous'>"+json.next+"</a>";
			}
			if(_nPageCount>1){
				$(json.container).html($el);
				$(json.container).find("a").bind("click", function(){
				pageContext.curIndex=$(this).attr("pNo");
				setTimeout(function (){
				   		handle(pageContext);
					}, json.delay);
				});				
			}
		},
		setStyles:function(styls){
			var requiredStyles=styls || '<style>'+
			'.pageNav{font:12px/12px "simsun","​Arial";color:#999;padding-top:20px}'+
			'.pageNav a.on{color:#666; font-weight:bold; text-decoration:underline}'+  
			'.pageNav a{padding:0 10px; color:#999; text-decoration:none; }'+ 
			'.pageNav a:hover {color:#2182b8; text-decoration:underline;}'+
			'.pageNav div{width:20px;height:12px;display:inline-block;}'+    
			'.pageNav a.previous,#pageNav a.next{font-weight:bold}'+
			'</style>';
			$(requiredStyles).appendTo("head");
		}
	}
}