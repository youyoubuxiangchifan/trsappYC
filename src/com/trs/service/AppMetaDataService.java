package com.trs.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.AppMetaDataDao;
import com.trs.model.AppFieldInfo;
import com.trs.model.AppFieldRel;
import com.trs.model.AppInfo;
import com.trs.model.AppTableInfo;
import com.trs.model.AppUser;
import com.trs.model.AppViewInfo;
import com.trs.util.CMyString;
import com.trs.util.DateUtil;
import com.trs.util.FileUtil;
import com.trs.util.Global;

/**
 * Description: 用于操作元数据表信息<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppTableInfoService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-7 下午03:55:42
 * @version 1.0
 */
@Service
public class AppMetaDataService extends BaseService{
	@Autowired
	private AppMetaDataDao appMetaDataDao;
	private String getDbFieldSql(String dbType, String fieldType, int length, String fieldName){
		StringBuffer sql = new StringBuffer(fieldName);
		sql.append(" ")
		.append(fieldType);
		//sqlserver的int类型不可以设置长度，长度为0的不设置长度
		if(length != 0){
			if("SQLSERVER".equals(dbType) && "INT".equals(fieldType.toUpperCase())){
				return sql.toString();
			}else{
				sql.append("(").append(length);
				if("ORACLE".equals(dbType) && "VARCHAR2".equals(fieldType.toUpperCase())){
					sql.append(" char");
				}
				sql.append(")");
				//sql.append("(").append(length).append(")");
			}
		}
		return sql.toString();
	}
	private String buildCreateTableSql(String tableName,List<Object> sysPresetField, int tableType) throws Exception{
		// 获取数据库类型
		String dbType = Global.DB_TYPE.toUpperCase();//getDbType();
//		Map<String, String> dbFieldTypes = FileUtil.readProperties(AppMetaDataService.class.getClassLoader().getResourceAsStream("config/dbtype/DBFieldType.properties"));
		Properties dbFieldTypes = FileUtil.getProperties(AppMetaDataService.class.getClassLoader().getResourceAsStream("config/dbtype/DBFieldType.properties"));
		if (dbFieldTypes == null || dbFieldTypes.size() == 0) {
			throw new Exception("数据库字段类型列表为空！");
		}
		
		StringBuffer sql = new StringBuffer("create table ");
		sql.append(tableName).append("(")
		.append(getDbFieldSql(dbType, dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER).toString(), 10, "metadataid"));
		//.append("metadataid ")
		 //   .append(dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER)).append("(10)");
		if("SQLSERVER".equals(dbType)){
			sql.append(" identity (1,1)");
		} else if("MYSQL".equals(dbType)){
			sql.append(" auto_increment");
		}
		sql.append(" primary key,")
			.append(getDbFieldSql(dbType, dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER).toString(), 10, "APPID"))
			.append(",");
//			.append("APPID ")//应用分类
//			.append(dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER))
//			.append("(10),");
		if(tableType == 2){
			sql.append(getDbFieldSql(dbType, dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER).toString(), 10, "THEMEID"))
			.append(",");
//			sql.append("THEMEID ")//主表主键编号
//			.append(dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER))
//			.append("(10),");
		}
		if(sysPresetField != null && sysPresetField.size() > 0){
			AppFieldInfo sysPreField = null;
			for (Object object : sysPresetField) {
				sysPreField = (AppFieldInfo)object;
//				int fieldStyle = sysPreField.getFieldStyle() == null ? 0 : sysPreField.getFieldStyle();
//				if(tbType != 1 && fieldStyle == 2){
//					continue;
//				}
				//int fieldStyle = sysPreField.getFieldStyle() == null ? 0 : sysPreField.getFieldStyle();
				int sysFieldType = sysPreField.getSysFieldType() == null ? 0 : sysPreField.getSysFieldType();
				if(tableType == 0 && sysFieldType == 1){//表示通用类型不创建主题字段
					continue;
				}
				if(tableType == 1 && sysFieldType == 0){//表示主题类型不创建通用类型字段
					continue;
				}
				if(tableType == 2 && sysFieldType != -1){//表示意见类型不创建通用和主题
					continue;
				}
				int dbLength = (int)(sysPreField.getDblength() == null ? 0 : sysPreField.getDblength());
//				sql.append(sysPreField.getFieldName())
//					.append(" ")
				sql.append(getDbFieldSql(dbType, dbFieldTypes.get(dbType + "." + sysPreField.getFieldType()).toString(), dbLength, sysPreField.getFieldName()));
//					.append(dbFieldTypes.get(dbType + "." + sysPreField.getFieldType()));
//					if(sysPreField.getDblength() != null){
//						sql.append("(")
//						.append(sysPreField.getDblength())
//						.append(") ");
//					}
					if(!CMyString.isEmpty(sysPreField.getDefaultValue())){
						sql.append(" default ")
						.append(sysPreField.getDefaultValue());
					}
					sql.append(",");
			}
		}
		//创建数据推送标识字段
		sql.append(getDbFieldSql(dbType, dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER).toString(), 2, "syncflag"))
		.append(",")
		//内置wcm文档编号
			.append(getDbFieldSql(dbType, dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER).toString(), 10, "WCMDOCID"))
			.append(")");
