package com.trs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.trs.dao.AppMetaDataDao;

/**
 * Description: 数据库表信息，表结构，字段信息操作实现<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppTableInfoDaoImpl
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-7 下午04:00:44
 * @version 1.0
 */
@Repository
public class AppMetaDataDaoImpl extends BaseDaoImpl implements AppMetaDataDao {
	
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
	public void executeBaseSql(String sql1 ,String sql2) throws SQLException{
		Session session = gethbSession();
		PreparedStatement pst = null;
		ResultSet rst = null;
		Connection conn = null;
		try {
			conn = session.connection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql1);
			pst.execute();
			pst = conn.prepareStatement(sql2);
			pst.execute();
			conn.commit();
			conn.setAutoCommit(true);
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

}
