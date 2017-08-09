/**
 * Created:         2010-6-12 15:24:09
 * Last Modified:   2010-6-12/2012-7-9
 * Description:
 *      class DBTools
 */
package com.trs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;


public class DBTools {
	private static Logger logger = Logger.getLogger(DBTools.class);
    private Connection connection = null;
    private PreparedStatement preStament = null;
    private Statement stament = null;
    private ResultSet resultSet = null; 
    
    private String db_type = ""; //数据库类型
    private String db_host = ""; //数据库地址    
    private String db_name = ""; //数据库名称(oracle:SID)
    private String db_account = ""; //数据库连接账号
    private String db_passwd = ""; //数据库连接密码
    
    /**	数据库类型-oracle */
    public static final String DB_TYPE_ORACLE = "oracle";
    /**	数据库类型-sqlServer */
    public static final String DB_TYPE_SQLSERVER = "sqlserver";
    /**	数据库类型-mysql */
    public static final String DB_TYPE_MYSQL = "mysql";
    /**	数据库类型-db2 */
    public static final String DB_TYPE_DB2 = "db2";
    /**	数据库类型-sybase */
    public static final String DB_TYPE_SYBASE = "sybase";
    /**	数据库类型-postgresql */
    public static final String DB_TYPE_POSTGRESQL = "postgresql";
    /**	数据库类型-access */
    public static final String DB_TYPE_ACCESS = "access";
    
    public DBTools(String _sdbType, String _sdbHost, String _sdbName, 
    		String _sdbAccount, String sdbPasswd) {
    	this.db_type = _sdbType;
    	this.db_host = _sdbHost;
    	this.db_name = _sdbName;
    	this.db_account = _sdbAccount;
    	this.db_passwd = sdbPasswd;
    }
    
