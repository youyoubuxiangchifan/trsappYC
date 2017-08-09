package com.trs.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.cache.CacheRoleOper;
import com.trs.dao.AppBaseDao;
import com.trs.dbhibernate.Page;
import com.trs.model.AppFieldInfo;
import com.trs.model.AppFieldRel;
import com.trs.model.AppFlowDoc;
import com.trs.model.AppFlowNode;
import com.trs.model.AppGroup;
import com.trs.model.AppInfo;
import com.trs.model.AppRoleOper;
import com.trs.model.AppRoleSys;
import com.trs.model.AppTableInfo;
import com.trs.model.AppViewInfo;
import com.trs.model.AppUser;
import com.trs.util.CMyString;
import com.trs.util.DateUtil;
import com.trs.util.Global;

/**
 * Description: 后台应用管理操作
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppBaseDaoService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-17 下午02:38:20
 * @version 1.0
 */
@Service
public class AppBaseService extends BaseService {
	@Autowired
	private AppBaseDao appBaseDao;
	/**
	 * 添加视图
	* Description: 添加视图<BR>   
	* @author liu.zhuan
	* @date 2014-3-27 下午04:09:59
	* @param appViewInfo 视图对象
	* @param groupIds 组织编号
	* @param user 登录用户
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public void addAppViewInfo(AppViewInfo appViewInfo,String groupIds, AppUser user) throws Exception{
		//验证数据，判断应用是否存在
		String viewName = appViewInfo.getViewName();
		if(CMyString.isEmpty(viewName)){
			throw new Exception("视图名称为空！");
		}
		List<Object> params = new ArrayList<Object>();
		params.add(viewName);
		boolean isHas = existData(AppViewInfo.class.getName(), "viewName = ?", params);
		if(isHas){
			throw new Exception("已存在相同名称的视图！");
		}
		//String groupIds = appViewInfo.getGroupIds();
		if(CMyString.isEmpty(groupIds)){
			throw new Exception("应用所属组织为空！");
		}
		//保存视图
		appBaseDao.save(appViewInfo);
		//类似创建到各个组织
		Long viewId = appViewInfo.getViewId();
		List<Object> appInfos = new ArrayList<Object>();
		List<Object> groupList = (List<Object>)findByIds("new map(groupId as groupId,gname as gname)", AppGroup.class.getName(), "groupId", groupIds);
		AppInfo appInfo = null;
		Map<String, Object> group = null;
		for (Object object : groupList) {
			group = (Map<String, Object>)object;
			if(group == null) continue;
			appInfo = new AppInfo();
			appInfo.setViewId(viewId);
			appInfo.setViewName(appViewInfo.getViewName());//同步视图名称到各个应用下去。
			appInfo.setGroupId(Long.parseLong(group.get("groupId").toString()));
			appInfo.setAppName(appViewInfo.getViewName() + "(" + group.get("gname") + ")");
			appInfo.setAppStatus(appViewInfo.getAppStatus());
			appInfo.setFlowId(new Long(0));
			appInfo.setDeleted(0);
			appInfo.setIsEmailWarn(appViewInfo.getIsEmailWarn());
			appInfo.setIsSmsRemind(appViewInfo.getIsSmsRemind());
			appInfo.setIsSmsWarn(appViewInfo.getIsSmsWarn());
			appInfo.setIsHasComment(appViewInfo.getIsHasComment());
			appInfo.setIsHasQueryNo(appViewInfo.getIsHasQueryNo());
			appInfo.setIsNeedTheme(appViewInfo.getIsNeedTheme());
			appInfo.setIsPublic(appViewInfo.getIsPublic());
			appInfo.setIsPush(appViewInfo.getIsPush());
			appInfo.setIsShowGroup(appViewInfo.getIsShowGroup());
			appInfo.setWcmDocType(appViewInfo.getWcmDocType());
			appInfo.setWcmChnlId(appViewInfo.getWcmChnlId());
			appInfo.setCruser(user.getUsername());
			appInfo.setIsSupAppendix(appViewInfo.getIsSupAppendix());
			appInfo.setLimitDayNum(appViewInfo.getLimitDayNum());
			appInfo.setIsSelGroup(0);
			appInfo.setIsHasSmtDesc(appViewInfo.getIsHasSmtDesc());
			appInfo.setSmtDesc(appViewInfo.getSmtDesc());
			appInfo.setListAddr(appViewInfo.getListAddr());
			appInfo.setDowithAddr(appViewInfo.getDowithAddr());
			appInfos.add(appInfo);
			
		}
		appBaseDao.saveOrUpdateAll(appInfos);
	}

	/**
	 * 修改视图
	* Description:  修改视图<BR>   
	* @author liu.zhuan
	* @date 2014-3-27 下午04:30:55
	* @param appViewInfo 视图对象
	* @param groupIds 组织编号
	* @param user 登录用户 
	* @throws Exception
	* @version 1.0
	 */
	public void updateAppViewInfo(AppViewInfo appViewInfo, String groupIds, AppUser user) throws Exception{
		//判断组织编号是否需要更新
		//String groupIds = appViewInfo.getGroupIds();
		if(!CMyString.isEmpty(groupIds)){
			List<Object> appInfos = new ArrayList<Object>();
//			List<Object> addAppInfos = new ArrayList<Object>();
//			List<Object> delAppInfos = new ArrayList<Object>();
			List<Object> oldAppInfos = find("", AppInfo.class.getName(), "viewId = ?", "", appViewInfo.getViewId());
			String groupIdArray[] = groupIds.split(",");
			List<String> groupIdList = Arrays.asList(groupIdArray);
			//遍历新传入的组织编号，和数据库中已存在的编号做比对，如果不存在，则为新增的组织编号。
			AppInfo appInfo = null;
			for (String groupId : groupIdList) {
				if(CMyString.isEmpty(groupId)) continue;
				Long nGroupId = Long.parseLong(groupId);
				boolean flag = false;//存在true,不存在false
				for (Object obj : oldAppInfos) {
					appInfo = (AppInfo)obj;
					if(groupId.equals(appInfo.getGroupId().toString())){
						flag = true;
						/*appInfo.setAppStatus(appViewInfo.getAppStatus());
						appInfo.setIsEmailWarn(appViewInfo.getIsEmailWarn());
						appInfo.setIsSmsRemind(appViewInfo.getIsSmsRemind());
						appInfo.setIsSmsWarn(appViewInfo.getIsSmsWarn());
						appInfo.setIsHasComment(appViewInfo.getIsHasComment());
						appInfo.setIsHasQueryNo(appViewInfo.getIsHasQueryNo());
						appInfo.setIsNeedTheme(appViewInfo.getIsNeedTheme());
						appInfo.setIsPublic(appViewInfo.getIsPublic());
						appInfo.setIsPush(appViewInfo.getIsPush());
						appInfo.setIsShowGroup(appViewInfo.getIsShowGroup());
						appInfo.setWcmChnlId(appViewInfo.getWcmChnlId());*/
						//如果是已逻辑删除的应用则恢复
						/*if(appInfo.getDeleted() == 1){
							appInfo.setDeleted(0);
						}*/
						/*appInfo.setOperTime(DateUtil.now());
						appInfo.setOperUser(user.getUsername());
						appInfos.add(appInfo);*/
						break;
					}
				}
				if(!flag){
					//addAppGroup.add(new AppGroupFlow(null,appInfo.getAppId(), Long.parseLong(groupId), null, user.getUsername(), null));
					appInfo = new AppInfo();
					appInfo.setViewId(appViewInfo.getViewId());
					appInfo.setViewName(appViewInfo.getViewName());
					appInfo.setGroupId(nGroupId);
					AppGroup group = (AppGroup)findById(AppGroup.class, nGroupId);
					appInfo.setAppName(appViewInfo.getViewName() + "(" + group.getGname() + ")");
					appInfo.setAppStatus(appViewInfo.getAppStatus());
					appInfo.setFlowId(new Long(0));
					appInfo.setDeleted(0);
					appInfo.setIsEmailWarn(appViewInfo.getIsEmailWarn());
					appInfo.setIsSmsRemind(appViewInfo.getIsSmsRemind());
					appInfo.setIsSmsWarn(appViewInfo.getIsSmsWarn());
					appInfo.setIsHasComment(appViewInfo.getIsHasComment());
					appInfo.setIsHasQueryNo(appViewInfo.getIsHasQueryNo());
					appInfo.setIsNeedTheme(appViewInfo.getIsNeedTheme());
					appInfo.setIsPublic(appViewInfo.getIsPublic());
					appInfo.setIsPush(appViewInfo.getIsPush());
					appInfo.setIsShowGroup(appViewInfo.getIsShowGroup());
					appInfo.setWcmDocType(appViewInfo.getWcmDocType());
					appInfo.setWcmChnlId(appViewInfo.getWcmChnlId());
					appInfo.setCruser(user.getUsername());
					appInfo.setMainTableId(appViewInfo.getMainTableId());
					appInfo.setMainTableName(appViewInfo.getMainTableName());
					appInfo.setItemTableId(appViewInfo.getItemTableId());
					appInfo.setItemTableName(appViewInfo.getItemTableName());
					appInfo.setIsSupAppendix(appViewInfo.getIsSupAppendix());
					appInfo.setLimitDayNum(appViewInfo.getLimitDayNum());
					appInfo.setIsSelGroup(0);
					appInfo.setIsHasSmtDesc(appViewInfo.getIsHasSmtDesc());
					appInfo.setSmtDesc(appViewInfo.getSmtDesc());
					appInfo.setListAddr(appViewInfo.getListAddr());
					appInfo.setDowithAddr(appViewInfo.getDowithAddr());
					appInfos.add(appInfo);
				}
			}
			//遍历数据库中已存在的编号，和新传入的组织编号做比对，如果不存在，则为删除的组织编号。
			/*for (Object obj : oldAppInfos) {
				if(obj == null) continue;
				appInfo = (AppInfo)obj;
				Long groupId = appInfo.getGroupId();
				if(!groupIdList.contains(groupId.toString())){
					appInfo.setDeleted(1);
					appInfo.setOperTime(DateUtil.now());
					appInfo.setOperUser(user.getUsername());
					appInfos.add(appInfo);
				}
			}*/
			//更新应用
			appBaseDao.saveOrUpdateAll(appInfos);
		}
		appBaseDao.update(appViewInfo);
	}

