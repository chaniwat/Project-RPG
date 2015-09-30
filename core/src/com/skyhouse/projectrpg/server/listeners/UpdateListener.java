package com.skyhouse.projectrpg.server.listeners;

import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPGGame;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.server.ProjectRPGServer;
import com.skyhouse.projectrpg.server.packets.CharacterDataPacket;

public class UpdateListener {
	
	public static class ClientSide extends Listener {
		
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
							if(ProjectRPGGame.characters.getOrDefault(connectionid, null) == null) {
								ProjectRPGGame.characters.put(connectionid, new Character(data));
							}
							ProjectRPGGame.characters.get(connectionid).setPosition(data.getPositionX(), data.getPositionY());
							ProjectRPGGame.characters.get(connectionid).setFilpX(data.isFlipX());
							ProjectRPGGame.characters.get(connectionid).actionstate = data.actionstate;
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
