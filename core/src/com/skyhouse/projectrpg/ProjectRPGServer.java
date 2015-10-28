package com.skyhouse.projectrpg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.net.instance.Instance;
import com.skyhouse.projectrpg.net.listeners.CommandThread;
import com.skyhouse.projectrpg.net.listeners.DisconnectListener;
import com.skyhouse.projectrpg.net.listeners.LoginListener;
import com.skyhouse.projectrpg.net.listeners.UpdateListener;
import com.skyhouse.projectrpg.net.utils.NetworkUtils;

/**
 * Server class of ProjectRPG.
 * @author Meranote
 */
public class ProjectRPGServer extends ApplicationAdapter {
	
	private Server server;
	private boolean serverRunning;
	public static HashMap<String, Instance> instances;
	private ArrayList<String> finishInstances;
	private CommandThread commandListener;
		
	@Override
	public void create() {
		instances = new HashMap<String, Instance>();
		finishInstances = new ArrayList<String>();
		
		server = new Server();
		Kryo kryo = server.getKryo();
		NetworkUtils.registerKryoClass(kryo);
		server.start();
		serverRunning = true;
		try {
			server.bind(54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Cannot bind this port, maybe used by another application");
			Gdx.app.exit();
			return;
		}
		
		ProjectRPG.Server.net = server;
		ProjectRPG.Server.instances = instances;
		
		server.addListener(new LoginListener.Server());
		server.addListener(new DisconnectListener.Server());
		server.addListener(new UpdateListener.Server());
		
		// New Instance
		MapData data = new MapData(Gdx.files.internal("mapdata/L01.map"));
		
		instances.put(data.name, new Instance(data.name, data));
		instances.get(data.name).start();
		
		commandListener = new CommandThread();
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
		
		while(serverRunning) {
			for(Entry<String, Instance> entry : instances.entrySet()) {
				if(!entry.getValue().isAlive()) {
					finishInstances.add(entry.getKey());
				}
			}
			while(!finishInstances.isEmpty()) instances.remove(finishInstances.remove(0));
			
			if(instances.isEmpty()) {
				server.stop();
				serverRunning = false;
				try {
					server.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
