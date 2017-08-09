package com.trs.service.test;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
public class Appquery extends JFrame {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private Connection conn; 
		private Statement statement; 
		private ResultSet resultSet;
		
		//GUI变量定义
		private JTable table; 
		private JTextArea inputQuery; 
		private JButton submitQuery; 

		@SuppressWarnings("deprecation")
		public Appquery() {
			
			//Form的标题
			super( "输入SQL语句，按查询按钮查看结果。" ); 
			
//			String url = "jdbc:mysql://localhost:3306/studentManager?useUnicode=true&amp;characterEncoding=UTF-8";
			
			String url = "jdbc:oracle:thin:@192.168.1.199:1521:orcl";
			
			String username = "trsapp";
			String password = "trsadmin"; 
			
			//加载驱动程序以连接数据库
			try {
				Class.forName( "oracle.jdbc.driver.OracleDriver" );
				conn = DriverManager.getConnection(url, username, password);
			}catch ( ClassNotFoundException cnfex ) { //捕获加载驱动程序异常
				System.err.println("装载 JDBC/ODBC 驱动程序失败。" );
				cnfex.printStackTrace(); 
				System.exit( 1 ); // terminate program 
			}catch ( SQLException sqlex ) { //捕获连接数据库异常
				System.err.println( "无法连接数据库" ); 
				sqlex.printStackTrace(); 
				System.exit( 1 ); // terminate program 
			} 
			
			//如果数据库连接成功，则建立GUI
			//SQL语句
			String test = "SELECT * FROM app_user"; 
			inputQuery  = new JTextArea( test, 4, 30 ); 
			submitQuery = new JButton( "查询" ); 
			
			//Button事件
			submitQuery.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent e ){ 
					getTable();
				}
			}); 
			
			JPanel topPanel = new JPanel(); 
			topPanel.setLayout( new BorderLayout() ); 
			
			//将"输入查询"编辑框布置到 "CENTER"
			topPanel.add( new JScrollPane( inputQuery), BorderLayout.CENTER ); 
			
			//将"提交查询"按钮布置到 "SOUTH"
			topPanel.add( submitQuery, BorderLayout.SOUTH ); 
			table = new JTable(); 
			Container c = getContentPane(); 
			c.setLayout( new BorderLayout() ); 
			
			//将"topPanel"编辑框布置到 "NORTH"
			c.add( topPanel, BorderLayout.NORTH ); 
			
			//将"table"编辑框布置到 "CENTER"
			c.add( table, BorderLayout.CENTER ); 
			getTable(); 
			setSize( 500, 300 ); 
			
			//显示Form
			show(); 
		} 

		private void getTable(){
			
			try { 
				//执行SQL语句
				String query = inputQuery.getText(); 
				statement = conn.createStatement(); 
				resultSet = statement.executeQuery( query ); 
				
				//在表格中显示查询结果
				displayResultSet( resultSet ); 
			}catch ( SQLException sqlex ) { 
				sqlex.printStackTrace(); 
			} 
		} 
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void displayResultSet( ResultSet rs ) throws SQLException { 
			
			//定位到达第一条记录
			boolean moreRecords = rs.next(); 
			
			//如果没有记录，则提示一条消息
			if ( ! moreRecords ) { 
				JOptionPane.showMessageDialog( this, "结果集中无记录" ); 
				setTitle( "无记录显示" ); 
				return; 
			} 
			
			Vector columnHeads = new Vector(); 
			Vector rows = new Vector(); 
			
			try { 
				//获取字段的名称
				ResultSetMetaData rsmd = rs.getMetaData(); 			
				
				for ( int i = 1; i <= rsmd.getColumnCount(); ++i ) 
					columnHeads.addElement( rsmd.getColumnName( i ) );
				
				//获取记录集
				do {
					rows.addElement( getNextRow( rs, rsmd ) ); 
				} while ( rs.next() ); 
				
				//在表格中显示查询结果
				table = new JTable( rows, columnHeads ); 
				JScrollPane scroller = new JScrollPane( table ); 
				Container c = getContentPane(); 
				c.remove(1); 
				c.add( scroller, BorderLayout.CENTER ); 
				
				//刷新Table
				c.validate();
			}catch ( SQLException sqlex ) { 
				sqlex.printStackTrace(); 
			} 
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private Vector getNextRow( ResultSet rs, ResultSetMetaData rsmd )throws SQLException{
			Vector currentRow = new Vector(); 
			
			for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
				currentRow.addElement( rs.getString( i ) ); 
			
			//返回一条记录 
			return currentRow; 
		} 

		public void shutDown() {
			
			try {
				//断开数据库连接
				conn.close(); 
			}catch ( SQLException sqlex ) {
				System.err.println( "Unable to disconnect" );
				sqlex.printStackTrace();
			}
		} 

		public static void main( String args[] ){
			
			final Appquery app = new Appquery(); 
			
			app.addWindowListener( new WindowAdapter() {
				public void windowClosing( WindowEvent e ){
					app.shutDown();
					System.exit( 0 ); 
				}
			});
		}

}
