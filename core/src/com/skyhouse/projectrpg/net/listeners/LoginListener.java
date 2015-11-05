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
				if(response.uid >= 1) requestCharacter(response.uid);
				else if(response.uid == -1) ProjectRPG.client.scenemanager.getScene(HomeScene.class).showErrorDialog("Incorrect username or password.");
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
				ProjectRPG.server.net.sendToTCP(connection.getID(), response);
			}
		}
		
	}
	
}
