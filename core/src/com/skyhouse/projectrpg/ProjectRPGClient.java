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
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.net.listeners.DisconnectListener;
import com.skyhouse.projectrpg.net.listeners.LoginListener;
import com.skyhouse.projectrpg.net.listeners.UpdateListener;
import com.skyhouse.projectrpg.net.packets.InitialRequest;
import com.skyhouse.projectrpg.net.utils.NetworkUtils;
import com.skyhouse.projectrpg.scene.GameScene;
import com.skyhouse.projectrpg.scene.HomeScene;
import com.skyhouse.projectrpg.scene.LoadingScene;
import com.skyhouse.projectrpg.scene.MenuScene;
import com.skyhouse.projectrpg.scene.SceneManager;
import com.skyhouse.projectrpg.scene.StartScene;
import com.skyhouse.projectrpg.utils.assetloader.MapLoader;
import com.skyhouse.projectrpg.utils.font.ThaiCharacter;

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
	
	@SuppressWarnings("unused")
	private void initialNetwork() {
		net = new Client();
		Kryo kryo = net.getKryo();
		NetworkUtils.registerKryoClass(kryo);
		net.start();
		try {
			net.connect(5000, "server.projectrpg.dev", 54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Can't connect to server");
			Gdx.app.exit();
		}
		ProjectRPG.Client.net = net;
	}
	
	private void initialGraphics() {
		batch = new SpriteBatch();
	}
	
	private void initialAssets() {
		InternalFileHandleResolver resolver = new InternalFileHandleResolver();
		assetmanager = new AssetManager();
		assetmanager.setLoader(Map.class, new MapLoader(resolver));
		assetmanager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetmanager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		ProjectRPG.Client.assetmanager = assetmanager;
		
		FreeTypeFontLoaderParameter fontparams = new FreeTypeFontLoaderParameter();
		fontparams.fontFileName = "font/Roboto-Regular.ttf";
		fontparams.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiCharacter.THAI_CHARS;
		assetmanager.load("font/Roboto-Regular.ttf", BitmapFont.class, fontparams);
		assetmanager.finishLoading();
		
		scenemanager = new SceneManager();
		ProjectRPG.Client.scenemanager = scenemanager;
		
		scenemanager.addScene("startscene", new StartScene(batch));
		scenemanager.addScene("homescene", new HomeScene(batch));
		scenemanager.addScene("loadingscene", new LoadingScene(batch));
		scenemanager.addScene("gamescene", new GameScene(batch));
		scenemanager.addScene("menuscene", new MenuScene(batch));
		
		scenemanager.setUseScene("gamescene");
	}
	
	@SuppressWarnings("unused")
	private void startNetwork() {
		net.addListener(new LoginListener.ClientSide());
		//net.addListener(new DisconnectListener.ClientSide());
		//net.addListener(new UpdateListener.ClientSide());
		net.sendTCP(new InitialRequest());
	}
	
	@Override
	public void resize (int width, int height) {
		scenemanager.resize(width, height);
	}

	@Override
	public void render () {
		assetmanager.update();
		
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Render
		scenemanager.updateAndDraw(Gdx.graphics.getDeltaTime());
		
		GLProfiler.reset();
	}
	
	@Override
	public void dispose () {
		//net.close();
	}
}
