package com.skyhouse.projectrpg;

import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Logger;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.skyhouse.projectrpg.graphics.SpriterGlobal;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.map.MapLoader;
import com.skyhouse.projectrpg.net.Network;
import com.skyhouse.projectrpg.scene.GameScene;
import com.skyhouse.projectrpg.server.listeners.DisconnectListener;
import com.skyhouse.projectrpg.server.listeners.LoginListener;
import com.skyhouse.projectrpg.server.listeners.UpdateListener;
import com.skyhouse.projectrpg.server.packets.DisconnectRequest;
import com.skyhouse.projectrpg.server.packets.InitialRequest;
import com.skyhouse.projectrpg.utils.spriter.ThaiCharacter;

public class ProjectRPGGame extends ApplicationAdapter {
	
	public static Client client;
	
	SpriteBatch batch;
	AssetManager assetmanager;
	BitmapFont font;

	GameScene gamescene;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Logger.DEBUG);
		
		initialNetwork();
		initialPhysics();
		initialGraphics();
		initialAssets();
		
		gamescene = new GameScene(batch, assetmanager);
		gamescene.loadMap("L01", true);
		
		client.addListener(new LoginListener.ClientSide(gamescene));
		client.addListener(new DisconnectListener.ClientSide(gamescene));
		client.addListener(new UpdateListener.ClientSide(gamescene));
		client.sendTCP(new InitialRequest());
		
		Gdx.app.debug(ProjectRPG.TITLE, "Version = " + ProjectRPG.VERSION);
		Gdx.app.debug(ProjectRPG.TITLE, "created");
		
		GLProfiler.enable();
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
	}
	
	private void initialPhysics() {
		
	}
	
	private void initialGraphics() {
		batch = new SpriteBatch();
		
		SpriterGlobal.init(batch);
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
		
		assetmanager.finishLoading();
	}
	
	@Override
	public void resize (int width, int height) {
		gamescene.resize(width, height);
	}

	@Override
	public void render () {		
		// Update & Process
		
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Render
		gamescene.updateAndDraw(Gdx.graphics.getDeltaTime());
		
		GLProfiler.reset();
	}
	
	@Override
	public void dispose () {
		client.sendTCP(new DisconnectRequest());
		client.close();
		SpriterGlobal.dispose();
	}
}
