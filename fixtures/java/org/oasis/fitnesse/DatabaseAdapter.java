
// Name: DatabaseAdapter
// Author: Edward Jakubowski ejakubowski7@gmail.com
// Last update: 04/06/2014
// Description: This Fixture add support to run Database commands in fitnesse
// Requirements: database libs (like sqljdbc4.jar)
// Examples:
//   Connection sourceConn = DatabaseAdapter.connect(sourceDbStr, dbUser, dbPassword);
//   sourceDbType = DatabaseAdapter.getDbType(sourceDbStr);
//   ResultSet count = DatabaseAdapter.executeSql(sourceConn, "select count(*) from test-table");
//   if (count.next())
//     tableCount = count.getInt(1);

package org.oasis.fitnesse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

public class DatabaseAdapter {
	
	public final static String DB_TYPE_SQLSERVER = "sqlserver"; 
	public final static String DB_TYPE_ORACLE = "oracle";
	public final static String DB_TYPE_UNKNOWN = "unknown"; 
	
	public static String getDbType(String dbConnectStr){
		//sqlDbConnect("jdbc:sqlserver://localhost:1433;instanceName=INSTANCE1;database=AdventureWorks;integratedSecurity=true;");
		//jdbc:oracle:thin:@//oracle.hostserver2.mydomain.ca:1522/ABCD
		if (dbConnectStr.contains("jdbc:sqlserver:"))
			return DB_TYPE_SQLSERVER;
		else if (dbConnectStr.contains("jdbc:oracle:"))
			return DB_TYPE_ORACLE;
		else
			return DB_TYPE_UNKNOWN;
	}
	
	public static Connection connect(String dbConnectStr, String dbUser, String dbPassword) {
		String dbType = getDbType(dbConnectStr);
		if (dbType == DB_TYPE_SQLSERVER){
			return sqlConnect(dbConnectStr, dbUser, dbPassword);
		}
		return null;
	}

	public static String getDatabaseName(Connection conn, String dbType) {

		if (dbType == DB_TYPE_SQLSERVER){
			return sqlGetDatabaseName(conn);
		}
		
		return "";
	}

	public static List<String> getTableList(Connection conn, String dbType) {
		List<String> tables = new ArrayList<String>();
		
		if (dbType == DB_TYPE_SQLSERVER){
			return sqlGetTableList(conn);
		}
		
		return tables;
	}

	public static List<String> getColumnList(Connection conn, String dbType, String tableName) {
		List<String> tables = new ArrayList<String>();
		
		if (dbType == DB_TYPE_SQLSERVER){
			return sqlGetColumnList(conn, tableName);
		}
		
		return tables;
	}
	
	public static String getAllSafeColumns(Connection conn, String dbType, String tableName, List<String> columnFilterList) {		
		if (dbType == DB_TYPE_SQLSERVER){
			return sqlGetAllSafeColumns(conn, tableName, columnFilterList);
		}
		return "";
	}
	
	public static String getUniqueIdentifierFieldname(Connection conn, String dbType, String tableName) {
		if (dbType == DB_TYPE_SQLSERVER){
			return sqlGetUniqueIdentifierFieldname(conn, tableName);
		}
		return "";
	}
	
	public static void executeSqlPrintResults(Connection conn, String sql) {
		try {
			Statement statement = conn.createStatement();
			//String sql = "select * from sysobjects where type='u'";
			//String sql = "SELECT * FROM sys.Tables WHERE type = 'U'";
			//SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'ALL_SOURCE'
			ResultSet rs = statement.executeQuery(sql);
			//print column header
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int c = 1 ; c < rsmd.getColumnCount() ; c++)
				System.out.print(rsmd.getColumnName(c) + ", ");
			System.out.println("");
			
			//print recordset data
			while (rs.next()) {
				for (int c = 1 ; c < rsmd.getColumnCount() ; c++)
					System.out.print(rs.getString(c) + ", ");
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Tediff.writeToLog(e);
		}
	}

	public static ResultSet executeSql(Connection conn, String sql) {
		ResultSet rs = null;
		try {
			Statement statement = conn.createStatement();
			rs = statement.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}	
	
	public static List<String> executeSqlListResults(Connection conn, String sql) {
		List<String> result = new ArrayList<String>();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//get recordset data
			while (rs.next()) {
				String row = "";
				for (int c = 1 ; c < rsmd.getColumnCount() ; c++)
					row += rs.getString(c) + ",";
				result.add(row);
			}
		} catch (Exception e) {
			result.add("error: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/* 
	 * SQL Server Specific Code below
	 */
	private static Connection sqlConnect(String dbConnectStr, String dbUser, String dbPassword)
	{
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(dbConnectStr, dbUser, dbPassword);
			//System.out.println("connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	private static String sqlGetDatabaseName(Connection conn) {
		String dbName = "";
		String sql = "SELECT DISTINCT TABLE_CATALOG FROM INFORMATION_SCHEMA.COLUMNS";
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			//print recordset data
			while (rs.next()) {
				dbName = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbName;
	}

	private static List<String> sqlGetTableList(Connection conn) {
		List<String> tables = new ArrayList<String>();
		String sql = "SELECT name FROM sys.Tables WHERE type = 'U' order by name";
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			//print recordset data
			while (rs.next()) {
				tables.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tables;
	}

	private static List<String> sqlGetColumnList(Connection conn, String tableName) {
		List<String> tables = new ArrayList<String>();
		String sql = "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? order by TABLE_NAME,COLUMN_NAME";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tableName);
			ResultSet rs = ps.executeQuery();
			//print recordset data
			while (rs.next()) {
				tables.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tables;
	}

	private static String sqlGetAllSafeColumns(Connection conn, String tableName, List<String> columnFilterList) {
		String result = "";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? order by TABLE_NAME,COLUMN_NAME";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tableName);
			ResultSet rs = ps.executeQuery();
			//print recordset data
			while (rs.next()) {
				if (columnFilterList.size() > 0) // filter columns if the columnFilterList is used
					if (!columnFilterList.contains(tableName + "." + rs.getString(1)))
						continue;
				if (rs.getString(2).equals("ntext"))
					result += "rtrim(cast(" + rs.getString(1) + " as nvarchar(max))) as " + rs.getString(1) + ", ";
				else if (rs.getString(2).equals("text"))
					result += "rtrim(cast(" + rs.getString(1) + " as nvarchar(max))) as " + rs.getString(1) + ", ";
				else if (rs.getString(2).equals("sql_variant"))
					result += "rtrim(cast(" + rs.getString(1) + " as nvarchar(max))) as " + rs.getString(1) + ", ";
				else
					result += "[" + rs.getString(1) + "]" + ", ";
			}
			if (result.endsWith(", "))
				result = result.substring(0,result.length() - 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String sqlGetUniqueIdentifierFieldname(Connection conn, String tableName) {
		String uiName = "";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? order by TABLE_NAME,COLUMN_NAME";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tableName);
			ResultSet rs = ps.executeQuery();
			//print recordset data
			while (rs.next()) {
				if (rs.getString(2).equals("uniqueidentifier")) {
					uiName = rs.getString(1);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return uiName;
	}
	
}
