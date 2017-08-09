package com.trs.model;

import java.util.Date;

import com.trs.util.CMyDateTime;
import com.trs.util.Global;


/**
 * AppLog entity. @author MyEclipse Persistence Tools
 */

public class AppLog  implements java.io.Serializable {


    // Fields    

     private Long logid;
     private Integer logtype;
     private String logdesc;
     private String loguser;
     private Date logoptime;
     private String loguserip;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppLog() {
    	this.logoptime = new java.util.Date(System.currentTimeMillis());
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppLog(Long logid) {
        this.logid = logid;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param logid
	* @param logtype
	* @param logdesc
	* @param loguser
	* @param logoptime
	* @param loguserip
	* @param cruser
	* @param crtime 
	*/ 
	public AppLog(Long logid, Integer logtype, String logdesc, String loguser,
			Date logoptime, String loguserip, String cruser, Date crtime) {
		this.logid = logid;
		this.logtype = logtype;
		this.logdesc = logdesc;
		this.loguser = loguser;
		this.logoptime = logoptime;
		this.loguserip = loguserip;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return logid 
	 */
	public Long getLogid() {
		return logid;
	}

	/** 
	 * 设置属性： logid
	 * @param logid 
	 */
	public void setLogid(Long logid) {
		this.logid = logid;
	}

	/** 
	 * @return logtype 
	 */
	public Integer getLogtype() {
		return logtype;
	}

	/** 
	 * 设置属性： logtype
	 * @param logtype 
	 */
	public void setLogtype(Integer logtype) {
		this.logtype = logtype;
	}

	/** 
	 * @return logdesc 
	 */
	public String getLogdesc() {
		return logdesc;
	}

	/** 
	 * 设置属性： logdesc
	 * @param logdesc 
	 */
	public void setLogdesc(String logdesc) {
		this.logdesc = logdesc;
	}

	/** 
	 * @return loguser 
	 */
	public String getLoguser() {
		return loguser;
	}

	/** 
	 * 设置属性： loguser
	 * @param loguser 
	 */
	public void setLoguser(String loguser) {
		this.loguser = loguser;
	}

	/** 
	 * @return logoptime 
	 */
	public Date getLogoptime() {
		return logoptime;
	}

	/** 
	 * 设置属性： logoptime
	 * @param logoptime 
	 */
	public void setLogoptime(Date logoptime) {
		this.logoptime = logoptime;
	}

	/** 
	 * @return loguserip 
	 */
	public String getLoguserip() {
		return loguserip;
	}

	/** 
	 * 设置属性： loguserip
	 * @param loguserip 
	 */
	public void setLoguserip(String loguserip) {
		this.loguserip = loguserip;
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
	 * 
	* Description:取得crtime，返回yyyy-MM-dd HH:mm:ss<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午09:54:02
	* @return
	* @version 1.0
	 */
	public String getCrData() {
		CMyDateTime date = new CMyDateTime();
		date.setDateTime(crtime);
		return date.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG);
	}
	
	/**
	 * 
	* Description:取得logoptime，返回yyyy-MM-dd HH:mm:ss<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午09:54:02
	* @return
	* @version 1.0
	 */
	public String getLogopTime() {
		CMyDateTime date = new CMyDateTime();
		date.setDateTime(logoptime);
		return date.toString(CMyDateTime.DEF_DATETIME_FORMAT_PRG);
	}
	
	/**
	 * 
	* Description: 通过logtype，返回日志类型名称 <BR>   
	* @author jin.yu
	* @date 2014-4-4 上午10:05:06
	* @return String 日志类型名称
	* @version 1.0
	 */
	public String getLogTypeName() {
		String slogtype = "";
		if(logtype==Global.LOGS_SAVE){
			slogtype = "保存";
		}else if(logtype==2){
			slogtype = "修改";
		}else if(logtype==3){
			slogtype = "删除";
		}else if(logtype==4){
			slogtype = "用户登录";
		}else if(logtype==4){
			slogtype = "用户退出";
		}else {
			slogtype = "其它类型";
		}
		return slogtype;
	}
}