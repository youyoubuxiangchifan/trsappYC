package com.trs.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.PublicAppBaseDao;
import com.trs.dbhibernate.Page;
import com.trs.model.AppComment;
import com.trs.model.AppInfo;
/**
 * 评论处理服务层
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: AppCommentService
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-18 下午02:07:59
 * @version 1.0
 */
@Service
public class AppCommentService extends BaseService{
	private static  Logger LOG =  Logger.getLogger(AppCommentService.class);
	@Autowired
	private PublicAppBaseDao publicAppBaseDao;
	/**
	 * 返回应用的评论开启状态
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午02:23:26
	* Last Modified:
	* @param appId
	* @return Integer 0 没有开启 1 私有评论 2 公开评论
	* @version 1.0
	 */
	public Integer isOpenComment(Long appId)throws Exception{
		AppInfo appInfo = (AppInfo)publicAppBaseDao.findById(AppInfo.class, appId);
		return appInfo.getIsHasComment();
	}
	/**
	 * 返回当前数据编号对应的评论信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午02:24:54
	* Last Modified:
	* @param appId 应用编号
	* @param metadateId 当前数据编号
	* @param commentStatus 评论的状态 0未审核 1 已审核 2已删除
	* @version 1.0
	 * @throws Exception 
	 */
	public Page getAppToComments(Long appId,Long metadateId,int commentStatus,String sOrder,int nStartPage, int nPageSize) throws Exception{
		List<Object> paramters = new ArrayList<Object>();
		paramters.add(appId);
		paramters.add(metadateId);
		paramters.add(commentStatus);
		return findPage(null, AppComment.class.getName(), "appId=? and dataId=? and commentStatus=?", sOrder, nStartPage, nPageSize, paramters);
	}
	/**
	 * 返回单条评论详细信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午02:25:52
	* Last Modified:
	* @param commentId 评论编号
	* @version 1.0
	 */
	public AppComment getComment(Long commentId)throws Exception{
		return (AppComment)findById(AppComment.class, commentId);
	}

	/**
	 * 保存单条评论信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午03:04:25
	* Last Modified:
	* @param appComment
	* @version 1.0
	 */
	public void saveAppComment(AppComment appComment)throws Exception{
		publicAppBaseDao.save(appComment);
	}
	/**
	 * 批量更新评论对象
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午03:13:03
	* Last Modified:
	* @param parameters List<Object> 格式:List<AppComment>
	* @version 1.0
	 */
	public void updateAppComments(List<Object> parameters)throws Exception{
		publicAppBaseDao.saveOrUpdateAll(parameters);
	}
	
	/**
	 * 删除对应ID编号的评论信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-18 下午03:06:22
	* Last Modified:
	* @param commentId
	* @throws Exception
	* @version 1.0
	 */
	public void delAppComment(Long commentId) throws Exception{
		delete(AppComment.class, commentId);
	}
}