	/**
	 * 删除视图
	 * Description: 删除视图
	 * @author liu.zhuan
	 * @date 2014-3-17 下午03:17:27
	 * @param appId 应用编号
	 * @param nMod 删除类型，0为逻辑删除，1为物理删除
	 * @version 1.0
	 * @throws Exception 
	 */
	public void deleteAppViewInfo(Long viewId, int nMod, AppUser user) throws Exception{
		AppViewInfo appViewInfo = (AppViewInfo)appBaseDao.findById(AppViewInfo.class, viewId);
		if(appViewInfo == null){
			throw new Exception("ID为：" + viewId + "的视图没有找到！");
		}
		if(nMod == 1 && appViewInfo.getDeleted() == 1){//物理删除
			//删除视图信息
			appBaseDao.delete(appViewInfo);
			//删除视图下应用
			List<Object> appInfoList = find("", AppInfo.class.getName(), "viewId = ? and deleted = 1", "", viewId);
			if(appInfoList != null && appInfoList.size() > 0){
				appBaseDao.deleteAll(appInfoList);
			}
			//删除视图字段
			List<Object> appFieldRelList = find("", AppFieldRel.class.getName(), "viewId = ?", "", viewId);
			if(appFieldRelList != null && appFieldRelList.size() > 0){
				appBaseDao.deleteAll(appFieldRelList);
			}
			//删除数据？？？？？
		} else {//逻辑删除
			appViewInfo.setViewName("$del." + appViewInfo.getViewName());
			appViewInfo.setDeleted(1);
			appViewInfo.setOperTime(DateUtil.now());
			appViewInfo.setOperUser(user.getUsername());
			appBaseDao.update(appViewInfo);
			//逻辑删除应用
			List<Object> appInfoList = find("", AppInfo.class.getName(), "viewId = ?", "", viewId);
			if(appInfoList != null && appInfoList.size() > 0){
				AppInfo appInfo = null;
				for (Object object : appInfoList) {
					appInfo = (AppInfo)object;
					appInfo.setDeleted(1);
					appInfo.setOperTime(DateUtil.now());
					appInfo.setOperUser(user.getUsername());
				}
				appBaseDao.saveOrUpdateAll(appInfoList);
			}
		}
	}
	
