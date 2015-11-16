package com.skyhouse.projectrpg.net.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mysql.jdbc.Driver;
import com.skyhouse.projectrpg.net.database.utils.PlayerDatabaseUtils;

/**
 * Database.
 * @author Meranote
 */
public class Database {
	
	private Connection connection;
	
	public PlayerDatabaseUtils game;
	
	private static final String DEFAULTURL = "jdbc:mysql://188.166.232.27/projectrpg?autoReconnect=true";
	private static final String DEFAULTUSERNAME = "root";
	private static final String DEFAULTPASSWORD = "projectrpg00";
	
	/**
	 * Construct connection to database.
	 */
	public Database() {
		
		if(!finder(Gdx.files.getLocalStoragePath() + "/config.ini")) {
			createconfig(DEFAULTURL, DEFAULTUSERNAME, DEFAULTPASSWORD);
		}
		
		String[] databasepath = readconfig();
		
		try {
			Driver myDriver = new Driver();
			DriverManager.registerDriver(myDriver);
			connection = DriverManager.getConnection(databasepath[0], databasepath[1], databasepath[2]);
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
	
	public void createconfig(String url, String user, String pass) {
		try {
			File file = new File(Gdx.files.getLocalStoragePath() + "/config.ini");
			FileWriter fwriter = new FileWriter(file);
			BufferedWriter bufWriter = new BufferedWriter(fwriter);
			
			bufWriter.write("url = "+url);
			bufWriter.newLine();
			
			bufWriter.write("user = "+user);
			bufWriter.newLine();
			
			bufWriter.write("pass = "+pass);
			bufWriter.newLine();
			
			bufWriter.close();
		} catch (IOException e) {
			throw new GdxRuntimeException(e);
		}
	}
	
	public String[] readconfig() {
		try {
			File file = new File(Gdx.files.getLocalStoragePath() + "/config.ini");
			FileReader fReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fReader);
			
			String[] values = new String[3];
			
			String[] readData;
			for(int i = 0; i < 3; i++) {
				readData = bufReader.readLine().split(" = ");
				values[i] = readData[1];
				for(int j = 2; j < readData.length; j++) {
					values[i] += "=" + readData[j];
				}
			}
			bufReader.close();
			return values;
		} catch (FileNotFoundException e) {
			throw new GdxRuntimeException(e);
		} catch (IOException e) {
			throw new GdxRuntimeException(e);
		}
	}
	
	public boolean finder(String pathtoconfig){
    	File dir = new File(pathtoconfig);
    	return dir.exists();
    }
	
}
