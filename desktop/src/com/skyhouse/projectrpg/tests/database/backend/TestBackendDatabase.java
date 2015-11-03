package com.skyhouse.projectrpg.tests.database.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

public class TestBackendDatabase {
	
	private String url = "jdbc:mysql://188.166.232.27/projectrpg";
	private String username = "root";
	private String password = "projectrpg00";
	private Connection connection;
	
	public TestBackendDatabase() {
		try {
			Driver myDriver = new Driver();
			DriverManager.registerDriver(myDriver);
			connection = DriverManager.getConnection(url, username, password);
			
			System.out.println("Database connected!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void close() throws SQLException {
		connection.close();
	}

}
