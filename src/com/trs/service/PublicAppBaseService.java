package com.trs.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.appcontext.AppSynSendMessage;
import com.trs.appcontext.ApplicationCt;
import com.trs.dao.PublicAppBaseDao;
import com.trs.dbhibernate.ChangeDBType;
import com.trs.dbhibernate.Page;
import com.trs.model.AppAppendix;
import com.trs.model.AppDatastatus;
import com.trs.model.AppFieldRel;
import com.trs.model.AppFlow;
import com.trs.model.AppFlowContext;
import com.trs.model.AppFlowDoc;
import com.trs.model.AppFlowNode;
import com.trs.model.AppGroup;
import com.trs.model.AppGrpuser;
import com.trs.model.AppInfo;
import com.trs.model.AppRecordLock;
import com.trs.model.AppRelGroup;
import com.trs.model.AppRole;
import com.trs.model.AppUser;
import com.trs.model.AppViewInfo;
import com.trs.model.FlowGroupAndUsers;
import com.trs.util.CMyString;
import com.trs.util.DateUtil;
import com.trs.util.Global;
/**
 * 应用信息处理服务层
 * Description:后台用户登录后 获取代办 处理信息提供服务<BR>
 * 查询用户有权限访问的应用流程：用户->(用户)查询角色->(用户)查询角色对应应用->完成，返回用户的应用列表
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: PublicAppBaseService
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-14 下午02:04:35
 * @version 1.0
 */
@Service
public class PublicAppBaseService extends BaseService {
	private static  Logger LOG =  Logger.getLogger(PublicAppBaseService.class);
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private MessageSend messageSend;
	@Autowired
	private PublicAppBaseDao publicAppBaseDao;
	@Autowired
	private ApplicationCt applicationCt;
	@Autowired
	private AppFlowService appFlowService;

	/**
	* 自定义工作流规则调用方法,返回appuser集合
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-27 下午01:47:42
	* Last Modified:
	* @param className
	* @return String 返回用户的ID集合
	* @version 1.0
	 */
	public String changeflows(String className,AppFlowContext appFlowContext){
		return applicationCt.dynamicClass(className,"createToUsers",appFlowContext);
	}
	public Map<String,Object> createNodeIdAndFLowUser(String className,AppFlowContext appFlowContext){
		return applicationCt.dynamicClassMap(className,"createNodeIdAndFLowUser",appFlowContext);
	}
	/**
	 * 返回用户的组织ID集合
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-27 下午02:56:31
	* Last Modified:
	* @param userId 用户ID
	* @return String 组织id集合 格式为 1,3,4  以[,]逗号分隔的id集合,如果没有则返回空字符串
	* @throws Exception
	* @version 1.0
	 */
	public String findByGroupIds(Long userId) throws Exception{
		StringBuffer groupIds = new StringBuffer();
		List<Object> groups = appUserService.getUserGroups(userId);
		for(Object group: groups){
			AppGroup appGroup = (AppGroup)group;
			//groupIds.append(appGroup.getGroupId()).append(",");
			groupIds.append(appGroup.getIndependGroupId()).append(",");//独立组织编号
		}
		return groupIds.length()>0?groupIds.toString().substring(0,groupIds.toString().length()-1):"";
	}
	/**
	 * 获取当前用户ID对应的角色集合编号
	* Description:注意：此处返回的数据为string类型，格式为 1,3,4  以[,]逗号分隔的id集合,如果没有则返回空字符串<BR>
	* @author wen.junhui
	* @date Create: 2014-3-14 下午03:13:16
	* Last Modified:
	* @param userId  当前用户ID
	* @return String 角色集合，逗号分隔
	* @version 1.0
	 */
	public String findByRoleIds(Long userId) throws Exception{
		StringBuffer strBuff = new StringBuffer();
		List<Object> appRoleUsers = appUserService.getUserRoles(userId);
			for(Object approle:appRoleUsers){
				AppRole appRole = (AppRole)approle;
				strBuff.append(appRole.getRoleId()).append(",");
			}
		return strBuff.length()>0?strBuff.toString().substring(0,strBuff.length()-1):"";
	}

