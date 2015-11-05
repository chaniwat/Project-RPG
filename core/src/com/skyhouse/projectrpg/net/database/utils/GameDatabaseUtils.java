package com.skyhouse.projectrpg.net.database.utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.skyhouse.projectrpg.data.CharacterData;

/**
 * Utility database : member section.<br>
 * Collection of function that related to member detail data.
 * @author Meranote
 */
public class GameDatabaseUtils extends DatabaseUtils {
	
	/**
	 * Construct a new member utility for database.
	 * @param connection
	 */
	public GameDatabaseUtils(Connection connection) {
		super(connection);
	}
	
	/**
	 * Check username and password. <br>
	 * @return <b>uid</b> or <b>-1</b> if not match to any record
	 */
	public int getLogin(final String username, final String password) {
		int uid = 0;		
		try {
			String sql = "SELECT * FROM member WHERE username = ? AND password = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() >= 1) {
				uid =  result.getInt("uid");
			} else {
				uid = -1;
			}
			
			statement.close();
			result.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return uid;
	}
	
	/**
	 * Get character data by uid.
	 * @return {@link CharacterData} or <b>null</b> if not exist in "game_data" table.
	 */
	public CharacterData getCharacterData(final int uid) {
		CharacterData data = null;
		
		try {
			String sql = "SELECT name, last_x, last_y FROM game_data WHERE uid = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() >= 1) {
				data = new CharacterData();
				data.name = result.getString("name");
				data.x = result.getInt("last_x");
				data.y = result.getInt("last_y");
			}
			
			statement.close();
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	/**
	 * Get last instance of uid.
	 * @return instance name
	 */
	public String getLastInstance(final int uid) {
		String last_instance = "";
		
		try {
			String sql = "SELECT last_instance FROM game_data WHERE uid = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, uid);
			result = statement.executeQuery();
			
			last_instance = result.getString("last_instance");
			
			statement.close();
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return last_instance;
	}

}
