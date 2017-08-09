package com.trs.model;

import java.util.Date;

import com.trs.util.CMyDateTime;


/**
 * AppSysConfig entity. @author MyEclipse Persistence Tools
 */

public class AppSysConfig  implements java.io.Serializable {


    // Fields    

     private Long configId;
     private String configName;
     private String configValue;
     private String configDesc;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppSysConfig() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppSysConfig(Long configId) {
        this.configId = configId;
    }

	/** 
	 * @return configId 
	 */
	public Long getConfigId() {
		return configId;
	}

	/** 
	 * 设置属性： configId
	 * @param configId 
	 */
	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	/** 
	 * @return configName 
	 */
	public String getConfigName() {
		return configName;
	}

	/** 
	 * 设置属性： configName
	 * @param configName 
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	/** 
	 * @return configValue 
	 */
	public String getConfigValue() {
		return configValue;
	}

	/** 
	 * 设置属性： configValue
	 * @param configValue 
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	/** 
	 * @return configDesc 
	 */
	public String getConfigDesc() {
		return configDesc;
	}

	/** 
	 * 设置属性： configDesc
	 * @param configDesc 
	 */
	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
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
	 * 
	* Description:取得crtime，返回yyyy-MM-dd<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午09:54:02
	* @return
	* @version 1.0
	 */
	public String getCrData() {
		CMyDateTime date = new CMyDateTime();
		date.setDateTime(crtime);
		return date.toString(CMyDateTime.DEF_DATE_FORMAT_PRG);
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
	* @param configId
	* @param configName
	* @param configValue
	* @param configDesc
	* @param cruser
	* @param crtime 
	*/ 
	public AppSysConfig(Long configId, String configName, String configValue,
			String configDesc, String cruser, Date crtime) {
		this.configId = configId;
		this.configName = configName;
		this.configValue = configValue;
		this.configDesc = configDesc;
		this.cruser = cruser;
		this.crtime = crtime;
	}
    
    
}