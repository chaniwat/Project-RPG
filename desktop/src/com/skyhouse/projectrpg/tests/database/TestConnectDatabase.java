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
		
		// try-catch ��������ͨѺ Error �ҡ��������� Database ������ (�Ҩ���Դ�ҡ syntax SQL �Դ �ç�� �������͵��� server �����)
		try {
			// ���ҧ Driver **��鹵͹�ǡ����鴵�µ�ǹ� ��� setup ���������� ��С� ��ͧ��� Internet ���¹� ����������Ѻ server ���
			Driver myDriver = new Driver();
			DriverManager.registerDriver(myDriver);
			
			// ���ҧ�����������
			Connection connection = DriverManager.getConnection(url, username, password);
			
			System.out.println("Database connected!");
			
			// �Դ����������� **�ӷء��������ͨ������ (�������ش�ͧ�ѧ����)
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TestConnectDatabase();
	}
	
}
