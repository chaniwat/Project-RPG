package com.skyhouse.projectrpg.server.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.entities.data.CharacterData.CharacterActionState;
import com.skyhouse.projectrpg.physics.CharacterBody;
import com.skyhouse.projectrpg.scene.GameScene;
import com.skyhouse.projectrpg.server.ProjectRPGServer;
import com.skyhouse.projectrpg.server.packets.InitialRequest;
import com.skyhouse.projectrpg.server.packets.InitialResponse;

public class LoginListener {
	
	public static class ClientSide extends Listener {
		
		GameScene scene;
		
		public ClientSide(GameScene scene) {
			this.scene = scene;
		}
		
		@Override
		public void received(final Connection connection, Object object) {
			if(object instanceof InitialResponse) {
				final InitialResponse response = (InitialResponse)object;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						scene.setMainCharacter(connection.getID(), response.data);
					}
				});
			}
		}
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof InitialRequest) {
				CharacterData character = new CharacterData(connection.getID(), -7f, 4f, CharacterActionState.IDLE);
				ProjectRPGServer.characters.putIfAbsent(connection.getID(), new CharacterBody(character));
				
				InitialResponse response = new InitialResponse();
				response.data = character;
				connection.sendTCP(response);
			}
		}
	}
	
}
