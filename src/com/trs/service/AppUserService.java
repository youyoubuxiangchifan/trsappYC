/**   
* @Title: AppUserService.java 
* @Package com.trs.service 
* @Description: TODO 
* @author jin.yu 
* @date 2014-3-10 下午05:03:55 
* @version V1.0   
*/
package com.trs.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trs.dao.AppUserDao;
import com.trs.model.AppGrpuser;
import com.trs.model.AppRoleUser;
import com.trs.model.AppUser;
import com.trs.util.CMyString;
import com.trs.util.DBTools;
import com.trs.util.DateUtil;
import com.trs.util.FileUtil;
import com.trs.util.Global;
import com.trs.util.MD5;

/**
 * 
 * Description: 互动平台用户模块管理<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: AppUserService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-12 下午03:48:14
 * @version 1.0
 */
@Service
public class AppUserService extends BaseService{
	@Autowired
	private AppUserDao appUserDao ;
	@Autowired
	private AppSysConfigService appSysConfigService ;
	/**
	 * 
	* Description: 新增用户 <BR>   
	* @author jin.yu
	* @date 2014-3-12 下午03:48:26
	* @param appUser 用户对象
	* @version 1.0
	 * @throws Exception 
	 */
	public void addUser(AppUser appUser) throws Exception{
		if(!existUser(appUser.getUsername())){
			String password = appUser.getPassword();
			MD5 md5 = new MD5();
			password = md5.getMD5ofStr(password);//密码进行MD5加密
			appUser.setPassword(password);
			appUser.setIsDeleted(0);//未删除的状态
			appUserDao.save(appUser);//保存一个用户
		}else {
			throw new Exception(appUser.getUsername()+"要新增的用户已存在！");
		}
	}
	