	/**
	 * 返回用户有权限的应用集合
	* Description:管理员用户返回所有的应用集合<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-27 下午02:57:23
	* Last Modified:
	* @param userId
	* @return List<Object> 返回结果为应用的集合 ：List<AppInfo> 
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getAppInfos(Long userId) throws Exception{
		StringBuffer hql  = new StringBuffer("select a from AppInfo a ");
		//非管理 员用户按照角色和组织对应的应用查询 管理员用户加载所有的
		if(!appUserService.isAdminUser(userId)){
			String roleIds=	findByRoleIds(userId);
			//String groups = findByGroupIds(userId);
//			hql.append(",AppRoleSys s where a.groupId in(").append(groups).append(") and s.roleId in(").append(roleIds).append(") and a.appId=s.appId");
			hql.append(",AppRoleSys s where s.roleId in(").append(roleIds).append(") and a.appId=s.appId");
		}
		List<Object> appInfos =publicAppBaseDao.find(hql.toString());
		return appInfos;
	}
	/**
	 * 返回对应field 字段的结果字符串,查询的表为appinfo
	* Description:查询应用的 viewId 字段集合，则field传入viewId <BR>  
	* @author wen.junhui
	* @date Create: 2014-3-27 下午04:45:07
	* Last Modified:
	* @param userId 用户ID
	* @param field 要返回的字段对象 ，bean 中的名字
	* @param appId 应用ID ，查询用户某个应用对应的 字段则传入实际的应用id ，否则传入0
	* @return String 返回的结果为："1,2,3,4,5" 已逗号分隔的field字段 结果,会过滤掉重复的值
	* @throws Exception
	* @version 1.0
	 */
	public String getAppInfoFields(Long userId,String field,Long appId) throws Exception{
		StringBuffer fieldsValues = new StringBuffer();
		if(CMyString.isEmpty(field)){
			throw new Exception("field字段不能为空");
		}
		StringBuffer hql  = new StringBuffer("select distinct a.").append(field).append(" from AppInfo a ");
		//非管理 员用户按照角色和组织对应的应用查询 管理员用户加载所有的
		if(!appUserService.isAdminUser(userId)){
			String roleIds=	findByRoleIds(userId);
//			String groups = findByGroupIds(userId);
//			if(CMyString.isEmpty(roleIds) || CMyString.isEmpty(groups)){
//				return "";
//			}
//			hql.append(",AppRoleSys s where a.groupId in(").append(groups).append(") and s.roleId in(").append(roleIds).append(") and a.appId=s.appId");
			hql.append(",AppRoleSys s where s.roleId in(").append(roleIds).append(") and a.appId=s.appId");
			if(appId>0){ //查询指定应用
				hql.append(" and a.appId=").append(appId);
			}
		}else{
			if(appId>0){ //查询指定应用
				hql.append(" where a.appId=").append(appId);
			}
		}
		
		List<Object> appInfos =publicAppBaseDao.find(hql.toString());
		for(Object obj : appInfos){
			fieldsValues.append(obj).append(",");
		}
		return fieldsValues.toString().length()>0?fieldsValues.toString().substring(0,fieldsValues.toString().length()-1):"";
	}
	/**
	 * 返回用户--应用对应的视图信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-27 下午04:23:11
	* Last Modified:
	* @param viewIds 视图ID集合，已逗号分隔的字符串
	* @return List<Object> 返回结果为:List<AppViewInfo>
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getAppViewInfos(String viewIds) throws Exception{
		return find(null, AppViewInfo.class.getName(), "deleted=? and viewId in("+viewIds+")", " crtime desc ", 0);
		//return findByIds(AppViewInfo.class.getName(), "viewId", viewIds);
	}
	/**
	 *  返回视图对应的应用信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-27 下午04:40:22
	* Last Modified:
	* @param userId 当前用户ID
	* @param viewId 视图编号
	* @return List<Object> 返回结果为应用信息：List<AppInfo>
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getViewToAppInfo(Long userId,Long viewId) throws Exception{
		StringBuffer hql  = new StringBuffer("select a from AppInfo a ");
		//非管理 员用户按照角色和组织对应的应用查询
		if(!appUserService.isAdminUser(userId)){
			String roleIds=	findByRoleIds(userId);
//			String groups = findByGroupIds(userId);
//			hql.append(",AppRoleSys s where a.groupId in(").append(groups).append(") and s.roleId in(").append(roleIds).append(") and a.appId=s.appId and a.deleted=0 and a.viewId=").append(viewId);
			hql.append(",AppRoleSys s where s.roleId in(").append(roleIds).append(") and a.appId=s.appId and a.deleted=0 and a.viewId=").append(viewId);
		}else{
			hql.append("where a.deleted=0 and a.viewId=").append(viewId);//管理员用户不做过滤
		}
		List<Object> appInfos =publicAppBaseDao.find(hql.toString());
		return appInfos;
	}
	
	
//	
//	/**
//	 * 统计返回当前用户有权限的所有应用的不同状态（代办、已处理、代处理）的信息总数
//	* Description:参数中status 对应当前信息的处理状态;已签收但未处理也属于代办<BR>  
//	* status 参数为 null 时 则仅返回当前用户对应应用所有信件总数;<BR>
//	* @author wen.junhui
//	* @date Create: 2014-3-20 下午05:58:20
//	* Last Modified:
//	* @param userId  当前用户ID
//	* @param userName 当前用户用户名
//	* @param status  要查询的信息处理状态  ，例如：0 未处理   1 已处理，具体参见系统数据字典中定义
//	* @return List<Map<String, Integer>> 
//	* 返回的数据格式 List< map<appId,20><appName,局长信箱><waitNumber,20><appInfoMaxNumber,50> >
//	* 【appName=应用名称,appId=应用编号,waitNumber=信件待办的总数,appInfoMaxNumber=当前用户对应该应用的所有信息】
//	* @throws Exception
//	* @version 1.0
//	 */
//	public List<Map<String, Object>> findWaitWorksCount(Long userId,String userName,String status) throws Exception{
//		
//		List<Map<String, Object>> waitList = new ArrayList<Map<String,Object>>();
//		Map<String,Object> waitMap = new HashMap<String, Object>();
//		StringBuffer swhere = new StringBuffer("tousers=? and appid=?");
//		//查询当前用户有权限的应用列表信息
//		List<Object> appInfoList = findAppInfos(userId);
//		List<Object> params = new ArrayList<Object>();
//		for(Object appInfos : appInfoList){
//			AppInfo appInfo = (AppInfo)appInfos;
//			//TOUSERS(处理用户) WORKED(已处理)已签收未处理也属于代办
//			params.add(userName);
//			params.add(appInfo.getAppId());
//			//status 不为空查询 对应信件状态总数，否则查询所有状态信件
//			int appInfoMaxNumber = 0;
//			if(!CMyString.isEmpty(status)){
//				params.add(status);
//				swhere.append(" and worked=? ");
//				//信息总数
//				 appInfoMaxNumber = findWaitWorkCount(appInfo.getAppId(), userName, null);
//			}
//			//获取代办信息数字
//			int waitNumber = count(AppFlowDoc.class.getName(), swhere.toString(),params);
//			
//			waitMap.put("appId", appInfo.getAppId());
//			waitMap.put("appName", appInfo.getAppName());
//			waitMap.put("waitNumber", appInfoMaxNumber==0?appInfoMaxNumber:waitNumber);
//			waitMap.put("appInfoMaxNumber", appInfoMaxNumber==0?waitNumber:appInfoMaxNumber);
//			waitList.add(waitMap);
//		}
//		return waitList;
//	}
	/**
	 * 统计返回当前用户单个应用的不同状态（代办、已处理、代处理）的信息总数
	* Description:参数中status 对应当前信息的处理状态;已签收未处理也属于代办  ；<BR>  
	* 当查询当前用户某应用所有信件总数时，status 参数为 null<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-20 下午06:10:54
	* Last Modified:
	* @param appId 应用编号
	* @param userName 当前登陆用户名
	* @param status 要查询的信息处理状态  ，例如：0 未处理   1 已处理，具体参见系统数据字典中定义
	* @return int 单个应用的不同状态的信息总数
	* @throws Exception
	* @version 1.0
	 */
	public int findWaitWorkCount(Long appId,String userName,String status) throws Exception{
		List<Object> params = new ArrayList<Object>();
		StringBuffer swhere = new StringBuffer("tousers=? and appid=?");
		params.add(userName);
		params.add(appId);
		//status 不为空查询 对应信件状态总数，否则查询所有状态信件
		if(!CMyString.isEmpty(status)){
			params.add(status);
			swhere.append(" and worked=? ");
		}
		int waitNumber = count(AppFlowDoc.class.getName(), swhere.toString(),params);
		return waitNumber;
	}
	//	/**
	//	 * 返回应用(元数据)的单条信息 <原生SQL>
	//	* Description:<BR>  
	//	* @author wen.junhui
	//	* @date Create: 2014-3-25 下午04:33:01  
	//	* Last Modified:调用baseservice中方法
	//	* @param metadataid 数据编号
	//	* @param tableName 表名称   数据库表名称
	//	* @return Map<String,Object> 
	//	* @throws Exception
	//	* @version 1.0
	//	 */
	//	public Map<String, Object> getAppMedataInfo(Long metadataid,String tableName) throws Exception{
	//		return getAppMedataInfo(metadataid, tableName);	
	//	}
	/*#########################################################################################################*/
	/*####                              TODO 工作流模块方法                                                                                                                       ########*/
	/*#########################################################################################################*/
	/**
	 * 返回当前文档对应的工作流信息\注意appFlowGroups和appFlowUsers 返回的  只是nextAppFlowNodes第一个节点对应的信息
	* Description:返回数据：Map格式: flowDoclist 所有流转轨迹信息  appFlowNode 当前节点   
	* 	appFlowNodes 当前工作流所有节点信息
	* 	nextAppFlowNodes下一节点 集合
	*   appFlowGroups下一节点处理组织 
	*   appFlowUsers下一节点处理用户<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-8 下午02:54:57
	* Last Modified: 
	* @param metadataId 当前文档编号
	* @param appId 所属应用编号
	* @return Map 返回格式 Map<String, Object>
	* @throws Exception
	* @version 1.0
	 */
	public Map<String, Object> getAppFlowDocs(Long metadataId ,Long appId,AppUser loginUser) throws Exception{
		Map<String, Object> allResult= new HashMap<String, Object>();
		List<Object> parameters  = new ArrayList<Object>();
		parameters.add(metadataId);
		parameters.add(appId);
		//当前工作流所有节点信息
		List<Object> appFlowNodes =appFlowService.getNodeByFlowId(getAppInfoToFlowId(appId));
		allResult.put("appFlowNodes", appFlowNodes);
		//工作流所有流转轨迹
		List<Object> flowDoclist =  find(null, AppFlowDoc.class.getName(), "objid=? and appId=?", "flowDocId asc ",parameters);
		allResult.put("flowDoclist",flowDoclist);
		//1 查询当前工作流的所有流转轨迹。2 查询下个处理节点的用户  3 查询下个节点的所有处理组织。 如果是自定义规则则只显示用户  
		//TODO 这里需要查询工作流下一个节点是否为自定义规则，如果是则按照自定义规则的方式获取用户即可
		 //获取当前流转轨迹信息 
		AppFlowDoc appFlowDoc = getCurrentFlowDoc(metadataId, appId,loginUser.getUsername()); 
		if(appFlowDoc==null){
			allResult.put("isShowNode","N");//没有下一个节点了。最后一个节点了。
			return allResult;
		}
		allResult.put("appFlowDoc",appFlowDoc);
		//当前节点信息
		AppFlowNode appFlowNode =(AppFlowNode)appFlowService.findByFlowNode(appFlowDoc.getNodeId()).get(0);
		allResult.put("appFlowNode",appFlowNode);
		if(appFlowNode.getTogether()==1){
			//会签,查询当前有多少个人没有处理
			List<Object> flowDocIds =find("flowDocId", AppFlowDoc.class.getName(), "prenodeid=? and isOwnerWork=0 and objid="+metadataId, null, appFlowDoc.getPrenodeid());
			if(flowDocIds.size()>1){//至少还有一个人没有会签完成，则不进行下一个节点的选择
				allResult.put("isShowNode","HN");//会签未完成，不显示下个节点
				return allResult;
			}
			allResult.put("isShowNode","HY");//会签最后一个人，显示下个节点
		}else{
			allResult.put("isShowNode","HY");//非会签，也显示下一个节点
		}
		//下一节点<可能是多个节点>
		if(CMyString.isEmpty(appFlowNode.getNextNodeIds())){//已经流转完毕，没有下个节点
			allResult.put("isShowNode","N");//没有下一个节点了。最后一个节点了。
			return allResult;
		}
		List<Object> nextAppFlowNodes = appFlowService.getNextFlowNodes(appFlowNode.getNextNodeIds());
		allResult.put("nextAppFlowNodes",nextAppFlowNodes);
		if(nextAppFlowNodes.size()<1){
			throw new Exception("工作流节点有误，获取下个节点集合为空，请检查工作流配置!");
		}
		AppFlowNode nextAppFlowNode = (AppFlowNode)nextAppFlowNodes.get(0);
		allResult.put("nextNodeId", nextAppFlowNode.getNodeId());
		//是否自定义规则   
		if(CMyString.isEmpty(nextAppFlowNode.getRuleName())){//否
			//下一节点的处理部门，过滤完成的
			if(!CMyString.isEmpty(nextAppFlowNode.getNodeDep())){
				List<Object> appFlowGroups =getFlowNodeGrps(appId, nextAppFlowNode.getNodeDep());
				allResult.put("appFlowGroups", appFlowGroups);
			}
			//下一节点的处理用户,过滤完成的
			if(!CMyString.isEmpty(nextAppFlowNode.getNodeUser())){
				List<Object> appFlowUsers = getFlowNodeUs(appId, nextAppFlowNode.getNodeUser());
				allResult.put("appFlowUsers", appFlowUsers);
			}
			
		}else{//是
			//自定义规则返回的用户id集合
			AppFlowContext appFlowContext = setAppFlowContext(appId, appFlowDoc.getFlowDocId(), appFlowNode.getFlowId(), metadataId, appFlowNode.getNodeId(), "",loginUser);
			String appUserStr = changeflows(nextAppFlowNode.getRuleName(),appFlowContext);
			
			//下一节点的处理用户,过滤完成的
			List<Object> appFlowUsers =getFlowNodeUs(appId, appUserStr);
			allResult.put("appFlowUsers", appFlowUsers);
//			List<FlowGroupAndUsers> flowGroupAndUsers=changeGroupAndUsers(appUserStr);
//			allResult.put("flowGroupAndUsers", flowGroupAndUsers);
			
		}
		//TODO 已处理，未处理
		return allResult;
	}
	
