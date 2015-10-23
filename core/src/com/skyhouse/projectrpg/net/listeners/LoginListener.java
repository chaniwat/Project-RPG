package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.game.GameManager;
import com.skyhouse.projectrpg.net.packets.InitialRequest;
import com.skyhouse.projectrpg.net.packets.InitialResponse;
import com.skyhouse.projectrpg.scene.GameScene;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class LoginListener {
	
	public static class ClientSide extends Listener {
		
		@Override
		public void received(final Connection connection, Object object) {
			if(object instanceof InitialResponse) {
				final CharacterData data = ((InitialResponse)object).data;
				Gdx.app.postRunnable(new Runnable() {
					
					@Override
					public void run() {
						GameManager manager = ProjectRPG.Client.scenemanager.getScene("gamescene", GameScene.class).getGameManager();
						manager.addCharacter(data, new SpriterPlayer("entity/GreyGuy/player.scml"));
						manager.setControlCharacter(data.id);
					}
				});
				
			}
		}
		
	}
	
	public static class ServerSide extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof InitialRequest) {
				CharacterData c = new CharacterData();
				c.id = connection.getID();
				c.x = (float)((Math.random() * 7) + 5);
				c.y = 8f;
				ProjectRPG.Server.instances.get("main").addCharacter(c);
				InitialResponse r = new InitialResponse();
				r.data = c;
				connection.sendTCP(r);
			}
		}
	}
	
}
