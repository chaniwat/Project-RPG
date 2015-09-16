package com.skyhouse.projectrpg.server.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPGGame;
import com.skyhouse.projectrpg.server.ProjectRPGServer;
import com.skyhouse.projectrpg.server.packets.DisconnectRequest;
import com.skyhouse.projectrpg.server.packets.DisconnectResponse;

public class DisconnectListener {
	
	public static class ClientSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof DisconnectResponse) {
				final DisconnectResponse response = (DisconnectResponse)object;
				
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPGGame.characters.get(response.connectionid).dispose();
						ProjectRPGGame.characters.remove(response.connectionid);
					}
				});
			}
		}
		
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof DisconnectRequest) {
				ProjectRPGServer.characters.get(connection.getID()).dispose();
				ProjectRPGServer.characters.remove(connection.getID());
				
				DisconnectResponse response = new DisconnectResponse();
				response.connectionid = connection.getID();
				ProjectRPGServer.server.sendToAllExceptTCP(connection.getID(), response);
			}
		}
		
	}
	
}
