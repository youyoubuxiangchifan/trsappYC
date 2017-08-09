package com.trs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.trs.dao.impl.BaseDaoImpl;
import com.trs.dbhibernate.Page;
import com.trs.model.AppFieldRel;
import com.trs.model.AppInfo;
import com.trs.util.CMyString;

/**
 * 
 * Description:通用服务层<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: BaseService
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-12 下午05:57:33
 * @version 1.0
 */
@Service
public class BaseService {
	@Autowired
	@Qualifier("baseDaoImpl") 
	private BaseDaoImpl baseDao;
	
	public BaseDaoImpl getBaseDao() {
		return baseDao;
	}

	/**
	 * 保存对象数据
	 * Description: 保存对象数据<BR>   
	 * @author jin.yu
	 * @date 2014-3-12 下午08:01:40
	 * @param obj 数据对象
	 * @version 1.0
	 */
	public void save(Object obj){
		baseDao.save(obj);
	}
	/**
	 * 保存对象
	* Description:保存失败将抛出exception 异常<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-28 下午02:31:27
	* Last Modified:
	* @param obj 要保存的bean对象 
	* @throws Exception
	* @version 1.0
	 */
	public void saveObj(Object obj) throws Exception{
		baseDao.saveObj(obj);
	}
	
	
	/**
	 * 修改对象数据
	 * Description:  修改对象数据<BR>   
	 * @author jin.yu
	 * @date 2014-3-12 下午08:10:11
	 * @param obj 数据对象
	 * @version 1.0
	 */
	public void update(Object obj){
		baseDao.update(obj);
	}
	
	/**
	 * 
	 * Description:  删除对象数据<BR>   
	 * @author jin.yu
	 * @date 2014-3-12 下午08:13:03
	 * @param obj 数据对象
	 * @version 1.0
	 */
	public void delete(Object obj){
		baseDao.delete(obj);
	}
	
