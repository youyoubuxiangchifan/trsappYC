package com.trs.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.trs.dao.BaseDao;
import com.trs.util.CMyDateTime;
import com.trs.util.DateUtil;
/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: BaseManager
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-12 下午04:39:48
 * @version 1.0
 */
@Repository
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

//	private PreparedStatement pst = null;
//	private ResultSet rst = null;
//	private Connection conn = null;

	/**
	 * 为setSessionFactory加一层@Autowired的外套以适应Annotation环境
	 * 单数据源使用@Autowired;多数据源使用@Resource(name="xxxxSessionFactory")
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactoryAnnotation(SessionFactory sessionFactory)
			throws HibernateException {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 保存一个对象到数据库
	 * Description: 传人参数为一个实体对象<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午07:15:02
	 * @param model 实体对象 
	 * @version 1.0
	 */
	public void save(Object model) {
		getHibernateTemplate().save(model);
	}
	/**
	 * 保存一个对象到数据库
	 * Description: 传人参数为一个实体对象<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午07:15:02
	 * @param model 实体对象 
	 * @version 1.0
	 */
	public void saveObj(Object model) throws Exception{
		getHibernateTemplate().save(model);
	}

	/**
	 * 根据hql语句获取一个查询对象
	 * Description: 传入参数：hql语句 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午07:15:38
	 * @param hql hql语句
	 * @return Query 查询对象
	 * @throws HibernateException
	 * @version 1.0
	 */
	public Query getQuery(String hql) throws HibernateException {
		return getSession().createQuery(hql);
	}

	/**
	 * 获取hibernate session
	 * Description: 获取hibernate session <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午07:16:23
	 * @return Session
	 * @throws HibernateException
	 * @version 1.0
	 */
	public Session gethbSession() throws HibernateException {
		return getSession();
	}

	/**
	 * 关闭session
	 * Description: 传入参数：已获取的hibernate session对象。 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午07:24:14
	 * @param session
	 * @throws HibernateException
	 * @version 1.0
	 */
	public void closehbSession(Session session) throws HibernateException {
		//session.close();
		releaseSession(session);
	}

	/**
	 * 删除一个对象
	 * Description: 传入参数：实体对象 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午10:30:30
	 * @param model 实体对象
	 * @version 1.0
	 */
	public void delete(Object model) {
		getHibernateTemplate().delete(model);
	}
	
	/**
	 * 批量删除对象
	 * Description: 传入参数：要删除的实体对象集合。 <BR>
	 * @author jin.yu
	 * @date 2014-3-17 下午10:30:30
	 * @param parameters 实体对象集合
	 * @version 1.0
	 */
	public void deleteAll(List<Object> parameters){
		getHibernateTemplate().deleteAll(parameters);
	}
	
	/**
	 * 更新一个实体对象
	 * Description: 传人参数：需要修改的持久状态的实体对象<BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午10:29:58
	 * @param model 实体对象
	 * @version 1.0
	 */
	public void update(Object model) {
		getHibernateTemplate().update(model);
	}

	/**
	 * 批量保存/更新对象
	 * Description:传入参数：需要保存或修改的对象集合<BR>
	 * @author jin.yu
	 * @date 2014-3-11 下午06:07:16
	 * @param parameters 对象集合
	 * @version 1.0
	 */
	public void saveOrUpdateAll(List<Object> parameters) {
		getHibernateTemplate().saveOrUpdateAll(parameters);
	}

	/**
	 * 通用查询
	 * Description: 传入参数：hql语句 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param hql hql语句
	 * @return List 符合查询条件的对象集合
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(String hql) {
		return (List<Object>) getHibernateTemplate().find(hql);
	}

	/**
	 * 通用分页查询
	 * Description: 传入参数：hql语句；数据开始索引；查询数据页码大小 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param hql String hql语句
	 * @param startIndex int 数据开始索引
	 * @param pageSize int 查询数据页码大小
	 * @return List 符合查询条件的对象集合
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(final String hql, final int startIndex, final int pageSize) {
		List<Object> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Query query = session.createQuery(hql);
						query.setFirstResult(startIndex);
						query.setMaxResults(pageSize);
						return query.list();
					}
				});
		return result;
	}
	
	/**
	 * 单个条件查询
	 * Description: 单个条件查询<BR>   
	 * @author liu.zhuan
	 * @date 2014-3-13 下午04:20:52
	 * @param hql hql语句
	 * @param param	条件参数值
	 * @return List 符合查询条件的对象结合
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(String hql, Object param){
		return getHibernateTemplate().find(hql, param);
	}
	
	/**
	 * 通用分页,按条件查询
	 * Description: 通用分页,按条件查询 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午09:18:38
	 * @param hql hql语句
	 * @param parameters List 条件参数值集合
	 * @param startIndex int 数据开始索引
	 * @param pageSize int 数据页码大小
	 * @return List 符合查询条件的对象结合
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(final String hql, final List<Object> parameters,
			final int startIndex, final int pageSize) {
		List<Object> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Query query = session.createQuery(hql);
						if (parameters != null && parameters.size() > 0) {
							for (int i = 0; i < parameters.size(); i++) {
								query.setString(i, parameters.get(i) == null ? "" : parameters.get(i).toString());
							}
						}
						query.setFirstResult(startIndex);
						query.setMaxResults(pageSize);
						return query.list();
					}
				});
		return result;
	}

	/**
	 * 用于根据条件查询对象集合
	 * Description: 用于根据条件查询对象集合 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param hql 查询hql语句
	 * @param parameters 条件参数值集合
	 * @return List 用于根据条件查询对象集合
	 * @throws Exception
	 * @version 1.0
	 */
