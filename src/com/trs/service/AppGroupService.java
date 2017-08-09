/**   
m* @Title: AppGroupService.java 
* @Package com.trs.service 
* @Description: TODO 
* @author jin.yu 
* @date 2014-3-10 下午05:04:14 
* @version V1.0   
*/
package com.trs.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.AppGroupDao;
import com.trs.dbhibernate.Page;
import com.trs.model.AppGroup;
import com.trs.model.AppGrpuser;
import com.trs.model.AppUser;
import com.trs.util.CMyString;
import com.trs.util.DBTools;
import com.trs.util.DateUtil;
import com.trs.util.FileUtil;
import com.trs.util.Global;
import com.trs.util.MD5;

/**
 * @Description:<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: AppGroupService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午05:04:14
 * @version 1.0
 */
@Service
public class AppGroupService extends BaseService{
	@Autowired
	private AppGroupDao appGroupDao ;
	@Autowired
	private AppUserService appUserService ;
	@Autowired
	private AppSysConfigService appSysConfigService ;
	/**
	 * 
	* Description:  新增一个组织<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午08:12:01
	* @param appGroup 组织对象
	* @throws Exception
	* @version 1.0
	 */
	public void addGroup(AppGroup appGroup) throws Exception{
		if(!existGroup(appGroup.getGname(), appGroup.getParentId())){
			//取得组织的平级组织的最大排序
			List<Object> maxOrder = find("max(grouporder)", AppGroup.class.getName(), "parentId = ?", "", appGroup.getParentId());
			int groupOrder = 1;
			if(maxOrder.get(0)!=null){
				groupOrder = (Integer)maxOrder.get(0) + 1;
			}
			appGroup.setGrouporder(groupOrder);//设置组织的排序
			appGroup.setContainChild(0);//设置组织没有子组织
			if(appGroup.getIsIndependent()==0){
				appGroup.setIndependGroupId(getIndependGroupID(appGroup.getParentId()));//设置当前组织的独立组织ID
			}
			appGroupDao.save(appGroup);
			if(appGroup.getIsIndependent()==1){
				appGroup.setIndependGroupId(appGroup.getGroupId());
				appGroupDao.update(appGroup);
			}
			//System.out.println(appGroup.getGroupId());
			if(appGroup.getParentId()!= 0){
				AppGroup parentAppGroup = (AppGroup)findById(AppGroup.class, appGroup.getParentId());
				if(parentAppGroup.getContainChild() == null || parentAppGroup.getContainChild()==0){
					parentAppGroup.setContainChild(1);//更改父组织是否有子组织信息
					appGroupDao.update(parentAppGroup);
				}
			}
			
		}else {
			throw new Exception("要新增的组织已存在！");
		}
	}
	
	/**
	 * 
	* Description:  修改一个组织<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午06:31:10
	* @param appGroup 组织对象
	* @throws Exception
	* @version 1.0
	 */
	public void updateGroup(AppGroup appGroup) throws Exception{
		//AppGroup appGroupOld = (AppGroup)findById(AppGroup.class, appGroup.getGroupId());
		//if(appGroupOld.getIsIndependent() == appGroup.getIsIndependent()){
			if(appGroup.getIsIndependent() == 0){//取消独立组织设定
				List<Object> appChildGroupList = find("", AppGroup.class.getName(), "independGroupId = ?", null, appGroup.getGroupId());
				Long independGroupId = getIndependGroupID(appGroup.getParentId());
				for (Object obj : appChildGroupList) {
					AppGroup appChildGroup = (AppGroup)obj;
					appChildGroup.setIndependGroupId(independGroupId);
				}
				appGroup.setIndependGroupId(independGroupId);
				appGroupDao.saveOrUpdateAll(appChildGroupList);
			}else{//设定当前组织为独立组织
				appGroup.setIndependGroupId(appGroup.getGroupId());//设定当前组织的独立自主ID为自己
				ChangeIndependGroup(appGroup.getGroupId(),appGroup.getGroupId());
			}
		//}
		appGroupDao.update(appGroup);
	}
	
