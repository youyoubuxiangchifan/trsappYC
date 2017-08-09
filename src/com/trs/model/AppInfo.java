package com.trs.model;

import java.util.Date;


/**
 * AppInfo entity. @author MyEclipse Persistence Tools
 */

public class AppInfo{


    // Fields    

     private Long appId;
     private String appName;
     private Long viewId;
     private String viewName;
     private Long groupId;
     private Long flowId;
     private Integer isNeedTheme;
     private Integer appStatus;
     private Integer isPublic;
     private Integer isHasQueryNo;
     private Integer isHasComment;
     private Integer isEmailWarn;
     private Integer isSmsWarn;
     private Integer isSmsRemind;
     private Integer isPush;
     private Long wcmChnlId;
     private Integer deleted;
     private Date crtime;
     private String cruser;
     private Integer isShowGroup;
     private Date operTime;
     private String operUser;
     private Long mainTableId;
     private String mainTableName;
     private Long itemTableId;
     private String itemTableName;
     private Integer limitDayNum;
     private Integer isSelGroup;
     private String itemGroupId;
     private Integer isSupAppendix;
     private Integer isHasSmtDesc;
     private String smtDesc;
     private String listAddr;
     private String dowithAddr;
     private String weblistAddr;
 	 private String webdowithAddr;
 	 private String webdetailAddr;
 	 private Integer wcmDocType;//文档类型 0表示文档类型，1表示元数据类型
	
	
	  /** 
	* Description: TODO<BR>
	* @param appId
	* @param appName
	* @param viewId
	* @param viewName
	* @param groupId
	* @param flowId
	* @param isNeedTheme
	* @param appStatus
	* @param isPublic
	* @param isHasQueryNo
	* @param isHasComment
	* @param isEmailWarn
	* @param isSmsWarn
	* @param isSmsRemind
	* @param isPush
	* @param wcmChnlId
	* @param deleted
	* @param crtime
	* @param cruser
	* @param isShowGroup
	* @param operTime
	* @param operUser
	* @param mainTableId
	* @param mainTableName
	* @param itemTableId
	* @param itemTableName
	* @param limitDayNum
	* @param isSelGroup
	* @param itemGroupId
	* @param isSupAppendix
	* @param isHasSmtDesc
	* @param smtDesc
	* @param listAddr
	* @param dowithAddr
	* @param weblistAddr
	* @param webdowithAddr
	* @param webdetailAddr
	* @param wcmDocType 
	*/
	public AppInfo(Long appId, String appName, Long viewId, String viewName,
			Long groupId, Long flowId, Integer isNeedTheme, Integer appStatus,
			Integer isPublic, Integer isHasQueryNo, Integer isHasComment,
			Integer isEmailWarn, Integer isSmsWarn, Integer isSmsRemind,
			Integer isPush, Long wcmChnlId, Integer deleted, Date crtime,
			String cruser, Integer isShowGroup, Date operTime, String operUser,
			Long mainTableId, String mainTableName, Long itemTableId,
			String itemTableName, Integer limitDayNum, Integer isSelGroup,
			String itemGroupId, Integer isSupAppendix, Integer isHasSmtDesc,
			String smtDesc, String listAddr, String dowithAddr,
			String weblistAddr, String webdowithAddr, String webdetailAddr,
			Integer wcmDocType) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.viewId = viewId;
		this.viewName = viewName;
		this.groupId = groupId;
		this.flowId = flowId;
		this.isNeedTheme = isNeedTheme;
		this.appStatus = appStatus;
		this.isPublic = isPublic;
		this.isHasQueryNo = isHasQueryNo;
		this.isHasComment = isHasComment;
		this.isEmailWarn = isEmailWarn;
		this.isSmsWarn = isSmsWarn;
		this.isSmsRemind = isSmsRemind;
		this.isPush = isPush;
		this.wcmChnlId = wcmChnlId;
		this.deleted = deleted;
		this.crtime = crtime;
		this.cruser = cruser;
		this.isShowGroup = isShowGroup;
		this.operTime = operTime;
		this.operUser = operUser;
		this.mainTableId = mainTableId;
		this.mainTableName = mainTableName;
		this.itemTableId = itemTableId;
		this.itemTableName = itemTableName;
		this.limitDayNum = limitDayNum;
		this.isSelGroup = isSelGroup;
		this.itemGroupId = itemGroupId;
		this.isSupAppendix = isSupAppendix;
		this.isHasSmtDesc = isHasSmtDesc;
		this.smtDesc = smtDesc;
		this.listAddr = listAddr;
		this.dowithAddr = dowithAddr;
		this.weblistAddr = weblistAddr;
		this.webdowithAddr = webdowithAddr;
		this.webdetailAddr = webdetailAddr;
		this.wcmDocType = wcmDocType;
	}
	/**
	 * @return the webdetailAddr
	 */
	public String getWebdetailAddr() {
		return webdetailAddr;
	}
	/** 
	 * 设置属性： webdetailAddr
	 * @param webdetailAddr 
	 */
	public void setWebdetailAddr(String webdetailAddr) {
		this.webdetailAddr = webdetailAddr;
	}
	public String getWeblistAddr() {
			return weblistAddr;
		}
		public void setWeblistAddr(String weblistAddr) {
			this.weblistAddr = weblistAddr;
		}
		public String getWebdowithAddr() {
			return webdowithAddr;
		}
		public void setWebdowithAddr(String webdowithAddr) {
			this.webdowithAddr = webdowithAddr;
		}
	
	public String getListAddr() {
		return listAddr;
	}
	public void setListAddr(String listAddr) {
		this.listAddr = listAddr;
	}
	public String getDowithAddr() {
		return dowithAddr;
	}
	public void setDowithAddr(String dowithAddr) {
		this.dowithAddr = dowithAddr;
	}
	/** 
 	* Description: TODO<BR> 
 	*/ 
 	public AppInfo() {
 		super();
 		// TODO Auto-generated constructor stub
 		this.crtime = new java.util.Date(System.currentTimeMillis());
 	}
 	
	public Integer getIsSupAppendix() {
		return isSupAppendix;
	}
	public void setIsSupAppendix(Integer isSupAppendix) {
		this.isSupAppendix = isSupAppendix;
	}
	public String getItemGroupId() {
		return itemGroupId;
	}

	public void setItemGroupId(String itemGroupId) {
		this.itemGroupId = itemGroupId;
	}

	public Integer getIsSelGroup() {
		return isSelGroup;
	}

	public void setIsSelGroup(Integer isSelGroup) {
		this.isSelGroup = isSelGroup;
	}

	public Integer getLimitDayNum() {
		return limitDayNum;
	}

	public void setLimitDayNum(Integer limitDayNum) {
		this.limitDayNum = limitDayNum;
	}

	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public Long getMainTableId() {
		return mainTableId;
	}
	public void setMainTableId(Long mainTableId) {
		this.mainTableId = mainTableId;
	}
	public String getMainTableName() {
		return mainTableName;
	}
	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
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
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Long getViewId() {
		return viewId;
	}
	public void setViewId(Long viewId) {
		this.viewId = viewId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public Integer getIsNeedTheme() {
		return isNeedTheme;
	}
	public void setIsNeedTheme(Integer isNeedTheme) {
		this.isNeedTheme = isNeedTheme;
	}
	public Integer getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(Integer appStatus) {
		this.appStatus = appStatus;
	}
	public Integer getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	public Integer getIsHasQueryNo() {
		return isHasQueryNo;
	}
	public void setIsHasQueryNo(Integer isHasQueryNo) {
		this.isHasQueryNo = isHasQueryNo;
	}
	public Integer getIsHasComment() {
		return isHasComment;
	}
	public void setIsHasComment(Integer isHasComment) {
		this.isHasComment = isHasComment;
	}
	public Integer getIsEmailWarn() {
		return isEmailWarn;
	}
	public void setIsEmailWarn(Integer isEmailWarn) {
		this.isEmailWarn = isEmailWarn;
	}
	public Integer getIsSmsWarn() {
		return isSmsWarn;
	}
	public void setIsSmsWarn(Integer isSmsWarn) {
		this.isSmsWarn = isSmsWarn;
	}
	public Integer getIsSmsRemind() {
		return isSmsRemind;
	}
	public void setIsSmsRemind(Integer isSmsRemind) {
		this.isSmsRemind = isSmsRemind;
	}
	public Integer getIsPush() {
		return isPush;
	}
	public void setIsPush(Integer isPush) {
		this.isPush = isPush;
	}
	public Long getWcmChnlId() {
		return wcmChnlId;
	}
	public void setWcmChnlId(Long wcmChnlId) {
		this.wcmChnlId = wcmChnlId;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Date getCrtime() {
		return crtime;
	}
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}
	public String getCruser() {
		return cruser;
	}
	public void setCruser(String cruser) {
		this.cruser = cruser;
	}
	public Integer getIsShowGroup() {
		return isShowGroup;
	}
	public void setIsShowGroup(Integer isShowGroup) {
		this.isShowGroup = isShowGroup;
	}
	public Date getOperTime() {
		return operTime;
	}
	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	public String getOperUser() {
		return operUser;
	}
	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}
	
	public Integer getIsHasSmtDesc() {
		return isHasSmtDesc;
	}
	public void setIsHasSmtDesc(Integer isHasSmtDesc) {
		this.isHasSmtDesc = isHasSmtDesc;
	}
	public String getSmtDesc() {
		return smtDesc;
	}
	public void setSmtDesc(String smtDesc) {
		this.smtDesc = smtDesc;
	}
	/**
	 * @return the wcmDocType
	 */
	public Integer getWcmDocType() {
		return wcmDocType;
	}
	/** 
	 * 设置属性： wcmDocType
	 * @param wcmDocType 
	 */
	public void setWcmDocType(Integer wcmDocType) {
		this.wcmDocType = wcmDocType;
	}
	public String getPublicDesc(){
    	String pDesc = "";
    	int state = 0;
    	if(this.isPublic!=null)
    		state = this.isPublic.intValue();
    	switch(state){
    		case 0:
    			pDesc = "处理后公开";
    			break;
    		case 1:
    			pDesc = "不公开";
    			break;
    		case 2:
    			pDesc = "直接公开";
    			break;
    	}
    	return pDesc;
    }

}