/**   
* Description: TODO 
* Title: AppRoleService.java 
* @Package com.trs.service 
* @author jin.yu 
* @date 2014-3-18 下午01:56:52 
* @version V1.0   
*/
package com.trs.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.AppRoleDao;
import com.trs.dbhibernate.Page;
import com.trs.model.AppInfo;
import com.trs.model.AppRole;
import com.trs.model.AppRoleOper;
import com.trs.model.AppRoleSys;
import com.trs.model.AppRoleSysUser;
import com.trs.model.AppRoleUser;
import com.trs.model.AppUser;
import com.trs.util.CMyString;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: AppRoleService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-18 下午01:56:52
 * @version 1.0
 */
@Service
public class AppRoleService extends BaseService{
	@Autowired
	private AppRoleDao appRoleDao;
	
	/**
	 * 
	* Description:  新增角色<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午02:03:11
	* @param appRole 角色对象
	* @throws Exception
	* @version 1.0
	 */
	public void addRole(AppRole appRole) throws Exception{
		if(!existRole(appRole.getRolename())){
			save(appRole);
		}
	}
	
	/**
	 * 
	* Description:  修改角色<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午07:42:19
	* @param appRole 角色对象
	* @throws Exception
	* @version 1.0
	 */
	public void updateRole(AppRole appRole) throws Exception{
		update(appRole);
	}
	
	/**
	 * 
	* Description:  删除角色<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午02:15:08
	* @param roleId 角色ID
	* @version 1.0
	 * @throws Exception 
	 */
	public void deleteRole(String roleId) throws Exception{
		//删除角色信息
		deleteAll(AppRole.class.getName(), "roleId", roleId);
		//删除角色用户信息
		deleteAll(AppRoleUser.class.getName(), "roleId", roleId);
	}
	
	/**
	 * 
	* Description: 判断角色是否已经存在 <BR>   
	* @author jin.yu
	* @date 2014-3-18 下午02:03:31
	* @param roleName 角色名称
	* @return boolean
	* @throws Exception
	* @version 1.0
	 */
	public boolean existRole(String roleName) throws Exception{
		return existData(AppRole.class.getName(), "rolename=?", roleName);
	}
	
	/**
	 * 
	* Description:  查询角色中的用户信息,返回Page对象<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午02:22:08
	* @param roleId 角色ID
	* @param sOrder 排序
	* @param nStartPage 当前页数
	* @param nPageSize 每页条数
	* @return Page
	* @throws Exception
	* @version 1.0
	 */
	public Page findRoleUser(Long roleId, String sOrder,int nStartPage, int nPageSize) throws Exception{	
		List<Object> paramters = new ArrayList<Object>();
		paramters.add(roleId);
		int totalResults = count(AppRoleUser.class.getName(), "roleId=?", paramters);
		Page page = new Page(nStartPage, nPageSize, totalResults);
		List<Object> listRoleUser  = find("new map(g.roleuserId as roleuserId,u.username as username,u.truename as truename,u.status as status,u.userId as userId)","AppRoleUser g,AppUser u","g.roleId=? and g.userId=u.userId",sOrder,page.getStartIndex(),nPageSize,paramters);
		page.setLdata(listRoleUser);
		return page;
	}
	
	/**
	 * 
	* Description: 查询角色中的用户信息，返回用户ID字符串，以逗号隔开 <BR>   
	* @author jin.yu
	* @date 2014-3-24 下午04:12:02
	* @param roleId 角色ID
	* @return String 以字符串隔开的用户ID字符串
	* @throws Exception
	* @version 1.0
	 */
	public String findRoleUser(Long roleId) throws Exception{
		String roleUser = "";
		List<Object> listRoleUser  = find("u.userId","AppRoleUser g,AppUser u","g.roleId=? and g.userId=u.userId", "", roleId);
		roleUser = CMyString.join((ArrayList<Object>)listRoleUser, ",");
		return roleUser;
	}
	/**
	 * 
	* Description:  删除角色中对应的用户<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午02:32:53
	* @param roleUserIds 用户和角色的关联表ID
	* @throws Exception
	* @version 1.0
	 */
	public void delRoleUser(String roleUserIds) throws Exception{
		//删除角色中对应的用户
		deleteAll("AppRoleUser", "roleuserId", roleUserIds);
	}
	
