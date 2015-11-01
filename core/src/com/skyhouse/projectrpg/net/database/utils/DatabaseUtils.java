package com.skyhouse.projectrpg.net.database.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class DatabaseUtils {

	protected Connection connection;
	protected PreparedStatement statement;
	protected ResultSet result;
	
	public DatabaseUtils(Connection connection) {
		this.connection = connection;
	}
	
}