	//**************************工作流*用户获取模块开始
	/**
	 * <暂时废弃，目前没有用到，请勿调用,没有过滤权限>返回自定义规则匹配的组织和用户FlowGroupAndUsers对象,
	* Description:请参照 FlowGroupAndUsers 对象进行构造<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-21 下午09:28:10
	* Last Modified:
	* @param appUserStr  用户编号集合，逗号分隔
	* @return List<FlowGroupAndUsers> 返回格式请参照com.trs.model.FlowGroupAndUsers
	* @throws SQLException
	* @throws IOException
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<FlowGroupAndUsers> changeGroupAndUsers(String appUserStr) throws SQLException, IOException{
		StringBuffer sql = new StringBuffer("select  distinct g.group_id , g.gname,u.user_id,u.username  from app_grpuser ag,app_group g,app_user u where ag.user_id in(");
		sql.append(appUserStr).append(") and ag.group_id=g.group_id and u.user_id=ag.user_id order by g.group_id asc");
		List<Object> flowTogrp=publicAppBaseDao.executeQueryObjs(sql.toString());
		List<FlowGroupAndUsers> flowToUserAndGrp = new ArrayList<FlowGroupAndUsers>();
		String grpid = null;
		for(Object obj:flowTogrp){
			FlowGroupAndUsers flowGroupAndUsers = new FlowGroupAndUsers();
			Map<Object,Object> maps = (Map<Object,Object>)obj;
			if(grpid!=null&&grpid.equals((String)maps.get("GROUP_ID"))){
				continue;
			}
			flowGroupAndUsers.setGroupId(Long.valueOf((String)maps.get("GROUP_ID")));
			flowGroupAndUsers.setGroupName((String)maps.get("GNAME"));
			List<AppUser> appUs = new ArrayList<AppUser>();
			for(Object objUser:flowTogrp){
				Map<Object,Object> mapus = (Map<Object,Object>)objUser;
				if(((String)maps.get("GROUP_ID")).equals((String)mapus.get("GROUP_ID"))){
					AppUser appUser = new AppUser();
					appUser.setUserId(Long.valueOf((String)mapus.get("USER_ID")));
					appUser.setUsername((String)mapus.get("USERNAME"));
					appUs.add(appUser);
				}
			}
			grpid=(String)maps.get("GROUP_ID");
			flowGroupAndUsers.setUsers(appUs);
			flowToUserAndGrp.add(flowGroupAndUsers);
		}
		return flowToUserAndGrp;
		
	}
	/**
	 * 返回节点编号对应的工作流节点信息中的详细处理用户及部门
	* Description:目前仅异步调用获取节点对应的组织和部门时使用<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-11 下午02:44:02
	* Last Modified:
	* @param appId 应用ID
	* @param metedataId 文档编号
	* @param nodeId 节点编号
	* @return Map<String, Object> 返回格式： Map<String,object> appFlowGroups 处理部门  appFlowUsers处理用户
	* @throws Exception
	* @version 1.0
	 */
	public Map<String, Object> getChangeNextFlowNode(Long appId,Long metedataId,Long nodeId,AppUser loginUser) throws Exception{
		
		Map<String, Object> flowGrpOrUsers = new HashMap<String, Object>();
		AppFlowNode appFlowNode = (AppFlowNode)appFlowService.findByFlowNode(nodeId).get(0);
		//是否自定义规则   
		if(CMyString.isEmpty(appFlowNode.getRuleName())){//否
			//下一节点的处理部门，过滤完成的
			if(!CMyString.isEmpty(appFlowNode.getNodeDep())){
				List<Object> appFlowGroups =getFlowNodeGrps(appId, appFlowNode.getNodeDep());
				flowGrpOrUsers.put("appFlowGroups", appFlowGroups);
			}
			//下一节点的处理用户,过滤完成的
			if(!CMyString.isEmpty(appFlowNode.getNodeUser())){
				List<Object> appFlowUsers = getFlowNodeUs(appId, appFlowNode.getNodeUser());
				flowGrpOrUsers.put("appFlowUsers", appFlowUsers);
			}
			
		}else{//是
			 //获取当前流转轨迹信息 
			AppFlowDoc appFlowDoc = getCurrentFlowDoc(metedataId, appId,loginUser.getUsername()); 
			//自定义规则返回的用户id集合
			AppFlowContext appFlowContext = setAppFlowContext(appId, appFlowDoc.getFlowDocId(), appFlowNode.getFlowId(), metedataId, appFlowNode.getNodeId(), "",loginUser);
			String appUserStr = changeflows(appFlowNode.getRuleName(),appFlowContext);
			//下一节点的处理用户,过滤完成的
			List<Object> appFlowUsers =getFlowNodeUs(appId, appUserStr);
			flowGrpOrUsers.put("appFlowUsers", appFlowUsers);
//			List<FlowGroupAndUsers> flowGroupAndUsers=changeGroupAndUsers(appUserStr);
//			flowGrpOrUsers.put("flowGroupAndUsers", flowGroupAndUsers);
		}
		return flowGrpOrUsers;
	}
	/**
	 * 返回当前工作流节点的(上)下一个节点配置的组织信息<该方法只是为了过滤组织后返回结果，实际要传入的参数在节点信息表中查询>
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-5 下午02:07:07
	* Last Modified:
	* @param appId 应用ID
	* @param grpIds 组织编号集合  已逗号分隔
	* @return List<Object>  格式：List<AppGroup>
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getFlowNodeGrps(Long appId,String grpIds) throws Exception{
		StringBuffer hql = new StringBuffer("select distinct a.groupId from AppGroup a, AppGrpuser g ,AppRoleSys s ,AppRoleUser r where");
		hql.append(" g.groupId in(").append(grpIds).append(")");
		hql.append(" and a.groupId=g.groupId ");
		hql.append(" and g.userId=r.userId ");
		hql.append(" and s.roleId = r.roleId ");
		hql.append(" and s.appId=").append(appId);
		List<Object> flowToGrpIds = publicAppBaseDao.find(hql.toString());
		if(flowToGrpIds.size()<1){
			//throw new Exception("工作流节点配置的接收部门没有权限访问当前的应用，请检查该部门下的【用户】是否有权限访问当前应用。");
			return new ArrayList<Object>();
		}
		List<Object> appGrps =findByIds(AppGroup.class.getName(), "groupId", flowToGrpIds);
		return appGrps;
	}
	/**
	 * 根据传入的部门编号集合返回部门对应的所有用户
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-24 下午04:42:02
	* Last Modified:
	* @param appId 应用编号
	* @param grpIds 组合编号集合 ，已逗号分隔的
	* @return List<Object> 返回格式：List<AppUser>
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getFlowNodeGrpToUsers(Long appId,String grpIds) throws Exception{
		StringBuffer hql = new StringBuffer("select distinct a.userId from AppUser a, AppGrpuser g ,AppRoleSys s ,AppRoleUser r where");
		hql.append(" g.groupId in(").append(grpIds).append(")");
		hql.append(" and a.userId=g.userId ");
		hql.append(" and g.userId=r.userId ");
		hql.append(" and s.roleId = r.roleId ");
		hql.append(" and s.appId=").append(appId);
		List<Object> flowToGrpIds = publicAppBaseDao.find(hql.toString());
		if(flowToGrpIds.size()<1){
			//throw new Exception("工作流节点配置的接收部门没有权限访问当前的应用，请检查该部门下的【用户】是否有权限访问当前应用。");
			return new ArrayList<Object>();
		}
		List<Object> appGrps =findByIds(AppUser.class.getName(), "userId", flowToGrpIds);
		return appGrps;
	}
	/**
	 * 返回当前工作流节点的(上)下一个节点配置的用户信息<该方法只是为了过滤用户后返回结果，实际要传入的参数在节点信息表中查询>
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-5 下午02:32:50
	* Last Modified:
	* @param appId
	* @param uids 用户编号集合  已逗号分隔
	* @return List<Object> 格式：List<AppUser>
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getFlowNodeUs(Long appId,String uids) throws Exception{
		StringBuffer hql = new StringBuffer(" select distinct a.userId from AppUser a ,AppRoleSys s ,AppRoleUser r where ");
		hql.append(" a.userId=r.userId ");
		hql.append(" and s.roleId = r.roleId ");
		hql.append(" and s.appId=").append(appId);
		hql.append(" and a.userId in(").append(uids).append(")");
		List<Object> flowToUserIds = publicAppBaseDao.find(hql.toString());
		if(flowToUserIds.size()<1){
			//throw new Exception("工作流节点配置的用户没有权限访问该应用");
			return new ArrayList<Object>();
		}
		List<Object> appUsers =findByIds(AppUser.class.getName(), "userId", flowToUserIds);
		return appUsers;
	}
	/**
	 * 返回下一个节点接收的用户和部门进行处理后的最终用户集合 
	* Description:<保存流转轨迹调用>已经进行重复过滤，并且对权限进行过滤，没有权限的用户将不会返回<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-5 上午11:23:07
	* Last Modified:
	* @param grpIds  工作流节点上的组织编号集合，已逗号分隔 
	* @param appId  当前应用编号
	* @param userIds 工作流节点上的用户编号集合，已逗号分隔
	* @return List<Object>  格式：List<AppUser>
	* @version 1.0
	 * @throws Exception 
	 */
	public List<Object> getGrpAndRoleAndFolwUserToUsers(String grpIds,Long appId,String userIds) throws Exception{
		List<Object> appUsers = null;
		if(CMyString.isEmpty(grpIds)&&!CMyString.isEmpty(userIds)){
			return getFlowNodeUs(appId, userIds);
		}else if(!CMyString.isEmpty(grpIds)&&CMyString.isEmpty(userIds)){
			return getFlowNodeGrpToUsers(appId, grpIds);
		}else if(!CMyString.isEmpty(grpIds)&&!CMyString.isEmpty(userIds)){
			StringBuffer hql = new StringBuffer(" select distinct (d.userId) from AppUser au, AppRoleUser t,AppGrpuser d,AppRoleSys r where ");
			hql.append(" t.userId = d.userId and t.roleId = r.roleId ");
//			hql.append(" and (d.groupId in (").append(grpIds).append(")").append("or t.userId in (").append(userIds).append(")) ");
			hql.append(" and t.userId in (").append(userIds).append(") ");
			hql.append(" and r.appId=").append(appId);
			List<Object> flowToUserIds = publicAppBaseDao.find(hql.toString());
			appUsers =findByIds(AppUser.class.getName(), "userId", flowToUserIds);
			return appUsers;
		}
		return appUsers;
	}
	//**************************工作流*用户获取模块结束
	/*
	 * 字段拼接，前台不调用此方法
	 */
	public String splitFields(String sFields){
		StringBuffer field = new StringBuffer(" distinct af.Objid, ");
		String[] cusFields = sFields.split(",");
		for(String str : cusFields){
			field.append("am.").append(str.trim()).append(",");
		}
		return field.toString().substring(0, field.toString().length()-1);
	}
	/*
	 * 设置自定义规则对象
	 */
	public AppFlowContext setAppFlowContext(Long appId,Long flowdocId,Long flowId,Long metadataId,Long nodeId,String operType,AppUser loginUser){
		AppFlowContext appFlowContext = new AppFlowContext();
		appFlowContext.setAppId(appId);
		appFlowContext.setFlowdocId(flowdocId);
		appFlowContext.setFlowId(flowId);
		appFlowContext.setMetadateId(metadataId);
		appFlowContext.setNodeId(nodeId);
		appFlowContext.setOperType(operType);
		appFlowContext.setLoginUser(loginUser);
		return appFlowContext;
	}
	/**
	 * 返回某条元数据信息的指定节点流转轨迹
	* Description:返回数据：Map格式: flowDoclist 所有流转轨迹信息
	* 	appFlowNodes 当前工作流所有节点信息<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-8 下午08:03:10
	* Last Modified:
	* @param appId 应用编号
	* @param metadataId 数据编号
	* @param nodeId 节点编号
	* @param preflowdocid 前一流转编号
	* @return Map<String, Object>
	* @throws Exception
	* @version 1.0
	 */
	public Map<String, Object> getMetadataFlowDoc(Long appId,Long metadataId,Long nodeId,Long preflowdocid) throws Exception{
		Map<String, Object> allResult= new HashMap<String, Object>();
		//当前工作流当前节点信息
		List<Object> appFlowNodes =appFlowService.getNodeByFlowId(getAppInfoToFlowId(appId));
		allResult.put("appFlowNodes", appFlowNodes);
		//工作流所有流转轨迹
		List<Object> parameters  = new ArrayList<Object>();
		parameters.add(metadataId);
		parameters.add(appId);
		parameters.add(nodeId);
		parameters.add(preflowdocid);
		List<Object> flowDoclist =  find(null, AppFlowDoc.class.getName(), "objid=? and appId=? and nodeId=? and preflowdocid=?", "flowDocId desc ",parameters);
		allResult.put("flowDoclist",flowDoclist);
		return allResult;
	}
	
