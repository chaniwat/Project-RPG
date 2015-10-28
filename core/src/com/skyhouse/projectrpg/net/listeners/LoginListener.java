package com.skyhouse.projectrpg.net.listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.net.packets.InitialRequest;
import com.skyhouse.projectrpg.net.packets.InitialResponse;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

/**
 * Login listener.
 * @author Meranote
 */
public class LoginListener {
	
	private LoginListener() {}
	
	/**
	 * Login listener for client-side.
	 * @author Meranote
	 */
	public static class Client extends Listener {
		
		@Override
		public void received(final Connection connection, Object object) {
			if(object instanceof InitialResponse) {
				final InitialResponse response = ((InitialResponse)object);
				final CharacterData data = response.data;
				Gdx.app.postRunnable(new Runnable() {
					
					@Override
					public void run() {
						GameManager manager = ProjectRPG.Client.gamemanager;
						manager.getMapManager().addMap(response.pathToMap, manager.getWorld());
						manager.getEntityManager().addCharacter(connection.getID(), data, new SpriterPlayer("entity/GreyGuy/player.scml"), manager.getWorld());
						manager.setCurrentInstance(response.instance);
					}
					
				});
			}
		}
		
	}
	
	/**
	 * Login listener for server-side.
	 * @author Meranote
	 */
	public static class Server extends Listener {
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof InitialRequest) {
				CharacterData data = new CharacterData();
				data.x = (float)((Math.random() * 7) + 5);
				data.y = 8f;
				ProjectRPG.Server.instances.get("TestLevel").addCharacter(connection.getID(), data);
				InitialResponse response = new InitialResponse();
				response.data = data;
				response.instance = "TestLevel";
				response.pathToMap = "mapdata/L01.map";
				connection.sendTCP(response);
			}
		}
	}
	
}
