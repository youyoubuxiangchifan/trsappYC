package com.trs.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.AppFlowDao;
import com.trs.model.AppFlow;
import com.trs.model.AppFlowDoc;
import com.trs.model.AppFlowNode;
import com.trs.model.AppInfo;
import com.trs.model.AppUser;
import com.trs.model.AppWeekday;
import com.trs.util.CMyString;
import com.trs.util.DateUtil;

/**
 * Description: 流程管理操作<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppFlowService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-19 上午11:51:21
 * @version 1.0
 */
@Service
public class AppFlowService extends BaseService {
	@Autowired
	private AppFlowDao appFlowDao;
//	/**
//	 * 新增工作流
//	 * Description: 新增工作流<BR>
//	 * @author liu.zhuan
//	 * @date 2014-3-19 下午04:23:09
//	 * @param appFlow 流程对象
//	 * @version 1.0
//	 */
//	public void addAppFlow(AppFlow appFlow, AppUser user){
//		//新增一个工作流
//		appFlowDao.save(appFlow);
//		/*Long flowId = appFlow.getFlowId();
//		//新增开始，结束节点
//		AppFlowNode startNode = new AppFlowNode();
//		startNode.setFlowId(flowId);
//		startNode.setNodeName("开始");
//		startNode.setNodeDesc("开始节点");
//		startNode.setCruser(user.getUsername());
////		startNode.setNodeDocStatus(0);//此处不设置默认值
//		startNode.setLimitDayNum(0);
//		startNode.setNodeOrder(1);
//		appFlowDao.save(startNode);
//		AppFlowNode endNode = new AppFlowNode();
//		endNode.setFlowId(flowId);
//		endNode.setNodeName("结束");
//		endNode.setNodeDesc("结束节点");
//		endNode.setCruser(user.getUsername());
////		endNode.setNodeDocStatus(0);//此处不设置默认值
//		endNode.setLimitDayNum(0);
//		endNode.setNodeOrder(2);
//		appFlowDao.save(endNode);
//		
//		//修改工作流开始结束节点信息
//		Long sNodeId = startNode.getNodeId();
//		Long eNodeId = endNode.getNodeId();
//		appFlow.setSnodeId(sNodeId);
//		appFlow.setEnodeId(eNodeId);*/
//		appFlow.setCruser(user.getUsername());
//		appFlowDao.update(appFlow);
//	}
	/**
	 * 删除工作流
	 * Description:  删除工作流信息，同时删除工作流下的节点信息以及应用关联信息。<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-20 下午06:24:26
	 * @param flowId String 工作流编号
	 * @throws Exception
	 * @version 1.0
	 */
	public void deleteAppFlow(Long flowId) throws Exception{
		AppFlow appFlow = (AppFlow)appFlowDao.findById(AppFlow.class, flowId);
		if(appFlow == null){
			throw new Exception("没有找到对应的工作流对象！");
		}
		appFlowDao.delete(appFlow);
		//删除节点
		List<Object> flowNodes = find("", AppFlowNode.class.getName(), "flowId = ?", "", flowId);
		appFlowDao.deleteAll(flowNodes);
		//删除应用关联
		List<Object> appInfoList = find("", AppInfo.class.getName(), "flowId = ?", "", flowId);
		AppInfo appInfo = null;
		for (Object object : appInfoList) {
			appInfo = (AppInfo)object;
			appInfo.setFlowId(new Long(0));
		}
		appFlowDao.saveOrUpdateAll(appInfoList);
	}
	/**
	 * 根据节点序号判断开始或结束节点是否存在
	 * Description:  根据节点序号判断开始或结束节点是否存在<BR>   
	 * @author liu.zhuan
	 * @date 2014-4-10 下午03:06:45
	 * @param flowId 工作流编号
	 * @param nodeOrder 节点序号为0表示为开始节点，1为结束节点，任务节点为-1
	 * @return boolean true表示存在，false表示不存在
	 * @throws Exception
	 * @version 1.0
	 */
	public boolean hasStartOrEndNode(long flowId, int nodeOrder) throws Exception{
		StringBuffer sWhere = new StringBuffer("flowId = ? and nodeOrder = ?");
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(flowId);
		parameters.add(nodeOrder);
		int nodeCount = count(AppFlowNode.class.getName(), sWhere.toString(), parameters);
		if(nodeCount > 0){
			return true;
		}
		return false;
	}
	/**
	 * Description:  添加节点<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-20 上午11:17:59
	 * @param flowNode
	 * @version 1.0
	 * @throws Exception 
	 */
	public void addFlowNode(AppFlowNode flowNode) throws Exception{
		long flowId = flowNode.getFlowId();
		AppFlow appFlow = (AppFlow)findById(AppFlow.class, flowId);
		if(appFlow == null){
			throw new Exception("要添加的节点没有指定工作流！");
		}
		appFlowDao.save(flowNode);
		long nodeId = flowNode.getNodeId();
		if(flowNode.getNodeOrder() == 0){//节点为开始节点
			appFlow.setSnodeId(nodeId);
		} else if(flowNode.getNodeOrder() == 1){//为结束节点
			appFlow.setEnodeId(nodeId);
		}
		appFlowDao.update(appFlow);
	}
	