	/**
	 * 删除应用
	 * Description: 删除应用
	 * @author liu.zhuan
	 * @date 2014-3-17 下午03:17:27
	 * @param appIds 应用编号,以","隔开。
	 * @param nMod 删除类型，0为逻辑删除，1为物理删除
	 * @version 1.0
	 * @throws Exception 
	 */
	public void deleteAppInfo(Long appId, int nMod) throws Exception{
		AppInfo appInfo = (AppInfo)findById(AppInfo.class, appId);
		if(appInfo == null){
			throw new Exception("没有找到编号为：" + appId + "的应用！");
		}
		if(nMod == 1 && appInfo.getDeleted() == 1){//物理删除
			//删除应用信息
			appBaseDao.delete(appInfo);
			//删除应用角色关联
			List<Object> appRoleList = find("", AppRoleSys.class.getName(), "appId = ?", "", appId);
			if(appRoleList != null && appRoleList.size() > 0){
				appBaseDao.deleteAll(appRoleList);
			}
			//删除工作流转轨迹
			List<Object> appFlowDocList = find("", AppFlowDoc.class.getName(), "appId = ?", "", appId);
			if(appFlowDocList != null && appFlowDocList.size() > 0){
				appBaseDao.deleteAll(appFlowDocList);
			}
			//删除数据？？？
		} else {//逻辑删除
			appInfo.setDeleted(1);
			appBaseDao.update(appInfo);
		}
	}
	
