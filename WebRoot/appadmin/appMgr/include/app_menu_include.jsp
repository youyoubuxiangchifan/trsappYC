<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.trs.cache.CacheRoleOper"%>
<%@page import="com.trs.util.Global"%>
<%
/**
* 思路:	1. 根据当前用户的角色，从缓存中获取当前应用对应的角色进行匹配
*		2. 根据匹配后的角色 获取该角色对应的操作合集
*/
	boolean isAdmin = (Boolean)session.getAttribute("isAdmin");
	StringBuffer opers = new StringBuffer();
	if(!isAdmin){
		String appids = request.getAttribute("appId").toString();
		//获取应用对应的角色
		HashMap<Long, String>  roleMaps = CacheRoleOper.ROLEMAP;
		//当前应用对应的所有角色
		String appRoleId = roleMaps.get(Long.parseLong(appids))!=null? roleMaps.get(Long.parseLong(appids)):"";
		String [] appRoleIds = appRoleId.split(",");
		String userRoleIds =(String)session.getAttribute("roleIds")!=null?(String)session.getAttribute("roleIds"):"";
		//当前用户对应该应用的角色
		String [] curr_userRoleIds = userRoleIds.split(",");
		HashMap<String,String> curr_rMaps = new HashMap<String,String>();
		for(String curr_rids : curr_userRoleIds){
			curr_rMaps.put(curr_rids,curr_rids);
		}
		StringBuffer currUserRole = new StringBuffer();
		for(String currAppRoleId : appRoleIds){
			if(currAppRoleId.equals(""))
				continue;
			if(curr_rMaps.containsKey(currAppRoleId)){//当前用户有该角色
				currUserRole.append(currAppRoleId).append(",");
			}
		}
		//获取角色对应的操作
		String[] roleOpers = currUserRole.toString().split(",");
		
		HashMap<Long, String>  operMaps = CacheRoleOper.OPERMAP;
		for(String role_id : roleOpers){
			if(role_id.equals(""))
				continue;
			opers.append(operMaps.get(Long.parseLong(role_id))).append(",");//获取到用户、角色对应的操作
		}
		
 %>
	<ul class="toolBar">
	<%if(opers.toString().indexOf(Global.ADDINFO)!=-1){ %>
		<c:if test="${deleted == '0'}">
			<li><a class="add" href="<%=basePath%>appMgr.do?method=getMetaDataToAddEdit&appId=${appId}" max="true" target="dialog" title="添加"><span>添加 </span></a></li>
		</c:if>
	<%} %>
		<c:if test="${appInfo.isNeedTheme eq 0}">
			<li class="${status == -1 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&status=-1" target="navTab" title="${appInfo.appName }"><span>全部</span></a></li>
			<li class="${status == 0 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&status=0&worked=0&IsOwnerWork=0" target="navTab" title="${appInfo.appName }"><span>待办理</span></a></li>
			<li class="${status == 2 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&status=2&worked=1&IsOwnerWork=1" target="navTab" title="${appInfo.appName }"><span>已办理</span></a></li>
		</c:if>
		<c:if test="${appInfo.isNeedTheme eq 1}">
		<li class="${themeStatus == 1 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&themeStatus=1" target="navTab" title="${appInfo.appName }"><span>进行中</span></a></li>
		<li class="${themeStatus == 0 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&themeStatus=0" target="navTab" title="${appInfo.appName }"><span>已结束</span></a></li>
		</c:if>
		<li class="line">line</li>
	<%if(opers.toString().indexOf(Global.DELINFO)!=-1){ %>
		<c:if test="${deleted == '0'}"><li><a class="delete" title="您要删除选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=delDatas&tableName=${tableName}&flag=1" ><span>删除</span></a></li></c:if>
	<%} %>
	<%if(opers.toString().indexOf(Global.GKINFO)!=-1){ %>
		<c:if test="${deleted == '0'}"><li><a class="delete" title="您要公开选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=openDatas&tableName=${tableName}" ><span>公开</span></a></li></c:if>
		<c:if test="${deleted == '0'}"><li><a class="edit" title="您不公开选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=hideDatas&tableName=${tableName}" ><span>不公开</span></a></li></c:if>
	<%} %>
	<%if(opers.toString().indexOf(Global.LJINFO)!=-1){ %>
		<c:if test="${deleted == '1'}"><li><a class="edit" title="您要恢复选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=delDatas&tableName=${tableName}&flag=0" ><span>还原</span></a></li></c:if>
		<c:if test="${deleted == '0'}"><li><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId}&deleted=1" target="navTab"><span>垃圾箱</span></a></li></c:if>
	<%} %>
	</ul>
<%}else{%>

	<ul class="toolBar">
		<c:if test="${deleted == '0'}">
			<li><a class="add" href="<%=basePath%>appMgr.do?method=getMetaDataToAddEdit&appId=${appId}" max="true" target="dialog" title="添加"><span>添加</span></a></li>
		</c:if>
		<c:if test="${appInfo.isNeedTheme eq 0}">
			<li class="${status == -1 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&status=-1" target="navTab" title="${appInfo.appName }"><span>全部</span></a></li>
			<li class="${status == 0 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&status=0&worked=0&IsOwnerWork=0" target="navTab" title="${appInfo.appName }"><span>待办理</span></a></li>
			<li class="${status == 2 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&status=2&worked=1&IsOwnerWork=1" target="navTab" title="${appInfo.appName }"><span>已办理</span></a></li>
		</c:if>
		<c:if test="${appInfo.isNeedTheme eq 1}">
		<li class="${themeStatus == 1 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&themeStatus=1" target="navTab" title="${appInfo.appName }"><span>进行中</span></a></li>
		<li class="${themeStatus == 0 ? 's_tab_curr' : ''}"><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId }&themeStatus=0" target="navTab" title="${appInfo.appName }"><span>已结束</span></a></li>
		</c:if>
		<li class="line">line</li>
		<c:if test="${deleted == '0'}"><li><a class="delete" title="您要删除选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=delDatas&tableName=${tableName}&flag=1" ><span>删除</span></a></li></c:if>
		<c:if test="${deleted == '0'}"><li><a class="delete" title="您要公开选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=openDatas&tableName=${tableName}" ><span>公开</span></a></li></c:if>
		<c:if test="${deleted == '0'}"><li><a class="edit" title="您不公开选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=hideDatas&tableName=${tableName}" ><span>不公开</span></a></li></c:if>
		<c:if test="${deleted == '1'}"><li><a class="edit" title="您要恢复选中的数据吗？" target="selectedTodo" rel="dataIds" postType="string" href="<%=basePath%>appMgr.do?method=delDatas&tableName=${tableName}&flag=0" ><span>还原</span></a></li></c:if>
		<c:if test="${deleted == '0'}"><li><a class="icon" href="<%=basePath%>appMgr.do?method=appDataList&appId=${appId}&deleted=1" target="navTab"><span>垃圾箱</span></a></li></c:if>
	</ul>
<%}%>