package com.trs.model;

import java.util.Date;


/**
 * AppFieldInfo entity. @author MyEclipse Persistence Tools
 */

public class AppFieldInfo  implements java.io.Serializable {


    // Fields    

     private Long fieldId;
     private Long tableId;
     private String tableName;
     private String fieldName;
     private String fieldDesc;
     private String fieldType;
     private String dbtype;
     private Long fieldLength;
     private Integer fieldStyle;
     private Long dblength;
     private Long dbscale;
     private String defaultValue;
     private String enmValue;
     private Integer notNull;
     private Integer notEdit;
     private Integer hiddenField;
     private Integer isReserved;
     private String cruser;
     private Date crtime;
     private Integer sysFieldType;
     private String formFieldType;

    // Constructors

    /** default constructor */
    public AppFieldInfo() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppFieldInfo(Long fieldId) {
        this.fieldId = fieldId;
    }

	

	/** 
	* Description: TODO<BR>
	* @param fieldId
	* @param tableId
	* @param tableName
	* @param fieldName
	* @param fieldDesc
	* @param fieldType
	* @param dbtype
	* @param fieldLength
	* @param fieldStyle
	* @param dblength
	* @param dbscale
	* @param defaultValue
	* @param enmValue
	* @param notNull
	* @param notEdit
	* @param hiddenField
	* @param isReserved
	* @param cruser
	* @param crtime
	* @param sysFieldType
	* @param formFieldType 
	*/ 
	public AppFieldInfo(Long fieldId, Long tableId, String tableName,
			String fieldName, String fieldDesc, String fieldType,
			String dbtype, Long fieldLength, Integer fieldStyle, Long dblength,
			Long dbscale, String defaultValue, String enmValue,
			Integer notNull, Integer notEdit, Integer hiddenField,
			Integer isReserved, String cruser, Date crtime,
			Integer sysFieldType, String formFieldType) {
		super();
		this.fieldId = fieldId;
		this.tableId = tableId;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.fieldDesc = fieldDesc;
		this.fieldType = fieldType;
		this.dbtype = dbtype;
		this.fieldLength = fieldLength;
		this.fieldStyle = fieldStyle;
		this.dblength = dblength;
		this.dbscale = dbscale;
		this.defaultValue = defaultValue;
		this.enmValue = enmValue;
		this.notNull = notNull;
		this.notEdit = notEdit;
		this.hiddenField = hiddenField;
		this.isReserved = isReserved;
		this.cruser = cruser;
		this.crtime = crtime;
		this.sysFieldType = sysFieldType;
		this.formFieldType = formFieldType;
	}

	public String getFormFieldType() {
		return formFieldType;
	}

	public void setFormFieldType(String formFieldType) {
		this.formFieldType = formFieldType;
	}

	public Integer getSysFieldType() {
		return sysFieldType;
	}

	public void setSysFieldType(Integer sysFieldType) {
		this.sysFieldType = sysFieldType;
	}

	/** 
	 * @return fieldId 
	 */
	public Long getFieldId() {
		return fieldId;
	}

	/** 
	 * 设置属性： fieldId
	 * @param fieldId 
	 */
	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	/** 
	 * @return tableId 
	 */
	public Long getTableId() {
		return tableId;
	}

	/** 
	 * 设置属性： tableId
	 * @param tableId 
	 */
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	/** 
	 * @return tableName 
	 */
	public String getTableName() {
		return tableName;
	}

	/** 
	 * 设置属性： tableName
	 * @param tableName 
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/** 
	 * @return fieldName 
	 */
	public String getFieldName() {
		return fieldName;
	}

	/** 
	 * 设置属性： fieldName
	 * @param fieldName 
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/** 
	 * @return fieldDesc 
	 */
	public String getFieldDesc() {
		return fieldDesc;
	}

	/** 
	 * 设置属性： fieldDesc
	 * @param fieldDesc 
	 */
	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	/** 
	 * @return fieldType 
	 */
	public String getFieldType() {
		return fieldType;
	}

	/** 
	 * 设置属性： fieldType
	 * @param fieldType 
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/** 
	 * @return dbtype 
	 */
	public String getDbtype() {
		return dbtype;
	}

	/** 
	 * 设置属性： dbtype
	 * @param dbtype 
	 */
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	/** 
	 * @return fieldLength 
	 */
	public Long getFieldLength() {
		return fieldLength;
	}

