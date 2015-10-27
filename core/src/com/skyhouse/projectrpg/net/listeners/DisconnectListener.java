package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.ProjectRPGClient;
import com.skyhouse.projectrpg.ProjectRPGServer;
import com.skyhouse.projectrpg.net.packets.DisconnectRequest;
import com.skyhouse.projectrpg.net.packets.DisconnectResponse;
import com.skyhouse.projectrpg.scene.GameScene;

public class DisconnectListener {
	
	public static class ClientSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof DisconnectResponse) {
				final DisconnectResponse response = (DisconnectResponse)object;
				
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPG.Client.gamemanager.getEntityManager().removeCharacter(response.connectionid);
					}
				});
			}
		}
		
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof DisconnectRequest) {
				DisconnectRequest request =(DisconnectRequest)object;
				ProjectRPG.Server.instances.get(request.instance).removeCharacter(connection.getID());
				
				DisconnectResponse response = new DisconnectResponse();
				response.connectionid = connection.getID();
				ProjectRPG.Server.net.sendToAllExceptTCP(connection.getID(), response);
			}
		}
		
	}
	
}
