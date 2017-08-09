package com.trs.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.trs.util.CMyString;


/**
 * AppFieldRel entity. @author MyEclipse Persistence Tools
 */

public class AppFieldRel {


    // Fields    

     private Long appFieldId;
     private Long mainTableId;
     private String mainTableName;
     private Long viewId;
     private Long fieldId;
     private String fieldName;
     private String fieldType;
     private Long fieldLength;
     private String fieldDesc;
     private String defaultValue;
     private String enmValue;
     private Integer fieldOrder;
     private Integer inOutline;
     private Integer inDetail;
     private Integer searchField;
     private Integer titleField;
     private Integer isWebShow;
     private Integer notNull;
     private Integer notEdit;
     private Integer hiddenField;
     private Integer fieldStyle;
     private Integer isReserved;
     private Integer isGrpField;
     
     public Integer getIsGrpField() {
		return isGrpField;
	}

	public void setIsGrpField(Integer isGrpField) {
		this.isGrpField = isGrpField;
	}
	private String cruser;
     private Date crtime;
     private String formFieldType;

    // Constructors

    /** default constructor */
    public AppFieldRel() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppFieldRel(Long appFieldId) {
        this.appFieldId = appFieldId;
    }

	

	/** 
	* Description: TODO<BR>
	* @param appFieldId
	* @param mainTableId
	* @param mainTableName
	* @param viewId
	* @param fieldId
	* @param fieldName
	* @param fieldType
	* @param fieldLength
	* @param fieldDesc
	* @param defaultValue
	* @param enmValue
	* @param fieldOrder
	* @param inOutline
	* @param inDetail
	* @param searchField
	* @param titleField
	* @param isWebShow
	* @param notNull
	* @param notEdit
	* @param hiddenField
	* @param fieldStyle
	* @param isReserved
	* @param cruser
	* @param crtime
	* @param formFieldType 
	*/ 
	public AppFieldRel(Long appFieldId, Long mainTableId, String mainTableName,
			Long viewId, Long fieldId, String fieldName, String fieldType,
			Long fieldLength, String fieldDesc, String defaultValue,
			String enmValue, Integer fieldOrder, Integer inOutline,
			Integer inDetail, Integer searchField, Integer titleField,
			Integer isWebShow, Integer notNull, Integer notEdit,
			Integer hiddenField, Integer fieldStyle, Integer isReserved,
			String cruser, Date crtime, String formFieldType,Integer isGrpField) {
		super();
		this.appFieldId = appFieldId;
		this.mainTableId = mainTableId;
		this.mainTableName = mainTableName;
		this.viewId = viewId;
		this.fieldId = fieldId;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldLength = fieldLength;
		this.fieldDesc = fieldDesc;
		this.defaultValue = defaultValue;
		this.enmValue = enmValue;
		this.fieldOrder = fieldOrder;
		this.inOutline = inOutline;
		this.inDetail = inDetail;
		this.searchField = searchField;
		this.titleField = titleField;
		this.isWebShow = isWebShow;
		this.notNull = notNull;
		this.notEdit = notEdit;
		this.hiddenField = hiddenField;
		this.fieldStyle = fieldStyle;
		this.isReserved = isReserved;
		this.cruser = cruser;
		this.crtime = crtime;
		this.formFieldType = formFieldType;
		this.isGrpField =  isGrpField;
	}
	public String getFormFieldType() {
		return formFieldType;
	}

	public void setFormFieldType(String formFieldType) {
		this.formFieldType = formFieldType;
	}
	public Integer getIsReserved() {
		return isReserved;
	}

	public void setIsReserved(Integer isReserved) {
		this.isReserved = isReserved;
	}

	public Long getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(Long fieldLength) {
		this.fieldLength = fieldLength;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/** 
	 * @return appFieldId 
	 */
	public Long getAppFieldId() {
		return appFieldId;
	}

	/** 
	 * 设置属性： appFieldId
	 * @param appFieldId 
	 */
	public void setAppFieldId(Long appFieldId) {
		this.appFieldId = appFieldId;
	}

	/** 
	 * @return mainTableId 
	 */
	public Long getMainTableId() {
		return mainTableId;
	}

	/** 
	 * 设置属性： mainTableId
	 * @param mainTableId 
	 */
	public void setMainTableId(Long mainTableId) {
		this.mainTableId = mainTableId;
	}

	/** 
	 * @return mainTableName 
	 */
	public String getMainTableName() {
		return mainTableName;
	}

	/** 
	 * 设置属性： mainTableName
	 * @param mainTableName 
	 */
	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}

	/** 
	 * @return viewId 
	 */
	public Long getViewId() {
		return viewId;
	}

	/** 
	 * 设置属性： viewId
	 * @param viewId 
	 */
	public void setViewId(Long viewId) {
		this.viewId = viewId;
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
	 * @return fieldOrder 
	 */
	public Integer getFieldOrder() {
		return fieldOrder;
	}

	/** 
	 * 设置属性： fieldOrder
	 * @param fieldOrder 
	 */
	public void setFieldOrder(Integer fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	/** 
	 * @return inOutline 
	 */
	public Integer getInOutline() {
		return inOutline;
	}

	/** 
	 * 设置属性： inOutline
	 * @param inOutline 
	 */
	public void setInOutline(Integer inOutline) {
		this.inOutline = inOutline;
	}

	/** 
	 * @return inDetail 
	 */
	public Integer getInDetail() {
		return inDetail;
	}

	/** 
	 * 设置属性： inDetail
	 * @param inDetail 
	 */
	public void setInDetail(Integer inDetail) {
		this.inDetail = inDetail;
	}

	/** 
	 * @return searchField 
	 */
	public Integer getSearchField() {
		return searchField;
	}

	/** 
	 * 设置属性： searchField
	 * @param searchField 
	 */
	public void setSearchField(Integer searchField) {
		this.searchField = searchField;
	}

	/** 
	 * @return titleField 
	 */
	public Integer getTitleField() {
		return titleField;
	}

	/** 
	 * 设置属性： titleField
	 * @param titleField 
	 */
	public void setTitleField(Integer titleField) {
		this.titleField = titleField;
	}

	/** 
	 * @return isWebShow 
	 */
	public Integer getIsWebShow() {
		return isWebShow;
	}

	/** 
	 * 设置属性： isWebShow
	 * @param isWebShow 
	 */
	public void setIsWebShow(Integer isWebShow) {
		this.isWebShow = isWebShow;
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
    /**
     * 
    * Description:转换枚举值 <BR>   
    * @author zhangzun
    * @date 2014-4-24 下午04:27:44
    * @return
    * @version 1.0
     */
    public List<String> getEnmList() {
    	if(CMyString.isEmpty(enmValue))
    		return null;
    	List<String> list = Arrays.asList(enmValue.split("~"));
		return list;
	}
}