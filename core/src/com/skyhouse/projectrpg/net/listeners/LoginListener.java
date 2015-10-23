package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.ProjectRPGServer;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.CharacterData.CharacterActionState;
import com.skyhouse.projectrpg.net.packets.InitialRequest;
import com.skyhouse.projectrpg.net.packets.InitialResponse;
import com.skyhouse.projectrpg.physicsO.CharacterBody;
import com.skyhouse.projectrpg.scene.GameScene;

public class LoginListener {
	
	public static class ClientSide extends Listener {
		
		@Override
		public void received(final Connection connection, Object object) {
			if(object instanceof InitialResponse) {
				final InitialResponse response = (InitialResponse)object;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPG.Client.scenemanager.getScene("gamescene", GameScene.class).setMainCharacter(connection.getID(), response.data);
					}
				});
			}
		}
		
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof InitialRequest) {
				CharacterData character = new CharacterData(connection.getID(), ((float)Math.random() * 13f) - 3, ((float)Math.random() * 5f) + 4, CharacterActionState.IDLE);
				ProjectRPGServer.characters.putIfAbsent(connection.getID(), new CharacterBody(character));
				
				InitialResponse response = new InitialResponse();
				response.data = character;
				connection.sendTCP(response);
			}
		}
	}
	
}
