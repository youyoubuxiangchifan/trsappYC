package com.trs.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
public interface BaseDao {

	/**
	 * 
	 * Description: 保存对象 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-7 下午07:15:02
	 * @param model
	 *            实体对象
	 * @version 1.0
	 */
	public void save(final Object model);
	/**
	 * 保存一个对象到数据库
	 * Description: 传人参数为一个实体对象<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午07:15:02
	 * @param model 实体对象 
	 * @version 1.0
	 */
	public void saveObj(Object model) throws Exception;
	/**
	 * 
	 * Description: 通用更新对象<BR>
	 * 
	 * @author jin.yu
	 * @date 2014-3-11 下午06:05:07
	 * @param model
	 * @version 1.0
	 */
	public void update(final Object model);

	/**
	 * 
	 * Description: 通用批量更新对象<BR>
	 * 
	 * @author jin.yu
	 * @date 2014-3-11 下午06:05:21
	 * @param parameters
	 * @version 1.0
	 */
	public void saveOrUpdateAll(List<Object> parameters);

	/**
	 * 
	 * Description: 通用删除<BR>
	 * 
	 * @author jin.yu
	 * @date 2014-3-11 下午06:05:56
	 * @param model
	 * @version 1.0
	 */
	public void delete(final Object model);
	
	/**
	 * 
	* Description:  批量删除对象<BR>   
	* @author jin.yu
	* @date 2014-3-17 上午11:24:04
	* @version 1.0
	 */
	public void deleteAll(List<Object> parameters);
	
	/**
	 * 
	 * Description: 通用查询 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param hql
	 * @return
	 * @version 1.0
	 */
	public List<Object> find(final String hql);

	/**
	 * 
	 * Description: 通用分页查询 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param hql
	 * @return
	 * @version 1.0
	 */
	public List<Object> find(final String hql, int startIndex, int pageSize);

	/**
	 * 
	* Description: 单个条件 查询<BR>   
	* @author liu.zhuan
	* @date 2014-3-13 下午04:20:52
	* @param hql hql语句
	* @param param	条件参数
	* @return List<Object>
	* @version 1.0
	 */
	public List<Object> find(String hql, Object param);
	
	/**
	 * 
	 * Description: 通用分页,按条件查询 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param hql
	 * @return
	 * @version 1.0
	 */
	public List<Object> find(final String hql, final List<Object> parameters,
			final int startIndex, final int pageSize);

	/**
	 * 
	 * Description: 用于根据条件查询对象集合 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param queryString
	 *            查询hql语句
	 * @param parameters
	 *            where子句参数列表
	 * @return List<Object>
	 * @throws Exception
	 * @version 1.0
	 */
	public List<Object> find(final String hql, final List<Object> parameters);

	/**
	 * 
	 * Description: 用于根据条件查询对象集合 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param hql
	 *            查询hql语句
	 * @param parameters
	 *            where子句参数列表
	 * @return Object[]
	 * @throws Exception
	 * @version 1.0
	 */
	public List<Object> find(final String hql, final Object[] parameters);

	/**
	 * 
	 * Description: 用于查询单个对象 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param classes
	 *            查询对象Class
	 * @param id
	 *            对象主键ID
	 * @return Object
	 * @throws Exception
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public Object findById(Class classes, Long id);
	
	/**
	 * 
	* Description:  根据对象ID集合查询数据对象集合<BR>   
	* @author jin.yu
	* @date 2014-3-17 下午02:52:54
	* @param hql 查询语句
	* @return List<Object>
	* @version 1.0
	 */
	public List<Object> findByIds(final String hql, final List<Object> ids);
	
	/**
	 * 
	 * Description: 用于查询对象记录数 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param classes 查询对象Class
	 * @return Object
	 * @throws Exception
	 * @version 1.0
	 */
	public Integer count(String hql);
	
	/**
	 * Description: 执行原生sql语句 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午04:05:22
	 * @param sql sql语句
	 * @version 1.0
	 * @throws SQLException
	 */
	public void executeBaseSql(String sql) throws SQLException;
	/**
	 * 原生SQL 批量保存数据
	* Description:相同的SQL语句，参数值不同<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 上午11:13:11
	* Last Modified:
	* @param sql 原生SQL语句,完整的SQL,带占位符的SQL
	* @param parameters 要保存的参数,注意参数位置要和sql中的顺序对应
	* @throws SQLException
	* @version 1.0
	 */
	public void saveBaseSqls(String sql ,List<List<Object>> parameters) throws SQLException;
	/**
	 * 原生SQL批量保存数据，不同的Sql语句，参数值以拼接的形式，此方式不安全，不建议前台使用
	* Description:仅用于数据初始化，不建议直接调用<BR>  
	* @author wen.junhui
	* @date Create: 2014-10-10 上午10:01:45
	* Last Modified:
	* @param sqlList sql 语句列表 
	* @throws SQLException
	* @version 1.0
	 */
	public void saveBaseSqls(List<String> sqlList) throws SQLException;
	/**
	 * 原生SQL 保存单条数据
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 上午11:13:11
	* Last Modified:
	* @param sql 原生SQL语句,完整的SQL,带占位符的SQL
	* @param parameters 要保存的参数,注意参数位置要和sql中的顺序对应
	* @throws SQLException
	* @version 1.0
	 */
	public Long saveBaseSql(String sql ,List<Object> parameters) throws SQLException;
	/**
	 * Description: 执行原生sql语句，可返回对象集合 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午04:53:18
	 * @param sql sql语句
	 * @return List<Object>
	 * @version 1.0
	 * @throws SQLException
	 *             ,IOException
	 */
	public List<Object> executeQueryObjs(String sql) throws SQLException,
			IOException;
	/**
	 * Description: 执行原生sql语句，可返回对象集合<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午05:25:16
	 * @param sql 原生sql语句，完整sql，带占位符
	 * @param parameters List 参数值列表，需和占位符顺序一致。
	 * @return Object
	 * @version 1.0
	 * @throws SQLException, IOException
	 */
	public List<Object> executeQueryObjs(String sql, List<Object> parameters) throws SQLException,IOException ;
	/**
	* 执行原生sql语句，可返回对象 <BR>
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-28 下午01:26:08
	* Last Modified:
	* @param sql sql语句
	* @param parameters  参数值
	* @return Object 原数据集合 Map
	* @throws Exception
	* @version 1.0
	 */
	public Object executeQueryObj(String sql, List<Object> parameters) throws Exception ;
	/**
	 * Description: 执行原生sql语句，可返回对象<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午05:25:16
	 * @param sql sql语句
	 * @return Object
	 * @version 1.0
	 * @throws SQLException,IOException
	 */
	public Object executeQueryObj(String sql) throws SQLException, IOException,Exception;
	
	/**
	 * 查询整型结果集通用方法
	* Description: 查询整型结果 <BR>   
	* @author liu.zhuan
	* @date 2014-3-24 下午07:50:53
	* @param sql String 原生sql语句
	* @return int 整型值
	* @throws SQLException
	* @version 1.0
	 */
	public int executeQueryInt(String sql) throws SQLException;
	/**
	 * 查询整型结果集通用方法,带占位符参数
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-26 下午04:04:15
	* Last Modified:
	* @param sql  原生sql语句，完整sql，带占位符
	* @param parameters List 参数值列表，需和占位符顺序一致。
	* @return int
	* @throws SQLException
	* @version 1.0
	 */
	public int executeQueryInt(String sql,List<Object> parameters) throws SQLException;
}
