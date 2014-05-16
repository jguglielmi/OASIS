
// Name: DatabaseAdapter
// Author: Edward Jakubowski ejakubowski@qed-sys.com
// Last update: 11/12/2013
// Description: This Fixture add support to run Database commands in fitnesse
// Requirements: database libs (like sqljdbc4.jar)
// Examples:

package org.qedsys.fitnesse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.qedsys.fitnesse.*;

public class SqlDriverFixture {
	
	Connection dbConn = null;
	String dbType = "";
	public static void main(String[] args) {
		System.out.println("starting driver...");

		System.out.println("done.");
	}
	
	public boolean connectToAsUserWithPassword( String databaseStr, String user, String pwd) {
		pwd = org.oasis.plugin.Util.processDecryptionString(pwd);
		dbConn = DatabaseAdapter.connect(databaseStr, user, pwd);
		dbType = DatabaseAdapter.getDbType(databaseStr);
		if (dbConn != null)
			return true;
		else
			return false;
	}

	public boolean connectTo( String databaseStr) {
		dbConn = DatabaseAdapter.connect(databaseStr, null, null);
		dbType = DatabaseAdapter.getDbType(databaseStr);
		if (dbConn != null)
			return true;
		else
			return false;
	}
	
	public boolean executeSql(String sql) {
		try {
			ResultSet rs = DatabaseAdapter.executeSql(dbConn, sql);
			return rs.next();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int executeCount(String sql) {
		try {
			ResultSet rs = DatabaseAdapter.executeSql(dbConn, sql);
			if (rs.next())
				return rs.getInt(1);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public String executeGetString(String sql) {
		try {
			ResultSet rs = DatabaseAdapter.executeSql(dbConn, sql);
			if (rs.next())
				return rs.getString(1);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String listTables() {
		String result = "";
		List<String> tables = DatabaseAdapter.getTableList(dbConn, dbType);
		for (String table: tables) {
			result += table + ",";
		}
		return result;
	}
	
	public boolean tableExists(String tableName) {
		List<String> tables = DatabaseAdapter.getTableList(dbConn, dbType);
		return tables.contains(tableName);
	}	
}