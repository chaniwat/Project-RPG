package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.CharacterRequest;
import com.skyhouse.projectrpg.net.packets.CharacterResponse;
import com.skyhouse.projectrpg.scene.CharacterCreatorScene;

public class GameDataListener {

	private GameDataListener() {}
	
	public static class Client extends Listener {
	
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof CharacterResponse) {
				handleCharacterResponse((CharacterResponse)object);
			}
		}
		
		private void handleCharacterResponse(CharacterResponse response) {
			if(response.data != null) {
				Gdx.app.log("DEBUG", "This account already created character with name : " + response.data.name);
			} else {
				ProjectRPG.client.scenemanager.setUseScene(CharacterCreatorScene.class);
			}
		}
		
	}
	
	public static class Server extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof CharacterRequest) {
				CharacterRequest request = (CharacterRequest)object;
				CharacterResponse response = new CharacterResponse();
				response.data = ProjectRPG.server.database.game.getCharacterData(request.uid);
				ProjectRPG.server.net.sendToTCP(connection.getID(), response);
			}
		}
		
	}

}