	/**
	 * Description: 节点排序<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-19 下午08:47:04
	 * @param nodeId 节点编号
	 * @param order 排序
	 * @version 1.0
	 * @throws Exception 
	 */
	public void orderByFlowNode(Long flowId, Long nodeId, int order) throws Exception{
		//获取该节点
		AppFlowNode currFlowNode = (AppFlowNode)appFlowDao.findById(AppFlowNode.class, nodeId);
		int currOrder = currFlowNode.getNodeOrder();
		if(currOrder == order) return;
		//根据工作流Id和序号定位到该序号后的所有节点，序号批量加1
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(flowId);
		parameters.add(order);
		parameters.add(currOrder);
		String sWhere = "flowId = ? and nodeOrder >= ? and nodeOrder < ?";
		if(currOrder < order){
			sWhere = "flowId = ? and nodeOrder <= ? and nodeOrder > ?";
		}
		List<Object> nodeList = find("", AppFlowNode.class.getName(), sWhere, "nodeOrder asc", parameters);
		if(nodeList != null){
			AppFlowNode flowNode = null;
			for (Object object : nodeList) {
				flowNode = (AppFlowNode)object;
				if(currOrder < order){
					flowNode.setNodeOrder(flowNode.getNodeOrder() - 1);
				} else {
					flowNode.setNodeOrder(flowNode.getNodeOrder() + 1);
				}
			}
			appFlowDao.saveOrUpdateAll(nodeList);
		}
		//设定当前节点序号
		currFlowNode.setNodeOrder(order);
		appFlowDao.update(currFlowNode);
	}
	/**
	 * Description:  重新排列所有节点<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-20 上午10:49:52
	 * @version 1.0
	 * @throws Exception 
	 */
	public void orderByFlowNode(Long flowId) throws Exception{
		//查询所有节点，重新排序
		List<Object> flowNodes = find("", AppFlowNode.class.getName(), "flowId = ?", "nodeOrder asc", flowId);
		if(flowNodes != null && flowNodes.size() > 0){
			AppFlowNode flowNode = null;
			for (int i = 1; i <= flowNodes.size(); i++) {
				flowNode = (AppFlowNode) flowNodes.get(i-1);
				flowNode.setNodeOrder(i);
			}
			appFlowDao.saveOrUpdateAll(flowNodes);
		}
	}
	