	/**
	 * 
	* Description:  角色添加用户<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午05:53:03
	* @param roleId 角色ID
	* @param userIds 用户ID集合
	* @param loginUser 当前操作用户对象
	* @throws NumberFormatException
	* @throws Exception
	* @version 1.0
	 */
	public void addRoleUser(Long roleId,String userIds,AppUser loginUser) throws NumberFormatException, Exception{
		String[] arrUserIds = userIds.split(",");
		List<Object> addUserList = new ArrayList<Object>();
		List<Object> delUserList = new ArrayList<Object>();
		List<Object> oldUserIdList= find("",AppRoleUser.class.getName(), "roleId=?","", roleId);
		List<String> newUserIdList = Arrays.asList(arrUserIds);
		boolean existFlag = false;
		if(userIds == null || userIds.equals("")){
			//删除角色用户
			appRoleDao.deleteAll(oldUserIdList);
		}else{
			//判断用户是否新增
			for (String userId : newUserIdList) {
				existFlag = false;
				for (Object obj : oldUserIdList) {
					AppRoleUser appRoleuser = (AppRoleUser)obj;
					if (appRoleuser.getUserId()==Long.parseLong(userId)) {
						existFlag = true;
						break;
					}
				}
				if(!existFlag){
					addUserList.add(new AppRoleUser(null,roleId,Long.parseLong(userId),loginUser.getUsername(),null));
				}
			}
			
			//判断用户是否删除
			for (Object obj : oldUserIdList) {
				AppRoleUser appRoleuser = (AppRoleUser)obj;
				if (!newUserIdList.contains(appRoleuser.getUserId().toString())) {
					delUserList.add(appRoleuser);
				}
			}
			//新增角色用户
			appRoleDao.saveOrUpdateAll(addUserList);
			//删除角色用户
			appRoleDao.deleteAll(delUserList);
		}
	}
	
	/**
	 * 
	* Description:  角色添加关联应用<BR>   
	* @author jin.yu
	* @date 2014-3-19 下午02:33:58
	* @param roleId 角色ID
	* @param appIds 应用ID集合
	* @param loginUser 当前操作用户对象
	* @throws Exception
	* @version 1.0
	 */
	public void addRoleApp(Long roleId,String appIds,AppUser loginUser) throws Exception {
		String[] arrAppIds = appIds.split(",");
		List<Object> addAppList = new ArrayList<Object>();
		List<Object> delAppList = new ArrayList<Object>();
		List<Object> oldAppIdList= find("",AppRoleSys.class.getName(), "roleId=?","", roleId);
		List<String> newAppIdList = Arrays.asList(arrAppIds);
		boolean existFlag = false;
		if(appIds == null || appIds.equals("")){
			appRoleDao.deleteAll(oldAppIdList);
		}else{
			//判断应用是否新增
			for (String appId : newAppIdList) {
				existFlag = false;
				for (Object obj : oldAppIdList) {
					AppRoleSys appRoleSys = (AppRoleSys)obj;
					if (appRoleSys.getRoleId()==Long.parseLong(appId)) {
						existFlag = true;
						break;
					}
				}
				if(!existFlag){
					addAppList.add(new AppRoleSys(null,roleId,Long.parseLong(appId),loginUser.getUsername(),null));
				}
			}
			
			//判断应用是否删除
			for (Object obj : oldAppIdList) {
				AppRoleSys appRoleSys = (AppRoleSys)obj;
				if (!newAppIdList.contains(appRoleSys.getRoleId().toString())) {
					delAppList.add(appRoleSys);
				}
			}
			//新增角色应用
			appRoleDao.saveOrUpdateAll(addAppList);
			//删除角色应用
			appRoleDao.deleteAll(delAppList);
		}
	}
	