    /**
     * 获取数据对象集合
     * @param sql —— 访问数据库的SQL语句
     * @param fieldCounts —— 字段数量, 该值将小于等于SQL中的select数量
     * @return List —— 返回的数据对象
     * */
    public List<String[]> getDataset(String sql, int fieldCounts){
        List<String[]> list = new ArrayList<String[]>();
        try {
        	connection = this.getConnection();
            preStament = connection.prepareStatement(sql);
            resultSet = preStament.executeQuery();
            while(resultSet.next()){
                String[] dataArry = new String[fieldCounts];
                for(int i = 0; i < fieldCounts; i++){
                    dataArry[i] = resultSet.getString(i+1);
                }
                list.add(dataArry);
            }
        } catch (SQLException e) {
            //System.err.println("---访问数据库失败！---");
        	logger.error("访问数据库失败！", e);
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return list;
    }
    
    /**
     * 获取数据对象集合 含有CLOB字段
     * @param sql —— 访问数据库的SQL语句
     * @param fieldCounts —— 字段数量, 该值将小于等于SQL中的select数量
     * @return List —— 返回的数据对象
     * */
    public List<String[]> getClobDataset(String sql, int fieldCounts){
        List<String[]> list = new ArrayList<String[]>();
        //System.out.println("queryDB:"+sql); 
        logger.info("queryDB:"+sql);
        try {
            connection = this.getConnection();
            preStament = connection.prepareStatement(sql);
            resultSet = preStament.executeQuery(); 
            ResultSetMetaData metadata = resultSet.getMetaData();
            while(resultSet.next()){
                String[] dataArry = new String[fieldCounts];
                for(int i = 0; i < fieldCounts; i++){
                    if(Types.CLOB == metadata.getColumnType(i+1)){
                        dataArry[i] = ClobToString(resultSet.getClob(i+1));
                    }else{
                        dataArry[i] = resultSet.getString(i+1);
                    }
                }
                list.add(dataArry);
            }
        } catch (SQLException e) {
            //System.err.println("---访问数据库失败！---");
        	logger.error("访问数据库失败！", e);
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return list;
    }
    
    /**
     * 插入(或更新)数据表数据
     * @param sql —— 插入数据表的SQL语句
     * @return int —— 插入成功的记录数 -1：数据库连接失败；-2：访问数据库失败
     * */
    public int insert(String sql){
        int upds = 0;
        //System.out.println("insertDB:" + sql);
        logger.info("queryDB:"+sql);
        try {
            connection = this.getConnection();
            preStament = connection.prepareStatement(sql);
            stament = connection.createStatement();
            upds = stament.executeUpdate(sql);
            
        } catch (SQLException e) {
            //System.err.println("---访问数据库失败！---");
        	logger.error("访问数据库失败！", e);
            e.printStackTrace();
            upds = -2;
        } finally {
            closeDB();
        }
        return upds;
    }
    
    /*
     * 关闭数据库
     */
    private void closeDB(){
        try {
            if(resultSet != null) {
                resultSet.close();   
                resultSet = null;
            }
        } catch (SQLException e) {
            //System.err.println("---关闭数据库ResultSet失败！---");
        	logger.error("关闭数据库对象["+resultSet.getClass()+"]失败", e);
            e.printStackTrace();
        }
        
        try {
            if(preStament != null) {
                preStament.close();
                preStament = null;
            }
        } catch (SQLException e) {
            //System.err.println("---关闭数据库PreparedStatement失败！---");
        	logger.error("关闭数据库对象["+preStament.getClass()+"]失败", e);
            e.printStackTrace();
        }
        
        try {
            if(connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            //System.err.println("---关闭数据库Connection失败！---");
        	logger.error("关闭数据库对象["+connection.getClass()+"]失败", e);
            e.printStackTrace();
        }
    }
    //jdbc连接方法
    private Connection getConnection() {
        Connection conn = null;
        if(this.db_type == null || this.db_type.length() <= 0)
            return conn;
        
        String url = "";
        try {
            if("oracle".equals(this.db_type)){ 
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance(); 
                url = "jdbc:oracle:thin:@"+this.db_host+":1521:"+ db_name; //db_name为数据库的SID 
                conn = DriverManager.getConnection(url,this.db_account,this.db_passwd); 
            }else if("sqlserver".equals(this.db_type)){   
                //Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance(); //sql server2000到Sql Server7.0
                Class.forName("net.sourceforge.jtds.jdbc.Driver"); //sql server2005
                url = "jdbc:jtds:sqlserver://"+this.db_host+":1433;DatabaseName="+db_name;
                conn = DriverManager.getConnection(url,this.db_account,this.db_passwd);
            }else if("mysql".equals(this.db_type)){
                Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
                url = "jdbc:mysql://"+this.db_host+"/"+db_name+"?user="+this.db_account+"&password="+this.db_passwd+"&useUnicode=true&characterEncoding=utf-8";
                conn = DriverManager.getConnection(url); 
            }else if("db2".equals(this.db_type)){
                Class.forName("com.ibm.db2.jdbc.app.DB2Driver ").newInstance(); 
                url = "jdbc:db2://"+this.db_host+":5000/"+db_name; 
                conn = DriverManager.getConnection(url,this.db_account,this.db_passwd); 
            }else if("sybase".equals(this.db_type)){
                Class.forName("com.sybase.jdbc.SybDriver").newInstance(); 
                url = "jdbc:sybase:Tds:"+this.db_host+":5007/"+db_name;
                Properties sysProps = System.getProperties(); 
                sysProps.put("user", this.db_account); 
                sysProps.put("password", this.db_passwd); 
                conn = DriverManager.getConnection(url, sysProps); 
            }else if("postgresql".equals(this.db_type)){
                Class.forName("org.postgresql.Driver").newInstance(); 
                url = "jdbc:postgresql://"+this.db_host+"/"+db_name;  
                conn = DriverManager.getConnection(url,this.db_account,this.db_passwd); 
            }else if("access".equals(this.db_type)){
                //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
                //url = "jdbc:odbc:mdxx={Microsoft Access Driver(*.mdb)};DBQ=F:/xxk.mdb";
                Class.forName("org.objectweb.rmijdbc.Driver").newInstance();
                url = "jdbc:rmi://"+this.db_host+"/jdbc:odbc:mdxx";
                conn = DriverManager.getConnection(url);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    /*
     *把Clob对象转换成String 
     */
    private String ClobToString(Clob clob){
        if(clob == null) 
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reString;
    }

    /**
     * 获取数据对象集合
     * @param sql —— 访问数据库的SQL语句
     * @param fieldCounts —— 字段数量, 该值将小于等于SQL中的select数量
     * @return List —— 返回的数据对象
     * */
    public ResultSet getDatasetClob(String sql){
        List<Clob[]> list = new ArrayList<Clob[]>();
        //System.out.println("queryDB:"+sql); 
        logger.info("queryDB:"+sql);
        ResultSet resultSetBlob = null;
        try {
        	connection = this.getConnection();
            preStament = connection.prepareStatement(sql);
            resultSet = preStament.executeQuery(); 
            resultSetBlob = resultSet;
        } catch (SQLException e) {
            //System.err.println("---访问数据库失败！---");
        	logger.error("访问数据库失败！", e);
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return resultSetBlob;
    }
}