	/**
	 * 批量删除应用
	 * Description: 批量删除应用
	 * @author liu.zhuan
	 * @date 2014-3-17 下午03:17:27
	 * @param appIds 应用编号,以","隔开。
	 * @param nMod 删除类型，0为逻辑删除，1为物理删除
	 * @version 1.0
	 * @throws Exception 
	 */
	public void deleteAppInfos(String appIds, int nMod, AppUser user) throws Exception{
		if(CMyString.isEmpty(appIds)){
			throw new Exception("应用编号没有传入！");
		}
		List<Object> appInfos = findByIds(AppInfo.class.getName(), "appId", appIds);
		if(appInfos == null || appInfos.size() == 0){
			throw new Exception("没有找到要删除的应用对象！");
		}
		if(nMod == 1){//物理删除
			//删除应用信息
			appBaseDao.deleteAll(appInfos);
			//删除应用角色关联
			List<Object> appRoleList = findByIds(AppRoleSys.class.getName(), "appId", appIds);
			if(appRoleList != null && appRoleList.size() > 0){
				appBaseDao.deleteAll(appRoleList);
			}
			//删除工作流转轨迹
			List<Object> appFlowDocList = findByIds(AppFlowDoc.class.getName(), "appId", appIds);
			if(appFlowDocList != null && appFlowDocList.size() > 0){
				appBaseDao.deleteAll(appFlowDocList);
			}
			//删除数据？？？
		} else {//逻辑删除
			for (Object object : appInfos) {
				AppInfo appInfo = (AppInfo)object;
				appInfo.setDeleted(1);
				appInfo.setOperTime(DateUtil.now());
				appInfo.setOperUser(user.getUsername());
			}
			appBaseDao.saveOrUpdateAll(appInfos);
		}
	}
	/**
	 * 还原应用
	 * Description: 还原逻辑删除的应用 <BR>   
	 * @author liu.zhuan
	 * @date 2014-3-24 上午10:12:56
	 * @param appIds String 应用编号  以","号隔开。
	 * @throws Exception
	 * @version 1.0
	 */
	public void restoreAppViewInfo(String appIds, AppUser user) throws Exception{
		List<Object> appInfos = findByIds(AppInfo.class.getName(), "appId", appIds);
		if(appInfos == null || appInfos.size() == 0){
			throw new Exception("没有找到要处理的应用！");
		}
		AppInfo appInfo = null;
		for (Object object : appInfos) {
			appInfo = (AppInfo)object;
			appInfo.setDeleted(0);
			appInfo.setOperTime(DateUtil.now());
			appInfo.setOperUser(user.getUsername());
		}
		appBaseDao.saveOrUpdateAll(appInfos);
	}
	/**
	 * 分配工作流
	 * Description:  给指应用分配工作流<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-24 上午10:30:31
	 * @param appId	Long 应用组织工作流关系编号
	 * @param flowId Long 工作流编号
	 * @version 1.0
	 * @throws Exception 
	 */
	public void assignFlowToApp(Long appId, Long flowId) throws Exception{
		AppInfo appInfo = (AppInfo)appBaseDao.findById(AppInfo.class, appId);
		if(appInfo == null){
			throw new Exception("没有找到应用组织关系对象！");
		}
		appInfo.setFlowId(flowId);
		appBaseDao.update(appInfo);
	}
	
