package com.skyhouse.projectrpg.net.database.utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.InitialResponse;

public class MemberDatabaseUtils extends DatabaseUtils {
	
	public MemberDatabaseUtils(Connection connection) {
		super(connection);
	}
	
	public void login(final int netID, final String username, final String password) {
		try {
			String sql = "SELECT * FROM member WHERE username = ? AND password = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			result = statement.executeQuery();
			
			result.last();
			
			if(result.getRow() >= 1) {
				InitialResponse response = new InitialResponse();
				response.state = 1;
				ProjectRPG.Server.net.sendToTCP(netID, response);
			} else {
				InitialResponse response = new InitialResponse();
				response.state = -1;
				ProjectRPG.Server.net.sendToTCP(netID, response);
			}
			
			statement.close();
			result.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
