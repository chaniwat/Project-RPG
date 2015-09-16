package com.skyhouse.projectrpg.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;
import com.skyhouse.projectrpg.objects.CharacterData;

public class ProjectRPGServer extends ApplicationAdapter {
	
	Server server;
	ArrayList<CharacterData> charactersData;
	
	@Override
	public void create() {
		
		charactersData = new ArrayList<CharacterData>();
		server = new Server();
		
		Kryo kryo = server.getKryo();
		kryo.register(ArrayList.class);
		kryo.register(InitialRequest.class);
	    kryo.register(InitialResponse.class);
	    kryo.register(CharacterData.class);
		server.start();
		try {
			server.bind(54555, 54777);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		server.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				if(object instanceof InitialRequest) {
					InitialRequest request = (InitialRequest)object;
					int newid = connection.getID();
					charactersData.add(new CharacterData(newid, request.characterposx, request.characterposy, request.characterstate));
					
					InitialResponse response = new InitialResponse();
					response.clientid = newid;
					for(CharacterData data : charactersData) {
						response.charactersData.add(data);						
					}
					
					connection.sendTCP(response);
				}
			}
			
			@Override
			public void disconnected(Connection connection) {
				charactersData.remove(connection.getID() - 1);
			}
		});
		
		new Thread(new Runnable() {
			
			Scanner input;
			String command;
			
			@Override
			public void run() {
				input = new Scanner(System.in);
				while(true) {
					command = input.nextLine();	
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							receiveConsole(command);
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void render() {
		
	}
	
	private void receiveConsole(String command) {
		if(command.equals("test")) {
			Gdx.app.log(ProjectRPG.TITLE, "Checked!");
		}
	}

	@Override
	public void dispose() {
		
	}

}
