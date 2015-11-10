package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.net.packets.CharacterRequest;
import com.skyhouse.projectrpg.net.packets.CharacterResponse;
import com.skyhouse.projectrpg.net.packets.CreateCharacterRequest;
import com.skyhouse.projectrpg.net.packets.CreateCharacterResponse;
import com.skyhouse.projectrpg.net.packets.PlayRequest;
import com.skyhouse.projectrpg.net.packets.PlayResponse;
import com.skyhouse.projectrpg.scene.CharacterCreatorScene;
import com.skyhouse.projectrpg.scene.LoadingScene;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class GameDataListener {

	private GameDataListener() {}
	
	public static class Client extends Listener {
	
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof CharacterResponse) {
				handleCharacterResponse((CharacterResponse)object);
			}
			if(object instanceof CreateCharacterResponse) {
				handleCreateCharacterResponse((CreateCharacterResponse)object);
			}
			if(object instanceof PlayResponse) {
				handlePlayResponse((PlayResponse)object);
			}
		}
		
		private void handleCharacterResponse(CharacterResponse response) {
			if(response.data != null) {
				Gdx.app.debug("DEBUG", "คุณได้สร้างตัวละครแล้ว ชื่อ : " + response.data.name);
				requestToPlay(ProjectRPG.client.gamemanager.getUID(), response.data);
			} else {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPG.client.scenemanager.addScene(CharacterCreatorScene.class);
						ProjectRPG.client.scenemanager.setUseScene(CharacterCreatorScene.class);
					}
				});
			}
		}
		
		private void handleCreateCharacterResponse(CreateCharacterResponse response) {
			if(response.state == -1) {
				((CharacterCreatorScene) ProjectRPG.client.scenemanager.getCurrentScene()).showErrorDialog("ชื่อตัวละครถูกใช้แล้ว");
			} else {
				requestToPlay(ProjectRPG.client.gamemanager.getUID(), response.data);
			}
		}
		
		private void handlePlayResponse(final PlayResponse response) {
			if(response.state == 1) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPG.client.gamemanager.setCurrentInstance(response.instance);
						ProjectRPG.client.mapmanager.changeMap(response.mappath, ProjectRPG.client.gamemanager.getGameWorld());
						ProjectRPG.client.entitymanager.addCharacter(ProjectRPG.client.gamemanager.getUID(), response.data, new SpriterPlayer("entity/GreyGuy/player.scml"), ProjectRPG.client.gamemanager.getGameWorld());
						ProjectRPG.client.scenemanager.setUseScene(LoadingScene.class);
					}
				});
			}
		}
		
		private void requestToPlay(int uid, CharacterData data) {
			PlayRequest request = new PlayRequest();
			request.uid = uid;
			request.data = data;
			ProjectRPG.client.network.net.sendTCP(request);
		}
		
	}
	
	public static class Server extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof CharacterRequest) {
				handleCharacterRequest(connection.getID(), (CharacterRequest)object);
			}
			if(object instanceof CreateCharacterRequest) {
				handleCreateCharacterRequest(connection.getID(), (CreateCharacterRequest)object);
			}
			if(object instanceof PlayRequest) {
				handlePlayRequest(connection.getID(), (PlayRequest)object);
			}
		}
		
		private void handleCharacterRequest(int connectionID, CharacterRequest request) {
			CharacterResponse response = new CharacterResponse();
			response.data = ProjectRPG.server.database.game.getCharacterData(request.uid);
			ProjectRPG.server.net.sendToTCP(connectionID, response);
		}
		
		private void handleCreateCharacterRequest(int connectionID, CreateCharacterRequest request) {
			CreateCharacterResponse response = new CreateCharacterResponse();
			response.state = ProjectRPG.server.database.game.createCharacter(request.uid, request.name);
			if(response.state == 1) {
				response.data = ProjectRPG.server.database.game.getCharacterData(request.uid);
			}
			ProjectRPG.server.net.sendToTCP(connectionID, response);
		}
		
		private void handlePlayRequest(int connectionID, PlayRequest request) {
			request.data = ProjectRPG.server.system.playermanagement.connectPlayer(request.uid, request.data);
			PlayResponse response = new PlayResponse();
			response.state = 1;
			response.data = request.data;
			response.instance = ProjectRPG.server.townInstance.getName();
			response.mappath = ProjectRPG.server.townInstance.getMapPath();
			ProjectRPG.server.net.sendToTCP(connectionID, response);
		}
	}

}
