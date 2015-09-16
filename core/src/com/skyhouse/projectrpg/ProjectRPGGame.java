package com.skyhouse.projectrpg;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.Structure;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.entities.data.StructureData;
import com.skyhouse.projectrpg.graphics.TileTexture;
import com.skyhouse.projectrpg.graphics.TileTexture.TileTexturePosition;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.objects.BackgroundGlobal;
import com.skyhouse.projectrpg.server.DisconnectRequest;
import com.skyhouse.projectrpg.server.InitialRequest;
import com.skyhouse.projectrpg.server.ProjectRPGServer;
import com.skyhouse.projectrpg.server.utils.DisconnectListener;
import com.skyhouse.projectrpg.server.utils.LoginListener;
import com.skyhouse.projectrpg.server.utils.UpdateListener;
import com.skyhouse.projectrpg.utils.spriter.SpriterGlobal;

public class ProjectRPGGame extends ApplicationAdapter {
	
	public static class ProjectRPG {
		public static final String TITLE = "Project RPG";
		public static final String VERSION = "0.0(WIP)"; 
	}
	
	public static  Client client;
	
	SpriteBatch batch;
	GameplayViewport gameViewport;
	UIViewport uiViewport;
	GameplayInputProcess gameplayinput;
	
	BitmapFont font;
	
	public static HashMap<Integer, Character> characters;
 	public static Character maincharacter;
 	public static CharacterData maincharacterdata;
 	public static HashMap<String, Structure> structures;
	
	@Override
	public void create () {		
		client = new Client();
		Kryo kryo = client.getKryo();
		ProjectRPGServer.registerClass(kryo);
		client.start();
		try {
			client.connect(5000, "127.0.0.1", 54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Can't connect to server");
			Gdx.app.exit();
		}
		
		batch = new SpriteBatch();
		
		SpriterGlobal.init(batch);
		BackgroundGlobal.init(batch);
		
		gameViewport = new GameplayViewport(12f);
		uiViewport = new UIViewport();
		
		BackgroundGlobal.setBackground(new Texture(Gdx.files.internal("background.png")));
		BackgroundGlobal.setSizeByHeight(17);
		
		characters = new HashMap<Integer, Character>();
		structures = new HashMap<String, Structure>();
		
		// For Box2D test
	    TextureAtlas texture = new TextureAtlas(Gdx.files.internal("structures/greenland/tiles_spritesheet.pack"));
		
		structures.put("ground", new Structure(new StructureData(-20f, 0, 60f, 5f), new TileTexture(texture.findRegion("grassMid"))));
		structures.get("ground").getTileTexture().setRegionPosition(texture.findRegion("grassCenter"), TileTexturePosition.MiddleHorizontal, TileTexturePosition.Bottom);
		structures.put("box1", new Structure(new StructureData(1, 1, 1, 1),  new TileTexture(texture.findRegion("boxAlt"))));
		structures.put("box2", new Structure(new StructureData(2, 2, 1, 2),  new TileTexture(texture.findRegion("boxAlt"))));
		structures.put("box3", new Structure(new StructureData(3, 3, 3, 3),  new TileTexture(texture.findRegion("boxAlt"))));
		structures.put("box4", new Structure(new StructureData(6, 2, 1, 2),  new TileTexture(texture.findRegion("boxAlt"))));
		structures.put("box5", new Structure(new StructureData(7, 1, 1, 1),  new TileTexture(texture.findRegion("boxAlt"))));
		
		font = new BitmapFont();
		font.setColor(new Color(0, 0, 0, 1));
		
		client.addListener(new LoginListener.ClientSide());
		client.addListener(new DisconnectListener.ClientSide());
		client.addListener(new UpdateListener.ClientSide());
		client.sendTCP(new InitialRequest());
		
		Gdx.app.log(ProjectRPG.TITLE, "Version = " + ProjectRPG.VERSION);
		Gdx.app.log(ProjectRPG.TITLE, "created");
		
		GLProfiler.enable();
	}
	
	@Override
	public void resize (int width, int height) {
		gameViewport.update(width, height);
		uiViewport.update(width, height);
		
		Gdx.app.log(ProjectRPG.TITLE, "screen width: "+width);
		Gdx.app.log(ProjectRPG.TITLE, "screen height: "+height);
		Gdx.app.log(ProjectRPG.TITLE, "width viewport: "+gameViewport.getWorldWidth());
		Gdx.app.log(ProjectRPG.TITLE, "Height viewport: "+gameViewport.getWorldHeight());
	}

	@Override
	public void render () {
		if(maincharacter == null) return;
		// Input
		CharacterData maindata = new CharacterData(maincharacter);
		client.sendUDP(maindata);
		
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Update & Process
		gameViewport.setViewCenterToCharacter(maincharacter, 0, 2.4f);
		maincharacter.update(Gdx.graphics.getDeltaTime());
		
		BackgroundGlobal.setPosition(-(BackgroundGlobal.getWidth() / 2f) + (maincharacter.getPositionX() * 0.35f), -2f + (maincharacter.getPositionY() * 0.35f));
		
		// Render -  Gameplay
		gameViewport.apply();
		batch.setProjectionMatrix(gameViewport.getCamera().combined);
		
		// Background - Layer 0
		BackgroundGlobal.draw();
		
		// Structure (eg. Ladder, background object) - Layer 1
		batch.begin();
			
		batch.end();
			
		// Entities (eg. Character, Monster) - Layer 2
		for(Character character : characters.values()) {
			character.update(Gdx.graphics.getDeltaTime());
		}
		SpriterGlobal.updateAndDraw();
		
		// Structure (eg. floor, platforms) - Layer 3
		batch.begin();
			for(Structure structure : structures.values()) {
				structure.render(batch);
			}
		batch.end();
	
		// Render - UI
		uiViewport.apply();
		batch.setProjectionMatrix(uiViewport.getCamera().combined);
		
		// Text - Layer 1
		batch.begin();
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, uiViewport.getWorldHeight() - 20);
			font.draw(batch, String.format("Frametime : %.0f ms", Gdx.graphics.getDeltaTime()*1000), 20, uiViewport.getWorldHeight() - 40);
			font.draw(batch, String.format("Draw calls : %d | Bound calls : %d | Shader switch : %d", GLProfiler.drawCalls, GLProfiler.textureBindings, GLProfiler.shaderSwitches), 20, uiViewport.getWorldHeight() - 60);
			font.draw(batch, String.format("Player position: (%.2f, %.2f)", maincharacter.getPositionX(), maincharacter.getPositionY()), 20, uiViewport.getWorldHeight() - 80);
			font.draw(batch, String.format("Player state: %s", maincharacter.getState().name()), 20, uiViewport.getWorldHeight() - 100);
		batch.end();
		
		// Log
		
		GLProfiler.reset();
	}
	
	@Override
	public void dispose () {
		client.sendTCP(new DisconnectRequest());
		client.close();
		SpriterGlobal.dispose();
	}
}
