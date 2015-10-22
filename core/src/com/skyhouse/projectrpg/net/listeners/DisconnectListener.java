package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPGClient;
import com.skyhouse.projectrpg.ProjectRPGServer;
import com.skyhouse.projectrpg.net.packets.DisconnectRequest;
import com.skyhouse.projectrpg.net.packets.DisconnectResponse;
import com.skyhouse.projectrpg.scene.GameScene;

public class DisconnectListener {
	
	public static class ClientSide extends Listener {
		
		GameScene scene;
		
		public  ClientSide(GameScene scene) {
			this.scene = scene;
		}
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof DisconnectResponse) {
				final DisconnectResponse response = (DisconnectResponse)object;
				
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						scene.removeCharacter(response.connectionid);
					}
				});
			}
		}
		
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void disconnected(Connection connection) {
			if(ProjectRPGServer.characters.get(connection.getID()) != null) {
				ProjectRPGServer.characters.get(connection.getID()).dispose();
				ProjectRPGServer.characters.remove(connection.getID());				
			}
			
			DisconnectResponse response = new DisconnectResponse();
			response.connectionid = connection.getID();
			ProjectRPGServer.server.sendToAllExceptTCP(connection.getID(), response);
		}
		
	}
	
}
