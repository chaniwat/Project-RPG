package com.skyhouse.projectrpg.net.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import com.badlogic.gdx.Gdx;
import com.mysql.jdbc.Driver;
import com.skyhouse.projectrpg.net.database.utils.PlayerDatabaseUtils;

/**
 * Database.
 * @author Meranote
 */
public class Database {
	
	private Connection connection;
	
	public PlayerDatabaseUtils game;
	
	private static final String DEFAULTURL = "jdbc:mysql://188.166.232.27/projectrpg";
	private static final String DEFAULTUSERNAME = "root";
	private static final String DEFAULTPASSWORD = "projectrpg00";
	
	/**
	 * Construct connection to database.
	 */
	public Database() {
		
		// TODO read config.ini
		
		try {
			Driver myDriver = new Driver();
			DriverManager.registerDriver(myDriver);
			connection = DriverManager.getConnection(DEFAULTURL, DEFAULTUSERNAME, DEFAULTPASSWORD);
			Gdx.app.log("DEBUG", "Database connected!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		game = new PlayerDatabaseUtils(connection);
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
