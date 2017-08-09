package com.trs.model;

import java.util.Date;

import com.trs.util.CMyDateTime;


/**
 * AppDatastatus entity. @author MyEclipse Persistence Tools
 */

public class AppDatastatus  implements java.io.Serializable {


    // Fields    

     private Long datastatusId;
     private String statusname;
     private String statusdesc;
     private Integer type;
     private String cruser;
     private Date crtime;

    // Constructors

    /** default constructor */
    public AppDatastatus() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppDatastatus(Long datastatusId, String statusname, Date crtime) {
        this.datastatusId = datastatusId;
        this.statusname = statusname;
        this.crtime = crtime;
    }


	/** 
	* <p>Description: TODO</p> 
	* @param datastatusId
	* @param statusname
	* @param statusdesc
	* @param type
	* @param cruser
	* @param crtime 
	*/ 
	public AppDatastatus(Long datastatusId, String statusname,
			String statusdesc, Integer type, String cruser, Date crtime) {
		this.datastatusId = datastatusId;
		this.statusname = statusname;
		this.statusdesc = statusdesc;
		this.type = type;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return datastatusId 
	 */
	public Long getDatastatusId() {
		return datastatusId;
	}

	/** 
	 * 设置属性： datastatusId
	 * @param datastatusId 
	 */
	public void setDatastatusId(Long datastatusId) {
		this.datastatusId = datastatusId;
	}

	/** 
	 * @return statusname 
	 */
	public String getStatusname() {
		return statusname;
	}

	/** 
	 * 设置属性： statusname
	 * @param statusname 
	 */
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	/** 
	 * @return statusdesc 
	 */
	public String getStatusdesc() {
		return statusdesc;
	}

	/** 
	 * 设置属性： statusdesc
	 * @param statusdesc 
	 */
	public void setStatusdesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}

	/** 
	 * @return type 
	 */
	public Integer getType() {
		return type;
	}

	/** 
	 * 设置属性： type
	 * @param type 
	 */
	public void setType(Integer type) {
		this.type = type;
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
}