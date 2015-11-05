package com.skyhouse.projectrpg.net.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import com.badlogic.gdx.Gdx;
import com.mysql.jdbc.Driver;
import com.skyhouse.projectrpg.net.database.utils.GameDatabaseUtils;

/**
 * Database.
 * @author Meranote
 */
public class Database {
	
	private Connection connection;
	
	public GameDatabaseUtils game;
	
	/**
	 * Construct connection to database.
	 */
	public Database() {
		String url = "jdbc:mysql://188.166.232.27/projectrpg";
		String username = "root";
		String password = "projectrpg00";
		
		try {
			Driver myDriver = new Driver();
			DriverManager.registerDriver(myDriver);
			connection = DriverManager.getConnection(url, username, password);
			Gdx.app.log("DEBUG", "Database connected!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		game = new GameDatabaseUtils(connection);
	}
	
	/**
	 * Get database connection.
	 * @return {@link Connection}
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Close connection to database.
	 */
	public void close() {
		try {
			connection.close();
			Gdx.app.debug("DEBUG", "Connection close.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
