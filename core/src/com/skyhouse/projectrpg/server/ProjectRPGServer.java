package com.skyhouse.projectrpg.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.entities.data.StructureData;
import com.skyhouse.projectrpg.physics.CharacterBody;
import com.skyhouse.projectrpg.physics.PhysicGlobal;
import com.skyhouse.projectrpg.physics.StructureBody;
import com.skyhouse.projectrpg.server.listeners.CommandListener;
import com.skyhouse.projectrpg.server.listeners.DisconnectListener;
import com.skyhouse.projectrpg.server.listeners.LoginListener;
import com.skyhouse.projectrpg.server.listeners.UpdateListener;
import com.skyhouse.projectrpg.server.packets.CharacterDataPacket;
import com.skyhouse.projectrpg.server.packets.DisconnectRequest;
import com.skyhouse.projectrpg.server.packets.DisconnectResponse;
import com.skyhouse.projectrpg.server.packets.InitialRequest;
import com.skyhouse.projectrpg.server.packets.InitialResponse;

public class ProjectRPGServer extends ApplicationAdapter {
	
	public static Server server;
	public static HashMap<Integer, CharacterBody> characters;
	public static HashMap<String, StructureBody> structures;
	
	float ctime = 0f;
	
	@Override
	public void create() {		
		server = new Server();
		Kryo kryo = server.getKryo();
		ProjectRPGServer.registerClass(kryo);
		server.start();
		try {
			server.bind(54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Cannot bind this port, maybe used by another application");
			Gdx.app.exit();
		}
		
		characters = new HashMap<Integer, CharacterBody>();
		structures = new HashMap<String, StructureBody>();
		
		PhysicGlobal.init(0f, -10f,  true, false);
		structures.put("ground", new StructureBody(new StructureData(-20f, 0, 60f, 5f), BodyType.StaticBody));
		structures.put("box1", new StructureBody(new StructureData(1, 1, 1, 1),  BodyType.StaticBody));
		structures.put("box2", new StructureBody(new StructureData(2, 2, 1, 2),  BodyType.StaticBody));
		structures.put("box3", new StructureBody(new StructureData(3, 3, 3, 3),  BodyType.StaticBody));
		structures.put("box4", new StructureBody(new StructureData(6, 2, 1, 1),  BodyType.StaticBody));
		structures.put("box5", new StructureBody(new StructureData(7, 1, 1, 1),  BodyType.StaticBody));
		
		server.addListener(new LoginListener.ServerSide());
		server.addListener(new DisconnectListener.ServerSide());
		server.addListener(new UpdateListener.ServerSide());
		new Thread(new CommandListener()).start();
	}

	@Override
	public void render() {		
		// Simulate Physics
		PhysicGlobal.getWorld().step(1/60f, 8, 3);
		
		for(CharacterBody character : characters.values()) {
			character.update();
		}
		
		CharacterDataPacket update = new CharacterDataPacket();
		update.characters = new HashMap<Integer, CharacterData>();
		for(Entry<Integer, CharacterBody> character : ProjectRPGServer.characters.entrySet()) {
			update.characters.put(character.getKey(), character.getValue().getData());
		}
		server.sendToAllUDP(update);
		
		// log
	}

	@Override
	public void dispose() {
		
	}
	
	/**
	 * Registering the class before start the client or server
	 * @param kryo {@link Kryo} object
	 */
	public static void registerClass(Kryo kryo) {
		kryo.register(HashMap.class);
		kryo.register(CharacterData.class);
		kryo.register(CharacterData.CharacterState.class);
		kryo.register(InitialRequest.class);
		kryo.register(InitialResponse.class);
		kryo.register(DisconnectRequest.class);
		kryo.register(DisconnectResponse.class);
		kryo.register(CharacterDataPacket.class);
	}

}
