package com.skyhouse.projectrpg.scene;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.data.CharacterData;
import com.skyhouse.projectrpg.graphics.SpriterGlobal;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.graphics.viewports.UIViewport;
import com.skyhouse.projectrpg.input.GameplayControllerProcess;
import com.skyhouse.projectrpg.input.GameplayInputProcess;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.map.MapLoader;
import com.skyhouse.projectrpg.ui.GameSceneUI;
import com.skyhouse.projectrpg.ui.GameSceneUI.SlotPosition;

public class GameScene extends Scene {
	
	private GlyphLayout textlayout;
	private World world;
	private HashMap<String, Map> maps;
	private Map mainmap;
	private Sprite background;
	private HashMap<Integer, Character> characters;
	private Character maincharacter;
	private GameSceneUI ui;
	
	public GameScene(SpriteBatch batch) {
		super(batch);
		addViewport("Gameplay", new GameplayViewport(16f));
		addViewport("UI", new UIViewport());
		textlayout = new GlyphLayout();
		maps = new HashMap<String, Map>();
		characters = new HashMap<Integer, Character>();
		world = new World(new Vector2(0, -10f), true);
		ui = new GameSceneUI(getViewport("UI"), batch, font);
	}
	
	public void loadMap(String map, boolean waitforload) {
		String pathtomap = "mapdata/" + map + ".map";
		assetmanager.load(pathtomap, Map.class, new MapLoader.MapLoaderParameter(assetmanager));
		if(waitforload) assetmanager.finishLoading();
		maps.put(assetmanager.get(pathtomap, Map.class).getName(), assetmanager.get(pathtomap, Map.class));
		if(mainmap == null) {
			mainmap = maps.get(map);
			updateBackground();
		}
	}
	
	private void updateBackground() {
		Map.BackgroundData bgdata = mainmap.getBackgroundData();
		background = new Sprite(assetmanager.get(bgdata.path, Texture.class));
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background.setSize(bgdata.width, bgdata.height);
	}
	
	@Override
	public void resize(int width, int height) {
		updateViewport(width, height);
		ui.update();
	}

	@Override
	public void update(float deltatime) {
		world.step(1/60f, 8, 3);
		if(maincharacter == null) return;
		CharacterData maindata = new CharacterData(maincharacter);
		ProjectRPG.Client.net.sendUDP(maindata);
		for(Character character : characters.values()) {
			character.update(Gdx.graphics.getDeltaTime());
		}
		((GameplayViewport)getViewport("Gameplay")).setViewCenterToCharacter(maincharacter, 0, 2.45f);
		background.setPosition(-(background.getWidth() / 2f) + (maincharacter.getPositionX() * 0.35f), -2f + (maincharacter.getPositionY() * 0.35f));
		ui.act(deltatime);
		
		checkUseSkill(deltatime);
	}
	
	boolean processedS1 = false, processedS2 = false;
	boolean cooldownings1 = false, cooldownings2 = false;
	float s1time = 0f, s2time = 0f, cds1 = 10f, cds2 = 3f;
	private void checkUseSkill(float deltatime) {
		if(maincharacter.inputstate.s1_flag && !processedS1 && !cooldownings1) {
			ui.useSkill(SlotPosition.FIRST, cds1);
			cooldownings1 = true;
			processedS1 = true;
		} else {
			processedS1 = false;
		}
		
		if(maincharacter.inputstate.s2_flag && !processedS2 && !cooldownings2) {
			ui.useSkill(SlotPosition.SECOND, cds2);
			cooldownings2 = true;
			processedS2 = true;
		} else {
			processedS2 = false;
		}
		
		if(cooldownings1) {
			if(s1time > cds1) {
				s1time = 0f;
				cooldownings1 = false;
			}
			s1time += deltatime;
		}
		if(cooldownings2) {
			if(s2time > cds2) {
				s2time = 0f;
				cooldownings2 = false;
			}
			s2time += deltatime;
		}
	}

	@Override
	public void draw(float deltatime) {
		if(maincharacter == null) return;
		drawEntities();
		drawUI();
	}
	
	private void drawEntities() {
		useViewport("Gameplay");
		batch.begin();
			background.draw(batch);
			for(Map map : maps.values()) {
				map.draw(batch);				
			}
			SpriterGlobal.updateAndDraw(maincharacter.getSpriterPlayer());
		batch.end();
	}
	
	private void drawUI() {
		useViewport("UI");
		ui.draw();
		
		batch.begin();
			font.draw(batch, "FPS : "+Gdx.graphics.getFramesPerSecond(), 20, getViewport("UI").getScreenHeight() - 20);
			font.draw(batch, "Lantacy : "+ProjectRPG.Client.net.getReturnTripTime()+" ms", 20, getViewport("UI").getScreenHeight() - 40);
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
		input.setInputProcessor(new GameplayInputProcess(maincharacter));
		input.setControllerProcessor(new GameplayControllerProcess(maincharacter));
		input.use();
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

}