//	@SuppressWarnings("unchecked")
//	public List<Object> find(final String hql, final List<Object> parameters) {
//		Object result = getHibernateTemplate().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session)
//					throws HibernateException, SQLException {
//				// TODO Auto-generated method stub
//				Query query = session.createQuery(hql);
//				if (parameters != null && parameters.size() > 0) {
//					for (int i = 0; i < parameters.size(); i++) {
//						query.setString(i, parameters.get(i) == null ? "" : parameters.get(i).toString());
//					}
//				}
//				return query.list();
//			}
//		});
//		return (List<Object>) result;
//	}
	@SuppressWarnings("unchecked")
	public List<Object> find(final String hql, final List<Object> parameters) {
		Session session = gethbSession();
		Object result =null;
			try {
					Query query = session.createQuery(hql);
					if (parameters != null && parameters.size() > 0) {
						for (int i = 0; i < parameters.size(); i++) {
							query.setString(i, parameters.get(i) == null ? "" : parameters.get(i).toString());
						}
					}
					 result = query.list();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closehbSession(session);
			}
			return (List<Object>) result;
	}

	/**
	 * 用于根据条件查询对象集合
	 * Description: 用于根据条件查询对象集合 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param hql 查询hql语句
	 * @param parameters 条件参数值数组
	 * @return List 用于根据条件查询对象集合
	 * @throws Exception
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(final String hql, final Object[] parameters) {
		return getHibernateTemplate().find(hql, parameters);
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
	public Object findById(Class clazz, Long id) {
		return getHibernateTemplate().get(clazz, id);
	}
	
	/**
	 * 根据对象ID集合查询数据对象集合
	 * Description: 根据对象ID集合查询数据对象集合<BR>   
	 * @author jin.yu
	 * @date 2014-3-17 下午02:52:54
	 * @param hql 查询语句     带占位符?
	 * @param ids 条件参数值集合
	 * @return List 符合查询条件的对象集合
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findByIds(final String hql, final List<Object> ids){
		return getHibernateTemplate().execute(new HibernateCallback<List>() {
			public List doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery(hql).setParameterList("ids", ids).list();
			}
		});
	}

	/**
	 * 用于查询对象记录数
	 * Description: 用于查询对象记录数 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-10 下午05:19:17
	 * @param hql语句
	 * @return Integer 记录数
	 * @throws Exception
	 * @version 1.0
	 */
	public Integer count(String hql) {
		Long count = (Long) getHibernateTemplate().find(hql).listIterator()
				.next();
		return count.intValue();
	}


	/**
	 * Description: 执行原生sql语句 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午04:05:22
	 * @param sql ql语句
	 * @version 1.0
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	public void executeBaseSql(String sql) throws SQLException {
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.execute();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closehbSession(session);
		}
	}
	/**
	 * 原生SQL 批量保存数据
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 上午11:13:11
	* Last Modified:
	* @param sql 原生SQL语句,完整的SQL,带占位符的SQL
	* @param parameters 要保存的参数,注意参数位置要和sql中的顺序对应
	* @throws SQLException
	* @version 1.0
	 */
	@SuppressWarnings("deprecation")
	public void saveBaseSqls(String sql ,List<List<Object>> parameters) throws SQLException{
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			if (parameters != null && parameters.size() > 0) {
				for (int i = 0; i < parameters.size(); i++) {
					List<Object> obj= parameters.get(i);
							for(int j =0;j<obj.size();j++){
								pst.setObject(j+1, obj.get(j));
							}
							pst.addBatch();
				}
				pst.executeBatch();
				conn.commit();
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				throw e;
			}
			closehbSession(session);
		}
	}
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
	public void saveBaseSqls(List<String> sqlList) throws SQLException{
		Session session = gethbSession();
		Statement stmt = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (String sql : sqlList) {
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				closeDB(conn, stmt, rst);
			} catch (SQLException e) {
				throw e;
			}
			closehbSession(session);
		}
	}
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
	@SuppressWarnings("deprecation")
	public Long saveBaseSql(String sql ,List<Object> parameters) throws SQLException{
		Long id=0l;//保存生成的ID  
		String[] columnNames= {"METADATAID"} ;
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql,columnNames);
			if (parameters != null && parameters.size() > 0) {
				for (int i = 0; i < parameters.size(); i++) {
					pst.setObject(i+1, parameters.get(i));
				}
				pst.execute();
				conn.commit();
				if(sql.indexOf("insert")!=-1){
					rst = pst.getGeneratedKeys(); 
					if (rst != null&&rst.next()) {  
						//id=(long)rst.getInt(1);
					    id=rst.getLong(1);
					}
				}
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				throw e;
			}
			closehbSession(session);
		}
		return id;
	}
	/**
	 * 原生SQL 保存单条数据
	* Description:<BR>  
	* @author copy by liu.zhuan
	* @date Create: 2014-3-25 上午11:13:11
	* Last Modified:
	* @param sql 原生SQL语句,完整的SQL,带占位符的SQL
	* @param parameters 要保存的参数,注意参数位置要和sql中的顺序对应
	* @param returnField 执行sql后需要返回值的字段名
	* @throws SQLException
	* @return Object
	* @version 1.0
	 */
	@SuppressWarnings("deprecation")
	public Object saveBaseSql(String sql ,List<Object> parameters, String returnField) throws SQLException{
		//Long id=0l;//保存生成的ID  
		Object obj = null;
		String[] columnNames= {returnField} ;
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql,columnNames);
			if (parameters != null && parameters.size() > 0) {
				for (int i = 0; i < parameters.size(); i++) {
					pst.setObject(i+1, parameters.get(i));
				}
				pst.execute();
				conn.commit();
				if(sql.indexOf("insert")!=-1){
					rst = pst.getGeneratedKeys(); 
					if (rst != null&&rst.next()) {  
						//id=(long)rst.getInt(1);
						obj=rst.getObject(1);
					}
				}
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				throw e;
			}
			closehbSession(session);
		}
		return obj;
	}
	/**
	 * Description: 执行原生sql语句，可返回对象 <BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午04:53:18
	 * @param sql sql语句
	 * @return List<Object>
	 * @version 1.0
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public Object executeQueryObj(String sql) throws Exception {
		Map<String, Object> obj = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		Session session = gethbSession();
		try {
			conn = session.connection();
			//		conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();;
			pst = conn.prepareStatement(sql);
			pst.setMaxRows(1);
			rst = pst.executeQuery();
			if (rst.next()) {
				obj = new HashMap<String, Object>();
				ResultSetMetaData metaData = rst.getMetaData();
				int cols = metaData.getColumnCount();
				for (int i = 1; i <= cols; i++) {
					String sFieldName = metaData.getColumnLabel(i)
							;
					Object value = null;
					if (Types.CLOB == metaData.getColumnType(i)) {
						value = ClobToString(rst.getClob(i));
					}
					//else if(Types.DATE == metaData.getColumnType(i)){
						//CMyDateTime ctime = new CMyDateTime();
						//ctime.setDate(rst.getDate(i));
						//value = ctime.toString("yyyy-MM-dd HH:mm:ss");
					//} 
					else {
						value = rst.getString(i);
					}
					obj.put(sFieldName.toUpperCase(), value);
				}
			}
			return obj;
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closehbSession(session);
		}
	}
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
	@SuppressWarnings("deprecation")
	public Object executeQueryObj(String sql, List<Object> parameters) throws Exception {
		Map<String, Object> obj = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		Session session = gethbSession();
		try {
			conn = session.connection();
			//		conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();;
			pst = conn.prepareStatement(sql);
			if(parameters != null && parameters.size() > 0){
				for (int i = 0; i < parameters.size(); i++) {
					pst.setObject(i+1, parameters.get(i));
				}
			}
			pst.setMaxRows(1);
			rst = pst.executeQuery();
			if (rst.next()) {
				obj = new HashMap<String, Object>();
				ResultSetMetaData metaData = rst.getMetaData();
				int cols = metaData.getColumnCount();
				for (int i = 1; i <= cols; i++) {
					String sFieldName = metaData.getColumnLabel(i)
							;
					Object value = null;
					if (Types.CLOB == metaData.getColumnType(i)) {
						value = ClobToString(rst.getClob(i));
					}
					//else if(Types.DATE == metaData.getColumnType(i)){
						//CMyDateTime ctime = new CMyDateTime();
						//ctime.setDate(rst.getDate(i));
						//value = ctime.toString("yyyy-MM-dd HH:mm:ss");
					//} 
					else {
						value = rst.getString(i);
					}
					obj.put(sFieldName.toUpperCase(), value);
				}
			}
			return obj;
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closehbSession(session);
		}
	}
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
	@SuppressWarnings("deprecation")
	public List<Object> executeQueryObjs(String sql, List<Object> parameters) throws SQLException,
			IOException {
		List<Object> dataList = new ArrayList<Object>();
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			//TODO 
		//	conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();;
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			if(parameters != null && parameters.size() > 0){
				for (int i = 0; i < parameters.size(); i++) {
					pst.setObject(i+1, parameters.get(i));
				}
			}
			rst = pst.executeQuery();
			Map<String, Object> obj = null;
			ResultSetMetaData metaData = rst.getMetaData();
			while (rst.next()) {
				obj = new HashMap<String, Object>();
				int cols = metaData.getColumnCount();
				for (int i = 1; i <= cols; i++) {
					String sFieldName = metaData.getColumnLabel(i);
					Object value = null;
					if (Types.CLOB == metaData.getColumnType(i)) {
						value = ClobToString(rst.getClob(i));
					} else if(Types.DATE == metaData.getColumnType(i) || Types.TIMESTAMP == metaData.getColumnType(i)){
						CMyDateTime ctime = new CMyDateTime();
						ctime.setDateTimeWithTimestamp(rst.getTimestamp(i));
						value = ctime.toString("yyyy-MM-dd HH:mm:ss");
					}else {
						value = rst.getString(i);
					}
					obj.put(sFieldName.toUpperCase(), value);
				}
				dataList.add(obj);
			}
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closehbSession(session);
		}
		return dataList;
	}
	
	/**
	 * Description: 执行原生sql语句，可返回对象集合<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午05:25:16
	 * @param sql sql语句
	 * @return Object
	 * @version 1.0
	 * @throws SQLException, IOException
	 */
	@SuppressWarnings("deprecation")
	public List<Object> executeQueryObjs(String sql) throws SQLException,
			IOException {
		List<Object> dataList = new ArrayList<Object>();
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			rst = pst.executeQuery();
			Map<String, Object> obj = null;
			ResultSetMetaData metaData = rst.getMetaData();
			while (rst.next()) {
				obj = new HashMap<String, Object>();
				int cols = metaData.getColumnCount();
				for (int i = 1; i <= cols; i++) {
					String sFieldName = metaData.getColumnLabel(i);
					Object value = null;
					if (Types.CLOB == metaData.getColumnType(i)) {
						value = ClobToString(rst.getClob(i));
					} else {
						value = rst.getString(i);
					}
					obj.put(sFieldName.toUpperCase(), value);
				}
				dataList.add(obj);
			}
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closehbSession(session);
		}
		return dataList;
	}
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
	@SuppressWarnings("deprecation")
	public int executeQueryInt(String sql,List<Object> parameters) throws SQLException{
		int result = 0;
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			if(parameters != null && parameters.size() > 0){
				for (int i = 0; i < parameters.size(); i++) {
					pst.setObject(i+1, parameters.get(i));
				}
			}
			pst.setMaxRows(1);
			rst = pst.executeQuery();
			if (rst.next()) {
				result = rst.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				throw e;
			}
			closehbSession(session);
		}
		return result;
	}
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
	@SuppressWarnings("deprecation")
	public int executeQueryInt(String sql) throws SQLException{
		int result = 0;
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setMaxRows(1);
			rst = pst.executeQuery();
			if (rst.next()) {
				result = rst.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				closeDB(conn, pst, rst);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closehbSession(session);
		}
		return result;
	}
	/*
	 * 把Clob对象转换成String
	 */
	protected String ClobToString(Clob clob) throws SQLException, IOException {
		if (clob == null)
			return null;
		String reString = "";
		Reader rd;
		try {
			rd = clob.getCharacterStream(); // 得到数据流
			BufferedReader br = new BufferedReader(rd);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {
				sb.append(s);
				s = br.readLine();
			}
			reString = sb.toString();
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (IOException e) {
			throw new IOException(e);
		}
		return reString;
	}

	/*
	 * 关闭数据库
	 */
	public void closeDB(Connection conn, PreparedStatement pst, ResultSet rst)
			throws SQLException {
		if (conn != null) {
			conn.close();
			conn = null;
		}

		if (rst != null) {
			rst.close();
			rst = null;
		}

		if (pst != null) {
			pst.close();
			pst = null;
		}
	}
	/*
	 * 关闭数据库
	 */
	public void closeDB(Connection conn, Statement pst, ResultSet rst)
			throws SQLException {
		if (conn != null) {
			conn.close();
			conn = null;
		}

		if (rst != null) {
			rst.close();
			rst = null;
		}

		if (pst != null) {
			pst.close();
			pst = null;
		}
	}
}