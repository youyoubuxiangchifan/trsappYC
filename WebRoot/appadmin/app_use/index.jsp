<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
Java 运行时环境版本<%=System.getProperty("java.version")%> <br/>
Java 安装目录<%=System.getProperty("java.home")%><br/>
 操作系统的名称<%=System.getProperty("os.name")%><br/>
操作系统的版本<%=System.getProperty("os.version")%><br/>
文件编码<%=System.getProperty("file.encoding")%><br/>
<%
	long heapSize = Runtime.getRuntime().totalMemory();
    long heapMaxSize = Runtime.getRuntime().maxMemory();
    long heapFreeSize = Runtime.getRuntime().freeMemory();
  // Runtime.getRuntime().gc();
%>
系统启动后内存<%=heapSize/1024/1024%>M<br/>
最大可分配内存<%=heapMaxSize/1024/1024%>M<br/>
已使用内存<%=heapFreeSize/1024/1024%>M<br/>