	/**
	 * 文档工作流保存方法
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-12 下午07:04:29
	* Last Modified:
	* @param loginUser  登录用户
	* @param metadataId 文档编号
	* @param czyj 处理意见
	* @param operateType 操作<> 
	* @param flowDocId  当前流转轨迹编号
	* @param appId 应用编号
	* @param nextNodeId 下一个节点编号 会签节点不是最后一个人处理的话默认为0
	* @param nodeId  当前节点编号
	* @param flowToUserIds 已经选择的接受用户
	* @param flowToGroupIds 已经选择的接受部门
	* @param operType  操作，提供为工作流节点自定义操作使用 
	* @throws Exception
	* @version 1.0
	 */
	public String appMetaFlowSave(AppUser loginUser,Long metadataId,String czyj,String operateType,
			Long flowDocId,Long appId,Long nextNodeId,Long nodeId,String flowToUserIds,String flowToGroupIds,String operType) throws Exception{
		String flowId = getAppInfoFields(loginUser.getUserId(), "flowId", appId);
		String Code = "10000";
		if(nodeId >0){
			//有开始节点
			//查询当前的节点信息
			List<Object> appFlowNodes =	appFlowService.findByFlowNode(nodeId);
			AppFlowNode appFlowNode = (AppFlowNode)appFlowNodes.get(0); 
			
			if(CMyString.isEmpty(appFlowNode.getNextNodeIds())){//已经是结束节点了
				return Code;
			}
			AppFlowDoc appFlowDoc  = (AppFlowDoc)findById(AppFlowDoc.class, flowDocId);
			//TODO 签收直接签收后跳转，不执行下面的流程
			if(!CMyString.isEmpty(operateType)&&operateType.equals("签收")){
				saveAccept(appFlowDoc,operateType);
				return Code;
			}
			
			//会签开始
			if(appFlowNode.getTogether()==1){
				if(nextNodeId==0){//会签还没完成，只更新当前用户的处理意见
					saveCurrentFlowDoc(appFlowNode,appFlowDoc, loginUser.getUsername()+CMyString.showEmpty(loginUser.getTruename(),"")+czyj, 1,false,operateType,metadataId);
				}
				if(nextNodeId>0){//会签完成
					saveCurrentFlowDoc(appFlowNode,appFlowDoc, loginUser.getUsername()+CMyString.showEmpty(loginUser.getTruename(),"")+czyj, 1,true,operateType,metadataId);
					//会签完成
					String hqOpinion =gethqOpinion(flowDocId,metadataId);
					saveBeforeNextFlowDoc(nextNodeId, appId, nodeId, flowDocId, loginUser, appFlowDoc, metadataId,flowToUserIds, flowToGroupIds,hqOpinion);
				}
			}else{
				//非会签
				saveCurrentFlowDoc(appFlowNode,appFlowDoc, czyj, 0,true,operateType,metadataId);
				saveBeforeNextFlowDoc(nextNodeId, appId, nodeId, flowDocId, loginUser, appFlowDoc, metadataId,flowToUserIds, flowToGroupIds,null);
			}
		}else{
			Code = saveBeforeFirstNode(flowId, appId, loginUser.getUsername(), metadataId,operType);
		}
		return Code;
	}
	/**
	 * 开始节点调用方法
	* Description:该方法可以在前台普通用户提交信息时调用，系统后台不直接调用此方法，应该调用appMetaFlowSave方法<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午01:38:12
	* Last Modified:
	* @param flowId  工作流编号
	* @param appId 应用编号
	* @param userName  当前登录的用户名
	* @param metadataId  对象编号
	* @param operType 操作，提供为工作流节点自定义操作使用 
	* @throws NumberFormatException
	* @throws Exception
	* @version 1.0
	 */
	public String saveBeforeFirstNode(String flowId,Long appId,String userName,Long metadataId,String operType) throws NumberFormatException, Exception{
		//无开始节点，从开始节点开始
		//开始节点 获取开始节点
		 List<Object>  appFlows = appFlowService.getAppFlow(Long.parseLong(flowId));
		 Long sNodeId = 0l; 
		 for(Object obj : appFlows){
			 AppFlow appFlow = (AppFlow)obj;
			 sNodeId = appFlow.getSnodeId();
		 }
		 //获取当前节点信息
		List<Object> appFlowNodes =  appFlowService.findByFlowNode(sNodeId);//当前节点信息
		AppFlowNode appFlowNode  = null;
		for(Object obj :appFlowNodes){
			 appFlowNode = (AppFlowNode) obj;
		}
		if(!CMyString.isEmpty(appFlowNode.getOperRuleName())){//自定义操作
			if(CMyString.isEmpty(operType)){
				return "10001";
			}
			AppFlowContext appFlowContext = setAppFlowContext(appId, 0l, appFlowNode.getFlowId(), metadataId, appFlowNode.getNodeId(), operType, null);
			Map<String,Object> maps= createNodeIdAndFLowUser(appFlowNode.getOperRuleName(), appFlowContext);
			Long nextNodeId = Long.parseLong((String)maps.get("nodeId"));
			String flowToUserIds=(String) maps.get("userIds");
			if(CMyString.isEmpty(flowToUserIds)){
				return "10002";
			}
			appFlowNode =  (AppFlowNode)findById(AppFlowNode.class, nextNodeId);
			appFlowNode.setNodeUser(flowToUserIds);
			appFlowNode.setNodeDep(null);//自定义规则不需要部门，如果有也需要清空
		}
		if(!CMyString.isEmpty(appFlowNode.getRuleName())){//自定义规则启用
			 
			//自定义规则返回的用户id集合
			AppFlowContext appFlowContext = setAppFlowContext(appId, 0l, appFlowNode.getFlowId(), metadataId, appFlowNode.getNodeId(), "",null);
			 String uids = changeflows(appFlowNode.getRuleName(),appFlowContext);//获取自定义规则返回的用户信息
			 List<Object> flowUsers = getFlowNodeUs(appId, uids);
			 if(flowUsers!=null&&flowUsers.size()<1){
					return "10003";
			 }
			 saveFirstNode(flowUsers, appFlowNode, userName, metadataId, appId);
			
		}else{ //不启动自定义规则
			//appFlowNode.getNodeUser();当前节点的用户  appFlowNode.getNodeDep();当前节点的部门 
			List<Object> flowUsers = getGrpAndRoleAndFolwUserToUsers(appFlowNode.getNodeDep(), appId, appFlowNode.getNodeUser());
			 if(flowUsers!=null&&flowUsers.size()<1){
					return "10004";
			 }
			saveFirstNode(flowUsers, appFlowNode, userName, metadataId, appId);
		}
		return "10000";
	}
	/**
	 * 保存工作流第一个节点
	* Description:不建议二次开发调用,不直接调用该方法<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-5 下午12:07:12
	* Last Modified:
	* @param flowUsers 要保存的节点用户对象结合
	* @param appFlowNode 当前第一个节点对象
	* @param userName 当前登录用户的用户名<建议前台普通用户提交信息的时候传入参数统一为System用户，方便后台对用户的区分>
	* @param metadataId 当前文档编号
	* @param appId 应用ID
	* @version 1.0
	 */
	public void saveFirstNode(List<Object> flowUsers,AppFlowNode appFlowNode,String userName ,Long metadataId,Long appId){
		List<Object> appFlowDocs = new ArrayList<Object>();
		List<String> emails= new ArrayList<String>();
		List<String> tels= new ArrayList<String>();
		for(Object appusers:flowUsers){
			AppUser appUser= (AppUser)appusers;
			emails.add(appUser.getEmail());
			tels.add(appUser.getMoblie());
			AppFlowDoc appFlowDoc = new AppFlowDoc();
			appFlowDoc.setNodeId(appFlowNode.getNodeId());//当前节点
			appFlowDoc.setPrenodeid(-1l); //文档所处的前一节点编号
			appFlowDoc.setPostUser("System");//提交用户
			appFlowDoc.setPostTime(DateUtil.now());//提交时间
			appFlowDoc.setPostDesc("系统默认驱动流转");//提交说明  交办意见
			appFlowDoc.setTousers(appUser.getUsername());//目标用户
			appFlowDoc.setCrtime(DateUtil.now());//创建时间
			appFlowDoc.setCruser(userName);//创建用户
			appFlowDoc.setFlag(appFlowNode.getTogether());//是否会签  0 否 ，1 是
			appFlowDoc.setObjid(metadataId);//流转对象编号
			appFlowDoc.setReceivetime(DateUtil.now());//到达时间
			appFlowDoc.setWorked(0);//是否已处理，第一节点默认未处理
			//appFlowDoc.setWorktime(worktime)// 处理时间
			appFlowDoc.setPreflowdocid(-1l);//前一节点流转编号 ,默认第一节点的前一节点为-1
			appFlowDoc.setAccepted(0);//是否已签收
			appFlowDoc.setFlowStatus(appFlowNode.getNodeDocStatus()); //节点自定义状态
			//appFlowDoc.setAccepttime(accepttime) //签收时间
			appFlowDoc.setAppId(appId);//应用ID
			appFlowDoc.setIsOwnerWork(0);//是否我处理  0未处理，1 已处理
			appFlowDocs.add(appFlowDoc);
		}
		publicAppBaseDao.saveOrUpdateAll(appFlowDocs);
		sendEmailOrSMS(appFlowNode, emails, tels,appId,metadataId);
	}
	/**
	 * 驱动下个节点的工作流流转,写入轨迹
	* Description:调用 saveNextFlowDoc 方法，为该方法提供通用的调用方法<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-12 下午06:48:44
	* Last Modified:
	* @param nextNodeId 下一个节点的编号
	* @param appId  应用编号
	* @param parentNodeId  当前节点编号
	* @param parentFlowdocId 当前流转轨迹编号
	* @param loginUser 用户
	* @param appFlowDoc  当前流转轨迹对象
	* @param metadataId 文档编号
	* @param hqOpinion 会签意见，不为空则认为是会签的意见，将直接保存该字段
	* @throws Exception
	* @version 1.0
	 */
	public void saveBeforeNextFlowDoc(Long nextNodeId,Long appId,Long parentNodeId,
			Long parentFlowdocId,AppUser loginUser,AppFlowDoc appFlowDoc,Long metadataId,
			String flowToUserIds,String flowToGroupIds,String hqOpinion) throws Exception{
		List<Object> nextAppFlowNodes =	appFlowService.findByFlowNode(nextNodeId);
		AppFlowNode nextAppFlowNode = (AppFlowNode)nextAppFlowNodes.get(0); 
		
		List<Object> flowUsers = getGrpAndRoleAndFolwUserToUsers(flowToGroupIds, appId, flowToUserIds);
		
		if(CMyString.isEmpty(hqOpinion)){
			saveNextFlowDoc(flowUsers, nextAppFlowNode, parentNodeId, parentFlowdocId, loginUser, appFlowDoc.getTogetherusers(), metadataId, appId);
		}else{
			saveNextFlowDoc(flowUsers, nextAppFlowNode, parentNodeId, parentFlowdocId, loginUser, hqOpinion, metadataId, appId);
		}
	}
	/**
	 * 驱动下一个工作流节点信息,
	* Description:不建议二次开发调用<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-12 下午05:15:54
	* Last Modified:
	* @param flowUsers  工作流处理用户
	* @param nextAppFlowNode  工作流下一个节点信息
	* @param parentNodeId  当前节点编号
	* @param parentFlowdocId   当前流转轨迹编号
	* @param loginUser  当前登录用户
	* @param zcyj  处理意见，注意会签保存的是会签的处理意见合集
	* @param metadataId  文档编号
	* @param appId 应用编号
	* @version 1.0
	 * @throws Exception 
	 */
	public void saveNextFlowDoc(List<Object> flowUsers,AppFlowNode nextAppFlowNode,Long parentNodeId,Long parentFlowdocId,AppUser loginUser,String zcyj ,Long metadataId,Long appId) throws Exception{
		List<Object> appFlowDocs = new ArrayList<Object>();
		if(appFlowService.isLastNode(nextAppFlowNode.getNodeId())){
			//结束节点
			AppFlowDoc appFlowDoc = new AppFlowDoc();
			appFlowDoc.setNodeId(nextAppFlowNode.getNodeId());//当前节点
			appFlowDoc.setPrenodeid(parentNodeId); //文档所处的前一节点编号
			appFlowDoc.setPostUser(loginUser.getUsername());//提交用户
			appFlowDoc.setPostTime(DateUtil.now());//提交时间
			appFlowDoc.setPostDesc(zcyj);//提交说明  交办意见
			appFlowDoc.setTousers("System");//目标用户
			appFlowDoc.setCrtime(DateUtil.now());//创建时间
			appFlowDoc.setCruser(loginUser.getUsername());//创建用户
			appFlowDoc.setFlag(nextAppFlowNode.getTogether());//是否会签  0 否 ，1 是
			appFlowDoc.setObjid(metadataId);//流转对象编号
			appFlowDoc.setReceivetime(DateUtil.now());//到达时间
			appFlowDoc.setWorked(1);//是否已处理，默认未处理
			appFlowDoc.setWorktime(DateUtil.now());// 处理时间
			appFlowDoc.setPreflowdocid(parentFlowdocId);//前一节点流转编号 ,默认第一节点的前一节点为-1
			appFlowDoc.setAccepted(0);//是否已签收
			appFlowDoc.setFlowStatus(nextAppFlowNode.getNodeDocStatus()); //节点自定义状态
			//appFlowDoc.setAccepttime(accepttime) //签收时间
			appFlowDoc.setAppId(appId);//应用ID
			appFlowDoc.setIsOwnerWork(1);//是否我处理  0未处理，1 已处理
			appFlowDocs.add(appFlowDoc);
			publicAppBaseDao.saveOrUpdateAll(appFlowDocs);
		}else{
			List<String> emails= new ArrayList<String>();
			List<String> tels= new ArrayList<String>();
			for(Object appusers:flowUsers){
				AppUser appUser= (AppUser)appusers;
				emails.add(appUser.getEmail());
				tels.add(appUser.getMoblie());
				AppFlowDoc appFlowDoc = new AppFlowDoc();
				appFlowDoc.setNodeId(nextAppFlowNode.getNodeId());//当前节点
				appFlowDoc.setPrenodeid(parentNodeId); //文档所处的前一节点编号
				appFlowDoc.setPostUser(loginUser.getUsername());//提交用户
				appFlowDoc.setPostTime(DateUtil.now());//提交时间
				appFlowDoc.setPostDesc(zcyj);//提交说明  交办意见
				appFlowDoc.setTousers(appUser.getUsername());//目标用户
				appFlowDoc.setCrtime(DateUtil.now());//创建时间
				appFlowDoc.setCruser(loginUser.getUsername());//创建用户
				appFlowDoc.setFlag(nextAppFlowNode.getTogether());//是否会签  0 否 ，1 是
				appFlowDoc.setObjid(metadataId);//流转对象编号
				appFlowDoc.setReceivetime(DateUtil.now());//到达时间
				appFlowDoc.setWorked(0);//是否已处理，默认未处理
				//appFlowDoc.setWorktime(worktime)// 处理时间
				appFlowDoc.setPreflowdocid(parentFlowdocId);//前一节点流转编号 ,默认第一节点的前一节点为-1
				appFlowDoc.setAccepted(0);//是否已签收
				//appFlowDoc.setAccepttime(accepttime) //签收时间
				appFlowDoc.setFlowStatus(nextAppFlowNode.getNodeDocStatus()); //节点自定义状态
				appFlowDoc.setAppId(appId);//应用ID
				appFlowDoc.setIsOwnerWork(0);//是否我处理  0未处理，1 已处理
				appFlowDocs.add(appFlowDoc);
			}
			publicAppBaseDao.saveOrUpdateAll(appFlowDocs);
			sendEmailOrSMS(nextAppFlowNode, emails, tels,appId,metadataId);
		}
	}
	
