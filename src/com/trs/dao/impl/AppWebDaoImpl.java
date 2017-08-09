package com.trs.dao.impl;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.trs.dao.AppWebDao;
import com.trs.util.CMyDateTime;
@Repository
public class AppWebDaoImpl extends BaseDaoImpl implements AppWebDao{
	/**
	 * Description: 执行原生sql语句，可返回对象集合<BR>
	 * @author liu.zhuan
	 * @date 2014-3-7 下午05:25:16
	 * @param sql 原生sql语句，完整sql，带占位符,sDateFormat日期转换格式
	 * @param parameters List 参数值列表，需和占位符顺序一致。
	 * @return Object
	 * @version 1.0
	 * @throws SQLException, IOException
	 */
	@SuppressWarnings("deprecation")
	public List<Object> executeQueryObjsDateFormat(String sql, List<Object> parameters, String sDateFormat) throws SQLException, IOException {
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
						value = ctime.toString(sDateFormat);
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
}