	/**
	 * 
	* Description: 当前组织下的子组织取得，以page对象返回 <BR>   
	* @author jin.yu
	* @date 2014-3-17 上午10:55:40
	* @param parentId 父组织ID 
	* @param sOrder 排序条件
	* @param nStartPage 开始页数
	* @param nPageSize 每页数据数
	* @return Page
	* @throws Exception
	* @version 1.0
	 */
	public Page findChildGroup(Long parentId, String sOrder,int nStartPage, int nPageSize) throws Exception{
		List<Object> paramters = new ArrayList<Object>();
		paramters.add(parentId);//设定组织ID为取得条件
		return findPage("",AppGroup.class.getName(),"parentId=?",sOrder,nStartPage,nPageSize,paramters);
	}
	
	/**
	 * 
	* Description:  取得当前组织的子组织对象，以list返回信息<BR>   
	* @author jin.yu
	* @date 2014-3-20 下午09:00:37
	* @param parentId 父组织ID
	* @param sOrder 排序条件
	* @return List list中是组织的对象集合
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> findChildGroup(Long parentId, String sOrder) throws Exception{
		return find("", AppGroup.class.getName(), "parentId=?",sOrder, parentId);
	}
	
	/**
	 * 
	* Description:  判断组织是否独立组织<BR>
	* @author jin.yu
	* @date 2014-3-17 上午11:20:07
	* @param groupId 组织ID
	* @return
	* @version 1.0
	 */
	public boolean isIndependent(Long groupId){
		AppGroup appGroup = (AppGroup)findById(AppGroup.class, groupId);
		if(appGroup.getIsIndependent()==Global.GROUP_IS_INDEPENDENT){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	* Description:  通过组织ID批量删除组织对象<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午01:36:30
	* @param groupIds 组织ID，可传入多个
	* @version 1.0
	 * @throws Exception 
	 */
	public boolean deleteGroup(String groupIds,Long parentId) throws Exception{
		//取得所有要删除的组织对象
		List<Object> delGroupList = findByIds(AppGroup.class.getName(), "groupId", groupIds);
		//所有要删除的组织ID
		List<Object> delGroupIdList = new ArrayList<Object>();
		
		//遍历组织集合判断是否含有子组织
		for (Object obj : delGroupList) {
			AppGroup appDelGroup = (AppGroup)obj;
			if(appDelGroup.getContainChild()==0){
				delGroupIdList.add(appDelGroup.getGroupId());
			}else{
				return false;
			}
		}
		groupIds = CMyString.join((ArrayList<Object>)delGroupIdList, ",");
		if(groupIds != null & !"".equals(groupIds)){
			//删除对应的组织
			deleteAll(AppGroup.class.getName(), "groupId", groupIds);
			//删除组织中对应的用户
			deleteAll(AppGrpuser.class.getName(), "groupId", groupIds);
			
			//重新设置该父栏目下所有子栏目的顺序
			List<Object> groupList = find("", AppGroup.class.getName(), "parentId = ?"," grouporder asc ", parentId);
			for (int i=1;i <= groupList.size();i++) {
				AppGroup appGroup = (AppGroup)groupList.get(i-1);
				appGroup.setGrouporder(i);
			}
			if(groupList.size()==0){
				if(parentId != 0){
					AppGroup parentAppGroup = (AppGroup)findById(AppGroup.class, parentId);
					parentAppGroup.setContainChild(0);
					appGroupDao.update(parentAppGroup);
				}
			}
			appGroupDao.saveOrUpdateAll(groupList);
		}
		return true;
	}
	
	/**
	 * 
	* Description:  组织用户信息列表取得<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午10:22:22
	* @param groupId 组织ID
	* @param sOrder 排序条件
	* @param nStartPage 当前页数
	* @param nPageSize 每页条数
	* @return Page 通过Page.getLdata()取得数据集合
	* @throws Exception
	* @version 1.0
	 */
	public Page findGroupUser(Long groupId, int nStartPage, int nPageSize) throws Exception{	
		List<Object> paramters = new ArrayList<Object>();
		paramters.add(groupId);
		int totalResults = count(AppGrpuser.class.getName(), "groupId=?", paramters);
		Page page = new Page(nStartPage, nPageSize, totalResults);
		List<Object> listGrpUser  = find("new map(g.grpuserId as grpuserId,u.username as username,u.truename as truename,u.status as status,g.isAdmin as isAdmin,g.cruser as cruser,g.crtime as crtime)","AppGrpuser g,AppUser u","g.groupId=? and g.userId=u.userId","",page.getStartIndex(),nPageSize,paramters);
		page.setLdata(listGrpUser);
		return page;
	}
	
	/**
	 * 
	* Description: 通过组织ID取得组织下所有的关联用户，最后以逗号隔开的用户ID字符串返回 <BR>   
	* @author jin.yu
	* @date 2014-3-25 下午05:34:18
	* @param groupId 组织ID
	* @return String 逗号隔开的用户ID字符串
	* @throws Exception
	* @version 1.0
	 */
	public String findGroupUser(Long groupId) throws Exception{
		String roleUser = "";
		List<Object> listRoleUser  = find("u.userId","AppGrpuser g,AppUser u","g.groupId = ? and g.userId=u.userId", "", groupId);
		roleUser = CMyString.join((ArrayList<Object>)listRoleUser, ",");
		return roleUser;
	}
	
	/**
	 * 
	* Description: 弹出框中使用组织用户取得 <BR>   
	* @author jin.yu
	* @date 2014-3-22 下午07:44:30
	* @param groupId 组织ID
	* @param sWhere 查询条件字段名称
	* @param param 查询条件字段值
	* @param nStartPage 当前页
	* @param nPageSize 每页条数
	* @return Page
	* @throws Exception
	* @version 1.0
	 */
	public Page findGrpUserSel(Long groupId, String sWhere, Object param, int nStartPage, int nPageSize) throws Exception {
		if (groupId==null || groupId==0) {
			String selWhere = "status=? and isDeleted=?";//查询条件
			List<Object> paramters = new ArrayList<Object>();//查询条件值
			paramters.add(Global.USER_STATUS_REG);
			paramters.add(Global.USER_IS_NOT_DELETED);
			if (sWhere != null && !"".equals(sWhere)) {
				selWhere = selWhere + " and " + sWhere +" like ?";
				paramters.add("%"+param+"%");
			}
			int totalResults = count(AppUser.class.getName(), selWhere, paramters);
			Page page = new Page(nStartPage, nPageSize, totalResults);
			List<Object> listGrpUser  = find("new map(userId as userId,username as username,truename as truename,status as status,cruser as cruser,crtime as crtime)","AppUser",selWhere,"",page.getStartIndex(),nPageSize,paramters);
			page.setLdata(listGrpUser);
			return page;
		}else{
			String dataWhere = "g.groupId=? and g.userId=u.userId";//查询数据条件值
			List<Object> paramters = new ArrayList<Object>();
			paramters.add(groupId);
			if (sWhere != null && !"".equals(sWhere)) {
				dataWhere = dataWhere + " and u." + sWhere + " like ?";
				paramters.add("%"+param+"%");
			}
			int totalResults = count("AppGrpuser g,AppUser u", dataWhere, paramters);
			Page page = new Page(nStartPage, nPageSize, totalResults);
			List<Object> listGrpUser  = find("new map(u.userId as userId,u.username as username,u.truename as truename,u.status as status,u.cruser as cruser,u.crtime as crtime)","AppGrpuser g,AppUser u",dataWhere,"",page.getStartIndex(),nPageSize,paramters);
			page.setLdata(listGrpUser);
			return page;
		}
	}
	
	/**
	 * 
	* Description:  组织添加用户，适用于在组织中直接创建新用户<BR>   
	* @author jin.yu
	* @date 2014-3-16 下午08:02:26
	* @param appUser 用户对象
	* @param groupId 组织ID
	* @param loginUser 当前操作用户对象
	* @version 1.0
	 * @throws Exception 
	 */
	public void addGroupUser(Long groupId,AppUser appUser,AppUser loginUser) throws Exception{
		appUserService.addUser(appUser);
		appUser = (AppUser)findObject(AppUser.class.getName(), "username=?", appUser.getUsername());
		AppGrpuser appGrpuser= new AppGrpuser();
		appGrpuser.setGroupId(groupId);
		appGrpuser.setUserId(appUser.getUserId());
		appGrpuser.setIsAdmin(Global.GROUPUSER_IS_NOT_ADMIN);//设定为组织普通用户角色
		appGrpuser.setCruser(loginUser.getUsername());
		appUserService.save(appGrpuser);
	}
	
	/**
	 * 
	* Description: 给组织添加用户，用户从用户管理模块中取得<BR>   
	* @author jin.yu
	* @date 2014-3-16 下午08:07:39
	* @param groupId 组织id
	* @param userIds 用户ID集合 
	* @param loginUser 当前操作用户对象
	* @version 1.0
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void addGroupUser(Long groupId,String userIds,AppUser loginUser) throws NumberFormatException, Exception{
		String[] arrUserIds = userIds.split(",");
		List<Object> addUserList = new ArrayList<Object>();
		List<Object> delUserList = new ArrayList<Object>();
		List<Object> oldUserIdList= find("",AppGrpuser.class.getName(), "groupId=?","", groupId);
		List<String> newUserIdList = Arrays.asList(arrUserIds);
		boolean existFlag = false;
		if (userIds == null || userIds.equals("")) {
			appGroupDao.deleteAll(oldUserIdList);
		}else{
			//判断用户是否新增
			for (String userId : newUserIdList) {
				existFlag = false;
				for (Object obj : oldUserIdList) {
					AppGrpuser appGrpuser = (AppGrpuser)obj;
					if (appGrpuser.getUserId()==Long.parseLong(userId)) {
						existFlag = true;
						break;
					}
				}
				if(!existFlag){
					addUserList.add(new AppGrpuser(null,groupId,Long.parseLong(userId),Global.GROUPUSER_IS_NOT_ADMIN,"",loginUser.getUsername(),DateUtil.now()));
				}
			}
			
			//判断用户是否删除
			for (Object obj : oldUserIdList) {
				AppGrpuser appGrpuser = (AppGrpuser)obj;
				if (!newUserIdList.contains(appGrpuser.getUserId().toString())) {
					delUserList.add(appGrpuser);
				}
			}
			//新增组织用户
			appGroupDao.saveOrUpdateAll(addUserList);
			//删除组织用户
			appGroupDao.deleteAll(delUserList);
		}
	}
	
	/**
	 * 
	* Description: 根据组织跟用户的关联ID删除组织中关联的用户信息 <BR>   
	* @author jin.yu
	* @date 2014-3-17 下午04:33:34
	* @param grpuserIds 组织与用户的关联关系Id
	* @version 1.0
	 * @throws Exception 
	 */
	public void delGroupUser(String grpuserIds) throws Exception{
		//删除组织中对应的用户
		deleteAll(AppGrpuser.class.getName(), "grpuserId", grpuserIds);
	}
	
	/**
	 * 
	* Description: 根据传入的组织Id和用户Id删除组织中的用户关联 <BR>   
	* @author jin.yu
	* @date 2014-3-22 下午05:02:18
	* @param groupId 要删除用户的组织ID
	* @param userIds 要删除的用户Id
	* @throws Exception
	* @version 1.0
	 */
	public void delGroupUser(Long groupId,String userIds) throws Exception {
		String[] arrUserIds = userIds.split(",");
		List<Object> delUserList = new ArrayList<Object>();
		for (String userId : arrUserIds) {
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(groupId);
			paramList.add(userId);
			//查询要删除的组织用户关联对象
			AppGrpuser appGrpuser = (AppGrpuser)findObject(AppGrpuser.class.getName(), "groupId=? and userId=?", paramList);
			delUserList.add(appGrpuser);
		}
		appGroupDao.deleteAll(delUserList);
	}
	
	/**
	 * 
	* Description:  设定组织下用户为组织管理员<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午06:13:02
	* @param grpuserIds 组织与用户的关联关系Id
	* @version 1.0
	 * @throws Exception 
	 */
	public void setGrpUserAdmin(String grpuserIds) throws Exception{
		List<Object> dataList = findByIds(AppGrpuser.class.getName(), "grpuserId", grpuserIds);
		for (Object object : dataList) {
			AppGrpuser appGrpuser = (AppGrpuser)object;
			appGrpuser.setIsAdmin(Global.GROUPUSER_IS_ADMIN);
		}
		appGroupDao.saveOrUpdateAll(dataList);
	}
	
	/**
	 * 
	* Description:  取消组织下用户组织管理员角色<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午06:18:23
	* @param grpuserIds 用户ID集合
	* @version 1.0
	 * @throws Exception 
	 */
	public void cancelGrpUserAdmin(String grpuserIds) throws Exception{
		List<Object> dataList = findByIds(AppGrpuser.class.getName(), "grpuserId", grpuserIds);
		for (Object object : dataList) {
			AppGrpuser appGrpuser = (AppGrpuser)object;
			appGrpuser.setIsAdmin(Global.GROUPUSER_IS_NOT_ADMIN);
		}
		appGroupDao.saveOrUpdateAll(dataList);
	}
	
	/**
	 * 
	* Description:  判断用户在组织下是否组管理员用户<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午06:28:01
	* @param UserId 用户ID
	* @param GroupId 组织ID
	* @return boolean
	* @version 1.0
	 * @throws Exception 
	 */
	public boolean userIsGrpAdmin(Long UserId,Long GroupId) throws Exception{
		List<Object> param = new ArrayList<Object>();
		param.add(GroupId);		
		param.add(UserId);
		AppGrpuser appGrpuser = (AppGrpuser)findObject(AppGrpuser.class.getName(), "groupId=? and userId=?", param);
		if(appGrpuser!=null){
			if(appGrpuser.getIsAdmin()==Global.GROUPUSER_IS_ADMIN){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	* Description:  判断同目录下组织名称是否已存在<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午09:22:36
	* @param gname 组织名称
	* @param parentId
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public boolean existGroup(String gname,Long parentId) throws Exception{
	   	List<Object> param = new ArrayList<Object>();
    	param.add(gname);
    	param.add(parentId);
		return existData(AppGroup.class.getName(), "gname=? and parentId=?", param);
	}
	
	/**
	 * 
	* Description:  调整组织顺序<BR>   
	* @author jin.yu
	* @date 2014-3-19 上午09:51:59
	* @param groupId 组织ID
	* @param orderByType 顺序调整方式：1为上移，2为下移
	* @throws Exception
	* @version 1.0
	 */
	public void orderByGroup(Long groupId,int orderByType) throws Exception{
		AppGroup appGroup = (AppGroup)findById(AppGroup.class, groupId);
		int groupOrder = appGroup.getGrouporder();
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(appGroup.getParentId());
		if(orderByType==1 && groupOrder != 1){//1代表组织上移
			groupOrder = groupOrder - 1;
			paramList.add(groupOrder);
			AppGroup appGroupUp = (AppGroup)findObject(AppGroup.class.getName(), "parentId = ? and grouporder = ?", paramList);
			appGroupUp.setGrouporder(appGroupUp.getGrouporder() + 1);
			appGroup.setGrouporder(groupOrder);
			update(appGroup);			
			update(appGroupUp);
		}else if(orderByType==2){//2代表子组织下移
			groupOrder = groupOrder + 1;
			paramList.add(groupOrder);
			System.out.println(groupOrder);
			AppGroup appGroupUp = (AppGroup)findObject(AppGroup.class.getName(), "parentId = ? and grouporder = ?", paramList);
			if(appGroupUp!=null){//判断当前组织是否为最后一个
				appGroupUp.setGrouporder(appGroupUp.getGrouporder() - 1);
				appGroup.setGrouporder(groupOrder);
				update(appGroup);
				update(appGroupUp);
			}
		}
	}
	
	/**
	 * 
	* Description:  取得组织下所有的独立组织对象，以list形式返回<BR>   
	* @author jin.yu
	* @date 2014-3-31 下午09:32:00
	* @return List list中为一个或多个组织对象
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getIndependentGroup() throws Exception{
		List<Object> appGroupList = find("", AppGroup.class.getName(), "isIndependent = ?", "", Global.GROUP_IS_INDEPENDENT);
		return appGroupList;
	}
	
	/**
	 * 
	* Description:  通过TXT文件导入组织结构<BR>   
	* @author jin.yu
	* @date 2014-3-22 上午10:50:31
	* @param groupId 要导入的父栏目ID
	* @param file 要导入的TXT文件
	* @param loginUser 当前操作用户对象
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public void importGroup(Long groupId,File file,AppUser loginUser) throws Exception{
		List<Object> groupNameList = getGroupList(FileUtil.readTxtFile(file));
		Long parentGroupId = groupId;
		for (Object obj : groupNameList) {
			//System.out.println(((List<String>)obj).get(2));
			String sGroupName = (String)((List<String>)obj).get(2);
			String sGroupPath= (String)((List<String>)obj).get(1);
			int currentNum = (Integer)((List<Object>)obj).get(0);
			if(currentNum==0){
				parentGroupId = groupId;
			}else{
				String parameters = (sGroupPath).replace("?", "");
				String[] arrUserIds = parameters.split(",");
				parameters = arrUserIds[0];
				for (int i = 1; i < currentNum; i++) {
					parameters = parameters +","+arrUserIds[i];
				}
				AppGroup parentAppGroup = (AppGroup)findObject(AppGroup.class.getName(), "attribute = ?", parameters);
				parentGroupId = parentAppGroup.getGroupId();
			}
			AppGroup appGroup = new AppGroup();
			appGroup.setGname((sGroupName).replace("?", ""));
			appGroup.setParentId(parentGroupId);
			appGroup.setAttribute((sGroupPath).replace("?", ""));
			appGroup.setIsIndependent(Global.GROUP_ISNOT_INDEPENDENT);
			appGroup.setCruser(loginUser.getUsername());
			addGroup(appGroup);
		}
	}
	
	/**
	 * 
	* Description: 判断String字符串中有多少个tab，返回int值 <BR>   
	* @author jin.yu
	* @date 2014-3-22 上午10:42:17
	* @param str String类型字符串
	* @return int
	* @version 1.0
	 */
	public int getTabNumber(String str){
		return (str.split("\t")).length-1;
	}
	
	/**
	 * 
	* Description:  导入组织时使用，通过传入的组织list判断各个组织的层级<BR>   
	* @author jin.yu
	* @date 2014-3-22 上午10:44:57
	* @param groupNameList 组织名称list
	* @return list
	* @version 1.0
	 */
	public List<Object> getGroupList(List<Object> groupNameList) {
		List<Object> returnList = new ArrayList<Object>();
		String currentContent = "";
		String superiorContent = "";
		int currentNum = 0;
		int superiorNum = 0;
		for (Object object : groupNameList) {
			List<Object> list = new ArrayList<Object>();
			currentContent = (String)object;
			superiorNum = getTabNumber(currentContent);
			currentContent = currentContent.replaceAll("\\t", "");
			if(superiorNum == 0){
				Date sDate = new Date();
				superiorContent = DateUtil.getDateTime(sDate) + currentContent;
			}
			if(superiorNum > currentNum){
				superiorContent = superiorContent + "," + currentContent;
			}else if(superiorNum < currentNum && superiorNum != 0){
				String[] arrUserIds = superiorContent.split(",");
				superiorContent = arrUserIds[0];
				for (int i = 1; i < superiorNum; i++) {
					superiorContent = superiorContent +","+arrUserIds[i];
				}
				superiorContent = superiorContent + "," + currentContent;
			}else if(superiorNum > 0){
				String[] arrUserIds = superiorContent.split(",");
				superiorContent = arrUserIds[0];
				for (int i = 1; i < superiorNum; i++) {
					superiorContent = superiorContent +","+arrUserIds[i];
				}
				superiorContent = superiorContent + "," + currentContent;
			}
			list.add(superiorNum);
			list.add(superiorContent);
			list.add(currentContent);
			returnList.add(list);
			currentNum = superiorNum;
		}
		return returnList;
	}
	
	/**
	 * 
	* Description: 从WCM中抽取组织结构 到互动平台中<BR>   
	* @author jin.yu
	* @date 2014-4-16 上午11:38:31
	* @param loginUser 登录用户对象
	* @throws Exception
	* @version 1.0
	 */
	public void importWcmGroup(AppUser loginUser) throws Exception{
		String wcmDBInfo = appSysConfigService.findSysConfigCon("WCM_DB_INFO");
		String[] wcmDBinfo = wcmDBInfo.split(";");
		Map<String, Object> sysConfigMap = new HashMap<String, Object>();
		for (String configValue : wcmDBinfo) {
			String[] configValues = configValue.split("=");
			sysConfigMap.put(configValues[0], configValues[1]);
		}
		String wcmDBType = (String)sysConfigMap.get("TYPE");
		String wcmDBIp = (String)sysConfigMap.get("IP");
		String wcmDBSid = (String)sysConfigMap.get("SID");
		String wcmDBUserName = (String)sysConfigMap.get("USERNAME");
		String wcmDBPassWord = (String)sysConfigMap.get("PASSWORD");
		DBTools dbTools = new DBTools(wcmDBType, wcmDBIp,wcmDBSid,wcmDBUserName,wcmDBPassWord);
		String sql = "select t.groupid,t.gname,t.gdesc,t.parentid,t.grouporder from wcmgroup t order by t.groupid";
        List<String[]> datalist = dbTools.getDataset(sql, 4);
        
        for(int i=0; i < datalist.size(); i++){
            String[] datas = (String[]) datalist.get(i);
            AppGroup appGroup = new AppGroup();
            if(datas[3].equals("0")){
            	appGroup.setParentId(Long.valueOf(datas[3]));
        	}else{
        		AppGroup parentAppGroup = (AppGroup)findObject(AppGroup.class.getName(), "attribute = ?", datas[3]);
				if(parentAppGroup != null){
					appGroup.setParentId(parentAppGroup.getGroupId());
				}else{
					appGroup.setParentId(new Long(0));
				}
        	}
            appGroup.setGname(datas[1]);
            appGroup.setGdesc(datas[2]);
            appGroup.setAttribute(datas[0]);
            appGroup.setIsIndependent(Global.GROUP_ISNOT_INDEPENDENT);
            appGroup.setCruser(loginUser.getUsername());
            addGroup(appGroup);
        }
	}
	
	/**
	 * 
	* Description: 当前组织的上级独立 <BR>   
	* @author jin.yu
	* @date 2014-4-24 下午02:00:18
	* @param groupId 父组织ID
	* @return Long 独立组织ID
	* @version 1.0
	 */
	public Long getIndependGroupID(Long groupId){
		Long IndependGroupID = new Long(0);
		AppGroup appGroup = (AppGroup)findById(AppGroup.class, groupId);
		if (appGroup != null) {
			if(appGroup.getIsIndependent()==1){
				IndependGroupID = appGroup.getGroupId();
			}else{
				IndependGroupID = getIndependGroupID(appGroup.getParentId());
			}
		}
		return IndependGroupID;
	}
	
	/**
	 * 
	* Description: 修改当前组织下所有组织的独立组织ID <BR>   
	* @author jin.yu
	* @date 2014-5-5 上午10:01:43
	* @param groupId 当前组织ID
	* @param IndependGroupId 独立组织ID
	* @throws Exception
	* @version 1.0
	 */
	public void ChangeIndependGroup(Long groupId, Long IndependGroupId) throws Exception{
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(groupId);
		parameters.add(0);
		List<Object> groupList = find("", AppGroup.class.getName(), "parentId = ? and isIndependent = ?","", parameters);
		for (Object obj : groupList) {
			AppGroup appGroup = (AppGroup)obj;
			appGroup.setIndependGroupId(IndependGroupId);
			if(appGroup.getContainChild()==1 && appGroup.getIsIndependent()==0){
				ChangeIndependGroup(appGroup.getGroupId(),IndependGroupId);
			}
		}
		appGroupDao.saveOrUpdateAll(groupList);
	}
}