	/**
	 * 有工作流的应用默认添加数据之前判断当前应用的初始化节点是否正确
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午01:48:05
	* Last Modified:
	* @param flowId 工作流编号
	* @param appId 应用编号
	* @param userName 用户名
	* @param metadataId
	* @return String 
	* @throws NumberFormatException
	* @throws Exception
	* @version 1.0
	 */
	public String isSaveFlow(Long appId,Long metadataId,AppUser loginUser,String operType) throws NumberFormatException, Exception{
		String code = "11000"; //正确返回
		try {
			AppInfo appInfo =(AppInfo)appFlowService.findById(AppInfo.class, appId);
			//开始节点 获取开始节点
			 List<Object>  appFlows = appFlowService.getAppFlow(appInfo.getFlowId());
			 Long sNodeId = 0l; 
			 Long nNodeId = 0l;
			 for(Object obj : appFlows){
				 AppFlow appFlow = (AppFlow)obj;
				 sNodeId = appFlow.getSnodeId();
				 nNodeId = appFlow.getEnodeId();
			 }
			 if(sNodeId<1){
				 code="11001";
			 }
			 if(nNodeId<1){
				 code = "11002";
			 }
			 //获取当前节点信息
			List<Object> appFlowNodes =  appFlowService.findByFlowNode(sNodeId);//当前节点信息
			AppFlowNode appFlowNode  = null;
			for(Object obj :appFlowNodes){
				 appFlowNode = (AppFlowNode) obj;
			}
			if(!CMyString.isEmpty(appFlowNode.getOperRuleName())){//自定义操作
				if(CMyString.isEmpty(operType)){
					return "10001";
				}
				AppFlowContext appFlowContext = setAppFlowContext(appId, 0l, appFlowNode.getFlowId(), metadataId, appFlowNode.getNodeId(), operType, null);
				Map<String,Object> maps= createNodeIdAndFLowUser(appFlowNode.getOperRuleName(), appFlowContext);
				if(maps==null || maps.isEmpty()){
					return "10005";
				}
				Long nextNodeId = Long.parseLong((String)maps.get("nodeId"));
				String flowToUserIds=(String) maps.get("userIds");
				if(CMyString.isEmpty(flowToUserIds)){
					return "10002";
				}
				appFlowNode =  (AppFlowNode)findById(AppFlowNode.class, nextNodeId);
				appFlowNode.setNodeUser(flowToUserIds);
				appFlowNode.setNodeDep(null);//自定义规则不需要部门，如果有也需要清空
			}
			if(!CMyString.isEmpty(appFlowNode.getRuleName())){//自定义规则启用
				//自定义规则返回的用户id集合
				AppFlowContext appFlowContext = setAppFlowContext(appId, 0l, appFlowNode.getFlowId(), metadataId, appFlowNode.getNodeId(), "",loginUser);
				 String uids = changeflows(appFlowNode.getRuleName(),appFlowContext);//获取自定义规则返回的用户信息
				 if(CMyString.isEmpty(uids)){
					 code = "11003";
					 return code;
				 }
				 List<Object> us =	getFlowNodeUs(appId, uids);
				 if(us==null || us.size()<1){
					 code = "11004";
					 return code;
				 }
				
			}else{ //不启动自定义规则
				List<Object> us =	getGrpAndRoleAndFolwUserToUsers(appFlowNode.getNodeDep(), appId, appFlowNode.getNodeUser());
				 if(us==null || us.size()<1){
					 code = "11005";
					 return code;
				 }
			}
		} catch (Exception e) {
			code="19999";
			LOG.error("当前应用初始化节点角色没有配置有效的接收用户和部门，或者当前应用所属的组织中没有有效用户"+e);
		}
		return code;
	}
	/**
	 * 工作流节点状态
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-23 下午03:38:25
	* Last Modified:
	* @param datastatusId 状态编号
	* @return AppDatastatus
	* @version 1.0
	 */
	public AppDatastatus getDatastatus(Long datastatusId){
		return (AppDatastatus)findById(AppDatastatus.class, datastatusId);
	}
	/*
	 *  //获取会签意见
	 */
	public String gethqOpinion(Long flowDocId,Long matedateId) throws Exception{
		 //获取会签意见
		AppFlowDoc appFlowDoc = (AppFlowDoc) findById(AppFlowDoc.class, flowDocId);
		StringBuffer hql = new StringBuffer("select togetherusers from AppFlowDoc where prenodeid=").append(appFlowDoc.getPrenodeid()).append(" and objid=").append(matedateId);
		
		List<Object> flowDocIds =publicAppBaseDao.find(hql.toString());
		return CMyString.join((ArrayList<Object>)flowDocIds, "</br>");
	}
	/*
	 * 工作流签收
	 
	 */
	public void saveAccept(AppFlowDoc appFlowDoc,String operateType) throws SQLException{
	
		appFlowDoc.setAccepted(1);//签收
		appFlowDoc.setAccepttime(DateUtil.now());
		appFlowDoc.setOperateType(operateType);//当前节点操作方式
		publicAppBaseDao.update(appFlowDoc);//仅更新当前
	}
	/*
	 * 保存当前处理意见
	 * AppFlowDoc 当前流转轨迹
	 * czyj 处理意见
	 * flag 是否会签，0 否1是
	 * islast 如果是当前节点最后一个处理人 则为true
	 */
	public void saveCurrentFlowDoc(AppFlowNode appFlowNode,AppFlowDoc appFlowDoc ,String czyj,int flag,boolean islast,String operateType,Long matedateId) throws SQLException{
		if(islast){
			appFlowDoc.setWorked(1);//已处理
			//将所有会签,非会签记录的处理状态修改为已处理
			StringBuffer sql = new StringBuffer("update app_flow_doc set WORKED=1 where OBJID=").append(matedateId).append(" and ").append(" prenodeid=").append(appFlowDoc.getPrenodeid());
			publicAppBaseDao.executeBaseSql(sql.toString());
		}
		appFlowDoc.setWorktime(DateUtil.now());
		appFlowDoc.setAccepted(1);//已处理默认为已签收
		appFlowDoc.setAccepttime(DateUtil.now());
		appFlowDoc.setIsOwnerWork(1);//是当前用户处理
		appFlowDoc.setOperateType(operateType);//当前节点操作方式
		if(flag==1){
			appFlowDoc.setTogetherusers(czyj);//当前节点的处理意见,会签进行拼接
			appFlowDoc.setWorked(1); //会签节点将当前操作人的记录处理状态标记为已处理
		}else{
			appFlowDoc.setTogetherusers(czyj);//当前节点的处理意见
		}
		appFlowDoc.setFlag(flag);//会签
		publicAppBaseDao.update(appFlowDoc);//仅更新当前
	}
	/**
	 * 返回信息的当前流转轨迹
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-8 下午01:39:09
	* Last Modified:
	* @param metadataId  信息编号
	* @param flowId      工作流编号
	* @param toUser  当前用户名
	* @return AppFlowDoc  
	* @throws Exception
	* @version 1.0
	 */
	public AppFlowDoc getCurrentFlowDoc(Long metadataId,Long appId,String toUser) throws Exception{
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(metadataId);
		parameters.add(appId);
		parameters.add(toUser);
		List<Object> appFlowDoc =  find(null, AppFlowDoc.class.getName(), "objid=? and appId=? and tousers=? and worked=0 ", " flowDocId desc", parameters);
		AppFlowDoc currAppFlowDoc = null;
		if(appFlowDoc!=null&&appFlowDoc.size()>0){
			currAppFlowDoc = (AppFlowDoc)appFlowDoc.get(0);
		}
		
		return currAppFlowDoc;
	}
	
	
	/*
	 * 提醒发送调用
	 */
	public void sendEmailOrSMS(AppFlowNode appFlowNode,List<String> emails,List<String> tels,Long appId,Long metadataId){
		
		//TODO 邮件，短信提醒  是否会签  
		//查询节点组织和用户 ，要过滤重复用户。2 查询当前应用的角色信息， 3 将所有用户和角色信息进行对比过滤没有应用权限的用户
		if(appFlowNode.getIsemail()==1){ //邮件
			AppSynSendMessage appSynSendMessage = new AppSynSendMessage(0,appId,messageSend,emails,tels,metadataId);
		      new Thread(appSynSendMessage).start();
		}
		if(appFlowNode.getIsmessage()==1){//短信
			AppSynSendMessage appSynSendMessage = new AppSynSendMessage(1,appId,messageSend,emails,tels,metadataId);
		      new Thread(appSynSendMessage).start();
			
		}
	}
	/**
	 * 返回前一个流转轨迹信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-22 下午02:13:46
	* Last Modified:
	* @param flowDocId
	* @param metadataId
	* @return Map<String,Object> 返回格式  Map<String,Object>{nodeId,userIds}
	* @throws Exception
	* @version 1.0
	 */
	public Map<String,Object> backFlow(Long flowDocId,Long metadataId) throws Exception{
		 Map<String,Object>  backList = new HashMap<String, Object>();
		AppFlowDoc appFlowDoc  = (AppFlowDoc)findById(AppFlowDoc.class, flowDocId);
		AppFlowDoc befoappFlowDoc  =(AppFlowDoc)findById(AppFlowDoc.class, appFlowDoc.getPreflowdocid());
		List<Object> tousers =find("tousers", AppFlowDoc.class.getName(), "prenodeid=? and objid="+metadataId, null, befoappFlowDoc.getPrenodeid());
		StringBuffer swhere = new StringBuffer();
		for(int i=0;i<tousers.size();i++){
			swhere.append("username=").append("'").append(tousers.get(i)).append("'");
			if(i<tousers.size()-1){
				swhere.append(" or ");
			}
		}
		List<Object> uList = find( "userId",AppUser.class.getName(),"1=? and "+swhere+"",null,1);
		backList.put("nodeId",befoappFlowDoc.getNodeId());
		backList.put("userids",uList.toString().substring(1, uList.toString().length()-1));
		return backList;
	}
	/**
	 * 返回当前应用对应的工作流编号
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-8 下午02:26:55
	* Last Modified:
	* @param appId
	* @return
	* @version 1.0
	 */
	public Long getAppInfoToFlowId(Long appId){
		AppInfo appInfo = (AppInfo)findById(AppInfo.class, appId);
		return appInfo.getFlowId();
	}
	/**
	 * 判断当前应用是否有工作流
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-4 下午03:58:17
	* Last Modified:
	* @param appId
	* @return
	* @version 1.0
	 */
	public boolean isAppFlow(Long appId){
		AppInfo appInfo = (AppInfo)findById(AppInfo.class, appId);
		return appInfo.getFlowId()>0;
	}
	/**
	 * 返回应用绑定的工作流
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-22 下午01:23:27
	* Last Modified:
	* @param appId
	* @return
	* @version 1.0
	 */
	public AppFlow getAppFlow(Long appId){
		AppInfo appInfo = (AppInfo)findById(AppInfo.class, appId);
		AppFlow appFlow =(AppFlow) findById(AppFlow.class, appInfo.getFlowId());
		return appFlow;
	}
	/**
	 * 返回应用
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-22 下午01:23:27
	* Last Modified:
	* @param appId
	* @return
	* @version 1.0
	 */
	public AppInfo getAppInfo(Long appId){
		AppInfo appInfo = (AppInfo)findById(AppInfo.class, appId);
		
		return appInfo;
	}
	/*#########################################################################################################*/
	/*####                              工作流模块方法 结束                                                                                                                          ########*/
	/*#########################################################################################################*/
	/**
	 * 返回应用(元数据)的数据列表，有工作流<原生SQL>
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-3 下午05:26:59
	* Last Modified:
	* @param appId 应用ID
	* @param tableName 要查询的元数据表名，数据库中真实的表名
	* @param sFields 要查询的字段
	* @param toUser 当前登录用户名
	* @param workd 工作流状态   0.未处理;1.已处理
	* @param deleted 信息删除状态   0为未删除，1为已删除
	* @param Is_Owner_Work  是否是我办理的   0未处理,1已处理
	* @param sOrder  排序条件
	* @param nStartPage    
	* @param nPageSize
	* @return Page
	* @throws Exception
	* @version 1.0
	 */
	public Page queryFlowAppInfos(Long appId,String tableName,String sFields ,String toUser,String workd,int deleted,
			int Is_Owner_Work,String sOrder,int nStartPage, int nPageSize) throws Exception{
		//String sWhere,List<Object> parameters
		if(CMyString.isEmpty(sFields)){
			throw new Exception("概览字段不能为空");
		}
		
		StringBuffer tableNames = new StringBuffer(tableName);
		tableNames.append(" am "); //信息表
		tableNames.append(",APP_FLOW_DOC af "); //工作流表
		StringBuffer sWhere = new StringBuffer(" am.appid= af.app_id ");
		sWhere.append("and af.tousers=?");
		sWhere.append(" and af.worked=?");
		sWhere.append(" and am.deleted=? ");
		sWhere.append(" and af.Is_Owner_Work =?");
		sWhere.append(" and af.app_id =?");
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(toUser);
		parameters.add(workd);
		parameters.add(deleted);
		parameters.add(Is_Owner_Work);
		parameters.add(appId);
		StringBuffer sOrders = new StringBuffer("am.").append(sOrder.trim());
		
			//总记录数原生SQL
			String countSql = ChangeDBType.SqlCountRecord(tableNames.toString(), sWhere.toString());
			//查询总记录数
			int totalResults = publicAppBaseDao.executeQueryInt(countSql,parameters);
			Page page = new Page(nStartPage, nPageSize, totalResults);
			// page = new Page(nStartPage, nPageSize, 20);
			// page.setTotalResults(totalResults);
			String sSql = ChangeDBType.changeDbTypeSql(splitFields(sFields), tableNames.toString(),  sWhere.toString(), sOrders.toString(), page.getStartIndex(), page.getPageSize()*page.getStartPage());
			//sql
			LOG.debug(sSql);
			List<Object> dataList = publicAppBaseDao.executeQueryObjs(sSql,parameters);
			page.setLdata(dataList);
		
		return page;
	}
	/**
	 * 返回应用(元数据)的数据列表，无工作流<原生SQL>
	* Description:为了方法可以通用，目前where条件和排序条件是动态传入<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-24 下午05:44:40
	* Last Modified:
	* @param sWhere 执行原生SQL的条件  不需要where  <所有条件条件拼装在这里> 带占位符的SQL条件 eg:appId=? and groupId=?
	* @param parameters 参数的值 格式：List<Object>  where条件的实际参数值 
	* @param sOrder 排序条件，不需要order by 
	* @param nStartPage 开始页
	* @param nPageSize  页大小
	* @return Page page对象 包含分页信息和数据列表
	* @throws Exception
	* @version 1.0
	 */
	public Page queryAppInfos(String tableName,String sFields ,String sWhere,List<Object> parameters,String sOrder,int nStartPage, int nPageSize) throws Exception{
		if(CMyString.isEmpty(sFields)){
			throw new Exception("概览字段不能为空");
		}
			//总记录数原生SQL
			String countSql = ChangeDBType.SqlCountRecord(tableName, "distinct metadataid", sWhere);
			//查询总记录数
			int totalResults = publicAppBaseDao.executeQueryInt(countSql,parameters);
			Page page = new Page(nStartPage, nPageSize, totalResults);
			// page = new Page(nStartPage, nPageSize, 20);
			// page.setTotalResults(totalResults);
			String sSql = ChangeDBType.changeDbTypeSql(sFields, tableName, sWhere, sOrder, page.getStartIndex(), page.getPageSize()*page.getStartPage());
			//sql
			LOG.debug(sSql);
			List<Object> dataList = publicAppBaseDao.executeQueryObjs(sSql,parameters);
			page.setLdata(dataList);
		
		return page;
	}
	/**
	 * 返回应用(元数据)的数据列表，无工作流(供webservice使用)<原生SQL>
	* Description:为了方法可以通用，目前where条件和排序条件是动态传入<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-24 下午05:44:40
	* Last Modified:
	* @param sWhere 执行原生SQL的条件  不需要where  <所有条件条件拼装在这里> 带占位符的SQL条件 eg:appId=? and groupId=?
	* @param parameters 参数的值 格式：List<Object>  where条件的实际参数值 
	* @param sOrder 排序条件，不需要order by 
	* @return List<Object> 数据列表
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> queryAppInfos(String tableName,String sFields ,String sWhere,List<Object> parameters,String sOrder, int maxResult) throws Exception{
		if(CMyString.isEmpty(sFields)){
			throw new Exception("概览字段不能为空");
		}
		//总记录数原生SQL
		//String countSql = ChangeDBType.SqlCountRecord(tableName, sWhere);
		//查询总记录数
		//int totalResults = publicAppBaseDao.executeQueryInt(countSql,parameters);
		String sSql = ChangeDBType.changeDbTypeSql(sFields, tableName, sWhere, sOrder, maxResult);
		//sql
		LOG.debug(sSql);
		List<Object> dataList = publicAppBaseDao.executeQueryObjs(sSql,parameters);
		return dataList;
	}
//	/**
//	 * 返回应用对应的元数据表名
//	* Description:<BR>  
//	* @author wen.junhui
//	* @date Create: 2014-3-25 下午04:44:41
//	* Last Modified:调用baseservice 中方法
//	* @param appId 应用ID
//	* @param mainTable 主从表，0 是主表，1从表
//	* @return String 元数据表名称
//	* @throws Exception
//	* @version 1.0
//	 */
//	public String getMetaTableName(Long appId,int mainTable) throws Exception{
//		List<Object> params = new ArrayList<Object>();
//		//查询应用的表
//		String field = "mainTableName";
//		if(mainTable==1){ //1 返回从表表名
//			 field = "itemTableName";
//		}
//		params.add(appId);
//		//视图对应的字段信息表查询对应的表名
//		List<Object> appInfos = find(field, AppInfo.class.getName(), "appId=?", null, params);
//		if(appInfos.size()<1){
//			throw new Exception("元数据表名不存在");
//		}		
//		String tableName = (String)appInfos.get(0); 
//		if(CMyString.isEmpty(tableName)|| tableName.equals("null")){
//			throw new Exception("元数据表名称不能为空或者空字符串!");
//		}
//		return tableName;
//	}
	