	/**
	 * 
	* Description:  通过角色ID取得跟该角色相关的应用ID集合<BR>   
	* @author jin.yu
	* @date 2014-3-19 下午01:31:36
	* @param roleId 角色ID
	* @return String 以逗号隔开的应用ID字符串
	* @throws Exception
	* @version 1.0
	 */
	public String getRoleApp(Long roleId) throws Exception {
		List<Object> appIdList= find("appId",AppRoleSys.class.getName(), "roleId = ?","", roleId);
		return CMyString.join((ArrayList<Object>)appIdList, ",");
	}
	
	/**
	 * 
	* Description: 通过角色ID取得跟该角色相关的应用信息<BR>   
	* @author jin.yu
	* @date 2014-4-1 下午02:16:39
	* @param roleId 单个角色ID
	* @return Map 以map形式返回，map中包含应用的ID和应用的名称
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getRoleAppMap(Long roleId) throws Exception{
		//取得跟角色相关的应用信息
		List<Object> roleAppMap = find("new map(i.appId as appId,i.appName as appName)", "AppRoleSys s, AppInfo i", "s.roleId = ? and s.appId=i.appId", "", roleId);
		return roleAppMap;
	}
	
	/**
	 * 
	* Description: 通过角色ID和应用ID取得相关的操作ID集合 <BR>   
	* @author jin.yu
	* @date 2014-3-19 下午02:37:28
	* @param roleId 角色ID
	* @param appId 应用ID
	* @return Stirng 以逗号隔开的操作ID字符串
	* @throws Exception
	* @version 1.0
	 */
	public String getRoleAppOper(Long roleId,Long appId) throws Exception{
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(roleId);
		paramList.add(appId);
		List<Object> roleAppOperList = find("operId", AppRoleOper.class.getName(), "roleId = ? and appId = ?", "", paramList);
		return CMyString.join((ArrayList<Object>)roleAppOperList, ",");
	}
	
