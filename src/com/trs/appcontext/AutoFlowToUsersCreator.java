package com.trs.appcontext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trs.appcontext.IToUsersCreator;
import com.trs.model.AppFlowContext;
import com.trs.model.AppFlowNode;
import com.trs.model.AppUser;
import com.trs.util.CMyString;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AutoFlowToUsersCreator
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-22 下午06:52:03
 * @version 1.0
 */
public class AutoFlowToUsersCreator extends IToUsersCreator {

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
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> createNodeIdAndFLowUser(AppFlowContext appFlowContext){
		String groupId = appFlowContext.getOperType();
		String nextNodeId = "";
		try {
			AppFlowNode currNode = (AppFlowNode)findById(AppFlowNode.class, appFlowContext.getNodeId());
			if(currNode == null){
				return null;
			}
			String nextNodeIds = currNode.getNextNodeIds();
			if(CMyString.isEmpty(nextNodeIds)){
				return null;
			}
			//查询组织下有权限的用户
			List<Object> userIdList = getFlowNodeUs(appFlowContext.getAppId(), groupId);
			if(userIdList == null || userIdList.size() == 0){
				return null;
			}
			ArrayList<Object> tempUserIdList = new ArrayList<Object>();
			List<Object> nextNodeList = findByIds("new list(nodeId,nodeUser,nodeDep)",AppFlowNode.class.getName(), "nodeId", nextNodeIds);
			List<Object> nextNode = null;
			List<String> idList = null;
			//遍历下个节点，判断传入的组织在哪个节点中
			if(nextNodeList != null && nextNodeList.size() > 0){
				for (Object object : nextNodeList) {
					nextNode = (List<Object>)object;
					String nodeUser = (String)nextNode.get(1);
					String nodeDepIds = (String)nextNode.get(2);
					//如果存在部门编号，则判断部门是否在节点上，如果在，则保存节点编号，跳出循环。
					if(!CMyString.isEmpty(nodeDepIds)){
						idList = Arrays.asList(nodeDepIds.split(","));
						if(idList.contains(groupId)){
							nextNodeId = nextNode.get(0).toString();
							break;
						}
					}
					//如果节点上存在用户编号，则判断用户编号是否在当前部门中，如果在，则保存到临时集合中。
					if(!CMyString.isEmpty(nodeUser)){
						idList = Arrays.asList(nodeUser.split(","));
						for (Object obj : userIdList) {
							if(idList.contains(obj.toString())){
								tempUserIdList.add(obj);
							}
						}
					}
					//如果临时集合中有值，则保存节点编号，把下个节点的用户替换为临时变量中去。
					if(tempUserIdList.size() > 0){
						nextNodeId = nextNode.get(0).toString();
						userIdList = tempUserIdList;
						break;
					}
				}
			}
			if(!CMyString.isEmpty(groupId) && !CMyString.isEmpty(nextNodeId)){
				String userIds = CMyString.join((ArrayList<Object>)userIdList, ",");
				if(!CMyString.isEmpty(userIds)){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("nodeId", nextNodeId);
					map.put("userIds", userIds);
					return map;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.trs.service.IToUsersCreator#createToUsers(com.trs.model.AppFlowContext)
	 */
	@Override
	public String createToUsers(AppFlowContext appFlowContext) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 返回当前工作流节点的(上)下一个节点配置的用户信息<该方法只是为了过滤用户后返回结果，实际要传入的参数在节点信息表中查询>
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-5 下午02:32:50
	* Last Modified:
	* @param appId
	* @param groupId 组织编号  已逗号分隔
	* @return List<Object> 格式：List<AppUser>
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> getFlowNodeUs(Long appId,String groupId) throws Exception{
		StringBuffer hql = new StringBuffer(" select distinct a.userId from AppUser a ,AppRoleSys s ,AppRoleUser r where ");
		hql.append(" a.userId=r.userId ");
		hql.append(" and s.roleId = r.roleId ");
		hql.append(" and s.appId=").append(appId)
			.append(" and a.userId in(select g.userId from AppGrpuser g where g.groupId =")
			.append(groupId)
			.append(")");
		List<Object> flowToUserIds = getBaseDao().find(hql.toString());
		return flowToUserIds;
	}

}
