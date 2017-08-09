package com.trs.model;

import java.util.HashMap;

import com.trs.util.CMyDateTime;

/**
 * Description: 用于封装元数据对象信息<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: MetaData
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-8 下午02:44:42
 * @version 1.0
 */
public class MetaData {
	private String dbTableName;
	private int id;
	private int groupId;
	private String crUser;
	private HashMap<String, Object> properties;

	/**
	 * <p>
	 * Description: 默认构造函数<BR>
	 */
	public MetaData() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>
	 * Description: 构造一个元数据对象<BR>
	 * 
	 * @param dbTableName
	 * @param id
	 * @param groupId
	 * @param crUser
	 * @param metaData
	 */
	public MetaData(String dbTableName, int id, int groupId, String crUser,
			HashMap<String, Object> properties) {
		super();
		this.dbTableName = dbTableName;
		this.id = id;
		this.groupId = groupId;
		this.crUser = crUser;
		this.properties = properties;
	}

	/**
	 * @return dbTableName
	 */
	public String getDbTableName() {
		return dbTableName;
	}

	/**
	 * 设置属性： dbTableName
	 * 
	 * @param dbTableName
	 */
	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置属性： id
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return groupId
	 */
	public int getGroupId() {
		return groupId;
	}

	/**
	 * 设置属性： groupId
	 * 
	 * @param groupId
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return crUser
	 */
	public String getCrUser() {
		return crUser;
	}

	/**
	 * 设置属性： crUser
	 * 
	 * @param crUser
	 */
	public void setCrUser(String crUser) {
		this.crUser = crUser;
	}

	/**
	 * @return metaData
	 */
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	/**
	 * 设置属性： metaData
	 * 
	 * @param metaData
	 */
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	public Object getProperty(String paramString) {
		if ((paramString == null) || (getProperties() == null)) {
			return null;
		}
		paramString = paramString.toUpperCase();
		return properties.get(paramString);
	}

	public int getPropertyAsInt(String paramString, int paramInt) {
		Object localObject = getProperty(paramString);
		if (isNullValue(localObject))
			return paramInt;
		if ((localObject instanceof Number)) {
			return ((Number) localObject).intValue();
		}
		return Integer.parseInt(localObject.toString());
	}

	public String getPropertyAsString(String _sName) {
		Object objValue = getProperty(_sName);
		if (objValue == null)
			return null;
		return objValue.toString();
	}

	public String getPropertyAsString(String fieldName, String defaultValue) {
		Object localObject = getProperty(fieldName);
		if (localObject == null) {
			return defaultValue;
		}
		// 判断是否为时间类型
		/*
		 * if (((localObject instanceof Date)) && (((CMyDateTime)
		 * localObject).isNull())) { return paramString2; }
		 */

		return localObject.toString();
	}

	public boolean containsProperty(String paramString) {
		if ((paramString == null) || (getProperties() == null)) {
			return false;
		}
		return getProperties().containsKey(paramString.toUpperCase());
	}

	public CMyDateTime getPropertyAsDateTime(String paramString) {
		Object localObject = getProperty(paramString);
		if ((localObject instanceof CMyDateTime)) {
			return (CMyDateTime) localObject;
		}
		CMyDateTime localCMyDateTime = new CMyDateTime();
		if (isNullValue(localObject))
			return localCMyDateTime;
		try {
			localCMyDateTime.setDateTimeWithString(localObject.toString());
			return localCMyDateTime;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new CMyDateTime();
	}

	private boolean isNullValue(Object paramObject) {
		return (paramObject == null);
	}
}
