package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.game.GameManager;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class GameScene extends Scene {
	
	private GameManager manager;
	private BitmapFont font;
	
	private Box2DDebugRenderer b2ddebug = new Box2DDebugRenderer();
	
	public GameScene() {
		font = assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class);
		font.getData().markupEnabled = true;
		font.setColor(Color.BLACK);
		manager = new GameManager(assetmanager, input);
		addViewport("Gameplay", new GameplayViewport(16f));
		addViewport("UI", new UIViewport());
		manager.setGameplayViewport((GameplayViewport)getViewport("Gameplay"));
		manager.addMap("mapdata/L01.map");
	}

	@Override
	public void update(float deltatime) {
		manager.update(deltatime);
	}
	
	@Override
	public void draw(float deltatime) {
		if(manager.getAllCharacter().isEmpty() || manager.getAllMap().isEmpty()) return;
		drawEntities();
		drawUI();
		b2ddebug.render(manager.getB2DWorld(), getViewport("Gameplay").getCamera().combined);
	}
	
	private void drawEntities() {
		useViewport("Gameplay");
		batch.begin();
			manager.getBackground().draw(batch);
			for(Map m : manager.getAllMap()) {
				m.draw(batch);
			}
			for(Character c : manager.getAllCharacter()) {
				if(c.equals(manager.getControlCharacter())) continue;
				c.draw();
			}
			manager.getControlCharacter().draw();
		batch.end();
	}
	
	private void drawUI() {
		useViewport("UI");
		
		batch.begin();
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, getViewport("UI").getScreenHeight() - 20);
			font.draw(batch, "Lantacy : "+ProjectRPG.Client.network.net.getReturnTripTime()+" ms", 20, getViewport("UI").getScreenHeight() - 40);
		batch.end();
	}
	
	@Override
	public void dispose() {
		manager.dispose();
	}

	@Override
	public void start() {
		
	}
	
	public GameManager getGameManager() {
		return manager;
	}
	
}
