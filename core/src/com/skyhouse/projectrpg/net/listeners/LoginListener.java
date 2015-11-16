package com.skyhouse.projectrpg.net.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.CharacterRequest;
import com.skyhouse.projectrpg.net.packets.LoginRequest;
import com.skyhouse.projectrpg.net.packets.LoginResponse;
import com.skyhouse.projectrpg.scene.HomeScene;

/**
 * Login listener.
 * @author Meranote
 */
public class LoginListener {
	
	private LoginListener() {}
	
	/**
	 * Login listener for client-side.
	 * @author Meranote
	 */
	public static class Client extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof LoginResponse) {
				LoginResponse response = (LoginResponse)object;
				if(response.uid >= 1) {
					ProjectRPG.client.gamemanager.setUID(response.uid);
					requestCharacter(response.uid);
				}
				else if(response.uid == -1) ProjectRPG.client.scenemanager.getScene(HomeScene.class).showErrorDialog("ไอดีหรือรหัสผ่านผิดหลาด");
				else if(response.uid == -2) ProjectRPG.client.scenemanager.getScene(HomeScene.class).showErrorDialog("มีการลงชื่อเข้าใช้ระบบอยู่");
			}
		}
		
		private void requestCharacter(int uid) {
			CharacterRequest request = new CharacterRequest();
			request.uid = uid;
			ProjectRPG.client.network.net.sendTCP(request);
		}
		
	}
	
	
	/**
	 * Login listener for server-side.
	 * @author Meranote
	 */
	public static class Server extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof LoginRequest) {
				LoginRequest request = (LoginRequest)object;
				LoginResponse response = new LoginResponse();
				response.uid = ProjectRPG.server.database.game.getLogin(request.username, request.password);
				if(ProjectRPG.server.system.playermanagement.isUserLogin(response.uid)) {
					response.uid = -2;
				} 
				if(response.uid > 0f){
					ProjectRPG.server.system.playermanagement.addClientPlayer(connection.getID(), response.uid);
				}
				ProjectRPG.server.net.sendToTCP(connection.getID(), response);
			}
		}
		
	}
	
}
