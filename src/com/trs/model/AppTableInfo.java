package com.trs.model;

import java.util.Date;

import com.trs.util.Global;

/**
 * AppTableInfo entity. @author MyEclipse Persistence Tools
 */

public class AppTableInfo implements java.io.Serializable {

	// Fields

	private Long tableInfoId;
	private String tableName;
	private String anotherName;
	private String tableDesc;
	private Integer tableType;
	private Integer useSysField;
	private Long itemTableId;
	private String itemTableName;
	private String cruser;
	private Date crtime;
	public Long getTableInfoId() {
		return tableInfoId;
	}
	public void setTableInfoId(Long tableInfoId) {
		this.tableInfoId = tableInfoId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getAnotherName() {
		return anotherName;
	}
	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}
	public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	public Integer getTableType() {
		return tableType;
	}
	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}
	public Integer getUseSysField() {
		return useSysField;
	}
	public void setUseSysField(Integer useSysField) {
		this.useSysField = useSysField;
	}
	public Long getItemTableId() {
		return itemTableId;
	}
	public void setItemTableId(Long itemTableId) {
		this.itemTableId = itemTableId;
	}
	public String getItemTableName() {
		return itemTableName;
	}
	public void setItemTableName(String itemTableName) {
		this.itemTableName = itemTableName;
	}
	public String getCruser() {
		return cruser;
	}
	public void setCruser(String cruser) {
		this.cruser = cruser;
	}
	public Date getCrtime() {
		return crtime;
	}
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}
	/** 
	* Description: TODO<BR> 
	*/ 
	public AppTableInfo() {
		super();
		this.crtime = new Date(System.currentTimeMillis());
		// TODO Auto-generated constructor stub
	}
	/** 
	* Description: TODO<BR>
	* @param tableInfoId
	* @param tableName
	* @param anotherName
	* @param tableDesc
	* @param tableType
	* @param useSysField
	* @param itemTableId
	* @param itemTableName
	* @param cruser
	* @param crtime 
	*/ 
	public AppTableInfo(Long tableInfoId, String tableName, String anotherName,
			String tableDesc, Integer tableType, Integer useSysField,
			Long itemTableId, String itemTableName, String cruser, Date crtime) {
		super();
		this.tableInfoId = tableInfoId;
		this.tableName = tableName;
		this.anotherName = anotherName;
		this.tableDesc = tableDesc;
		this.tableType = tableType;
		this.useSysField = useSysField;
		this.itemTableId = itemTableId;
		this.itemTableName = itemTableName;
		this.cruser = cruser;
		this.crtime = crtime;
	}
	
}