	/**
	 * 
	* Description:  用户新增，在Excel文件导入用户时或从WCM中抽取用户时使用<BR>   
	* @author jin.yu
	* @date 2014-3-24 下午07:58:12
	* @param appUser 用户对象
	* @throws Exception
	* @version 1.0
	 */
	public boolean addUserImport(AppUser appUser) throws Exception{
		if(!existUser(appUser.getUsername())){
			String password = appUser.getPassword();
			MD5 md5 = new MD5();
			password = md5.getMD5ofStr(password);//密码进行MD5加密
			appUser.setPassword(password);
			appUserDao.save(appUser);//保存一个用户
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 
	* Description: 修改用户 <BR>   
	* @author jin.yu
	* @date 2014-3-28 下午11:58:23
	* @param appUser
	* @throws Exception
	* @version 1.0
	 */
	public void updateUser(AppUser appUser) throws Exception{
		String password = appUser.getPassword();
		MD5 md5 = new MD5();
		password = md5.getMD5ofStr(password);//密码进行MD5加密
		appUser.setPassword(password);
		appUserDao.update(appUser);//修改一个用户
	}
	
	/**
	 * 
	* Description:修改用户状态<BR>   
	* @author jin.yu
	* @date 2014-3-12 下午10:05:19
	* @param UserIds 用户ID字符串，以逗号隔开
	* @param status 用户状态
	* @version 1.0
	 * @throws Exception 
	 */
	public void updateUserStatus(String UserIds,int status) throws Exception{
		List<Object> userList = findByIds("AppUser", "userId", UserIds);
		for(Object object : userList){
			AppUser appUser = (AppUser)object;
			appUser.setStatus(status);//设定用户状态
		}
		appUserDao.saveOrUpdateAll(userList);
	}
	
	/**
	 * 
	* Description: 彻底删除用户<BR>   
	* @author jin.yu
	* @date 2014-3-10 下午05:21:50
	* @param UserIds 用户ID字符串，以逗号隔开
	* @version 1.0
	 * @throws Exception 
	 */
	public void deleteUser(String UserIds) throws Exception{
		List<Object> userList = findByIds("AppUser", "userId", UserIds);
		for(Object object : userList){
			AppUser appUser = (AppUser)object;
			//删除用户跟组织的关联关系
			List<Object> userGroupList = find("", AppGrpuser.class.getName(), "userId = ? ", "", appUser.getUserId());
			appUserDao.deleteAll(userGroupList);
			//删除用户跟角色的关联关系
			List<Object> userRoleList = find("", AppRoleUser.class.getName(), "userId = ? ", "", appUser.getUserId());
			appUserDao.deleteAll(userRoleList);
			//对用户名做特殊处理，在最后拼接$del
			StringBuffer userName = new StringBuffer(appUser.getUsername());
			userName.append("$del");
			appUser.setUsername(userName.toString());
			appUser.setIsDeleted(Global.USER_IS_DELETED);//设定用户状态为删除
		}
		appUserDao.saveOrUpdateAll(userList);
		
	}
	
	/**
	 * 
	* Description: 用户修改密码<BR>
	* @author jin.yu
	* @date 2014-3-11 上午11:31:46
	* @param appUser 用户对象
	* @param newPassWord 新用户密码
	* @version 1.0
	 */
	public void resetPassWord(AppUser appUser,String newPassWord,int isWeakPasswd) throws Exception{
		AppUser appUserNew = (AppUser)appUserDao.findById(AppUser.class, appUser.getUserId());
		MD5 md5 = new MD5();
		appUserNew.setPassword(md5.getMD5ofStr(newPassWord));
		appUserNew.setWeakPasswd(isWeakPasswd);
		appUserDao.update(appUserNew);
	}
	
	/**
	 * 
	* Description:  用户修改密码<BR>   
	* @author jin.yu
	* @date 2014-3-11 下午07:35:10
	* @param appUser 用户对象
	* @param newPassWord 新用户密码
	* @param oldPassWord 老用户密码
	* @return boolean
	* @version 1.0
	 */
	public boolean resetPassWord(AppUser appUser,String newPassWord,String oldPassWord,int isWeakPasswd){
		AppUser appUserNew = (AppUser)appUserDao.findById(AppUser.class, appUser.getUserId());
		MD5 md5 = new MD5();
		String oldPassWordMD5 = md5.getMD5ofStr(oldPassWord);
		if(oldPassWordMD5.equals(appUser.getPassword())){
			appUserNew.setPassword(md5.getMD5ofStr(newPassWord));
			appUserNew.setWeakPasswd(isWeakPasswd);
			appUserDao.update(appUserNew);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	* Description:  判断用户表中用户名是否已存在<BR>   
	* @author jin.yu
	* @date 2014-3-13 上午10:05:30
	* @param userName
	* @return boolean
	* @throws Exception
	* @version 1.0
	 */
	public boolean existUser(String userName) throws Exception {
    	return existData(AppUser.class.getName(),"username=?",userName);
	}
	/**
	 * 
	* Description:  判断用户表中用户名是否已存在<BR>   
	* @author jin.yu
	* @date 2014-3-13 上午10:05:30
	* @param userName
	* @return boolean
	* @throws Exception
	* @version 1.0
	 */
	public boolean existUser(String userName,String password) throws Exception {
		String where = "username=? and password=? and isDeleted=0";
		MD5 md5 = new MD5();
		password = md5.getMD5ofStr(password);//密码进行MD5加密
		List<Object> params = new ArrayList<Object>();
		params.add(userName);
		params.add(password);
		return existData(AppUser.class.getName(),where, params);
	}
	/**
	 * 
	* Description:  通过用户ID判断用户是否超级管理员<BR>   
	* @author jin.yu
	* @date 2014-3-13 下午03:05:39
	* @param userId 用户ID
	* @return boolean
	* @version 1.0
	 */
	public boolean isAdminUser(Long userId){
		AppUser appUser = (AppUser)findById(AppUser.class,userId);
		if(appUser.getUsertype()==Global.USER_IS_ADMIN){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	* Description:  Excel用户导入<BR>   
	* @author jin.yu
	* @date 2014-3-13 上午10:10:54
	* @param file 导入Excel文件
	* @param loginUser 当前操作用户对象
	* @version 1.0
	 * @throws Exception 
	 */
	public Map<String, Object> importUser(File inputFile,AppUser loginUser) throws Exception{
		WritableWorkbook book = null;
        // Excel获得文件   
        Workbook wb = Workbook.getWorkbook(inputFile);  
        // 打开一个文件的副本，并且指定数据写回到原文件
        String fileName = DateUtil.formatDate(DateUtil.now())+".xls";
        String filePath = appSysConfigService.findSysConfigCon("APP_ACCESSORY_PATH")+fileName;
        book = Workbook.createWorkbook(new File(filePath), wb);  
        Sheet sheet = book.getSheet(0);  
        WritableSheet wsheet = book.getSheet(0);  
        int colunms = sheet.getColumns();
		List<List<Object>> listExl = FileUtil.readExcel(inputFile);
		inputFile.createNewFile();
		MD5 md5 = new MD5();
		int successUser = 0;
		int failUser = 0;
		boolean successFlag = false;
		for(int i=1;i<listExl.size();i++){
			List<Object> listUser = listExl.get(i);
			if(existUser((String)listUser.get(0))){
				failUser++;
				Label label = new Label(colunms, i, "导入失败",FileUtil.getDataCellFormatRed());
	            wsheet.addCell(label);				
				continue;
			}
			AppUser appUser = new AppUser();
			appUser.setUsername((String)listUser.get(0));
			appUser.setPassword(md5.getMD5ofStr((String)listUser.get(1)));
			appUser.setTruename((String)listUser.get(2));
			appUser.setMoblie((String)listUser.get(3));
			appUser.setAddress((String)listUser.get(4));
			appUser.setEmail((String)listUser.get(5));
			appUser.setZipcode((String)listUser.get(6));
			appUser.setStatus(Global.USER_STATUS_REG);
			appUser.setUsertype(Global.USER_IS_NOT_ADMIN);
			appUser.setIsDeleted(Global.USER_IS_NOT_DELETED);
			appUser.setCruser(loginUser.getUsername());
			appUser.setCrtime(DateUtil.now());
			successFlag = addUserImport(appUser);
			if (successFlag) {
				successUser++;
				Label label = new Label(colunms, i, "导入成功",FileUtil.getDataCellFormatBlack());
	            wsheet.addCell(label);
			}
		}
        book.write();
        book.close();
		Map<String, Object> returnMsg = new HashMap<String, Object>();
		returnMsg.put("amountUser", listExl.size()-1);
		returnMsg.put("successUser", successUser);
		returnMsg.put("failUser", failUser);
		returnMsg.put("fileName", fileName);
		return returnMsg;
	}
	
	/**
	 * 
	* Description:  通过用户Id取得用户关联的所有组织对象<BR>   
	* @author jin.yu
	* @date 2014-3-18 下午03:38:36
	* @param userId 单个用户ID
	* @return list 组织对象集合list
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getUserGroups(Long userId) throws Exception{
		List<Object> groupIdsList = find("groupId",AppGrpuser.class.getName(), "userId=?","", userId);
		List<Object> groupList = findByIds("AppGroup", "groupId", groupIdsList);
		return groupList;
	}
	
	/**
	 * 
	* Description: 通过用户Id取得用户关联的所有组织ID<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午05:49:35
	* @param userId 单个用户ID
	* @return String 组织ID字符串，以逗号分割
	* @throws Exception
	* @version 1.0
	 */
	public String getUserGroupIds(Long userId) throws Exception{
		List<Object> groupIdsList = find("groupId",AppGrpuser.class.getName(), "userId=?","", userId);
		String userGroupIds = CMyString.join((ArrayList<Object>)groupIdsList, ",");
		return userGroupIds;
	}
	
	/**
	 * 
	* Description: 通过用户Id取得用户关联的所有组织管理员组织ID <BR>   
	* @author jin.yu
	* @date 2014-4-10 上午11:42:01
	* @param userId 单个用户ID
	* @return String 组织ID字符串，以逗号分割
	* @throws Exception
	* @version 1.0
	 */
	public String getUserIsAdminGroupIds(Long userId) throws Exception{
		List<Object> groupIdsList = find("groupId",AppGrpuser.class.getName(), "userId = ? and isAdmin = 1","", userId);
		String userGroupIds = CMyString.join((ArrayList<Object>)groupIdsList, ",");
		return userGroupIds;
	}
	
	/**
	 * 
	* Description: 通过用户Id取得用户关联的所有独立组织ID<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午07:50:50
	* @param userId 单个用户ID
	* @return String 独立组织ID字符串，以逗号分割
	* @throws Exception
	* @version 1.0
	 */
	public String getUserInGroupIds(Long userId) throws Exception {
		List<Object> groupIdsList = find("u.groupId", "AppGrpuser g,AppGroup u", "g.userId = ? and g.groupId = u.groupId and u.isIndependent = 1 ", "", userId);
		String userInGroupIdsString = CMyString.join((ArrayList<Object>)groupIdsList, ",");
		return userInGroupIdsString;
	}
	/**
	 * 
	* Description: 通过用户Id取得用户关联的所有角色对象 <BR>   
	* @author jin.yu
	* @date 2014-3-18 下午03:46:27
	* @param userId 单个用户ID
	* @return list 角色对象集合list
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getUserRoles(Long userId) throws Exception{
		List<Object> roleIdsList = find("roleId",AppRoleUser.class.getName(), "userId=?","", userId);
		List<Object> roleList = findByIds("AppRole", "roleId", roleIdsList);
		return roleList;
	}
	
	/**
	 * 
	* Description: 通过用户Id取得用户关联的所有角色ID <BR>   
	* @author jin.yu
	* @date 2014-3-27 下午05:52:25
	* @param userId 单个用户ID
	* @return String 角色ID字符串，以逗号分割
	* @throws Exception
	* @version 1.0
	 */
	public String getUserRoleIds(Long userId) throws Exception {
		List<Object> roleIdsList = find("roleId",AppRoleUser.class.getName(), "userId=?","", userId);
		String userRoleIds = CMyString.join((ArrayList<Object>)roleIdsList, ",");
		return userRoleIds;
	}
	
	/**
	 * 
	* Description: 从WCM中抽取用户信息 <BR>   
	* @author jin.yu
	* @date 2014-4-16 下午05:32:13
	* @param loginUser 登录用户对象
	* @throws Exception
	* @version 1.0
	 */
	public void importWcmUser(AppUser loginUser) throws Exception{
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
		String initUserPassWord = (String)sysConfigMap.get("INIT_USER_PASSWORD");
		DBTools dbTools = new DBTools(wcmDBType, wcmDBIp,wcmDBSid,wcmDBUserName,wcmDBPassWord);
		String sql = "select t.username,t.nickname,t.truename,t.address,t.mobile,t.email from WCMUSER t where t.status=30";
        List<String[]> datalist = dbTools.getDataset(sql, 6);
        MD5 md5 = new MD5();
        
        for(int i=0; i < datalist.size(); i++){
            String[] datas = (String[]) datalist.get(i);
            AppUser appUser = new AppUser();
            appUser.setUsername(datas[0]);
            appUser.setTruename(datas[2]);
            appUser.setPassword(md5.getMD5ofStr(initUserPassWord));
			appUser.setMoblie(datas[4]);
			appUser.setAddress(datas[3]);
			appUser.setEmail(datas[5]);
			appUser.setStatus(Global.USER_STATUS_REG);
			appUser.setUsertype(Global.USER_IS_NOT_ADMIN);
			appUser.setIsDeleted(Global.USER_IS_NOT_DELETED);
			appUser.setCruser(loginUser.getUsername());
			addUserImport(appUser);
        }
	}
	
	/**
	 * 
	* Description:用户登录功能，通过传入的用户名和密码对用户进行验证，返回AppUser对象 <BR>   
	* @author jin.yu
	* @date 2014-3-26 下午04:17:45
	* @param userName 用户名
	* @param passWord 用户密码
	* @return AppUser用户对象
	* @throws Exception
	* @version 1.0
	 */
	public AppUser loginUser(String userName,String passWord) throws Exception{
		MD5 md5 = new MD5();
		passWord = md5.getMD5ofStr(passWord);
		List<Object> paramters = new ArrayList<Object>();
		paramters.add(userName);
		paramters.add(passWord);
		AppUser appUser = (AppUser) findObject(AppUser.class.getName(), "username=? and password=?", paramters);
		return appUser;
	}
}
