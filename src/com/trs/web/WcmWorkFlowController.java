/**
 * 
 */
package com.trs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.dbhibernate.Page;
import com.trs.model.AppDatastatus;
import com.trs.model.AppFlow;
import com.trs.model.AppFlowNode;
import com.trs.model.AppGroup;
import com.trs.model.AppUser;
import com.trs.model.TreeNode;
import com.trs.service.AppFlowService;
import com.trs.service.AppGroupService;
import com.trs.service.AppLogService;
import com.trs.service.AppSysConfigService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.Global;
import com.trs.util.jsonUtil;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: WcmWorkFlowController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2015-3-22 上午10:42:21
 * @version 1.0
 */
@Controller
@RequestMapping("workflow.do")
public class WcmWorkFlowController {
	@Autowired
	private AppFlowService appFlowService;
	@Autowired
	private AppGroupService appGroupService; 
	@Autowired
	private AppLogService logService;
	@Autowired
	private AppSysConfigService configService;
	private static  Logger loger =  Logger.getLogger(RoleController.class);
	/**
	 * 
	* Description:分页查询流程信息 <BR>   
	* @author zhangzun
	* @date 2014-3-24 下午04:47:07
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=list")
	   public String list(HttpServletRequest request,HttpServletResponse res){
		   String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
		   String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?Global.DEFUALT_PAGESIZE:request.getParameter("numPerPage");	  
		 Page page = null;
		 try {
			page = appFlowService.findPage("", AppFlow.class.getName(),"", "crtime desc",
					Integer.valueOf(currPage),Integer.valueOf(pageSize), null);
		 } catch (Exception e) {
			e.printStackTrace();
			loger.error("查询流程列表失败。", e);
		}
		   request.setAttribute("page", page);
		   request.setAttribute("flowList",page.getLdata());
		   return "appadmin/wcmflow/flow_list";
	   }
	/**
	 * 
	* Description:添加流程 <BR>   
	* @author zhangzun
	* @date 2014-4-12 下午05:08:53
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=add")
	 @ResponseBody
	 public String add(HttpServletRequest request,HttpServletResponse res){
		   String sFlowName = CMyString.showEmpty(request.getParameter("flowName"),"");
		   String sFlowDesc = CMyString.showEmpty(request.getParameter("flowDesc"),"");
		   String sJson = "";
		   AppFlow appFlow = null;
		   AppUser user = CrtlUtil.getCurrentUser(request);
		   try {
			   appFlow = new AppFlow();
			   appFlow.setFlowName(sFlowName);
			   appFlow.setFlowDesc(sFlowDesc);
			   appFlow.setCrtime(new Date());
			   appFlow.setCruser(user.getUsername());
			   appFlowService.save(appFlow);//update by liu.zhuan
			   sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			   StringBuffer logMsg = new StringBuffer("用户");
			   logMsg.append(user.getUsername()).append("成功添加了名为“").append(sFlowName).append("”的工作流。");
			   logService.addAppLog(1, logMsg.toString(), user);
		   } catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300","数据格式有误！", "","", "", "");
			e.printStackTrace();
			loger.error("参数不合法。", e);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			e.printStackTrace();
			loger.error("添加流程失败。", e);
		}
		   
		   return sJson;
	 }
	 /**
	  * 
	 * Description:编辑工作流的信息 <BR>   
	 * @author zhangzun
	 * @date 2014-3-24 下午08:10:57
	 * @param request
	 * @param res
	 * @return String
	 * @version 1.0
	  */
	 @RequestMapping(params = "method=edite")
	 @ResponseBody
	 public String edite(HttpServletRequest request,HttpServletResponse res){
		 String sFlowName = CMyString.showEmpty(request.getParameter("flowName"),"");
		   String sFlowDesc = CMyString.showEmpty(request.getParameter("flowDesc"),"");
		   String sId = CMyString.showEmpty(request.getParameter("id"),"0");
		   AppFlow appFlow = null;
		   String sJson = "";
		   try {
			   appFlow = (AppFlow) appFlowService.findById(AppFlow.class,Long.valueOf(sId));
			   appFlow.setFlowName(sFlowName);
			   appFlow.setFlowDesc(sFlowDesc);
			   appFlowService.update(appFlow);
			   sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
			   AppUser user = CrtlUtil.getCurrentUser(request);//当前登入用户取得
			   StringBuffer logMsg = new StringBuffer("用户");
			   logMsg.append(user.getUsername()).append("成功修改了名为“").append(sFlowName).append("”的工作流信息。");
			   logService.addAppLog(2, logMsg.toString(), user);
		   } catch (Exception e) {
			   sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", ""); 
			e.printStackTrace();
			loger.error("修改流程信息失败。", e);
		}
		   return sJson;
	   }
	 /**
	  * 
	 * Description:校验工作流名称的唯一性 <BR>   
	 * @author zhangzun
	 * @date 2014-3-24 下午09:16:53
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	  */
	 @RequestMapping(params = "method=check")
	 @ResponseBody
	 public String check(HttpServletRequest request,HttpServletResponse res){
		 String sFlowName = CMyString.showEmpty(request.getParameter("flowName"),"");
		 String sWhere = "flowName=?";
		 boolean isExite = true;
		 try {
			isExite = appFlowService.existData(AppFlow.class.getName(), sWhere, sFlowName);
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("校验工作流唯一性失败。", e);
		}
		return isExite+"";
	 }
	/**
	 * 
	* Description:删除工作流信息 <BR>   
	* @author zhangzun
	* @date 2014-3-24 下午06:27:33
	* @param request
	* @param res
	* @return
	* @version 1.0
	 */
	@RequestMapping(params = "method=del")
	 @ResponseBody
	 public String del(HttpServletRequest request,HttpServletResponse res){
		   String sId = CMyString.showEmpty(request.getParameter("id"),"0");
		   String sJson = "";
		   try {
			   AppFlow appFlow = (AppFlow)appFlowService.findById(AppFlow.class, Long.valueOf(sId));
			   appFlowService.deleteAppFlow(Long.valueOf(sId));
			   sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
			   AppUser user = CrtlUtil.getCurrentUser(request);//当前登入用户取得
			   StringBuffer logMsg = new StringBuffer("用户");
			   logMsg.append(user.getUsername()).append("成功删除了名为“").append(appFlow.getFlowName()).append("”的工作流。");
			   logService.addAppLog(3, logMsg.toString(), CrtlUtil.getCurrentUser(request));
		   } catch (NumberFormatException e) {
			   sJson = jsonUtil.getJsonStr("300","数据格式有误！", "","", "", "");
			   e.printStackTrace();
			   loger.error("参数不合法。", e);
		   } catch (Exception e) {
			   sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			   e.printStackTrace();
			   loger.error("删除流程失败。", e);
		}
		   
		   return sJson;
	 }
    /**
     * 
    * Description:获取工作流的编辑页面 <BR>   
    * @author zhangzun
    * @date 2014-3-24 下午05:33:03
    * @param request
    * @param res
    * @return String
    * @version 1.0
     */
	@RequestMapping(params = "method=flow_edite")
	 public String fieldEdite(HttpServletRequest request,HttpServletResponse res){
		 String sId = CMyString.showEmpty(request.getParameter("id"),"0");
		 AppFlow flow = null;
		 try {
			 flow = (AppFlow) appFlowService.findById(AppFlow.class, Long.valueOf(sId));
		 }catch (NumberFormatException e) {
				e.printStackTrace();
				loger.error("参数不合法。", e);
		 }
		 if("0".equals(sId)){
			 request.setAttribute("method", "add");
		 }else{
			 request.setAttribute("method", "edite");
		 }
		 request.setAttribute("flow", flow);
		 return "appadmin/wcmflow/flow_edite";
	}
	/**
	 * 
	* Description:查询工作流下面的所有节点<BR>   
	* @author zhangzun
	* @date 2014-3-25 上午10:02:45
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=listNode")
	public String listNode(HttpServletRequest request, HttpServletResponse res) {
		String sWcmFlowId = CMyString.showEmpty(request.getParameter("id"), "0");
		AppFlow appFlow = null;
		int nodeCount = 0;
		List<Object> flowList = null;
		try {
			//获取工作流列表
			flowList = appFlowService.find("flowName,flowDesc", AppFlow.class.getName(), "", "flowId desc", null);
			//String sFlowId = configService.findSysConfigCon("WCM_FLOW_" + sWcmFlowId, "0");
			//long nFlowId = Long.valueOf(CMyString.showEmpty(sFlowId, "0")); // 工作流id
			appFlow = (AppFlow)appFlowService.findObject(AppFlow.class.getName(), "flowDesc = ?", sWcmFlowId);
			nodeCount = appFlowService.count(AppFlowNode.class.getName(), "flowId = ?", appFlow.getFlowId());
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询流程列表失败。", e);
		}
		request.setAttribute("flowList", flowList);
		request.setAttribute("appFlow", appFlow);
		request.setAttribute("nodeCount", nodeCount);
		// request.setAttribute("page", page);
		// request.setAttribute("nodeList",page.getLdata());
		return "appadmin/wcmflow/flow";
	}
	/**
	 * 
	* Description:删除工作流节点<BR>   
	* @author zhangzun
	* @date 2014-3-25 下午02:15:33
	* @param request
	* @param res
	* @return String
	* @version 1.0
	 */
	@RequestMapping(params = "method=delNode")
	@ResponseBody
	 public String delNode(HttpServletRequest request,HttpServletResponse res){
	   String sId = CMyString.showEmpty(request.getParameter("id"),"0");
	   String sFlowId = CMyString.showEmpty(request.getParameter("flowId"),"0");
	   String sJson = "";
	   try {
		    AppFlowNode appFlowNode = (AppFlowNode)appFlowService.findById(AppFlowNode.class, Long.valueOf(sId));
			appFlowService.deleteFlowNode(Long.valueOf(sFlowId), Long.valueOf(sId));
			sJson = jsonUtil.getJsonStr("200","操作成功！", "t_listNode","t_listNode", "", "");
			AppUser user = CrtlUtil.getCurrentUser(request);//当前登入用户取得
			AppFlow appFlow = (AppFlow)appFlowService.findById(AppFlow.class, Long.valueOf(sId));
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功删除了“").append(appFlow.getFlowName()).append("”工作流中的“").append(appFlowNode.getNodeName()).append("”节点。");
			logService.addAppLog(3, logMsg.toString(), user);
		} catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300","数据格式有误！", "","", "", "");
			e.printStackTrace();
			loger.error("参数不合法。", e);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			e.printStackTrace();
			loger.error("删除流程节点失败。", e);
		}
		   return sJson;
	 }
	 /**
	  * 
	 * Description:校验工作流节点是否存在<BR>   
	 * @author zhangzun
	 * @date 2014-3-25 下午02:16:29
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	  */
	 @RequestMapping(params = "method=checkNode")
	 @ResponseBody
	 public String checkNode(HttpServletRequest request,HttpServletResponse res){
		 long nFlowId = Long.valueOf(CMyString.showEmpty(request.getParameter("flowId"),"0"));
		 String sNodeName = CMyString.showEmpty(request.getParameter("nodeName"),"");
		 List<Object> parameters = new ArrayList<Object>();
		 parameters.add(nFlowId);
		 parameters.add(sNodeName);
		 boolean isExite = true;
		 try {
			isExite = appFlowService.existData(AppFlowNode.class.getName(), "flowId = ? and nodeName = ?", parameters);
		 } catch (Exception e) {
			e.printStackTrace();
			loger.error("校验工作流节点是否存在失败。", e);
		}
		return isExite+"";
	 }
	 /**
	  * 
	 * Description:添加工作流节点 <BR>   
	 * @author zhangzun
	 * @date 2014-3-25 下午02:30:32
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	  */
	 @RequestMapping(params = "method=addNode")
	 @ResponseBody
	 public String addNode(HttpServletRequest request,HttpServletResponse res){
		   String sFlowId = CMyString.showEmpty(request.getParameter("flowId"),"0");
		   String showRule= CMyString.showEmpty(request.getParameter("showRule"),"");
		   String sNodeName= CMyString.showEmpty(request.getParameter("fnodeName"),"");
		   String sNodeDesc= CMyString.showEmpty(request.getParameter("fnodeDesc"),"");
		   String sLimitDayNum= CMyString.showEmpty(request.getParameter("limitDayNum"),"0");
		   String sNodeDocStatus= CMyString.showEmpty(request.getParameter("nodeDocStatus"),"0");
		   String sTogether= CMyString.showEmpty(request.getParameter("together"),"0");
		   String sIsassign= CMyString.showEmpty(request.getParameter("isassign"),"0");
		   String sIsaccept= CMyString.showEmpty(request.getParameter("isaccept"),"0");
		   String sIsemail= CMyString.showEmpty(request.getParameter("isemail"),"0");
		   String sIsmessage= CMyString.showEmpty(request.getParameter("ismessage"),"0");
		   String sIsFinish= CMyString.showEmpty(request.getParameter("isFinish"),"0");
		   String sRuleName= CMyString.showEmpty(request.getParameter("ruleName"),"");
		   String sNodeUser= CMyString.showEmpty(request.getParameter("nodeUser"),"");
		   String sNodeDep= CMyString.showEmpty(request.getParameter("nodeDep"),"");
		   String sOperRuleName= CMyString.showEmpty(request.getParameter("operRuleName"),"");
		   //String sNodeRole= CMyString.showEmpty(request.getParameter("nodeRole"),"");
		   String sJson = "";
		   AppFlowNode flowNode = null;
		   try {
			AppUser user = CrtlUtil.getCurrentUser(request);
			int nNodeOrder = Integer.valueOf(CMyString.showEmpty(request.getParameter("nodeOrder"),"-1"));
			flowNode = new AppFlowNode();
			flowNode.setFlowId(Long.valueOf(sFlowId));
			flowNode.setNodeName(sNodeName);
			flowNode.setNodeDesc(sNodeDesc);
			flowNode.setLimitDayNum(Integer.valueOf(sLimitDayNum));
			flowNode.setNodeDocStatus(Long.valueOf(sNodeDocStatus));
			flowNode.setTogether(Integer.valueOf(sTogether));
			flowNode.setIsassign(Integer.valueOf(sIsassign));
			flowNode.setIsaccept(Integer.valueOf(sIsaccept));
			flowNode.setIsemail(Integer.valueOf(sIsemail));
			flowNode.setIsmessage(Integer.valueOf(sIsmessage));
			flowNode.setIsFinish(Integer.valueOf(sIsFinish));
			//flowNode.setCruser(user.getUsername());
			if("1".equals(showRule)){
				flowNode.setRuleName(sRuleName);
			}else{
				flowNode.setNodeUser(sNodeUser);
				flowNode.setNodeDep(sNodeDep);
				//flowNode.setNodeRole(sNodeRole);
			}
			flowNode.setNodeOrder(nNodeOrder);
			//flowNode.setOperRuleName(sOperRuleName);
			if(!sOperRuleName.isEmpty()){
				flowNode.setOperRuleName(sOperRuleName);
				flowNode.setRuleName("");
				flowNode.setNodeUser("");
				flowNode.setNodeDep("");
			}
			appFlowService.addFlowNode(flowNode);
			//sJson = jsonUtil.getJsonStr("200","操作成功！", "t_listNode","t_listNode", "closeCurrent", "");
			sJson="{\"message\":\""+CMyString.native2Ascii(sNodeName)+"\",\"callbackType\":\"closeCurrent\"}";
			//AppFlow appFlow = (AppFlow)appFlowService.findById(AppFlow.class, Long.valueOf(sFlowId));
			/*StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功给名为“").append(appFlow.getFlowName()).append("”工作流添加了“").append(sNodeName).append("”节点。");
			 logService.addAppLog(1, logMsg.toString(), CrtlUtil.getCurrentUser(request));*/
		   } catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300","数据格式有误！", "","", "", "");
			e.printStackTrace();
			loger.error("参数不合法。", e);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			e.printStackTrace();
			loger.error("添加工作流节点 失败。", e);
		}
		return sJson;
	 }
	 /**
	  * 
	  * Description:修改工作流节点 <BR>   
	  * @author zhangzun
	  * @date 2014-3-25 下午02:30:32
	  * @param request
	  * @param res
	  * @return
	  * @version 1.0
	  */
	 @RequestMapping(params = "method=editeNode")
	 @ResponseBody
	 public String editeNode(HttpServletRequest request,HttpServletResponse res){
		 String sId = CMyString.showEmpty(request.getParameter("id"),"0");
		   String sNodeName= CMyString.showEmpty(request.getParameter("fnodeName"),"");
		   String showRule= CMyString.showEmpty(request.getParameter("showRule"),"");
		   String sNodeDesc= CMyString.showEmpty(request.getParameter("fnodeDesc"),"");
		   String sLimitDayNum= CMyString.showEmpty(request.getParameter("limitDayNum"),"0");
		   String sNodeDocStatus= CMyString.showEmpty(request.getParameter("nodeDocStatus"),"0");
		   String sTogether= CMyString.showEmpty(request.getParameter("together"),"0");
		   String sIsassign= CMyString.showEmpty(request.getParameter("isassign"),"0");
		   String sIsaccept= CMyString.showEmpty(request.getParameter("isaccept"),"0");
		   String sIsemail= CMyString.showEmpty(request.getParameter("isemail"),"0");
		   String sIsmessage= CMyString.showEmpty(request.getParameter("ismessage"),"0");
		   String sIsFinish= CMyString.showEmpty(request.getParameter("isFinish"),"0");
		   String sRuleName= CMyString.showEmpty(request.getParameter("ruleName"),"");
		   String sNodeUser= CMyString.showEmpty(request.getParameter("nodeUser"),"");
		   String sNodeDep= CMyString.showEmpty(request.getParameter("nodeDep"),"");
		   String sOperRuleName= CMyString.showEmpty(request.getParameter("operRuleName"),"");
		   //int nNodeOrder = Integer.valueOf(CMyString.showEmpty(request.getParameter("nodeOrder"),"-1"));
		   //String sNodeRole= CMyString.showEmpty(request.getParameter("nodeRole"),"");
		   String sJson = "";
		   AppFlowNode flowNode = null;
		   try {
			flowNode = (AppFlowNode) appFlowService.findById(AppFlowNode.class, Long.valueOf(sId));
			flowNode.setNodeName(sNodeName);
			flowNode.setNodeDesc(sNodeDesc);
			flowNode.setLimitDayNum(Integer.valueOf(sLimitDayNum));
			flowNode.setNodeDocStatus(Long.valueOf(sNodeDocStatus));
			flowNode.setTogether(Integer.valueOf(sTogether));
			flowNode.setIsassign(Integer.valueOf(sIsassign));
			flowNode.setIsaccept(Integer.valueOf(sIsaccept));
			flowNode.setIsemail(Integer.valueOf(sIsemail));
			flowNode.setIsmessage(Integer.valueOf(sIsmessage));
			flowNode.setIsFinish(Integer.valueOf(sIsFinish));
			if("1".equals(showRule)){
				flowNode.setRuleName(sRuleName);
				flowNode.setNodeUser("");
				flowNode.setNodeDep("");
				//flowNode.setNodeRole("");
			}else{
				flowNode.setNodeUser(sNodeUser);
				flowNode.setNodeDep(sNodeDep);
				//flowNode.setNodeRole(sNodeRole);
				flowNode.setRuleName("");
			}
			//flowNode.setNodeOrder(nNodeOrder);
			//flowNode.setOperRuleName(sOperRuleName);
			if(!sOperRuleName.isEmpty()){
				flowNode.setRuleName("");
				flowNode.setNodeUser("");
				flowNode.setNodeDep("");
			}
			flowNode.setOperRuleName(sOperRuleName);
			appFlowService.update(flowNode);
			//sJson = jsonUtil.getJsonStr("200","操作成功！", "t_listNode","t_listNode", "closeCurrent", "");
			sJson="{\"message\":\""+CMyString.native2Ascii(sNodeName)+"\",\"callbackType\":\"closeCurrent\"}";
			/*AppUser user = CrtlUtil.getCurrentUser(request);//当前登入用户取得
			AppFlow appFlow = (AppFlow)appFlowService.findById(AppFlow.class, flowNode.getFlowId());
			StringBuffer logMsg = new StringBuffer("用户");
			logMsg.append(user.getUsername()).append("成功修改了“").append(appFlow.getFlowName()).append("”工作流中的“").append(sNodeName).append("”节点信息。");
			logService.addAppLog(1, logMsg.toString(), user);*/
		   } catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300","数据格式有误！", "","", "", "");
			e.printStackTrace();
			loger.error("参数不合法。", e);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			e.printStackTrace();
			loger.error("修改工作流节点失败。", e);
		}
		return sJson;
	 }
	/**
	 * 
	 * Description:添加工作流节点 <BR>   
	 * @author zhangzun
	 * @date 2014-3-25 下午02:30:32
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=node_edite")
	public String nodeEdite(HttpServletRequest request, HttpServletResponse res) {
		//String sId = CMyString.showEmpty(request.getParameter("id"), "0");
		String sNodeName = CMyString.showEmpty(request.getParameter("nodeName"), "");
		long nFlowId = Long.valueOf(CMyString.showEmpty(request.getParameter("flowId"), "0"));
		//开始节点存在标识
		boolean startFlag = false;
		//结束节点存在标识
		boolean endFlag = false;
		AppFlowNode flowNode = null;
		AppFlowNode beforeNode = null;
		List<Object> statuts = null;
		String sUserNames = "";
		String sOrgNames = "";
		List<Map<String, String>> userRuleList = null;
		List<Map<String, String>> autoUserRuleList = null;
		String sMethod = "addNode";
		try {
			sNodeName = java.net.URLDecoder.decode(sNodeName,"UTF-8");
			Map<String,List<Map<String, String>>> ruleMap = appFlowService.parseAppGuid("userRule");
			if(ruleMap.size() > 0){
				userRuleList = ruleMap.get("userRule");
				autoUserRuleList = ruleMap.get("autoUserRule");
			}
			//flowNode = (AppFlowNode) appFlowService.findById(AppFlowNode.class, Long.valueOf(sId));
			List<Object> paramters = new ArrayList<Object>();
			paramters.add(nFlowId);
			paramters.add(sNodeName);
			flowNode = (AppFlowNode)appFlowService.findObject(AppFlowNode.class.getName(), "flowId = ? and nodeName = ?", paramters);
			statuts = appFlowService.find("", AppDatastatus.class.getName(), "", "crtime", null);
			startFlag = appFlowService.hasStartOrEndNode(nFlowId, 0);
			endFlag = appFlowService.hasStartOrEndNode(nFlowId, 1);
			if (flowNode != null) {
				//if (flowNode.getNodeOrder() > 1)
				//	beforeNode = appFlowService.getFlowNode(Long.valueOf(sFlowId), flowNode.getNodeOrder(), 0);
				if (!CMyString.isEmpty(flowNode.getNodeUser())) {
					List<Object> lUserName = appFlowService.findByIds("username", AppUser.class.getName(), "userId", flowNode.getNodeUser());
					sUserNames = CMyString.join((ArrayList<Object>) lUserName, ",");
				}
				if (!CMyString.isEmpty(flowNode.getNodeDep())) {
					List<Object> lGroupName = appFlowService.findByIds("gname", AppGroup.class.getName(), "groupId", flowNode.getNodeDep());
					sOrgNames = CMyString.join((ArrayList<Object>) lGroupName, ",");
				}
				sMethod = "editeNode";
			}
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询节点信息失败。", e);
		}
		request.setAttribute("startFlag", startFlag);
		request.setAttribute("endFlag", endFlag);
		request.setAttribute("method", sMethod);
		/*if (beforeNode != null) {
			request.setAttribute("beforName", beforeNode.getNodeName());
		}*/
		request.setAttribute("orgNames", sOrgNames);
		request.setAttribute("userNames", sUserNames);
		request.setAttribute("statuts", statuts);
		request.setAttribute("flowId", nFlowId);
		request.setAttribute("node", flowNode);
		request.setAttribute("userRule", userRuleList);
		request.setAttribute("autoUserRule", autoUserRuleList);
		return "appadmin/wcmflow/node_edite";
	}
	 /**
	  * 
	 * Description:添加工作流节点 <BR>   
	 * @author zhangzun
	 * @date 2014-3-25 下午02:30:32
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	  */
	 @RequestMapping(params = "method=order_edite")
	 public String orderEdite(HttpServletRequest request,HttpServletResponse res){
		   String sId = CMyString.showEmpty(request.getParameter("id"),"0");
		   String sFlowid = CMyString.showEmpty(request.getParameter("flowId"),"0");
		   int maxOrder = 0;
		   AppFlowNode flowNode = null;
		   AppFlowNode lastNode = null;
		   try {
			   flowNode = (AppFlowNode) appFlowService.findById(AppFlowNode.class,
					   Long.valueOf(sId));
			   lastNode = appFlowService.getLastFlowNode(Long.valueOf(sFlowid));
			   maxOrder = lastNode.getNodeOrder();
		   } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询流程列表失败。", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询流程列表失败。", e);
		}
		   request.setAttribute("flowId", sFlowid);
		   request.setAttribute("id", sId);
		   request.setAttribute("nodeName", flowNode.getNodeName());
		   request.setAttribute("maxOrder",maxOrder);
		   return "appadmin/wcmflow/order_edite";
		 }
	 /**
	  * 
	 * Description:调整工作流节点的顺序<BR>   
	 * @author zhangzun
	 * @date 2014-3-25 下午02:16:29
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	  */
	 @RequestMapping(params = "method=changeOrder")
	 @ResponseBody
	 public String changeOrder(HttpServletRequest request,HttpServletResponse res){
		 String sId = CMyString.showEmpty(request.getParameter("id"),"0");
		 String sFlowid = CMyString.showEmpty(request.getParameter("flowId"),"0");
		 String sOrder = CMyString.showEmpty(request.getParameter("order"),"2");
		 String sJson = "";
		 try {
			appFlowService.orderByFlowNode(Long.valueOf(sFlowid), Long.valueOf(sId),
					Integer.valueOf(sOrder));
			sJson = jsonUtil.getJsonStr("200","操作成功！", "t_listNode","t_listNode", "closeCurrent", "");
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "t_listNode","t_listNode", "", "");
			e.printStackTrace();
			loger.error("查询流程列表失败。", e);
		}
		return sJson;
	 }
	 /**
	     * 
	    * Description:获取添加用户的对话框 <BR>   
	    * @author zhangzun
	    * @date 2014-3-24 下午05:33:03
	    * @param request
	    * @param res
	    * @return String
	    * @version 1.0
	     */
		@RequestMapping(params = "method=userDialog")
		 public String userDialog(HttpServletRequest request,HttpServletResponse res){
			 String sId = CMyString.showEmpty(request.getParameter("id"),"0");//节点id
			 request.setAttribute("id", sId);
			 return "appadmin/wcmflow/userDialog";
		}
		 /**
	     * 
	    * Description:获取添加组织的对话框 <BR>   
	    * @author zhangzun
	    * @date 2014-3-24 下午05:33:03
	    * @param request
	    * @param res
	    * @return String
	    * @version 1.0
	     */
		@RequestMapping(params = "method=orgDialog")
		 public String orgDialog(HttpServletRequest request,HttpServletResponse res){
			 //String sId = CMyString.showEmpty(request.getParameter("id"),"0");//节点id
			 //request.setAttribute("id", sId);
			 return "appadmin/wcmflow/orgDialog";
		}
		/**
	     * 
	    * Description:获取部门用户<BR>   
	    * @author zhangzun
	    * @date 2014-3-24 下午05:33:03
	    * @param request
	    * @param res
	    * @return String
	    * @version 1.0
	     */
		@RequestMapping(params = "method=nodeUser")
		 public String nodeUser(HttpServletRequest request,HttpServletResponse res){
				String sdeptID = CMyString.showEmpty(request.getParameter("deptID"),"0"); //部门ID为null就查所有的用户
				String sFiledValue = CMyString.showEmpty(request.getParameter("sFiledValue"),"");//检索内容
				String selectFiled = CMyString.showEmpty(request.getParameter("selectFiled"),"");	//检索字段
				String sFlag = CMyString.showEmpty(request.getParameter("flag"),"0"); //查询角色用户的标示字段
				String currPage = CMyString.isEmpty(request.getParameter("pageNum"))?Global.DEFUALT_STARTPAGE:request.getParameter("pageNum");
				String pageSize = CMyString.isEmpty(request.getParameter("numPerPage"))?"10":request.getParameter("numPerPage");	  
				Page page = null;
				String sField = "";
				if(!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)){
					sField = selectFiled;
				}
				try {
					page = appGroupService.findGrpUserSel(Long.valueOf(sdeptID),sField, sFiledValue,
							Integer.valueOf(currPage),
							Integer.valueOf(pageSize));
					request.setAttribute("users",page.getLdata());
					request.setAttribute("page", page);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					loger.error("参数不合法。", e);
				} catch (Exception e) {
					e.printStackTrace();
					loger.error("查询组织下的用户失败。", e);
				}
			 request.setAttribute("flag", sFlag);
			 request.setAttribute("deptID", sdeptID);
			 request.setAttribute("sFiledValue", sFiledValue);
			 request.setAttribute("selectFiled", selectFiled);
			 return "appadmin/wcmflow/nodeUser";
		}

	/**
	 * 保存流程信息
	 * Description:保存流程信息 <BR>
	 * @author liuzhuan
	 * @date 2014-3-25 下午02:30:32
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=saveWorkFlow")
	@ResponseBody
	public String saveWorkFlow(HttpServletRequest request, HttpServletResponse res) {
		String sJson = "";
		try {
			long nFlowId = Long.valueOf(CMyString.showEmpty(request.getParameter("flowId"), "0"));
			String sFlowJsonData = CMyString.showEmpty(request.getParameter("flowJsonData"), "");
			String sDelJsonData = CMyString.showEmpty(request.getParameter("delData"), "");
			appFlowService.saveWorkFlow(nFlowId, sFlowJsonData, sDelJsonData);
			// sJson = jsonUtil.getJsonStr("200","操作成功！",
			// "t_listNode","t_listNode", "closeCurrent", "");
			sJson = jsonUtil.getJsonStr("200", "操作成功！", "", "", "", "");
		} catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300", "数据格式有误！", "", "", "", "");
			e.printStackTrace();
			loger.error("参数不合法。", e);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300", "操作失败！", "", "", "", "");
			e.printStackTrace();
			loger.error("保存流程信息失败。", e);
		}
		return sJson;
	}
	/**
	 *  获取组织树结构
	 * Description: 获取组织树结构<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=getGroupTree")
	@ResponseBody
	public String getGroupTree(HttpServletRequest request, HttpServletResponse res) {
		long pid = Long.valueOf(CMyString.showEmpty(request.getParameter("pid"), "0"));
		long id = Long.valueOf(CMyString.showEmpty(request.getParameter("id"), "0"));
		String groupType = request.getParameter("groupType");
		String rel = request.getParameter("rel");
		String sWhere = "parentId = ?";
//		if("0".equals(groupType)){
//			sWhere += "and isIndependent=1";
//		}
		if(pid != 0 && id == 0){
			id = pid;
			sWhere = "groupId = ?";
		}
		//String sJson = "[{id:0,pId:-10,name:\""+CMyString.native2Ascii("组织管理")+"\",url:\"group.do?method=listGroup\",target:\"navTab\",open:false,isParent:true}]";
		String sJson = "";
		List<TreeNode> list = new ArrayList<TreeNode>();
		List<Object> listData = null;
		try {
			listData=appFlowService.find("", AppGroup.class.getName(), sWhere,"grouporder asc", id);//appGroupService.findChildGroup(Long.valueOf(id), "grouporder asc");
		} catch (NumberFormatException e) {	
			e.printStackTrace();
			loger.error("参数不合法。",e);
		} catch (Exception e) {	
			e.printStackTrace();
			loger.error("查询子组织失败。",e);
		}
		if(!listData.isEmpty()){
			for(int i=0;i<listData.size();i++){
				TreeNode treeNode = new TreeNode();
				AppGroup appGroup = (AppGroup)listData.get(i);
				treeNode.setId(appGroup.getGroupId().toString());
				treeNode.setPId(appGroup.getParentId().toString());
	
				treeNode.setName(CMyString.native2Ascii(appGroup.getGname()));
				//treeNode.setUrl("group.do?method=listGroup&groupIds="+appGroup.getGroupId().toString());
				treeNode.setIsParent(appGroup.getContainChild()==1?true:false);
				//treeNode.setTarget("_blank");
				treeNode.setRel(rel);
				if("0".equals(groupType) && appGroup.getIsIndependent() == 0){
					treeNode.setChkDisabled(true);
				}
				list.add(treeNode);
			}
		}
		sJson = TreeNode.getTreeJsonStr(list);
		//System.out.println(sJson);
		return sJson;
	}
	/**
	 *  获取有默认父节点的组织树结构
	 * Description: 获取组织树结构<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-28 上午11:23:03
	 * @param request
	 * @param res
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(params = "method=getParGroupTree")
	@ResponseBody
	public String getParGroupTree(HttpServletRequest request, HttpServletResponse res) {
		long id = Long.valueOf(CMyString.showEmpty(request.getParameter("id"), "0"));
		String josnStr = "[{id:0,pId:-10,name:\""+CMyString.native2Ascii("组织机构")+"\",open:true,isParent:true,children:&_&}]";
		String sJson = "";
		List<TreeNode> list = new ArrayList<TreeNode>();
		List<Object> listData = null;
		try {
			listData=appFlowService.find("", AppGroup.class.getName(), "parentId = ?","grouporder asc", id);//appGroupService.findChildGroup(Long.valueOf(id), "grouporder asc");
		} catch (NumberFormatException e) {	
			e.printStackTrace();
			loger.error("数据不合法。",e);
		} catch (Exception e) {	
			e.printStackTrace();
			loger.error("查询子组织失败。",e);
		}
		for(int i=0;i<listData.size();i++){
			TreeNode treeNode = new TreeNode();
			AppGroup appGroup = (AppGroup)listData.get(i);
			treeNode.setId(appGroup.getGroupId().toString());
			treeNode.setPId(appGroup.getParentId().toString());

			treeNode.setName(CMyString.native2Ascii(appGroup.getGname()));
			//treeNode.setUrl("group.do?method=listGroup&groupIds="+appGroup.getGroupId().toString());
			treeNode.setIsParent(appGroup.getContainChild()==1?true:false);
			//treeNode.setTarget("_blank");
			//treeNode.setRel(rel);
			list.add(treeNode);
		}
		sJson = TreeNode.getTreeJsonStr(list);
		if(id==0){
			sJson = josnStr.replace("&_&", sJson);
		}
		return sJson;
	}
}
