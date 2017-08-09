package com.trs.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trs.dao.AppWebDao;
import com.trs.dbhibernate.ChangeDBType;
import com.trs.dbhibernate.Page;
import com.trs.model.AppAppendix;
import com.trs.model.AppFieldRel;
import com.trs.model.AppInfo;
import com.trs.model.AppSysConfig;
import com.trs.util.CMyString;
import com.trs.util.Global;

/**
 * 前台通用服务层
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: AppWebService
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-28 上午10:28:36
 * @version 1.0
 */
@Service
public class AppWebService extends BaseService {
	private static  Logger LOG =  Logger.getLogger(AppWebService.class);
	@Autowired
	private AppWebDao appWebDao;
	/**
	 * 
	* Description:根据查询编号和密码查询数据信息 <BR>   
	* @author zhangzun
	* @date 2014-4-21 下午05:30:52
	* @param appId  	应用id
	* @param querynumber 数据查询编号
	* @param querypwd	 数据查询密码
	* @return   数据的键值
	* @version 1.0
	 * @throws Exception 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getAppDataByPassCode(Long appId,String querynumber,String querypwd) throws SQLException, Exception{
		Map<String,Object> map= null;
		AppInfo appInfo = (AppInfo) findById(AppInfo.class,appId);
		if(appInfo==null)
			return null;
		String tableName = appInfo.getMainTableName();
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("select from ");
		sql.append(tableName);
		sql.append(" where ");
		sql.append(" APPID=? and QUERYNUMBER=? and QUERYPWD=? ");
		params.add(appId);
		params.add(querynumber);
		params.add(querypwd);
			List<Object> list = appWebDao.executeQueryObjs(sql.toString(), params);
			if(list!=null && list.size()>0){
				map = (Map<String,Object>)list.get(0);
			}
		
		return map;
	}
	/**
	 * 返回单条原数据详细信息
	* Description:根据元数据id查询 <BR>   
	* @author zhangzun
	* @date 2014-4-21 下午06:46:02
	* @param appId 应用id
	* @param metadataId 信息编号
	* @param mainTable  主从表，0 是主表，1从表
	* @return Map<String,Object> 
	* @throws SQLException
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getAppDataById(Long appId,Long metadataId,int mainTable) throws SQLException, Exception{
		Map<String,Object> map= null;
		String tableName = getMetaTableName(appId, mainTable);
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("select from ");
		sql.append(tableName);
		sql.append(" where ");
		sql.append(" APPID=? and METADATAID=?");
		params.add(appId);
		params.add(metadataId);
		List<Object> list = appWebDao.executeQueryObjs(sql.toString(), params);
		if(list!=null && list.size()>0){
			map = (Map<String,Object>)list.get(0);
		}
		return map;
	}
	/**
	 * 返回应用对应的元数据表名
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 下午04:44:41
	* Last Modified:
	* @param appId 应用ID
	* @param mainTable 主从表，0 是主表，1从表
	* @return String 元数据表名称
	* @throws Exception
	* @version 1.0
	 */
	public String getMetaTableName(Long appId,int mainTable) throws Exception{
		List<Object> params = new ArrayList<Object>();
		//查询应用的表
		String field = "mainTableName";
		if(mainTable==1){ //1 返回从表表名
			 field = "itemTableName";
		}
		params.add(appId);
		//视图对应的字段信息表查询对应的表名
		List<Object> appInfos = find(field, AppInfo.class.getName(), "appId=?", null, params);
		if(appInfos.size()<1){
			throw new Exception("元数据表名不存在");
		}		
		String tableName = (String)appInfos.get(0); 
		if(CMyString.isEmpty(tableName)|| tableName.equals("null")){
			throw new Exception("元数据表名称不能为空或者空字符串!");
		}
		return tableName;
	}
    /**
     * 
    * Description:根据字段的所属类别查询应用的字段信息 <BR>   
    * @author zhangzun
    * @date 2014-4-24 下午03:06:49
    * @param viewId     视图id
    * @param mainTableId 元数据表id
    * @param fieldStyle 字段的所属类别 0:提交类型,1:回复类型,2:主题类型
    * @return List<Object>  应用的字段信息
    * @throws Exception
    * @version 1.0
     */
	public List<Object> getAppFields(Long viewId,Long mainTableId,int fieldStyle) throws Exception{
		if(viewId==null || mainTableId==null)
			throw new Exception("传入的参数 不合法。");
		//查询应用字段信息
		List<Object> list = null;
		String sWhere = "viewId = ? and mainTableId = ? and fieldStyle=?";
		List<Object> param = new ArrayList<Object>();
		param.add(viewId);
		param.add(mainTableId);
		param.add(fieldStyle);
		list = find("", AppFieldRel.class.getName(), sWhere, "fieldOrder asc", param);
		return list;
	}
	/** 
	* Description: 根据组织编号查询出组织对应的应用 <BR>   
	* author liujian
	* date 2014-4-24 下午04:20:25
	* @param groupId 组织编号
	* @return 返回组织对应的应用List
	* @throws Exception
	* version 1.0
	*/
	public List<Object> getApps(Long groupId) throws Exception{
		if(groupId==null)
			throw new Exception("传入的参数 不合法。");
		//查询应用字段信息
		String sWhere = "groupId = ? and deleted=? and appStatus=0";
		List<Object> params = new ArrayList<Object>();
		List<Object> appList = null;
		params.add(Long.valueOf(groupId));
		params.add(0);
		appList = find("", AppInfo.class.getName(), sWhere,
				"crtime desc", params);
		
		return appList;
	}
	/**
	 * 查询应用的字段,新建一条应用信息时也调用此方法，第二个(inOutline)参数为-1 即可获取所有的字段集合
	* Description:  app_field_rel 该对象中查询<BR>  
	* @author liujian
	* @date Create: 2014-4-25 下午11:18:56
	* Last Modified:
	* @param viewId 应用的视图ID
	* @param inOutline 0 是否概览显示，-1查询所有字段  2 查询检索字段 3查询标题字段4前台细览字段
	* @param tableName 表名
	* @return List<Object> 返回应用字段对象<AppFieldRel>集合,每个字段一条记录
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> queryAppFields(Long viewId,int inOutline,String tableName) throws Exception{
		if(viewId<1){
			throw new Exception("视图ID不能小于等于0");
		}		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sSql = new StringBuffer("viewId=?").append(" and mainTableName=?");
		params.add(viewId);
		params.add(tableName);
		if(inOutline==1){
			params.add(inOutline);//是否可概览显示,需要根据系统数据字典参数中定义"inOutline"
			sSql.append(" and inOutline =?");
		}if(inOutline==2){
			sSql.append(" and searchField=1");
		}if(inOutline==3){
			params.add(1); //查询标题字段
			sSql.append(" and titleField=?");
		}
		if(inOutline==4){
			params.add(1); //查询细览字段字段
			sSql.append(" and isWebShow=?");
		}
		List<Object> appFieldRels = find(null, AppFieldRel.class.getName(), sSql.toString(), " fieldOrder asc ", params);
		return appFieldRels;
	}
	/**
	 * 查询应用的字段,服务于查询从表
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-28 下午06:28:56
	* Last Modified:
	* @param viewId 试图编号
	* @param tableName 表名
	* @param sWhere where条件
	* @param parameters
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> queryAppFields(Long viewId,String tableName,String sWhere,List<Object> parameters) throws Exception{
		if(viewId<1){
			throw new Exception("视图ID不能小于等于0");
		}		
		StringBuffer sSql = new StringBuffer("viewId=?").append(" and mainTableName=?");
		parameters.add(0,viewId);
		parameters.add(1,tableName);
		if(CMyString.isEmpty(sWhere)){
			throw new Exception("查询条件不能为空");
		}
		sSql.append(" and ").append(sWhere);
		List<Object> appFieldRels = find(null, AppFieldRel.class.getName(), sSql.toString(), " fieldOrder asc ", parameters);
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
		if(appinfo.get("metadataid")!=null){
			//更新操作
			sSql.append("update ").append(tableName).append(" set ");
			for (String key : appinfo.keySet()) {
				sFileds.append(key).append("=?").append(",");
				parameters.add(appinfo.get(key));
			}
			//更新字段
			sSql.append(sFileds.toString().substring(0,sFileds.length()-1));
			//条件
			sSql.append(" where metadataid=?");
			parameters.add(appinfo.get("metadataid"));//元数据默认ID 字段
		}else{
			//新增操作
			sSql.append("insert into ").append(tableName).append("(");
			for (String key : appinfo.keySet()) {
				sFileds.append(key).append(",");
				params.append("?").append(",");
				parameters.add(appinfo.get(key));
			}
			if(Global.DB_TYPE.equals("oracle"))
				sSql.append(" metadataid,");
			sSql.append(sFileds.toString().substring(0,sFileds.length()-1)).append(")").append(" values(");
			if(Global.DB_TYPE.equals("oracle"))
				sSql.append(" HIBERNATE_SEQUENCE.nextval, ");
			sSql.append(params.toString().substring(0,params.length()-1)).append(")");
		}
			
		//TODO 仅限于oracle 使用序列
		//日志
		LOG.debug(sSql.toString());
		Long metadataId = appWebDao.saveBaseSql(sSql.toString(), parameters);
		return metadataId;
		
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
	public Page queryAppMetadatas(String tableName,String sFields ,String sWhere,List<Object> parameters,String sOrder,int nStartPage, int nPageSize) throws Exception{
		if(CMyString.isEmpty(sFields)){
			throw new Exception("概览字段不能为空");
		}
			//总记录数原生SQL
			String countSql = ChangeDBType.SqlCountRecord(tableName, sWhere);
			//查询总记录数
			int totalResults = appWebDao.executeQueryInt(countSql,parameters);
			Page page = new Page(nStartPage, nPageSize, totalResults);
			// page = new Page(nStartPage, nPageSize, 20);
			// page.setTotalResults(totalResults);
			String sSql = ChangeDBType.changeDbTypeSql(sFields, tableName, sWhere, sOrder, page.getStartIndex(), page.getPageSize()*page.getStartPage());
			//sql
			LOG.debug(sSql);
			List<Object> dataList = appWebDao.executeQueryObjsDateFormat(sSql,parameters, "yyyy-MM-dd");
			page.setLdata(dataList);
		
		return page;
	}
	/**
	 * 返回应用(元数据)的单条信息 <原生SQL>
	* Description:返回所有信息<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 下午04:33:01
	* Last Modified:
	* @param metadataid 数据编号
	* @param tableName 表名称   数据库表名称
	* @return Map<String,Object> 
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAppMetadata(Long metadataid,String tableName) throws Exception{
		StringBuffer sql = new StringBuffer("select * from ");
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(metadataid);
		//元数据表名称
		sql.append(tableName);
		sql.append(" where ").append(" metadataid=?");
		Map<String, Object> obj = (Map<String, Object>)appWebDao.executeQueryObj(sql.toString(),parameters);
		return obj;	
	}
	/**
	 * 按照字段和检索条件返回应用(元数据)的单条信息 <原生SQL> 带?占位符的
	* Description:不支持模糊检索,返回部分数据 <BR>  
	* @author wen.junhui
	* @date Create: 2014-4-28 下午02:01:19
	* Last Modified:
	* @param tableName 表名称
	* @param fileds 要查询的字段集合  ,已逗号分隔
	* @param sWhere 检索where条件,占位符，不需要where关键字， 首尾不需要and 或者or 格式如：ispublic=? and deleted=?
	* @param parameters  检索值,必须和swhere匹配，否则会报错
	* @return Map<String, Object> 返回map集合，key为字段名，value为字段的值
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAppMetadata(String tableName,String fileds,String sWhere,List<Object> parameters) throws Exception{
		StringBuffer sql = new StringBuffer("select ").append(fileds).append(" from ");
		if(CMyString.isEmpty(tableName)){
			throw new Exception("查询的表名不能为空！");
		}
		if(CMyString.isEmpty(fileds)){
			throw new Exception("查询的字段不能为空！");
		}
		
		//元数据表名称
		sql.append(tableName);
		sql.append(" where ");
		if(!CMyString.isEmpty(sWhere)&&parameters!=null){
			sql.append(sWhere).append(" ");
//			parameters.add(0,metadataid);
		}else{
//			parameters = new ArrayList<Object>();
//			parameters.add(metadataid);
			throw new Exception("查询条件不能为空！");
		}
		Map<String, Object> obj = (Map<String, Object>)appWebDao.executeQueryObj(sql.toString(),parameters);
		return obj;	
	}
	/**
	 * 
	* Description:更新应用数据的点击量<BR>   
	* @author zhangzun
	* @date 2014-4-30 下午03:38:17
	* @param tableName  元数据表名
	* @param dataId     元数据id
	* @throws Exception
	* @version 1.0
	 */
	public void addHitCount(String tableName,Long dataId) throws Exception{
		List<Object> params = new ArrayList<Object>();
		params.add(dataId);
		Map<String, Object> metaData = getAppMetadata(tableName,"HITCOUNTS","METADATAID=?",params);
		Integer hitConut =  Integer.valueOf(CMyString.showEmpty((String)metaData.get("HITCOUNTS"), "0"));
		Map<String,Object> appinfo = new HashMap<String,Object>();
		appinfo.put("metadataid", dataId);
		appinfo.put("HITCOUNTS", hitConut+1);
		saveOrUpdateMetadata(appinfo,tableName);
	}
	/**
	 * 
	* Description:获取应用数据的附件<BR>   
	* @author zhangzun
	* @date 2014-4-30 下午04:13:29
	* @param appId   // 应用id
	* @param dataId  //应用数据id
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getAppendixs(Long appId,Long metadataId) throws Exception{
		if(appId==null || metadataId==null)
			throw new Exception("传入的参数有误！");
		List<Object> list = null;
		String sWhere = "appId=? and metadataId=?";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(appId);
		parameters.add(metadataId);
		list = this.find("", AppAppendix.class.getName(), sWhere, "appendixId desc", parameters);
		return list;
	}
	
}
