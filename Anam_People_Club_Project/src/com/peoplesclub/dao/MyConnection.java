package com.peoplesclub.dao;

import java.sql.*;

public class MyConnection {

	private final static String driver="oracle.jdbc.OracleDriver";
	private final static String url="jdbc:oracle:thin:@localhost:1521:xe";
	private final static String userName="system";
	private final static String password="anam2003";
	static Connection con;
	public static Connection getConnection(){
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url,userName,password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	public static void closeResources(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*
		public static void main(String[] args) {
			System.out.println("connection: "+getConnection());
		}
	 */
}
