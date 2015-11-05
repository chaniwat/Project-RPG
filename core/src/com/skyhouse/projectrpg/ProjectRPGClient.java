package com.skyhouse.projectrpg;

import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.manager.InputManager;
import com.skyhouse.projectrpg.manager.SceneManager;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.net.listeners.DisconnectListener;
import com.skyhouse.projectrpg.net.listeners.GameDataListener;
import com.skyhouse.projectrpg.net.listeners.LoginListener;
import com.skyhouse.projectrpg.net.listeners.UpdateListener;
import com.skyhouse.projectrpg.net.packets.DisconnectRequest;
import com.skyhouse.projectrpg.net.packets.LoginRequest;
import com.skyhouse.projectrpg.net.utils.NetworkUtils;
import com.skyhouse.projectrpg.scene.CharacterCreatorScene;
import com.skyhouse.projectrpg.scene.GameScene;
import com.skyhouse.projectrpg.scene.HomeScene;
import com.skyhouse.projectrpg.scene.LoadingScene;
import com.skyhouse.projectrpg.scene.MenuScene;
import com.skyhouse.projectrpg.scene.Scene;
import com.skyhouse.projectrpg.scene.StartScene;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;
import com.skyhouse.projectrpg.utils.asset.loader.MapLoader;
import com.skyhouse.projectrpg.utils.font.ThaiLanguage;

/**
 * Client class of ProjectRPG.
 * @author Meranote
 */
public class ProjectRPGClient extends ApplicationAdapter {
	
	private AssetManager assetmanager;
	private InputManager inputmanager;
	private SceneManager scenemanager;
	private GameManager gamemanager;
	private Client net;
	
	@Override
	public void create()  {		
		Gdx.app.setLogLevel(Logger.DEBUG);
		GLProfiler.enable();
		
		inputmanager = new InputManager();
		ProjectRPG.client.inputmanager = inputmanager;
		
		initialNetwork();
		initialGraphics();
		initialAssets();		
		startGame();
		
		Gdx.app.debug(ProjectRPG.TITLE, "Version = " + ProjectRPG.VERSION);
	}
	
	private void initialNetwork() {
		net = new Client();
		ProjectRPG.client.network.net = net;
		Kryo kryo = net.getKryo();
		NetworkUtils.registerKryoClass(kryo);
	}
	
	private void initialGraphics() {
		SpriteBatch batch = new SpriteBatch();
		ShapeRenderer renderer = new ShapeRenderer();
		
		ProjectRPG.client.graphic.batch = batch;
		ProjectRPG.client.graphic.renderer = renderer;
		
		Scene.initScene(batch, renderer);
		SpriterPlayer.init(batch, renderer);
	}
	
	private void initialAssets() {
		InternalFileHandleResolver resolver = new InternalFileHandleResolver();
		assetmanager = new AssetManager();
		ProjectRPG.client.assetmanager = assetmanager;
		assetmanager.setLoader(Map.class, new MapLoader(resolver));
		assetmanager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetmanager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter fontparams = new FreeTypeFontLoaderParameter();
		fontparams.fontFileName = "font/Roboto-Regular.ttf";
		fontparams.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ThaiLanguage.THAI_CHARS;
		assetmanager.load("font/Roboto-Regular.ttf", BitmapFont.class, fontparams);
		assetmanager.finishLoading();

		VisUI.load(SkinScale.X1);
		
		gamemanager = new GameManager();
		ProjectRPG.client.gamemanager = gamemanager;
		
		scenemanager = new SceneManager();
		ProjectRPG.client.scenemanager = scenemanager;
		
		scenemanager.addScene(StartScene.class);
		scenemanager.addScene(HomeScene.class);
		scenemanager.addScene(CharacterCreatorScene.class);
		scenemanager.addScene(LoadingScene.class);
		scenemanager.addScene(GameScene.class);
		scenemanager.addScene(MenuScene.class);
		
		scenemanager.setUseScene(StartScene.class);
	}
	
	private void startGame() {
		net.start();
		try {
			net.connect(5000, "server.projectrpg.dev", 54555, 54556);
		} catch (IOException e) {
			Gdx.app.error(ProjectRPG.TITLE, "Can't connect to server");
			Gdx.app.exit();
		}
		
		net.addListener(new LoginListener.Client());
		net.addListener(new GameDataListener.Client());
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
		
		// Update
		float deltaTime = Gdx.graphics.getDeltaTime();
		assetmanager.update();
		scenemanager.update(deltaTime);
		gamemanager.update(deltaTime);
		inputmanager.update(deltaTime);
		
		// Update Fixed time
		ProjectRPG.updateFixedTime(deltaTime);
		for(int i = 0; i < ProjectRPG.getUpdateFixedCount(); i++) {
			float step = ProjectRPG.getStep();
			scenemanager.updateFixed(step);
			gamemanager.updateFixed(step);
		}
		
		// Render
		scenemanager.draw(deltaTime);
		
		GLProfiler.reset();
	}
	
	@Override
	public void dispose () {		
		net.close();
		net.stop();
		
		scenemanager.dispose();
		VisUI.dispose();
		
		try {
			net.dispose();
		} catch (IOException e) {
			throw new GdxRuntimeException(e);
		}
	}
}
