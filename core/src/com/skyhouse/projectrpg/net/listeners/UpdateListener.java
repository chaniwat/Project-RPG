package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.ProjectRPGServer;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.net.packets.UpdateRequest;
import com.skyhouse.projectrpg.net.packets.UpdateResponse;
import com.skyhouse.projectrpg.scene.GameScene;

public class UpdateListener {
	
	public static class ClientSide extends Listener {
				
		private GameManager gamemanager;
		
		public ClientSide() {
			gamemanager = ProjectRPG.Client.gamemanager;
		}
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof UpdateResponse) {
				final UpdateResponse response = (UpdateResponse)object;
				gamemanager.setCurrentInstance(response.currentInstance);
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						gamemanager.updateAllCharacter(response.data);
					}
				});
			}
		}
	
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof UpdateRequest) {
				UpdateRequest request = (UpdateRequest)object;
				ProjectRPGServer.instances.get(request.currentInstance).updateCharacter(connection.getID(), request.input);
			}
		}
		
	}
	
}