	/**
	 * Description:  删除单个工作流节点<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-20 上午10:46:43
	 * @param flowId 工作流编号
	 * @param nodeId 节点编号
	 * @throws Exception
	 * @version 1.0
	 */
	public void deleteFlowNode(Long flowId, Long nodeId) throws Exception{
		//先删除节点
		AppFlowNode delFlowNode = (AppFlowNode)findById(AppFlowNode.class, nodeId);
		if(delFlowNode == null){
			throw new Exception("要删除的对象不存在！");
		}
		appFlowDao.delete(delFlowNode);
		//重新排序
		orderByFlowNode(flowId);
		//删除节点对应的流转轨迹
		List<Object> flowDocs = find("", AppFlowDoc.class.getName(), "nodeId = ?", "", nodeId);
		appFlowDao.deleteAll(flowDocs);
	}
	
	/**
	 * Description:  删除多个工作流节点<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-20 上午10:46:43
	 * @param flowId 工作流编号
	 * @param nodeIds 节点编号字符串，以","隔开
	 * @throws Exception
	 * @version 1.0
	 */
	public void deleteFlowNodes(Long flowId, String nodeIds) throws Exception{
		//先删除所选节点
		List<Object> delFlowNodes = findByIds(AppFlowNode.class.getName(), "nodeId", nodeIds);
		if(delFlowNodes == null){
			throw new Exception("要删除的对象集合不存在！");
		}
		appFlowDao.deleteAll(delFlowNodes);
		//重新排序
		orderByFlowNode(flowId);
		//删除节点对应的流转轨迹
		List<Object> flowDocs = findByIds(AppFlowDoc.class.getName(), "nodeId", nodeIds);
		appFlowDao.deleteAll(flowDocs);
	}
	/**
	 * 根据序号和位置获取节点信息
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-3-26 下午11:40:58
	* @param flowId Long 工作流编号
	* @param order int 序号，当序号为0的时候只获取倒数第二个节点。
	* @param pointer int 位置，0表示前一节点，1表示后一节点。当序号大于1以及位置为0时获取前一节点。当序号小于最大序号且位置为1时获取下个节点。
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public AppFlowNode getFlowNode(Long flowId, int order, int pointer) throws Exception{
		int maxOrder = getLastFlowNodeOrder(flowId);
		if(order == 0){
			order = maxOrder - 1;
		} else if(order > 1 && pointer == 0){
			order = order - 1;
		} else if(order < maxOrder && pointer == 1){
			order = order + 1;
		}
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(flowId);
		parameters.add(order);
		List<Object> list = appFlowDao.find("from AppFlowNode where flowId = ? and nodeOrder = ?", parameters);//find("max(nodeOrder)", AppFlowNode.class.getName(), "flowId = ?", "", flowId);
		if(list != null && list.size() > 0){
			return (AppFlowNode)list.iterator().next();
		}
		return null;
	}
	/**
	 * 获取序号最大的工作流节点
	 * Description: 获取序号最大的工作流节点<BR>
	 * @author liu.zhuan
	 * @date 2014-3-20 下午01:42:15
	 * @param flowId 工作流编号
	 * @return AppFlowNode
	 * @throws Exception
	 * @version 1.0
	 */
	public AppFlowNode getLastFlowNode(Long flowId) throws Exception{
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(flowId);
		List<Object> list = appFlowDao.find("from AppFlowNode where flowId = ? order by nodeOrder desc", parameters, 0, 1);//find("max(nodeOrder)", AppFlowNode.class.getName(), "flowId = ?", "", flowId);
		if(list != null && list.size() > 0){
			return (AppFlowNode)list.iterator().next();
		}
		return null;
	}
	/**
	 * 获取序号最大的工作流节点序号
	 * Description: 获取序号最大的工作流节点序号<BR>
	 * @author liu.zhuan
	 * @date 2014-3-20 下午01:42:15
	 * @param flowId 工作流编号
	 * @return int 节点最大序号
	 * @throws Exception
	 * @version 1.0
	 */
	public int getLastFlowNodeOrder(Long flowId) throws Exception{
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(flowId);
		List<Object> list = find("max(nodeOrder)", AppFlowNode.class.getName(), "flowId = ?", "", flowId);//appFlowDao.find("select max(nodeOrder) from AppFlowNode where flowId = ?", parameters, 0, 1);
		if(list != null && list.size() > 0){
			return (Integer)list.iterator().next();
		}
		return 2;
	}
	/**
	 * 根据工作流编号查询工作流下的节点信息
	 * Description:  根据工作流编号查询工作流下的节点信息<BR>   
	 * @author liu.zhuan
	 * @date 2014-4-4 下午03:49:26
	 * @param flowId long 工作流编号
	 * @return List 指定工作流编号的节点信息
	 * @throws Exception
	 * @version 1.0
	 */
	public List<Object> getNodeByFlowId(Long flowId) throws Exception{
		return find("", AppFlowNode.class.getName(), "flowId = ?", "", flowId);
	}
	/**
	 * 返回当前节点信息
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-4 下午05:54:09
	* Last Modified:
	* @param nodeId
	* @return
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> findByFlowNode(Long nodeId) throws Exception{
		return find("", AppFlowNode.class.getName(), "nodeId = ?", "", nodeId);
	}
	/**
	 * 根据节点编号判断是否结束节点
	* Description:是返回true 否返回false<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-15 下午08:55:18
	* Last Modified:
	* @param enodeId  节点编号
	* @return boolean 是返回true 否返回false
	* @throws Exception
	* @version 1.0
	 */
	public boolean isLastNode(Long enodeId) throws Exception{
		 List<Object> enodeIds = find("flowId", AppFlow.class.getName(), "enodeId = ?", "", enodeId);
		 if(enodeIds.size()>0){
			 return true;
		 }
		 return false;
	}
	/**
	 * 返回工作流信息对象
	 * Description:  根据工作流编号查询工作流下的节点信息<BR>   
	 * @author liu.zhuan
	 * @date 2014-4-4 下午03:49:26
	 * @param flowId long 工作流编号
	 * @return List 指定工作流编号的节点信息
	 * @throws Exception
	 * @version 1.0
	 */
	public List<Object> getAppFlow(Long flowId) throws Exception{
		return find("", AppFlow.class.getName(), "flowId = ?", "", flowId);
	}
	/**
	 * 根据当前节点获取下个所有节点
	 * Description: 根据当前节点获取下一个所有节点,下个节点可能有多个 <BR>   
	 * @author liu.zhuan
	 * @date 2014-4-4 下午04:06:57
	 * @param nodeId 当前节点编号
	 * @return AppFlowNode 下一节点信息
	 * @throws Exception
	 * @version 1.0
	 */
	public List<Object> getNextFlowNodes(String nextNodeIds) throws Exception{
		if(CMyString.isEmpty(nextNodeIds)){
			throw new Exception("下一个节点的ID集合不能为空!");
		}
		return findByIds(AppFlowNode.class.getName(),"nodeId", nextNodeIds);
		 
	}
	/**
	 * 根据当前节点获取上一节点
	 * Description: 根据当前节点获取上一节点 <BR>   
	 * @author liu.zhuan
	 * @date 2014-4-4 下午04:06:57
	 * @param nodeId 当前节点编号
	 * @return AppFlowNode 上一节点信息
	 * @throws Exception
	 * @version 1.0
	 */
	public AppFlowNode getPreviousFlowNode(Long nodeId) throws Exception{
		AppFlowNode currFlowNode = (AppFlowNode)findById(AppFlowNode.class, nodeId);
		if(currFlowNode == null){
			throw new Exception("根据当前节点编号没有找到对应的节点信息！");
		}
		int preOrder = currFlowNode.getNodeOrder() - 1;
		if(preOrder < 1){
			preOrder = 1;
		}
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(currFlowNode.getFlowId());
		parameters.add(preOrder);
		return (AppFlowNode)findObject(AppFlowNode.class.getName(), "flowId = ? and nodeOrder = ?", parameters);
	}
	/**
	 * 
	* Description:解析自定义规则配置文件 <BR>   
	* @author zhangzun
	* @date 2014-4-8 上午10:44:39
	* @return List<Map<String,String>>
	* @version 1.0
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<Map<String,String>>> parseAppGuid(String id) throws Exception{
		Map<String,List<Map<String,String>>> ruleMap = new HashMap<String,List<Map<String,String>>>();
		//File file = null;
		InputStream is = null;
		try {  
	        is = this.getClass().getClassLoader().getResourceAsStream("config/app_guid.xml");//.getClassLoader().getResource("").getPath()+ "config/app_guid.xml";
	        //file = new File(path);
	        SAXReader sax = new SAXReader();  
	        Document xmlDoc = sax.read(is);
	        Iterator itguid = null;
	        List<Map<String,String>> list = null;
	        Map<String,String> map = null;
	        Element root = xmlDoc.getRootElement();//根节点  app
	        Element element = null;//子节点，遍历每一个规则类型app_guid
	        Element guid = null;//子节点，遍历每一个规则类型下的每一条规则
	        Iterator it = root.elementIterator("app_guid"); 
	        while(it.hasNext()){
	        	list = new ArrayList<Map<String,String>>();
	        	element = (Element)it.next();
        		itguid = element.elementIterator();
        		while(itguid.hasNext()){
	        		guid = (Element)itguid.next();
	        		map = new HashMap<String,String>();
	        		map.put("className", guid.attributeValue("className"));
	        		map.put("guidDesc", guid.attributeValue("guidDesc"));
	        		list.add(map);
        		}
        		ruleMap.put(element.attributeValue("id"), list);
	        }
		}catch(DocumentException e){
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
		return ruleMap;
	}
	/**
	 * 保存工作流信息
	 * Description: 保存工作流信息 <BR>   
	 * @author liu.zhuan
	 * @date 2014-4-11 下午02:22:03
	 * @param flowId 工作流编号
	 * @param flowJsonData 工作流json信息
	 * @version 1.0
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void saveWorkFlow(long flowId, String flowJsonData, String delJsonData) throws Exception{
		try {
			//先获取所有节点名称和节点编号
			Map<String, String> oldNodeMap = new HashMap<String, String>();
			List<Object> nodeList = find("", AppFlowNode.class.getName(), "flowId = ?", "", flowId);
			//遍历所有节点，保存节点名称和节点编号对应
			AppFlowNode flowNode = null;
			for (Object object : nodeList) {
				flowNode = (AppFlowNode)object;
				oldNodeMap.put(flowNode.getNodeName(), flowNode.getNodeId().toString());
			}
			//更新工作流json数据
			AppFlow appFlow = (AppFlow)findById(AppFlow.class, flowId);
			String oldFlowJsonData = appFlow.getFlowJsonData();
//			JSONObject line = null;
//			List<String> nextNodeList = null;//保存下个节点名称
			Map<String, List<String>> delLineMap = null;
			List<String> delNodeIds = null;
			if(!CMyString.isEmpty(oldFlowJsonData)){
				JSONObject oldJsonObject = JSONObject.fromObject(oldFlowJsonData);
				JSONObject oldNodesData = (JSONObject)oldJsonObject.get("nodes");
//				JSONObject oldLinesData = (JSONObject)oldJsonObject.get("lines");
				JSONObject delJsonObject = JSONObject.fromObject(delJsonData);
				//先处理要删除的节点和连线
				JSONObject delNodeData = (JSONObject)delJsonObject.get("delNodeData");
				JSONObject delLineData = (JSONObject)delJsonObject.get("delLineData");
				if(delNodeData != null && delNodeData.size() > 0){
					delNodeIds = new ArrayList<String>();
					String nodeName = null;
					for(Iterator iter = delNodeData.keys(); iter.hasNext();){     
						String key = (String)iter.next();  
						nodeName = delNodeData.getJSONObject(key).getString("name");
						delNodeIds.add(oldNodeMap.get(nodeName));
					}
				}
				if(delLineData != null && delLineData.size() > 0){
					delLineMap = getLineMap(oldNodeMap, oldNodesData, delLineData);
				}
			}
			//解析json
			JSONObject jsonObject = JSONObject.fromObject(flowJsonData);
			JSONObject nodesJsonData = (JSONObject)jsonObject.get("nodes");
			JSONObject linesJsonData = (JSONObject)jsonObject.get("lines");
			Map<String, List<String>> nextNodeMap = getLineMap(oldNodeMap, nodesJsonData, linesJsonData);//new HashMap<String, List<String>>();
			
			//遍历所有节点，保存下个节点集合
			List<String> nextNodeIds = null;
			List<Object> delNodeList = new ArrayList<Object>();
			for (Object object : nodeList) {
				flowNode = (AppFlowNode)object;
				//更新下个节点信息
				if(delLineMap != null && delLineMap.containsKey(flowNode.getNodeId().toString())){
					String nodeIds = flowNode.getNextNodeIds();
					List<String> list = Arrays.asList(nodeIds.split(","));
					nextNodeIds = delLineMap.get(flowNode.getNodeId().toString());
					String newIds = "";
					for (int i = 0; i < list.size(); i++) {
						if(!nextNodeIds.contains(list.get(i))){
							newIds += list.get(i) + ",";
						}
					}
					newIds = CMyString.isEmpty(newIds) ? "" : newIds.substring(0,newIds.length() - 1);
					flowNode.setNextNodeIds(newIds);
				}
				//删除节点
				if(delNodeIds != null && delNodeIds.size() > 0 && delNodeIds.contains(flowNode.getNodeId().toString())){
					delNodeList.add(flowNode);
					continue;
				}
				if(nextNodeMap.containsKey(flowNode.getNodeId().toString())){//如果数据库中的节点在传人的节点中存在，则修改下个节点信息
					nextNodeIds = nextNodeMap.get(flowNode.getNodeId().toString());
					flowNode.setNextNodeIds(nextNodeIds.isEmpty() ? null : CMyString.join((ArrayList<String>)nextNodeIds, ","));
				}
			}
			appFlow.setFlowJsonData(flowJsonData);
			appFlowDao.update(appFlow);
			appFlowDao.saveOrUpdateAll(nodeList);
			appFlowDao.deleteAll(delNodeList);
		} catch (Exception e) {
			throw e;
		}
	}
	/** 
	* Description:  <BR>   
	* @author liu.zhuan
	* @date 2014-4-14 上午10:44:26
	* @param oldNodeMap
	* @param delLineMap
	* @param oldNodesData
	* @param delLineData
	* @version 1.0
	*/
	@SuppressWarnings("unchecked")
	private Map<String, List<String>> getLineMap(Map<String, String> nodeMap, JSONObject nodesData, JSONObject lineData) {
		Map<String, List<String>> lineMap = new HashMap<String, List<String>>();
		JSONObject line = null;
		List<String> nextNodeList = null;
		for(Iterator iter = lineData.keys(); iter.hasNext();){
			String key = (String)iter.next();  
		    line = lineData.getJSONObject(key);
		    String fromNodeName = nodesData.getJSONObject(line.getString("from")).getString("name");
		    String toNodeName = nodesData.getJSONObject(line.getString("to")).getString("name");
		    String fromId = nodeMap.get(fromNodeName);
		    String toId = nodeMap.get(toNodeName);
		    if(CMyString.isEmpty(fromId) || CMyString.isEmpty(toId)){
		    	continue;
		    }
		    if(lineMap.containsKey(fromId)){
		    	nextNodeList = lineMap.get(fromId);
		    } else {
		    	nextNodeList = new ArrayList<String>();
		    }
		    nextNodeList.add(toId);
		    lineMap.put(fromId, nextNodeList);
		}
		return lineMap;
	}
}
