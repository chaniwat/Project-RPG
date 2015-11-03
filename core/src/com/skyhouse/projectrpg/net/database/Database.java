package com.skyhouse.projectrpg.net.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.mysql.jdbc.Driver;
import com.skyhouse.projectrpg.net.database.utils.MemberDatabaseUtils;

public class Database {
	
	private Connection connection;
	
	public MemberDatabaseUtils member;
	
	public Database() {
		String url = "jdbc:mysql://localhost/projectrpg";
		String username = "root";
		String password = "mezote00";
		
		try {
			Driver myDriver = new Driver();
			DriverManager.registerDriver(myDriver);
			connection = DriverManager.getConnection(url, username, password);
			Gdx.app.log("DEBUG", "Database connected!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		member = new MemberDatabaseUtils(connection);
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void close() {
		try {
			connection.close();
			Gdx.app.debug("DEBUG", "Connection close.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
