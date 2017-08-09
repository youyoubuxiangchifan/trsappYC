<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String errMsg = (String)request.getAttribute("errMsg");
String c = (String)request.getAttribute("c");
if(c == null || c.equals("")){
	//request.getRequestDispatcher("trsapp/login.do?method=loginUser").forward(request, response);
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <base href="<%=basePath%>">
    
    <title>用户登入</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="appadmin/css/styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="appadmin/css/layout.css" />
	<script type="text/javascript" src="appadmin/js/RSA.js"></script>  
	<script type="text/javascript" src="appadmin/js/BigInt.js"></script>  
	<script type="text/javascript" src="appadmin/js/Barrett.js"></script>  
	<script type="text/javascript">
	function rsalogin()
	{
	   bodyRSA();   
	   var result = encryptedString(key, document.getElementById("passWord").value);
	   document.getElementById("passWord").value = result;
	   loginForm.submit();
	}
	
	var key ;   
	function bodyRSA()
	{
	    setMaxDigits(130);
	    key = new RSAKeyPair("${e}","","${m}");
	}
	
	//重新加载随机码
	function loadimage(){
		document.getElementById("captchaImg").src = "appadmin/image.jsp?randName=loginCaptcha&"+Math.random();
	}
	</script>
  </head>
  
  <body class="warp">
  <div class="cot1">
    <form id="loginForm" method="post" focus="username" action="${basePath}login.do?method=loginUser">  
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="11%" height="47" align="right">用户名：</td>
    <td width="89%"><input id="userName" name="userName" type="text" class="iput1"/></td>
  </tr>
  <tr>
    <td height="47" align="right">密码：</td>
    <td><input id="passWord" type="password" name="passWord" class="iput1"/></td>
  </tr>
  <tr>
    <td height="47" align="right">验证码：</td>
    <td>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
		    	<td width="190px;"><input type="text" id="loginCaptcha" name="loginCaptcha" class="iput2"/></td><td><img name="captchaImg" id="captchaImg" src="" width="55" height="24" align="absmiddle" onclick="loadimage();" title="点击刷新验证码"/></td>
		    </tr>
	    </table>
    </td>
  </tr>
  <tr>
    <td height="60">&nbsp;</td>
    <td><input name="dr" type="image" src="appadmin/images/trs_hd3.jpg"  onclick="rsalogin();"/><span class="hz_mm"><a href="#">忘记密码？</a></span></td>
  </tr>
  <tr>
    <td height="10">&nbsp;</td>
    <td style="color:red;">${errMsg}</td>
  </tr>
</table> 
</form>
</div>
<script type="text/javascript">
	document.getElementById("captchaImg").src = "appadmin/image.jsp?randName=loginCaptcha&"+Math.random();
</script>
  </body>
</html>
