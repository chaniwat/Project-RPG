package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.ProjectRPGServer;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;
import com.skyhouse.projectrpg.net.packets.UpdateResponse;

/**
 * Update listener.
 * @author Meranote
 *
 */
public class UpdateListener {
	
	private UpdateListener() {}
	
	/**
	 * Update listener for client-side.
	 * @author Meranote
	 */
	public static class Client extends Listener {
						
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof UpdateResponse) {
				final UpdateResponse response = (UpdateResponse)object;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPG.client.entitymanager.updateAllCharacter(ProjectRPG.client.gamemanager.getUID(), response.data, false);
					}
				});
			}
		}
	
	}
	
	/**
	 * Update listener for server-side.
	 * @author Meranote
	 */
	public static class Server extends Listener {
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof UpdateRequest) {
				UpdateRequest request = (UpdateRequest)object;
				ProjectRPGServer.instances.get(request.currentInstance).updateCharacter(request.uid, request.input);
			}
		}
	}
	
}