	/**
	 * 删除对象数据
	 * Description:  删除对象数据<BR>   
	 * @author jin.yu
	 * @date 2014-3-12 下午08:13:03
	 * @param clazz 数据对象class
	 * @param id 数据对象编号
	 * @version 1.0
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void delete(Class clazz, Long id) throws Exception{
		Object obj = baseDao.findById(clazz, id);
		if(obj == null){
			throw new Exception("要删除的对象不存在！");
		}
		baseDao.delete(obj);
	}
	
	/**
	 * 根据编号删除对象集合
	 * Description: 根据编号删除对象集合<BR>   
	 * @author jin.yu
	 * @date 2014-3-12 下午09:15:09
	 * @param sFrom String 要删除的对象。如：AppUser,不包含from关键字
	 * @param sFieldName String 要删除对象的条件字段名称。如：groupId
	 * @param sIds 要删除对象的ID String 以","隔开。
	 * @version 1.0
	 * @throws Exception 
	 */
	public void deleteAll(String sFrom, String sFieldName, String sIds) throws Exception{
		List<Object> list = findByIds(sFrom, sFieldName, sIds);
		baseDao.deleteAll(list);
	}
	/** 
	* Description: 执行原生SQL <BR>   
	* author liujian
	* date 2014-4-16 上午09:32:43
	* @param sql 查询条件 ，可以添加占位符
	* @param parameters  查询条件的值
	* @return 返回List对象，List存放Map对象
	* @throws Exception
	* version 1.0
	*/
	public List<Object> find(String sql,List<Object> parameters) throws Exception {
		List<Object> dataList = new ArrayList<Object>();
		dataList = baseDao.executeQueryObjs(sql,parameters);
		return dataList;
	}	
	/*******拼接hql的方法从basedaoimpl中移出到baseservice中******/
	/**
	 * 通用分页查询
	 * Description: 通用分页查询 <BR>
	 * 此方法用于封装支持where条件，查询部分字段，排序方式，分页操作。
	 * 注意： 	1.当需要查询部分字段时，需要在参数封装时实例化一个对象。 
	 * 		      如select new AppUser(param1,param2,...) from AppUser，将返回对象的集合，前提是对象有支持该属性列表的构造方法。
	 *        或者select new Map/List(param1,param2,...) from AppUser,将返回存有list/或map的集合对象。
	 * 		2.当没有设置字段列表selectfields时，将查询所有字段值。
	 * 		3.当设置where条件后，必须输入参数列表parameters。
	 * 		4.当设置startpage和pagesize的值大于-1时，才会根据分页查询数据，否则返回所有对象集合。
	 * 		5.当设置排序方式order时，才会按字段值排序。
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param sSelectFields String 查询字段  eq:不包含select关键字，如userId,userName，或new list(userName)或new map(userName)。当查询全部字段时可以输入空值或字符串。
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ? and trueName = ?
								模糊查询时，需要在参数值中加入"%",where语句中不可以加类似username like '%?%',正确方式为：username like ?
	 * @param sOrder String 排序 eq:不包含order by 关键字，如userId asc/desc
	 * @param nStartPage int 数据开始数 
	 * @param nPageSize int 每页几条
	 * @param parameters List 条件参数值
	 * @return List 符合查询条件的对象集合
	 * @version 1.0
	 * @throws Exception
	 */
	public List<Object> find(String sSelectFields, String sFrom, String sWhere,
			String sOrder, int nStartIndex, int nPageSize,
			List<Object> paramters) throws Exception {
		StringBuffer hql = new StringBuffer();
		if (!CMyString.isEmpty(sSelectFields)) {
			hql.append("select ").append(sSelectFields);
		}
		if (!CMyString.isEmpty(sFrom)) {
			hql.append(" from ").append(sFrom);
		} else {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (!CMyString.isEmpty(sWhere)) {
			hql.append(" where ").append(sWhere);
		}
		if (!CMyString.isEmpty(sOrder)) {
			hql.append(" order by ").append(sOrder);
		}
		if ((!CMyString.isEmpty(sWhere) && (sWhere.indexOf("?") > 0 || sWhere.indexOf(":") > 0))
				&& paramters == null) {
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		if (CMyString.isEmpty(sWhere) && paramters != null && paramters.size() > 0) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		if (nStartIndex > -1 && nPageSize > -1) {
			return baseDao.find(hql.toString(), paramters, nStartIndex, nPageSize);
		}
		return baseDao.find(hql.toString(), paramters);
	}
	
	/**
	 * 单个条件 查询
	 * Description: 此方法只支持一个条件参数的查询<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-13 下午04:20:52
	 * @param sSelectFields String 查询字段  eq:不包含select关键字，如userId,userName或new list(userName)或new map(userName)。当查询全部字段时可以输入空值或字符串。
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ?
	 * @param sOrder String 排序 eq:不包含order by 关键字，如userId asc/desc
	 * @param param	条件参数
	 * @return List 符合查询条件的对象集合
	 * @version 1.0
	 * @throws Exception 
	 */
	public List<Object> find(String sSelectFields, String sFrom, String sWhere, String sOrder, Object param) throws Exception{
		StringBuffer hql = new StringBuffer();
		if (!CMyString.isEmpty(sSelectFields)) {
			hql.append("select ").append(sSelectFields);
		}
		if (!CMyString.isEmpty(sFrom)) {
			hql.append(" from ").append(sFrom);
		} else {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (!CMyString.isEmpty(sWhere)) {
			hql.append(" where ").append(sWhere);
		}
		if (!CMyString.isEmpty(sOrder)) {
			hql.append(" order by ").append(sOrder);
		}
		if ((!CMyString.isEmpty(sWhere) && (sWhere.indexOf("?") > 0 || sWhere.indexOf(":") > 0))
				&& param == null) {
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		if (CMyString.isEmpty(sWhere) && param != null) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		
		return baseDao.find(hql.toString(), param);
	}
	
	/**
	 * 根据参数列表查询数据集合
	 * Description: 根据参数列表查询数据集合 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param sSelectFields String 查询字段  eq:不包含select关键字，如userId,userName或new list(userName)或new map(userName)。当查询全部字段时可以输入空值或字符串。
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ? and trueName = ?
	 * @param sOrder String 排序 eq:不包含order by 关键字，如userId asc/desc
	 * @param parameters 条件参数值
	 * @return List 符合查询条件的对象集合
	 * @version 1.0
	 * @throws Exception
	 */
	public List<Object> find(String sSelectFields, String sFrom, String sWhere,
			String sOrder, List<Object> parameters) throws Exception {
		StringBuffer hql = new StringBuffer();
		if (!CMyString.isEmpty(sSelectFields)) {
			hql.append("select ").append(sSelectFields);
		}
		if (!CMyString.isEmpty(sFrom)) {
			hql.append(" from ").append(sFrom);
		} else {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (!CMyString.isEmpty(sWhere)) {
			hql.append(" where ").append(sWhere);
		}
		if (!CMyString.isEmpty(sOrder)) {
			hql.append(" order by ").append(sOrder);
		}
		if ((!CMyString.isEmpty(sWhere) && (sWhere.indexOf("?") > 0 || sWhere.indexOf(":") > 0))
				&& parameters == null) {
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		if (CMyString.isEmpty(sWhere) && parameters != null && parameters.size() > 0) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		return baseDao.find(hql.toString(), parameters);
	}
	
	/**
	 * 用于查询对象记录数,支持多个条件查询
	 * @Description: 用于查询对象记录数,支持多个条件查询<BR>
	 * @author jin.yu
	 * @date 2014-3-12 下午02:00:07
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ? and trueName = ?
	 * @param params 参数值
	 * @return Integer 记录数
	 * @throws Exception
	 * @version 1.0
	 */
	public Integer count(String sFrom, String sWhere, List<Object> params)
			throws Exception {
		StringBuffer hql = new StringBuffer("select count(*)");
		if (!CMyString.isEmpty(sFrom)) {
			hql.append(" from ").append(sFrom);
		} else {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (!CMyString.isEmpty(sWhere)) {
			hql.append(" where ").append(sWhere);
		}
		if (!CMyString.isEmpty(sWhere) && (sWhere.indexOf("?") > 0 || sWhere.indexOf(":") > 0) && params == null) {
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		if (CMyString.isEmpty(sWhere) && params != null && params.size() > 0) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		return ((Long) baseDao.find(hql.toString(), params).listIterator().next())
				.intValue();
	}
	/**
	 * 用于查询对象记录数,单个条件查询
	 * @Description: 此方法只支持单个条件查询<BR>
	 * @author jin.yu
	 * @date 2014-3-12 下午02:00:07
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ?
	 * @param param 参数值
	 * @return Integer 记录数
	 * @throws Exception
	 * @version 1.0
	 */
	public Integer count(String sFrom, String sWhere, Object param)
			throws Exception {
		StringBuffer hql = new StringBuffer("select count(*)");
		if (!CMyString.isEmpty(sFrom)) {
			hql.append(" from ").append(sFrom);
		} else {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (!CMyString.isEmpty(sWhere)) {
			hql.append(" where ").append(sWhere);
		}
		if (!CMyString.isEmpty(sWhere) && (sWhere.indexOf("?") > 0) && param == null) {
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		if (CMyString.isEmpty(sWhere) && param != null) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		return ((Long) baseDao.find(hql.toString(), param).listIterator().next())
				.intValue();
	}
	/**
	 * 
	* Description: 获取分页列表 <BR>   
	* @author liu.zhuan
	* @date 2014-3-12 下午02:40:40
	* @param sSelectFields 查询字段  当查询全部字段时可以输入空值或字符串。
	* @param sFrom 查询对象
	* @param sWhere	条件
	* @param sOrder 排序
	* @param nStartPage 开始页数
	* @param nPageSize 每页几条
	* @param parameters 条件参数值
	* @version 1.0
	 * @throws Exception 
	 */
	public Page findPage(String sSelectFields, String sFrom, String sWhere, String sOrder, int nStartPage, int nPageSize, List<Object> paramters) throws Exception{
		int totalResults = count(sFrom, sWhere, paramters);
		Page page = new Page(nStartPage, nPageSize, totalResults);
		List<Object> data = find(sSelectFields, sFrom, sWhere, sOrder, page.getStartIndex(), nPageSize, paramters);
		page.setLdata(data);
		return page;
	}
	
	/**
	 * 通用分页查询 
	 * Description: 通用分页查询 <BR>
	 * 此方法用于封装支持where条件，查询部分字段，排序方式，分页操作。
	 * 注意： 	
	 * 		1.当需要查询部分字段时，需要在参数封装时实例化一个对象。 
	 * 		      如select new AppUser(param1,param2,...) from AppUser，将返回对象的集合，前提是对象有支持该属性列表的构造方法。
	 *        或者select new Map/List(param1,param2,...) from AppUser,将返回存有list/或map的集合对象。
	 * 		2.当没有设置字段列表selectfields时，将查询所有字段值。
	 * 		3.当设置where条件后，必须输入参数列表parameters。
	 * 		4.当设置startpage和pagesize的值大于-1时，才会根据分页查询数据，否则返回所有对象集合。
	 * 		5.当设置排序方式order时，才会按字段值排序。
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param sSelectFields String 查询字段  eq:不包含select关键字，如userId,userName或new list(userName)或new map(userName)。
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ? and trueName = ?
	 * @param sOrder String 排序 eq:不包含order by 关键字，如userId asc/desc
	 * @param nStartPage int 数据开始数 
	 * @param nPageSize int 每页几条
	 * @param parameters List 条件参数值
	 * @return Page 符合查询条件的分页对象
	 * @version 1.0
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page findPage(Map<String, Object> filter) throws Exception {
		String sSelectFields = "";
		String sFrom = "";
		String sWhere = "";
		String sOrder = "";
		Integer nStartPage = -1;
		Integer nPageSize = -1;
		List params = null;
		for (String key : filter.keySet()) {
			if (CMyString.isEmpty(key)) {
				continue;
			}
			Object value = filter.get(key);
			if (value == null) {
				continue;
			}
			if ("SELECTFIELDS".equals(key.toUpperCase())) {
				sSelectFields = (String) value;
			}
			if ("FROM".equals(key.toUpperCase())) {
				sFrom = (String) value;
			}
			if ("WHERE".equals(key.toUpperCase())) {
				sWhere = (String) value;
			}
			if ("ORDER".equals(key.toUpperCase())) {
				sOrder = (String) value;
			}
			if ("PARAMETERS".equals(key.toUpperCase())) {
				params = (List) value;
			}
			if ("STARTPAGE".equals(key.toUpperCase())) {
				nStartPage = (Integer) value;
			}
			if ("PAGESIZE".equals(key.toUpperCase())) {
				nPageSize = (Integer) value;
			}
		}
		int totalResults = count(sFrom, sWhere, params);
		Page page = new Page(nStartPage, nPageSize, totalResults);
		List<Object> data = find(sSelectFields, sFrom, sWhere, sOrder, page.getStartIndex(), nPageSize, params);
		page.setLdata(data);
		return page;
	}
	
	
	/**
	 * 用于查询单个对象
	 * Description: 用于查询单个对象 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param classes 查询对象Class eq:如AppUser.class
	 * @param id 对象主键ID
	 * @return Object
	 * @throws Exception
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public Object findById(Class clazz,Long id) {
		return baseDao.findById(clazz, id);
	}
	
	/**
	 * 根据对象ID集合查询数据对象集合
	 * Description:  根据对象ID集合查询数据对象集合<BR>   
	 * @author jin.yu
	 * @date 2014-3-17 下午02:52:54
	 * @param sFrom String 要查询对象名称 eq:不需要select关键字，如AppUser
	 * @param sField String 要查询的字段名称 eq:如userId
	 * @param Ids List 要查询的对象ID集合
	 * @return List 符合条件的对象集合
	 * @version 1.0
	 * @throws Exception 
	 */
	public List<Object> findByIds(final String sFrom,final String sField, final List<Object> ids) throws Exception{
		StringBuffer hql = new StringBuffer();
		if (CMyString.isEmpty(sFrom)) {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (CMyString.isEmpty(sField)) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		if(ids == null || ids.size() == 0){
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		hql.append(" from ").append(sFrom).append(" where ").append(sField).append(" in(:ids) ");
		return baseDao.findByIds(hql.toString(), ids);
	}
	/**
	 * 通过ID字符串查询多条数据信息
	 * Description:  通过ID字符串查询多条数据信息<BR>   
	 * @author jin.yu
	 * @date 2014-3-17 下午03:06:17
	 * @param sFrom String 要查询对象名称 eq:不需要select关键字，如AppUser
	 * @param sField String 要查询的字段名称 eq:如userId
	 * @param Ids String 要查询的对象ID值，以","隔开
	 * @return List 符合条件的对象集合
	 * @version 1.0
	 * @throws Exception 
	 */
	public List<Object> findByIds(String sFrom, String sField, String sIds) throws Exception{
		if(CMyString.isEmpty(sIds)){
			throw new Exception("要查询的对象ID值没有输入！");
		}
		String[] arrIds = sIds.split(",");
		//将传入的Id组装成一个数组
		List<Object> idsList = new ArrayList<Object>();
		for(int i=0;i<arrIds.length;i++){
			idsList.add(Long.parseLong(arrIds[i]));
		}
		return findByIds(sFrom, sField, idsList);
	}
	
	/**
	 * 通过ID字符串查询多条数据信息
	 * Description:  通过ID字符串查询多条数据信息<BR>   
	 * @author jin.yu
	 * @date 2014-3-17 下午03:06:17
	 * @param sSelectFields String 要查询的字段列表 eq：不需要select关键字
	 * @param sFrom String 要查询对象名称 eq:不需要from关键字，如AppUser
	 * @param sIn String 不包括where 和 in关键字，只需输入字段名称 eq:如userId
	 * @param Ids String 要查询的对象ID值，以","隔开
	 * @return List 符合条件的对象集合
	 * @version 1.0
	 * @throws Exception 
	 */
	public List<Object> findByIds(String sSelectFields, String sFrom, String sIn, String sIds) throws Exception{
		StringBuffer hql = new StringBuffer();
		if(CMyString.isEmpty(sIds)){
			throw new Exception("要查询的对象ID值没有输入！");
		}
		if (CMyString.isEmpty(sFrom)) {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (CMyString.isEmpty(sIn)) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		if(!CMyString.isEmpty(sSelectFields)){
			hql.append("select ").append(sSelectFields);
		}
		hql.append(" from ").append(sFrom).append(" where ").append(sIn).append(" in(:ids) ");
		String[] arrIds = sIds.split(",");
		//将传入的Id组装成一个数组
		List<Object> idsList = new ArrayList<Object>();//(List)Arrays.asList(arrIds);
		for(int i=0;i<arrIds.length;i++){
			idsList.add(Long.parseLong(arrIds[i]));
		}
		System.out.println(hql.toString());
		return baseDao.findByIds(hql.toString(), idsList);//findByIds(sFrom, sField, idsList);
	}
	
	/**
	 * 根据对象名，条件获取对象信息
	 * Description: 根据对象名，条件获取对象信息,支持单个条件查询<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午05:25:16
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ?
	 * @param param 条件值
	 * @return Object 符合条件的对象
	 * @version 1.0
	 * @throws Exception 
	 */
	public Object findObject(String sFrom, String sWhere, Object param) throws Exception {
		List<Object> dataList = find("", sFrom, sWhere,"", param);
		if(dataList != null && dataList.size() > 0){
			return dataList.iterator().next();
		}
		return null;
	}
	
	/**
	 * 根据对象名，条件获取对象信息
	 * Description:  根据对象名，条件获取对象信息 ,支持多条件查询<BR>   
	 * @author jin.yu
	 * @date 2014-3-17 下午07:36:16
	 * @param sFrom String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere String 条件 eq:不包含where关键字，如userName = ? and trueName = ?
	 * @param paramters List条件值
	 * @return Object 符合条件的对象
	 * @version 1.0
	 * @throws Exception 
	 */
	public Object findObject(String sFrom, String sWhere, List<Object> paramters) throws Exception {
		StringBuffer hql = new StringBuffer();
		if (!CMyString.isEmpty(sFrom)) {
			hql.append(" from ").append(sFrom);
		} else {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (!CMyString.isEmpty(sWhere)) {
			hql.append(" where ").append(sWhere);
		}
		if ((!CMyString.isEmpty(sWhere) && (sWhere.indexOf("?") > 0 || sWhere.indexOf(":") > 0))
				&& paramters == null) {
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		if (CMyString.isEmpty(sWhere) && paramters != null  && paramters.size() > 0) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		List<Object> dataList = baseDao.find(hql.toString(), paramters);
		if(dataList != null && dataList.size() > 0){
			return dataList.iterator().next();
		}
		return null;
	}
	/**
	 * 判断数据在表中是否有重复
	 * Description: 判断数据在表中是否有重复,支持多条件查询<BR>   
	 * @author jin.yu
	 * @date 2014-3-13 下午08:55:03
	 * @param sFrom 表对象	eq：AppUser
	 * @param sWhere 查询条件 eq：username = ? and userId = ?
	 * @param paramters 查询条件的值
	 * @return boolean true表示数据存在，false表示不存在
	 * @throws Exception
	 * @version 1.0
	 */
	public boolean existData(String sFrom, String sWhere, List<Object> paramters) throws Exception {
		int userCount = count(sFrom,sWhere,paramters);
		if(userCount > 0){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * Description:  判断数据在表中是否有重复，单条件查询<BR>   
	 * @author jin.yu
	 * @date 2014-3-13 下午08:55:03
	 * @param sFrom 表对象	eq：AppUser
	 * @param sWhere 查询条件 eq：username = ?
	 * @param paramter 查询条件的值
	 * @return boolean true表示数据存在，false表示不存在
	 * @throws Exception
	 * @version 1.0
	 */
	public boolean existData(String sFrom, String sWhere, Object paramter) throws Exception {
		int userCount = count(sFrom, sWhere, paramter);
		if(userCount>0){
			return true;
		}
		return false;
	}
	/**
	 * 返回应用(元数据)的单条信息 <原生SQL>  
	* Description:为其他插件类应用提供查询服务<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 下午04:33:01
	* Last Modified:
	* @param metadataid 数据编号
	* @param tableName 表名称   数据库表名称
	* @return Map<String,Object> 
	* @throws Exception
	* @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAppMedataInfo(Long metadataid,String tableName) throws Exception{
		StringBuffer sql = new StringBuffer("select * from ");
		//元数据表名称
		sql.append(tableName);
		sql.append(" where ").append(" METADATAID=").append(metadataid);
		Map<String, Object> obj = (Map<String, Object>)baseDao.executeQueryObj(sql.toString());
		return obj;	
	}
	/**
	 * 返回应用对应的元数据表名
	* Description:为其他插件类应用提供查询服务<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 下午04:44:41
	* Last Modified:
	* @param appId 应用ID
	* @param mainTable 主从表，0 是主表，1从表
	* @return String 元数据表名称
	* @throws Exception
	* @version 1.0
	 */
	public String getMetaTableName(Long appId,int mainTable) throws Exception{
		List<Object> params = new ArrayList<Object>();
		//查询应用的表
		String field = "mainTableName";
		if(mainTable==1){ //1 返回从表表名
			 field = "itemTableName";
		}
		params.add(appId);
		//视图对应的字段信息表查询对应的表名
		List<Object> appInfos = find(field, AppInfo.class.getName(), "appId=?", null, params);
		if(appInfos.size()<1){
			throw new Exception("元数据表名不存在");
		}		
		String tableName = (String)appInfos.get(0); 
		if(CMyString.isEmpty(tableName)|| tableName.equals("null")){
			throw new Exception("元数据表名称不能为空或者空字符串!");
		}
		return tableName;
	}
	/**
	 * 查询应用的字段,新建一条应用信息时也调用此方法，第二个(inOutline)参数为-1 即可获取所有的字段集合
	* Description:  为其他插件类应用提供查询服务 app_field_rel 该对象中查询<BR>  
	* @author liujian
	* @date Create: 2014-4-25 下午11:18:56
	* Last Modified:
	* @param viewId 应用的视图ID
	* @param inOutline 0 是否概览显示，-1查询所有字段  2 查询检索字段 3查询标题字段4前台细览字段
	* @param tableName 表名
	* @return List<Object> 返回应用字段对象<AppFieldRel>集合,每个字段一条记录
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> queryAppFields(Long viewId,int inOutline,String tableName) throws Exception{
		if(viewId<1){
			throw new Exception("视图ID不能小于等于0");
		}		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sSql = new StringBuffer("viewId=?").append(" and mainTableName=?");
		params.add(viewId);
		params.add(tableName);
		if(inOutline==1){
			params.add(inOutline);//是否可概览显示,需要根据系统数据字典参数中定义"inOutline"
			sSql.append(" and inOutline =?");
		}if(inOutline==2){
			sSql.append(" and searchField=1");
		}if(inOutline==3){
			params.add(1); //查询标题字段
			sSql.append(" and titleField=?");
		}
		if(inOutline==4){
			params.add(1); //查询细览字段字段
			sSql.append(" and isWebShow=?");
		}
		List<Object> appFieldRels = find(null, AppFieldRel.class.getName(), sSql.toString(), " fieldOrder asc ", params);
		return appFieldRels;
	}
}
