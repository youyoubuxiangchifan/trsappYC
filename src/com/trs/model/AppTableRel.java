package com.trs.model;

import java.util.Date;


/**
 * AppTableRel entity. @author MyEclipse Persistence Tools
 */

public class AppTableRel  implements java.io.Serializable {


    // Fields    

     private Long tableRelId;
     private Long mainTableId;
     private String mainTableName;
     private Long itemTableId;
     private String itemTableName;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppTableRel() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppTableRel(Long tableRelId) {
        this.tableRelId = tableRelId;
    }

	/** 
	 * @return tableRelId 
	 */
	public Long getTableRelId() {
		return tableRelId;
	}

	/** 
	 * 设置属性： tableRelId
	 * @param tableRelId 
	 */
	public void setTableRelId(Long tableRelId) {
		this.tableRelId = tableRelId;
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
	 * @return itemTableId 
	 */
	public Long getItemTableId() {
		return itemTableId;
	}

	/** 
	 * 设置属性： itemTableId
	 * @param itemTableId 
	 */
	public void setItemTableId(Long itemTableId) {
		this.itemTableId = itemTableId;
	}

	/** 
	 * @return itemTableName 
	 */
	public String getItemTableName() {
		return itemTableName;
	}

	/** 
	 * 设置属性： itemTableName
	 * @param itemTableName 
	 */
	public void setItemTableName(String itemTableName) {
		this.itemTableName = itemTableName;
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

	/** 
	* <p>Description: TODO</p> 
	* @param tableRelId
	* @param mainTableId
	* @param mainTableName
	* @param itemTableId
	* @param itemTableName
	* @param cruser
	* @param crtime 
	*/ 
	public AppTableRel(Long tableRelId, Long mainTableId, String mainTableName,
			Long itemTableId, String itemTableName, String cruser, Date crtime) {
		this.tableRelId = tableRelId;
		this.mainTableId = mainTableId;
		this.mainTableName = mainTableName;
		this.itemTableId = itemTableId;
		this.itemTableName = itemTableName;
		this.cruser = cruser;
		this.crtime = crtime;
	}
    
   
}