	/**
	 * 查询应用的字段,新建一条应用信息时也调用此方法，第二个(inOutline)参数为-1 即可获取所有的字段集合
	* Description:  app_field_rel 该对象中查询<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-24 下午05:39:56
	* Last Modified:
	* @param userId 当前用户
	* @param appId 应用的ID
	* @param inOutline 0 是否概览显示，-1查询所有字段  2 查询检索字段(-1,1,2)
	* @param mainTable 主从表，0 是主表，1从表
	* @return List<Object> 返回应用字段对象<AppFieldRel>集合,每个字段一条记录
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> queryAppFields(Long userId,Long appId,int inOutline,int mainTable) throws Exception{
		if(appId<1){
			throw new Exception("应用ID不能小于等于0");
		}
		String viewId =getAppInfoFields(userId, "viewId", appId);//视图编号
		String tableName = getMetaTableName(appId, mainTable);//表名称
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sSql = new StringBuffer("viewId=?").append(" and mainTableName=?");
		params.add(viewId);
		params.add(tableName);
		if(inOutline==1){
			params.add(inOutline);//是否可概览显示,需要根据系统数据字典参数中定义"inOutline"
			sSql.append(" and inOutline = ?");
		}if(inOutline==2){
			sSql.append(" and searchField = 1");
		}
		LOG.debug(sSql);
		List<Object> appFieldRels = find(null, AppFieldRel.class.getName(), sSql.toString(), " fieldOrder asc ", params);
		return appFieldRels;
	}
	/**
	 * 保存修改应用(元数据)对应的信息,原生SQL
	* Description:metadataid 为系统内置的元数据主键字段，修改信息该字段必须为实际值，新增数据map中不需要该字段<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 下午02:29:56
	* Last Modified:
	* @param appinfo 要保存的元数据信息,以map的形式组装数据， 格式 Map<"name","wenjunhui"> 
	* @param tableName 要保存修改的元数据表名，必须为实际数据库中的表名
	* @throws SQLException
	* @version 1.0
	 */
	public Long saveOrUpdateMetadata(Map<String,Object> appinfo,String tableName) throws SQLException{
		if(appinfo.size()<1){
			throw new SQLException("空值不能保存：保存修改元数据的信息不能为空！");
		}
		if(CMyString.isEmpty(tableName)){
			throw new SQLException("必须指定需要保存修改的元数据表名称！");
		}
		List<Object> parameters  = new ArrayList<Object>();
		//字段拼接
		StringBuffer sFileds = new StringBuffer("");
		//? 参数拼接
		StringBuffer params = new StringBuffer();
		//sql 语句拼接
		StringBuffer sSql = new StringBuffer();
		if(appinfo.get("METADATAID")!=null){
//			更新操作METADATAID 
			sSql.append("update ").append(tableName).append(" set ");
			for (String key : appinfo.keySet()) {
				if(key.equals("METADATAID")){continue;}
				sFileds.append(key).append("=?").append(",");
				parameters.add(appinfo.get(key));
			}
			//更新字段
			sSql.append(sFileds.toString().substring(0,sFileds.length()-1));
			//条件
			sSql.append(" where METADATAID=?");
			parameters.add(appinfo.get("METADATAID"));//元数据默认ID 字段
		}else{
			//新增操作
			sSql.append("insert into ").append(tableName).append("(");
			for (String key : appinfo.keySet()) {
				sFileds.append(key).append(",");
				params.append("?").append(",");
				parameters.add(appinfo.get(key));
			}
			if(Global.DB_TYPE.equals("oracle"))
				sSql.append(" METADATAID,");
			sSql.append(sFileds.toString().substring(0,sFileds.length()-1)).append(")").append(" values(");
			if(Global.DB_TYPE.equals("oracle"))
				sSql.append(" HIBERNATE_SEQUENCE.nextval, ");
			sSql.append(params.toString().substring(0,params.length()-1)).append(")");
		}
			
		//TODO 仅限于oracle 使用序列
		//日志
		LOG.debug(sSql.toString());
		Long metadataId = publicAppBaseDao.saveBaseSql(sSql.toString(), parameters);
		return metadataId;
		
	}
	/**
	 * 删除元数据   原生SQL
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-29 下午05:39:41
	* Last Modified:
	* @param appId
	* @param tableName
	* @param metadataIds
	* @throws SQLException
	* @version 1.0
	 */
	public void delMetadatas(Long appId,String tableName,String metadataIds) throws SQLException{
		
		
		
	}
	/**
	 * 批量保存修改应用(元数据)对应的信息,原生SQL<BR> 
	 * 注意每次执行的操作不管数据多少，都只能选择一种操作方式,更新或者新增<BR> 
	* Description: metadataid 为系统内置的元数据主键字段，修改信息该字段必须为实际值，新增数据map中不需要该字段<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 下午03:22:50
	* Last Modified:
	* @param appinfo 需要保存修改的元数据，以以map的形式组装数据， 格式 Map<"name","wenjunhui"> ，然后加入到List中
	* @param tableName 要保存修改的元数据表名，必须为实际数据库中的表名
	* @throws SQLException
	* @version 1.0
	 */
	public void saveOrUpdateMetadatas(List<Map<String,Object>> appinfo,String tableName) throws SQLException{
		if(appinfo.size()<1){
			throw new SQLException("空值不能保存：保存修改元数据的信息不能为空！");
		}
		if(CMyString.isEmpty(tableName)){
			throw new SQLException("必须指定需要保存修改的元数据表名称！");
		}
		List<List<Object>> parameters = new ArrayList<List<Object>>();
		//sql
		StringBuffer sSql = new StringBuffer();
		//字段拼接
		StringBuffer sFileds = new StringBuffer("");
		//sql 语句拼接
		StringBuffer params = new StringBuffer();
		//修改操作
		if(appinfo.get(0).get("METADATAID")!=null){
			sSql.append("update ").append(tableName).append(" set ");
			for (String key : appinfo.get(0).keySet()) {
				if(key.equals("METADATAID")){continue;}
				sFileds.append(key).append("=?").append(",");
			}
			//字段
			sSql.append(sFileds.toString().substring(0,sFileds.length()-1));
			//条件
			sSql.append(" where METADATAID=?");
			for(Map<String,Object> currAppInfo : appinfo){
				List<Object> parameter  = new ArrayList<Object>();
				for (String key : currAppInfo.keySet()) {
					if(key.equals("METADATAID")){continue;}
					parameter.add(currAppInfo.get(key));
				}
				//这里是为了更新操作需要在where条件中设置需要更新的元数据ID
				parameter.add(currAppInfo.get("METADATAID"));
				parameters.add(parameter);
			}
		}else{
			//新增操作
			sSql.append("insert into ").append(tableName).append("(");
			for (String key : appinfo.get(0).keySet()) {
				sFileds.append(key).append(",");
				params.append("?").append(",");
			}
			for(Map<String,Object> currAppInfo : appinfo){
				List<Object> parameter  = new ArrayList<Object>();
				for (String key : currAppInfo.keySet()) {
					parameter.add(currAppInfo.get(key));
				}
				parameters.add(parameter);
			}
				//TODO 仅限于oracle 使用序列
				if(Global.DB_TYPE.equals("oracle")){
					sSql.append(" METADATAID, ");
				}
				sSql.append(sFileds.toString().substring(0,sFileds.length()-1)).append(")").append(" values(");
				
				if(Global.DB_TYPE.equals("oracle")){
					sSql.append(" HIBERNATE_SEQUENCE.nextval, ");
				}
				sSql.append(params.toString().substring(0,params.length()-1)).append(")");
		}
		//日志
		LOG.debug(sSql.toString());
		publicAppBaseDao.saveBaseSqls(sSql.toString(), parameters);
		
	}
	
