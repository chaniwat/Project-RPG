package com.skyhouse.projectrpg;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Logger;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.Structure;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.graphics.SpriterGlobal;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.input.GameplayControllerProcess;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.map.MapLoader;
import com.skyhouse.projectrpg.net.Network;
import com.skyhouse.projectrpg.objects.BackgroundGlobal;
import com.skyhouse.projectrpg.server.listeners.DisconnectListener;
import com.skyhouse.projectrpg.server.listeners.LoginListener;
import com.skyhouse.projectrpg.server.listeners.UpdateListener;
import com.skyhouse.projectrpg.server.packets.DisconnectRequest;
import com.skyhouse.projectrpg.server.packets.InitialRequest;
import com.skyhouse.projectrpg.utils.spriter.ThaiCharacter;

public class ProjectRPGGame extends ApplicationAdapter {
	
	Client client;
	
	SpriteBatch batch;
	AssetManager assetmanager;
	BitmapFont font;
	
	public static GameplayViewport gameViewport;
	public static UIViewport uiViewport;
	public static Map map;
	public static HashMap<Integer, Character> characters;
 	public static Character maincharacter;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Logger.DEBUG);
		
		characters = new HashMap<Integer, Character>();
		
		initialNetwork();
		initialGraphics();
		initialAssets();
		
		Gdx.app.debug(ProjectRPG.TITLE, "Version = " + ProjectRPG.VERSION);
		Gdx.app.debug(ProjectRPG.TITLE, "created");
		
		GLProfiler.enable();
		
		for(Controller controller : Controllers.getControllers()) {
			Gdx.app.log(ProjectRPG.TITLE, controller.getName());
		}
	}
	
	private void initialNetwork() {
		client = new Client();
		Kryo kryo = client.getKryo();
		Network.registerKryoClass(kryo);
		client.start();
		try {
			client.connect(5000, "server.projectrpg.dev", 54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Can't connect to server");
			Gdx.app.exit();
		}
		
		client.addListener(new LoginListener.ClientSide());
		client.addListener(new DisconnectListener.ClientSide());
		client.addListener(new UpdateListener.ClientSide());
		client.sendTCP(new InitialRequest());
	}
	
	private void initialGraphics() {
		batch = new SpriteBatch();
		
		SpriterGlobal.init(batch);
		BackgroundGlobal.init(batch);
		
		gameViewport = new GameplayViewport(12f);
		uiViewport = new UIViewport();
	}
	
	private void initialAssets() {
		InternalFileHandleResolver resolver = new InternalFileHandleResolver();
		assetmanager = new AssetManager();
		assetmanager.setLoader(Map.class, new MapLoader(resolver));
		assetmanager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetmanager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter fontparams = new FreeTypeFontLoaderParameter();
		fontparams.fontFileName = "fonts/Roboto-Regular.ttf";
		fontparams.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiCharacter.THAI_CHARS;
		assetmanager.load("fonts/Roboto-Regular.ttf", BitmapFont.class, fontparams);
		
		assetmanager.load("mapdata/L01.map", Map.class, new MapLoader.MapParameter(assetmanager));
		assetmanager.finishLoading();		
		
		map = assetmanager.get("mapdata/L01.map", Map.class);
		
		font = assetmanager.get("fonts/Roboto-Regular.ttf", BitmapFont.class);
		font.setColor(new Color(0, 0, 0, 1));
	}
	
	@Override
	public void resize (int width, int height) {
		gameViewport.update(width, height);
		uiViewport.update(width, height);
		
		Gdx.app.debug(ProjectRPG.TITLE, "screen width: "+width);
		Gdx.app.debug(ProjectRPG.TITLE, "screen height: "+height);
		Gdx.app.debug(ProjectRPG.TITLE, "width viewport: "+gameViewport.getWorldWidth());
		Gdx.app.debug(ProjectRPG.TITLE, "Height viewport: "+gameViewport.getWorldHeight());
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
		SpriterGlobal.updateAndDrawExcept(maincharacter.getSpriterPlayer());
		maincharacter.getSpriterPlayer().update();
		SpriterGlobal.draw(maincharacter.getSpriterPlayer());
		
		// Structure (eg. floor, platforms) - Layer 3
		batch.begin();
			map.draw(batch);
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
			font.draw(batch, String.format("Player state: %s", maincharacter.actionstate.name()), 20, uiViewport.getWorldHeight() - 100);
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
