package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.input.listener.GameplayControllerListener;
import com.skyhouse.projectrpg.input.listener.GameplayInputListener;
import com.skyhouse.projectrpg.manager.GameManager;
import com.skyhouse.projectrpg.map.Map;

/**
 * Game scene. <br>
 * <b>Gameplay happen here.</b>
 * @author Meranote
 */
public class GameScene extends Scene {
	
	private GameManager manager = ProjectRPG.Client.gamemanager;
	private Sprite background = new Sprite();
	private BitmapFont font;
	
	private Box2DDebugRenderer b2ddebug = new Box2DDebugRenderer();
	
	/**
	 * Construct a new game scene.
	 */
	public GameScene() {
		font = ProjectRPG.Client.assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class);
		font.getData().markupEnabled = true;
		font.setColor(Color.BLACK);
		addViewport(new GameplayViewport(16f));
		addViewport(new ScreenViewport());
	}
	
	@Override
	public void change() {
		ProjectRPG.Client.inputmanager.setInputProcessor(GameplayInputListener.class);
		ProjectRPG.Client.inputmanager.setControllerProcessor(GameplayControllerListener.class);
	}

	@Override
	public void update(float deltatime) {
		updateBackground();
		
		if(manager.getPlayerCharacter() != null) {
			getViewport(GameplayViewport.class).setViewCenterToCharacter(manager.getPlayerCharacter(), 0, 1.5f);
			background.setPosition(
					-(background.getWidth() / 2f) + (manager.getPlayerCharacter().getData().x * 0.35f), 
					-2f + (manager.getPlayerCharacter().getData().y * 0.35f));
		}
	}
	
	/**
	 * Update background to current background map.
	 */
	private void updateBackground() {
		if((background.getTexture() == null || !background.getTexture().equals(manager.getCurrentMap().getBackground())) && manager.getCurrentMap() != null) {
			float baseHeight = 17f;
			Texture t = manager.getCurrentMap().getBackground();
			background.setRegion(t);
			background.setSize(baseHeight * (t.getWidth()/t.getHeight()), baseHeight);
			background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);			
		}
	}
	
	@Override
	public void draw(float deltatime) {
		if(manager.getEntityManager().getAllCharacter().isEmpty() || manager.getMapManager().getAllMap().isEmpty()) return;
		drawEntities();
		drawGUI();
		b2ddebug.render(manager.getWorld(), getViewport(GameplayViewport.class).getCamera().combined);
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
			for(Map m : manager.getMapManager().getAllMap().values()) {
				m.draw(batch);
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
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, getViewport(ScreenViewport.class).getScreenHeight() - 20);
			font.draw(batch, "Lantacy : "+ProjectRPG.Client.network.net.getReturnTripTime()+" ms", 20, getViewport(ScreenViewport.class).getScreenHeight() - 40);
		batch.end();
	}
	
	@Override
	public void dispose() {
		manager.dispose();
	}
	
}
