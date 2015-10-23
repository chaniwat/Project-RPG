package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.game.GameManager;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;

public class GameScene extends Scene {
	
	private GameManager manager;
	private ShapeRenderer renderer;
	private Sprite background;
	
	public GameScene(SpriteBatch batch) {
		super(batch);
		manager = new GameManager(assetmanager, input);
		renderer = new ShapeRenderer();
		addViewport("Gameplay", new GameplayViewport(16f));
		addViewport("UI", new UIViewport());
		background = new Sprite();
		manager.loadMap("mapdata/L01.map");
		CharacterData data = new CharacterData();
		data.id = 1;
		data.x = 6f;
		data.y = 6f;
		manager.addCharacter(new Character(manager.getB2DWorld(), new SpriterPlayer("entity/GreyGuy/player.scml", batch, renderer), data));
		manager.setControlCharacter(data.id);
	}

	@Override
	public void update(float deltatime) {
		manager.update(deltatime);

		((GameplayViewport)getViewport("Gameplay")).setViewCenterToCharacter(manager.getControCharacter(), 0, 1.5f);
		updateBackground();
	}
	
	private void updateBackground() {
		if((background.getTexture() == null || !background.getTexture().equals(manager.getCurrentMap().getBackground())) && manager.getCurrentMap() != null) {
			float baseHeight = 17f;
			Texture t = manager.getCurrentMap().getBackground();
			background.setRegion(t);
			background.setSize(baseHeight * (t.getWidth()/t.getHeight()), baseHeight);
			background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);			
		}
		
		background.setPosition(-(background.getWidth() / 2f) + (manager.getControCharacter().getData().x * 0.35f), -2f + (manager.getControCharacter().getData().y * 0.35f));
	}
	
	@Override
	public void draw(float deltatime) {
		if(manager.getAllCharacter().isEmpty() || manager.getAllMap().isEmpty()) return;
		drawEntities();
		drawUI();
	}
	
	private void drawEntities() {
		useViewport("Gameplay");
		batch.begin();
			background.draw(batch);
			for(Map m : manager.getAllMap()) {
				m.draw(batch);
			}
			for(Character c : manager.getAllCharacter()) {
				if(c.equals(manager.getControCharacter())) continue;
				c.draw();
			}
			manager.getControCharacter().draw();
		batch.end();
	}
	
	private void drawUI() {
		useViewport("UI");
		
		batch.begin();
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, getViewport("UI").getScreenHeight() - 20);
			//font.draw(batch, "Lantacy : "+ProjectRPG.Client.net.getReturnTripTime()+" ms", 20, getViewport("UI").getScreenHeight() - 40);
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