	/**
	 * 
	* Description:  给角色关联的应用添加操作权限<BR>   
	* @author jin.yu
	* @date 2014-3-19 下午04:24:01
	* @param roleId 角色ID
	* @param appId 应用ID
	* @param operIds 操作ID集合
	* @param loginUser 当前操作用户对象
	* @throws Exception
	* @version 1.0
	 */
	public void addRoleAppOper(Long roleId,Long appId,String operIds,AppUser loginUser) throws Exception {
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(roleId);
		paramList.add(appId);
		String[] arrOperIds = operIds.split(",");
		List<Object> addOperList = new ArrayList<Object>();
		List<Object> delOperList = new ArrayList<Object>();
		List<Object> oldOperIdList = find("",AppRoleOper.class.getName(), "roleId = ? and appId = ?","", paramList);
		List<String> newOperIdList = Arrays.asList(arrOperIds);
		boolean existFlag = false;
		if(operIds == null || operIds.equals("")){
			//删除角色应用操作
			appRoleDao.deleteAll(oldOperIdList);
		}else{
			//判断操作是否新增
			for (String operId : newOperIdList) {
				existFlag = false;
				for (Object obj : oldOperIdList) {
					AppRoleOper appRoleOper = (AppRoleOper)obj;
					if (appRoleOper.getOperId()==Long.parseLong(operId)) {
						existFlag = true;
						break;
					}
				}
				if(!existFlag){
					addOperList.add(new AppRoleOper(null,roleId,Long.parseLong(operId),appId,loginUser.getUsername(),null));
				}
			}
			
			//判断操作是否删除
			for (Object obj : oldOperIdList) {
				AppRoleOper appRoleOper = (AppRoleOper)obj;
				if (!newOperIdList.contains(appRoleOper.getOperId().toString())) {
					delOperList.add(appRoleOper);
				}
			}
			//新增角色应用操作
			appRoleDao.saveOrUpdateAll(addOperList);
			//删除角色应用操作
			appRoleDao.deleteAll(delOperList);
		}
	}
	/**
	 * Description:  设置角色下应用管理员<BR>   
	 * @author liu.zhuan
	 * @date 2014-8-20 下午05:32:19
	 * @param roleUserIds 角色用户对象ID，以","号隔开的字符串。
	 * @throws Exception
	 * @version 1.0
	 */
	public void setUserToAppAdmin(String roleUserIds, String flag) throws Exception{
		List<Object> roleUsers = (List<Object>)findByIds(AppRoleUser.class.getName(), "roleuserId", roleUserIds);
		if(roleUsers != null && roleUsers.size() > 0){
			AppRoleUser roleUser = null;
			for (int i = 0; i < roleUsers.size(); i++) {
				roleUser = (AppRoleUser)roleUsers.get(i);
				roleUser.setIsAppAdmin(Integer.parseInt(flag));
			}
			appRoleDao.saveOrUpdateAll(roleUsers);
		}
	}
	/**
	 * Description: 获取角色下管理员用户的所有应用编号 <BR>   
	 * @author liu.zhuan
	 * @date 2014-12-16 下午06:01:31
	 * @param roleId 角色编号
	 * @param userId 用户编号
	 * @return 应用编号
	 * @throws Exception
	 * @version 1.0
	 */
	public String getRoleSysUser(Long roleId, Long userId) throws Exception {
		List<Object> param = new ArrayList<Object>();
		param.add(roleId);
		param.add(userId);
		List<Object> appIdList= find("appId",AppRoleSysUser.class.getName(), "roleId = ? and userId = ?","", param);
		return CMyString.join((ArrayList<Object>)appIdList, ",");
	}
	/**
	 * Description:  绑定用户为应用管理员<BR>   
	 * @author liu.zhuan
	 * @date 2014-8-20 下午05:32:19
	 * @param roleUserIds 角色用户对象ID，以","号隔开的字符串。
	 * @throws Exception
	 * @version 1.0
	 */
	public void setUserToAppAdmin(String roleAppIds, long userId, long roleId) throws Exception{
		List<Object> param = new ArrayList<Object>();
		param.add(roleId);
		param.add(userId);
		List<Object> oldRoleSysUserList= find("",AppRoleSysUser.class.getName(), "roleId = ? and userId = ?","", param);
		if(CMyString.isEmpty(roleAppIds)){
			appRoleDao.deleteAll(oldRoleSysUserList);
		}else{
			boolean existFlag = false;
			List<Object> addList = new ArrayList<Object>();
			List<Object> delList = new ArrayList<Object>();
			List<String> roleAppIdList = Arrays.asList(roleAppIds.split(","));
			AppRoleSysUser rsu = null;
			for (String roleAppId : roleAppIdList) {
				existFlag = false;
				for (Object object : oldRoleSysUserList) {
					rsu = (AppRoleSysUser) object;
					if(rsu.getAppId().toString().equals(roleAppId)){
						existFlag = true;
						break;
					}
				}
				if(!existFlag){
					rsu = new AppRoleSysUser();
					rsu.setAppId(Long.valueOf(roleAppId));
					rsu.setRoleId(roleId);
					rsu.setUserId(userId);
					addList.add(rsu);
				}
			}
			for (Object object : oldRoleSysUserList) {
				rsu = (AppRoleSysUser) object;
				if(!roleAppIdList.contains(rsu.getAppId().toString())){
					delList.add(rsu);
				}
			}
			appRoleDao.saveOrUpdateAll(addList);
			appRoleDao.deleteAll(delList);
		}
	}
}
