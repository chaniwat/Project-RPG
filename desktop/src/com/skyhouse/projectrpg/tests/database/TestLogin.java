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
		
		// ���ͺ�����͡�Թ
		testLogin("tester", "tester001");
		
		try {
			database.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void testLogin(String username, String password) {
		try {
			// ��¹���� SQL : ���͡�ء���ҧ�ҡ㹵��ҧ member ����Ңͧ������� username ��ҡѺ *����÷�� 1* ��� password ��ҡѺ *����÷�� 2*
			String sql = "SELECT * FROM member WHERE username = ? AND password = ?";
			// ����� statement �ҡ�����¹
			statement = connection.prepareStatement(sql);
			// ����� username ŧ����÷�� 1 �繤��ʵ�ԧ
			statement.setString(1, username);
			// ����� password ŧ����÷�� 2 �繤��ʵ�ԧ
			statement.setString(2, password);
			// �����ż� (��ǹ���еԴ��ʹ٢����ź� database ����觼��Ѿ���Ѻ��)
			result = statement.executeQuery();
			
			// ����͹��ѧ���˹��ش���¢ͧ���Ѿ��
			result.last();
			
			// ��ҵ��˹觢ͧ���ҡ�����Ƿ�� 1 �ʴ�����բ����� ��� username �Ѻ password �ç�ѹ
			if(result.getRow() >= 1) {
				System.out.println("Login success!");
			} else {
				System.out.println("Login failed! Incorrect username or password, or not registered.");
			}
			
			// ��ҧ statement �Ѻ result
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