	/** 
	 * 设置属性： fieldLength
	 * @param fieldLength 
	 */
	public void setFieldLength(Long fieldLength) {
		this.fieldLength = fieldLength;
		if(fieldLength != null){
			this.dblength = fieldLength * 2;
		}
	}

	/** 
	 * @return fieldStyle 
	 */
	public Integer getFieldStyle() {
		return fieldStyle;
	}

	/** 
	 * 设置属性： fieldStyle
	 * @param fieldStyle 
	 */
	public void setFieldStyle(Integer fieldStyle) {
		this.fieldStyle = fieldStyle;
	}

	/** 
	 * @return dblength 
	 */
	public Long getDblength() {
		return dblength;
	}

	/** 
	 * 设置属性： dblength
	 * @param dblength 
	 */
	public void setDblength(Long dblength) {
		this.dblength = dblength;
	}

	/** 
	 * @return dbscale 
	 */
	public Long getDbscale() {
		return dbscale;
	}

	/** 
	 * 设置属性： dbscale
	 * @param dbscale 
	 */
	public void setDbscale(Long dbscale) {
		this.dbscale = dbscale;
	}

	/** 
	 * @return defaultValue 
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/** 
	 * 设置属性： defaultValue
	 * @param defaultValue 
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/** 
	 * @return enmValue 
	 */
	public String getEnmValue() {
		return enmValue;
	}

	/** 
	 * 设置属性： enmValue
	 * @param enmValue 
	 */
	public void setEnmValue(String enmValue) {
		this.enmValue = enmValue;
	}

	/** 
	 * @return notNull 
	 */
	public Integer getNotNull() {
		return notNull;
	}

	/** 
	 * 设置属性： notNull
	 * @param notNull 
	 */
	public void setNotNull(Integer notNull) {
		this.notNull = notNull;
	}

	/** 
	 * @return notEdit 
	 */
	public Integer getNotEdit() {
		return notEdit;
	}

	/** 
	 * 设置属性： notEdit
	 * @param notEdit 
	 */
	public void setNotEdit(Integer notEdit) {
		this.notEdit = notEdit;
	}

	/** 
	 * @return hiddenField 
	 */
	public Integer getHiddenField() {
		return hiddenField;
	}

	/** 
	 * 设置属性： hiddenField
	 * @param hiddenField 
	 */
	public void setHiddenField(Integer hiddenField) {
		this.hiddenField = hiddenField;
	}

	/** 
	 * @return isReserved 
	 */
	public Integer getIsReserved() {
		return isReserved;
	}

	/** 
	 * 设置属性： isReserved
	 * @param isReserved 
	 */
	public void setIsReserved(Integer isReserved) {
		this.isReserved = isReserved;
	}

	/** 
	 * @return cruser 
	 */
	public String getCruser() {
		return cruser;
	}

	/** 
	 * 设置属性： cruser
	 * @param cruser 
	 */
	public void setCruser(String cruser) {
		this.cruser = cruser;
	}

	/** 
	 * @return crtime 
	 */
	public Date getCrtime() {
		return crtime;
	}

	/** 
	 * 设置属性： crtime
	 * @param crtime 
	 */
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}
    public String getTypeDesc(){
    	String typeDesc = "";
    	if(this.fieldType.equals("text")){
    		typeDesc = "文本";
    	} else if(this.fieldType.equals("textarea")){
    		typeDesc = "多行文本";
    	} else if(this.fieldType.equals("password")){
    		typeDesc = "密码文本";
    	} else if(this.fieldType.equals("hidden")){
    		typeDesc = "隐藏文本";
    	} else if(this.fieldType.equals("editor")){
    		typeDesc = "复杂编辑器";
    	} else if(this.fieldType.equals("smeditor")){
    		typeDesc = "简易编辑器";
    	} else if(this.fieldType.equals("date")){
    		typeDesc = "日期";
    	} else if(this.fieldType.equals("select")){
    		typeDesc = "下拉列表";
    	} else if(this.fieldType.equals("checkbox")){
    		typeDesc = "多选框";
    	} else if(this.fieldType.equals("radio")){
    		typeDesc = "单选按钮";
    	} else {
    		typeDesc = "数值";
    	}
    	return typeDesc;
    }
    
}