package com.trs.appcontext;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trs.model.AppFlowContext;
import com.trs.service.BaseService;

/**
 * 工作流自定义父类,子类通过重载实现
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: IToUsersCreator
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-26 下午11:19:08
 * @version 1.0
 */
@Repository
public abstract class IToUsersCreator extends BaseService  {
	/**
	 * 返回自定义规则上的用户ID集合
	* Description:返回的格式必须是用户ID集合，以逗号分隔的用户ID<BR>  
	* @param AppFlowContext 自定义规则传参对象
	* @author wen.junhui
	* @date Create: 2014-4-8 下午02:39:02
	* Last Modified:
	* @return String 返回的格式必须是用户ID集合，以逗号分隔的用户ID ，例如[1,2,3,4]
	* @version 1.0
	 */
	public abstract String createToUsers(AppFlowContext appFlowContext);
	/**
	 * 自定操作规则方法，返回Map格式
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-22 下午06:36:37
	* Last Modified:
	* @param AppFlowContext 自定义规则传参对象
	* @return Map<String,Object> 格式：Map<String,Objcet>{<nodeId,value>,<userIds,value>} nodeId节点编号，userIds 用户编号集合
	* @version 1.0
	 */
	public abstract Map<String,Object> createNodeIdAndFLowUser(AppFlowContext appFlowContext);
}
