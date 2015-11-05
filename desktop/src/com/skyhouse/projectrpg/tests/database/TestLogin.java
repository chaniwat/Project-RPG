package com.skyhouse.projectrpg.tests.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.LoginResponse;
import com.skyhouse.projectrpg.tests.database.backend.TestBackendDatabase;

public class TestLogin {

	private TestBackendDatabase database;
	private Connection connection;
	private PreparedStatement statement;
	private ResultSet result;
	
	public TestLogin() {
		database = new TestBackendDatabase();
		connection = database.getConnection();
		
		// ทดสอบการล๊อกอิน
		testLogin("tester", "tester001");
		
		try {
			database.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void testLogin(String username, String password) {
		try {
			// เขียนภาษา SQL : เลือกทุกอย่างจากในตาราง member ที่ค่าของคอลัมม์ username เท่ากับ *ตัวแปรที่ 1* และ password เท่ากับ *ตัวแปรที่ 2*
			String sql = "SELECT * FROM member WHERE username = ? AND password = ?";
			// เตรียม statement จากที่เขียน
			statement = connection.prepareStatement(sql);
			// ใส่ค่า username ลงตัวแปรที่ 1 เป็นค่าสตริง
			statement.setString(1, username);
			// ใส่ค่า password ลงตัวแปรที่ 2 เป็นค่าสตริง
			statement.setString(2, password);
			// ประมวลผล (ส่วนนี้จะติดต่อดูข้อมูลบน database และส่งผลลัพธ์กลับมา)
			result = statement.executeQuery();
			
			// เลื่อนไปยังตำแหน่งสุดท้ายของผลลัพธ์
			result.last();
			
			// ถ้าตำแหน่งของแถวมากกว่าแถวที่ 1 แสดงว่ามีข้อมูล และ username กับ password ตรงกัน
			if(result.getRow() >= 1) {
				System.out.println("Login success!");
			} else {
				System.out.println("Login failed! Incorrect username or password, or not registered.");
			}
			
			// ล้าง statement กับ result
			statement.close();
			result.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TestLogin();
	}
	
}
