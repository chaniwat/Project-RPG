package com.skyhouse.projectrpg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.net.instance.Instance;
import com.skyhouse.projectrpg.net.listeners.CommandListener;
import com.skyhouse.projectrpg.net.listeners.DisconnectListener;
import com.skyhouse.projectrpg.net.listeners.LoginListener;
import com.skyhouse.projectrpg.net.listeners.UpdateListener;
import com.skyhouse.projectrpg.net.packets.CharacterDataPacket;
import com.skyhouse.projectrpg.net.utils.NetworkUtils;

public class ProjectRPGServer extends ApplicationAdapter {
	
	private Server server;
	private HashMap<String, Instance> instances;
	private CommandListener commandListener;
		
	@Override
	public void create() {
		instances = new HashMap<String, Instance>();
		
		server = new Server();
		Kryo kryo = server.getKryo();
		NetworkUtils.registerKryoClass(kryo);
		server.start();
		try {
			server.bind(54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Cannot bind this port, maybe used by another application");
			Gdx.app.exit();
			return;
		}
		ProjectRPG.Server.net = server;
		
		//server.addListener(new LoginListener.ServerSide());
		//server.addListener(new DisconnectListener.ServerSide());
		//server.addListener(new UpdateListener.ServerSide());
		
		// New Thread		
		instances.put("main", new Instance(new MapData(Gdx.files.internal("mapdata/L01.map"))));
		instances.get("main").start();
		
		commandListener = new CommandListener();
		commandListener.start();
	}

	@Override
	public void render() {		
		
	}

	@Override
	public void dispose() {
		for(Instance i : instances.values()) {
			i.finish();
		}
		server.stop();
		try {
			server.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
