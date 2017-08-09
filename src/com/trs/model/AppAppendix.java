package com.trs.model;

import javax.activation.DataHandler;



/**
 * AppAppendix entity. @author MyEclipse Persistence Tools
 */

public class AppAppendix  implements java.io.Serializable {


    // Fields    

     private Long appendixId;
     private Long metadataId;
     private Long appId;
     private String fileName;
     private String fileExt;
     private String srcfile;
     private int appendixType;//附件类别
     private DataHandler handler;

    // Constructors


	/** 
	* Description: TODO<BR>
	* @param appendixId
	* @param metadataId
	* @param appId
	* @param fileName
	* @param fileExt
	* @param srcfile
	* @param appendixType
	* @param handler 
	*/
	public AppAppendix(Long appendixId, Long metadataId, Long appId,
			String fileName, String fileExt, String srcfile, int appendixType,
			DataHandler handler) {
		super();
		this.appendixId = appendixId;
		this.metadataId = metadataId;
		this.appId = appId;
		this.fileName = fileName;
		this.fileExt = fileExt;
		this.srcfile = srcfile;
		this.appendixType = appendixType;
		this.handler = handler;
	}

	/**
	 * @return the appendixType
	 */
	public int getAppendixType() {
		return appendixType;
	}

	/** 
	 * 设置属性： appendixType
	 * @param appendixType 
	 */
	public void setAppendixType(int appendixType) {
		this.appendixType = appendixType;
	}

	/** 
	 * 设置属性： handler
	 * @param handler 
	 */
	public void setHandler(DataHandler handler) {
		this.handler = handler;
	}
	/**
	 * @return the handler
	 */
	public DataHandler getHandler() {
		return handler;
	}
	
	/** default constructor */
    public AppAppendix() {
    }

	/** minimal constructor */
    public AppAppendix(Long appendixId) {
        this.appendixId = appendixId;
    }

	

	/** 
	 * @return appendixId 
	 */
	public Long getAppendixId() {
		return appendixId;
	}

	/** 
	 * 设置属性： appendixId
	 * @param appendixId 
	 */
	public void setAppendixId(Long appendixId) {
		this.appendixId = appendixId;
	}

	/** 
	 * @return metadataId 
	 */
	public Long getMetadataId() {
		return metadataId;
	}

	/** 
	 * 设置属性： metadataId
	 * @param metadataId 
	 */
	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	/** 
	 * @return appId 
	 */
	public Long getAppId() {
		return appId;
	}

	/** 
	 * 设置属性： appId
	 * @param appId 
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}

	/** 
	 * @return fileName 
	 */
	public String getFileName() {
		return fileName;
	}

	/** 
	 * 设置属性： fileName
	 * @param fileName 
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	 * @return fileExt 
	 */
	public String getFileExt() {
		return fileExt;
	}

	/** 
	 * 设置属性： fileExt
	 * @param fileExt 
	 */
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/** 
	 * @return srcfile 
	 */
	public String getSrcfile() {
		return srcfile;
	}

	/** 
	 * 设置属性： srcfile
	 * @param srcfile 
	 */
	public void setSrcfile(String srcfile) {
		this.srcfile = srcfile;
	}
    
   
}