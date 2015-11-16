package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.DisconnectRequest;
import com.skyhouse.projectrpg.net.packets.DisconnectResponse;

/**
 * Disconnect listener.
 * @author Meranote
 */
public class DisconnectListener {
	
	private DisconnectListener() {};
	
	/**
	 * Disconnect listener for client-side.
	 * @author Meranote
	 */
	public static class Client extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof DisconnectResponse) {
				final DisconnectResponse response = (DisconnectResponse)object;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPG.client.gamemanager.getEntityManager().removeCharacter(response.uid);
					}
				});
			}
		}
	}
	
	/**
	 * Disconnect listener for server-side.
	 * @author Meranote
	 */
	public static class Server extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof DisconnectRequest) {
				
			}
		}
		
		@Override
		public void disconnected(Connection connection) {
			if(ProjectRPG.server.system.playermanagement.containConnectionID(connection.getID())) {
				int uid = ProjectRPG.server.system.playermanagement.getUID(connection.getID());
				ProjectRPG.server.system.playermanagement.disconnectPlayer(connection.getID());
			}
		}
		
	}
	
}
