package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.net.instance.TownInstance;
import com.skyhouse.projectrpg.net.packets.CharacterRequest;
import com.skyhouse.projectrpg.net.packets.CharacterResponse;
import com.skyhouse.projectrpg.net.packets.ClientReadyRequest;
import com.skyhouse.projectrpg.net.packets.CreateCharacterRequest;
import com.skyhouse.projectrpg.net.packets.CreateCharacterResponse;
import com.skyhouse.projectrpg.net.packets.ConnectToPlayRequest;
import com.skyhouse.projectrpg.net.packets.ConnectToPlayResponse;
import com.skyhouse.projectrpg.scene.CharacterCreatorScene;
import com.skyhouse.projectrpg.scene.LoadingScene;
import com.skyhouse.projectrpg.scene.MenuScene;
import com.skyhouse.projectrpg.scene.menu.tab.CharacterTab;
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
			if(object instanceof ConnectToPlayResponse) {
				handlePlayResponse((ConnectToPlayResponse)object);
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
		
		private void handlePlayResponse(final ConnectToPlayResponse response) {
			if(response.state == 1) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPG.client.gamemanager.setCurrentInstance(response.instance);
						ProjectRPG.client.mapmanager.changeMap(response.mappath, ProjectRPG.client.gamemanager.getGameWorld());
						ProjectRPG.client.entitymanager.addCharacter(ProjectRPG.client.gamemanager.getUID(), response.data, ProjectRPG.client.gamemanager.getGameWorld());
						ProjectRPG.client.scenemanager.getScene(MenuScene.class).getMenuTab(CharacterTab.class).updatePlayerCharacter(response.data);
						ProjectRPG.client.scenemanager.setUseScene(LoadingScene.class);
					}
				});
			}
		}
		
		private void requestToPlay(int uid, CharacterData data) {
			ConnectToPlayRequest request = new ConnectToPlayRequest();
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
			if(object instanceof ConnectToPlayRequest) {
				handlePlayRequest(connection.getID(), (ConnectToPlayRequest)object);
			}
			if(object instanceof ClientReadyRequest) {
				ProjectRPG.server.system.playermanagement.playerReady(connection.getID());
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
		
		private void handlePlayRequest(int connectionID, ConnectToPlayRequest request) {
			TownInstance instance = ProjectRPG.server.townInstance;
			request.data = ProjectRPG.server.system.playermanagement.prepareConnectPlayer(request.uid, request.data, instance.getName());
			ConnectToPlayResponse response = new ConnectToPlayResponse();
			response.state = 1;
			response.data = request.data;
			response.instance = instance.getName();
			response.mappath = instance.getMapPath();
			ProjectRPG.server.net.sendToTCP(connectionID, response);
		}
	}

}
