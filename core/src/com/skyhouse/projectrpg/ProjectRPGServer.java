package com.skyhouse.projectrpg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.net.Network;
import com.skyhouse.projectrpg.net.listeners.CommandListener;
import com.skyhouse.projectrpg.net.listeners.DisconnectListener;
import com.skyhouse.projectrpg.net.listeners.LoginListener;
import com.skyhouse.projectrpg.net.listeners.UpdateListener;
import com.skyhouse.projectrpg.net.packets.CharacterDataPacket;
import com.skyhouse.projectrpg.physics.CharacterBody;
import com.skyhouse.projectrpg.physics.PhysicGlobal;
import com.skyhouse.projectrpg.physics.StructureBody;

public class ProjectRPGServer extends ApplicationAdapter {
	
	public static Server server;
	public static HashMap<Integer, CharacterBody> characters;
	public static HashMap<String, StructureBody> structures;
	
	public static Map map;
	
	float ctime = 0f;
	
	Scanner input;
	String command;
	
	@Override
	public void create() {	
		server = new Server();
		Kryo kryo = server.getKryo();
		Network.registerKryoClass(kryo);
		server.start();
		try {
			server.bind(54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Cannot bind this port, maybe used by another application");
			Gdx.app.exit();
			return;
		}
		
		characters = new HashMap<Integer, CharacterBody>();
		structures = new HashMap<String, StructureBody>();
		
		PhysicGlobal.init(0f, -10f,  true, false);
		try {
			map = new Map(Gdx.files.internal("mapdata/L01.map"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		server.addListener(new LoginListener.ServerSide());
		server.addListener(new DisconnectListener.ServerSide());
		server.addListener(new UpdateListener.ServerSide());
		
		// New Thread
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
}
