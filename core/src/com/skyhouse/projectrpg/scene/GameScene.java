package com.skyhouse.projectrpg.scene;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.ProjectRPGGame;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.graphics.SpriterGlobal;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.input.GameplayControllerProcess;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.map.MapLoader;

public class GameScene extends SceneAdapter {

	AssetManager assetmanager;
	
	SpriteBatch batch;
	BitmapFont font;
	
	GameplayViewport gameViewport;
	UIViewport uiViewport;
	
	World world;
	HashMap<String, Map> maps;
	Sprite background;

	Character maincharacter;
	HashMap<Integer, Character> characters;
	
	public GameScene(SpriteBatch batch, AssetManager assetmanager) {
		this.batch = batch;
		this.assetmanager = assetmanager;
		
		gameViewport = new GameplayViewport(12f);
		uiViewport = new UIViewport();
		
		maps = new HashMap<String, Map>();
		characters = new HashMap<Integer, Character>();
		
		font = assetmanager.get("fonts/Roboto-Regular.ttf", BitmapFont.class);
		font.getData().markupEnabled = true;
		font.setColor(Color.BLACK);
	}
	
	public void loadMap(String map, boolean waitforload) {
		String pathtomap = "mapdata/" + map + ".map";
		assetmanager.load(pathtomap, Map.class, new MapLoader.MapParameter(assetmanager));
		if(waitforload) assetmanager.finishLoading();
		
		maps.put(assetmanager.get(pathtomap, Map.class).getName(), assetmanager.get(pathtomap, Map.class));
		
		setBackgroundFromMap(maps.get("L01"));
	}
	
	private void setBackgroundFromMap(Map map) {
		Map.BackgroundData bgdata = map.getBackgroundData();
		background = new Sprite(assetmanager.get(bgdata.path, Texture.class));
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background.setSize(bgdata.width, bgdata.height);
	}
	
	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height);
		uiViewport.update(width, height);
	}

	@Override
	public void update(float deltatime) {
		if(maincharacter == null) return;
		CharacterData maindata = new CharacterData(maincharacter);
		ProjectRPGGame.client.sendUDP(maindata);
		
		for(Character character : characters.values()) {
			character.update(Gdx.graphics.getDeltaTime());
		}
		gameViewport.setViewCenterToCharacter(maincharacter, 0, 2.4f);
		background.setPosition(-(background.getWidth() / 2f) + (maincharacter.getPositionX() * 0.35f), -2f + (maincharacter.getPositionY() * 0.35f));
	}

	@Override
	public void draw(float deltatime) {
		if(maincharacter == null) return;
		drawEntities();
		drawUI();
	}
	
	private void drawEntities() {
		gameViewport.apply();
		batch.setProjectionMatrix(gameViewport.getCamera().combined);
		
		batch.begin();
			background.draw(batch);
			for(Map map : maps.values()) {
				map.draw(batch);				
			}
			SpriterGlobal.updateAndDraw(maincharacter.getSpriterPlayer());
		batch.end();
	}
	
	private void drawUI() {
		uiViewport.apply();
		batch.setProjectionMatrix(uiViewport.getCamera().combined);
		
		batch.begin();
			font.draw(batch, "FPS : [GREEN]"+Gdx.graphics.getFramesPerSecond(), 20, uiViewport.getWorldHeight() - 20);
			font.draw(batch, String.format("Frametime : %.0f ms", Gdx.graphics.getDeltaTime()*1000), 20, uiViewport.getWorldHeight() - 40);
			font.draw(batch, String.format("Draw calls : %d | Bound calls : %d | Shader switch : %d", GLProfiler.drawCalls, GLProfiler.textureBindings, GLProfiler.shaderSwitches), 20, uiViewport.getWorldHeight() - 60);
			font.draw(batch, String.format("Player position: (%.2f, %.2f)", maincharacter.getPositionX(), maincharacter.getPositionY()), 20, uiViewport.getWorldHeight() - 80);
			font.draw(batch, String.format("Player state: %s", maincharacter.actionstate.name()), 20, uiViewport.getWorldHeight() - 100);
		batch.end();
	}
	
	public void addCharacter(int id, CharacterData data) {
		if(characters.getOrDefault(id, null) == null) characters.put(id, new Character(data));
	}
	
	public void removeCharacter(int id) {
		if(characters.getOrDefault(id, null) == null) return;
		characters.get(id).dispose();
		characters.remove(id);
	}
	
	public void updateCharacter(int id, CharacterData data) {
		characters.get(id).setPosition(data.getPositionX(), data.getPositionY());
		characters.get(id).setFilpX(data.isFlipX());
		characters.get(id).actionstate = data.actionstate;
	}
	
	public void setMainCharacter(int id, CharacterData data) {
		addCharacter(id, data);
		maincharacter = characters.get(id);
		Gdx.input.setInputProcessor(new GameplayInputProcess(maincharacter));
		if(Controllers.getControllers().size > 0) {
			Controller controller = Controllers.getControllers().get(0);
			controller.addListener(new GameplayControllerProcess(maincharacter));
		}
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

}
