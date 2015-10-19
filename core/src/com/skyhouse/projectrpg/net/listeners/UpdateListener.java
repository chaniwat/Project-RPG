package com.skyhouse.projectrpg.net.listeners;

import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPGServer;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.net.packets.CharacterDataPacket;
import com.skyhouse.projectrpg.scene.GameScene;

public class UpdateListener {
	
	public static class ClientSide extends Listener {
		
		GameScene scene;
		
		public ClientSide(GameScene scene) {
			this.scene = scene;
		}
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof CharacterDataPacket) {
				final CharacterDataPacket update = (CharacterDataPacket)object;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						if(update.characters == null) return;
						for(Entry<Integer, CharacterData> character : update.characters.entrySet()) {
							int connectionid = character.getKey();
							CharacterData data = character.getValue();
							scene.addCharacter(connectionid, data);
							scene.updateCharacter(connectionid, data);
						}
					}
				});
			}
		}
	
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof CharacterData) {
				final CharacterData data = (CharacterData)object;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						ProjectRPGServer.characters.get(data.getID()).getData().inputstate = data.inputstate;
					}
				});
			}
		}
		
	}
	
}
