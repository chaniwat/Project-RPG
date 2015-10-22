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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Logger;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.skyhouse.projectrpg.graphics.SpriterGlobal;
import com.skyhouse.projectrpg.graphics.font.ThaiCharacter;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.map.MapLoader;
import com.skyhouse.projectrpg.net.Network;
import com.skyhouse.projectrpg.net.listeners.DisconnectListener;
import com.skyhouse.projectrpg.net.listeners.LoginListener;
import com.skyhouse.projectrpg.net.listeners.UpdateListener;
import com.skyhouse.projectrpg.net.packets.DisconnectRequest;
import com.skyhouse.projectrpg.net.packets.InitialRequest;
import com.skyhouse.projectrpg.scene.GameScene;
import com.skyhouse.projectrpg.scene.SceneManager;

/**
 * Client class of ProjectRPG.
 * @author Meranote
 */
public class ProjectRPGClient extends ApplicationAdapter {
	
	private Client net;
	private AssetManager assetmanager;
	private SpriteBatch batch;
	private SceneManager scenemanager;
	
	@Override
	public void create()  {		
		Gdx.app.setLogLevel(Logger.DEBUG);
		GLProfiler.enable();
		
		initialNetwork();
		initialGraphics();
		initialAssets();		
		startNetwork();
		
		Gdx.app.debug(ProjectRPG.TITLE, "Version = " + ProjectRPG.VERSION);
		Gdx.app.debug(ProjectRPG.TITLE, "created");
	}
	
	private void initialNetwork() {
		net = new Client();
		Kryo kryo = net.getKryo();
		Network.registerKryoClass(kryo);
		net.start();
		try {
			net.connect(5000, "server.projectrpg.dev", 54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Can't connect to server");
			Gdx.app.exit();
		}
		ProjectRPG.Client.net = net;
	}
	
	private void startNetwork() {
		GameScene gamescene = scenemanager.getCurrentScene(GameScene.class); 
		net.addListener(new LoginListener.ClientSide(gamescene));
		net.addListener(new DisconnectListener.ClientSide(gamescene));
		net.addListener(new UpdateListener.ClientSide(gamescene));
		net.sendTCP(new InitialRequest());
	}
	
	private void initialGraphics() {
		batch = new SpriteBatch();
		SpriterGlobal.init(batch, new ShapeRenderer());
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
		ProjectRPG.Client.assetmanager = assetmanager;
		scenemanager = new SceneManager();
		scenemanager.addScene("gamescene", new GameScene(batch));
		scenemanager.getScene("gamescene", GameScene.class).loadMap("L01", true);
		scenemanager.setUseScene("gamescene");
		ProjectRPG.Client.scenemanager = scenemanager;
	}
	
	@Override
	public void resize (int width, int height) {
		scenemanager.resize(width, height);
	}

	@Override
	public void render () {		
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Render
		scenemanager.updateAndDraw(Gdx.graphics.getDeltaTime());
		
		GLProfiler.reset();
	}
	
	@Override
	public void dispose () {
		net.close();
		SpriterGlobal.dispose();
	}
}
