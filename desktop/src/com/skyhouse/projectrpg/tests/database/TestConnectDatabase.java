package com.skyhouse.projectrpg.tests.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.badlogic.gdx.Gdx;
import com.mysql.jdbc.Driver;

public class TestConnectDatabase {
	
	public TestConnectDatabase() {
		String url = "jdbc:mysql://188.166.232.27/projectrpg";
		String username = "root";
		String password = "projectrpg00";
		
		// try-catch มีไว้เพื่อจับ Error จากการเชื่อมไป Database ทั้งหมด (อาจจะเกิดจาก syntax SQL ผิด ไรงี้ หรือเชื่อต่อไป server ไม่ได้)
		try {
			// สร้าง Driver **ขั้นตอนพวกนี้โค้ดตายตัวนะ เรา setup ไว้หมดแล้ว และก็ ต้องต่อ Internet ด้วยนะ เพราะเชื่อมกับ server ไปละ
			Driver myDriver = new Driver();
			DriverManager.registerDriver(myDriver);
			
			// สร้างการเชื่อมต่อ
			Connection connection = DriverManager.getConnection(url, username, password);
			
			System.out.println("Database connected!");
			
			// ปิดการเชื่อมต่อ **ทำทุกครั้งเมื่อจบโปรแกรม (ไว้ท้ายสุดของฟังก์ชั่น)
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TestConnectDatabase();
	}
	
}
