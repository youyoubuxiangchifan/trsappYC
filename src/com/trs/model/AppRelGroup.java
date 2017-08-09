package com.trs.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.trs.util.CMyString;


/**
 * AppFieldRel entity. @author MyEclipse Persistence Tools
 */

public class AppRelGroup {


    // Fields    

     private Long appRelGroupId;
     private Long viewId;
   
     private String fieldName;
     private String grpName;
     private Long fieldId;

	public Long getFieldId() {
		return fieldId;
	}


	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}


	public Long getAppRelGroupId() {
		return appRelGroupId;
	}


	public void setAppRelGroupId(Long appRelGroupId) {
		this.appRelGroupId = appRelGroupId;
	}


	public Long getViewId() {
		return viewId;
	}


	public void setViewId(Long viewId) {
		this.viewId = viewId;
	}


	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	public String getGrpName() {
		return grpName;
	}


	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	 public AppRelGroup(){}
	/** minimal constructor */
    public AppRelGroup(Long appRelGroupId,long viewId,String fieldName,String grpName) {
        this.appRelGroupId = appRelGroupId;
        this.viewId = viewId;
        this.fieldName = fieldName;
        this.grpName= grpName;
    }

	

	
}