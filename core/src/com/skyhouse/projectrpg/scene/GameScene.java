package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
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

/**
 * Game scene. <br>
 * <b>Gameplay happen here.</b>
 * @author Meranote
 */
public class GameScene extends Scene {
	
	private GameManager manager = ProjectRPG.client.gamemanager;
	private Sprite background = new Sprite();
	private BitmapFont font;
	
	private Box2DDebugRenderer b2ddebug = new Box2DDebugRenderer();
	
	/**
	 * Construct a new game scene.
	 */
	public GameScene() {
		font = ProjectRPG.client.graphic.font.regular;
		addViewport(new GameplayViewport(16f));
		addViewport(new ScreenViewport());
	}
	
	@Override
	public void change() {
		ProjectRPG.client.inputmanager.setInputProcessor(GameplayInputListener.class);
		ProjectRPG.client.inputmanager.setControllerProcessor(GameplayControllerListener.class);
	}

	@Override
	public void update(float deltatime) {
		if(manager.isGameReady()) {
			if(manager.getPlayerCharacter() != null) {
				getViewport(GameplayViewport.class).setViewCenterToCharacter(manager.getPlayerCharacter(), 0, 1.5f);
			}
			
			updateBackground();
		}
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
			drawGUI();
			
			b2ddebug.render(manager.getGameWorld(), getViewport(GameplayViewport.class).getCamera().combined);
		}
		
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
			manager.getCurrentMap().draw(batch);
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
		manager.dispose();
	}
	
}