	/**
	 * 判断当前对象是否被锁定，锁定返回true ，否则返回false
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-1 上午11:19:00
	* Last Modified:
	* @param metaDataId 数据编号
	* @param metaTableName 表名 数据库中实际表名
	* @return boolean 
	* @throws Exception
	* @version 1.0
	 */
	public boolean isLockObject(Long metaDataId,String metaTableName) throws Exception{
		List<Object> appReLock = getisLockAppRelock(metaDataId,metaTableName);
		return appReLock.size()>0;
	}
	/**
	 * 查找要锁定的对象
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-1 上午11:28:26
	* Last Modified:
	* @param metaDataId
	* @param metaTableName
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getisLockAppRelock(Long metaDataId,String metaTableName) throws Exception{
		if(metaDataId<1){
			throw new Exception("要判断锁定的数据编号不正确");
		}
		if(CMyString.isEmpty(metaTableName)){
			throw new Exception("要判断锁定的对象表名不能为空");
		}
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(metaTableName);
		parameters.add(metaDataId);
		StringBuffer sWhere = new StringBuffer("tableName=? and recordId=?");
		List<Object> appReLock = find(null,AppRecordLock.class.getName(), sWhere.toString(), null, parameters);
		return appReLock;
	}
	/**
	 * 锁定对象
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-1 上午11:21:03
	* Last Modified:
	* @param metaDataId
	* @param metaTableName
	* @param loginUser
	* @version 1.0
	 * @throws Exception 
	 */
	public void LockObject(Long metaDataId,String metaTableName,AppUser loginUser) throws Exception{
		if(metaDataId<1){
			throw new Exception("要锁定的数据编号不正确");
		}
		if(CMyString.isEmpty(metaTableName)){
			throw new Exception("要锁定的对象表名不能为空");
		}
		AppRecordLock appRecordLock = new AppRecordLock();
		appRecordLock.setLockUser(loginUser.getUsername());
		appRecordLock.setRecordId(metaDataId);
		appRecordLock.setTableName(metaTableName);
		//appRecordLock.setLockTime(CMyDateTime.);
		save(appRecordLock);
	}
	/**
	 * 解除锁定
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-1 上午11:24:43
	* Last Modified:
	* @param metaDataId
	* @param metaTableName
	* @param loginUser
	* @version 1.0
	 * @throws Exception 
	 */
	public void ULockObject(Long metaDataId,String metaTableName,AppUser loginUser) throws Exception{
		if(metaDataId<1){
			throw new Exception("要解锁的数据编号不正确");
		}
		if(CMyString.isEmpty(metaTableName)){
			throw new Exception("要解锁的对象表名不能为空");
		}
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(metaTableName);
		parameters.add(metaDataId);
		parameters.add(loginUser.getUsername());
		StringBuffer sWhere = new StringBuffer("tableName=? and recordId=? and lockUser=?");
		List<Object> appReLock = find(null,AppRecordLock.class.getName(), sWhere.toString(), null, parameters);
		if(appReLock.size()>0){
			AppRecordLock appRecordLock = (AppRecordLock)appReLock.get(0);
			delete(appRecordLock);
		}else{
			throw new Exception("对象解除锁定失败!");
		}
	}
	/**
	 * 异常关闭或者刷新页面时，解除锁定对象
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-1 上午11:24:43
	* Last Modified:
	* @param metaDataId
	* @param metaTableName
	* @param loginUser
	* @version 1.0
	 * @throws Exception 
	 */
	public void ULockErrorObject(AppUser loginUser) throws Exception{
		StringBuffer sWhere = new StringBuffer("lockUser=?");
		List<Object> appReLock = find(null,AppRecordLock.class.getName(), sWhere.toString(), null, loginUser.getUsername());
		for(Object apprl: appReLock){
			AppRecordLock appRecordLock = (AppRecordLock)apprl;
			delete(appRecordLock);
		}
	}
	/**
	 * 返回元数据的附件信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-5-6 下午05:00:30
	* Last Modified:
	* @param appId 应用ID
	* @param metadataId 元数据编号
	* @return List<Object> appendixs附件列表
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getAppAppendixs(Long appId,Long metadataId) throws Exception{
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(metadataId);
		parameters.add(appId);
		List<Object> appendixs = find(null, AppAppendix.class.getName(), "metadataId=? and appId=? ", " appendixId desc ",parameters);
		return appendixs;
	}
	/**
	 * Description: 获取用户所在部门，只取一个 <BR>   
	 * @author liu.zhuan
	 * @date 2014-11-15 上午12:32:02
	 * @param userId
	 * @return AppGroup
	 * @throws Exception
	 * @version 1.0
	 */
	public AppGroup getUserGroup(long userId) throws Exception{
		AppGrpuser grpuser = (AppGrpuser)findObject(AppGrpuser.class.getName(), "userId = ?", userId);
		if(grpuser == null) return null;
		return (AppGroup)findById(AppGroup.class, grpuser.getGroupId());
	}
	/**
	 * Description: 判断是否为应用管理员 <BR>   
	 * @author liu.zhuan
	 * @date 2014-11-18 下午03:05:28
	 * @param appId
	 * @param userId
	 * @return 0不是应用管理员，大于0为应用管理员
	 * @throws SQLException
	 * @version 1.0
	 */
	public int isAppAdminUser(long appId, long userId) throws SQLException{
		List<Object> params = new ArrayList<Object>();
		params.add(appId);
		params.add(userId);
		return publicAppBaseDao.executeQueryInt("select count(b.role_sys_user_id) from app_role_sys a,app_role_sys_user b where a.role_id = b.role_id and a.app_id = b.app_id and a.app_id = ? and b.user_id=?", params);
	}
	/**
	 * Description: 查询分组视图字段列表 <BR>   
	 * @author liu.zhuan
	 * @date 2015-5-25 上午11:06:16
	 * @param viewId
	 * @param grpName
	 * @return List<Object> 视图字段列表
	 * @version 1.0
	 */
	public List<Object> queryGrpFields(long viewId, String grpName){
		List<Object> params = new ArrayList<Object>();
		params.add(viewId);
		params.add(grpName);
		StringBuffer hql = new StringBuffer("from ");
		hql.append(AppFieldRel.class.getName())
			.append(" a where a.viewId = ? and (a.fieldId in(select b.fieldId from ")
			.append(AppRelGroup.class.getName())
			.append(" b where b.grpName = ?) or a.fieldStyle = 1)");
		return publicAppBaseDao.find(hql.toString(), params);
	}
	/**
	 *
	* Description: 查询应用的分组字段<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-23 上午11:07:07
	* Last Modified:
	* @param viewId 试图ID
	* @return  List<Object> isGrpFields  分组字段 AppFieldRel 对象
	* @version 1.0
	 */
	public List<Object> isgrp(Long viewId){
		//查询分组字段
		List<Object> isGrpFields = new ArrayList<Object>();
		try {
			isGrpFields = find(null, AppFieldRel.class.getName(), "isGrpField =1 and viewId =? ", "fieldOrder", viewId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isGrpFields;
	}
}
