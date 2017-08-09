function oStar(obj)
{
	var oStar = document.getElementById(obj);
	var aLi = oStar.getElementsByTagName("li");
	var oUl = oStar.getElementsByTagName("ul")[0];
	var oSpan = oStar.getElementsByTagName("span")[1];
	var oP = oStar.getElementsByTagName("p")[0];
	var i = oStar.iScore = oStar.iStar = 0;
	var oInp = oStar.getElementsByTagName("input");
	var aMsg = [
				"很不满意",
				"不满意",
				"一般",
				"满意",
				"非常满意"
				]
	
	for (i = 1; i <= aLi.length; i++)
	{
		aLi[i - 1].index = i;
		//鼠标移过显示分数
		aLi[i - 1].onmouseover = function ()
		{
			fnPoint(this.index);
			//浮动层显示
			oP.style.display = "block";
			//计算浮动层位置
			oP.style.left = oUl.offsetLeft + this.index * this.offsetWidth - 104 + "px";
			//匹配浮动层文字内容
			oP.innerHTML = "<em><b>" + this.index + "</b> 分 " + aMsg[this.index - 1] + "</em>" 
		};
		//鼠标离开后恢复上次评分
		aLi[i - 1].onmouseout = function ()
		{
			fnPoint();
			//封闭浮动层
			oP.style.display = "none"
		};
		//点击后进行评分处理
		aLi[i - 1].onclick = function ()
		{
			//alert(oInp[0]);
			oInp[0].value = this.index;
			oStar.iStar = this.index;
			oP.style.display = "none";
			//oSpan.innerHTML = "<strong>" + (this.index) + " 分</strong> (" + aMsg[this.index - 1] + ")";
            //alert(oInp.value);
            //alert(oInp[0].name);
		}
	}
	//评分处理
	function fnPoint(iArg)
	{
		//分数赋值
		oStar.iScore = iArg || oStar.iStar;
		for (i = 0; i < aLi.length; i++) aLi[i].className = i < oStar.iScore ? "on" : "";	
	}
}