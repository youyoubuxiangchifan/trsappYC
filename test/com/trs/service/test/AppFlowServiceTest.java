package com.trs.service.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.model.AppFlow;
import com.trs.model.AppFlowDoc;
import com.trs.model.AppFlowNode;
import com.trs.service.AppBaseService;
import com.trs.service.AppFlowService;
import com.trs.util.DateUtil;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppFlowServiceTest
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-20 下午01:48:39
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class AppFlowServiceTest extends AbstractJUnit4SpringContextTests{
	@Autowired
	private AppFlowService appFlowService;
	/*@Test
	public void addAppFlowTest(){
		AppFlow appFlow = new AppFlow();
		appFlow.setFlowName("网上咨询工作流3");
		appFlow.setCrtime(DateUtil.parseDate("2014-03-01"));
		appFlowService.addAppFlow(appFlow);
	}*/
	/*@Test
	public void addFlowNode() throws Exception{
		AppFlowNode flowNode = new AppFlowNode();
		flowNode.setFlowId(new Long(234));
		flowNode.setNodeName("终审");
		flowNode.setNodeDesc("终审");
//		startNode.setNodeDocStatus(0);//此处不设置默认值
		flowNode.setLimitDayNum(0);
		flowNode.setCrtime(new Date());
		appFlowService.addFlowNode(flowNode);
	}*/
//	@Test
//	public void orderByFlowNodeTest() throws Exception{
//		//appFlowService.orderByFlowNode(new Long(234), new Long(241), 3);
//		appFlowService.deleteFlowNode(new Long(234), new Long(241));
//		//appFlowService.deleteFlowNodes(new Long(234), "240,242");
//		//List<Object> delFlowNodes = appFlowService.findByIds(AppFlowNode.class.getName(), "nodeId", "416,417");
////		AppFlowDoc flowDoc = new AppFlowDoc();
////		flowDoc.setNodeId(new Long(417));
////		appFlowService.save(flowDoc);
////		List<Object> flowDocs = appFlowService.findByIds(AppFlowDoc.class.getName(), "nodeId", "416,417");
////		System.out.println(flowDocs.size());
//	}
//	@Test
//	public void saveFlowTest(){
//		String json = "{\"title\":\"市长信箱\",\"nodes\":{\"demo_node_1\":{\"name\":\"二审节点\",\"left\":73,\"top\":121,\"type\":\"task\",\"width\":100,\"height\":24},\"demo_node_9\":{\"name\":\"node_9\",\"left\":328,\"top\":124,\"type\":\"task\",\"width\":86,\"height\":24},\"demo_node_10\":{\"name\":\"node_10\",\"left\":632,\"top\":48,\"type\":\"task\",\"width\":86,\"height\":24},\"demo_node_14\":{\"name\":\"node_14\",\"left\":630,\"top\":125,\"type\":\"task\",\"width\":86,\"height\":24},\"demo_node_17\":{\"name\":\"node_17\",\"left\":631,\"top\":212,\"type\":\"task\",\"width\":86,\"height\":24},\"demo_node_19\":{\"name\":\"node_19\",\"left\":843,\"top\":125,\"type\":\"task\",\"width\":86,\"height\":24}},\"lines\":{\"demo_line_11\":{\"type\":\"sl\",\"from\":\"demo_node_1\",\"to\":\"demo_node_9\",\"name\":\"\",\"marked\":false},\"demo_line_12\":{\"type\":\"sl\",\"from\":\"demo_node_9\",\"to\":\"demo_node_10\",\"name\":\"\",\"marked\":false},\"demo_line_16\":{\"type\":\"sl\",\"from\":\"demo_node_9\",\"to\":\"demo_node_14\",\"name\":\"\",\"marked\":false},\"demo_line_18\":{\"type\":\"sl\",\"from\":\"demo_node_9\",\"to\":\"demo_node_17\",\"name\":\"\",\"marked\":false},\"demo_line_20\":{\"type\":\"sl\",\"from\":\"demo_node_10\",\"to\":\"demo_node_19\",\"name\":\"\",\"marked\":false},\"demo_line_21\":{\"type\":\"sl\",\"from\":\"demo_node_14\",\"to\":\"demo_node_19\",\"name\":\"\",\"marked\":false},\"demo_line_22\":{\"type\":\"sl\",\"from\":\"demo_node_17\",\"to\":\"demo_node_19\",\"name\":\"\",\"marked\":false}},\"areas\":{}}";
//		try {
//			appFlowService.saveWorkFlow(1117167, json);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
