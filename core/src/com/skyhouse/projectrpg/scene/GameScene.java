package com.skyhouse.projectrpg.scene;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.entity.NPC;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.input.listener.GameplayControllerListener;
import com.skyhouse.projectrpg.input.listener.GameplayInputListener;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.map.MapBodyBuilder;
import com.skyhouse.projectrpg.scene.MenuScene.TabSet;
import com.skyhouse.projectrpg.utils.tiled.TextureMapObjectRenderer;

/**
 * Game scene. <br>
 * <b>Gameplay happen here.</b>
 * @author Meranote
 */
public class GameScene extends Scene {
	
	private static final float FADETIME = 0.2f;
	private float accumulatorFadeTime;
	private boolean fadeInFlag, fadeOutFlag;
	
	private float screenwidth, screenheight;
	
	private GameManager manager = ProjectRPG.client.gamemanager;
	private Sprite background = new Sprite();
	private BitmapFont font;
	private BitmapFont fontbold;
	
	private Box2DDebugRenderer b2ddebug = new Box2DDebugRenderer();
	
	TiledMap map;
	OrthogonalTiledMapRenderer maprenderer;
	TextureMapObjectRenderer objectrenderer;
	RayHandler rayHandler;
	PointLight mylight;
	
	/**
	 * Construct a new game scene.
	 */
	public GameScene() {
		font = ProjectRPG.client.graphic.font.uifont;
		fontbold = ProjectRPG.client.graphic.font.uifontbold;
		addViewport(new GameplayViewport(16f));
		addViewport(new ScreenViewport());
		
		map = new TmxMapLoader().load("data/tmx/firstlevel.tmx");
		maprenderer = new OrthogonalTiledMapRenderer(map, 1f / 70f);
		objectrenderer = new TextureMapObjectRenderer(map, 1f / 70f);
		
		MapBodyBuilder.buildShapes(map, 70f, manager.getGameWorld());
		
		rayHandler = new RayHandler(manager.getGameWorld());
		mylight = new PointLight(rayHandler, 32, new Color(1f, 1f, 1f, 1f), 6, 9f, 5.5f);
		mylight.setSoftnessLength(3f);
		RayHandler.useDiffuseLight(true);
	}
	
	@Override
	public void change() {
		ProjectRPG.client.inputmanager.setInputProcessor(GameplayInputListener.class);
		ProjectRPG.client.inputmanager.setControllerProcessor(GameplayControllerListener.class);
		
		accumulatorFadeTime = FADETIME;
		fadeOutFlag = true;
		fadeInFlag = false;
	}

	@Override
	public void update(float deltatime) {
		screenwidth = getViewport(ScreenViewport.class).getWorldWidth();
		screenheight = getViewport(ScreenViewport.class).getWorldHeight();
		
		rayHandler.setAmbientLight(0.05f, 0.05f, 0.05f, 1.0f);
		
		getViewport(GameplayViewport.class).setViewSize(13.5f);
		
		if(manager.isGameReady()) {
			if(manager.getPlayerCharacter() != null) {
				getViewport(GameplayViewport.class).setViewCenterToCharacter(manager.getPlayerCharacter(), 0, 1.5f, manager.getCurrentMap().getData().mapWidth);
			}
			
			updateBackground();
		}
		
		if(fadeOutFlag) {
			if(accumulatorFadeTime > 0f) {
				accumulatorFadeTime -= deltatime;
			} else {
				accumulatorFadeTime = 0f;
				fadeOutFlag = false;
			}
		} else if(fadeInFlag) {
			if(accumulatorFadeTime < FADETIME) {
				accumulatorFadeTime += deltatime;
			} else {
				accumulatorFadeTime = FADETIME;
				ProjectRPG.client.scenemanager.getScene(MenuScene.class).setTabSet(TabSet.MENU);
				ProjectRPG.client.scenemanager.setUseScene(MenuScene.class);
			}
		}
		
		rayHandler.setCombinedMatrix((OrthographicCamera) getViewport(GameplayViewport.class).getCamera());
	}
	
	/**
	 * Update background to current background map.
	 */
	private void updateBackground() {
		if((background.getTexture() == null || !background.getTexture().equals(manager.getCurrentMap().getBackground())) && manager.getMapManager().isMapReady()) {
			float baseHeight = getViewport(GameplayViewport.class).getWorldHeight();
			Texture t = manager.getCurrentMap().getBackground();
			background.setRegion(t);
			background.setSize(baseHeight * (t.getWidth()/t.getHeight()), baseHeight);
			background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);	
		}
		
		background.setPosition(0, 0);
	}
	
	@Override
	public void draw(float deltatime) {
		if(manager.isGameReady()) {
			drawEntities();
			rayHandler.updateAndRender();
			drawGUI();
			
			b2ddebug.render(manager.getGameWorld(), getViewport(GameplayViewport.class).getCamera().combined);
		}
		
		ProjectRPG.client.graphic.enableGLAlphaBlend();
		renderer.begin(ShapeType.Filled);
			// Overlay
			if((accumulatorFadeTime / FADETIME) > 0f) {
				renderer.setColor(0, 0, 0, accumulatorFadeTime / FADETIME);
				renderer.rect(0, 0, screenwidth, screenheight);
			}
		renderer.end();
		ProjectRPG.client.graphic.disableGLAlphaBlend();
	}
	
	/**
	 * Draw all entities : Characters, monsters, structures, etc.
	 */
	private void drawEntities() {
		/**
		 * Draw order (back -> front).
		 * 1. Parallax background
		 * 2. Map (structures)
		 * 3. Character (except player character)
		 * 4.1. NPC
		 * 4.2. Monster
		 * 5. player character
		 * 6. effects
		 */
		useViewport(GameplayViewport.class);
		
		batch.begin();
			background.draw(batch);
//			maprenderer.setView((OrthographicCamera) getViewport(GameplayViewport.class).getCamera());
//			maprenderer.render();
//			objectrenderer.setView((OrthographicCamera) getViewport(GameplayViewport.class).getCamera());
//			objectrenderer.render();
			manager.getCurrentMap().draw(batch);
			for(NPC npc : manager.getEntityManager().getNpcs()) {
				npc.draw(batch);
			}
			for(Character c : manager.getEntityManager().getAllCharacter().values()) {
				if(c.equals(manager.getPlayerCharacter())) continue;
				c.draw(batch);
			}
			manager.getPlayerCharacter().draw(batch);
		batch.end();
	}
	
	/**
	 * Draw the graphic user interface.
	 */
	private void drawGUI() {
		useViewport(ScreenViewport.class);
		
		batch.begin();
			font.draw(batch, "[BLACK]FPS : "+Gdx.graphics.getFramesPerSecond(), 20, getViewport(ScreenViewport.class).getScreenHeight() - 20);
			font.draw(batch, "[BLACK]Lantacy : "+ProjectRPG.client.network.net.getReturnTripTime()+" ms", 20, getViewport(ScreenViewport.class).getScreenHeight() - 40);
		batch.end();
	}
	
	@Override
	public void dispose() {
		rayHandler.dispose();
		manager.dispose();
	}
	
	public void changeToMenuScene() {
		fadeInFlag = true;
	}
	
}
