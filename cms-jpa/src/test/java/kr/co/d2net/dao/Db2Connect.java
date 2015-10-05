package kr.co.d2net.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Db2Connect {

	public static void main(String[] args) {
		Connection con = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			
			con = DriverManager.getConnection("jdbc:db2://14.36.147.34:50000/cms", "cms", "cms");
			System.out.println("connection");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}
		}
	}

}