//		sql.append("syncflag ")
//		.append(dbFieldTypes.get(dbType + "." + Global.FIELD_TYPE_NUMBER))
//		.append("(2))");
		//sql = new StringBuffer(sql.substring(0, sql.length() - 1)).append(")");
		return sql.toString();
	}
	/**
	 * 
	 * Description: 创建元数据表,保存表信息,创建表字段 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午02:49:03
	 * @param appTableInfo 元数据表对象
	 * @version 1.0
	 * @throws Exception
	 */
	public void createAppTable(AppTableInfo appTableInfo, AppUser user) throws Exception {
		// 获取数据库类型
		String dbType = Global.DB_TYPE.toUpperCase();//getDbType();
//		Map<String, String> dbFieldTypes = FileUtil.readProperties(AppMetaDataService.class.getClassLoader().getResourceAsStream("config/dbtype/DBFieldType.properties"));
		Properties dbFieldTypes = FileUtil.getProperties(AppMetaDataService.class.getClassLoader().getResourceAsStream("config/dbtype/DBFieldType.properties"));
		if (dbFieldTypes == null || dbFieldTypes.size() == 0) {
			throw new Exception("数据库字段类型列表为空！");
		}
		// 创建表
		String dbTableName = Global.DB_TABLE_PREFIX + appTableInfo.getTableName().trim();
		//判断表是否存在
		if(existData(AppTableInfo.class.getName(), "tableName = ?", dbTableName)){
			throw new Exception("新建的元数据表在数据库中已存在！");
		}
		int tableType = appTableInfo.getTableType() == null ? 0 : appTableInfo.getTableType();
		//int nUseSysField = appTableInfo.getUseSysField();
		//新建表,0为通用类型，1为主题类型
		//添加系统预留字段，从元数据字段信息表中读取表id为-1的字段列表，如果表中不存在则代码中添加
		List<Object> sysPresetField = appMetaDataDao.find("from AppFieldInfo where tableId = -1");// and fieldStyle != 2
		Long itemTableId = null;
		String itemTableName = null;
		if(tableType == 0){
//			if(nUseSysField == 1){
//				sysPresetField = appMetaDataDao.find("from AppFieldInfo where tableId in(-1,0)");
//			}
			String sql = buildCreateTableSql(dbTableName, sysPresetField, 0);//通用类型sql
			//System.out.println("sql:"+sql);
			try {
				appMetaDataDao.executeBaseSql(sql.toString());
			} catch (SQLException e) {
				throw e;
			}
		} else {
			String sql = buildCreateTableSql(dbTableName, sysPresetField, 1);//主题sql
			itemTableName = dbTableName + "item";
			String itemTbSql = buildCreateTableSql(itemTableName, sysPresetField, 2);//附表sql
			//System.out.println("sql:"+sql);
			//System.out.println("itemTbSql:"+itemTbSql);
			try {
				appMetaDataDao.executeBaseSql(sql, itemTbSql);
			} catch (SQLException e) {
				throw e;
			}
			// 保存附表元数据表信息
			AppTableInfo itemTableInfo = new AppTableInfo();
			itemTableInfo.setTableName(itemTableName);
			itemTableInfo.setCruser(user.getUsername());
			itemTableInfo.setTableType(-1);
			appMetaDataDao.save(itemTableInfo);
			itemTableId = itemTableInfo.getTableInfoId();
			appTableInfo.setItemTableId(itemTableId);
			appTableInfo.setItemTableName(itemTableName);
		}
		// 保存元数据表信息
		appTableInfo.setCruser(user.getUsername());
		appTableInfo.setTableName(dbTableName);
		appMetaDataDao.save(appTableInfo);
		
		List<Object> sysFieldList = new ArrayList<Object>();
		if(sysPresetField != null && sysPresetField.size() > 0){
			AppFieldInfo field = null;
			AppFieldInfo sysField = null;
			for (Object object : sysPresetField) {
				field = (AppFieldInfo)object;
				//int fieldStyle = field.getFieldStyle() == null ? 0 : field.getFieldStyle();
				int sysFieldType = field.getSysFieldType() == null ? 0 : field.getSysFieldType();
				if(tableType == 0 && sysFieldType == 1){//表示通用类型不创建主题字段
					continue;
				}
				if(tableType == 1 && sysFieldType == 0){//表示主题类型不创建通用类型字段
					continue;
				}
				sysField = new AppFieldInfo(null, appTableInfo.getTableInfoId(), appTableInfo.getTableName(), field.getFieldName(), field.getFieldDesc(), field.getFieldType(), field.getDbtype(), field.getFieldLength(), field.getFieldStyle(), field.getDblength(), field.getDbscale(), field.getDefaultValue(), field.getEnmValue(), field.getNotNull(), field.getNotEdit(), field.getHiddenField(), field.getIsReserved(), user.getUsername(), DateUtil.now(), field.getSysFieldType(), field.getFormFieldType());
				if(tableType == 1 && itemTableId != null && sysFieldType == -1){//表示意见类型需创建公共字段
					sysFieldList.add(new AppFieldInfo(null, itemTableId, itemTableName, field.getFieldName(), field.getFieldDesc(), field.getFieldType(), field.getDbtype(), field.getFieldLength(), field.getFieldStyle(), field.getDblength(), field.getDbscale(), field.getDefaultValue(), field.getEnmValue(), field.getNotNull(), field.getNotEdit(), field.getHiddenField(), field.getIsReserved(), user.getUsername(), DateUtil.now(), field.getSysFieldType(), field.getFormFieldType()));
				}
				sysFieldList.add(sysField);
			}
		}
//		AppFieldInfo sysField = null;
//		if (nUseSysField == 1) {// 标识为1，创建表时添加预留字段
//			List<Object> fieldList = appMetaDataDao.find("from AppFieldInfo where tableId = 0");
//			if (fieldList != null && fieldList.size() > 0) {
//				AppFieldInfo field = null;
//				for (int i = 0; i < fieldList.size(); i++) {
//					//更新字段信息
//					field = (AppFieldInfo) fieldList.get(i);
//					sysField = new AppFieldInfo(null, appTableInfo.getTableInfoId(), appTableInfo.getTableName(), field.getFieldName(), field.getFieldDesc(), field.getFieldType(), field.getDbtype(), field.getFieldLength(), field.getFieldStyle(), field.getDblength(), field.getDbscale(), field.getDefaultValue(), field.getEnmValue(), field.getNotNull(), field.getNotEdit(), field.getHiddenField(), field.getIsReserved(), user.getUsername(), DateUtil.now());
//					sysFieldList.add(sysField);
//					String dbFieldType = (String)dbFieldTypes.get(dbType + "."
//							+ field.getFieldType());
//					StringBuffer sql = new StringBuffer("alter table ");
//					sql.append(appTableInfo.getTableName()).append(" add ").append(
//							field.getFieldName()).append(" ").append(dbFieldType);
//					Long dbLength = field.getDblength();// 字段长度
//					if (dbLength != null && dbLength != 0) {
//						sql.append("(").append(dbLength).append(")");
//					}
//					appMetaDataDao.executeBaseSql(sql.toString());
//				}
//			}
//		}
		appMetaDataDao.saveOrUpdateAll(sysFieldList);
	}
	
	/**
	 * 
	 * Description: 用于更新元数据表信息 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午03:06:15
	 * @param appTableInfo 元数据表对象
	 * @return
	 * @version 1.0
	 */
	public void updateAppTable(AppTableInfo appTableInfo) {
		appMetaDataDao.update(appTableInfo);
	}

	/**
	 * 
	 * Description: 用于删除元数据表信息，删除元数据表 ，删除表之间关联关系<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午03:06:15
	 * @param id 元数据编号
	 * @return
	 * @version 1.0
	 * @throws Exception 
	 */
	public void deleteAppTable(long id) throws Exception {
		AppTableInfo tableInfo = (AppTableInfo) appMetaDataDao.findById(
				AppTableInfo.class, id);
		if(tableInfo == null){
			throw new Exception("编号为" + id + "的元数据没有找到！");
		}
		try {
			StringBuffer sTableIds = new StringBuffer();
			//查询该表是否有子表，如果有，删除关联关系
			if(tableInfo.getTableType() == Global.METADATA_TYPE_OPINION){
				//删除子表
				AppTableInfo itemTableInfo = (AppTableInfo)findById(AppTableInfo.class, tableInfo.getItemTableId());
				if(itemTableInfo != null){
					sTableIds.append(tableInfo.getItemTableId()).append(",");
					appMetaDataDao.delete(itemTableInfo);
					appMetaDataDao.executeBaseSql("drop table " + itemTableInfo.getTableName());
				}
			}
			String tbName = tableInfo.getTableName();
			//删除字段信息表中的字段信息
			sTableIds.append(tableInfo.getTableInfoId());
			List<Object> fieldList = findByIds(AppFieldInfo.class.getName(), "tableId", sTableIds.toString());
			if(fieldList != null && fieldList.size() > 0)
				appMetaDataDao.deleteAll(fieldList);
			//清空字段关系表中对应的字段关联
			List<Object> fieldRelList = findByIds(AppFieldRel.class.getName(), "mainTableId", sTableIds.toString());
			if(fieldRelList != null && fieldRelList.size() > 0)
				appMetaDataDao.deleteAll(fieldRelList);
			//删除表信息
			appMetaDataDao.delete(tableInfo);
			
			//清空所有应用的主表从表
			List<Object> appInfoList = findByIds(AppInfo.class.getName(), "mainTableId", String.valueOf(id));
			AppInfo appInfo = null;
			for (Object object : appInfoList) {
				appInfo = (AppInfo)object;
				appInfo.setMainTableId(null);
				appInfo.setMainTableName(null);
				appInfo.setItemTableId(null);
				appInfo.setItemTableName(null);
			}
			appMetaDataDao.saveOrUpdateAll(appInfoList);
			//清空所有视图的主表从表
			List<Object> viewInfoList = findByIds(AppViewInfo.class.getName(), "mainTableId", String.valueOf(id));
			AppViewInfo viewInfo = null;
			for (Object object : viewInfoList) {
				viewInfo = (AppViewInfo)object;
				viewInfo.setMainTableId(null);
				viewInfo.setMainTableName(null);
				viewInfo.setItemTableId(null);
				viewInfo.setItemTableName(null);
			}
			appMetaDataDao.saveOrUpdateAll(viewInfoList);
			//删除表
			appMetaDataDao.executeBaseSql("drop table " + tbName);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * Description: 添加元数据表字段信息，添加表字段 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午03:07:51
	 * @param appFieldInfo 元数据字段对象
	 * @version 1.0
	 * @throws Exception
	 */
	public void addTableField(AppFieldInfo appFieldInfo) throws Exception {
		//判断表字段是否存在
		List<Object> params = new ArrayList<Object>();
		params.add(appFieldInfo.getTableId());
		params.add(appFieldInfo.getFieldName());
		if(existData(AppFieldInfo.class.getName(), "tableId = ? and fieldName = ?", params)){
			throw new Exception("添加失败！数据库表中已存在要添加的字段！");
		}
		if(isDBKeyWords(appFieldInfo.getFieldName().toUpperCase())){
			//System.out.println(appFieldInfo.getFieldName().toUpperCase());
			throw new Exception("添加失败！要添加的字段为数据库保留字段！");
		}
		//alter table APP_FIELD_INFO add  test2 varchar(200) default 'aaaaa'
		String dbType = Global.DB_TYPE.toUpperCase();//getDbType();
		//Map<String, String> dbFieldTypes = FileUtil.readProperties(AppMetaDataService.class.getClassLoader().getResourceAsStream("config/dbtype/DBFieldType.properties"));
		Properties dbFieldTypes = FileUtil.getProperties(AppMetaDataService.class.getClassLoader().getResourceAsStream("config/dbtype/DBFieldType.properties"));
		if (dbFieldTypes == null || dbFieldTypes.size() == 0) {
			throw new Exception("数据库字段类型列表为空！");
		}
		// 获取字段类型
		String fieldType = appFieldInfo.getFieldType();
		if(fieldType == null){
			throw new Exception("要新建的字段类型为空！");
		}
		String dbFieldType = (String)dbFieldTypes.get(dbType + "."
				+ fieldType);
		StringBuffer sql = new StringBuffer("alter table ");
		sql.append(appFieldInfo.getTableName()).append(" add ").append(
				appFieldInfo.getFieldName()).append(" ").append(dbFieldType);
		Long dbLength = appFieldInfo.getDblength();// 字段长度
		if (dbLength != null && dbLength != 0) {
			if(!("SQLSERVER".equals(dbType) && "INT".equals(dbFieldType.toUpperCase()))){
				sql.append("(").append(dbLength);
				if("ORACLE".equals(dbType) && "VARCHAR2".equals(appFieldInfo.getDbtype())){
					sql.append(" char");
				}
				sql.append(")");
			}
		} else {
			if(!fieldType.equals(Global.FIELD_TYPE_EDITOR) && !fieldType.equals(Global.FIELD_TYPE_SMEDITOR) && !fieldType.equals(Global.FIELD_TYPE_DATE)){
				sql.append("(200)");
				//appFieldInfo.setFieldLength(new Long(100));
				appFieldInfo.setDblength(new Long(200));
			}
		}
		//由于加上默认值后字段删除不了
//		String defaultValue = appFieldInfo.getDefaultValue();// 默认值
//		if (!CMyString.isEmpty(defaultValue)) {
//			sql.append(" default ").append(defaultValue);
//		}
		//System.out.println(sql.toString());
		appMetaDataDao.executeBaseSql(sql.toString());
		appFieldInfo.setDbtype(dbFieldType);
		appFieldInfo.setFieldName(appFieldInfo.getFieldName().toUpperCase());
		appMetaDataDao.save(appFieldInfo);
	}

	/**
	 * 
	 * Description: 更新元数据表字段信息，更新表字段<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午03:09:39
	 * @param appFieldInfo 元数据字段对象
	 * @version 1.0
	 * @throws SQLException 
	 */
	public void updateTableField(AppFieldInfo appFieldInfo) throws SQLException {
		//alter table app_field_info modify  test1 varchar(500)
		String dbType = Global.DB_TYPE.toUpperCase();
		StringBuffer sql = new StringBuffer("alter table ");
		sql.append(appFieldInfo.getTableName());
		if(dbType.equals("SQLSERVER")){
			sql.append(" alter column ");
		} else {
			sql.append(" modify ");
		}
		sql.append(appFieldInfo.getFieldName()).append(" ").append(appFieldInfo.getDbtype());
		Long dbLength = appFieldInfo.getDblength();// 字段长度
		if (dbLength != null) {
			if(!("SQLSERVER".equals(dbType) && "INT".equals(appFieldInfo.getDbtype()))){
				sql.append("(").append(dbLength);
				if("ORACLE".equals(dbType) && "VARCHAR2".equals(appFieldInfo.getDbtype())){
					sql.append(" char");
				}
				sql.append(")");
			}
			//sql.append("(").append(dbLength.toString()).append(")");
		}
		//由于加上默认值后字段删除不了
//		String defaultValue = appFieldInfo.getDefaultValue();// 默认值
//		if (!CMyString.isEmpty(defaultValue)) {
//			sql.append(" default ").append(defaultValue);
//		}
		//System.out.println(sql.toString());
		//执行修改字段sql语句
		if(!Global.FIELD_TYPE_SMEDITOR.equals(appFieldInfo.getFieldType().toLowerCase()) && !Global.FIELD_TYPE_EDITOR.equals(appFieldInfo.getFieldType().toLowerCase())){
			appMetaDataDao.executeBaseSql(sql.toString());
		}
		//执行更新字段信息
		appMetaDataDao.update(appFieldInfo);
	}

	/**
	 * 
	 * Description: 删除元数据表字段信息，表字段 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午03:10:45
	 * @param id 元数据字段编号
	 * @version 1.0
	 * @throws Exception 
	 */
	public void deleteTableField(Long id) throws Exception {
		//alter table test drop column test;
		AppFieldInfo appFieldInfo = (AppFieldInfo)appMetaDataDao.findById(AppFieldInfo.class, id);
		StringBuffer sql = new StringBuffer("alter table ");
		sql.append(appFieldInfo.getTableName()).append(" drop column ").append(
				appFieldInfo.getFieldName());
		//System.out.println(sql.toString());
		//删除字段信息
		appMetaDataDao.delete(appFieldInfo);
		//删除字段关联
		List<Object> fieldRelList = findByIds(AppFieldRel.class.getName(), "fieldId", id.toString());
		appMetaDataDao.deleteAll(fieldRelList);
		//执行删除表字段sql
		appMetaDataDao.executeBaseSql(sql.toString());
	}
	/**
	 * Description: 用于判断字段名是否为关键字 <BR>   
	 * @author liu.zhuan
	 * @date 2014-3-19 上午10:09:42
	 * @param fieldName
	 * @return boolean
	 * @throws IOException
	 * @version 1.0
	 */
	public boolean isDBKeyWords(String fieldName) throws IOException{
		Properties properties = FileUtil.getProperties(AppMetaDataService.class.getClassLoader().getResourceAsStream("config/dbtype/DBKeyWords.properties"));
		if(properties == null){
			throw new IOException("读取DBFieldType.properties失败！");
		}
		return properties.containsKey(fieldName);
	}
}
