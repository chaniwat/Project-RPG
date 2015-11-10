package com.skyhouse.projectrpg.net.database.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class DatabaseUtils {

	protected Connection connection;
	
	public DatabaseUtils(Connection connection) {
		this.connection = connection;
	}
	
}