	/**
	 * 取消分配工作流
	 * Description:  取消分配工作流<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-24 上午10:30:31
	 * @param appGrpFlowId	Long	应用组织工作流关系编号
	 * @version 1.0
	 * @throws Exception 
	 */
	public void cancelFlowToApp(Long appId) throws Exception{
		AppInfo appInfo = (AppInfo)appBaseDao.findById(AppInfo.class, appId);
		if(appInfo == null){
			throw new Exception("没有找到应用组织关系对象！");
		}
		appInfo.setFlowId(new Long(0));
		appBaseDao.update(appInfo);
	}
	/**
	 * 获取视图下已绑定的组织编号
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-3-27 下午09:07:20
	* @param viewId 视图编号
	* @return List<Object> 组织编号集合
	* @version 1.0
	 * @throws Exception 
	 */
	public String getGroupIdsByViewId(Long viewId) throws Exception{
		ArrayList<Object> list = (ArrayList<Object>)find("groupId", AppInfo.class.getName(), "viewId = ?", "", viewId);
		return CMyString.join(list, ",");
	}
	/**
	 * 根据视图编号查询视图字段列表
	* Description: 根据视图编号查询视图字段列表 <BR>   
	* @author liu.zhuan
	* @date 2014-3-28 下午09:07:31
	* @param viewId 视图编号
	* @param currPage 当前页码
	* @param pageSize 页码大小
	* @param tableType 表类型，0为通用类型，1为意见类型
	* @return Page 分页对象
	* @throws Exception
	* @version 1.0
	 */
	public Page getViewFieldList(long viewId, int currPage, int pageSize, int tableType) throws Exception{
		if(viewId == 0){
			throw new Exception("没有找到视图编号为0的字段列表！");
		}
		AppViewInfo viewInfo = (AppViewInfo)findById(AppViewInfo.class, viewId);
		List<Object> paramters = new ArrayList<Object>();
		StringBuffer sWhere = new StringBuffer("viewId = ? and mainTableName = ?");
		paramters.add(viewId);
		if(tableType == 0){
			//sWhere.append(" and mainTableName = ?");
			paramters.add(viewInfo.getMainTableName());
		} else {
			//sWhere.append(" and mainTableName = ?");
			paramters.add(viewInfo.getItemTableName());
		}
		return findPage("", AppFieldRel.class.getName(), sWhere.toString() , "fieldOrder asc", currPage, pageSize, paramters);
	}
	/**
	 * 根据视图获取元数据字段列表
	 * Description:  根据视图获取元数据字段列表<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-29 下午06:45:25
	 * @param viewId 视图编号
	 * @param tbType 元数据类型
	 * @return List<Object>
	 * @throws Exception
	 * @version 1.0
	 */
	public List<Object> getDBTableByView(long viewId, int tbType) throws Exception{
		if(viewId == 0){
			throw new Exception("没有找到视图编号为0的元数据列表！");
		}
		AppViewInfo viewInfo = (AppViewInfo)findById(AppViewInfo.class, viewId);
		String sWhere = "";
		Object param = null;
		if(viewInfo.getMainTableId() != null){//通用类型时根据主表查询
			sWhere = "tableInfoId = ?";
			param = viewInfo.getMainTableId();
		}/* else if(tbType == 1 && viewInfo.getItemTableId() != null){//意见类型时根据附表查询
			sWhere = "tableInfoId = ?";
			param = viewInfo.getItemTableId();
		} */else {
			sWhere = "tableType = ?";
			param = tbType;
		}
		return find("", AppTableInfo.class.getName(), sWhere, "", param);
	}
	/**
	 * 视图字段维护
	 * Description: 视图字段维护 <BR>   
	 * @author liu.zhuan
	 * @date 2014-3-29 下午08:56:34
	 * @param viewId 视图编号
	 * @param tbType 元数据类型
	 * @param tableId 元数据表编号
	 * @param tableName 元数据表名
	 * @param sOutLinefields 概览字段列表
	 * @param sTitleFields 标题字段列表
	 * @param sSearchFields 检索字段列表
	 * @param sIsWebFields 是否前台展现列表
	 * @param sAllFields 所有已选择字段
	 * @throws Exception
	 * @version 1.0
	 */
	public void addEditViewField(long viewId, int tbType, long tableId, String tableName, String sOutLinefields, String sTitleFields, String sSearchFields, String sIsWebFields, String sAllFields, String cruser,String isGrpField) throws Exception{
		if(tableId == 0 || CMyString.isEmpty(tableName)){
			throw new Exception("没有选择元数据！");
		}
		if(CMyString.isEmpty(sAllFields)){
			throw new Exception("没有选择字段！");
		}
		//更新视图表，主表名，附表名
		AppViewInfo viewInfo = (AppViewInfo)findById(AppViewInfo.class, viewId);
		if(Global.METADATA_TYPE_NATIONAL == tbType){
			viewInfo.setMainTableId(tableId);
			viewInfo.setMainTableName(tableName);
		} else {
			viewInfo.setItemTableId(tableId);
			viewInfo.setItemTableName(tableName);
		}
		appBaseDao.update(viewInfo);
		//更新视图应用，更新主表名，附表名
		List<Object> appInfoList = find("", AppInfo.class.getName(), "viewId = ?", "", viewId);
		for (Object object : appInfoList) {
			AppInfo appInfo = (AppInfo) object;
			appInfo.setViewId(viewId);
			appInfo.setViewName(appInfo.getViewName());
			if(Global.METADATA_TYPE_NATIONAL == tbType){
				appInfo.setMainTableId(tableId);
				appInfo.setMainTableName(tableName);
			} else {
				appInfo.setItemTableId(tableId);
				appInfo.setItemTableName(tableName);
			}
		}
		appBaseDao.saveOrUpdateAll(appInfoList);
		//比对视图字段，更新视图字段列表
		List<String> allFields = Arrays.asList(sAllFields.split(","));
		List<String> outLineList = null;
		List<String> titleFieldList = null;
		List<String> searchFieldList = null;
		List<String> isWebFieldList = null;
		if(!CMyString.isEmpty(sOutLinefields)){
			outLineList = Arrays.asList(sOutLinefields.split(","));
		}
		if(!CMyString.isEmpty(sTitleFields)){
			titleFieldList = Arrays.asList(sTitleFields.split(","));
		}
		if(!CMyString.isEmpty(sSearchFields)){
			searchFieldList = Arrays.asList(sSearchFields.split(","));
		}
		if(!CMyString.isEmpty(sIsWebFields)){
			isWebFieldList = Arrays.asList(sIsWebFields.split(","));
		}
		List<Object> fieldRelList = new ArrayList<Object>();
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(viewId);
		parameters.add(tableId);
		List<Object> oldFieldRelList = find("", AppFieldRel.class.getName(), "viewId = ? and mainTableId = ?", "fieldOrder asc", parameters);
		boolean isHas = false;//判断有没有绑定过字段，true为已绑定，false为没有绑定
		if(oldFieldRelList != null && oldFieldRelList.size() > 0)
			isHas = true;
		//比对字段列表，判断新传入的字段编号是否在数据库中已存在，如果存在则修改，如果不存在则添加。
		AppFieldRel fieldRel = null;
		AppFieldInfo fieldInfo = null;
		//for (String fieldId : allFields) {
		for (int i = 0; i < allFields.size(); i++) {
			String fieldId = allFields.get(i);
			if(CMyString.isEmpty(fieldId)) continue;
			boolean flag = false;//存在true,不存在false
			if(isHas){
				for (Object object : oldFieldRelList) {
					fieldRel = (AppFieldRel)object;
					if(fieldId.equals(fieldRel.getFieldId().toString())){//存在则修改
						flag = true;
						if(outLineList != null && outLineList.contains(fieldRel.getFieldId().toString())){
							fieldRel.setInOutline(1);
						} else {
							fieldRel.setInOutline(0);
						}
						if(titleFieldList != null && titleFieldList.contains(fieldRel.getFieldId().toString())){
							fieldRel.setTitleField(1);
						} else {
							fieldRel.setTitleField(0);
						}
						if(searchFieldList != null && searchFieldList.contains(fieldRel.getFieldId().toString())){
							fieldRel.setSearchField(1);
						} else {
							fieldRel.setSearchField(0);
						}
						if(isWebFieldList != null && isWebFieldList.contains(fieldRel.getFieldId().toString())){
							fieldRel.setIsWebShow(1);
						} else {
							fieldRel.setIsWebShow(0);
						}
						if(!CMyString.isEmpty(isGrpField) && fieldRel.getFieldId().toString().equals(isGrpField)){
							fieldRel.setIsGrpField(1);
						}else{
							fieldRel.setIsGrpField(0);
						}
							
						fieldRel.setFieldOrder(i+1);
						fieldRelList.add(fieldRel);
						break;
					}
				}
			}
			if(!flag){
				fieldInfo = (AppFieldInfo)findById(AppFieldInfo.class, Long.valueOf(fieldId));
				fieldRel = new AppFieldRel();
				fieldRel.setViewId(viewId);
				fieldRel.setMainTableId(tableId);
				fieldRel.setMainTableName(tableName);
				fieldRel.setFieldId(Long.valueOf(fieldId));
				fieldRel.setFieldName(fieldInfo.getFieldName());
				fieldRel.setFieldLength(fieldInfo.getDblength());
				fieldRel.setFieldDesc(fieldInfo.getFieldDesc());
				fieldRel.setFieldStyle(fieldInfo.getFieldStyle());
				fieldRel.setFieldType(fieldInfo.getFieldType());
				fieldRel.setFieldOrder(i+1);//排序处理
				fieldRel.setIsReserved(fieldInfo.getIsReserved());
				fieldRel.setDefaultValue(fieldInfo.getDefaultValue());
				fieldRel.setEnmValue(fieldInfo.getEnmValue());
				fieldRel.setHiddenField(fieldInfo.getHiddenField());
				fieldRel.setNotEdit(fieldInfo.getNotEdit());
				fieldRel.setNotNull(fieldInfo.getNotNull());
				fieldRel.setFormFieldType(fieldInfo.getFormFieldType());
				//如果系统字段为隐藏字段的不细览显示
				if(fieldInfo.getHiddenField() != null && fieldInfo.getHiddenField() == 1 && fieldInfo.getIsReserved() == 1)
					fieldRel.setInDetail(0);
				else
					fieldRel.setInDetail(1);
				if(outLineList != null && outLineList.contains(fieldRel.getFieldId().toString())){
					fieldRel.setInOutline(1);
				}
				if(titleFieldList != null && titleFieldList.contains(fieldRel.getFieldId().toString())){
					fieldRel.setTitleField(1);
				}
				if(searchFieldList != null && searchFieldList.contains(fieldRel.getFieldId().toString())){
					fieldRel.setSearchField(1);
				}
				if(isWebFieldList != null && isWebFieldList.contains(fieldRel.getFieldId().toString())){
					fieldRel.setIsWebShow(1);
				}
				if(!CMyString.isEmpty(isGrpField) && fieldRel.getFieldId().toString().equals(isGrpField)){
					fieldRel.setIsGrpField(1);
				}
				fieldRel.setCrtime(DateUtil.now());
				fieldRel.setCruser(cruser);
				fieldRelList.add(fieldRel);
			}
		}
		appBaseDao.saveOrUpdateAll(fieldRelList);
		//遍历数据库中已存在的字段，和新传入的字段编号做比对，如果不存在，则为要删除的字段。
		List<Object> delFieldRelList = new ArrayList<Object>();
		if(isHas){
			for (Object object : oldFieldRelList) {
				if(object == null) continue;
				fieldRel = (AppFieldRel) object;
				if(!allFields.contains(fieldRel.getFieldId().toString())){
					delFieldRelList.add(fieldRel);
				}
			}
			appBaseDao.deleteAll(delFieldRelList);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getItemGroupEnum(String groupIds) throws Exception{
		StringBuffer itemGroups = new StringBuffer();
		List<Object> groupList = (List<Object>)findByIds("new map(groupId as groupId,gname as gname)", AppGroup.class.getName(), "groupId", groupIds);
		if(groupList != null && groupList.size() > 0){
			Map<String, Object> group = null;
			for (Object object : groupList) {
				group = (Map<String, Object>)object;
				itemGroups.append(group.get("groupId"))
					.append(":")
					.append(group.get("gname"))
					.append("~");
			}
			itemGroups = new StringBuffer(itemGroups.substring(0, itemGroups.length() - 1));
		}
		return itemGroups.toString();
	}
	/**
	 * 获取工作流开始节点或第二个节点上的所有有权限的组织编号集合
	* Description: 获取工作流开始节点或第二个节点上的所有组织编号集合 <BR>   
	* @author liu.zhuan
	* @date 2014-5-8 下午01:29:03
	* @param appId 应用编号
	* @param flowId 工作流编号
	* @return List<Object> 组织编号集合
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAuthFlowNodeGrpIds(AppInfo appInfo, long flowId) throws Exception{
		StringBuffer groupIds = new StringBuffer();//节点上所有组织编号
		StringBuffer userIds = new StringBuffer();//节点上所有用户编号
		//获取工作流下的第一个节点
		AppFlowNode firstNode = (AppFlowNode)findObject(AppFlowNode.class.getName(), "flowId = ? and nodeOrder = 0", flowId);
		if(firstNode == null){
			throw new Exception("工作流没有设置开始节点！");
		}
		//获取节点上的用户和组织集合
		if(CMyString.isEmpty(firstNode.getOperRuleName())){
			groupIds.append(CMyString.showEmpty(firstNode.getNodeDep(),"0"));
			userIds.append(CMyString.showEmpty(firstNode.getNodeUser(),"0"));
		}else{//如果第一个节点上设置了自动跳转，则找第二个节点
			if(CMyString.isEmpty(firstNode.getNextNodeIds())){
				throw new Exception("工作流的开始节点没有设置下个节点！");
			}
			if(appInfo.getIsSelGroup() == 0){
				throw new Exception("要使用此工作流，必须在应用中开启子组织选择！");
			}
			List<Object> secNodeList = findByIds("new list(nodeId,nodeUser,nodeDep)",AppFlowNode.class.getName(), "nodeId", firstNode.getNextNodeIds());
			List<Object> secNode = null;
			if(secNodeList != null && secNodeList.size() > 0){
				for (Object object : secNodeList) {
					secNode = (List<Object>)object;
					String nodeUser = (String)secNode.get(1);
					String nodeDepIds = (String)secNode.get(2);
					if(!CMyString.isEmpty(nodeUser)){
						userIds.append(nodeUser).append(",");
					}
					if(!CMyString.isEmpty(nodeDepIds)){
						groupIds.append(nodeDepIds).append(",");
					}
				}
			}
			if(CMyString.isEmpty(userIds.toString())){
				userIds.append("0");
			}else{
				userIds = new StringBuffer(userIds.substring(0, userIds.length() - 1));
			}
			if(CMyString.isEmpty(groupIds.toString())){
				groupIds.append("0");
			}else{
				groupIds = new StringBuffer(groupIds.substring(0, groupIds.length() - 1));
			}
		}
		//满足工作流接收条件的有权限的用户组织
		return getAuthFlowNodeGrpIds(appInfo.getAppId(), groupIds.toString(), userIds.toString());
	}
	/**
	 * 获取工作流上开始节点或第二个节点上有权限的所有组织编号集合
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-5-8 下午01:31:51
	* @param appId 应用编号
	* @param groupIds 组织编号，逗号隔开
	* @param userIds 用户编号，逗号隔开
	* @return List<Object> 组织编号集合
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAuthFlowNodeGrpIds(Long appId, String groupIds, String userIds) throws Exception{
		StringBuffer hql = new StringBuffer(" select distinct a.userId from AppUser a ,AppRoleSys s ,AppRoleUser r where ");
		hql.append(" a.userId=r.userId ");
		hql.append(" and s.roleId = r.roleId ");
		hql.append(" and s.appId=").append(appId)
			.append(" and (a.userId in(select g.userId from AppGrpuser g where g.groupId in(")
			.append(groupIds)
			.append(")) or a.userId in(")
			.append(userIds)
			.append("))");
		//System.out.println(hql.toString());
		List<Object> flowToUserIds = appBaseDao.find(hql.toString());
		return getGroupIdsByUserIds(CMyString.join((ArrayList)flowToUserIds, ","));
	}
	/**
	 * 获取应用下所有有访问权限的用户编号集合
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-5-8 下午01:34:28
	* @param appId 应用编号
	* @return List<Object> 用户编号集合
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getAuthUserIdsByApp(Long appId) throws Exception{
		StringBuffer hql = new StringBuffer(" select distinct a.userId from AppUser a ,AppRoleSys s ,AppRoleUser r where ");
		hql.append(" a.userId=r.userId ");
		hql.append(" and s.roleId = r.roleId ");
		hql.append(" and s.appId=").append(appId);
		List<Object> flowToUserIds = appBaseDao.find(hql.toString());
		return flowToUserIds;
	}
	/**
	 * 根据用户编号获取所在组织编号
	* Description: 根据用户编号获取所在组织编号 <BR>   
	* @author liu.zhuan
	* @date 2014-5-8 下午01:26:32
	* @param userIds 用户编号，用逗号隔开
	* @return List<Object> 组织编号集合
	* @version 1.0
	 */
	private List<Object> getGroupIdsByUserIds(String userIds){
		StringBuffer hql = new StringBuffer("select distinct a.groupId from AppGrpuser a where a.userId in (").append(userIds).append(")");
		List<Object> flowToGrpIds = appBaseDao.find(hql.toString());
		return flowToGrpIds;
	}
	/**
	 * 判断应用绑定的工作流用户是否有效
	 * Description:   判断应用绑定的工作流用户是否有效<BR>   
	 * @author liu.zhuan
	 * @date 2014-5-7 下午05:17:26
	 * @param appId 应用 编号
	 * @param flowId 工作流编号
	 * @return boolean true有效，false无效
	 * @throws Exception
	 * @version 1.0
	 */
	public boolean validateAppFlowBind(long appId, long flowId) throws Exception{
		//获取应用
		AppInfo appInfo = (AppInfo)appBaseDao.findById(AppInfo.class, appId);
		//long groupId = appInfo.getGroupId();
		//获取和应用绑定的组织和子组织
		List<String> groupIdList = new ArrayList<String>();
		groupIdList.add(appInfo.getGroupId().toString());
		if(appInfo.getIsSelGroup() == 1){
			String itemGroupIds = appInfo.getItemGroupId();
			String itemGrpArr[] = itemGroupIds.split("~");
			String itemGrpIdArr[] = null;
			for (String item : itemGrpArr) {
				itemGrpIdArr = item.split(":");
				if(!groupIdList.contains(itemGrpIdArr[0])){
					groupIdList.add(itemGrpIdArr[0]);
				}
			}
		}
		//System.out.println(groupIdList);
		//获取权限用户和组织
		List<Object> authUserIds = getAuthUserIdsByApp(appId);
		if(authUserIds == null || authUserIds.size() == 0){
			throw new Exception("应用没有设置有访问权限的用户！");
		}
		//获取工作流节点上的所有有权限用户和组织
		List<Object> authFlowGrpIds = getAuthFlowNodeGrpIds(appInfo, flowId);
		if(authFlowGrpIds.size() == 0){
			throw new Exception("工作流的开始节点上没有设置可访问此应用权限的用户,或者组织或节点上设置的用户或组织没有访问此应用的权限！");
		}
		//判断应用的数据接收组织是否在工作流中绑定，而且是有权限的组织。
		for (String groupId : groupIdList) {
			boolean isHas = false;
			for (Object object : authFlowGrpIds) {
				if(groupId.equals(object.toString())){
					isHas = true;
					break;
				}
			}
			if(!isHas){
				return false;
			}
		}
		return true;
	}
	/**
	 * 系统启动加载角色与应用关联关系
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-7-29 下午03:18:29
	* Last Modified:
	* @return List<Object> 所有角色和应用的关联数据
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getCacheRoleApp() throws Exception{
		List<Object> roleApps = find(null, AppRoleSys.class.getName(), null, null, null);
		return roleApps;
	}
	/**
	 * 系统启动加载角色与操作资源对应关系
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-7-29 下午03:21:03
	* Last Modified:
	* @return List<Object> 所有的应用角色操作表
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getCacheAppRoleOper() throws Exception{
		List<Object> approleApps = find(null, AppRoleOper.class.getName(), null, null, null);
		return  approleApps;
	}
}
