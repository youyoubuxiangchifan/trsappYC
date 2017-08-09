package com.trs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.dbhibernate.Page;
import com.trs.model.AppFieldInfo;
import com.trs.model.AppTableInfo;
import com.trs.model.AppUser;
import com.trs.service.AppLogService;
import com.trs.service.AppMetaDataService;
import com.trs.util.CMyString;
import com.trs.util.CrtlUtil;
import com.trs.util.Global;
import com.trs.util.jsonUtil;

/**
 * 
 * Description:用于处理元数据的操作请求<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author zhangzun
 * @ClassName: MetaDataController
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-19 上午09:48:01
 * @version 1.0
 */
@Controller
@RequestMapping("/meta.do")
public class MetaDataController {
	
	@Autowired
	private AppMetaDataService mservice;
	@Autowired
	private AppLogService logService;
	private static  Logger loger =  Logger.getLogger(RoleController.class);
   /**
    * 
   * Description:遍历查询元数据的信息 <BR>   
   * @author zhangzun
   * @date 2014-3-19 上午09:53:43
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
		page = mservice.findPage("", AppTableInfo.class.getName(), "tableType != -1", "crtime desc",
				Integer.valueOf(currPage),Integer.valueOf(pageSize), null);
	 } catch (Exception e) {
		e.printStackTrace();
		loger.error("查询元数据列表失败。", e);
	}
	   request.setAttribute("page", page);
	   request.setAttribute("metaList",page.getLdata());
	   return "appadmin/metadata/meta_list";
   }
 /**
  * 
 * Description:添加元数据并创建表 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:23:01
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=add")
 @ResponseBody
 public String add(HttpServletRequest request,HttpServletResponse res){
	 String sJson = "";
	 try {
		   String sAnotherName = request.getParameter("anotherName");
		   String sTableName = request.getParameter("tableName");
		   String sTableType = CMyString.showEmpty(request.getParameter("tableType"), "0");
		   //String sMainTable = request.getParameter("mainTable");
		   //String sUseSysField = request.getParameter("useSysField");
		   String sTableDesc = request.getParameter("tableDesc");
		   int nTableType = Integer.valueOf(sTableType);
		   AppTableInfo tableInfo = new AppTableInfo();
		   tableInfo.setAnotherName(sAnotherName);
		   tableInfo.setTableName(sTableName);
		   tableInfo.setTableDesc(sTableDesc);
		   tableInfo.setTableType(nTableType);
//	   if(Global.METADATA_TYPE_OPINION==nTableType)
//		   tableInfo.setMainTableId(Long.valueOf(sMainTable));
		   //tableInfo.setUseSysField(Integer.valueOf(sUseSysField));
		   //tableInfo.setCruser(cruser);
		   tableInfo.setCrtime(new Date());
		   AppUser user = CrtlUtil.getCurrentUser(request);
		   mservice.createAppTable(tableInfo,user);
		   sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
		   StringBuffer logMsg = new StringBuffer("用户");
		   logMsg.append(user.getUsername()).append("成功添加了名为").append(tableInfo.getTableName()).append("的元数据。");
		   logService.addAppLog(1, logMsg.toString(),user);
	   } catch (Exception e) {
		   sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", ""); 
		   e.printStackTrace();
		   loger.error("添加元数据失败。", e);
	}
	   return sJson;
   }
 /**
  * 
 * Description:编辑元数据并创建表 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:23:01
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=edite")
 @ResponseBody
 public String edite(HttpServletRequest request,HttpServletResponse res){
	   String anotherName = request.getParameter("anotherName");
	   String sTableDesc = request.getParameter("tableDesc");
	   String sId = request.getParameter("id");
	   sId = CMyString.showEmpty(sId, "0");
	   AppTableInfo tableInfo =null;
	   String sJson = "";
	   try {
		   tableInfo = (AppTableInfo) mservice.findById(AppTableInfo.class,Long.valueOf(sId));
		   tableInfo.setAnotherName(anotherName);
		   tableInfo.setTableDesc(sTableDesc);
		   mservice.updateAppTable(tableInfo);
		   sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "closeCurrent", "");
		   AppUser user = CrtlUtil.getCurrentUser(request);
		   StringBuffer logMsg = new StringBuffer("用户");
		   logMsg.append(user.getUsername()).append("成功修改了名为").append(tableInfo.getTableName()).append("的元数据。");
		   logService.addAppLog(2, logMsg.toString(), user);
	   } catch (Exception e) {
		   sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", ""); 
		   e.printStackTrace();
		   loger.error("修改元数据失败。", e);
	}
	   return sJson;
   }
 /**
  * 
 * Description:删除元数据并删除表 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:26:14
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=del")
 @ResponseBody
 public String del(HttpServletRequest request,HttpServletResponse res){
	   String sId = CMyString.showEmpty(request.getParameter("id"),"0");
	   String sJson = "";
	   try {
		   AppTableInfo tableInfo = (AppTableInfo) mservice.findById(AppTableInfo.class,Long.valueOf(sId));
		   mservice.deleteAppTable(Long.valueOf(sId));
		   sJson = jsonUtil.getJsonStr("200","操作成功！", "","", "", "");
		   AppUser user = CrtlUtil.getCurrentUser(request);
		   StringBuffer logMsg = new StringBuffer("用户");
		   logMsg.append(user.getUsername()).append("成功删除了名为").append(tableInfo.getTableName()).append("的元数据。");
		   logService.addAppLog(3, logMsg.toString(), user);
	   } catch (NumberFormatException e) {
		sJson = jsonUtil.getJsonStr("300","数据格式有误！", "","", "", "");
		e.printStackTrace();
		loger.error("参数不合法。", e);
	} catch (Exception e) {
		sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
		e.printStackTrace();
		loger.error("删除元数据失败。", e);
	}
	   return sJson;
 }
 /**
  * 
 * Description:新增元数据信息 编辑页<BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:52:29
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=meta_add")
 public String metaAdd(HttpServletRequest request,HttpServletResponse res){
	   String sWhere = "tableType=?";
	   List<Object> mateList = null;
	   List<Object> param = new ArrayList<Object>();
	   param.add(0);
	   try {
		   mateList = mservice.find("", "AppTableInfo",sWhere, "crtime desc",param);
	   } catch (Exception e) {
		   e.printStackTrace();
		   loger.error("查询主表元数据信息列表失败。", e);
	   }
	  // mservice.findPage(sSelectFields, sFrom, sWhere, sOrder, nStartPage, nPageSize, paramters);
	   request.setAttribute("mateList", mateList);
	   return "appadmin/metadata/meta_add";
}
 /**
  * 
 * Description:编辑元数据信息页面 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:52:29
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=meta_edite")
 public String metaEdite(HttpServletRequest request,HttpServletResponse res){
	   String sId = request.getParameter("id");
	   sId = CMyString.showEmpty(sId, "0");
	   AppTableInfo tableInfo = (AppTableInfo) mservice.findById(AppTableInfo.class,Long.valueOf(sId));

	   // mservice.findPage(sSelectFields, sFrom, sWhere, sOrder, nStartPage, nPageSize, paramters);
	   request.setAttribute("meta", tableInfo);
	   return "appadmin/metadata/meta_edite";
}
 /**
  * 
 * Description:验证表名的唯一性 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:32:08
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=check")
 @ResponseBody
 public String check(HttpServletRequest request,HttpServletResponse res){
	 	String sTableName = CMyString.showEmpty(request.getParameter("tableName")); //角色名称
	 	List<Object> param = new ArrayList<Object>();
	 	param.add(Global.DB_TABLE_PREFIX+sTableName.toLowerCase());//增加表名前缀并转为小写
	 	boolean isExist = false;
	 	try {
	 		isExist = mservice.existData("AppTableInfo", "tableName=?", param);
	 	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("验证表名的唯一性失败。", e);
		}
		if(isExist)
	 	   return "true";
		else
		   return "false";
 	}
 /**
  * 
 * Description:验证表字段的唯一性和合法性（非关键字）<BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:32:08
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=checkField")
 @ResponseBody
 public String checkField(HttpServletRequest request,HttpServletResponse res){
	 	String sFieldName = CMyString.showEmpty(request.getParameter("fieldName")); //角色名称
	 	String tableId = CMyString.showEmpty(request.getParameter("tableId")); //角色名称
	 	List<Object> param = new ArrayList<Object>();
	 	String sWhere = "";
	 	boolean isExist = false;
	 	boolean isKey = false;
	 	try {
	 		sWhere = "fieldName=? and tableId=?";
	 		param.add(sFieldName);
		 	param.add(Long.valueOf(tableId));
	 		isKey = mservice.isDBKeyWords(sFieldName);
	 		isExist = mservice.existData("AppFieldInfo", sWhere, param);
	 	} catch (Exception e) {
			e.printStackTrace();
			loger.error("验证表字段的唯一性和合法性失败。", e);
			return "false";
			
		}
		if(isExist || isKey)
	 	   return "true";
		else
		   return "false";
 	}

	/**
	 * 
	 * Description:查询元数据的字段信息并返回到列表页 <BR>
	 * 
	 * @author zhangzun
	 * @date 2014-3-20 下午02:27:05
	 * @param request
	 * @param res
	 * @return String
	 * @version 1.0
	 */
	@RequestMapping(params = "method=findFields")
	public String findFields(HttpServletRequest request, HttpServletResponse res) {
		String currPage = CMyString.isEmpty(request.getParameter("pageNum")) ? Global.DEFUALT_STARTPAGE
				: request.getParameter("pageNum");
		String pageSize = CMyString.isEmpty(request.getParameter("numPerPage")) ? Global.DEFUALT_PAGESIZE
				: request.getParameter("numPerPage");
		long nTableId = Long.valueOf(CMyString.showEmpty(request
				.getParameter("tableId"), "0"));// 元数据表id
		String sTableName = request.getParameter("tableName");// 元数据表名
		String selectFiled = request.getParameter("selectFiled");// 查询字段
		String sFiledValue = request.getParameter("sFiledValue");// 查询的值
		long nMainTableId = Long.valueOf(CMyString.showEmpty(request
				.getParameter("mainTableId"), "0"));// 主表编号
		long nItemTableId = Long.valueOf(CMyString.showEmpty(request
				.getParameter("itemTableId"), "0"));// 从表编号
		int tbType = 0;//表类型，供字段修改时判断字段类别使用，0通用，1主题，2意见表
		if(nItemTableId != 0 && nItemTableId == nTableId){
			tbType = 2;
		} else if(nItemTableId != 0 && nMainTableId == nTableId){
			tbType = 1;
		}
		String sWhere = "tableId = ?";
		List<Object> param = new ArrayList<Object>();
		if (nTableId == 0)
			nTableId = nMainTableId;
		param.add(nTableId);
		if (!CMyString.isEmpty(sFiledValue) && !CMyString.isEmpty(selectFiled)) {
			sWhere += " and " + selectFiled + " like ?";
			param.add("%" + sFiledValue + "%");
		}
		Page page = null;
		try {
			page = mservice.findPage("", "AppFieldInfo", sWhere, "crtime desc",
							Integer.valueOf(currPage), Integer
									.valueOf(pageSize), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询元数据的字段信息列表失败。", e);
		}
		request.setAttribute("tbType", tbType);
		request.setAttribute("selectFiled", selectFiled);
		request.setAttribute("sFiledValue", sFiledValue);
		request.setAttribute("tableId", nTableId);
		request.setAttribute("mainTableId", nMainTableId);
		request.setAttribute("itemTableId", nItemTableId);
		request.setAttribute("itemTableName", request
				.getParameter("itemTableName"));
		request.setAttribute("tableName", sTableName);
		request.setAttribute("page", page);
		request.setAttribute("fieldList", page.getLdata());
		return "appadmin/metadata/field_list";
	}
 /**
  * 
 * Description:删除元数据字段信息并删除元数据字段<BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:26:14
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=delField")
 @ResponseBody
 public String delField(HttpServletRequest request,HttpServletResponse res){
	   String sId = CMyString.showEmpty(request.getParameter("id"),"0");
	   String sJson = "";
	   try {
		   AppFieldInfo filedInfo = (AppFieldInfo) mservice.findById(AppFieldInfo.class,Long.valueOf(sId));
		   mservice.deleteTableField(Long.valueOf(sId));
		   sJson = jsonUtil.getJsonStr("200","操作成功！", "t_listField","t_listField", "", "");
		   AppUser user = CrtlUtil.getCurrentUser(request);
		   StringBuffer logMsg = new StringBuffer("用户");
		   logMsg.append(user.getUsername()).append("成功删除了名为").append(filedInfo.getFieldName()).append("的元数据字段信息。");
		   logService.addAppLog(3, logMsg.toString(), user);
	   } catch (NumberFormatException e) {
			sJson = jsonUtil.getJsonStr("300","数据格式有误！", "","", "", "");
			e.printStackTrace();
			loger.error("数据不合法。", e);
		} catch (Exception e) {
			sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "", "");
			e.printStackTrace();
			loger.error("删除元数据字段信息失败。", e);
		}
	   return sJson;
 }
 /**
  * 
 * Description:添加元数据字段信息页面 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:52:29
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=field_add")
 public String fieldAdd(HttpServletRequest request,HttpServletResponse res){
	   String sTableId = request.getParameter("tableId");
	   String sTableName = request.getParameter("tableName");
	   String sItemTableId = request.getParameter("itemTableId");
	   request.setAttribute("tableId", sTableId);
	   request.setAttribute("tableName", sTableName);
	   request.setAttribute("itemTableId", sItemTableId);
	   request.setAttribute("itemTableName", request.getParameter("itemTableName"));
	   return "appadmin/metadata/field_add";
}
 /**
  * 
 * Description:添加元数据字段信息页面 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:52:29
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=field_edite")
 public String fieldEdite(HttpServletRequest request,HttpServletResponse res){
	 String sId = CMyString.showEmpty(request.getParameter("id"),"0");
	 String fieldType = "";
	 AppFieldInfo fieldInfo = null;
	 try {
		 	fieldInfo = (AppFieldInfo) mservice.findById(AppFieldInfo.class, Long.valueOf(sId));
		 	fieldType = fieldInfo.getFieldType();
		 	if(!CMyString.isEmpty(fieldInfo.getFormFieldType()))
		 		fieldType += "," + fieldInfo.getFormFieldType();
	 }catch (NumberFormatException e) {
			e.printStackTrace();
			loger.error("参数不合法。", e);
	 }
	 request.setAttribute("tbType", request.getParameter("tbType"));
	 request.setAttribute("field", fieldInfo);
	 request.setAttribute("fieldType", fieldType);
	 return "appadmin/metadata/field_edite";
}
 /**
  * 
 * Description:新建元数据字段字段 <BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:23:01
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=addFiled")
 @ResponseBody
 public String addFiled(HttpServletRequest request,HttpServletResponse res){
	   String sFieldDesc = request.getParameter("fieldDesc");
	   String sFieldName = request.getParameter("fieldName");
	   String sFieldType = request.getParameter("fieldType");
	   String sEnmValue = request.getParameter("enmValue");
	   String sDblength = request.getParameter("dbLength");
	   //String sFieldLength = request.getParameter("dblength");
	   String sFieldStyle = request.getParameter("fieldStyle");
	   String sDefaultValue = request.getParameter("defaultValue");
	   String sNotNull = CMyString.showEmpty(request.getParameter("notNull"), "0");
	   String sNotEdit = CMyString.showEmpty(request.getParameter("notEdit"), "0");
	   String sHiddenField = CMyString.showEmpty(request.getParameter("hiddenField"), "0");
	   String sTableId = request.getParameter("tableId");
	   String sTableName = request.getParameter("tableName");
	   String fieldTypeArr[] = sFieldType.split(",");
	   String sFormFieldType = "";
	   if(fieldTypeArr != null && fieldTypeArr.length > 1){
		   sFieldType = fieldTypeArr[0];
		   sFormFieldType = fieldTypeArr[1];
	   }
	   AppFieldInfo filedInfo = new AppFieldInfo();
	   String sJson = "";
	   try {
		   AppUser user = CrtlUtil.getCurrentUser(request);
		   filedInfo.setTableId(Long.valueOf(sTableId));
		   filedInfo.setTableName(sTableName);
		   filedInfo.setFieldDesc(sFieldDesc);
		   filedInfo.setFieldName(sFieldName);
		   filedInfo.setFieldType(sFieldType);
		   filedInfo.setFormFieldType(sFormFieldType);
		   filedInfo.setCrtime(new Date());
		   if(!CMyString.isEmpty(sEnmValue))
			   filedInfo.setEnmValue(sEnmValue);
		   if(!CMyString.isEmpty(sDblength))
			   filedInfo.setDblength(Long.valueOf(sDblength));
		   if(!CMyString.isEmpty(sFieldStyle))
			   filedInfo.setFieldStyle(Integer.valueOf(sFieldStyle));
		   if(!CMyString.isEmpty(sDefaultValue))
			   filedInfo.setDefaultValue(sDefaultValue);
		   if(!CMyString.isEmpty(sNotNull))
			   filedInfo.setNotNull(Integer.valueOf(sNotNull));
		   if(!CMyString.isEmpty(sNotEdit))
			   filedInfo.setNotEdit(Integer.valueOf(sNotEdit));
		   if(!CMyString.isEmpty(sHiddenField))
			   filedInfo.setHiddenField(Integer.valueOf(sHiddenField));
		   filedInfo.setIsReserved(0);
		   filedInfo.setCruser(user.getUsername());
		   mservice.addTableField(filedInfo);
		   sJson = jsonUtil.getJsonStr("200","操作成功！", "t_listField","", "closeCurrent", "");
		   StringBuffer logMsg = new StringBuffer("用户");
		   logMsg.append(user.getUsername()).append("成功添加了名为").append(filedInfo.getFieldName()).append("的元数据字段信息。");
		   logService.addAppLog(1, logMsg.toString(),user);
	   } catch (Exception e) {
		   sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", ""); 
		e.printStackTrace();
		loger.error("添加元数据字段失败。", e);
	}
	   return sJson;
   }
 /**
  * 
 * Description:修改元数据字段<BR>   
 * @author zhangzun
 * @date 2014-3-19 上午11:23:01
 * @param request
 * @param res
 * @return String
 * @version 1.0
  */
 @RequestMapping(params = "method=editeFiled")
 @ResponseBody
 public String editeFiled(HttpServletRequest request,HttpServletResponse res){
	   String sId = CMyString.showEmpty(request.getParameter("id"),"0");
	   String sFieldDesc = request.getParameter("fieldDesc");
	   String sFieldName = request.getParameter("fieldName");
	   String sEnmValue = request.getParameter("enmValue");
	   String sDBLength = request.getParameter("dbLength");
	   //String sFieldStyle = request.getParameter("fieldStyle");
	   int nFieldStyle = Integer.valueOf(CMyString.showEmpty(request.getParameter("fieldStyle"), "-1"));
	   String sDefaultValue = request.getParameter("defaultValue");
	   String sNotNull = CMyString.showEmpty(request.getParameter("notNull"), "0");
	   String sNotEdit = CMyString.showEmpty(request.getParameter("notEdit"), "0");
	   String sHiddenField = CMyString.showEmpty(request.getParameter("hiddenField"), "0");
	   AppFieldInfo filedInfo = null;
	   String sJson = "";
	   try {
		   filedInfo = (AppFieldInfo) mservice.findById(AppFieldInfo.class,Long.valueOf(sId));
		   if(filedInfo!=null){
			   filedInfo.setFieldDesc(sFieldDesc);
			   filedInfo.setFieldName(sFieldName);
			   filedInfo.setEnmValue(sEnmValue);
			   if(!CMyString.isEmpty(sDBLength))
				   filedInfo.setDblength(Long.valueOf(sDBLength));
//			   if(!CMyString.isEmpty(sFieldStyle))
//				   filedInfo.setFieldStyle(Integer.valueOf(sFieldStyle));
			   if(filedInfo.getIsReserved() == 0){//当字段类别不为系统字段的时候才可以修改。
				   filedInfo.setFieldStyle(nFieldStyle);
			   }
			   filedInfo.setDefaultValue(sDefaultValue);
			   filedInfo.setNotNull(Integer.valueOf(sNotNull));
			   filedInfo.setNotEdit(Integer.valueOf(sNotEdit));
			   filedInfo.setHiddenField(Integer.valueOf(sHiddenField));
			   //mservice.update(filedInfo);
			   mservice.updateTableField(filedInfo);
			   sJson = jsonUtil.getJsonStr("200","操作成功！", "t_listField","", "closeCurrent", "");
			   AppUser user = CrtlUtil.getCurrentUser(request);
			   StringBuffer logMsg = new StringBuffer("用户");
			   logMsg.append(user.getUsername()).append("成功修改了名为").append(filedInfo.getFieldName()).append("的元数据字段信息。");
			   logService.addAppLog(2, logMsg.toString(), user);
		   }
		   } catch (Exception e) {
		   sJson = jsonUtil.getJsonStr("300","操作失败！", "","", "closeCurrent", ""); 
		   e.printStackTrace();
		   loger.error("修改元数据字段失败。", e);
	}
	   return sJson;
   }
}
