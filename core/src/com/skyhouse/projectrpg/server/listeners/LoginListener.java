package com.skyhouse.projectrpg.server.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPGGame;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.entities.data.CharacterData.CharacterActionState;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.physics.CharacterBody;
import com.skyhouse.projectrpg.server.ProjectRPGServer;
import com.skyhouse.projectrpg.server.packets.InitialRequest;
import com.skyhouse.projectrpg.server.packets.InitialResponse;

public class LoginListener {
	
	public static class ClientSide extends Listener {
		
		@Override
		public void received(final Connection connection, Object object) {
			if(object instanceof InitialResponse) {
				final InitialResponse response = (InitialResponse)object;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPGGame.characters.putIfAbsent(connection.getID(), new Character(response.data));
						ProjectRPGGame.maincharacter = ProjectRPGGame.characters.get(connection.getID());
						Gdx.input.setInputProcessor(new GameplayInputProcess(ProjectRPGGame.maincharacter));
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
