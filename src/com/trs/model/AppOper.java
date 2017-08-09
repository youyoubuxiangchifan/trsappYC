package com.trs.model;

import java.util.Date;

import com.trs.util.CMyDateTime;


/**
 * AppOper entity. @author MyEclipse Persistence Tools
 */

public class AppOper  implements java.io.Serializable {


    // Fields    

     private Long operid;
     private String opname;
     private String opdesc;
     private String operFlag;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppOper() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppOper(Long operid) {
        this.operid = operid;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param operid
	* @param opname
	* @param opdesc
	* @param operFlag
	* @param cruser
	* @param crtime 
	*/ 
	public AppOper(Long operid, String opname, String opdesc, String operFlag,
			String cruser, Date crtime) {
		this.operid = operid;
		this.opname = opname;
		this.opdesc = opdesc;
		this.operFlag = operFlag;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return operid 
	 */
	public Long getOperid() {
		return operid;
	}

	/** 
	 * 设置属性： operid
	 * @param operid 
	 */
	public void setOperid(Long operid) {
		this.operid = operid;
	}

	/** 
	 * @return opname 
	 */
	public String getOpname() {
		return opname;
	}

	/** 
	 * 设置属性： opname
	 * @param opname 
	 */
	public void setOpname(String opname) {
		this.opname = opname;
	}

	/** 
	 * @return opdesc 
	 */
	public String getOpdesc() {
		return opdesc;
	}

	/** 
	 * 设置属性： opdesc
	 * @param opdesc 
	 */
	public void setOpdesc(String opdesc) {
		this.opdesc = opdesc;
	}

	/** 
	 * @return operFlag 
	 */
	public String getOperFlag() {
		return operFlag;
	}

	/** 
	 * 设置属性： operFlag
	 * @param operFlag 
	 */
	public void setOperFlag(String operFlag) {
		this.operFlag = operFlag;
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
}